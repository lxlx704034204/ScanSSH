/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import Pojos.*;
import Service.IPService;
import Service.UploadService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelDirectTCPIP;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckSSHLive {

    private static final int MaxThread = 20000;

    //thong tin ve ssh se duoc cung cap cho controller
    private long TotalIps = 0;
    private long TotalIpsChecked = 0;
    private int NumberOfThreads;// truyen vao tu controller
    private int NumberOfIpsLive = 0;
    private int CurrentThreadActive = 0;
    // du lieu de ssh hoat dong
    private static Object syncObj = new Object();
    private Object ObjThread = new Object();

    private Thread[] thread;
    private Thread ThreadCheckStop;
    private List<InfoToConnectSSH> ListsResultIps = null;
    private List<InfoToConnectSSH> ListsIP; //truyen vao tu controller
    private boolean[] Bit_CheckIps;
    private int TimeOut = 30;
    private int CountIp = 0;
    private int TotalRange = 0;
    private boolean flag = true;
    private String HostCheckFresh = "checkip.dyndns.org";
    private int PortCheckFresh = 80;
    @Autowired
    IPService iPService;
    @Autowired
    UploadService uploadService;
    @Autowired
    JSch sshClient;

    //khoi tao cac gia tri ban dau
    public void StartSetting() throws InterruptedException {
        String Message = "";
        ListsResultIps = new ArrayList<>();
        //tinh tong ip
        TotalIps = ListsIP.size();
        //
        if (NumberOfThreads > MaxThread) {
            Message = "thread qua nhieu";
            return;
        }

        //tao mang thread
        if (TotalIps <= NumberOfThreads) {
            NumberOfThreads = (int) TotalIps;
        }
        thread = new Thread[NumberOfThreads];
        //tao mang bit_check
        Bit_CheckIps = new boolean[NumberOfThreads];
        //run thread
        for (int i = 0; i < NumberOfThreads; i++) {
            Run(i);
            Thread.sleep(100);
        }

        Thread manager = new Thread() {
            @Override
            public void run() {
                try {
                    manager();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        };
        manager.start();

        //tao thread check viec dung
        ThreadCheckStop = new Thread() {
            @Override
            public void run() {
                try {
                    CheckStopAndUpload();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        };
        Thread.sleep(60000);
        ThreadCheckStop.start();

    }

    public void CheckStopAndUpload() throws FileNotFoundException, InterruptedException {
//true

        while (true) {
            Thread.sleep(30000);
            try {
                if (!flag) {
                    uploadService.uploadFileTempToSFtpServer(ListsResultIps);
                    flag = true;
                    break;
                } else {
                    //false
                    flag = false;
                    for (int i = 0; i < thread.length; i++) {

                        flag = flag || thread[i].isAlive();
                    }

                }
            } catch (Exception e) {
                e.getMessage();
            }

        }

    }

    public void Run(int id_thread) {

        thread[id_thread] = new Thread() {
            @Override
            public void run() {
                try {
                    CurrentThreadActive++;
                    Check_USER_PASS_START(id_thread);

                } catch (Exception e) {
                    e.getMessage();
                }

            }
        };
        thread[id_thread].start();
    }

    public void manager() {
        try {
            while (true) {
                Thread.sleep(2000);
                // System.out.println(" CurrentThreadActive: " + CurrentThreadActive);
                if (CurrentThreadActive == 0) {
                    break;
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void Check_USER_PASS_START(int id_thread) {

        InfoToConnectSSH info = new InfoToConnectSSH();
        try {
            while (true) {
                synchronized (syncObj) {
                    //kiem tra con range trong list range khong
                    if (CountIp <= TotalIps) {
                        info = ListsIP.get(CountIp);
                        CountIp++;
                    } else {
                        CurrentThreadActive--;
                        break;
                    }

                }

                byte check = CHECK_LIVE(info.getHost(), info.getUsername(), info.getPassword(), id_thread);
                TotalIpsChecked++;
                System.out.println("ip :" + info.getHost() + " user :" + info.getUsername() + " pass : " + info.getPassword());
                if (check == 1) {

                    InfoToConnectSSH info1 = new InfoToConnectSSH();
                    info1.setHost(info.getHost());
                    info1.setUsername(info.getUsername());
                    info1.setPassword(info.getPassword());

                    ListsResultIps.add(info1);

                }

            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public byte CHECK_LIVE(String STR_IP, String User, String Pass, int id) throws JSchException {

        Session session = null;
        session = sshClient.getSession(User, STR_IP);
        session.setTimeout(TimeOut * 1000);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("GSSAPIAuthentication", "no");
        session.setConfig("kex", "diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256");
        session.setConfig("server_host_key", "ssh-dss,ssh-rsa,ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521");
        session.setConfig("cipher.c2s",
                "blowfish-cbc,3des-cbc,aes128-cbc,aes192-cbc,aes256-cbc,aes128-ctr,aes192-ctr,aes256-ctr,3des-ctr,arcfour,arcfour128,arcfour256");

        try {

            if (apply_user_pass(session, Pass, id) == 1) {

                return 1;
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return 0;
    }

    public byte apply_user_pass(Session s, String pass, int id) throws IOException {

        try {
            Bit_CheckIps[id] = false;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Check_ssh(s, id, pass);

                    } catch (JSchException ex) {
                        Logger.getLogger(ScanSSH.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            thread.start();

            int g = 0;
            while ((thread.isAlive() == true) && (g < TimeOut)) {

                Thread.sleep(1000);
                g++;
            }
            thread.stop();

            if (g >= TimeOut) {
                s.disconnect();
                return 0;
            }

            if (Bit_CheckIps[id] == false) {
                s.disconnect();
                return 0;
            }

            s.disconnect();
            return 1;
        } catch (Exception e) {
            e.getMessage();
            s.disconnect();
            return 0;
        }

    }

    public void Check_ssh(Session session, int id, String pass) throws JSchException {

        try {
            //check connect ip
            session.setPassword(pass);
            session.connect();

            //check fresh ip
            Channel channel = session.openChannel("direct-tcpip");

            ((ChannelDirectTCPIP) channel).setHost(HostCheckFresh);
            ((ChannelDirectTCPIP) channel).setPort(PortCheckFresh);

            channel.connect(10000);

            Bit_CheckIps[id] = true;
            NumberOfIpsLive++;
        } catch (Exception ex) {
            ex.getMessage();

        }

    }

    //thong tin ve ssh se duoc cung cap cho controller
    public long getTotalIps() {
        return TotalIps;
    }

    public long getTotalIpsChecked() {
        return TotalIpsChecked;
    }

    public int getNumberOfThreads() {
        return NumberOfThreads;
    }

    public int getNumberOfIpsLive() {
        return NumberOfIpsLive;
    }

    public void setNumberOfThreads(int NumberOfThreads) {
        this.NumberOfThreads = NumberOfThreads;
    }

    public List<InfoToConnectSSH> getListsResultIps() {
        return ListsResultIps;
    }

    public void setListsResultIps(List<InfoToConnectSSH> ListsResultIps) {
        this.ListsResultIps = ListsResultIps;
    }

    public int getCurrentThreadActive() {
        return CurrentThreadActive;
    }

    public List<InfoToConnectSSH> getListsIP() {
        return ListsIP;
    }

    public void setListsIP(List<InfoToConnectSSH> ListsIP) {
        this.ListsIP = ListsIP;
    }

    public int getTimeOut() {
        return TimeOut;
    }

    public void setTimeOut(int TimeOut) {
        this.TimeOut = TimeOut;
    }

    public Thread[] getThread() {
        return thread;
    }

}
