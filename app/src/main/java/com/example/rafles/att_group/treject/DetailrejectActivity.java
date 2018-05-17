package com.example.rafles.att_group.treject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rafles.att_group.R;
import com.example.rafles.att_group.treject.KonfigTreject;
import com.example.rafles.att_group.RetrofitCrud.RequestHandler;
import com.example.rafles.att_group.treject.DetailrejectActivity;
import com.example.rafles.att_group.treject.DetailrejectActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DetailrejectActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText Noid;
    private EditText connote;
    private EditText CN35;
    private EditText CN38;
    private  EditText datecreate;

    private Button buttonUpdate;
    private Button buttonDelete;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailreject);
        //Mengabil data Konfigurasi yg dilempar dari class TampilSemuaPgw
        Intent intent = getIntent();
        id = intent.getStringExtra(KonfigTreject.EMP_ID);

//        Noid = (EditText) findViewById(R.id.Noid);
//        connote = (EditText) findViewById(R.id.connote);
//        CN35 = (EditText) findViewById(R.id.CN35);
//        CN38 = (EditText) findViewById(R.id.CN38);
//        datecreate = (EditText) findViewById(R.id.datecreate);
//
//        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
//        buttonDelete = (Button) findViewById(R.id.buttonDelete);
//
//        buttonUpdate.setOnClickListener(this);
//        buttonDelete.setOnClickListener(this);
//        //set value editTextId dari hasil lempar class
//        Noid.setText(id);
        //tampilkan value dari edit data
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        GetReject();
        testing();
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private  void testing(){
        Toast.makeText(DetailrejectActivity.this,"testings",Toast.LENGTH_LONG).show();
    }
    private void GetReject(){
        class GetReject extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DetailrejectActivity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showReject(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(KonfigTreject.URL_GET_EMP,id);
                return s;
            }
        }
        GetReject ge = new GetReject();
        ge.execute();
    }

    private void showReject(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(KonfigTreject.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String con = c.getString(KonfigTreject.TAG_CONNOTE);
            String cn5 = c.getString(KonfigTreject.TAG_CN35);
            String cn8 = c.getString(KonfigTreject.TAG_CN38);
            String datecr = c.getString(KonfigTreject.TAG_DATECREATE);

            connote.setText(con);
            CN35.setText(cn5);
            CN38.setText(cn8);
            datecreate.setText(datecr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateReject(){
        final String con = connote.getText().toString().trim();
        final String cn5 = CN35.getText().toString().trim();
        final String cn8 = CN38.getText().toString().trim();
        final String datecr = datecreate.getText().toString().trim();

        class updateReject extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DetailrejectActivity.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(DetailrejectActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(KonfigTreject.KEY_EMP_ID,id);
                hashMap.put(KonfigTreject.KEY_EMP_CONNOTE,con);
                hashMap.put(KonfigTreject.KEY_EMP_CN35,cn5);
                hashMap.put(KonfigTreject.KEY_EMP_CN38,cn8);
                hashMap.put(KonfigTreject.KEY_EMP_DATECREATE,datecr);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(KonfigTreject.URL_UPDATE_EMP,hashMap);

                return s;
            }
        }

        updateReject ue = new updateReject();
        ue.execute();
    }

    private void deleteReject(){
        class deleteReject extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DetailrejectActivity.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(DetailrejectActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(KonfigTreject.URL_DELETE_EMP, id);
                return s;
            }
        }

        deleteReject de = new deleteReject();
        de.execute();
    }

    private void confirmDeleteEmployee(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Pegawai ini?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteReject();
                        startActivity(new Intent(DetailrejectActivity.this,DetailrejectActivity.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            updateReject();
        }

        if(v == buttonDelete){
            confirmDeleteEmployee();
        }
    }
}
