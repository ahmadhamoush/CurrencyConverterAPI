package com.example.currencyconverterapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    String rate_url;
    int updated_rate;

    private EditText number;
    private CheckBox lbp;
    private CheckBox dollar;
    private TextView converted_text;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number = (EditText) findViewById(R.id.number);
        lbp = findViewById(R.id.lbp);
        dollar = findViewById(R.id.usd);
        converted_text = (TextView) findViewById(R.id.converted_text);

        // fetch rate at the start of the app to store the updated rate globally to be used
        fetch_rate();

    }



    public void convert(View v){
        if(dollar.isChecked() && lbp.isChecked()){
            Toast.makeText(getBaseContext(), "Please check only one currency", Toast.LENGTH_LONG).show();
        }
        else if(lbp.isChecked()){
            LBPtoDollar();
        }
        else if(dollar.isChecked()){
            DollarToLBP();
        }
        else{
            Toast.makeText(getBaseContext(), "Please check a currency", Toast.LENGTH_LONG).show();
        }
    }
    public void LBPtoDollar(){
        double input_number = Double.parseDouble(number.getText().toString());
        input_number /= updated_rate;
        converted_text.setText("Converted: " +String.valueOf(df.format(input_number)) +"$");
    }
    public void DollarToLBP(){
        double input_number = Double.parseDouble(number.getText().toString());
        input_number*= updated_rate;
        converted_text.setText("Converted: " +String.valueOf(df.format(input_number)) +"LBP");
    }


    public void fetch_rate() {
        rate_url = "http://172.20.10.3/currency_api/api1.php";

        getRate(rate_url);

    }


    public void getRate(String url){
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        updated_rate = Integer.parseInt(response.substring(9,14));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(error.getMessage(), "onErrorResponse: ");
                converted_text.setText("Failed to fetch rate");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}