package com.example.yudystriawan.projectpapb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.yudystriawan.projectpapb.Adapter.FoodAdapter;
import com.example.yudystriawan.projectpapb.Adapter.ReviewAdapter;
import com.example.yudystriawan.projectpapb.Data.Review;

import java.util.ArrayList;

public class DetailFoodActivity extends AppCompatActivity {

    private TextView textView;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);

        recyclerView = findViewById(R.id.recycle_review);
        textView = findViewById(R.id.nama_restoran);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String restoran = (String)b.get("NAMA_RESTORAN");
        String id = (String)b.get("ID");
        String tlat = (String)b.get("LAT");
        String tlong = (String)b.get("LONG");
        textView.setText(restoran+"---"+id);

        getReviewList();

    }

    private void getReviewList() {
        ArrayList<Review> reviews = new ArrayList<>();
        reviews.add(new Review("NAMA1", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis,"));
        reviews.add(new Review("NAMA2", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis,"));
        reviews.add(new Review("NAMA3", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis,"));
        reviews.add(new Review("NAMA4", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis,"));
        reviews.add(new Review("NAMA5", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis,"));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reviewAdapter = new ReviewAdapter(LayoutInflater.from(this), reviews);
        recyclerView.setAdapter(reviewAdapter);
    }
}
