package restcontroller;

import Business.ScanSSH;
import Pojos.RangeIp;
import Service.GetInfoService;
import Service.IPService;
import Service.ReadService;
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

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(
            @RequestParam("name") String name
    ) {
        try {
            List<String> range = new ArrayList<>();
            range.add("118.69.60.253-118.69.60.254");

            //get file range
            scanSSH.setListsRange(range);
            scanSSH.setListsUserPass(getInfoService.getListUserPass(readService.readFileFromFtpServer("ftp.lisatthu.heliohost.org", "lisatthu35@lisatthu.heliohost.org", "lisatthu35", name)));
            scanSSH.setNumberOfThreads(1);
            scanSSH.StartSetting();

            int a = 0;
        } catch (Exception e) {
            e.getMessage();
        }
        return "test ";
    }

}
