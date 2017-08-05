/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.net.Socket;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigSpring {

    @Bean
    Properties properties() {

        return new Properties();
    }

    @Bean
    JSch sshClient() {

        return new JSch();
    }

    @Bean
    Socket Socket() {

        return new Socket();
    }

    @Bean
    Session session() throws JSchException {
        Session session = null;
        String host = ConstantVariable.ip;
        String user = ConstantVariable.username;
        String password = ConstantVariable.password;
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

        return session;
    }

}
