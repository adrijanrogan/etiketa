package com.github.adrijanrogan.etiketa.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.github.adrijanrogan.etiketa.R;
import com.github.adrijanrogan.etiketa.ui.browse.BrowseActivity;


public class StartActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 9858;

    // Nimamo dovoljenja -> zahtevamo
    // sicer -> zazenemo BrowseActivity
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        int permissionCheck = ContextCompat.checkSelfPermission
                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            proceed();
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                proceed();
            } else {
                Toast.makeText(this,
                        "Prosimo sprejmite zahtevo za dostop do pomnilnika",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void proceed() {
        Intent intent = new Intent(this, BrowseActivity.class);
        startActivity(intent);
    }
}
