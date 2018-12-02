package com.example.yudystriawan.projectpapb.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yudystriawan.projectpapb.Data.Food;
import com.example.yudystriawan.projectpapb.DetailFoodActivity;
import com.example.yudystriawan.projectpapb.R;

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
    public void onBindViewHolder(@NonNull final FoodViewHolder foodViewHolder, final int i) {
        final Food food = foodArrayList.get(i);

        foodViewHolder.image_food.setImageResource(food.getImage());
        foodViewHolder.text_foodDesc.setText(food.getDesc());
        foodViewHolder.image_direction.setImageResource(food.getButton());
        foodViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailFoodActivity.class);
                intent.putExtra("NAMA_RESTORAN", food.getDesc());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        public TextView text_foodDesc;
        public ImageView image_food;
        public ImageView image_direction;
        public LinearLayout linearLayout;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            text_foodDesc = itemView.findViewById(R.id.food_desc);
            image_food = itemView.findViewById(R.id.image_food);
            image_direction = itemView.findViewById(R.id.image_getDirection);
            linearLayout = itemView.findViewById(R.id.linear_food);
        }
    }
}
