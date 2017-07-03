/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import Business.ScanSSH;
import Pojos.InfoToConnectSSH;
import Service.GetInfoService;
import Service.ReadService;
import Service.UploadService;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    @Autowired
    ServletContext servletContext;

    @Autowired
    UploadService uploadService;
    @Autowired
    ReadService readService;
    @Autowired
    GetInfoService getInfoService;
    @Autowired
    ScanSSH scanSSH;

    private static float tongssh = 0;
    private static float sshdacheck = 0;
    private static float sshlive = 0;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String greeding() {

        return "index";
    }

    @RequestMapping(value = {"/UploadFile"}, method = RequestMethod.GET)
    public String UploadFile(
            HttpServletRequest request, HttpSession session) {

        return "UploadFile";
    }

    @RequestMapping(value = {"/UploadFile"}, method = RequestMethod.POST)
    public String UploadFile(
            HttpServletRequest request, HttpSession session, ModelMap mm,
            @RequestParam("file") MultipartFile file
    ) {
        String message = "";
        try {
            //message = uploadService.uploadFileToFtpServer("ftp.lisatthu.heliohost.org", "lisatthu35@lisatthu.heliohost.org", "lisatthu35", file);
            message = uploadService.uploadFileTempToSFtpServer(file, "");
            mm.addAttribute("message", message);
        } catch (Exception e) {
            mm.addAttribute("message", message);
            e.getMessage();
        }
        return "UploadFile";
    }

    @RequestMapping(value = "/getListFile", method = RequestMethod.GET)
    public String getListFile(ModelMap mm) {
        String message = "";
        try {
            //List<String> lists = getInfoService.getListFileOnFtpServer("ftp.lisatthu.heliohost.org", "lisatthu35@lisatthu.heliohost.org", "lisatthu35");
            List<String> lists = getInfoService.getListFileOnSFtpServer();
            mm.addAttribute("listsFile", lists);

        } catch (Exception e) {
            message = e.getMessage();
        }
        mm.addAttribute("message", message);
        return "ListFile";
    }

    @RequestMapping(value = "/getListInfo", method = RequestMethod.GET)
    public String getListInfo1() {

        return "ListInfo";
    }

    @RequestMapping(value = "/getListInfo", method = RequestMethod.POST)
    public String getListInfo2(ModelMap mm,
            @RequestParam(value = "name") String name) {
        String message = "";
 
        try {
            //List<InfoToConnectSSH> lists = getInfoService.getListInfoToConnectSSH(readService.readFileFromFtpServer("ftp.lisatthu.heliohost.org", "lisatthu35@lisatthu.heliohost.org", "lisatthu35", name));
            List<String> lists = readService.readFileTMPFromSFtpServer(name);
            mm.addAttribute("listsInfo", lists);

        } catch (Exception e) {
            message = e.getMessage();
        }
        mm.addAttribute("message", message);
        return "ListInfo";
    }

    @RequestMapping(value = {"/CheckSsh"}, method = RequestMethod.GET)
    public String UpdateCheckSsh1() {

        return "ScanSSH";
    }

    @RequestMapping(value = {"/CheckSsh"}, method = RequestMethod.POST)
    public String UpdateCheckSsh2(
            @RequestParam("url") String url,
            @RequestParam("range") String range,
            @RequestParam("userpass") String userpass,
            @RequestParam("thread") int thread,
            HttpServletRequest request, HttpSession session, ModelMap mm
    ) {
        try {
            scanSSH.setListsRange(readService.readFileTMPFromSFtpServer(range));
            scanSSH.setListsUserPass(getInfoService.getListUserPass(readService.readFileTMPFromSFtpServer(userpass)));
            scanSSH.setNumberOfThreads(thread);
            scanSSH.StartSetting();

        } catch (Exception e) {
            e.getMessage();
        }

        session.setAttribute("url", url + "/UpdateCheckSsh");
        return "ResultSSH";
    }

    @RequestMapping(value = {"/ResultSSH"}, method = RequestMethod.GET)
    public String ResultSSH(ModelMap mm) {
        tongssh = scanSSH.getTotalIps();
        sshdacheck = scanSSH.getTotalIpsChecked();

        if (scanSSH.getListsResultIps() != null && scanSSH.getListsResultIps().size() > 0) {
            mm.addAttribute("listsInfo", scanSSH.getListsResultIps());
            sshlive = scanSSH.getNumberOfIpsLive();
        }
        mm.addAttribute("tongssh", tongssh);
        mm.addAttribute("sshdacheck", sshdacheck);
        mm.addAttribute("sshlive", sshlive);
        try {

        } catch (Exception e) {
            e.getMessage();

        }
        return "ResultSSH";
    }

    @RequestMapping(value = "/SaveSsh", method = RequestMethod.GET)
    public String test2(RedirectAttributes redirectAttrs) {
        String message = "";
        try {
            uploadService.uploadFileTempToSFtpServer(scanSSH.getListsResultIps());
            message = "upload thanh cong : so ssh :" + scanSSH.getListsResultIps().size();

        } catch (Exception e) {
            message = e.getMessage();
        }
        redirectAttrs.addFlashAttribute("message", message);
        return "redirect:/ResultSSH ";
    }

}
