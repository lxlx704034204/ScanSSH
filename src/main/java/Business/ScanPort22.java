/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import Pojos.*;
import Service.IPService;
import Service.UploadService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScanPort22 {

    private static final int MaxThread = 10000;
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
    private List<String> ListsIP; //truyen vao tu controller
    private int TotalRange = 0;
    private String String_IpRangeFocus = "";
    private String String_IpRangeEndFocus = "";
    private long Long_IpRangeFocus = 0;
    private long Long_IpRangeEndFocus = 0;
    private int TimeOut = 6;
    private int CountIpRange = 0;
    private int IndexOfListRange = 0;
    private boolean flag = true;
    @Autowired
    IPService iPService;
    @Autowired
    UploadService uploadService;

    //khoi tao cac gia tri ban dau
    public void StartSetting() throws InterruptedException {
        String Message = "";
        ListsIP = new ArrayList<>();
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

    public void CheckStopAndUpload() throws FileNotFoundException {
//true
        if (thread.length == NumberOfThreads) {
            while (true) {
                try {
                    if (!flag) {
                        uploadService.uploadFileTxtToSFtpServer(ListsIP);
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

    }

    public void Run(int id_thread) {

        thread[id_thread] = new Thread() {
            @Override
            public void run() {
                try {
                    Check_USER_PASS_START(id_thread);
                    CurrentThreadActive++;
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
                synchronized (ObjThread) {
                    CurrentThreadActive++;
                }
                if (L_IpBeginTemp <= L_IpEndTemp) {
                    byte check = CHECK_LIVE(S_IpBeginTemp);
                    System.out.println("ip :" + S_IpBeginTemp);
                    if (check == 1) {
                        ListsIP.add(S_IpBeginTemp);
                    }

                }
                synchronized (ObjThread) {
                    CurrentThreadActive--;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public byte CHECK_LIVE(String STR_IP) throws IOException {
        Socket soket= new Socket();
        try {
            soket.connect(new InetSocketAddress(STR_IP, 22), TimeOut*1000);
            soket.close();
            NumberOfIpsLive++;
            return 1;
        } catch (Exception e) {
            e.getMessage();
            soket.close();
        }
        return 0;
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

    public int getCurrentThreadActive() {
        return CurrentThreadActive;
    }

    public Thread[] getThread() {
        return thread;
    }

    public List<String> getListsIP() {
        return ListsIP;
    }

    public void setListsIP(List<String> ListsIP) {
        this.ListsIP = ListsIP;
    }

}
