package com.example.sampleapp2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class AnalyzedWordFragment extends Fragment {
    ListView listView;
    ArrayAdapter aa ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analyzedwords, container, false);
        Log.w("ScanActivity", "Infalted analyzed fragment");
        listView = (ListView) v.findViewById(R.id.listView);
        if (listView == null) {
            Log.w("ScanActivity", "Listview is NULL");
        }
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.w("ScanActivity", "Onviewcreated");
        //Log.w("ScanActivity", Integer.toString(aa.getCount()));
        if (aa == null) {
            Log.w("ScanActivity", "aa Null");
        } else {
            Log.w("ScanActivity", Integer.toString(aa.getCount()));
        }
        listView.setAdapter(aa);
        //listView = getView().findViewById(R.id.listView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //listener = (HomeFragmentListener) getActivity();
        //listView = getActivity().findViewById(R.id.listView);
        Log.w("ScanActivity", "on activity created");


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.w("ScanActivity", "attach to parent");

    }


    @Override
    public void onDetach() {
        super.onDetach();

    }
    public void publishTexts(ArrayAdapter arrayAdapter) {
        this.aa = arrayAdapter;
        if (arrayAdapter == null) {
            Log.w("ScanActivity", "arrayAdapter Null");
        } else {
            Log.w("ScanActivity", Integer.toString(arrayAdapter.getCount()));
        }
        if (listView != null) {



        } else {

            Log.w("ScanActivity", "listview Null");
        }

    }
}
