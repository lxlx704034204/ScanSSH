/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import Business.*;

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
    @Autowired
    CheckSSHLive checkSSHLive;
    @Autowired
    CheckSSHVPS checkSSHVPS;
    @Autowired
    ScanPort22 scanPort22;

    @RequestMapping(value = {"/UpdateCheckSsh"}, method = RequestMethod.GET)
    public String UpdateCheckSsh2(ModelMap mm) {

        float tongssh = scanSSH.getTotalIps2();
        float sshdacheck = scanSSH.getTotalIpsChecked2();
        float sshlive = scanSSH.getNumberOfIpsLive2();

        return "tong ssh : " + tongssh + " tong da check : " + sshdacheck + " tong live : " + sshlive + " so thread da tao : " + scanSSH.getCurrentThreadActive2()
                + " / tong so thread : " + scanSSH.getNumberOfThreads2();
    }

    @RequestMapping(value = {"/UpdateCheckSshVps"}, method = RequestMethod.GET)
    public String UpdateCheckSshVps(ModelMap mm) {

        float tongssh = checkSSHVPS.getTotalIps();
        float sshdacheck = checkSSHVPS.getTotalIpsChecked();
        float sshlive = checkSSHVPS.getNumberOfIpsLive();

        return "tong ssh : " + tongssh + " tong da check : " + sshdacheck + " tong live : " + sshlive + " so thread da tao : " + checkSSHVPS.getCurrentThreadActive()
                + " / tong so thread : " + checkSSHVPS.getNumberOfThreads();
    }
    
    @RequestMapping(value = {"/UpdateScanPort"}, method = RequestMethod.GET)
    public String UpdateScanPort(ModelMap mm) {

        float tongssh = scanPort22.getTotalIps();
        float sshdacheck = scanPort22.getTotalIpsChecked();
        float sshlive = scanPort22.getNumberOfIpsLive();

        return "tong ssh : " + tongssh + " tong da check : " + sshdacheck + " tong live : " + sshlive + " so thread da tao : " + scanPort22.getCurrentThreadActive()
                + " / tong so thread : " + scanPort22.getNumberOfThreads();
    }
    
    @RequestMapping(value = {"/UpdateCheckLive"}, method = RequestMethod.GET)
    public String UpdateCheckLive(ModelMap mm) {

        float tongssh = checkSSHLive.getTotalIps();
        float sshdacheck = checkSSHLive.getTotalIpsChecked();
        float sshlive = checkSSHLive.getNumberOfIpsLive();

        return "tong ssh : " + tongssh + " tong da check : " + sshdacheck + " tong live : " + sshlive + " so thread da tao : " + checkSSHLive.getCurrentThreadActive()
                + " / tong so thread : " + checkSSHLive.getNumberOfThreads();
    }

}
