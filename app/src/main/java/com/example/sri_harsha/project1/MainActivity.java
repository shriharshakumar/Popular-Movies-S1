package com.example.sri_harsha.project1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public String sort_mode="unsorted";
    public List<String> urll = new ArrayList<String>();
    public GridView gridview;
    public String baseUrl;
    public String apiKey = "02905bb18b5b26adf05d70a6bb9bc6a3";
    public Retrieve retrieve;
    public JSONObject jsonRootObject;
    public JSONArray jsonArray;
    public TextView sort_meth;
    ImageAdapter adapter;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort_popular) {
            sort_mode = "popular";
            sort_meth.setText("Sorted by Popularity");
            baseUrl = "http://api.themoviedb.org/3/movie/".concat(sort_mode).concat("?api_key=");
            retrieve = new Retrieve();
            retrieve.execute(baseUrl.concat(apiKey));
            return true;
        }

        if (id == R.id.sort_rate) {
            sort_mode = "top_rated";
            sort_meth.setText("Sorted by Ratings");
            baseUrl = "http://api.themoviedb.org/3/movie/".concat(sort_mode).concat("?api_key=");
            retrieve = new Retrieve();
            retrieve.execute(baseUrl.concat(apiKey));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sort_meth = (TextView) findViewById(R.id.sort_meth);

        //Loading image from below url into imageView
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                try {
                    intent.putExtra("json", jsonArray.getJSONObject(position).toString());
                } catch (JSONException e) {
                    Log.e("Start intent", "JSON Array", e);
                }
                startActivity(intent);
            }
        });


        if (sort_mode.equals("unsorted")) {
            sort_mode = "popular";
        }
        baseUrl = "http://api.themoviedb.org/3/movie/".concat(sort_mode).concat("?api_key=");
        retrieve = new Retrieve();
        retrieve.execute(baseUrl.concat(apiKey));
    }




    public class Retrieve extends AsyncTask<String, Void, String> {

        String MovieJsonStr;

        protected String doInBackground(String... urls) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(urls[0]);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                MovieJsonStr = buffer.toString();
                return MovieJsonStr;
            }
            catch (IOException e) {

                Log.e("Retrieve", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }
            finally {
                if (urlConnection != null || urlConnection == null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {

                    try {

                        reader.close();

                    }
                    catch (final IOException e) {
                        Log.e("Retrieve", "Error closing stream", e);
                    }
                }
            }
        }

        protected void onPostExecute(String result) {
            getPoster(result);
        }
    }

        public void getPoster(String mJsonStr) {
        try {
            if(mJsonStr == null){
                sort_meth.setText("Internet Not Available");
                new AlertDialog.Builder(this).setTitle("Error").setMessage("Internet not Available").show();
                return;
            }
            jsonRootObject = new JSONObject(mJsonStr);

            //Get the instance of JSONArray that contains JSONObjects
            jsonArray = jsonRootObject.optJSONArray("results");

            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);


                urll.add(i, jsonObject.optString("poster_path").toString());


            }
            adapter = new ImageAdapter(this, urll);
            gridview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e("Json fragment", "JSON Exception", e);
        }
    }


}
