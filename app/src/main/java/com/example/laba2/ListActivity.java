package com.example.laba2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
        EditText editText = findViewById(R.id.editText);

        myStringArray = new ArrayList<>();
        ArrayList<String> selectedElements = new ArrayList<>();
        ArrayAdapter TextAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, myStringArray);
        textList.setAdapter(TextAdapter);

        textList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                String element = (String) TextAdapter.getItem(position);
                if (textList.isItemChecked(position))
                    selectedElements.add(element);
                else
                    selectedElements.remove(element);
            }
        });

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
                TextAdapter.notifyDataSetChanged();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String elementName = editText.getText().toString();
                TextAdapter.add(elementName);
                editText.setText("");
                TextAdapter.notifyDataSetChanged();
            }
        });
        buttonRm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myStringArray.size()!=0) {
                    textList.clearChoices();
                    for(int i=0; i < selectedElements.size();i++)
                    {
                        TextAdapter.remove(selectedElements.get(i));
                    }
                    selectedElements.clear();
                    TextAdapter.notifyDataSetChanged();
                }
            }
        });

    }
}