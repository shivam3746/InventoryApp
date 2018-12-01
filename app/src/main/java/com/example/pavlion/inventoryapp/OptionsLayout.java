package com.example.pavlion.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class OptionsLayout extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_layout);

        ImageView drama = findViewById(R.id.category1);
        ImageView science = findViewById(R.id.category2);
        ImageView romance = findViewById(R.id.category3);
        ImageView adventure = findViewById(R.id.category4);


       drama.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i1=new Intent(OptionsLayout.this,EditorActivity.class);
               i1.putExtra("Type",getResources().getString(R.string.book_type_1));
               startActivity(i1);
           }
       });
        romance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2=new Intent(OptionsLayout.this,EditorActivity.class);
                i2.putExtra("Type",getResources().getString(R.string.book_type_3));
                startActivity(i2);
                }
        });
        science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3=new Intent(OptionsLayout.this,EditorActivity.class);
                i3.putExtra("Type",getResources().getString(R.string.book_type_2));
                startActivity(i3);
            }
        });
        adventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i4 = new Intent(OptionsLayout.this, EditorActivity.class);
                i4.putExtra("Type", getResources().getString(R.string.book_type_4));
                startActivity(i4);
            }
        });
    }

}
