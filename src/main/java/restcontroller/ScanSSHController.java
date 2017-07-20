/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import Business.*;
import Service.GetInfoService;
import Service.ReadService;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    @Autowired
    CheckSSHVPSController checkSSHVPS;

    @RequestMapping(value = {"/CheckSsh"}, method = RequestMethod.POST)
    public String ScanSsh(
            @RequestParam("url") String url,
            @RequestParam("range") String range,
            @RequestParam("userpass") String userpass,
            @RequestParam("thread") int thread,
            HttpServletRequest request, HttpSession session, ModelMap mm
    ) {
        session.setAttribute("url", url + "/UpdateCheckSsh");

        if (!scanSSH.isFlagActive()) {
            Thread scanssh = new Thread() {
                @Override
                public void run() {
                    try {
                        scanSSH.setListsRange(readService.readFileTMPFromSFtpServer(range));
                        scanSSH.setListsUserPass(getInfoService.getListUserPass(readService.readFileTMPFromSFtpServer(userpass)));
                        scanSSH.setNumberOfThreads(thread);
                        scanSSH.StartSetting();

                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            };
            scanssh.start();
        }

        return "ResultSSH";
    }

    @RequestMapping(value = {"/CheckSsh"}, method = RequestMethod.GET)
    public String UpdateCheckSsh1(ModelMap mm) {

        try {
            Thread check = new Thread() {
                @Override
                public void run() {
                    try {

                        List<String> lists = getInfoService.getListFileOnSFtpServer();
                        mm.addAttribute("listsFile", lists);

                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            };
            check.start();

        } catch (Exception e) {
        }
        return "ScanSSH";
    }

    @RequestMapping(value = {"/AutoUpdate"}, method = RequestMethod.GET)
    public String AutoUpdate(ModelMap mm, HttpSession session,
            @RequestParam("url") String url) {
        session.setAttribute("url", url + "/UpdateCheckSsh");

        return "redirect:/ResultSSH";
    }

    @RequestMapping(value = {"/ResultSSH"}, method = RequestMethod.GET)
    public String ResultSSH(ModelMap mm) {

        try {
            Thread rs = new Thread() {
                @Override
                public void run() {
                    try {
                        float tongssh = scanSSH.getTotalIps();
                        float sshdacheck = scanSSH.getTotalIpsChecked();
                        float sshlive = scanSSH.getNumberOfIpsLive();

                        if (scanSSH.getListsResultIps() != null && scanSSH.getListsResultIps().size() > 0) {
                            mm.addAttribute("listsInfo", scanSSH.getListsResultIps());
                        }

                        mm.addAttribute("tongssh", tongssh);
                        mm.addAttribute("sshdacheck", sshdacheck);
                        mm.addAttribute("threadactive", scanSSH.getCurrentThreadActive());
                        mm.addAttribute("sshlive", sshlive);
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            };
            rs.start();
        } catch (Exception e) {
        }

        return "ResultSSH";
    }

}
