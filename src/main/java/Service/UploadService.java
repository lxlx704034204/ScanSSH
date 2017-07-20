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

import com.jcraft.jsch.Session;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

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

        String time = GetTime.getTimeZoneDate();
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
            OutputStream outputStream = channelSftp.put("/var/www/html/wsplateform/range/" + "resultssh-" + time + ".txt");//remote
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

        String time = GetTime.getTimeZoneDate();
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
            OutputStream outputStream = channelSftp.put("/var/www/html/wsplateform/range/" + "ListRangeEnable-" + time + ".txt");//remote
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

    public String uploadFileTxtToSFtpServer(ScanSSH scanSSH) throws FileNotFoundException {

        String message = "";

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

            channelSftp.cd("/var/www/html/wsplateform/range/save-status");//local

            Path path = Paths.get("");
            //
            uploadStatus(channelSftp, scanSSH, path);
            //
            uploadUserPass(channelSftp, scanSSH, path);
            //
            uploadRange(channelSftp, scanSSH, path);
            //
            uploadResult(channelSftp, scanSSH, path);
            //
            channel.disconnect();

            return "filename.txt";
        } catch (Exception e) {
            message = e.getMessage();
        }
        return message;
    }

    public String uploadUserPass(ChannelSftp channelSftp, ScanSSH scanSSH, Path path) {
        try {

            OutputStream outputStream4 = channelSftp.put("/var/www/html/wsplateform/range/save-status/" + "save-userpass" + ".txt");//remote
            //write byte to stream
            OutputStreamWriter outputStreamWriter4 = new OutputStreamWriter(outputStream4, "UTF-8");
            BufferedWriter bufferedWriter4 = new BufferedWriter(outputStreamWriter4);

            for (int i = 0; i < scanSSH.getListsUserPass().size(); i++) {
                bufferedWriter4.write(scanSSH.getListsUserPass().get(i).getUsername() + "|" + scanSSH.getListsUserPass().get(i).getPassword());
                bufferedWriter4.newLine();
            }

            bufferedWriter4.close();

            Files.copy(path, outputStream4);

        } catch (Exception e) {
            e.getMessage();
        }
        return "loi";

    }

    public String uploadResult(ChannelSftp channelSftp, ScanSSH scanSSH, Path path) {
        try {

            OutputStream outputStream3 = channelSftp.put("/var/www/html/wsplateform/range/save-status/" + "save-result" + ".txt");//remote
            //write byte to stream
            OutputStreamWriter outputStreamWriter3 = new OutputStreamWriter(outputStream3, "UTF-8");
            BufferedWriter bufferedWriter3 = new BufferedWriter(outputStreamWriter3);

            for (int i = 0; i < scanSSH.getListsResultIps().size(); i++) {
                bufferedWriter3.write(scanSSH.getListsResultIps().get(i).getHost() + "|"
                        + scanSSH.getListsResultIps().get(i).getUsername() + "|"
                        + scanSSH.getListsResultIps().get(i).getPassword() + "|"
                        + scanSSH.getListsResultIps().get(i).getCountry() + "|"
                        + scanSSH.getListsResultIps().get(i).getDescription());
                bufferedWriter3.newLine();
            }

            bufferedWriter3.close();

            Files.copy(path, outputStream3);

        } catch (Exception e) {
            e.getMessage();
        }
        return "loi";

    }

    public String uploadRange(ChannelSftp channelSftp, ScanSSH scanSSH, Path path) {
        try {

            OutputStream outputStream2 = channelSftp.put("/var/www/html/wsplateform/range/save-status/" + "save-range" + ".txt");//remote
            //write byte to stream
            OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(outputStream2, "UTF-8");
            BufferedWriter bufferedWriter2 = new BufferedWriter(outputStreamWriter2);

            for (int i = 0; i < scanSSH.getListsRange().size(); i++) {
                bufferedWriter2.write(scanSSH.getListsRange().get(i));
                bufferedWriter2.newLine();
            }

            bufferedWriter2.close();

            Files.copy(path, outputStream2);

        } catch (Exception e) {
            e.getMessage();
        }
        return "loi";

    }

    public String uploadStatus(ChannelSftp channelSftp, ScanSSH scanSSH, Path path) {
        try {

            OutputStream outputStream1 = channelSftp.put("/var/www/html/wsplateform/range/save-status/" + "save-status" + ".txt");//remote
            //write byte to stream
            OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(outputStream1, "UTF-8");
            BufferedWriter bufferedWriter1 = new BufferedWriter(outputStreamWriter1);

            bufferedWriter1.write(scanSSH.getIndexOfListRange() + "|" + scanSSH.getLong_IpRangeFocus());

            bufferedWriter1.close();

            Files.copy(path, outputStream1);

        } catch (Exception e) {
            e.getMessage();
        }
        return "loi";

    }

}
