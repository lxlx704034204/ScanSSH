/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Alex
 */
public class GetTime {

    public static String getTimeZoneDate() {

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a");

            // From TimeZone Server
            Date date = new Date();
            java.util.TimeZone tz = java.util.TimeZone.getDefault();

            // To TimeZone Asia/Bangkok
            SimpleDateFormat sdfVn = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a");
            java.util.TimeZone tzInVn = java.util.TimeZone.getTimeZone("Asia/Bangkok");
            sdfVn.setTimeZone(tzInVn);

            String sDateInVN = sdfVn.format(date); // Convert to String first
            Date dateInAmerica = formatter.parse(sDateInVN); // Create a new Date object

            return sDateInVN;
        } catch (ParseException e) {
            e.getMessage();
        }
        return "";
    }
}
