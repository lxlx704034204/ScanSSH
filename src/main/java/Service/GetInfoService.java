/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Pojos.InfoToConnectSSH;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetInfoService {

    @Autowired
    Session session;

    public List<InfoToConnectSSH> getListInfoToConnectSSH(List<String> ListsInfo) {

        List<InfoToConnectSSH> lists = new ArrayList<>();
        try {
            for (int i = 0; i < ListsInfo.size(); i++) {
                if (!ListsInfo.get(i).equals("") && !ListsInfo.get(i).equals(" ")) {
                    InfoToConnectSSH info = new InfoToConnectSSH();
                    String[] temp = ListsInfo.get(i).split("\\|");
                    if (temp.length >= 3) {
                        info.setHost(temp[0]);
                        info.setUsername(temp[1]);
                        info.setPassword(temp[2]);
                        if (temp.length >= 4) {
                            info.setCountry(temp[3]);
                        }

                        lists.add(info);
                    }

                }

            }
            return lists;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public List<InfoToConnectSSH> getListUserPass(List<String> ListsInfo) {

        List<InfoToConnectSSH> lists = new ArrayList<>();
        try {
            for (int i = 0; i < ListsInfo.size(); i++) {
                if (!ListsInfo.get(i).equals("") && !ListsInfo.get(i).equals(" ")) {
                    InfoToConnectSSH info = new InfoToConnectSSH();
                    String[] temp = ListsInfo.get(i).split("\\|");
                    if (temp.length >= 2) {
                        info.setUsername(temp[0]);
                        info.setPassword(temp[1]);

                        lists.add(info);
                    }

                }

            }
            return lists;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public List<String> getListFileOnFtpServer(String FtpServerName, String Username, String Password) throws IOException {
        FTPClient client = new FTPClient();
        List<String> lists = new ArrayList<>();
        try {
            client.connect(FtpServerName);
            client.login(Username, Password);
            FTPFile[] listsFile = getFileFtpServer(client);
            client.disconnect();
            if (listsFile != null) {
                for (FTPFile listsFile1 : listsFile) {
                    lists.add(listsFile1.getName());
                }
                return lists;
            } else {
                return null;
            }

        } catch (IOException e) {
            e.getMessage();
            client.disconnect();
        }
        return null;
    }

    public List<String> getListFileOnSFtpServer() {
        List<String> lists = new ArrayList<>();
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
            channelSftp.cd(ConstantVariable.homedir+"/range");
            
            Vector<ChannelSftp.LsEntry> list = channelSftp.ls("*.txt");
            for (ChannelSftp.LsEntry entry : list) {
                lists.add(entry.getFilename());
            }
            channel.disconnect();
            
            return lists;
        } catch (JSchException | SftpException e) {
            e.getMessage();
            
        }
        return null;
    }

    public FTPFile[] getFileFtpServer(FTPClient ftpClient) throws IOException, IOException {
        try {
            FTPFile[] result = ftpClient.listFiles();
            return result;
        } catch (IOException e) {
            e.getMessage();
        }
        return null;
    }
}
