package com.shemchavez.ass.Shared;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.shemchavez.ass.Activity.ContainerActivity;
import com.shemchavez.ass.Activity.LoginActivity;
import com.shemchavez.ass.Constructor.Driver;
import com.shemchavez.ass.Constructor.Rental;
import com.shemchavez.ass.Constructor.Taxi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;  

/**
 * Created by shemchavez on 1/24/2017.
 */

public class Global {

    /**
     * Set custom font
     * @param context - context
     * @param fontName - font name
     * @return - custom font style
     */
    public static Typeface setCustomFont(Context context, String fontName) {
        Typeface customFont = null;
        switch (fontName) {
            case "Bebas":
                customFont = Typeface.createFromAsset(context.getAssets(), "fonts/BebasNeue.otf");
                break;
            case "RobotoRegular":
                customFont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
                break;
        }
        return customFont;
    }

    /**
     * Hide keyboard
     * @param activity - activity
     */
    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            Window window = activity.getWindow();
            if (window != null) {
                View v = window.getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm!=null){
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
    }

    /**
     * Display complete name
     * @param firstName - first name
     * @param middleName - middle name
     * @param lastName - last name
     * @return - complete name
     */
    public static String displayFullName(String firstName, String middleName, String lastName){
        return firstName + " " + middleName.charAt(0) + ". " + lastName;
    }

    /**
     * Date range
     * @param strDateFrom - date from
     * @param strDateTo - date to
     * @return - date range
     */
    public static String getDateRange(String strDateFrom, String strDateTo){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long dateDifference = 0;
        try {
            Date dateFrom = format.parse(strDateFrom);
            Date dateTo = format.parse(strDateTo);
            // In ms
            dateDifference = dateFrom.getTime() - dateTo.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Convert ms -> day
        return String.valueOf(dateDifference / 86400000);
    }

    /**
     * Format date "mm/dd/yy"
     * @param inputtedDate - unformatted date "yyyy-MM-dd"
     * @return - formatted date
     */
    public static String formatDateFormat(String inputtedDate) {
        Date date;
        String newDateFormat = "";

        SimpleDateFormat sfOriginalDateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        SimpleDateFormat sfParseDateFormat = new SimpleDateFormat("MM/dd/yy", java.util.Locale.getDefault());
        try {
            date = sfOriginalDateFormat.parse(inputtedDate);
            newDateFormat = sfParseDateFormat.format(date);

        } catch (ParseException e) {
            Log.v("###", e.toString());
        }

        return newDateFormat;
    }

    /**
     * Format date time "MM/dd/yy h:mm a
     * @param inputtedDate - unformatted datetime "yyyy-MM-dd H:mm:ss"
     * @return formatted date time
     */
    public static String formatDateTimeFormat(String inputtedDate) {

        Date date;
        String newDateFormat = "";

        SimpleDateFormat sfOriginalDateFormat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss", java.util.Locale.getDefault());
        SimpleDateFormat sfParseDateFormat = new SimpleDateFormat("MM/dd/yy h:mm a", java.util.Locale.getDefault());

        try {
            date = sfOriginalDateFormat.parse(inputtedDate);
            newDateFormat = sfParseDateFormat.format(date);

        } catch (ParseException e) {
            Log.v("###", e.toString());
        }

        return newDateFormat;
    }

    /**
     * Base64 to bitmap
     * @param base64Str - base64 string
     * @return - bitmap
     * @throws IllegalArgumentException
     */
    public static Bitmap base64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    /**
     * Bitmap to base64
     * @param bitmap - bitmap
     * @return - base64 string
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    /**
     * Rotate bitmap
     * @param source - bitmap
     * @param angle - degrees
     * @return - rotated bitmap
     */
    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * Get bitmap from url
     * @param src - image url
     * @return
     */
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    /**
     * Check if theres internet connection
     * @param context - context
     * @return - boolean result
     */
    public static boolean IsReachable(Context context) {
        // First, check we have any sort of connectivity
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        boolean isReachable = false;
        if (netInfo != null && netInfo.isConnected()) {

            try {
                URL url = new URL(Config.url);

                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(1000);

                return true;

            } catch (IOException e) {

            }
        }
        return isReachable;
    }

    /**
     * Set session
     * @param context - context
     */
    public static void setSession(Context context) {
        Log.v("###", "setSession");
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefDriver, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(String.valueOf(Config.session), true);
        editor.commit();
    }

    /**
     * Get session
     * @param context - context
     * @return - boolean result
     */
    public static Boolean getSession(Context context) {
        Log.v("###", "getSession");
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefDriver, Context.MODE_PRIVATE);
        Boolean getSession = sharedPreferences.getBoolean(String.valueOf(Config.session), false);
        return  getSession;
    }

    /**
     * Set temporary image url
     * @param context - context
     * @param image - temporary image url
     */
    public static void setTempImage(Context context, String image) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefDriver, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Config.tempImage, image);
        editor.commit();
    }

    /**
     * Remove session
     * @param context - context
     */
    public static void removeSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefDriver, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(String.valueOf(Config.session), false);
        editor.commit();
    }

    /**
     * Set temporary id
     * @param context - context
     * @param id - temporary id
     */
    public static void setTempID(Context context, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefDriver, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Config.tempID, id);
        editor.commit();
    }

    /**
     * Get temporary id
     * @param context - context
     * @return - temporary id
     */
    public static String getTempID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefDriver, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.tempID, "");
    }

    /**
     * Get temporary image url
     * @param context - context
     * @return - image url
     */
    public static String getTempImage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefDriver, Context.MODE_PRIVATE);
        return Config.plainURL + sharedPreferences.getString(Config.tempImage, "");
    }

    /**
     * Set temporary full name
     * @param context - context
     * @param fullName - full name
     */
    public static void setTempFullName(Context context, String fullName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefDriver, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Config.tempFullName, fullName);
        editor.commit();
    }

    /**
     * Get temporary full name
     * @param context - context
     * @return - temporary full name
     */
    public static String getTempFullName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefDriver, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.tempFullName, "");
    }

    /**
     * Set temporary taxi id
     * @param context - context
     * @param id - temporary taxi id
     */
    public static void setTempTaxiID(Context context, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefTaxi, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Config.taxiID, id);
        editor.commit();
    }

    /**
     * Get temporary taxi id
     * @param context - context
     * @return - temporary taxi id
     */
    public static String getTempTaxiID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefTaxi, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.taxiID, "");
    }

    /**
     * Set temporary rental id
     * @param context - context
     * @param id - temporary rental id
     */
    public static void setTempRentalID(Context context, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefRental, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Config.rentalID, id);
        editor.commit();
    }

    /**
     * Get temporary rental id
     * @param context - context
     * @return - temporary rental id
     */
    public static String getTempRentalID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefRental, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.rentalID, "");
    }

    public static void setRentalStatus(Context context, Boolean status) {
        Log.v("###", "setRentalStatus");
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefRental, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(String.valueOf(Config.rentalStatus), status);
        editor.commit();
    }

    public static Boolean getRentalStatus(Context context) {
        Log.v("###", "getSession");
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.sharedPrefRental, Context.MODE_PRIVATE);
        Boolean getRentalStatus = sharedPreferences.getBoolean(String.valueOf(Config.rentalStatus), false);
        return getRentalStatus;
    }

}


