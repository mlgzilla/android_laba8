package com.example.laba2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ArrayList<String> myStringArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Bundle arguments = getIntent().getExtras();
        String role = arguments.get("role").toString();

        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText(role);

        ListView textList = findViewById(R.id.textList);

        Button buttonLang = findViewById(R.id.buttonLang);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        Button buttonRm = findViewById(R.id.buttonRm);

        myStringArray = new ArrayList<String>();
        ArrayAdapter<String> TextAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myStringArray);
        textList.setAdapter(TextAdapter);

        buttonLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonAdd.getText().toString().equals("Add")){
                    buttonAdd.setText("Добавить");
                    buttonRm.setText("Удалить");
                }
                else {
                    buttonAdd.setText("Add");
                    buttonRm.setText("Remove");
                }
                view.requestLayout();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStringArray.add("element " + myStringArray.size());
                view.requestLayout();
            }
        });
        buttonRm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myStringArray.size()!=0) {
                    myStringArray.remove(myStringArray.size()-1);
                    view.requestLayout();
                }
            }
        });

    }
}