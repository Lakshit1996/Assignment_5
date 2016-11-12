package com.example.lakshit.assignment_5;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String Event;
    String HTML;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView=(TextView)findViewById(R.id.textView2);

    }
    public void download(View view)
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo!=null && activeNetworkInfo.isConnected())
        {
            new eventupdate().execute();
        }
        else
        {
            textView.setText("No Network Connection Available...");
        }


    }

    private class eventupdate extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://www.iiitd.ac.in/about");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                String result = "";
//                publishProgress();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    result += line + "\n";
                    HTML = result;
//                    buffer.append(line + "\n");
                }

//                if (buffer.length() == 0) {
//                    return null;
//                }
                Log.d("entire_data", HTML);
                return HTML;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

        }

//        @Override
//        protected void onPreExecute()
//        {
//
//            progressDialog=new ProgressDialog(getApplicationContext());
//            progressDialog.setTitle("Download in Progress...");
//            progressDialog.setMax(100);
//            progressDialog.setProgress(0);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.show();
//        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Intent intent=new Intent(getApplicationContext(),Main2Activity.class);
            intent.putExtra("Data", result.substring(0,1000));
            startActivity(intent);
        }

//        @Override
//        protected void onProgressUpdate(Integer... params){
//                int progress=params[0];
//                progressDialog.setProgress(progress);
//            //Update a progress bar here, or ignore it, it's up to you
//        }

    }

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
