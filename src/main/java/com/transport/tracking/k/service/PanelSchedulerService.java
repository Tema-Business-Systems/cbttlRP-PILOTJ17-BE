package com.transport.tracking.k.service;

import com.transport.tracking.response.DocsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Component
public class PanelSchedulerService {

    @Autowired
    private AsyncSchdulerService asyncSchdulerService;



    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] arg) {
        PanelSchedulerService panelService = new PanelSchedulerService();
        panelService.waitMethod();
    }

    private synchronized void waitMethod() {

        while (true) {
            try {
                // These coordinates are screen coordinates
                int xCoord = 500;
                int yCoord = 500;

                // Move the cursor
                Robot robot = new Robot();
                robot.mouseMove(xCoord, yCoord);
            } catch (AWTException e) {
            }
            try {
                this.wait(5000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }

    }

    public List<DocsVO> getDocswithRange(List<String> site, Date sdate , Date edate) {

         List<DocsVO> docsList = new ArrayList<>();

         docsList = asyncSchdulerService.getDocsWithRange(site,sdate,edate);


         return docsList;

    }

    public List<DocsVO> getDocswithSelDate2(List<String> site, Date seldate) {
        List<DocsVO> docsList = new ArrayList<>();
        docsList = asyncSchdulerService.getDocsWithSelDate2(site, seldate);
        // asyncScheduleService.getDocsWithSelDate2(site,seldate);
        return docsList;
    }





    public List<DocsVO> getDocswithRange2(List<String> site, Date sdate , Date edate) {

        List<DocsVO> docsList = new ArrayList<>();

        docsList = asyncSchdulerService.getDocsWithRange2(site,sdate,edate);


        return docsList;

    }



}


