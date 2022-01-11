package com.rejointech.planeta.Utils;

public class Constants {
    public static final String REGISTERPREFS = "REGISTERPREFS";
    public static final int CAMERA_PIC_REQUEST = 123;
    public static final int CAMERA_PICK_PHOTO_FOR_AVATAR = 111;
    public static int SPLASH_TIMEOUT = 2000;
    public static String LOG = "###-----------LOG IS HEre BIG ONE-------------------###############";
    public static String baseurl = "https://plantifier.herokuapp.com/api/";
    public static String bearer = "Bearer ";
    public static String signupurl = baseurl + "v1/user/signup";
    public static String verifyotpurl = baseurl + "v1/user/verifyOtp";
    public static String signinurl = baseurl + "v1/user/login";
    public static String profileurl = baseurl + "v1/user/me/";


    public static String prefregistername = "prefregistername";
    public static String prefregisteremail = "prefregisteremail";
    public static String prefregisterphone = "prefregisterphone";
    public static String prefregisterrole = "prefregisterrole";
    public static String token = "prefregistertoken";
    public static String prerrfbackendotp = "prefbackendotp";
}
