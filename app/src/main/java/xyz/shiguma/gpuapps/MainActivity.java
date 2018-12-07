package xyz.shiguma.gpuapps;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterMethod;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity {

    private final String ConsumerKey = "Xstj6EdlszseIQ7rPiOprcW5O";
    private final String ConsumerSecret = "1wBMUqt9U6BTDopLnoLfCD1Sjp8U5Zv8QNqYQX8ljOdaClzlDl";
    private final String AccessToken = "2389446445-FAVjbX4JVOGfy2GOar9oRtsJMAS4syP6ov6AACG";
    private final String AccessTokenSecret = "LFtPkJwGseL2hmUVSylDe83nO1h58HGGPdyG5vEEJmazU";

    private final static ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }


        //twitter
        final AsyncTwitter asyncTwitter;
        try {
            asyncTwitter = executorService.submit(() -> {
                final Configuration conf = new ConfigurationBuilder()
                        .setOAuthConsumerKey(ConsumerKey)
                        .setOAuthConsumerSecret(ConsumerSecret)
                        .setOAuthAccessToken(AccessToken)
                        .setOAuthAccessTokenSecret(AccessTokenSecret)
                        .build();
                return new AsyncTwitterFactory(conf).getInstance();
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return;
        }

        asyncTwitter.addListener(new TwitterAdapter() {
            @Override
            public void onException(TwitterException e, TwitterMethod m) {
                e.printStackTrace();
            }
        });
        Button button = (Button) findViewById(R.id.Tweetbutton);
        EditText editText = findViewById(R.id.editText2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                asyncTwitter.updateStatus(text);
                editText.getEditableText().clear();
                Toast.makeText(MainActivity.this, "Tweeted", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
