package com.example.rafles.att_group.barcode;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.rafles.att_group.R;

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
//        mScannerView.resumeCameraPreview(this);
        confirmUpdate(kode);
    }


//    MY functio customs
private void confirmUpdate(final String nomor) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Confirm");
    builder.setMessage("Are you sure Update this ID ?");
    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            // Do nothing but close the dialog
            Toast.makeText(BarcodeActivity.this, "Data di update! "+nomor, Toast.LENGTH_LONG).show();
//            simpan data(nomor);
            startActivity(getIntent());
            dialog.dismiss();
        }
    });
    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(BarcodeActivity.this, "Dibatalkan! "+nomor , Toast.LENGTH_LONG).show();
            startActivity(getIntent());
            dialog.dismiss();
        }
    });
    AlertDialog alert = builder.create();
    alert.show();
}

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
