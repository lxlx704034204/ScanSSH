/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

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
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;

@Scope("session")
@RestController
public class GreedingController {

    private static float tongssh = 0;
    private static float sshdacheck = 0;
    List<String> temp = new ArrayList<>();
    static Object syncObj = new Object();
    static Object syncObjF = new Object();
    static Object syncObjR = new Object();
    static Object syncObjCNTRY = new Object();

    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String greeding() {

        return "Hello ";
    }

    @RequestMapping(value = {"/UpdateCheckSsh"}, method = RequestMethod.GET)
    public String UpdateCheckSsh(
            HttpServletRequest request, HttpSession session, ModelMap mm
    ) {
        sshdacheck++;
        try {
            return tongssh + "/" + sshdacheck;
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
            sshClient.connect("210.211.99.203");
            sshClient.authPassword("admin", "123456");
            Session session = sshClient.startSession();
            session.allocateDefaultPTY();
            output = sshClient.isConnected() + "";

            //String rootPath = servletContext.getRealPath("");
            //File dir = new File(rootPath + File.separator + "resources");
            File dir = new File("/app/target");

            getListInfo(dir.getPath() + "\\ssh-19-4-28-4-2017.txt");
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
