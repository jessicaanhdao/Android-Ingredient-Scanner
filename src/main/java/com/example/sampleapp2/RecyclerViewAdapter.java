package com.example.sampleapp2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private SearchFragmentListener listener ;
    public interface SearchFragmentListener {
        void searchIngredient(String s);
    }
    private ArrayList<String> ingredientList = new ArrayList<String>();
    private Context context;
    public RecyclerViewAdapter(Context context, ArrayList<String> list ){
        this.context = context;
        this.ingredientList = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_listitems, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder called");
        holder.ingredientName.setText(ingredientList.get(position));
        holder.foreground.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick clicked on "+ingredientList.get(position));
                Toast.makeText(context, ingredientList.get(position), Toast.LENGTH_SHORT).show();
                listener = (SearchFragmentListener) context;
                listener.searchIngredient(ingredientList.get(position));
            }
        });
    }



    @Override
    public int getItemCount() {
        Log.d(TAG, "list size "+ ingredientList.size());

        return ingredientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView ingredientName;
        ConstraintLayout background,foreground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            background = itemView.findViewById(R.id.background);
            foreground = itemView.findViewById(R.id.foreground);

        }
    }
    public void delete(int position) {
        ingredientList.remove(position);
        notifyItemRemoved(position);
    }

    public void restore(String word, int position) {
        ingredientList.add(position,word);
        notifyItemInserted(position);
    }
}
