package com.example.rafles.att_group.ApiMaps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rafles.att_group.R;
import com.firebase.ui.auth.AuthUI;

public class LoginFirebaseActivity extends AppCompatActivity {
    Button btnLogin,btn2;
    private final static int LOGIN_PERMISSION=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_firebase);
        btn2=(Button) findViewById(R.id.btn2);
        btnLogin=(Button) findViewById(R.id.btnSignIn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAllowNewEmailAccounts(true).build(),LOGIN_PERMISSION);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginFirebaseActivity.this,MapTracking.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_PERMISSION)
        {
            startNewActivity(resultCode,data);
        }
    }

    private void startNewActivity(int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            Intent intent=new Intent(LoginFirebaseActivity.this,ListOnline.class);
            startActivity(intent);
            finish();
        } else
        {
            Toast.makeText(LoginFirebaseActivity.this,"Login falsess",Toast.LENGTH_SHORT).show();
        }
    }
}
