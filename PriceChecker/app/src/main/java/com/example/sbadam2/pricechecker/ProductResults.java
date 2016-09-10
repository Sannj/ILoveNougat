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
        alertDialog.setTitle("Alert");
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
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
            Uri zapposUri = new Uri.Builder().scheme("https").authority("api.zappos.com").path("Search").appendQueryParameter("term", searchTerm).appendQueryParameter("key", AUTH_KEY_ZAPPOS).appendQueryParameter("limit", "25").build();
            new RestCallActivity().execute(zapposUri.toString());
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_toast), Toast.LENGTH_LONG).show();
        }
    }


    private class RestCallActivity extends AsyncTask<String, Void, JSONArray> {

        JSONObject jobj;
        JSONArray jarray;

        @Override
        protected void onPreExecute() {
            progressBar.setMax(10);
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            try {
                jobj = zapposCall(params[0]);
                jarray = jobj.getJSONArray("results");
                if(jarray.length() == 0){
                    foundResults = false;
                }
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jbj = jarray.getJSONObject(i);
                    String productName = jbj.getString("productName");
                    String brandName = jbj.getString("brandName");
                    String origPrice = jbj.getString("originalPrice");
                    String finalPrice = jbj.getString("price");
                    String discount = jbj.getString("percentOff");
                    String productId = jbj.getString("productId");
                    String imageUrl = jbj.getString("thumbnailImageUrl");
                    URL url = new URL(jbj.getString("thumbnailImageUrl"));
                    String styleId = jbj.getString("styleId");
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    String productUrl = jbj.getString("productUrl");
                    Product p = new Product(productName, brandName, origPrice, finalPrice, discount, bmp, productUrl, imageUrl, styleId, productId);
                    products.add(p);
                    publishProgress();

                }
            } catch (IOException ex) {
                ex.getLocalizedMessage();
            } catch (JSONException e) {
                e.getLocalizedMessage();
            }
            return jarray;
        }

        @Override
        protected void onPostExecute(JSONArray jarray) {
            if(foundResults == false){
                alertDialog.show();
            }
            else {
                progressBar.setVisibility(View.GONE);
                adapter = new RecyclerViewAdapter(ProductResults.this, products);
                recyclerView.setAdapter(adapter);
            }

        }


        private JSONObject zapposCall(String Zurl) throws IOException, JSONException {
            InputStream is = null;
            JSONObject jobj = null;
            try {
                URL url = new URL(Zurl);
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
                    jobj = new JSONObject(responseBuilder.toString());
                }

            } finally {
                if (is != null) {
                    is.close();
                }
            }
            return jobj;
        }

    }

}


