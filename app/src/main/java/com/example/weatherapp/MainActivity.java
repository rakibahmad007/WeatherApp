package com.example.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private TextView tempTextView, minTextView, maxTextView, feelsTextView, humidityTextView, pressureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        tempTextView = findViewById(R.id.tempTextView);
        minTextView = findViewById(R.id.minTextView);
        maxTextView = findViewById(R.id.maxTextView);
        feelsTextView = findViewById(R.id.feelsTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        pressureTextView = findViewById(R.id.pressureTextView);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = searchEditText.getText().toString().trim();
                if (!city.isEmpty()) {
                    fetchWeatherData(city);
                } else {
                    Toast.makeText(MainActivity.this, "Enter a city name!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchWeatherData(String city) {
        String apiKey = "1119bc99cc4708987a7b8366b246fcb7";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appId=" + apiKey + "&units=metric";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject main = response.getJSONObject("main");
                            double temp = main.optDouble("temp", 0);
                            double tempMin = main.optDouble("temp_min", 0);
                            double tempMax = main.optDouble("temp_max", 0);
                            double feelsLike = main.optDouble("feels_like", 0);
                            int humidity = main.optInt("humidity", 0);
                            int pressure = main.optInt("pressure", 0);


                            tempTextView.setText(String.format("Temp: %.2f°C", temp));
                            minTextView.setText(String.format("Min: %.2f°C", tempMin));
                            maxTextView.setText(String.format("Max: %.2f°C", tempMax));
                            feelsTextView.setText(String.format("Feels: %.2f°C", feelsLike));
                            humidityTextView.setText(String.format("Humidity: %d%%", humidity));
                            pressureTextView.setText(String.format("Pressure: %d hPa", pressure));


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        queue.add(jsonObjectRequest);
    }
}
