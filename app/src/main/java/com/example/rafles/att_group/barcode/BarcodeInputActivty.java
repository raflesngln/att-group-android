package com.example.rafles.att_group.barcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafles.att_group.R;
import com.example.rafles.att_group.RetrofitCrud.RetrofitActivity;
import com.example.rafles.att_group.RetrofitCrud.TampilPegawai;
import com.example.rafles.att_group.camera.CameraAction;

public class BarcodeInputActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_input_activty);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText tcn35 = (EditText) findViewById(R.id.tcn35);
                EditText tcn38 = (EditText) findViewById(R.id.tcn38);
                String cn35 = tcn35.getText().toString();
                String cn38 = tcn38.getText().toString();
//                jika inputan kosong validasi
                        if(cn35.trim().equals("") || cn38.trim().equals("")){
                            Toast.makeText(BarcodeInputActivty.this,"CN35 atau CN38 Tidak boleh kosong!",Toast.LENGTH_LONG).show();
                        } else{
                            Intent myIntent = new Intent(BarcodeInputActivty.this, BarcodeActivity.class);
                            myIntent.putExtra("cn35", cn35);
                            myIntent.putExtra("cn38", cn38);
                            startActivity(myIntent);
                        }

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
