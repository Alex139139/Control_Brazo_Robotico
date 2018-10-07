package alex139139.controlbrazorobotico.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import alex139139.controlbrazorobotico.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Test extends Fragment {


    public Test() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_test, container, false);

        return view;
    }

}
