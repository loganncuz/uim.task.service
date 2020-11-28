package com.ncuz.task.service.services;

import com.ncuz.task.service.api.ContentType;
import com.ncuz.task.service.api.GetFactory;
import com.ncuz.task.service.api.PostFactory;
import com.ncuz.task.service.api.RestConfig;
import com.ncuz.task.service.utility.PropertiesUtility;
import log4j.helper.service.Log4jService;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;

@Service
public class TaskSchedulerService {
    @Autowired
    Environment environment;
    @Autowired
    PropertiesUtility propertiesUtility;

    @Autowired
    Log4jService log4jService;
    private static Logger logger;
    @PostConstruct
    private void post() {
        logger=log4jService.getLogger(TaskSchedulerService.class,"file","../log_app/task.service");
//        logger.debug("PropertiesUtility POST");
    }

    @PreDestroy
    private void destroy()   {

    }

    public JSONArray getServerAll(){
        JSONArray result=new JSONArray();
        RestConfig config=new RestConfig(ContentType.JSON,environment.getProperty("uim.task.scheduler.server"));
        config.setMethod(environment.getProperty("client.method.get")+"-all");
        GetFactory gs=new GetFactory(config);
        gs.accept();
        //logger.debug("GET SERVER :"+gs.getConfig().getOutput());
        //logger.debug("GET SERVER ARRAY :"+gs.getConfig().getOutputArray().size());
        if (gs.getConfig().getOutputArray().size()>0){
            result= gs.getConfig().getOutputArray();
        }
        logger.debug("GET SERVER ALL :"+result);
        config=null;
        config=null;
        return result;
    }

    public JSONObject getServer(String ip){
        JSONObject result=new JSONObject();
        RestConfig config=new RestConfig(ContentType.JSON,environment.getProperty("uim.task.scheduler.server"));
//        config.setExtendPath("?ipaddress="+ip);
        MultivaluedMap<String, String> params=new MultivaluedHashMap<>();
        params.add("ipaddress",ip);
        config.setParams(params);
        ;
//        config.setParams();
        config.setMethod(environment.getProperty("client.method.get"));

//        logger.debug("getGroup URL :"+config.getServerName()+" | "+config.getMethodName()+" | "+config.getExtendPath());
        GetFactory gs=new GetFactory(config);
        gs.accept();
//        logger.debug("GET SERVER :"+gs.getConfig().getOutput());
        if (gs.getConfig().getOutput().size()>0){
            result= gs.getConfig().getOutput();
        }
        config=null;
        return result;

    }

    public JSONObject setServer(String ip,boolean isActive){
//        logger.debug("TEST REST CLIENT :"+environment.getProperty("uim.task.scheduler.server"));
//        logger.debug("TEST REST CLIENT :"+environment.getProperty("client.method.get"));
//        logger.debug("TEST REST CLIENT :"+environment.getProperty("client.method.set"));
        RestConfig config=new RestConfig(ContentType.JSON,environment.getProperty("uim.task.scheduler.server"));

        config.setInput("ip-address",ip);
        config.setInput("isActive",isActive);
        config.setMethod(environment.getProperty("client.method.set"));
        PostFactory ps=new PostFactory(config);
        JSONObject result=new JSONObject();
//        logger.debug("");
//        logger.debug("****************** loginAuth ***********************");
        ps.accept();

        if (ps.getConfig().getOutput().size()>0){
            if(ps.getStatus()==200){
                //RestApiUtil.setToken(ps.getConfig().getOutput().get("ticket").toString());
                result=ps.getConfig().getOutput();
//                logger.debug("User Module 200 : "+ps.getConfig().getOutput());
            }
            else{
                //RestApiUtil.setToken(null);
//                logger.debug("User Module <>200 : "+ps.getConfig().getOutput());
            }

        }else{
            //RestApiUtil.setToken(null);
//            logger.debug("User Module No Output : "+ps.getConfig().getOutput());
        }
        ps=null;
        config=null;
        return result;
    }
}
