/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import Business.*;
import Pojos.InfoToConnectSSH;
import Service.DeleteService;
import Service.DowloadService;
import Service.GetInfoService;
import Service.ReadService;
import Service.UploadService;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TaskController {

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
    @Autowired
    DowloadService dowloadService;
    @Autowired
    DeleteService deleteService;

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

    @RequestMapping(value = "/SaveSsh", method = RequestMethod.GET)
    public String SaveSsh(RedirectAttributes redirectAttrs) {

        try {

            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        uploadService.uploadFileTempToSFtpServer(scanSSH.getListsResultIps());
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            };
            thread.start();

        } catch (Exception e) {
            e.getMessage();
        }

        return "ResultSSH ";
    }

    @RequestMapping(value = "/dowloadFile", method = RequestMethod.POST)
    public String dowloadFile(
            RedirectAttributes redirectAttrs,
            @RequestParam(value = "name") String name,
            HttpServletRequest request,
            HttpServletResponse response) {
        String message = "";
        try {

            dowloadService.dowloadFileFromSFTPServer(name, request, response);
            Thread.sleep(1000);
        } catch (Exception e) {
            message = e.getMessage();
        }
        redirectAttrs.addFlashAttribute("message", message);
        return "ListFile";
    }

    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    public String deleteFile(
            RedirectAttributes redirectAttrs,
            @RequestParam(value = "name") String name) {
        String message = "";
        try {
            message = deleteService.delFileOnSFtpServer(name);
        } catch (Exception e) {
            message = e.getMessage();
        }
        redirectAttrs.addFlashAttribute("message", message);
        return "redirect:/getListFile ";
    }

  

}
