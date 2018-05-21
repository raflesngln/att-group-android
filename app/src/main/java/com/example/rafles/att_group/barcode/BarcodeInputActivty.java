package com.example.rafles.att_group.barcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafles.att_group.R;
import com.example.rafles.att_group.RetrofitCrud.RetrofitActivity;
import com.example.rafles.att_group.RetrofitCrud.TampilPegawai;
import com.example.rafles.att_group.camera.CameraAction;

public class BarcodeInputActivty extends AppCompatActivity {

    private final int CAMERA_RESULT=101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_input_activty);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

               // Camera barcod action
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(BarcodeInputActivty.this, android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                    barcodeActivityOpen();
                } else{
                    if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
                        Toast.makeText(getApplicationContext(),"permission needed",Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_RESULT);
                }


            }
        });

        //Clear action nab
        FloatingActionButton fabClear = (FloatingActionButton) findViewById(R.id.fabClear);
        fabClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtcn35 = (EditText) findViewById(R.id.tcn35);
                EditText txtcn38 = (EditText) findViewById(R.id.tcn38);
                txtcn35.setText("");
                txtcn38.setText("");
                txtcn35.requestFocus();
                Toast.makeText(BarcodeInputActivty.this,"New Input data CN35 and CN38,Please input!",Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==CAMERA_RESULT){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                barcodeActivityOpen();
            } else{
                Toast.makeText(getApplicationContext(),"Permission needed",Toast.LENGTH_LONG).show();
            }
        } else{
            super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }

    public void barcodeActivityOpen(){
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
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
