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
import java.util.Properties;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadService {

    @Autowired
    Session session;

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

    public List<String> readFileTMPFromSFtpServer(String filename) throws IOException, ClassNotFoundException {
        List<String> lists = new ArrayList<>();
        BufferedReader reader = null;
        String read = "";
        try {

            Channel channel = null;
            ChannelSftp channelSftp = null;
            session.setTimeout(15000);
            if (!session.isConnected()) {
                session.setPassword("ftp123");
                session.connect();
            }

            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

            
            InputStream fis = channelSftp.get("/var/www/html/wsplateform/range/" + filename);

            reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

            while ((read = reader.readLine()) != null) {
                lists.add(read);
            }
            channel.disconnect();

            return lists;

        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

}
