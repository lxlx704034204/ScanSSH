package restcontroller;

import Business.CheckSSHLive;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Socket;
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
    @Autowired
    CheckSSHLive checkSSHLive;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(
            @RequestParam("name") String name
    ) {
        try {

            List<String> range = new ArrayList<>();
            List<String> userpass = readService.readFileTMPFromSFtpServer(name);
            List<InfoToConnectSSH> info = new ArrayList<>();
            InfoToConnectSSH i = new InfoToConnectSSH();
            i.setUsername("admin");
            i.setPassword("password");
            info.add(i);
            range.add("118.69.60.253-118.69.60.255");
            range.add("118.70.181.50-118.70.181.59");
            //get file range
            scanSSH.setListsRange(range);
            scanSSH.setListsUserPass(info);
            scanSSH.setNumberOfThreads(2);
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

    @RequestMapping(value = "/testcheckssh", method = RequestMethod.GET)
    public String testcheckssh(
            @RequestParam("name") String name,
            @RequestParam("thread") int thread) {
        try {
            List<String> slist = readService.readFileTMPFromSFtpServer(name);
            checkSSHLive.setListsIP(getInfoService.getListInfoToConnectSSH(slist));
            checkSSHLive.setNumberOfThreads(thread);
            checkSSHLive.setTimeOut(40);
            checkSSHLive.StartSetting();
            int a = 0;
        } catch (Exception e) {
            e.getMessage();
        }
        return "test ";
    }

    @RequestMapping(value = "/testport", method = RequestMethod.GET)
    public String testport() throws MalformedURLException, IOException {

        Socket soket = new Socket();
        try {
            soket.connect(new InetSocketAddress("117.253.105.180", 22), 15000);
            soket.close();

        } catch (Exception e) {
            e.getMessage();
            soket.close();
        }

        return "test ";
    }

}
