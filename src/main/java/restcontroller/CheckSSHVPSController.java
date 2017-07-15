/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import Business.CheckSSHVPS;
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
public class CheckSSHVPSController {

    @Autowired
    ReadService readService;
    @Autowired
    GetInfoService getInfoService;
    @Autowired
    CheckSSHVPS checkSSHVPS;

    @RequestMapping(value = {"/checkSshVps"}, method = RequestMethod.POST)
    public String checkSshVps2(
            @RequestParam("url") String url,
            @RequestParam("range") String range,
            @RequestParam("thread") int thread,
            HttpServletRequest request, HttpSession session, ModelMap mm
    ) {
        session.setAttribute("url", url + "/UpdateCheckSshVps");
        Thread checkssh = new Thread() {
            @Override
            public void run() {
                try {
                    List<String> slist = readService.readFileTMPFromSFtpServer(range);
                    checkSSHVPS.setListsIP(getInfoService.getListInfoToConnectSSH(slist));
                    checkSSHVPS.setNumberOfThreads(thread);
                    checkSSHVPS.StartSetting();
                    int x = 9;
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        };
        checkssh.start();

        return "ResultScanSHHVPS";
    }

    @RequestMapping(value = {"/checkSshVps"}, method = RequestMethod.GET)
    public String checkSshVps(
            ModelMap mm
    ) {
        try {
            List<String> lists = getInfoService.getListFileOnSFtpServer();
            mm.addAttribute("listsFile", lists);
        } catch (Exception e) {
            e.getMessage();
        }
        return "ScanSSHVPS";
    }

    @RequestMapping(value = {"/ResultScanSHHVPS"}, method = RequestMethod.GET)
    public String ResultScanPort(ModelMap mm) {
        float tongip = checkSSHVPS.getTotalIps();
        float ipdacheck = checkSSHVPS.getTotalIpsChecked();
        float iplive = checkSSHVPS.getNumberOfIpsLive();

        if (checkSSHVPS.getListsResultIps() != null && checkSSHVPS.getListsResultIps().size() > 0) {
            mm.addAttribute("listsInfo", checkSSHVPS.getListsResultIps());
        }

        mm.addAttribute("tongip", tongip);
        mm.addAttribute("ipdacheck", ipdacheck);
        mm.addAttribute("threadactive", checkSSHVPS.getCurrentThreadActive());
        mm.addAttribute("iplive", iplive);

        return "ResultScanSHHVPS";
    }
}
