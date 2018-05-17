package com.example.rafles.att_group.barcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
Button btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_input_activty);
        btnScan=(Button) findViewById(R.id.btnScan);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tcn35 = (EditText) findViewById(R.id.tcn35);
                EditText tcn38 = (EditText) findViewById(R.id.tcn38);
                String cn35 = tcn35.getText().toString();
                String cn38 = tcn38.getText().toString();


                Intent myIntent = new Intent(BarcodeInputActivty.this, BarcodeActivity.class);
                myIntent.putExtra("emailku", cn35);
                myIntent.putExtra("alamatku", cn38);
                startActivity(myIntent);

                Toast.makeText(BarcodeInputActivty.this, "Hello world ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
