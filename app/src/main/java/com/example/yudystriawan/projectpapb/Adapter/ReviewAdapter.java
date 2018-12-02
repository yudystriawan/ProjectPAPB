package com.example.yudystriawan.projectpapb.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yudystriawan.projectpapb.Data.Review;
import com.example.yudystriawan.projectpapb.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter <ReviewAdapter.ReviewViewHolder> {
    LayoutInflater mInflater;
    ArrayList<Review> reviewArrayList;

    public ReviewAdapter(LayoutInflater mInflater, ArrayList<Review> reviewArrayList) {
        this.mInflater = mInflater;
        this.reviewArrayList = reviewArrayList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Create view from layout
        View itemView = mInflater.inflate(
                R.layout.review_list, viewGroup, false);
        return new ReviewAdapter.ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {
        final Review review = reviewArrayList.get(i);

        reviewViewHolder.text_nama.setText(review.getNama());
        reviewViewHolder.text_review.setText(review.getKomentar());

    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView text_nama, text_review;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            text_nama = itemView.findViewById(R.id.nama_user);
            text_review = itemView.findViewById(R.id.review_user);
        }
    }
}
