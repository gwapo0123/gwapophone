package com.shemchavez.ass.Constructor;

/**
 * Created by shemchavez on 2/15/17.
 */

public class Rental {

    public String rentalID;
    public String rentalDriverID;
    public String rentalTaxiID;
    public String rentalDateFrom;
    public String rentalDateTo;
    public String rentalTotalPayment;

    public Rental(String rentalID, String rentalDriverID, String rentalTaxiID, String rentalDateFrom, String rentalDateTo, String rentalTotalPayment) {
        this.rentalID = rentalID;
        this.rentalDriverID = rentalDriverID;
        this.rentalTaxiID = rentalTaxiID;
        this.rentalDateFrom = rentalDateFrom;
        this.rentalDateTo = rentalDateTo;
        this.rentalTotalPayment = rentalTotalPayment;
    }

    public String getRentalID() {
        return rentalID;
    }

    public void setRentalID(String rentalID) {
        this.rentalID = rentalID;
    }

    public String getRentalDriverID() {
        return rentalDriverID;
    }

    public void setRentalDriverID(String rentalDriverID) {
        this.rentalDriverID = rentalDriverID;
    }

    public String getRentalTaxiID() {
        return rentalTaxiID;
    }

    public void setRentalTaxiID(String rentalTaxiID) {
        this.rentalTaxiID = rentalTaxiID;
    }

    public String getRentalDateFrom() {
        return rentalDateFrom;
    }

    public void setRentalDateFrom(String rentalDateFrom) {
        this.rentalDateFrom = rentalDateFrom;
    }

    public String getRentalDateTo() {
        return rentalDateTo;
    }

    public void setRentalDateTo(String rentalDateTo) {
        this.rentalDateTo = rentalDateTo;
    }

    public String getRentalTotalPayment() {
        return rentalTotalPayment;
    }

    public void setRentalTotalPayment(String rentalTotalPayment) {
        this.rentalTotalPayment = rentalTotalPayment;
    }
}
