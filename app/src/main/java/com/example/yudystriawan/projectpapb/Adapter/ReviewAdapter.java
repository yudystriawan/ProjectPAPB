package com.example.yudystriawan.projectpapb.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yudystriawan.projectpapb.Data.Review;
import com.example.yudystriawan.projectpapb.DetailFoodActivity;
import com.example.yudystriawan.projectpapb.EditActivity;
import com.example.yudystriawan.projectpapb.InsertActivity;
import com.example.yudystriawan.projectpapb.MainActivity;
import com.example.yudystriawan.projectpapb.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public void onBindViewHolder(@NonNull final ReviewViewHolder reviewViewHolder, final int i) {
        final Review review = reviewArrayList.get(i);

        reviewViewHolder.text_nama.setText(review.getNama());
        reviewViewHolder.text_review.setText(review.getKomentar());

        //DELETE REVIEW
        reviewViewHolder.tdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Add a new document with a generated ID

                db.collection(review.getDb())
                        .document(review.getIndeks())
                        .collection("LstReview")
                        .document(review.getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    reviewArrayList.remove(i);
                                    notifyItemRemoved(i);
                                    notifyDataSetChanged();
                                    Toast.makeText(view.getContext(), "Comment Deleted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(view.getContext(), "ERR. del document", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

        });

        //EDIT REVIEW
        reviewViewHolder.tedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(view.getContext(), EditActivity.class);
                intent.putExtra("DB", review.getDb());
                intent.putExtra("INDEKS", review.getIndeks());
                intent.putExtra("ID", review.getId());
                intent.putExtra("KOMENTAR", review.getKomentar());
                view.getContext().startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView text_nama, text_review, tdelete, tedit;
        public ImageView image_user;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            text_nama = itemView.findViewById(R.id.nama_user);
            text_review = itemView.findViewById(R.id.review_user);
            image_user = itemView.findViewById(R.id.image_user);
            tdelete = itemView.findViewById(R.id.delete);
            tedit = itemView.findViewById(R.id.edit);
        }
    }
}
