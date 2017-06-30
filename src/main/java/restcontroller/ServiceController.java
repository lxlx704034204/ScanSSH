/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import Business.ScanSSH;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import Pojos.*;
import Service.ReadService;
import Service.UploadService;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
public class ServiceController {

    List<String> temp = new ArrayList<>();

    private static float tongssh = 0;
    private static float sshdacheck = 0;
    private static String ip = "";
    @Autowired
    ServletContext servletContext;
    @Autowired
    UploadService uploadService;
    @Autowired
    ReadService readService;
    @Autowired
    ScanSSH scanSSH;

    @RequestMapping(value = {"/UpdateCheckSsh"}, method = RequestMethod.GET)
    public String UpdateCheckSsh2() {

        tongssh = scanSSH.getTotalIps();
        sshdacheck = scanSSH.getTotalIpsChecked();

        if (scanSSH.getListsResultIps() != null && scanSSH.getListsResultIps().size() > 0) {
            ip = scanSSH.getListsResultIps().get(0).getHost();

        }

        try {
            return sshdacheck + "/" + tongssh + "/" + ip;
        } catch (Exception e) {
            e.getMessage();
            return "fails";
        }

    }

    @RequestMapping(value = "/ssh", method = RequestMethod.GET)
    public String ssh() {
        String output = "";
        try {
            SSHClient sshClient = new SSHClient();
            sshClient.addHostKeyVerifier(new PromiscuousVerifier());
            sshClient.setTimeout(30000);
            sshClient.connect("210.211.99.207");

            sshClient.authPassword("admin", "123456");
            Session session = sshClient.startSession();
            session.allocateDefaultPTY();
            output = sshClient.isConnected() + "";

            return output;
        } catch (Exception e) {
            e.getMessage();
            return e.getMessage();
        }

    }

    public List<InfoToConnectSSH> getListInfo(String path) {

        BufferedReader br = null;
        FileReader fr = null;

        try {

            fr = new FileReader(path);
            br = new BufferedReader(fr);

            String sCurrentLine;

            br = new BufferedReader(new FileReader(path));
            int i = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                temp.add(sCurrentLine);

                i++;
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null) {
                    br.close();
                }

                if (fr != null) {
                    fr.close();
                }

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

        return null;
    }

}
