package com.example.rafles.att_group.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafles.att_group.MainActivity;
import com.example.rafles.att_group.R;

public class MyloginActivity extends AppCompatActivity {
Button btnLogin;
AutoCompleteTextView txtUsername;
EditText txtpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylogin);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        txtUsername=(AutoCompleteTextView) findViewById(R.id.txtUsername);
        txtpassword=(EditText) findViewById(R.id.txtpassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent myIntent = new Intent(MyloginActivity.this, MainActivity.class);
                Intent intent = new Intent(MyloginActivity.this, MainActivity.class);
                startActivity(intent);
//                Toast.makeText(MyloginActivity.this,"hello world",Toast.LENGTH_LONG).show();

            }
        });
    }
}
