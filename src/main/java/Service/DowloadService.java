/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Business.ScanSSH;
import Pojos.InfoToConnectSSH;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DowloadService {

    @Autowired
    ScanSSH scanSSH;

    public String dowloadFile() throws IOException {
        Random rd = new Random();
        int temp = rd.nextInt(9999);
        BufferedWriter bufferedWriter = null;
        List<InfoToConnectSSH> lists = scanSSH.getListsResultIps();
        try {
            FileOutputStream outputStream = new FileOutputStream("D:\\KetQuaSSH" + temp + ".txt");

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            for (int i = 0; i < lists.size(); i++) {
                bufferedWriter.write(lists.get(i).getHost() + "|"
                        + lists.get(i).getUsername() + "|"
                        + lists.get(i).getPassword() + "|"
                        + lists.get(i).getCountry() + "|"
                        + lists.get(i).getDescription());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            return "D:\\KetQuaSSH" + temp + ".txt";
        } catch (Exception e) {
            e.getMessage();
            bufferedWriter.close();
        }
        return null;
    }
}
