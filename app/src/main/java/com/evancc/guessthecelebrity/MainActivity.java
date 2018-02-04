package com.evancc.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    String myPage;
    List<String> pictureUrls = new ArrayList<String>();
    List<String> names = new ArrayList<String>();
    String celeb = "";
    String picture = "";
    ImageView imageView = null;

    public class DownloadPage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = null;
            URL url;
            HttpURLConnection urlConnection;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1) {
                    char currentChar = (char) data;

                    result += currentChar;

                    data = reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;

            }
        }
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream in = httpURLConnection.getInputStream();
                Bitmap image = BitmapFactory.decodeStream(in);

                return image;
            } catch (Exception e) {
                return null;
            }

        }


    }

    public void getCelebritiesDetails() {

        //Method to get URLs and Alt tags for celebrities

        Pattern p = Pattern.compile("<meta(.*?)></meta>");
        Matcher m = p.matcher(myPage);
        //Populating the pictureUrls array
        while(m.find()) {
            //Log.i("Image URL", m.group(1));
            pictureUrls.add(m.group(1));
        }

        p = Pattern.compile("alt=\"(.*?)\"");
        m = p.matcher(myPage);
        //Populating the names array
        while(m.find()) {
            names.add(m.group(1));
        }
    }

    public void createQuestion() {
        Random r = new Random();
        int currentNum = r.nextInt(80) + 0;
        int num = 0;
        int cnt = 0;
        int buttonNum = 0;
        DownloadImage downloadImage = new DownloadImage();

        celeb = names.get(currentNum);
        picture = pictureUrls.get(currentNum);

        try {
            Bitmap bitmap = downloadImage.execute(picture).get();
            imageView.setImageBitmap(bitmap);

            while(cnt < 3) {
                buttonNum = r.nextInt(3) + 0;
                num = r.nextInt(80) + 0;
                if(num != currentNum) {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void selectAnswer(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);

        DownloadPage page = new DownloadPage();
        try {
            myPage = page.execute("http://www.posh24.se/kandisar").get();
            Log.i("Webpage", myPage);
            getCelebritiesDetails();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
