package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private Request request;
    private ActivityMainBinding binding;
    private SharedPreferences pref;
    private OkHttpClient client = new OkHttpClient();
    private EditText epsip;
    private Button bSave;
    private Button bLed1, bLed2, bLed3;
    private ImageButton Up, Down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("MyPref",MODE_PRIVATE);


        epsip = (EditText) findViewById(R.id.epsip);
        bSave = (Button) findViewById(R.id.bSave);
        bLed1 = (Button) findViewById(R.id.bLed1);
        bLed2 = (Button) findViewById(R.id.bLed2);
        bLed3 = (Button) findViewById(R.id.bLed3);
        Up = (ImageButton) findViewById(R.id.UpButton);
        Down = (ImageButton) findViewById(R.id.DownButton);
        get_ip();

         View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.bLed1:
                    {
                        post("led1");
                        break;
                    }
                    case R.id.bLed2:
                    {
                        post("led2");
                        break;
                    }
                    case R.id.bLed3:
                    {
                        post("led3");
                        break;
                    }
                }



            }
        };

        View.OnClickListener onTouchListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId())
                {
                    case R.id.UpButton: {

                            post("Up");
                        try {
                            Thread.sleep(1000);
                        } catch(InterruptedException ex) {}
                            break;

                    }
                    case R.id.DownButton:
                    {
                            post("Down");
                        try {
                            Thread.sleep(1000);
                        } catch(InterruptedException ex) {}
                        break;
                    }
                }

            }
        };

        Up.setOnClickListener(onTouchListener);
        Down.setOnClickListener(onTouchListener);

        bLed1.setOnClickListener(onClickListener);
        bLed2.setOnClickListener(onClickListener);
        bLed3.setOnClickListener(onClickListener);

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!epsip.getText().equals(null))
                {
                    save_ip(String.valueOf(epsip.getText()));
                }
            }
        });


    }

    private void save_ip(String ip)
    {
        SharedPreferences.Editor ed = pref.edit();
        ed.putString("ip", ip);
        ed.apply();
    }

    private void get_ip()
    {
        String ip = pref.getString("ip", "");
        if(ip!=null)
        {
            if(!ip.isEmpty())
            {
                epsip.setText(ip);
            }
        }
    }

    private void post(String post)
    {
        Thread t = new Thread(() -> {
            request = new Request.Builder().url("http://"+epsip.getText()+"/"+post).build();
            try{
                Response response = client.newCall(request).execute();
                if(response.isSuccessful())
                {
                    String resultText = response.body().toString();
                }

            }
            catch(IOException e)
            {}
        });
        t.setDaemon(true);
        t.start();
    }
}