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
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

    @Autowired
    Session session;

    public String uploadFileToFtpServer(String FtpServerName, String Username, String Password, MultipartFile file) throws IllegalStateException, IOException {
        FTPClient client = new FTPClient();
        FileInputStream fis = null;
        // FileOutputStream fos = null;
        String message = "";
        File upfile = multipartToFile(file);
        try {

            if (!file.isEmpty()) {

                client.connect(FtpServerName);
                client.login(Username, Password);

                //
                // Create an InputStream of the file to be uploaded
                //
                fis = new FileInputStream(upfile);

                //
                // Store file to server
                //
                client.storeFile(file.getOriginalFilename(), fis);
                client.logout();
                return file.getOriginalFilename();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    public String uploadFileLocal(MultipartFile file, String path) throws IOException {
        String message = "";
        try {
            if (!file.isEmpty()) {

                byte[] bytes = file.getBytes();

                //Creating the directory to store file
                File dir = new File(path);

                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + file.getOriginalFilename());
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                return dir.getPath() + "/" + file.getOriginalFilename();
            }
        } catch (Exception e) {
            message = e.getMessage();
        }
        return message;
    }

    public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
                + multipart.getOriginalFilename());
        multipart.transferTo(tmpFile);
        return tmpFile;
    }

    public String uploadFileTempToSFtpServer(List<InfoToConnectSSH> ListsInfo) throws FileNotFoundException {

        Random r = new Random();
        int n = r.nextInt(9999);
        //FileInputStream fis = null;
        // FileOutputStream fos = null;
        String message = "";
        String filePath = new File("").getAbsolutePath();
        filePath.concat("nb-configuration.xml");

        try {
            //config
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

            channelSftp.cd("/var/www/html/wsplateform/range");//local

            //write data to bytes
            byte[] bytes = ObjectToByte(ListsInfo);

            Path path = Paths.get("/app/nb-configuration.xml");
            OutputStream outputStream = channelSftp.put("/var/www/html/wsplateform/range/" + "resultssh" + n + ".txt");//remote
            //write byte to stream
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            for (int i = 0; i < ListsInfo.size(); i++) {
                bufferedWriter.write(ListsInfo.get(i).getHost() + "|"
                        + ListsInfo.get(i).getUsername() + "|"
                        + ListsInfo.get(i).getPassword() + "|"
                        + ListsInfo.get(i).getCountry() + "|"
                        + ListsInfo.get(i).getDescription());
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            //outputStream.write(bytes);

            Files.copy(path, outputStream);

            channel.disconnect();

            return "filename.txt";
        } catch (Exception e) {
            message = e.getMessage();
        }
        return message;
    }

    public String uploadFileTxtToSFtpServer(List<String> ListsInfo) throws FileNotFoundException {

        Random r = new Random();
        int n = r.nextInt(9999);
        //FileInputStream fis = null;
        // FileOutputStream fos = null;
        String message = "";
        String filePath = new File("").getAbsolutePath();
        filePath.concat("nb-configuration.xml");

        try {
            //config
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

            channelSftp.cd("/var/www/html/wsplateform/range");//local

            //write data to bytes
            //byte[] bytes = ObjectToByte(ListsInfo);

            Path path = Paths.get("/app/nb-configuration.xml");
            OutputStream outputStream = channelSftp.put("/var/www/html/wsplateform/range/" + "ListRangeEnable" + n + ".txt");//remote
            //write byte to stream
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            for (int i = 0; i < ListsInfo.size(); i++) {
                bufferedWriter.write(ListsInfo.get(i));
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            //outputStream.write(bytes);

            Files.copy(path, outputStream);

            channel.disconnect();

            return "filename.txt";
        } catch (Exception e) {
            message = e.getMessage();
        }
        return message;
    }

    public String uploadFileTempToSFtpServer(MultipartFile file, String path) throws FileNotFoundException {

        String message = "";

        try {
            if (!file.isEmpty()) {
                //config
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

                channelSftp.cd("/var/www/html/wsplateform/range");//local

                //write data to bytes
                byte[] bytes = file.getBytes();

                Path paths = Paths.get("/app/nb-configuration.xml");
                OutputStream outputStream = channelSftp.put("/var/www/html/wsplateform/range/" + file.getOriginalFilename());//remote
                //write byte to stream
                outputStream.write(bytes);

                Files.copy(paths, outputStream);
                outputStream.close();
                channel.disconnect();

                return message = file.getOriginalFilename();
            }

        } catch (Exception e) {
            message = e.getMessage();
        }
        return message;
    }

    public void upload() {
        /* 
        co 2 cach upload file len sftp server
        
        
        1.
        channelSftp.cd("/var/www/html/transcript_files");
        Path path = Paths.get("D:\\ListRange.txt");//local
        OutputStream outputStream = channelSftp.put("/var/www/html/transcript_files/ListRange.txt");//remote
        Files.copy(path, outputStream);
         
        2.
        FileInputStream fis = new FileInputStream("D:\\t.tmp");
        channelSftp.put(fis, "t.tmp");
         */

    }

    public byte[] ObjectToByte(List<?> ListsInfo) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos1 = new ObjectOutputStream(baos);
            oos1.writeObject(ListsInfo);
            oos1.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
}
