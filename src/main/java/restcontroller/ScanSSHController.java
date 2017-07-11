/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import Business.*;
import Service.GetInfoService;
import Service.ReadService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ScanSSHController {

    @Autowired
    ScanPort22 scanPort22;
    @Autowired
    ScanSSH scanSSH;
    @Autowired
    ReadService readService;
    @Autowired
    GetInfoService getInfoService;

    @RequestMapping(value = {"/CheckSsh"}, method = RequestMethod.POST)
    public String ScanSsh(
            @RequestParam("url") String url,
            @RequestParam("range") String range,
            @RequestParam("userpass") String userpass,
            @RequestParam("thread") int thread,
            HttpServletRequest request, HttpSession session, ModelMap mm
    ) {
        session.setAttribute("url", url + "/UpdateCheckSsh");

        Thread scanssh = new Thread() {
            @Override
            public void run() {
                try {
                    scanSSH.setListsRange(readService.readFileTMPFromSFtpServer(range));
                    scanSSH.setListsUserPass(getInfoService.getListUserPass(readService.readFileTMPFromSFtpServer(userpass)));
                    scanSSH.setNumberOfThreads(thread);
                    scanSSH.StartSetting();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        };
        scanssh.start();

        return "ResultSSH";
    }

    @RequestMapping(value = {"/scanPort"}, method = RequestMethod.GET)
    public String scanPort(
            ModelMap mm
    ) {
        try {
            List<String> lists = getInfoService.getListFileOnSFtpServer();
            mm.addAttribute("listsFile", lists);
        } catch (Exception e) {
            e.getMessage();
        }
        return "ScanPort";
    }

    @RequestMapping(value = {"/scanPort"}, method = RequestMethod.POST)
    public String scanPort(
            @RequestParam("url") String url,
            @RequestParam("range") String range,
            @RequestParam("thread") int thread,
            HttpServletRequest request, HttpSession session, ModelMap mm
    ) {

        Thread checkssh = new Thread() {
            @Override
            public void run() {
                try {
                    scanPort22.setListsRange(readService.readFileTMPFromSFtpServer(range));
                    scanPort22.setNumberOfThreads(thread);
                    scanPort22.StartSetting();
                    int x = 9;
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        };
        checkssh.start();

        //session.setAttribute("url", url + "/UpdateCheckSsh");
        return "ResultScanPort";
    }
}
