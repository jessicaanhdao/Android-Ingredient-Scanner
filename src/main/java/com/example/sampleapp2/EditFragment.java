package com.example.sampleapp2;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class EditFragment extends Fragment {
    private ArrayList<String> ingredientList = new ArrayList<String>();
    RecyclerView recyclerView;
    EditFragmentAdapter adapter;
    Button doneButton;

    private HomeFragment.HomeFragmentListener listener ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_editrecycler, container, false);
        Log.w("EditActivity", "Inflated edit recycler fragment");
        return v;

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.HomeFragmentListener) {
            listener = (HomeFragment.HomeFragmentListener) context; //our activity
        } else {
            throw new RuntimeException(context.toString() + " must implement HomeFragmentListener");
        }
        Log.w("EditActivity", "attach to parent");

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getIngredientList(ArrayList<String> scannedList) {
        Log.w(TAG, "init ingredient list");
        ingredientList.addAll(scannedList);

    }
    private void initRecyclerView() {
        recyclerView = getView().findViewById(R.id.recyclerView);
        adapter = new EditFragmentAdapter(getContext(), ingredientList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        Log.w("ScanActivity", "Onviewcreated");
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.delete(viewHolder.getAdapterPosition());
                Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                //super.clearView(recyclerView, viewHolder);
                View foregroundView = ((RecyclerViewAdapter.ViewHolder)viewHolder).foreground;
                getDefaultUIUtil().clearView(foregroundView);
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                //super.onSelectedChanged(viewHolder, actionState);
                if (viewHolder != null) {
                    View foregroundView = ((RecyclerViewAdapter.ViewHolder)viewHolder).foreground;
                    getDefaultUIUtil().onSelected(foregroundView);
                }
            }

            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }


            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View foregroundView = ((RecyclerViewAdapter.ViewHolder)viewHolder).foreground;
                getDefaultUIUtil().onDraw(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);
            }

            @Override
            public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                //super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View foregroundView = ((RecyclerViewAdapter.ViewHolder)viewHolder).foreground;
                getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);

        doneButton = getView().findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onWordsSent(ingredientList);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.w("ScanActivity", "on activity created");


    }
}
