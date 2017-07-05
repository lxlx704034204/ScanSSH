package restcontroller;

import Business.ScanSSH;
import Pojos.*;
import Service.GetInfoService;
import Service.IPService;
import Service.ReadService;
import Service.UploadService;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
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
            range.add("117.253.105.44-117.253.105.45");
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

}
