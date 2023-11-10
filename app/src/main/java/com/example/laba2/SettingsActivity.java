package com.example.laba2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    String userLogin;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.settings_activity);

        Bundle arguments = getIntent().getExtras();
        userLogin = arguments.get("userLogin").toString();

        Button submitButton = findViewById(R.id.submitButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button backButton = findViewById(R.id.backButton);

        EditText passwdText = findViewById(R.id.editTextPassword);
        Intent authIntent = new Intent(this, AuthActivity.class);
        Intent listIntent = new Intent(this, ListActivity.class);

        Toast myToast = new Toast(this);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String newPass = passwdText.getText().toString();
                        if (!newPass.equals("")) {
                            db.updatePass(db.findByLogin(userLogin), newPass);
                            Toast.makeText(SettingsActivity.this, "Pass was changed.", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(SettingsActivity.this, "Pass cant be empty.", Toast.LENGTH_LONG).show();

                    }
                }).start();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db.removeUser(userLogin);
                        startActivity(authIntent);
                    }
                }).start();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listIntent.putExtra("userLogin", userLogin);
                startActivity(listIntent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
