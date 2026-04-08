package com.transport.tracking.k.controller;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class ShutdownManager {

    @Autowired
    private ApplicationContext appContext;

    //@Value("${shutdown.date}")
    private Date date;

    /*
     * Invoke with `0` to indicate no error or different code to indicate
     * abnormal exit. es: shutdownManager.initiateShutdown(0);
     **/
    public void initiateShutdown(int returnCode){
        SpringApplication.exit(appContext, () -> returnCode);
    }

    //@Scheduled(cron = "0 0/01 * * * ?")
    public void cronJobSch() {
        /*Date currentDate = new Date();
        if(currentDate.after(date) || currentDate.equals(date)) {
            initiateShutdown(0);
        }*/
    }
}
