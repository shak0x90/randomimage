package com.beingcoderz.randomimage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.Random;



public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Random random;
    int randimint;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DownloadManager manager;
    private static final String API = "https://picsum.photos/seed/";
    private static final String HEIGHT_WIDTH = "/400/600";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);


        sharedPreferences = getSharedPreferences("save",MODE_PRIVATE);
        editor = sharedPreferences.edit();



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                random = new Random();
                randimint = random.nextInt(500);
                if (isNetworkConnected()){
                    fetchImage(randimint);
                }else{
                    Toast.makeText(getApplicationContext(),"No connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void fetchImage(int random){
        Glide.with(this).load(API+random+HEIGHT_WIDTH).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        editor.putString("offline_fetch",API+random+HEIGHT_WIDTH);
        editor.apply();
    }


    @Override
    protected void onStart() {
        super.onStart();
        offlinefetchImage();
    }

    private void offlinefetchImage(){
        Glide.with(this).load(sharedPreferences.getString("offline_fetch","default")).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}