/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import Business.ScanSSH;


import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;
import Pojos.*;
import Service.ReadService;
import Service.UploadService;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class ServiceController {

    List<String> temp = new ArrayList<>();

    @Autowired
    ServletContext servletContext;
    @Autowired
    UploadService uploadService;
    @Autowired
    ReadService readService;
    @Autowired
    ScanSSH scanSSH;
    private static float tongssh = 0;
    private static float sshdacheck = 0;
    private static float sshlive = 0;


    @RequestMapping(value = {"/UpdateCheckSsh"}, method = RequestMethod.GET)
    public String UpdateCheckSsh2(ModelMap mm) {

        tongssh = scanSSH.getTotalIps();
        sshdacheck = scanSSH.getTotalIpsChecked();
        sshlive = scanSSH.getNumberOfIpsLive();

        try {

        } catch (Exception e) {
            e.getMessage();

        }
        return "tong ssh : " + tongssh + " tong da check : " + sshdacheck + " tong live : " + sshlive + " so thread da tao : " + scanSSH.getCurrentThreadActive()
                + " / tong so thread : " + scanSSH.getNumberOfThreads();
    }

}
