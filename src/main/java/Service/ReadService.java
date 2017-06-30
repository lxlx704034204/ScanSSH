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


    


}
