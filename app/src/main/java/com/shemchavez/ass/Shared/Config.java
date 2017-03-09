package com.shemchavez.ass.Shared;

/**
 * Created by dna on 02/02/2017.
 */

public class Config {

    /**
     * URL
     */
    public static String url        = "http://192.168.8.102:8080/ass_web/mobile_queries/requests.php";
    public static String plainURL   = "http://192.168.8.102:8080/ass_web/mobile_queries/";


    /**
     * Sharedpreference
     */

    // Driver
    public static String sharedPrefDriver       = "driver";
        public static String tempImage          = "tempImage";
        public static String tempID             = "tempID";
        public static String tempFullName       = "tempFullName";

        public static Boolean session           = false;
        public static String id                 = "id";
        public static String password           = "password";
        public static String image              = "image";

    // Taxi
    public static String sharedPrefTaxi         = "taxi";
        public static String taxiID             = "taxiID";

    // Rental
    public static String sharedPrefRental       = "rental";
        public static String rentalID           = "rentalID";
        public static Boolean rentalStatus      = false;

    /**
     * Fragment name
     */
    public static final String FragmentDriverInformation          = "FragmentDriverInformation";
    public static final String FragmentEditDriverInformation      = "FragmentEditDriverInformation";
    public static final String FragmentRentedTaxiInformation      = "FragmentRentedTaxiInformation";
    public static final String FragmentRentalInformation          = "FragmentRentalInformation";
    public static final String FragmentReminder                   = "FragmentReminder";
    public static final String FragmentReminderInformation        = "FragmentReminderInformation";
    public static final String FragmentHistory                    = "FragmentHistory";
    public static final String FragmentNoRental                   = "FragmentNoRental";
}
