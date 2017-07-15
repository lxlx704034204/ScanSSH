/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import Business.ScanPort22;
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
public class ScanPortSSHController {

    @Autowired
    ScanPort22 scanPort22;
    @Autowired
    ReadService readService;
    @Autowired
    GetInfoService getInfoService;

    @RequestMapping(value = {"/scanPort"}, method = RequestMethod.POST)
    public String scanPort(
            @RequestParam("url") String url,
            @RequestParam("range") String range,
            @RequestParam("thread") int thread,
            HttpServletRequest request, HttpSession session, ModelMap mm
    ) {
        session.setAttribute("url", url + "/UpdateScanPort");
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

    @RequestMapping(value = {"/ResultScanPort"}, method = RequestMethod.GET)
    public String ResultScanPort(ModelMap mm) {
        float tongip = scanPort22.getTotalIps();
        float ipdacheck = scanPort22.getTotalIpsChecked();
        float iplive = scanPort22.getNumberOfIpsLive();

        if (scanPort22.getListsIP() != null && scanPort22.getListsIP().size() > 0) {
            mm.addAttribute("listsInfo", scanPort22.getListsIP());
        }

        mm.addAttribute("tongip", tongip);
        mm.addAttribute("ipdacheck", ipdacheck);
        mm.addAttribute("threadactive", scanPort22.getCurrentThreadActive());
        mm.addAttribute("iplive", iplive);

        return "ResultScanPort";
    }
}
