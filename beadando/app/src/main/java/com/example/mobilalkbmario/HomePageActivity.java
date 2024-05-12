package com.example.mobilalkbmario;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;

public class HomePageActivity extends AppCompatActivity {
    private static final int CONTACTS_PERMISSION_REQUEST_CODE = 100;
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Find views
        ImageButton userIcon = findViewById(R.id.userIcon);
        Button square1 = findViewById(R.id.square1);
        Button square3 = findViewById(R.id.square3);
        Button changePasswordButton = findViewById(R.id.change_password_button);
        Button logoutButton = findViewById(R.id.logout_button);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout);

        // Check if contacts permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACTS_PERMISSION_REQUEST_CODE);
        }

        // Set click listeners
        userIcon.setOnClickListener(v -> {
            // Open UserProfileActivity
            if (drawerLayout.isDrawerOpen(findViewById(R.id.sidebarLayout))) {
                drawerLayout.closeDrawer(findViewById(R.id.sidebarLayout));
            } else {
                drawerLayout.openDrawer(findViewById(R.id.sidebarLayout));
            }
        });

        square1.setOnClickListener(v -> {
            // Open Square1Activity
            startActivity(new Intent(HomePageActivity.this, Square1ActivityCreate.class));
        });

        square3.setOnClickListener(v -> {
            // Open Square3Activity
            startActivity(new Intent(HomePageActivity.this, Square3ActivityRead.class));
        });

        // Set click listener for change password button
        changePasswordButton.setOnClickListener(v -> {
            // Implement logic to open ChangePasswordActivity
            startActivity(new Intent(HomePageActivity.this, ChangePasswordActivity.class));
        });

        logoutButton.setOnClickListener(v -> {
            // Perform logout operation
            FirebaseAuth.getInstance().signOut();
            // Redirect user to the login screen or wherever you want
            startActivity(new Intent(HomePageActivity.this, MainActivity.class));
            finish();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACTS_PERMISSION_REQUEST_CODE) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(this, "Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "You have been logged out.", Toast.LENGTH_SHORT).show();
    }
}
