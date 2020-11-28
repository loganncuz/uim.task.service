package com.ncuz.task.service;

import com.ncuz.encryption.service.PropertiesService;
import com.ncuz.task.service.api.ContentType;
import com.ncuz.task.service.api.PostFactory;
import com.ncuz.task.service.api.RestConfig;
import com.ncuz.task.service.services.TaskSchedulerService;
import com.ncuz.task.service.services.TaskService;
import com.ncuz.task.service.utility.PropertiesUtility;
import log4j.helper.service.Log4jService;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
@EnableAutoConfiguration
public class Application implements CommandLineRunner {
    @Autowired
    TaskSchedulerService taskSchedulerService;
    @Autowired
    PropertiesUtility propertiesUtility;
    @Autowired
    TaskService taskService;
    @Autowired
    Log4jService log4jService;
    @Autowired
    Environment environment;
    private static Logger logger;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println(System.getProperty("os.name"));
    }

    @PostConstruct
    private void post() {
        logger=log4jService.getLogger(Application.class,"file","../log_app/task.service");
        PropertiesService.initApplicationProperties();
        propertiesUtility.loadDataSourceProperties();
    }

    @Override
    public void run(String... args) throws Exception {
        taskService.begin();
        test();
    }

    private void test(){
//       JSONObject a= taskSchedulerService.setServer("10.59.67.42",false);
//       logger.debug("SET SERVER :"+a);
        JSONObject a= taskSchedulerService.getServer("10.59.67.42");
        logger.debug("GET SERVER :"+a);
        JSONArray list= taskSchedulerService.getServerAll();
        logger.debug("GET SERVER ALL :"+list);
    }
}
