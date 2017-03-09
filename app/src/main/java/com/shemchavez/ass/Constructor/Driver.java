package com.shemchavez.ass.Constructor;

/**
 * Created by shemchavez on 2/8/2017.
 */

public class Driver {

    public String id;
    public String emailAddress;
    public String password;
    public String image;
    public String firstName;
    public String middleName;
    public String lastName;
    public String completeAddress;
    public String contactNumber;
    public String emergencyNumber;
    public String licenseNumber;
    public String lat;
    public String lng;
    public String changePasswordStatus;

    public Driver(String id, String emailAddress, String password, String image, String firstName, String middleName, String lastName, String completeAddress, String contactNumber, String emergencyNumber, String licenseNumber, String lat, String lng, String changePasswordStatus) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.password = password;
        this.image = image;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.completeAddress = completeAddress;
        this.contactNumber = contactNumber;
        this.emergencyNumber = emergencyNumber;
        this.licenseNumber = licenseNumber;
        this.lat = lat;
        this.lng = lng;
        this.changePasswordStatus = changePasswordStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompleteAddress() {
        return completeAddress;
    }

    public void setCompleteAddress(String completeAddress) {
        this.completeAddress = completeAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getChangePasswordStatus() {
        return changePasswordStatus;
    }

    public void setChangePasswordStatus(String changePasswordStatus) {
        this.changePasswordStatus = changePasswordStatus;
    }
}
