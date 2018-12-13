package com.example.yudystriawan.projectpapb;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditActivity extends Activity {
    String komentar, database, indeks, id, newKomentar;
    TextInputEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editText = findViewById(R.id.editText);

        Intent intent = getIntent();
        final Bundle b = intent.getExtras();
        database = b.getString("DB");
        indeks = b.getString("INDEKS");
        id = b.getString("ID");
        komentar = b.getString("KOMENTAR");
        editText.setText(komentar);
    }

    public void btnEdit(View view) {
        newKomentar = editText.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference reviewDb = db.collection(database)
                .document(indeks)
                .collection("LstReview")
                .document(id);
        reviewDb.update("Comment", newKomentar)
                .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
