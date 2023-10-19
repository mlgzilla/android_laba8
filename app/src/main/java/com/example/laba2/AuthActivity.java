package com.example.laba2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AuthActivity extends AppCompatActivity {
    String role;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.auth_activity);
        role = "Guest";

        Button submitButton = findViewById(R.id.submitButton);
        Button regButton = findViewById(R.id.regButton);
        EditText loginText = findViewById(R.id.editTextLogin);
        EditText passwdText = findViewById(R.id.editTextPassword);
        Toast myToast = new Toast(this);
        Intent openIntent = new Intent(this, ListActivity.class);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer userId = db.authUser(loginText.getText().toString(), passwdText.getText().toString());
                if (userId != null) {
                    openIntent.putExtra("userId", userId);
                    startActivity(openIntent);
                } else
                    Toast.makeText(AuthActivity.this, "Wrong login or password, try again.", Toast.LENGTH_LONG).show();
            }
        });
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setLogin(loginText.getText().toString());
                user.setPass(passwdText.getText().toString());

            }
        });
    }
}
