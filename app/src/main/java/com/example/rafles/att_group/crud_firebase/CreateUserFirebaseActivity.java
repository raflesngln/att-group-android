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

import java.util.ArrayList;
import java.util.List;


public class CreateUserFirebaseActivity extends AppCompatActivity {
    // variable yang merefers ke Firebase Realtime Database
    private DatabaseReference database;
    // variable fields EditText dan Button
    private Button btSubmit,bt_save;
    private EditText idusers,username,password,fullname,phone,address;

    //a list to store all the artist from firebase database
    List<Usermodel> users;
    //our database reference object
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_firebase);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // inisialisasi fields EditText dan Button
        idusers = (EditText) findViewById(R.id.t_idusers);
        username = (EditText) findViewById(R.id.t_username);
        password = (EditText) findViewById(R.id.t_password);
        fullname = (EditText) findViewById(R.id.t_fullname);
        phone = (EditText) findViewById(R.id.t_phone);
        address = (EditText) findViewById(R.id.t_address);
        // mengambil referensi ke Firebase Database
        database = FirebaseDatabase.getInstance().getReference();

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        //tangkap id dari action edit lalu cocokkan dgn data di database
        final Usermodel userPut = (Usermodel) getIntent().getSerializableExtra("data");
        users = new ArrayList<>();

        btSubmit = (Button) findViewById(R.id.bt_submit);
        bt_save = (Button) findViewById(R.id.bt_save);

        //jika ada lemparan data dari activity lain berarti ada edit update data
        if (userPut != null) {
            idusers.setText(userPut.getIdusers());
            username.setText(userPut.getUsername());
            password.setText(userPut.getPassword());
            fullname.setText(userPut.getFullname());
            phone.setText(userPut.getPhone());
            address.setText(userPut.getAddress());
            bt_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userPut.setIdusers(idusers.getText().toString());
                    userPut.setUsername(username.getText().toString());
                    userPut.setPassword(password.getText().toString());
                    userPut.setFullname(fullname.getText().toString());
                    userPut.setPhone(phone.getText().toString());
                    userPut.setAddress(address.getText().toString());
                    updateUsers(userPut);
                }
            });
        } else { // berarti data baru,maka simpan baru
            bt_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addUsers(v);
                }
            });

        }
    }

    private void addUsers(View v) {
        String iduser = idusers.getText().toString().trim();
        String usr = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String fullnm = fullname.getText().toString().trim();
        String ph = phone.getText().toString().trim();
        String addr = address.getText().toString().trim();
        //checking if the value is provided
        if (TextUtils.isEmpty(iduser)||TextUtils.isEmpty(usr) || TextUtils.isEmpty(pass)) {
            Snackbar.make(v,"Complete Your Input!",Snackbar.LENGTH_SHORT).show();
        } else {
            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key
            String id = databaseUsers.push().getKey();
            //creating an User Object
            Usermodel usermodel = new Usermodel(iduser,usr,pass,fullnm,ph,addr);
            //Saving the User
            databaseUsers.child(id).setValue(usermodel);
            resetFormInput();
            Snackbar.make(v,"Saving data successfull!",Snackbar.LENGTH_SHORT).show();
        }

    }


    private boolean isEmpty(String s) {
        // Cek apakah ada fields yang kosong, sebelum disubmit
        return TextUtils.isEmpty(s);
    }
    private void updateuser(Usermodel usernew) {
        // kodingan untuk next tutorial ya :p
    }
    private void simpanData(Usermodel usernew) {
        /**
         * Ini adalah kode yang digunakan untuk mengirimkan data ke Firebase Realtime Database
         * dan juga kita set onSuccessListener yang berisi kode yang akan dijalankan
         * ketika data berhasil ditambahkan
         */
        database.child("users").push().setValue(usernew).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                resetFormInput();
                Snackbar.make(findViewById(R.id.bt_submit), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void resetFormInput() {
        idusers.setText("");
        username.setText("");
        password.setText("");
        fullname.setText("");
        phone.setText("");
        address.setText("");
    }


    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan kembali ke Intent ini
        return new Intent(activity, CreateUserFirebaseActivity.class);
    }

    private void updateUsers(Usermodel tbl_user) {
        /**
         * Baris kode yang digunakan untuk mengupdate data users
         * yang sudah dimasukkan di Firebase Realtime Database
         */
        database.child("users") //akses parent index, ibaratnya seperti nama tabel
                .child(tbl_user.getKey()) //select users berdasarkan key
                .setValue(tbl_user) //set value barang yang baru
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        /**Baris kode yang akan dipanggil apabila proses update barang sukses*/
                        Snackbar.make(findViewById(R.id.bt_submit), "Data berhasil di Update", Snackbar.LENGTH_LONG).setAction("Oke", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();
                    }
                });
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
