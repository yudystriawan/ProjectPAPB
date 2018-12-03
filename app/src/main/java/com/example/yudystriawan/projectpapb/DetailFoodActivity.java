package com.example.yudystriawan.projectpapb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.yudystriawan.projectpapb.Adapter.FoodAdapter;
import com.example.yudystriawan.projectpapb.Adapter.ReviewAdapter;
import com.example.yudystriawan.projectpapb.Data.Restoran;
import com.example.yudystriawan.projectpapb.Data.Review;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DetailFoodActivity extends AppCompatActivity {

    private TextView textView, textView2;

    private RecyclerView recyclerRev;
    private RecyclerView.Adapter reviewAdapter;
    private FirebaseFirestore db;
    ArrayList<Review> review = new ArrayList<>();
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);

        mContext = this;
        recyclerRev = findViewById(R.id.recycle_review);
        textView = findViewById(R.id.nama_restoran);
        textView2 = findViewById(R.id.nama_restoran2);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String restoran = (String)b.get("NAMA_RESTORAN");
        String id = (String)b.get("ID");
        String tlat = (String)b.get("LAT");
        String tlong = (String)b.get("LONG");
        String oriLat = String.valueOf(b.get("OriginLat"));
        String oriLon = String.valueOf(b.get("OriginLon"));
        textView.setText(restoran);

        readRev(id);
        //getReviewList();

    }


    private void readRev(String indeks) {

        db = FirebaseFirestore.getInstance();

        db.collection("DaftarMakananSample")
                .document(indeks)
                .collection("LstReview")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String komentar, nama;
                        int sizeData = queryDocumentSnapshots.size();
                        if(sizeData != 0){
                            for (int i = 0; i < sizeData; i++) {
                                komentar = queryDocumentSnapshots.getDocuments().get(i).get("Username").toString();
                                nama = queryDocumentSnapshots.getDocuments().get(i).get("Comment").toString();
                                review.add(new Review(nama, komentar));
                            }
                            recyclerRev.setHasFixedSize(true);
                            recyclerRev.setLayoutManager(new LinearLayoutManager(mContext));

                            reviewAdapter = new ReviewAdapter(LayoutInflater.from(mContext), review);
                            recyclerRev.setAdapter(reviewAdapter);
                        }
                    }
                });

    }

}
