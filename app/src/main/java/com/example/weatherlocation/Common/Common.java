package com.example.weatherlocation.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String APP_ID="41cf9969de10289a770c43aa38b5180f";
    public static Location current_location=null;

    public static String convertUnixToDate(long dt) {

        Date date=new Date(dt*1000L);
        SimpleDateFormat sdf=new SimpleDateFormat("KK:mm EEE MM yyyy");
        String formated=sdf.format(date);

        return formated;
    }

    public static String convertUnixToNour(long dt) {
        Date date=new Date(dt*1000L);
        SimpleDateFormat sdf=new SimpleDateFormat("KK:mm");
        String formated=sdf.format(date);

        return formated;    }
}
