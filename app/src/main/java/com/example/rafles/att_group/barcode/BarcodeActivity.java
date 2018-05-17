package com.example.rafles.att_group.barcode;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.rafles.att_group.R;
import com.example.rafles.att_group.RetrofitCrud.Konfigurasi;
import com.example.rafles.att_group.RetrofitCrud.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class BarcodeActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler{
    private ZBarScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_barcode);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        //Log.v(TAG, rawResult.getContents()); // Prints scan results
        //Log.v(TAG, rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)
        String kode=rawResult.getContents();
        Toast.makeText(this, "Kode terbaca adalah "+kode, Toast.LENGTH_SHORT).show();

        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
        confirmUpdate(kode);
    }


//    MY functio customs
private void confirmUpdate(final String nomor) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Confirm");
    builder.setMessage("Yakin Simpan Data ini ?"+nomor);
    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
//            Toast.makeText(BarcodeActivity.this, "Data Disimpan! "+nomor, Toast.LENGTH_LONG).show();
//            Simpan data setelah klik oke
            simpanData(nomor);
            startActivity(getIntent());
            dialog.dismiss();
        }
    });
    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(BarcodeActivity.this, "Simpan Dibatalkan! "+nomor , Toast.LENGTH_LONG).show();
//            startActivity(getIntent());
            dialog.dismiss();
        }
    });
    AlertDialog alert = builder.create();
    alert.show();
}

    //Dibawah ini merupakan perintah untuk Menambahkan Pegawai (CREATE)
    private void simpanData(final String nomor){
        Intent intent = getIntent();
        String emailku = intent.getStringExtra("emailku");
        String alamatku = intent.getStringExtra("alamatku");

        final String id = nomor;
        final String name = "dodo aswin";
        final String alamat =emailku;
        final String jab = "IT Developer";

        class simpanData extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(BarcodeActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s)   {
                super.onPostExecute(s);
                loading.dismiss();
                String str = s;
                try {
                    String notif;
                    JSONObject obj  = new JSONObject(str);
                    //int success = obj.getInt("success");
                    String success = obj.getString("success");
                    String message = obj.getString("message");
                    if(success=="0"){
                        notif="GAGAL";
                    } else{
                        notif="BERHASIL";
                    }
                    Toast.makeText(BarcodeActivity.this,message + " Status "+ notif,Toast.LENGTH_LONG).show();

                } catch (JSONException e){
                    e.printStackTrace();
                    System.out.println("error Not defined");
                }

            }
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Konfigurasi.KEY_EMP_ID,id);
                params.put(Konfigurasi.KEY_EMP_NAMA,name);
                params.put(Konfigurasi.KEY_EMP_ALAMAT,alamat);
                params.put(Konfigurasi.KEY_EMP_JABATAN,jab);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Konfigurasi.URL_ADD, params);
                return res;
            }
        }

        simpanData ae = new simpanData();
        ae.execute();
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
