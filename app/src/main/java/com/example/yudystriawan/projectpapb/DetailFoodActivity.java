package com.example.yudystriawan.projectpapb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yudystriawan.projectpapb.Adapter.ReviewAdapter;
import com.example.yudystriawan.projectpapb.Data.Review;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DetailFoodActivity extends AppCompatActivity {

    private TextView textView, tReview, tAdd;
    private ImageView imageFood;
    private BottomNavigationView bottomNavigationView;

    private RecyclerView recyclerRev;
    private RecyclerView.Adapter reviewAdapter;
    String restoran,phone,id,tipeResto, jenisDatabase;
    double destLat,destLon,oriLat,oriLon;
    private FirebaseFirestore db;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);

        mContext = this;
        recyclerRev = findViewById(R.id.recycle_review);
        textView = findViewById(R.id.nama_restoran);
        bottomNavigationView = findViewById(R.id.bottom_nav_detail_food);
        imageFood = findViewById(R.id.image_food);
        tReview = findViewById(R.id.review);


        Intent intent = getIntent();
        final Bundle b = intent.getExtras();
        restoran = b.getString("DestName");
        phone = b.getString("PHONE");
        id = b.getString("ID");
        tipeResto = b.getString("Type");
        destLat = b.getDouble("DestLat");
        destLon = b.getDouble("DestLon");
        oriLat = b.getDouble("OriginLat");
        oriLon = b.getDouble("OriginLon");
        jenisDatabase = b.getString("Database");

        textView.setText(restoran);

        switch(tipeResto){
            case "Beverages":
                imageFood.setImageResource(R.drawable.beverages);
                break;
            case "Cake & Cookies":
                imageFood.setImageResource(R.drawable.cake);
                break;
            case "Chicken & Duck":
                imageFood.setImageResource(R.drawable.chicken);
                break;
            case "Chinese":
                imageFood.setImageResource(R.drawable.chinese);
                break;
            case "Rice Delights":
                imageFood.setImageResource(R.drawable.rice);
                break;
            case "Snack":
                imageFood.setImageResource(R.drawable.snack);
                break;
            case "Sweets & Desserts":
                imageFood.setImageResource(R.drawable.sweet);
                break;
            case "Bakso":
                imageFood.setImageResource(R.drawable.bakso);
                break;
            case "Coffee":
                imageFood.setImageResource(R.drawable.cofee);
                break;
            case "Fast Food":
                imageFood.setImageResource(R.drawable.fastfood);
                break;
            case "Pizza&Pasta":
                imageFood.setImageResource(R.drawable.pizza);
                break;
            case "Noodles":
                imageFood.setImageResource(R.drawable.noodles);
                break;
            case "Sate":
                imageFood.setImageResource(R.drawable.sate);
                break;
            case "Seafood":
                imageFood.setImageResource(R.drawable.seafood);
                break;
            case "Soto":
                imageFood.setImageResource(R.drawable.soto);
                break;
            case "Soup":
                imageFood.setImageResource(R.drawable.soup);
                break;
            case "Western":
                imageFood.setImageResource(R.drawable.western);
                break;
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent home = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.nav_call:
                        Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
                        startActivity(call);
                        break;
                    case R.id.nav_direction:
                        Intent direction = new Intent(getBaseContext(), MapsDirectionActivity.class);
                        direction.putExtra("OriginLat", oriLat);
                        direction.putExtra("OriginLon", oriLon);
                        direction.putExtra("DestLat", destLat);
                        direction.putExtra("DestLon", destLon);
                        direction.putExtra("DestName", restoran);
                        startActivity(direction);
                        break;
                    case R.id.nav_addComment:
                        Intent intent = new Intent(getBaseContext(), InsertActivity.class);
                        intent.putExtra("DB", jenisDatabase);
                        intent.putExtra("INDEKS", id);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        readRev(id, jenisDatabase);

    }

    public void readRev(final String indeks, final String database) {

        db = FirebaseFirestore.getInstance();

        db.collection(database)
                .document(indeks)
                .collection("LstReview")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String komentar, nama, idr;
                        String dbr = database;
                        String indeksR = indeks;
                        int sizeData = queryDocumentSnapshots.size();
                        ArrayList<Review> rev = new ArrayList<>();
                        if(sizeData != 0){
                            for (int i = 0; i < sizeData; i++) {
                                idr = queryDocumentSnapshots.getDocuments().get(i).getReference().getId();
                                komentar = queryDocumentSnapshots.getDocuments().get(i).get("Comment").toString();
                                nama = queryDocumentSnapshots.getDocuments().get(i).get("Username").toString();
                                rev.add(new Review(nama, komentar, idr, dbr, indeksR));
                            }
                        }
                        if (rev.size() != 0 ){
                            tReview.setText("Review");
                            recyclerRev.setHasFixedSize(true);
                            recyclerRev.setLayoutManager(new LinearLayoutManager(mContext));

                            reviewAdapter = new ReviewAdapter(LayoutInflater.from(mContext), rev);
                            recyclerRev.setAdapter(reviewAdapter);
                        }else{
                            tReview.setText("Tidak ada Review");
                        }
                    }
                });
    }
}
