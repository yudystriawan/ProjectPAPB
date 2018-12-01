package com.example.yudystriawan.projectpapb;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PublicKey;
import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    LayoutInflater mInflater;
    ArrayList<Food> foodArrayList;

    public FoodAdapter(LayoutInflater mInflater, ArrayList<Food> foodArrayList) {
        this.mInflater = mInflater;
        this.foodArrayList = foodArrayList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Create view from layout
        View itemView = mInflater.inflate(
                R.layout.food_list, viewGroup, false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i) {
        final Food food = foodArrayList.get(i);

        foodViewHolder.image_food.setImageResource(food.getImage());
        foodViewHolder.text_foodDesc.setText(food.getDesc());
        foodViewHolder.image_direction.setImageResource(food.getButton());

    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        public TextView text_foodDesc;
        public ImageView image_food;
        public ImageView image_direction;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            text_foodDesc = itemView.findViewById(R.id.food_desc);
            image_food = itemView.findViewById(R.id.image_food);
            image_direction = itemView.findViewById(R.id.image_getDirection);

        }
    }
}
