package com.example.mobilalkbmario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Square1ActivityCreate extends AppCompatActivity {

    private static final String TAG = "Square1ActivityCreate";

    private EditText etName, etSeverity, etDescription;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square1_create);

        etName = findViewById(R.id.editTextName);
        etSeverity = findViewById(R.id.editTextSeverity);
        etDescription = findViewById(R.id.editTextDescription);
        btnCreate = findViewById(R.id.buttonCreate);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(Square1ActivityCreate.this, HomePageActivity.class));
            finish(); // Optional: Finish the current activity to remove it from the back stack
        });

        btnCreate.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String severity = etSeverity.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (!name.isEmpty() && !severity.isEmpty() && !description.isEmpty()) {
                createIssue(name, severity, description);
            } else {
                Toast.makeText(Square1ActivityCreate.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createIssue(String name, String severity, String description) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Generate a unique ID on the client-side
        String documentId = UUID.randomUUID().toString();

        Map<String, Object> issueData = new HashMap<>();
        issueData.put("name", name);

        // Parse severity as integer
        int severityValue = 0; // Default value if parsing fails
        try {
            severityValue = Integer.parseInt(severity);
        } catch (NumberFormatException e) {
            // Severity is not a valid number, handle error if needed
            Log.e(TAG, "Invalid severity value: " + severity);
            // You may show a message to the user indicating that severity must be a number
            return;
        }
        issueData.put("severity", severityValue);
        issueData.put("description", description);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            issueData.put("reportedBy", userEmail);
        } else {
            issueData.put("reportedBy", "current_user_id");
        }

        // Add the document with the generated ID
        db.collection("issues").document(documentId)
                .set(issueData)
                .addOnSuccessListener(documentReference -> {
                    // Issue created successfully
                    Log.d(TAG, "Issue added with ID: " + documentId);
                    Toast.makeText(Square1ActivityCreate.this, "Issue created successfully", Toast.LENGTH_SHORT).show();
                    // You can navigate to another activity or perform additional actions here if needed
                    Intent intent = new Intent(Square1ActivityCreate.this, HomePageActivity.class);
                    intent.putExtra("issueId", documentId);
                    intent.putExtra("name", name);
                    intent.putExtra("severity", severity);
                    intent.putExtra("description", description);
                    startActivity(intent);

                })
                .addOnFailureListener(e -> {
                    // Error occurred while adding issue
                    Log.w(TAG, "Error adding issue", e);
                    Toast.makeText(Square1ActivityCreate.this, "Failed to create issue", Toast.LENGTH_SHORT).show();
                    // Handle error appropriately
                });
    }
}
