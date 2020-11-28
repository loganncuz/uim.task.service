package com.ncuz.task.service.services;

import com.ncuz.task.service.utility.PropertiesUtility;
import log4j.helper.service.Log4jService;
import org.apache.log4j.Logger;
import org.codehaus.plexus.util.cli.*;
import org.codehaus.plexus.compiler.CompilerMessage;
import org.codehaus.plexus.compiler.CompilerMessage.Kind;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    PropertiesUtility propertiesUtility;
    @Autowired
    TaskSchedulerService taskSchedulerService;

    @Autowired
    Log4jService log4jService;
    private static Logger logger;
    private static Logger loggerMessage;
    List<CompilerMessage> messages;
    List<CompilerMessage> messagesError;
    Writer stringWriter ;
    Writer stringWriterError;

    @PostConstruct
    private void post() {
        logger=log4jService.getLogger(TaskService.class,"file","../log_app/task.service");
//        logger.debug("PropertiesUtility POST");
    }

    @PreDestroy
    private void destroy()   {

    }

    public static List<CompilerMessage> parseCompilerOutput(BufferedReader bufferedReader )
            throws IOException
    {
        List<CompilerMessage> messages = new ArrayList<CompilerMessage>();

        String line = bufferedReader.readLine();

        while ( line != null )
        {
            messages.add( new CompilerMessage( line, Kind.NOTE ) );

            line = bufferedReader.readLine();
        }

        return messages;
    }

    public String[] getActionList(){
//        logger.debug("POST :" + propertiesUtility.getDataSourceProperties().getProperty("system.directory"));
        String[] actions= propertiesUtility.getDataSourceProperties().getProperty("task.actions").split(",");
        logger.info("ACTIONS LIST :"+Arrays.toString(actions) );
        logger.info("#################################################################\n");

        return actions;
    }

    private void parsingMessage(){
        try {
            messages = parseCompilerOutput( new BufferedReader( new StringReader( stringWriter.toString() ) ) );
            messagesError = parseCompilerOutput( new BufferedReader( new StringReader( stringWriterError.toString() ) ) );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void runProcess(String batfile, String directory)   {

        Commandline commandLine = new Commandline();

          stringWriter = new StringWriter();
          stringWriterError = new StringWriter();
        File executable = new File(directory + "/" +batfile);
        commandLine.setExecutable(executable.getAbsolutePath());
//        CommandLineUtils.StringStreamConsumer systemOut = new CommandLineUtils.StringStreamConsumer();
//        CommandLineUtils.StringStreamConsumer systemErr = new CommandLineUtils.StringStreamConsumer();

//        WriterStreamConsumer systemOut = new WriterStreamConsumer(
//                new OutputStreamWriter(System.out));
//
//        WriterStreamConsumer systemErr = new WriterStreamConsumer(
//                new OutputStreamWriter(System.out));
        StreamConsumer systemOut = new WriterStreamConsumer( stringWriter );

        StreamConsumer systemErr = new WriterStreamConsumer( stringWriterError );

        int returnCode = 0;
        try {
            returnCode = CommandLineUtils.executeCommandLine(commandLine, systemOut, systemErr);
            parsingMessage();
            loggerMessage.info("#############################################################");
            logMessage(messages,false);
            logMessage(messagesError,true);
            loggerMessage.info("############################################################# \n");
        } catch (CommandLineException e) {
            e.printStackTrace();
        }
        if (returnCode != 0) {
            logger.debug("Execution Batch Failed");
        } else {
            logger.debug("Execution Batch Success");
        };
    }

    private void logMessage(List<CompilerMessage> messages,boolean isError) {
        if(messages.size()>0){
            for(int i=0;i<messages.size();i++){
                if(isError){
                    loggerMessage.error(messages.get(i));
                }else{
                    loggerMessage.debug(messages.get(i));
                }
            }
        }
        
    }
    private void logMessage(Logger logger,List<CompilerMessage> messages,boolean isError) {
        if(messages.size()>0){
            for(int i=0;i<messages.size();i++){
                if(isError){
                    logger.error(messages.get(i));
                }else{
                    logger.debug(messages.get(i));
                }
            }
        }
    }

    public String[] getPriorityList(){
        logger.info("BARU getPriorityList");
//        logger.debug("POST :" + propertiesUtility.getDataSourceProperties().getProperty("system.directory"));
//        String[] ipList= propertiesUtility.getDataSourceProperties().getProperty("task.switch.priority").split(",");
//        logger.info("IP LIST :"+Arrays.toString(ipList) );
        JSONArray list= taskSchedulerService.getServerAll();
        String[] ipArrayList= new String[list.size()];
        for(int i=0;i<list.size();i++){
            JSONObject data= (JSONObject) list.get(i);
            String isactive="";
            logger.debug("DATA :"+data.get("ip-address").toString().trim()+" | "+data.get("isActive"));
            if(Boolean.parseBoolean(data.get("isActive").toString())){
                logger.debug("ON :"+Boolean.parseBoolean(data.get("isActive").toString()));
                isactive=":on";
            }else{
                logger.debug("OFF :"+Boolean.parseBoolean(data.get("isActive").toString()));
                isactive=":off";
            }
            ipArrayList[i]=data.get("ip-address").toString().trim()+isactive;
        }
        logger.info("IP ARRAY :"+Arrays.toString(ipArrayList) );

        logger.info("#################################################################\n");

        return ipArrayList;
    }

    public boolean getAutoSwitchServer(String ip){
        JSONObject data=taskSchedulerService.getServer(ip);
        logger.debug("getAutoSwitchServer :"+ip+" | "+data.get("isActive"));
        return Boolean.parseBoolean(data.get("isActive").toString()) ;
    }

    public boolean serverFailOverValidation()
    {
        String ipAddress="10.59.99.26";
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        boolean result=false;
        String localAddress=inetAddress.getHostAddress();
//        localAddress="10.59.67.42";
        String localHost=inetAddress.getHostName();
        System.out.println("IP Address:- " +localAddress);
        System.out.println("Host Name:- " + localHost);

        if(Boolean.valueOf(propertiesUtility.getDataSourceProperties().getProperty("task.switch.active"))==true){
            logger.info("SWITCH ON :"+propertiesUtility.getDataSourceProperties().getProperty("task.switch.active"));

            for (String a :  this.getPriorityList()) {

                String[] action=a.split(":");
                if(action[1].toUpperCase().equals("ON")){
                    logger.debug("IP PRIORITY :" + action[0]+" | Active : "+action[1].toUpperCase());
                    if(action[0].equals(localAddress)){
                        logger.debug("IP LOCAL :"+action[0]);
                        if(getAutoSwitchServer(action[0])){
                            logger.debug("IP  :"+action[0]+" ON");
                        }else{
                            logger.debug("IP  :"+action[0]+" OFF");
                            result=true;
                        };
                        break;
                    }else{
                        InetAddress ping = null;
                        try {
                            ping = InetAddress.getByName(action[0]);
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }

                        logger.debug("Sending Ping Request to " + action[0]);
                        try {
                            if (ping.isReachable(5000)){
                                logger.info("Host is reachable");
                                if(getAutoSwitchServer(action[0])){
                                    logger.debug("IP  :"+action[0]+" ON");
                                    result=true;
                                }else{
                                    logger.debug("IP  :"+action[0]+" OFF");

                                };
//                                result=true;
                                break;
                            }
                            else{
                                logger.info("Sorry ! We can't reach to this host");
//                                result=true;
//                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    if(action[0].equals(localAddress)){
                        logger.debug("IP LOCAL :"+action[0]);
                        if(getAutoSwitchServer(action[0])){
                            logger.debug("IP  :"+action[0]+" ON");
                        }else{
                            logger.debug("IP  :"+action[0]+" OFF");
                            result=true;
                        };
                        break;
                    }
                }
            }
        }else{
            logger.info("SWITCH OFF :"+propertiesUtility.getDataSourceProperties().getProperty("task.switch.active"));
        }
//        result=true;
        return result;

    }

    public void begin(){

         if(serverFailOverValidation()==false){
             logger.info("#################################################################");
             logger.info("BATCH DIRECTORY :"+propertiesUtility.getDataSourceProperties().getProperty("system.directory"));

             for (String a :  this.getActionList()){
                 logger.debug("Batch Action Start :"+a);
                 String[] action=a.split(":");
                 if(action[1].toUpperCase().equals("ON")){
                     loggerMessage=log4jService.getLogger(System.class,"file","../log_app/"+action[0]);
                     logger.debug(action[0]);
                     try {
                         runProcess(action[0],propertiesUtility.getDataSourceProperties().getProperty("system.directory"));
                         logMessage(logger,messages,false);
                         logMessage(logger,messagesError,true);
                     } finally{
                         logger.debug("Batch Action Finish");
                     }
                 }

                 logger.debug("Batch Action Next \n");
             }
             logger.info("#################################################################\n");
         }else{
             logger.info("#################################################################");
             logger.info("DO NOTHING");
             logger.info("#################################################################\n");
         }


    }
}
