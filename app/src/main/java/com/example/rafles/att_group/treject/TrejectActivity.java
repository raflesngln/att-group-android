package com.example.rafles.att_group.treject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rafles.att_group.R;
import com.example.rafles.att_group.treject.KonfigTreject;
import com.example.rafles.att_group.RetrofitCrud.RequestHandler;
import com.example.rafles.att_group.treject.TrejectActivity;
import com.example.rafles.att_group.treject.TampilrejectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TrejectActivity extends AppCompatActivity implements View.OnClickListener {
    //Dibawah ini merupakan perintah untuk mendefinikan View
    private EditText Noid;
    private EditText connote;
    private EditText CN35;
    private EditText CN38;
    private EditText datecreate;

    private Button buttonAdd;
    private Button buttonView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treject);
        //Inisialisasi dari View
        Noid = (EditText) findViewById(R.id.Noid);
        connote = (EditText) findViewById(R.id.connote);
        CN35 = (EditText) findViewById(R.id.CN35);
        CN38 = (EditText) findViewById(R.id.CN38);
        datecreate = (EditText) findViewById(R.id.datecreate);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonView = (Button) findViewById(R.id.buttonView);

        //Setting listeners to button
        buttonAdd.setOnClickListener(this);
        buttonView.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    //Dibawah ini merupakan perintah untuk Menambahkan Pegawai (CREATE)
    private void AddReject() {
        final String id = Noid.getText().toString().trim();
        final String con = connote.getText().toString().trim();
        final String cn5 = CN35.getText().toString().trim();
        final String cn8 = CN38.getText().toString().trim();
        final String datecr = datecreate.getText().toString().trim();

        class AddReject extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TrejectActivity.this, "Menambahkan...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                String str = s;
                try {
                    String notif;
                    JSONObject obj = new JSONObject(str);
                    //int success = obj.getInt("success");
                    String success = obj.getString("success");
                    String message = obj.getString("message");
                    if (success == "0") {
                        notif = "GAGAL";
                    } else {
                        notif = "BERHASIL";
                    }
                    Toast.makeText(TrejectActivity.this, message + " Status " + notif, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("error Not defined");
                }

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(KonfigTreject.KEY_EMP_ID, id);
                params.put(KonfigTreject.KEY_EMP_CONNOTE, con);
                params.put(KonfigTreject.KEY_EMP_CN35, cn5);
                params.put(KonfigTreject.KEY_EMP_CN38, cn8);
                params.put(KonfigTreject.KEY_EMP_DATECREATE, datecr);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(KonfigTreject.URL_ADD, params);
                return res;
            }
        }

        AddReject ae = new AddReject();
        ae.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonAdd) {
            AddReject();
        }

        if (v == buttonView) {
            startActivity(new Intent(this, TampilrejectActivity.class));
        }
    }

    //Dibawah ini untuk Cek data ada atau tidak dengan nomor tertentu
    private void cekIDExist(final String nomor) {
//final String id = editTextId.getText().toString().trim();
        class cekIdEmployee extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TrejectActivity.this, "Mencari data...", "Waiit...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                String str = s;
                try {
                    String notif;
                    JSONObject obj = new JSONObject(str);
                    //int success = obj.getInt("success");
                    String success = obj.getString("success");
                    String message = obj.getString("message");
                    if (success == "0") {
                        notif = "TIDAK KETEMU !!";
                    } else {
                        notif = "KETEMU !!";
                        confirmUpdate(nomor);
                    }
                    Toast.makeText(TrejectActivity.this, message + " Status " + notif, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("error Not defined");
                }

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                String id = nomor;
                String name = nomor;
                params.put(KonfigTreject.KEY_EMP_ID, id);
                params.put(KonfigTreject.KEY_EMP_CONNOTE, name);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(KonfigTreject.URL_CEK_EMP, params);
                return res;
            }
        }
        cekIdEmployee ae = new cekIdEmployee();
        ae.execute();
    }


    private void confirmUpdate(final String nomor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure Update this ID ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                Toast.makeText(TrejectActivity.this, "Data di update! ", Toast.LENGTH_LONG).show();
                callUpdateExist(nomor);
                dialog.dismiss();
            }

        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(TrejectActivity.this, "Dibatalkan! ", Toast.LENGTH_LONG).show();
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Update data jika idnya ketemu atau barcode ketemu
    private void callUpdateExist(final String nomor) {
//final String id = editTextId.getText().toString().trim();
        class updateIdReject extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TrejectActivity.this, "Megupdate data...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                String str = s;
                try {
                    String notif;
                    JSONObject obj = new JSONObject(str);
                    String success = obj.getString("success");
                    String message = obj.getString("message");
                    if (success == "0") {
                        notif = "Data NOt Update !!";
                    } else {
                        notif = "Data Updated !!";
                    }
                    Toast.makeText(TrejectActivity.this, message + " Status " + notif, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("error Not defined");
                }

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                String id = nomor;
                String name = "Raflesia nainggolan";
                String alamat = "Lorem ipsum dolor sit amet";
                String jabatan = "90000";
                String tgl = "2018-05-01";

                params.put(KonfigTreject.KEY_EMP_ID, id);
                params.put(KonfigTreject.KEY_EMP_CONNOTE, name);
                params.put(KonfigTreject.KEY_EMP_CN35, alamat);
                params.put(KonfigTreject.KEY_EMP_CN38, jabatan);
                params.put(KonfigTreject.KEY_EMP_DATECREATE, tgl);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(KonfigTreject.URL_UPDATE_EMP, params);
                return res;
            }
        }
        updateIdReject ae = new updateIdReject();
        ae.execute();
    }
}