// Square3ActivityRead.java

package com.example.mobilalkbmario;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Square3ActivityRead extends AppCompatActivity {

    private static final String TAG = "Square3ActivityRead";

    private EditText etUserId;
    private EditText etSeverity;

    private FirebaseFirestore db;
    private IssueListAdapter adapter;
    private List<Issue> issueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square3_read);

        etUserId = findViewById(R.id.editTextUserId);
        etSeverity = findViewById(R.id.editTextSeverity);
        Button btnFilter = findViewById(R.id.buttonFilter);
        ListView listViewIssues = findViewById(R.id.listViewIssues);

        db = FirebaseFirestore.getInstance();
        issueList = new ArrayList<>();
        adapter = new IssueListAdapter(this, R.layout.issue_list_item, issueList);
        listViewIssues.setAdapter(adapter);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(Square3ActivityRead.this, HomePageActivity.class));
            finish(); // Optional: Finish the current activity to remove it from the back stack
        });


        btnFilter.setOnClickListener(v -> {
            String userId = etUserId.getText().toString().trim();
            String severity = etSeverity.getText().toString().trim();

            if (!severity.isEmpty() && !isNumeric(severity)) {
                Toast.makeText(Square3ActivityRead.this, "Severity must be a number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(userId) && TextUtils.isEmpty(severity)) {
                getIssues();
            } else if (!TextUtils.isEmpty(userId) && !severity.isEmpty()) {
                filterIssuesByUserAndSeverity(userId, severity);
            } else if (!TextUtils.isEmpty(userId) && severity.isEmpty()) {
                filterIssuesByUser(userId);
            } else if (TextUtils.isEmpty(userId) && !severity.isEmpty()) {
                filterIssuesBySeverity(severity);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getIssues();
    }

    private void getIssues() {
        db.collection("issues")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    issueList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Issue issue = document.toObject(Issue.class);
                        issueList.add(issue);
                    }
                    adapter.notifyDataSetChanged();

                    // Send notification after data retrieval is successful
                    NotificationHelper.sendNotification(this, "List Updated", "Issues list has been updated.");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting issues", e);
                    Toast.makeText(Square3ActivityRead.this, "Failed to get issues: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



    private void filterIssuesByUser(String userId) {
        db.collection("issues")
                .whereEqualTo("reportedBy", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    issueList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Issue issue = document.toObject(Issue.class);
                        issueList.add(issue);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error filtering issues by user", e);
                    Toast.makeText(Square3ActivityRead.this, "Failed to filter issues: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Method to filter issues by severity only
    private void filterIssuesBySeverity(String severity) {
        // Convert severity to integer
        int severityValue = Integer.parseInt(severity);

        db.collection("issues")
                .whereEqualTo("severity", severityValue)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    issueList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Issue issue = document.toObject(Issue.class);
                        issueList.add(issue);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error filtering issues by severity", e);
                    Toast.makeText(Square3ActivityRead.this, "Failed to filter issues: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void filterIssuesByUserAndSeverity(String userId, String severity) {
        // Convert severity to integer
        int severityValue = Integer.parseInt(severity);

        db.collection("issues")
                .whereEqualTo("reportedBy", userId)
                .whereEqualTo("severity", severityValue)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    issueList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Issue issue = document.toObject(Issue.class);
                        issueList.add(issue);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error filtering issues by user and severity", e);
                    Toast.makeText(Square3ActivityRead.this, "Failed to filter issues: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    // Method to check if a string is numeric
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}

