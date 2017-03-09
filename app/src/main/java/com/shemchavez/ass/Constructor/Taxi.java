package com.shemchavez.ass.Constructor;

/**
 * Created by shemchavez on 2/15/17.
 */

public class Taxi {

    public String id;
    public String taxiImage;
    public String plateNumber;
    public String brand;
    public String bodyNumber;
    public String lastChangeOilDate;
    public String availabilityStatus;

    public Taxi(String id, String taxiImage, String plateNumber, String brand, String bodyNumber, String lastChangeOilDate, String availabilityStatus) {
        this.id = id;
        this.taxiImage = taxiImage;
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.bodyNumber = bodyNumber;
        this.lastChangeOilDate = lastChangeOilDate;
        this.availabilityStatus = availabilityStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaxiImage() {
        return taxiImage;
    }

    public void setTaxiImage(String taxiImage) {
        this.taxiImage = taxiImage;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBodyNumber() {
        return bodyNumber;
    }

    public void setBodyNumber(String bodyNumber) {
        this.bodyNumber = bodyNumber;
    }

    public String getLastChangeOilDate() {
        return lastChangeOilDate;
    }

    public void setLastChangeOilDate(String lastChangeOilDate) {
        this.lastChangeOilDate = lastChangeOilDate;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}
