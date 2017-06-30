/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import Pojos.InfoToConnectSSH;
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

@Controller
public class WebController {

    @Autowired
    ServletContext servletContext;

    @Autowired
    UploadService uploadService;
    @Autowired
    ReadService readService;

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
            message = uploadService.uploadToFtpServer("ftp.lisatthu.heliohost.org", "lisatthu35@lisatthu.heliohost.org", "lisatthu35", file);
            mm.addAttribute("message", "upload thanh cong");
        } catch (Exception e) {
            mm.addAttribute("message", message);
            e.getMessage();
        }
        return "UploadFile";
    }

    @RequestMapping(value = "/getListFile", method = RequestMethod.GET)
    public String getListFile(ModelMap mm) {

        try {
            //List<InfoToConnectSSH> list1 = readService.getListInfoToConnectSSH(readService.readFileFromFtpServer("ftp.lisatthu.heliohost.org", "lisatthu35@lisatthu.heliohost.org", "lisatthu35", "Touch.txt"));
            List<String> lists = uploadService.getListFileOnFtpServer("ftp.lisatthu.heliohost.org", "lisatthu35@lisatthu.heliohost.org", "lisatthu35");
            mm.addAttribute("listsFile", lists);

        } catch (Exception e) {
            e.getMessage();
        }
        return "ListFile";
    }

    @RequestMapping(value = "/getListInfo", method = RequestMethod.GET)
    public String getListInfo1() {

        return "ListInfo";
    }

    @RequestMapping(value = "/getListInfo", method = RequestMethod.POST)
    public String getListInfo2(ModelMap mm,
            @RequestParam(value = "name") String name) {

        try {
            List<InfoToConnectSSH> lists = readService.getListInfoToConnectSSH(readService.readFileFromFtpServer("ftp.lisatthu.heliohost.org", "lisatthu35@lisatthu.heliohost.org", "lisatthu35", name));

            mm.addAttribute("listsInfo", lists);

        } catch (Exception e) {
            e.getMessage();
        }
        return "ListInfo";
    }

    @RequestMapping(value = {"/CheckSsh"}, method = RequestMethod.GET)
    public String UpdateCheckSsh1(
            @RequestParam("url") String url,
            HttpServletRequest request, HttpSession session, ModelMap mm
    ) {
        session.setAttribute("url", url + "/UpdateCheckSsh");
        return "UploadFile";
    }

}
