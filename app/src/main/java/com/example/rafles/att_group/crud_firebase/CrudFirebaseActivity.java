package com.example.rafles.att_group.crud_firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rafles.att_group.R;

public class CrudFirebaseActivity extends AppCompatActivity {
    private Button btCreateDB,btViewDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_firebase);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btCreateDB = (Button) findViewById(R.id.bt_createdata);
        btViewDB = (Button) findViewById(R.id.bt_viewdata);

        btCreateDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CreateUserFirebaseActivity.getActIntent(CrudFirebaseActivity.this));
            }
        });

        btViewDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code for view
                Intent intent = new Intent(CrudFirebaseActivity.this, ReadUsersActivity.class);
                startActivity(intent);
            }
        });

    }




    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
