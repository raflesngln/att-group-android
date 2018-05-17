package com.example.rafles.att_group.treject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.rafles.att_group.R;
import com.example.rafles.att_group.RetrofitCrud.Konfigurasi;
import com.example.rafles.att_group.RetrofitCrud.RequestHandler;
import com.example.rafles.att_group.barcode.BarcodeActivity;
import com.example.rafles.att_group.barcode.BarcodeInputActivty;
import com.example.rafles.att_group.treject.DetailrejectActivity;
import com.example.rafles.att_group.treject.TampilrejectActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TampilrejectActivity extends AppCompatActivity implements ListView.OnItemClickListener{

    private ListView listView;

    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampilreject);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getJSON();
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void showReject(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(KonfigTreject.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(KonfigTreject.TAG_ID);
                String con = jo.getString(KonfigTreject.TAG_CONNOTE);
                String cn5 = jo.getString(KonfigTreject.TAG_CN35);
                String cn8 = jo.getString(KonfigTreject.TAG_CN38);
                String datecr = jo.getString(KonfigTreject.TAG_DATECREATE);

                HashMap<String,String> employees = new HashMap<>();
                employees.put(KonfigTreject.TAG_ID,id);
                employees.put(KonfigTreject.TAG_CONNOTE,con);
                employees.put(KonfigTreject.TAG_CN35,cn5);
                employees.put(KonfigTreject.TAG_CN38,cn8);
                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                TampilrejectActivity.this, list, R.layout.list_item_reject,
                new String[]{KonfigTreject.TAG_ID,KonfigTreject.TAG_CONNOTE,KonfigTreject.TAG_CN35},
                new int[]{R.id.id, R.id.name, R.id.tAddress});
        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilrejectActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showReject();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(KonfigTreject.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetailrejectActivity.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String empId = map.get(KonfigTreject.TAG_ID).toString();
        intent.putExtra(KonfigTreject.EMP_ID,empId);
        startActivity(intent);
    }
}