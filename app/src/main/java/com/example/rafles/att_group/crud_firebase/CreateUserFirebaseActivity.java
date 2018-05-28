package com.example.rafles.att_group.crud_firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.rafles.att_group.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.text.TextUtils.isEmpty;
import com.example.rafles.att_group.crud_firebase.Usermodel;


public class CreateUserFirebaseActivity extends AppCompatActivity {
    // variable yang merefers ke Firebase Realtime Database
    private DatabaseReference database;
    // variable fields EditText dan Button
    private Button btSubmit;
    private EditText idusers,username,password,fullname,phone,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_firebase);
        // inisialisasi fields EditText dan Button
        idusers = (EditText) findViewById(R.id.t_idusers);
        username = (EditText) findViewById(R.id.t_username);
        password = (EditText) findViewById(R.id.t_password);
        fullname = (EditText) findViewById(R.id.t_fullname);
        phone = (EditText) findViewById(R.id.t_phone);
        address = (EditText) findViewById(R.id.t_address);
        // mengambil referensi ke Firebase Database
        database = FirebaseDatabase.getInstance().getReference();

        btSubmit = (Button) findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(username.getText().toString()) && !isEmpty(password.getText().toString()) && !isEmpty(fullname.getText().toString()))
                    submitBarang(new Usermodel(idusers.getText().toString(), username.getText().toString(),password.getText().toString(), fullname.getText().toString(), phone.getText().toString(), address.getText().toString()));
                else
                    Snackbar.make(findViewById(R.id.bt_submit), "Data barang tidak boleh kosong", Snackbar.LENGTH_LONG).show();

                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        username.getWindowToken(), 0);
            }
        });
    }


    private boolean isEmpty(String s) {
        // Cek apakah ada fields yang kosong, sebelum disubmit
        return TextUtils.isEmpty(s);
    }
    private void updateBarang(Usermodel barang) {
        // kodingan untuk next tutorial ya :p
    }
    private void submitBarang(Usermodel barang) {
        /**
         * Ini adalah kode yang digunakan untuk mengirimkan data ke Firebase Realtime Database
         * dan juga kita set onSuccessListener yang berisi kode yang akan dijalankan
         * ketika data berhasil ditambahkan
         */
        database.child("users").push().setValue(barang).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                idusers.setText("");
                username.setText("");
                password.setText("");
                fullname.setText("");
                phone.setText("");
                address.setText("");
                Snackbar.make(findViewById(R.id.bt_submit), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, CreateUserFirebaseActivity.class);
    }

}
