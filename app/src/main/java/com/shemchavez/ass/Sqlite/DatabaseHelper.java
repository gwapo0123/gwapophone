package com.shemchavez.ass.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shemchavez.ass.Constructor.Driver;
import com.shemchavez.ass.Constructor.History;
import com.shemchavez.ass.Constructor.Reminder;
import com.shemchavez.ass.Constructor.Rental;
import com.shemchavez.ass.Constructor.Taxi;

/**
 * Created by shemchavez on 2/25/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ASS.db";

    /**
     * - DRIVER
     */
    private static final String TABLE_DRIVER = "Driver";
    private static final String TABLE_DRIVER_ID = "id"; // [0]
    private static final String TABLE_DRIVER_EMAIL_ADDRESS = "email_address"; // [1]
    private static final String TABLE_DRIVER_PASSWORD = "password"; // [2]
    private static final String TABLE_DRIVER_IMAGE = "image"; // [3]
    private static final String TABLE_DRIVER_FNAME = "fname"; // [4]
    private static final String TABLE_DRIVER_MNAME = "mname"; // [5]
    private static final String TABLE_DRIVER_LNAME = "lname"; // [6]
    private static final String TABLE_DRIVER_COMPLETE_ADDRESS = "complete_address"; // [7]
    private static final String TABLE_DRIVER_CONTACT_NUMBER = "contact_no"; // [8]
    private static final String TABLE_DRIVER_EMERGENCY_NUMBER = "emergency_no"; // [9]
    private static final String TABLE_DRIVER_LICENSE_NUMBER= "license_no"; // [10]
    private static final String TABLE_DRIVER_LAT = "lat"; // [11]
    private static final String TABLE_DRIVER_LNG = "lng"; // [12]
    private static final String TABLE_DRIVER_CHANGE_PASSWORD_STATUS = "change_password_status"; // [13]

    private static final String CREATE_TABLE_DRIVER = "CREATE TABLE " + TABLE_DRIVER + "(" +
                                                        TABLE_DRIVER_ID + " TEXT PRIMARY KEY, " +
                                                        TABLE_DRIVER_EMAIL_ADDRESS + " TEXT, " +
                                                        TABLE_DRIVER_PASSWORD + " TEXT, " +
                                                        TABLE_DRIVER_IMAGE + " TEXT, " +
                                                        TABLE_DRIVER_FNAME + " TEXT, " +
                                                        TABLE_DRIVER_MNAME + " TEXT, " +
                                                        TABLE_DRIVER_LNAME + " TEXT, " +
                                                        TABLE_DRIVER_COMPLETE_ADDRESS + " TEXT, " +
                                                        TABLE_DRIVER_CONTACT_NUMBER + " TEXT, " +
                                                        TABLE_DRIVER_EMERGENCY_NUMBER + " TEXT, " +
                                                        TABLE_DRIVER_LICENSE_NUMBER + " TEXT, " +
                                                        TABLE_DRIVER_LAT + " TEXT, " +
                                                        TABLE_DRIVER_LNG + " TEXT, " +
                                                        TABLE_DRIVER_CHANGE_PASSWORD_STATUS + " TEXT)";


    /**
     * - TAXI
     */
    private static final String TABLE_TAXI = "Taxi";
    private static final String TABLE_TAXI_ID = "id"; // [0]
    private static final String TABLE_TAXI_IMAGE = "image"; // [1]
    private static final String TABLE_TAXI_PLATE_NO = "plate_no"; // [2]
    private static final String TABLE_TAXI_BRAND = "brand"; // [3]
    private static final String TABLE_TAXI_BODY_NUMBER = "body_no"; // [4]
    private static final String TABLE_TAXI_LAST_CHANGE_OIL_DATE = "last_change_oil_date"; // [5]
    private static final String TABLE_TAXI_AVAILABILITY_STATUS = "availability_status"; // [6]

    private static final String CREATE_TABLE_TAXI = "CREATE TABLE " + TABLE_TAXI + "(" +
                                                    TABLE_TAXI_ID + " TEXT PRIMARY KEY, " +
                                                    TABLE_TAXI_IMAGE + " TEXT, " +
                                                    TABLE_TAXI_PLATE_NO + " TEXT, " +
                                                    TABLE_TAXI_BRAND + " TEXT, " +
                                                    TABLE_TAXI_BODY_NUMBER + " TEXT, " +
                                                    TABLE_TAXI_LAST_CHANGE_OIL_DATE + " TEXT, " +
                                                    TABLE_TAXI_AVAILABILITY_STATUS + " TEXT)";

    /**
     * - RENTAL
     */

    private static final String TABLE_RENTAL = "Rental";
    private static final String TABLE_RENTAL_ID = "id"; // [0]
    private static final String TABLE_RENTAL_DRIVER_ID = "driver_id"; // [1]
    private static final String TABLE_RENTAL_TAXI_ID = "taxi_id"; // [2]
    private static final String TABLE_RENTAL_DATE_FROM = "rental_date_from"; // [3]
    private static final String TABLE_RENTAL_DATE_TO = "rental_date_to"; // [4]
    private static final String TABLE_RENTAL_TOTAL_PAYMENT = "total_payment"; // [5]

    private static final String CREATE_TABLE_RENTAL = "CREATE TABLE " + TABLE_RENTAL + "(" +
            TABLE_RENTAL_ID + " TEXT PRIMARY KEY, " +
            TABLE_RENTAL_DRIVER_ID + " TEXT, " +
            TABLE_RENTAL_TAXI_ID + " TEXT, " +
            TABLE_RENTAL_DATE_FROM + " TEXT, " +
            TABLE_RENTAL_DATE_TO + " TEXT, " +
            TABLE_RENTAL_TOTAL_PAYMENT + " TEXT)";

    /**
     * - REMINDER
     */
    private static final String TABLE_REMINDER = "Reminder";
    private static final String TABLE_REMINDER_ID = "id";
    private static final String TABLE_REMINDER_TITLE = "title";
    private static final String TABLE_REMINDER_MESSAGE = "message";
    private static final String TABLE_REMINDER_DATE = "date";
    private static final String TABLE_REMINDER_STATUS = "status";

    private static final String CREATE_TABLE_REMINDER = "CREATE TABLE " + TABLE_REMINDER + "(" +
                                                        TABLE_REMINDER_ID + " TEXT PRIMARY KEY, " +
                                                        TABLE_REMINDER_TITLE + " TEXT, " +
                                                        TABLE_REMINDER_MESSAGE + " TEXT, " +
                                                        TABLE_REMINDER_DATE + " TEXT, " +
                                                        TABLE_REMINDER_STATUS + " TEXT)";

    private static final String TABLE_HISTORY = "History";
    private static final String TABLE_HISTORY_ID = "id"; // [0]
    private static final String TABLE_HISTORY_TAXI_ID = "taxi_id"; // [1]
    private static final String TABLE_HISTORY_RENTAL_DATE_FROM = "rental_date_from"; // [2]
    private static final String TABLE_HISTORY_RENTAL_DATE_TO = "rental_date_to"; // [3]
    private static final String TABLE_HISTORY_TOTAL_PAYMENT= "total_payment"; // [4]

    private static final String CREATE_TABLE_HISTORY = "CREATE TABLE " + TABLE_HISTORY + "(" +
            TABLE_HISTORY_ID + " TEXT PRIMARY KEY, " +
            TABLE_HISTORY_TAXI_ID + " TEXT, " +
            TABLE_HISTORY_RENTAL_DATE_FROM + " TEXT, " +
            TABLE_HISTORY_RENTAL_DATE_TO + " TEXT, " +
            TABLE_HISTORY_TOTAL_PAYMENT + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DRIVER);
        db.execSQL(CREATE_TABLE_TAXI);
        db.execSQL(CREATE_TABLE_RENTAL);
        db.execSQL(CREATE_TABLE_REMINDER);
        db.execSQL(CREATE_TABLE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAXI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RENTAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
    }

    /**
     *
     * @param driver
     * @return
     */
    public Boolean addDriver(Driver driver){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TABLE_DRIVER_ID, driver.getId());
        cv.put(TABLE_DRIVER_EMAIL_ADDRESS, driver.getEmailAddress());
        cv.put(TABLE_DRIVER_PASSWORD, driver.getPassword());
        cv.put(TABLE_DRIVER_IMAGE, driver.getImage());
        cv.put(TABLE_DRIVER_FNAME, driver.getFirstName());
        cv.put(TABLE_DRIVER_MNAME, driver.getMiddleName());
        cv.put(TABLE_DRIVER_LNAME, driver.getLastName());
        cv.put(TABLE_DRIVER_COMPLETE_ADDRESS, driver.getCompleteAddress());
        cv.put(TABLE_DRIVER_CONTACT_NUMBER, driver.getContactNumber());
        cv.put(TABLE_DRIVER_EMERGENCY_NUMBER, driver.getEmergencyNumber());
        cv.put(TABLE_DRIVER_LICENSE_NUMBER, driver.getLicenseNumber());
        cv.put(TABLE_DRIVER_LAT, driver.getLat());
        cv.put(TABLE_DRIVER_LNG, driver.getLng());
        cv.put(TABLE_DRIVER_CHANGE_PASSWORD_STATUS, driver.getChangePasswordStatus());

        long result = db.insert(TABLE_DRIVER, null, cv);
        if(result >= 1){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Fetch driver
     * @param id - Driver id
     * @return - Driver model
     */
    public Driver fetchDriver(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_DRIVER + " WHERE " + TABLE_DRIVER_ID + " = " + "\'" +id+"\'", null);
        Driver driver = null;

        if(result.getCount() != 0){
            while (result.moveToNext()){
                driver = new Driver(
                        result.getString(0), // id
                            result.getString(1), // email address
                        result.getString(2), // password
                        result.getString(3), // image
                        result.getString(4), // fname
                        result.getString(5), // mname
                        result.getString(6), // lname
                        result.getString(7), // complete address
                        result.getString(8), // contact number
                        result.getString(9), // emergency number
                        result.getString(10), // license number
                        result.getString(11), // lat
                        result.getString(12), // lng
                        result.getString(13) // change password
                );
            }
        }
        return driver;
    }

    /**
     * Update driver
     * @param driver - Driver model
     */
    public void updateDriver(Driver driver){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
            cv.put(TABLE_DRIVER_ID, driver.getId());
            cv.put(TABLE_DRIVER_EMAIL_ADDRESS, driver.getEmailAddress());
            cv.put(TABLE_DRIVER_PASSWORD, driver.getPassword());
            cv.put(TABLE_DRIVER_IMAGE, driver.getImage());
            cv.put(TABLE_DRIVER_FNAME, driver.getFirstName());
            cv.put(TABLE_DRIVER_MNAME, driver.getMiddleName());
            cv.put(TABLE_DRIVER_LNAME, driver.getLastName());
            cv.put(TABLE_DRIVER_COMPLETE_ADDRESS, driver.getCompleteAddress());
            cv.put(TABLE_DRIVER_CONTACT_NUMBER, driver.getContactNumber());
            cv.put(TABLE_DRIVER_EMERGENCY_NUMBER, driver.getEmergencyNumber());
            cv.put(TABLE_DRIVER_LICENSE_NUMBER, driver.getLicenseNumber());
            cv.put(TABLE_DRIVER_LAT, driver.getLat());
            cv.put(TABLE_DRIVER_LNG, driver.getLng());
            cv.put(TABLE_DRIVER_CHANGE_PASSWORD_STATUS, driver.getChangePasswordStatus());
        db.update(TABLE_DRIVER, cv, TABLE_DRIVER_ID + " = ?", new String[]{driver.getId()});
    }

    public Boolean addTaxi(Taxi taxi){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
            cv.put(TABLE_TAXI_ID, taxi.getId());
            cv.put(TABLE_TAXI_IMAGE, taxi.getTaxiImage());
            cv.put(TABLE_TAXI_PLATE_NO, taxi.getPlateNumber());
            cv.put(TABLE_TAXI_BRAND, taxi.getBrand());
            cv.put(TABLE_TAXI_BODY_NUMBER, taxi.getBodyNumber());
            cv.put(TABLE_TAXI_LAST_CHANGE_OIL_DATE, taxi.getLastChangeOilDate());
            cv.put(TABLE_TAXI_AVAILABILITY_STATUS, taxi.getAvailabilityStatus());
        long result = db.insert(TABLE_TAXI, null, cv);
        if(result >= 1){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     *
     * @param id - Taxi id
     * @return - Taxi model
     */
    public Taxi fetchTaxi(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_TAXI + " WHERE " + TABLE_TAXI_ID + " = " + "\'" +id+"\'", null);
        Taxi taxi = null;
        if (result.getCount() != 0) {
            while (result.moveToNext()){
                taxi = new Taxi(
                        result.getString(0), // id
                        result.getString(1), // taxi image
                        result.getString(2), // plate number
                        result.getString(3), // brand
                        result.getString(4), // body number
                        result.getString(5), // last change oil date
                        result.getString(6) // availability status
                );
            }
        }
        return taxi;
    }

    /**
     *  Update taxi
     * @param taxi - Taxi model
     */
    public void updateTaxi(Taxi taxi){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
            cv.put(TABLE_TAXI_ID, taxi.getId());
            cv.put(TABLE_TAXI_IMAGE, taxi.getTaxiImage());
            cv.put(TABLE_TAXI_PLATE_NO, taxi.getPlateNumber());
            cv.put(TABLE_TAXI_BRAND, taxi.getBrand());
            cv.put(TABLE_TAXI_BODY_NUMBER, taxi.getBodyNumber());
            cv.put(TABLE_TAXI_LAST_CHANGE_OIL_DATE, taxi.getLastChangeOilDate());
            cv.put(TABLE_TAXI_AVAILABILITY_STATUS, taxi.getAvailabilityStatus());
        db.update(TABLE_TAXI, cv, TABLE_TAXI_ID + " = ?", new String[]{taxi.getId()});
    }

    public Boolean addRental(Rental rental){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
            cv.put(TABLE_RENTAL_ID, rental.getRentalID());
            cv.put(TABLE_RENTAL_DRIVER_ID, rental.getRentalDriverID());
            cv.put(TABLE_RENTAL_TAXI_ID, rental.getRentalTaxiID());
            cv.put(TABLE_RENTAL_DATE_FROM, rental.getRentalDateFrom());
            cv.put(TABLE_RENTAL_DATE_TO, rental.getRentalDateTo());
            cv.put(TABLE_RENTAL_TOTAL_PAYMENT, rental.getRentalTotalPayment());
        long result = db.insert(TABLE_RENTAL, null, cv);
        if(result >= 1){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Fetch rental
     * @param id - Rental id
     * @return - Rental model
     */
    public Rental fetchRental(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_RENTAL + " WHERE " + TABLE_RENTAL_ID + " = " + "\'" +id+"\'", null);
        Rental rental = null;
        if (result.getCount() != 0){
            while (result.moveToNext()){
                rental = new Rental(
                        result.getString(0), // rental id
                        result.getString(1), // driver id
                        result.getString(2), // taxi id
                        result.getString(3), // rental date from
                        result.getString(4), // rental date to
                        result.getString(5)  // rental total payment
                );
            }
        }
        return rental;
    }

    public Boolean addReminder(Reminder reminder){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
            cv.put(TABLE_REMINDER_ID, reminder.getReminderID());
            cv.put(TABLE_REMINDER_TITLE, reminder.getTitle());
            cv.put(TABLE_REMINDER_MESSAGE, reminder.getMessage());
            cv.put(TABLE_REMINDER_DATE, reminder.getDate());
            cv.put(TABLE_REMINDER_STATUS, reminder.getStatus());
        long result = db.insert(TABLE_REMINDER, null, cv);
        if(result >= 1){
            return true;
        }
        else{
            return false;
        }
    }

    public Cursor fetchReminder(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_REMINDER, null);
        return result;
    }

    public int fetchCountUnreadReminder(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_REMINDER + " WHERE " + TABLE_REMINDER_STATUS + " = 0", null);
        return result.getCount();
    }

    public void updateReminderStatus(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TABLE_REMINDER_STATUS, "1");
        db.update(TABLE_REMINDER, cv, TABLE_REMINDER_ID + " = ?", new String[]{id});
    }

    public Boolean addHistory(History history){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TABLE_HISTORY_ID, history.getId());
        cv.put(TABLE_HISTORY_TAXI_ID, history.getTaxiNumber());
        cv.put(TABLE_HISTORY_RENTAL_DATE_FROM, history.getRentalDateFrom());
        cv.put(TABLE_HISTORY_RENTAL_DATE_TO, history.getRentalDateTo());
        cv.put(TABLE_HISTORY_TOTAL_PAYMENT, history.getTotalPayment());
        long result = db.insert(TABLE_HISTORY, null, cv);
        if(result >= 1){
            return true;
        }
        else{
            return false;
        }
    }

    public Cursor fetchHistory(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_HISTORY, null);
        return result;
    }

    public void deletaAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DRIVER, null, null);
        db.delete(TABLE_TAXI, null, null);
        db.delete(TABLE_RENTAL, null, null);
        db.delete(TABLE_REMINDER, null, null);
        db.delete(TABLE_HISTORY, null, null);
    }
}


