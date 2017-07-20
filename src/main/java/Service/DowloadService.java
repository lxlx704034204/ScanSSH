/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Business.ScanSSH;
import Pojos.InfoToConnectSSH;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.BufferedWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Random;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DowloadService {

    @Autowired
    ScanSSH scanSSH;
    @Autowired
    ReadService readService;
    @Autowired
    ServletContext servletContext;
    @Autowired
    Session session;

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
        } catch (IOException e) {
            e.getMessage();
            bufferedWriter.close();
        }
        return null;
    }

    public String dowloadFileFromSFTPServer1(String filename) throws IOException {
        Random rd = new Random();
        int temp = rd.nextInt(9999);
        BufferedWriter bufferedWriter = null;
        List<String> lists = null;
        try {
            //doc file sftp
            lists = readService.readFileTMPFromSFtpServer(filename);

            //ghi file local
            FileOutputStream outputStream = new FileOutputStream("D:\\" + filename);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            for (int i = 0; i < lists.size(); i++) {

                bufferedWriter.write(lists.get(i));
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            return "D:\\" + filename;
        } catch (IOException | ClassNotFoundException e) {

            System.out.println(e.getMessage());
            bufferedWriter.close();
        }
        return null;
    }

    public String dowloadFileFromSFTPServer(String filename, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        InputStream inputStream = null;
        OutputStream outStream = null;
        int BUFFER_SIZE = 4096;
        try {
            //doc file sftp
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

            inputStream = channelSftp.get(ConstantVariable.homedir+"/range/" + filename);

            // get MIME type of the file
            String mimeType = null;
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }

            // set content attributes for the response
            response.setContentType(mimeType);
            //response.setContentLength((int) lists.size());

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"",
                    filename);
            response.setHeader(headerKey, headerValue);

            // get output stream of the response
            outStream = response.getOutputStream();

            // write bytes read from the input stream into the output stream
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outStream.close();

        } catch (JSchException | SftpException | IOException e) {
            e.getMessage();
        }
        return null;
    }

   
}
