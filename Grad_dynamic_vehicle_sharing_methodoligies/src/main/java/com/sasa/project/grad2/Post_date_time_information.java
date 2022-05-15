package com.sasa.project.grad2;


import android.annotation.SuppressLint;

import com.sasa.project.grad2.Firebase_DataObjects.Post_info_DataObjects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class Post_date_time_information {
// only get method in post class
    ///

    public static Date Convert_firebaseDate_format(Post_info_DataObjects postInfo) {
        Date date;
        DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
/// convert elformat bta3t el fire base
        try {
            date = targetFormat.parse(Integer.toString(postInfo.tripDate));
        } catch (ParseException e) {
            return null;
        }

        return date;
    }

//    DateFormat originalFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
//    DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
//    Date date = originalFormat.parse("August 21, 2012");
//    String formattedDate = targetFormat.format(date);
    public static String post_date(Post_info_DataObjects postInfo) {

        Date date = Post_date_time_information.Convert_firebaseDate_format(postInfo);
        DateFormat originalFormat = new SimpleDateFormat("EEEE, MMMM dd", Locale.US);

        return originalFormat.format(date);
    }


    public static String Leave_after_time(Post_info_DataObjects postInfo) {

        Date date = Post_date_time_information.Convert_firebaseTime_format(postInfo.leave_time);
        DateFormat formattedDate = new SimpleDateFormat("h:mm a", Locale.US);

        return formattedDate.format(date);
    }

    public static String Arrive_before_time(Post_info_DataObjects postInfo) {
        Date date = Post_date_time_information.Convert_firebaseTime_format(postInfo.arrivalTime);
        DateFormat formattedDate = new SimpleDateFormat("h:mm a", Locale.ENGLISH);

        return formattedDate.format(date);
    }

    @SuppressLint("LongLogTag")
    private static Date Convert_firebaseTime_format(int timeFormat) {
        Date dateTime;
   // convert to a readable format mn el firebase
        String time = String.format("%04d", timeFormat);
        System.out.println("Converted Int to String: timeInt=" + timeFormat + " -> timeString=" + time);
        DateFormat targetFormat = new SimpleDateFormat("HHmm", Locale.ENGLISH);

        try {
            dateTime = targetFormat.parse(time);
        } catch (ParseException e) {
            return null;
        }

        return dateTime;
    }

}
