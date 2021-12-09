package com.example.itrain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomRecyclerViewTrainingAdapter extends RecyclerView.Adapter<CustomRecyclerViewTrainingAdapter.ViewHolder> {
    private ArrayList<String> names;

    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    CustomRecyclerViewTrainingAdapter(Context context, ArrayList<String> names) {
        this.layoutInflater = LayoutInflater.from(context);
        this.names = names;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.training_listactivity_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setName(this.names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtTitleExercises;
        private CardView rlBtnInfoExercise;

        private String name;

        public ViewHolder(View view){
            super(view);

            txtTitleExercises = (TextView) view.findViewById(R.id.txtTitleExercises);
            rlBtnInfoExercise = (CardView) view.findViewById(R.id.rlBtnInfoExercise);
            rlBtnInfoExercise.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(view, getBindingAdapterPosition());
        }

        public void setName(String name){
            this.name = name;
            this.txtTitleExercises.setText(name);
        }
    }

    String getName(int id) {
        return names.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
