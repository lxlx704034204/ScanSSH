/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Pojos.InfoToConnectSSH;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

@Service
public class ReadService {

    public List<String> readFileFromFtpServer(String FtpServerName, String Username, String Password, String filename) throws IOException {
        FTPClient ftpClient = new FTPClient();

        BufferedReader reader = null;
        InputStream fis = null;
        String read;
        List<String> lists = new ArrayList<>();
        try {
            ftpClient.connect(FtpServerName);
            ftpClient.login(Username, Password);

            fis = ftpClient.retrieveFileStream(filename);
            reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

            while ((read = reader.readLine()) != null) {
                lists.add(read);
            }
            return lists;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<String> readFileTMPFromSFtpServer(String FtpServerName, String Username, String Password, String filename) throws IOException, ClassNotFoundException {
        List<String> lists = new ArrayList<>();
        BufferedReader reader = null;
        String read="";
        try {
            Session session = null;
            JSch s = new JSch();
            Channel channel = null;
            ChannelSftp channelSftp = null;
            session = s.getSession(Username, FtpServerName);
            session.setPassword(Password);

            session.setTimeout(15000);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("GSSAPIAuthentication", "no");
            session.setConfig("kex", "diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256");
            session.setConfig("server_host_key", "ssh-dss,ssh-rsa,ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521");
            session.setConfig("cipher.c2s",
                    "blowfish-cbc,3des-cbc,aes128-cbc,aes192-cbc,aes256-cbc,aes128-ctr,aes192-ctr,aes256-ctr,3des-ctr,arcfour,arcfour128,arcfour256");
            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

            InputStream fis = channelSftp.get("/var/www/html/wsplateform/range/"+filename);

            reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

            while ((read = reader.readLine()) != null) {
                lists.add(read);
            }

        } catch (Exception e) {
        }
        return null;
    }

}
