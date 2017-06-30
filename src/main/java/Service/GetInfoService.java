/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Pojos.InfoToConnectSSH;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.stereotype.Service;

@Service
public class GetInfoService {

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
                for (int i = 0; i < listsFile.length; i++) {
                    lists.add(listsFile[i].getName());
                }
                return lists;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.getMessage();
            client.disconnect();
        }
        return null;
    }

    public FTPFile[] getFileFtpServer(FTPClient ftpClient) throws IOException, IOException {
        try {
            FTPFile[] result = ftpClient.listFiles();
            return result;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
}
