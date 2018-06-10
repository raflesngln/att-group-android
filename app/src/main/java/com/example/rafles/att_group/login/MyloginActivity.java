package com.example.rafles.att_group.login;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafles.att_group.MainActivity;
import com.example.rafles.att_group.R;
import com.example.rafles.att_group.RetrofitCrud.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MyloginActivity extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    Context mContext;

    Button btnLogin;
    AutoCompleteTextView txtUsername;
    EditText txtpassword;
//    private String usr;
//    private String pas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylogin);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        txtUsername=(AutoCompleteTextView) findViewById(R.id.txtUsername);
        txtpassword=(EditText) findViewById(R.id.txtpassword);

        mContext = this;
        sharedPrefManager = new SharedPrefManager(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekUser();
            }
        });

        // Code berikut berfungsi untuk mengecek session, Jika session true ( sudah login )
        // maka langsung memulai MainActivity.
        if (sharedPrefManager.getSPSudahLogin()){
            startActivity(new Intent(MyloginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        //untuk manage toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarlogin);
        setSupportActionBar(toolbar);// set toolbar ke dalam support action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// enable home button untuk navigasi
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_icon_apps_round);// set icon home button Toolbar
        getSupportActionBar().setTitle("  LOGIN USER");// set title/nama aplikasi
        getSupportActionBar().setDisplayUseLogoEnabled(true);// set logo toolbar
        getSupportActionBar().setLogo(R.drawable.powered_by_google_light);
        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorButton));
        }

    }

    private void cekUser(){
        final String usr = txtUsername.getText().toString().trim();
        final String pas = txtpassword.getText().toString().trim();

        class cekUser extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MyloginActivity.this,"Checking account...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                resultLogin(s);
//                Toast.makeText(MyloginActivity.this,"Respon dari server adalah : "+s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(KonfigLogin.KEY_EMP_USR,usr);
                hashMap.put(KonfigLogin.KEY_EMP_PAS,pas);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(KonfigLogin.URL_CEK_LOGIN,hashMap);
                return s;
            }
        }

        cekUser ue = new cekUser();
        ue.execute();
    }

    private void resultLogin(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(KonfigLogin.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String usr = c.getString(KonfigLogin.TAG_USR);
            String pas = c.getString(KonfigLogin.TAG_PAS);
            String status = c.getString(KonfigLogin.TAG_STATUS);
            Toast.makeText(MyloginActivity.this,"Berhasil,Respon dari server adalah : "+status,Toast.LENGTH_LONG).show();
            TextView tMessage=(TextView) findViewById(R.id.tMessage);
            tMessage.setText("");

            // Jika login berhasil maka data nama yang ada di response API akan diparsing ke activity selanjutnya.
            sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, usr);
            // Shared Pref ini berfungsi untuk menjadi trigger session login
            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
            startActivity(new Intent(mContext, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//            Intent intent = new Intent(MyloginActivity.this, MainActivity.class);
//            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
            TextView tMessage=(TextView) findViewById(R.id.tMessage);
            tMessage.setText("Username & Password is Wrong !");
//            Toast.makeText(MyloginActivity.this,"Username & Password is Wrong !",Toast.LENGTH_LONG).show();
        }

    }


}
