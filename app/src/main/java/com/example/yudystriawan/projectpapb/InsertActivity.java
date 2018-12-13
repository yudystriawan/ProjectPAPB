package com.example.yudystriawan.projectpapb;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InsertActivity extends Activity {
    String username, komentar, database, indeks;
    TextInputEditText insertUser, insertKomentar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        insertUser = findViewById(R.id.insertUser);
        insertKomentar = findViewById(R.id.insertKomentar);
        Intent intent = getIntent();
        final Bundle b = intent.getExtras();
        database = b.getString("DB");
        indeks = b.getString("INDEKS");

    }

    public void btnInsert(View view) {
        komentar = insertKomentar.getText().toString();
        username = insertUser.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("Username", username);
        user.put("Comment", komentar);

        // Add a new document with a generated ID
        Task<DocumentReference> documentReferenceTask = db.collection(database)
                .document(indeks)
                .collection("LstReview")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getBaseContext(), "Insert Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "ERR. add document", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
