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

    @RequestMapping(value = "/ssh/{ip}/{user}/{pass}", method = RequestMethod.GET)
    public String ssh(
            @PathVariable("ip") String ip,
            @PathVariable("user") String user,
            @PathVariable("pass") String pass) {
        String output = "";

        try {
            Session session = null;
            JSch s = new JSch();

            session = s.getSession(user, ip);
            session.setPassword(pass);

            session.setTimeout(15000);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("GSSAPIAuthentication", "no");
            session.setConfig("kex", "diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256");
            session.setConfig("server_host_key", "ssh-dss,ssh-rsa,ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521");
            session.setConfig("cipher.c2s",
                    "blowfish-cbc,3des-cbc,aes128-cbc,aes192-cbc,aes256-cbc,aes128-ctr,aes192-ctr,aes256-ctr,3des-ctr,arcfour,arcfour128,arcfour256");
            session.connect();

            output = session.isConnected() + "";

            return output;
        } catch (Exception e) {
            e.getMessage();
            return e.getMessage();
        }

    }

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
