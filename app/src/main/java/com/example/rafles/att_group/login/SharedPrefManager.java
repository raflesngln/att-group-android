package com.example.rafles.att_group.login;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SP_MAHASISWA_APP = "spMahasiswaApp";

    public static final String SP_NAMA = "spNama";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    //untuk google account login
    public static final String SP_GOOGLE_EMAIL = "googleEmail";
    public static final String SP_GOOGLE_UID = "googleUid";
    public static final String SP_GOOGLE_LOGIN = "googleLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_MAHASISWA_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }

    public String getSPEmail(){
        return sp.getString(SP_EMAIL, "");
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }

    //UNTUK GOOGLE LOGIN
    public String getSpGoogleUid(){
        return sp.getString(SP_GOOGLE_UID, "");
    }

    public String getSpGoogleEmail(){
        return sp.getString(SP_GOOGLE_EMAIL, "");
    }

    public Boolean getSpGoogleLogin(){
        return sp.getBoolean(SP_GOOGLE_LOGIN, false);
    }

}
