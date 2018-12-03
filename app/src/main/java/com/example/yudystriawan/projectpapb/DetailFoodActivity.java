package com.example.yudystriawan.projectpapb;

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
import android.widget.TextView;

import com.example.yudystriawan.projectpapb.Adapter.ReviewAdapter;
import com.example.yudystriawan.projectpapb.Data.Review;

import java.util.ArrayList;

public class DetailFoodActivity extends AppCompatActivity {

    private TextView textView;
    private BottomNavigationView bottomNavigationView;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);

        recyclerView = findViewById(R.id.recycle_review);
        textView = findViewById(R.id.nama_restoran);
        bottomNavigationView = findViewById(R.id.bottom_nav_detail_food);


        Intent intent = getIntent();
        final Bundle b = intent.getExtras();
        String restoran = (String)b.get("NAMA_RESTORAN");
        final String phone = (String)b.get("PHONE");
        String id = (String)b.get("ID");
        final String destLat = (String)b.get("LAT");
        final String destLon = (String)b.get("LONG");
        final String oriLat = String.valueOf(b.get("OriginLat"));
        final String oriLon = String.valueOf(b.get("OriginLon"));
        textView.setText(restoran);

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
                        Intent direction = new Intent(getBaseContext(), MapsPlacesActivity.class);
                        direction.putExtra("originLat", oriLat);
                        direction.putExtra("originLon", oriLon);
                        direction.putExtra("destLat", destLat);
                        direction.putExtra("destLon", destLon);
                        startActivity(direction);
                    default:
                        break;
                }
                return false;
            }
        });

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
