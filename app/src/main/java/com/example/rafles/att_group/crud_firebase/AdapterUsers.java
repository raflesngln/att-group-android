package com.example.rafles.att_group.crud_firebase;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rafles.att_group.R;
import com.example.rafles.att_group.crud_firebase.Usermodel;

import java.util.ArrayList;
import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.ViewHolder> {

    private ArrayList<Usermodel> daftarUsers;
    private Context context;
    ReadUsersActivity listener; //for refresh after delete

    public AdapterUsers(ArrayList<Usermodel> d_users, Context ctx) {
        daftarUsers = d_users;
        context = ctx;
        listener = (ReadUsersActivity) ctx;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // di tutorial ini kita hanya menggunakan data String untuk tiap item
        public TextView tvTitle;
        public TextView tvSubtitle;
        public  RelativeLayout headerlist;
//        private ImageView gambar;

        public ViewHolder(View v) {
            super(v);
            //Menginisialisasi View-View untuk kita gunakan pada RecyclerView
            tvTitle = (TextView) v.findViewById(R.id.tv_title);
            tvSubtitle = (TextView) v.findViewById(R.id.tv_subtitle);
            headerlist=(RelativeLayout) v.findViewById(R.id.headerlist);
//            gambar = (ImageView) v.findViewById(R.id.icongambar);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view baru
        View viewP = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_users, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(viewP);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - mengambil elemen dari dataset (ArrayList) pada posisi tertentu
        // - mengeset isi view dengan elemen dari dataset tersebut
        final String name = daftarUsers.get(position).getUsername();
        holder.tvTitle.setText(name);
        holder.tvSubtitle.setText("index  " + position);

        //Membuat Aksi Saat Judul Pada List ditekan
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Data Ke- :"+name+"  Ditekan", Snackbar.LENGTH_SHORT).show();
            }
        });

        //membuat long click
        holder.headerlist.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //create dialog action
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_action_user);
                dialog.setTitle("Pilih Aksi");
                dialog.show();
                Button editButton = (Button) dialog.findViewById(R.id.bt_edit_data);
                Button delButton = (Button) dialog.findViewById(R.id.bt_delete_data);

                //if tombol edit klik
                editButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                context.startActivity(CreateUserFirebaseActivity.getActIntent((Activity) context).putExtra("data", daftarUsers.get(position)));
                            }
                        }
                );
                //if tombol delete klik
                delButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                listener.onDeleteData(daftarUsers.get(position), position);
                            }
                        }
                );
                return true;

            }
        });



    }


    @Override
    public int getItemCount() {
        // menghitung ukuran dataset / jumlah data yang ditampilkan di RecyclerView
        return daftarUsers.size();
    }

    public interface FirebaseDataListener{
        void onDeleteData(Usermodel users, int position);
    }

}
