/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

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
public class CheckSSHLive {

    @Autowired
    Business.CheckSSHLive checkSSHLive;

    @Autowired
    ReadService readService;
    @Autowired
    GetInfoService getInfoService;

    @RequestMapping(value = {"/checkSshLive"}, method = RequestMethod.POST)
    public String checkSshLive(
            @RequestParam("url") String url,
            @RequestParam("range") String range,
            @RequestParam("thread") int thread,
            HttpServletRequest request, HttpSession session, ModelMap mm
    ) {
        //session.setAttribute("url", url + "/UpdateCheckSshVps");
        Thread checkssh = new Thread() {
            @Override
            public void run() {
                try {
                    List<String> slist = readService.readFileTMPFromSFtpServer(range);
                    checkSSHLive.setListsIP(getInfoService.getListInfoToConnectSSH(slist));
                    checkSSHLive.setNumberOfThreads(thread);
                    checkSSHLive.StartSetting();

                } catch (Exception e) {
                    e.getMessage();
                }

            }
        };
        checkssh.start();

        return "ResultSSHLive";
    }

    @RequestMapping(value = {"/checkSshLive"}, method = RequestMethod.GET)
    public String checkSshLive2(
            ModelMap mm
    ) {
        try {
            List<String> lists = getInfoService.getListFileOnSFtpServer();
            mm.addAttribute("listsFile", lists);
        } catch (Exception e) {
            e.getMessage();
        }
        return "CheckSSHLive";
    }

    @RequestMapping(value = {"/ResultCheckSshLive"}, method = RequestMethod.GET)
    public String ResultCheckSshLive(ModelMap mm) {
        float tongip = checkSSHLive.getTotalIps();
        float ipdacheck = checkSSHLive.getTotalIpsChecked();
        float iplive = checkSSHLive.getNumberOfIpsLive();

        if (checkSSHLive.getListsIP() != null && checkSSHLive.getListsIP().size() > 0) {
            mm.addAttribute("listsInfo", checkSSHLive.getListsIP());
        }

        mm.addAttribute("tongip", tongip);
        mm.addAttribute("ipdacheck", ipdacheck);
        mm.addAttribute("threadactive", checkSSHLive.getCurrentThreadActive());
        mm.addAttribute("iplive", iplive);

        return "ResultSSHLive";
    }
}
