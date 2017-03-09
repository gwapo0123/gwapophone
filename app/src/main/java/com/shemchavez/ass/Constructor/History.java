package com.shemchavez.ass.Constructor;

/**
 * Created by shemchavez on 1/29/2017.
 */

public class History {

    public String id;
    public String taxiNumber;
    public String rentalDateFrom;
    public String rentalDateTo;
    public String totalPayment;

    public History(String id, String taxiNumber, String rentalDateFrom, String rentalDateTo, String totalPayment) {
        this.id = id;
        this.taxiNumber = taxiNumber;
        this.rentalDateFrom = rentalDateFrom;
        this.rentalDateTo = rentalDateTo;
        this.totalPayment = totalPayment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaxiNumber() {
        return taxiNumber;
    }

    public void setTaxiNumber(String taxiNumber) {
        this.taxiNumber = taxiNumber;
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

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }
}
