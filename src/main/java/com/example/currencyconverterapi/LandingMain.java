package com.example.currencyconverterapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LandingMain extends AppCompatActivity {
    Button btn;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_main);

        btn = (Button) findViewById(R.id.convert2);
        name = (EditText) findViewById(R.id.name);

        animate(btn);
    }
    private void animate(View v){
        v.setAlpha(0f);
        v.setTranslationY(100);
        v.animate().alpha(1f).translationYBy(-100).setDuration(1500);
    }


    public void nextPage(View v){
        Intent obj = new Intent(getApplicationContext(), MainActivity.class);
        obj.putExtra("name", name.getText().toString());
        startActivity(obj);


    }
}