package com.example.rafles.att_group.MyFragment;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rafles.att_group.MainActivity;
import com.example.rafles.att_group.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends Fragment {
Button btn1,btn2;

    public TwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_two, container, false);
        View vietwo = inflater.inflate(R.layout.fragment_two, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Fragment 2");
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("(Your Content in fragment 2)");

        btn2 = (Button) vietwo.findViewById(R.id.btnFragment2);
        //agar button back work
        setHasOptionsMenu(true);
        return vietwo;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreeFragment fragmenttiga = new ThreeFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_content, fragmenttiga)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
    }

    @Override
    //membuat button back
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            getFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

}
