package com.example.rafles.att_group.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafles.att_group.Model.ModelData;
import com.example.rafles.att_group.R;

import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData>{
    private List<ModelData> mItems;
    private Context context;

    public AdapterData(Context context,List<ModelData> items){
        this.mItems=items;
        this.context=context;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_shipment,parent,false);
        HolderData holderData=new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        ModelData md=mItems.get(position);
        holder.iduser.setText(md.getIduser());
        holder.name.setText(md.getName());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class HolderData extends RecyclerView.ViewHolder{
        TextView iduser,name;

        public HolderData(View view){
            super(view);
            iduser=(TextView) view.findViewById(R.id.iduser);
            name=(TextView) view.findViewById(R.id.name);

        }
    }

}
