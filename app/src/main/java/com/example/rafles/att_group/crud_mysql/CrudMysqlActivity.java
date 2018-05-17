package com.example.rafles.att_group.crud_mysql;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.rafles.att_group.Adapter.AdapterData;
import com.example.rafles.att_group.Model.ModelData;
import com.example.rafles.att_group.R;
import com.example.rafles.att_group.Util.AppController;
import com.example.rafles.att_group.Util.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CrudMysqlActivity extends AppCompatActivity {
    RecyclerView mRecylceview;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<ModelData> mItems;
    Button btnInsert,btnDelete;
    ProgressDialog myprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_mysql);

        mRecylceview=(RecyclerView) findViewById(R.id.recycleviewTemp);
        btnInsert=(Button) findViewById(R.id.btn_insert);
        btnDelete=(Button)findViewById(R.id.btn_delete);

        myprogress=new ProgressDialog(CrudMysqlActivity.this);
        mItems=new ArrayList<>();
        loadJson();

        mManager=new LinearLayoutManager(CrudMysqlActivity.this,LinearLayoutManager.VERTICAL,false);
        mRecylceview.setLayoutManager(mManager);
        mAdapter=new AdapterData(CrudMysqlActivity.this,mItems);
        mRecylceview.setAdapter(mAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void loadJson(){
        myprogress.setMessage("loading data from server");
        myprogress.setCancelable(false);
        myprogress.show();
        JsonRequest reqData=new JsonArrayRequest(Request.Method.POST, ServerAPI.URL_DATA, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                myprogress.cancel();
                Log.d("volley","Respon"+response.toString());
                for (int i=0;i < response.length();i++){
                    try {
                        JSONObject data=response.getJSONObject(i);
                        ModelData md=new ModelData();
                        md.setIduser(data.getString("iduser"));
                        md.setName(data.getString("name"));
                        md.setAddress(data.getString("address"));
                        md.setDepartment(data.getString("department"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.notifyDataSetChanged();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        myprogress.cancel();
                        Log.d("volley","eror"+error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(reqData);

    }




    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
