package com.example.rafles.att_group.MyFragment;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.rafles.att_group.MainActivity;
import com.example.rafles.att_group.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment {
    Button btn1,btn2;

    public OneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_one, container, false);

        View viewone = inflater.inflate(R.layout.fragment_one, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Dashboard Fragment 1");
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("(Your Dashboard in fragment 1)");
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        btn1 = (Button) viewone.findViewById(R.id.btnFragment1);
        btn2 = (Button) viewone.findViewById(R.id.btnFragment2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"hai saya fragment satu",Toast.LENGTH_SHORT).show();
            }
        });
        return viewone;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoFragment fragmentKedua = new TwoFragment();
                getFragmentManager().beginTransaction()
                        //menggant fragment
                        .replace(R.id.frame_content, fragmentKedua)
                        //menyimpan fragment
                        .addToBackStack(null)
                        //transisi fragment
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        //mengeksekusi fragment transaction
                        .commit();
            }
        });
    }
}
