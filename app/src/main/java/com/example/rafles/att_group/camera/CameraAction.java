package com.example.rafles.att_group.camera;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rafles.att_group.R;

public class CameraAction extends AppCompatActivity {
    Button btncamera;
    ImageView imageFrame;
    private final int CAMERA_RESULT=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_action);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageFrame=(ImageView) findViewById(R.id.imageFrame);
        btncamera=(Button) findViewById(R.id.btncamera);

        imageFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CameraAction.this,"Your Click the picture!", Toast.LENGTH_LONG).show();
            }
        });

        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(CameraAction.this, android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                    dispatchTakenPictureIntent();
                } else{
                    if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
                        Toast.makeText(getApplicationContext(),"permission needed",Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_RESULT);
                }
            }
        });

    }

    private void dispatchTakenPictureIntent(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) !=null){
            startActivityForResult(intent,CAMERA_RESULT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==CAMERA_RESULT){
                Bundle extras=data.getExtras();
                Bitmap bitmap=(Bitmap) extras.get("data");
                imageFrame.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==CAMERA_RESULT){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                dispatchTakenPictureIntent();
            } else{
                Toast.makeText(getApplicationContext(),"Permission needed",Toast.LENGTH_LONG).show();
            }
        } else{
            super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}