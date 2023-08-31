package com.example.ml_model_integration_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

     private TextView predictionResult;
     private EditText studentCgpa, studentIq, studentProfileScore;
     Button predictButton, resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        predictButton = findViewById(R.id.predict_button);
        studentCgpa = findViewById(R.id.student_cgpa);
        studentIq = findViewById(R.id.student_iq);
        studentProfileScore = findViewById(R.id.student_profile_score);
        predictionResult = findViewById(R.id.predict_result);
        resetButton = findViewById(R.id.reset_button);

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract values from EditText fields
                String cgpaValue = studentCgpa.getText().toString();
                String iqValue = studentIq.getText().toString();
                String profileScoreValue = studentProfileScore.getText().toString();

                // Execute the network operation in an AsyncTask
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            MediaType mediaType = MediaType.parse("text/plain");
                            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("cgpa", cgpaValue)
                                    .addFormDataPart("iq", iqValue)
                                    .addFormDataPart("profile_score", profileScoreValue)
                                    .build();
                            Request request = new Request.Builder()
                                    .url("https://student-placement-predictor-0560007ca730.herokuapp.com/predict")
                                    .method("POST", body)
                                    .build();
                            Response response = client.newCall(request).execute();
                            return response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(String response) {
                        // Process the response here
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String placement = jsonObject.getString("placement");

                                // Update UI based on the placement value
                                if (placement.equals("1")) {
                                    predictionResult.setText("Tera nhi hoga toh kiska hoga");
                                } else {
                                    predictionResult.setText("Never Give Up");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                           // emptyText();
                        } else {
                            // Handle error
                            Toast.makeText(MainActivity.this, "Error Problem in getting response", Toast.LENGTH_SHORT).show();
                            //emptyText();

                        }
                    }
                }.execute();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyText();
            }
        });

    }

    public void emptyText(){
        studentCgpa.setText("");
        studentIq.setText("");
        studentProfileScore.setText("");
        predictionResult.setText("");
    }
}