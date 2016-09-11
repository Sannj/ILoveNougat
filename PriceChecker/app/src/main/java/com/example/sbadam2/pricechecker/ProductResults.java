package com.example.sbadam2.pricechecker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;


public class ProductResults extends AppCompatActivity {

    public final static String AUTH_KEY_ZAPPOS = "b743e26728e16b81da139182bb2094357c31d331";

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ArrayList<Product> products = new ArrayList<Product>();
    ProgressBar progressBar;
    boolean foundResults = true;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_results);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        alertDialog = new AlertDialog.Builder(ProductResults.this).create();
        alertDialog.setTitle(getResources().getString(R.string.alert_title_product_results_activity));
        alertDialog.setMessage(getResources().getString(R.string.no_products_alert_product_results_activity));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.alert_ok_button_product_results_activity),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                });

        try {
            checkConnection();
        } catch (MalformedURLException|ExecutionException|InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void checkConnection() throws MalformedURLException, ExecutionException, InterruptedException {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String searchTerm = extras.getString("SEARCH_TERM");
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Uri zapposUri = new Uri.Builder().scheme("https").authority("api.zappos.com").path("Search")
                    .appendQueryParameter("term", searchTerm)
                    .appendQueryParameter("key", AUTH_KEY_ZAPPOS)
                    .appendQueryParameter("limit", "25")
                    .build();
            new RestCallActivity().execute(zapposUri.toString());
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(
                    R.string.no_internet_toast), Toast.LENGTH_LONG).show();
        }
    }


    private class RestCallActivity extends AsyncTask<String, Void, JSONArray> {

        JSONObject jObj;
        JSONArray jArray;

        @Override
        protected void onPreExecute() {
            progressBar.setMax(10);
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            try {
                jObj = zapposCall(params[0]);
                jArray = jObj.getJSONArray("results");
                if(jArray.length() == 0){
                    foundResults = false;
                }
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jTempObj = jArray.getJSONObject(i);
                    String productName = jTempObj.getString("productName");
                    String brandName = jTempObj.getString("brandName");
                    String origPrice = jTempObj.getString("originalPrice");
                    String finalPrice = jTempObj.getString("price");
                    String discount = jTempObj.getString("percentOff");
                    String productId = jTempObj.getString("productId");
                    String imageUrl = jTempObj.getString("thumbnailImageUrl");
                    URL url = new URL(jTempObj.getString("thumbnailImageUrl"));
                    String styleId = jTempObj.getString("styleId");
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    String productUrl = jTempObj.getString("productUrl");
                    Product p = new Product(productName, brandName, origPrice, finalPrice, discount, bmp, productUrl, imageUrl, styleId, productId);
                    products.add(p);
                    publishProgress();
                }
            } catch (IOException|JSONException ex) {
                ex.getLocalizedMessage();
            }
            return jArray;
        }

        @Override
        protected void onPostExecute(JSONArray jArray) {
            if(!foundResults){
                alertDialog.show();
            }
            else {
                progressBar.setVisibility(View.GONE);
                adapter = new RecyclerViewAdapter(ProductResults.this, products);
                recyclerView.setAdapter(adapter);
            }
        }


        private JSONObject zapposCall(String zUrl) throws IOException, JSONException {
            InputStream is = null;
            JSONObject jObj = null;
            try {
                URL url = new URL(zUrl);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.connect();
                int response = conn.getResponseCode();
                if (response != 200) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.rest_call_failed), Toast.LENGTH_LONG).show();
                } else {
                    is = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    StringBuilder responseBuilder = new StringBuilder(2048);
                    String resStr;
                    while ((resStr = br.readLine()) != null) {
                        responseBuilder.append(resStr);
                    }
                    jObj = new JSONObject(responseBuilder.toString());
                }
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            return jObj;
        }
    }

}


