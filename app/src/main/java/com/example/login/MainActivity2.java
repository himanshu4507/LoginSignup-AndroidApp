package com.example.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity {

    TextView signup_text;
    EditText password,phonenumber;
    Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        password=findViewById(R.id.pass1);
        phonenumber=findViewById(R.id.phonenumber);
        btn1=findViewById(R.id.btn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phonenumber.getText().toString().isEmpty() && password.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity2.this, "Please enter both the values", Toast.LENGTH_SHORT).show();
                    return;
                }
                postData(phonenumber.getText().toString(),password.getText().toString());
            }
        });



        signup_text=findViewById(R.id.signup_text);
        String text=getString(R.string.new_here_sign_up);
        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent =new Intent(MainActivity2.this,MainActivity.class);
                startActivity(intent);
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false); // Remove underline
                ds.setColor(Color.parseColor("#1983FF")); // Set text color
            }
        };
       int start=text.indexOf("Sign Up");
       int end=start+ "Sign Up".length();
       spannableString.setSpan(clickableSpan,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        signup_text.setText(spannableString);
        signup_text.setMovementMethod(LinkMovementMethod.getInstance());
        signup_text.setHighlightColor(Color.TRANSPARENT);
    }
    private void postData(String email, String pass)
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                // as we are sending data in json format so
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        // below line is to create an instance for our retrofit api class.
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        // passing data from our text fields to our modal class.
        DataModel modal = new DataModel(email, pass);

        // calling a method to create a post and passing our modal class.
        Call<ResponseModel> call = retrofitAPI.createPost(modal);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                // this method is called when we get response from our api.
                if(response.code() == 200){
                Toast.makeText(MainActivity2.this, "Data added to API", Toast.LENGTH_SHORT).show();



                // on below line we are setting empty text
                // to our both edit text.
                phonenumber.setText("");
                password.setText("");

                    ResponseModel responseFromAPI = response.body();
                    Log.d("Response", responseFromAPI.toString());

                // we are getting response from our body
                // and passing it to our modal class.
                Log.d("Response", response.toString());

                    Toast.makeText(MainActivity2.this, "token:"+responseFromAPI.getToken(), Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(MainActivity2.this, "Error code: " + response.code(), Toast.LENGTH_SHORT).show();
                }



            }



            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                Toast.makeText(MainActivity2.this, "Connection Problem", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

