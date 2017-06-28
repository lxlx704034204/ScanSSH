/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Controller
public class WebController {

    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = {"/UploadFile"}, method = RequestMethod.GET)
    public String UploadFile(
            @RequestParam("url") String url,
            HttpServletRequest request, HttpSession session) {
        session.setAttribute("url", url + "/UpdateCheckSsh");
        return "UploadFile";
    }

    @RequestMapping(value = {"/UploadFile"}, method = RequestMethod.POST)
    public String UploadFile(
            HttpServletRequest request, HttpSession session, ModelMap mm,
            @RequestParam("file") MultipartFile file
    ) {
        String message = "";
        try {
            message = uploadFile(file);
            mm.addAttribute("message", message);
        } catch (Exception e) {
            mm.addAttribute("message", message);
            e.getMessage();
        }
        return "UploadFile";
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String message = "";
        try {
            if (!file.isEmpty()) {
                //String rootPath = servletContext.getRealPath("");
                // String[] temp = rootPath.split("target", 2);
                //File destination = new File(temp[0] + "src\\main\\resources\\" + file.getOriginalFilename()); // something like C:/Users/tom/Documents/nameBasedOnSomeId.png
                byte[] bytes = file.getBytes();

                //Creating the directory to store file
                String rootPath = servletContext.getRealPath("");
                File dir = new File(rootPath + File.separator + "resources");

                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + file.getOriginalFilename());
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                return file.getOriginalFilename();
            }
        } catch (Exception e) {
            message = e.getMessage();
        }
        return message;
    }

}
