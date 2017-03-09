package com.shemchavez.ass.Constructor;

/**
 * Created by shemchavez on 2/25/17.
 */

public class Reminder {
    public String reminderID;
    public String title;
    public String message;
    public String date;
    public int status;

    public Reminder(String reminderID, String title, String message, String date, int status) {
        this.reminderID = reminderID;
        this.title = title;
        this.message = message;
        this.date = date;
        this.status = status;
    }

    public String getReminderID() {
        return reminderID;
    }

    public void setReminderID(String reminderID) {
        this.reminderID = reminderID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
