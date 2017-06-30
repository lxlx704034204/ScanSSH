/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Pojos.InfoToConnectSSH;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

}
