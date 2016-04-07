package com.example.sri_harsha.project1;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sri Harsha on 4/2/2016.
 */
public class DetailActivity extends AppCompatActivity {

    public ImageView imageView;
    public TextView movie_name;
    public TextView movie_info;
    public TextView rating;
    public TextView release_date;

    public String position_json;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.detail_activity);


        imageView = (ImageView) findViewById(R.id.imageView);
        movie_name = (TextView) findViewById(R.id.movie_name);
        movie_info = (TextView) findViewById(R.id.movie_info);
        rating = (TextView) findViewById(R.id.rating);
        release_date = (TextView) findViewById(R.id.release_date);

        position_json = getIntent().getStringExtra("json");
        getMovieInfo(position_json);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }


    public void getMovieInfo(String info){
        try {

            JSONObject jsonObject = new JSONObject(info);

            Picasso.with(this)
                    .load("http://image.tmdb.org/t/p/w185/" + jsonObject.optString("poster_path").toString())
                    .into(imageView);
            movie_name.setText(jsonObject.optString("original_title").toString());
            movie_info.setText(jsonObject.optString("overview").toString());
            rating.append(jsonObject.optString("vote_average").toString());
            release_date.append(jsonObject.optString("release_date").toString());


        } catch (JSONException e) {
            Log.e("Json fragment", "JSON Exception", e);
        }

    }

}
