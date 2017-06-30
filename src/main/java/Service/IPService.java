/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Pojos.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class IPService {

    public long ipToLong2(String ipAddress) {

        long result = 0;

        String[] ipAddressInArray = ipAddress.split("\\.");

        for (int i = 3; i >= 0; i--) {

            long ip = Long.parseLong(ipAddressInArray[3 - i]);

            // left shifting 24,16,8,0 and bitwise OR
            // 1. 192 << 24
            // 1. 168 << 16
            // 1. 1 << 8
            // 1. 2 << 0
            result |= ip << (i * 8);

        }

        return result;
    }

    public String longToIp2(long ip) {
        StringBuilder sb = new StringBuilder(15);

        for (int i = 0; i < 4; i++) {

            // 1. 2
            // 2. 1
            // 3. 168
            // 4. 192
            sb.insert(0, Long.toString(ip & 0xff));

            if (i < 3) {
                sb.insert(0, '.');
            }

            // 1. 192.168.1.2
            // 2. 192.168.1
            // 3. 192.168
            // 4. 192
            ip = ip >> 8;

        }

        return sb.toString();
    }

    public List<RangeIp> getListRange(List<String> Ranges) {
        String temp[] = null;
        List<RangeIp> lists = new ArrayList<>();
        try {
            for (int i = 0; i < Ranges.size(); i++) {
                temp = Ranges.get(i).split("\\-");
                RangeIp rangeIp = new RangeIp();
                rangeIp.setRangeBegin(temp[0]);
                rangeIp.setRangeEnd(temp[1]);
                lists.add(rangeIp);
            }
            return lists;

        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public long getTotalIps(List<RangeIp> RangeIps) {
        long TotalIps = 0;
        try {
            for (int i = 0; i < RangeIps.size(); i++) {
                long rangebegin = ipToLong2(RangeIps.get(i).getRangeBegin());
                long rangeend = ipToLong2(RangeIps.get(i).getRangeEnd());

                for (long k = rangebegin; rangebegin <= rangeend; k++) {
                    rangebegin++;
                    TotalIps ++;
                }

            }
            return TotalIps;
        } catch (Exception e) {
            e.getMessage();
        }
        return -1;
    }
}
