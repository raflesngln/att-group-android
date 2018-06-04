package com.example.rafles.att_group.recyclerview_data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafles.att_group.R;

import java.util.ArrayList;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.UsersViewHolder> {

    private ArrayList<ModelRecycler> dataList;
    private ArrayList<Integer> iconPic;//Digunakan untuk Image/Gambar
    private Context context;

    public AdapterRecycler(ArrayList<ModelRecycler> dataList,ArrayList<Integer> iconPic) {
        this.dataList = dataList;
        this.iconPic=iconPic;
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {
        // di tutorial ini kita hanya menggunakan data String untuk tiap item
        public TextView tvTitle;
        public TextView tvSubtitle;
        private ImageView gambar;

        public UsersViewHolder(View v) {
            super(v);
            //Menginisialisasi View-View untuk kita gunakan pada RecyclerView
            tvTitle = (TextView) v.findViewById(R.id.tv_title);
            tvSubtitle = (TextView) v.findViewById(R.id.tv_subtitle);
            gambar = (ImageView) v.findViewById(R.id.icongambar);
        }
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_users, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, final int position) {
        // - mengambil elemen dari dataset (ArrayList) pada posisi tertentu
        // - mengeset isi view dengan elemen dari dataset tersebut
//        final String name = rvData.get(position);
        holder.tvTitle.setText(dataList.get(position).getUsername());
        holder.tvSubtitle.setText(dataList.get(position).getFullname());
        holder.gambar.setImageResource(iconPic.get(position));

        //Membuat Aksi Saat Judul Pada List ditekan
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Data Ke- : "+position+" Ditekan", Snackbar.LENGTH_SHORT).show();
            }
        });
        //membuat long click
        holder.tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(v,"long clicked",Snackbar.LENGTH_SHORT).show();
                return true;
            }
        });



    }

    @Override
    public int getItemCount() {
        // menghitung ukuran dataset / jumlah data yang ditampilkan di RecyclerView
        return dataList.size();

    }
}
