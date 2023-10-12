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

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.auth_activity);
        role = "Guest";

        Button submitButton = findViewById(R.id.submitButton);
        EditText passwdText = findViewById(R.id.editTextPassword);
        Toast myToast = new Toast(this);
        Intent openIntent = new Intent(this, ListActivity.class);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (passwdText.getText().toString()){
                    case "123456":{
                        openIntent.putExtra("role", "Employee");
                        startActivity(openIntent);
                        return;

                    }
                    case "qwerty":{
                        openIntent.putExtra("role", "Admin");
                        startActivity(openIntent);
                        return;
                    }
                    default:{
                        myToast.makeText(AuthActivity.this,
                                "Invalid Password", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

}
