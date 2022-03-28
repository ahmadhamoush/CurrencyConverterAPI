package com.example.currencyconverterapi;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    String rate_url;
    String updated_rate;
    String currency;
    Button btn;

    private EditText amount;
    private CheckBox lbp;
    private CheckBox dollar;
    private TextView result;
    private TextView rate_text;
    private TextView name_intent;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private void animate(View v){
        v.setAlpha(0f);
        v.setTranslationY(100);
        v.animate().alpha(1f).translationYBy(-100).setDuration(1500);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amount = (EditText) findViewById(R.id.number);
        lbp = findViewById(R.id.lbp);
        dollar = findViewById(R.id.usd);
        result = (TextView) findViewById(R.id.converted_text);
        rate_text = (TextView) findViewById(R.id.rate_text);
        btn = (Button) findViewById(R.id.convert);
        name_intent= (TextView) findViewById(R.id.name_intent);

        Intent i = getIntent();
        String name = (String) i.getSerializableExtra("name");
        name_intent.setText("Hello " + name);

        animate(btn);
        animate(lbp);
        animate(dollar);
        animate(result);
        animate(rate_text);
        animate(amount);
        animate(findViewById(R.id.convert_to));
        animate(name_intent);



        // fetch rate at the start of the app to store the updated rate globally to be used
        fetch_rate();



    }

    public void convert(View v){
        if(dollar.isChecked() && lbp.isChecked()){
            Toast.makeText(getBaseContext(), "Please check only one currency", Toast.LENGTH_LONG).show();
            result.setText("Please check only one currency");
        }
        else if(lbp.isChecked()){
            currency = "USD";
        }
         else if(dollar.isChecked()){
            currency = "LBP";
        }
        else{
            Toast.makeText(getBaseContext(), "Please check a currency", Toast.LENGTH_LONG).show();

        }

        String url = "http://172.20.10.3/currency_api/api2.php";
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("Response:", response.toString());


                try{
                    JSONObject jObject = new JSONObject(response);
                    JSONObject converted = jObject.getJSONObject("converted");
                    String amount = converted.getString("amount");
                    String rate = converted.getString("rate");
                    String currency = converted.getString("currency");
                    if(currency.equals("LBP")){
                        result.setText("Converted: " + amount + " LBP");
                    }
                    else if(currency.equals("USD")){
                        result.setText("Converted: " + amount + " USD");
                    }

                }catch (JSONException e){
                    Log.e("Error", e.getMessage());
                }

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Log.e("Err:", error.getMessage());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("amount", amount.getText().toString());
                MyData.put("rate", updated_rate);
                MyData.put("currency", currency);
                return MyData;
            }
        };

        MyRequestQueue.add(MyStringRequest);

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

                        updated_rate = response.substring(9,14);
                        rate_text.setText("Updated Rate: $ = " + updated_rate+ " LBP");
                        if(updated_rate.equals("ching")){
                            rate_text.setText("Sorry, you are not allowed to check the rates.");
                            result.setText("Failed to fetch rate");
                            Log.e("code", "rest_cannot_get_rates");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(error.getMessage(), "onErrorResponse: ");
                result.setText("Failed to fetch rate");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void goBack (View v){
        Intent obj = new Intent(getApplicationContext(), LandingMain.class);
        startActivity(obj);
    }

}