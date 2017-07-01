package restcontroller;

import Business.ScanSSH;
import Pojos.RangeIp;
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
            range.add("118.69.60.253-118.69.60.254");
            //get file range
            scanSSH.setListsRange(range);
            scanSSH.setListsUserPass(getInfoService.getListUserPass(readService.readFileFromFtpServer("54.173.17.38", "ftp", "ftp123", name)));
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
            uploadService.uploadFileTempToFtpServer("54.173.17.38", "ftp", "ftp123", scanSSH.getListsResultIps());
            int a = 0;
        } catch (Exception e) {
            e.getMessage();
        }
        return "test ";
    }

    @RequestMapping(value = "/testread", method = RequestMethod.GET)
    public String testread() {
        try {
            readService.readFileTMPFromSFtpServer("54.173.17.38", "ftp", "ftp123", "ListUserPass.txt");
            int a = 0;
        } catch (Exception e) {
            e.getMessage();
        }
        return "test ";
    }

}
