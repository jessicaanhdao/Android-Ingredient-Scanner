package com.example.sampleapp2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

public class SearchFragment extends Fragment {
    Button searchButton;
    Button backButton;
    TextInputLayout searchTextInputLayout;
    public static TextView data;
    public static TextView title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        searchButton = getView().findViewById(R.id.searchButton);
        backButton = getView().findViewById(R.id.backButton);
        searchTextInputLayout = getView().findViewById(R.id.searchTextInputLayout);
        data = getView().findViewById(R.id.textView);
        title = getView().findViewById(R.id.title);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchString = searchTextInputLayout.getEditText().getText().toString().trim();
                if (searchString.trim().equals("")) {
                    data.setText("Empty String");
                } else {
                    Toast.makeText(getContext(), searchString,Toast.LENGTH_SHORT).show();
                    //String cleanString = searchString.replaceAll(" ","_");
                    FetchData process = new FetchData();
                    process.execute(new String[] {searchString.trim()});
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });
    }
    public void searchIngredient(String s) {
        FetchData process = new FetchData();
        process.execute(new String[] {s});
    }
    //getFragmentManager().popBackStackImmediate();
}
