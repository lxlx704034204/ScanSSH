/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

    public String uploadToFtpServer(String FtpServerName, String Username, String Password, MultipartFile file) throws IllegalStateException, IOException {
        FTPClient client = new FTPClient();
        FileInputStream fis = null;
        FileOutputStream fos = null;
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

    public String uploadFile(MultipartFile file, String path) throws IOException {
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
