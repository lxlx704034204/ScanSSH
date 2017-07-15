/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteService {

    @Autowired
    Session session;

    public String delFileOnSFtpServer(String filename) {
        try {
            String path = ("/var/www/html/wsplateform/range/");
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
            channelSftp.cd(path);
            channelSftp.rm(path + filename);

            channel.disconnect();

            return "oke";
        } catch (JSchException | SftpException e) {
            e.getMessage();

        }
        return null;
    }

}
