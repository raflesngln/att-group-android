package com.example.rafles.att_group.crud_firebase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rafles.att_group.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReadUsersActivity extends AppCompatActivity {
    /**Mendefinisikan variable yang akan dipakai*/
    private DatabaseReference database;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Usermodel> daftarUsers;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_users);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**Inisialisasi RecyclerView & komponennya*/
        rvView = (RecyclerView) findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        /**Inisialisasi dan mengambil Firebase Database Reference*/
        database = FirebaseDatabase.getInstance().getReference();

        /**Mengambil data barang dari Firebase Realtime DB*/
        final ProgressDialog loading;
        loading = ProgressDialog.show(ReadUsersActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /**Saat ada data baru, masukkan datanya ke ArrayList*/
                loading.dismiss();
                daftarUsers = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    /**Mapping data pada DataSnapshot ke dalam object Users Dan juga menyimpan primary key pada
                     object Users untuk keperluan Edit dan Delete data*/
                    Usermodel users = noteDataSnapshot.getValue(Usermodel.class);
                    users.setKey(noteDataSnapshot.getKey());

                    /**Menambahkan object Users yang sudah dimappingke dalam ArrayList*/
                    daftarUsers.add(users);
                }
                /**Inisialisasi adapter dan data Users dalam bentuk ArrayListdan mengeset Adapter ke dalam RecyclerView*/
                adapter=new AdapterUsers(daftarUsers,ReadUsersActivity.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });

    }

    public static Intent getActIntent(Activity activity){
        return new Intent(activity, ReadUsersActivity.class);
    }


    public void onDeleteData(Usermodel d_users, final int position) {
        /**
         * Kode ini akan dipanggil ketika method onDeleteData
         * dipanggil dari adapter lewat interface.
         * Yang kemudian akan mendelete data di Firebase Realtime DB
         * berdasarkan key users.
         * Jika sukses akan memunculkan SnackBar
         */
        if(database!=null){
            database.child("users").child(d_users.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ReadUsersActivity.this,"success delete", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
