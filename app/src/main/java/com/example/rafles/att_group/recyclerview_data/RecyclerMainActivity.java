package com.example.rafles.att_group.recyclerview_data;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.rafles.att_group.R;
import com.example.rafles.att_group.recyclerview_data.AdapterRecycler;

import java.util.ArrayList;

public class RecyclerMainActivity extends AppCompatActivity {
    private RecyclerView rvView;
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView recyclerView;
    private AdapterRecycler adapter;
    private ArrayList<ModelRecycler> userlists;

    private ArrayList<Integer> Gambaricon;


    //Daftar Judul
    private String[] Judul = {"Rafles", "Taufan", "Aish", "Ferdi", "Taufik", "Dzaki", "Annisa","Karin","Monika","Gisela"};
    //Daftar Gambar
    private int[] Gambar = {R.drawable.ic_menu_camera, R.drawable.ic_menu_gallery, R.drawable.ic_menu_send, R.drawable.ic_menu_share,
            R.drawable.ic_menu_gallery, R.drawable.ic_menu_slideshow
            ,R.drawable.ic_menu_send, R.drawable.ic_menu_send, R.drawable.ic_menu_send,R.drawable.ic_menu_send};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Gambaricon = new ArrayList<>();
        addData();
        recyclerView  = (RecyclerView) findViewById(R.id.rv_main);
        adapter = new AdapterRecycler(userlists,Gambaricon);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecyclerMainActivity.this);


        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

    }

    private void addData() {
        for (int w=0; w<Judul.length; w++){
            Gambaricon.add(Integer.valueOf(Gambar[w]));
            userlists = new ArrayList<>();
            userlists.add(new ModelRecycler("101", "Rafles ", "Rafles Nainggolan"));
            userlists.add(new ModelRecycler("102", "Fadly ", "Fadly Yonk"));
            userlists.add(new ModelRecycler("103", "Ariyandi ", "Ariyandi Nugraha"));
            userlists.add(new ModelRecycler("104", "Aham ", "Aham Siswana"));
            userlists.add(new ModelRecycler("105", "mawar ", "mawar"));
            userlists.add(new ModelRecycler("106", "budi ", "budi"));
            userlists.add(new ModelRecycler("107", "sarah ", "sarah"));
            userlists.add(new ModelRecycler("108", "melati ", "melati"));
            userlists.add(new ModelRecycler("108", "melati ", "melati"));
            userlists.add(new ModelRecycler("108", "melati ", "melati"));
        }


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
