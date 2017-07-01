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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

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
                File dir = new File("");

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

    public String uploadFileTempToFtpServer(String FtpServerName, String Username, String Password, List<InfoToConnectSSH> ListsInfo) throws FileNotFoundException {

        //FileInputStream fis = null;
        // FileOutputStream fos = null;
        String message = "";

        try {
            //config
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
            
            
            channelSftp.cd("/var/www/html/wsplateform/range");//local
            
           
            
            //write data to bytes
            byte[] bytes = ObjectToByte(ListsInfo);
            
            
            Path path = Paths.get("/app/result.txt");
            OutputStream outputStream = channelSftp.put("/var/www/html/wsplateform/range/"+"filename.txt");//remote
            //write byte to stream
            outputStream.write(bytes);
            
            Files.copy(path, outputStream);

           
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
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

    public byte[] ObjectToByte(List<InfoToConnectSSH> ListsInfo) {

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
