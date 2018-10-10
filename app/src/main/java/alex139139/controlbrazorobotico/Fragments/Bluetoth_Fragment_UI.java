package alex139139.controlbrazorobotico.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import alex139139.controlbrazorobotico.R;


public class Bluetoth_Fragment_UI extends Fragment {


    public Bluetoth_Fragment_UI() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bluetooth_fragment_ui, container, false);
    }

}
