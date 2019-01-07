package com.example.bookkeeper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import java.util.Calendar;

/**
 * Created by Юлия on 24.05.2017.
 */

public class SmsService extends Service {

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_SMS = "sms";
    private SharedPreferences settings;
    boolean isAllowed = false;

    int amount;
    Calendar calendar;
    DBHelper dbHelper;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
//        if (settings.contains(APP_PREFERENCES_SMS)) {
//            isAllowed = settings.getBoolean(APP_PREFERENCES_SMS, false);
//        }
//        if (isAllowed) {
//            String sms_body = intent.getExtras().getString("sms_body");
//            String sms_from = intent.getExtras().getString("sms_from");
//            calendar = Calendar.getInstance();
//
//            if (processSms(sms_body, sms_from)) { // если подходит под шаблон, то сохраняем в бд
//                dbHelper = new DBHelper(this);
//                dbHelper.saveEntry(-1, amount, 7, calendar);
//            }
//            return START_STICKY;
//        } else
            stopSelf();
        return START_NOT_STICKY;
    }

    private boolean processSms(String sms_body, String sms_from) {
//        if (sms_from.equalsIgnoreCase("VTB24")) {
//            if (sms_body.contains("snyatie") || sms_body.contains("снятие")
//                    || sms_body.contains("spisanie") || sms_body.contains("списание")
//                    || sms_body.contains("Karti+") || sms_body.contains("Карты+")) {
//
//                int index = -1;
//                if (sms_body.contains(" RUR")) {
//                    index = sms_body.indexOf(" RUR");
//                }
//                if (sms_body.contains(" RUB")) {
//                    index = sms_body.indexOf(" RUB");
//                }
//
//                if (index == -1) {
//                    return false;
//                } else {
//                    String amountString = new String();
//                    boolean flag = false;
//                    while (sms_body.charAt(index) != ' ') {
//                        if (flag) {
//                            amountString = sms_body.charAt(index) + amountString;
//                        }
//                        if (sms_body.charAt(index) == '.' || sms_body.charAt(index) == ',') {
//                            flag = true;
//                        }
//                        index--;
//                    }
//                    try {
//                        amount = Integer.parseInt(amountString);
//                    } catch (NumberFormatException e) {
//                        return false;
//                    }
//                    return true; // всё совпало
//                }
//            }
//            return false;
//        }
//         if (sms_from.equalsIgnoreCase("900")) {
//                 if (sms_body.contains("оплата") || sms_body.contains("выдача наличных")
//                         || sms_body.contains("покупка") || sms_body.contains("перевод ")
//                         || sms_body.contains("погашение") || sms_body.contains("списание")
//                         || sms_body.contains("Оплата")) {
//                     int index = -1;
//                     if (sms_body.contains(" RUR")) {index = sms_body.indexOf(" RUR");}
//                     if (sms_body.contains(" руб.")) {index = sms_body.indexOf(" руб.");}
//                     if (sms_body.contains(" RUB")) {index = sms_body.indexOf(" RUB");}
//                     if (sms_body.contains("р ")) {index = sms_body.indexOf("р ");}
//                     if (sms_body.contains("р. ")) {index = sms_body.indexOf("р. ");}
//
//                     if (index == -1) {
//                             return false;
//                         } else {
//                             String amountString = new String();
//                             boolean flag = false;
//                             while(sms_body.charAt(index) != ' ') {
//                                 if (flag) {
//                                     amountString = sms_body.charAt(index) + amountString;
//                                 }
//                                 if (sms_body.charAt(index) == '.' || sms_body.charAt(index) ==',') {flag = true;}
//                                 index--;
//                             }
//                             try {
//                                 amount = Integer.parseInt(amountString);
//                             }
//                             catch (NumberFormatException e) {
//                                 return false;
//                             }
//                             return true; // всё совпало
//                     }
//                 }
//                 return false;
//            }
//        return false;
        return false;
    }
}