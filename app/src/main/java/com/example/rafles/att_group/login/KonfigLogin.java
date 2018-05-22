package com.example.rafles.att_group.login;

public class KonfigLogin {

    public static String URL="http://149.129.214.176/barcode/";

    public static final String URL_CEK_LOGIN =URL+"cekLogin.php";

    //Dibawah ini merupakan Kunci yang akan digunakan untuk mengirim permintaan ke Skrip PHP
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_USR = "userNama";
    public static final String KEY_EMP_PAS = "passwordK";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_USR = "userNama";
    public static final String TAG_PAS = "passwordK";
    public static final String TAG_STATUS = "status";
    //ID karyawan
    //emp itu singkatan dari Employee
    public static final String EMP_ID = "usr_id";
}
