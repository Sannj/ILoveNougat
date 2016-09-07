package com.example.sbadam2.pricechecker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class ProductPage extends AppCompatActivity {

    String productName;
    double finalPrice;
    String productUrl;
    TextView pName;
    TextView bName;
    TextView oPrice;
    TextView disc;
    TextView fPrice;
    TextView sixPmPrice;
    ImageView pImage;
    Product p;
    String targetStyleid;
    String targetProductId;
    double targetPrice;
    public final static String AUTH_KEY_6PM = "524f01b7e2906210f7bb61dcbe1bfea26eb722eb";
    boolean similarProductFound = false;
    boolean exactProductFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Intent intent = getIntent();
        p = intent.getParcelableExtra("product");
        pName = (TextView) findViewById(R.id.Title);
        bName = (TextView) findViewById(R.id.BrandNameValue);
        oPrice = (TextView) findViewById(R.id.OrigPriceValue);
        disc = (TextView) findViewById(R.id.DiscountValue);
        fPrice = (TextView) findViewById(R.id.FinalPriceValue);
        pImage = (ImageView) findViewById(R.id.PImage);
        targetProductId = p.productId;
        pName.setText(p.productName);
        pImage.setImageBitmap(p.photoId);
        bName.setText(p.brandName);
        String dis = p.discount;
        try {
            checkConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dis = dis.substring(0,1);
        int discount = Integer.parseInt(dis);
        TextView OrigPriceLabel = (TextView) findViewById(R.id.pOrigPrice);
        TextView DiscountLabel = (TextView) findViewById(R.id.pDiscount);
        TextView FinalPriceLabel = (TextView) findViewById(R.id.pFinalPrice);
        if(discount == 0){
            OrigPriceLabel.setText("Price :");
            DiscountLabel.setVisibility(View.GONE);
            FinalPriceLabel.setVisibility(View.GONE);
            oPrice.setText(p.origPrice);
        }
        else {
            OrigPriceLabel.setText("Original Price :");
            DiscountLabel.setVisibility(View.VISIBLE);
            FinalPriceLabel.setVisibility(View.VISIBLE);
            oPrice.setText(p.origPrice);
            disc.setText(p.discount);
            fPrice.setText(p.finalPrice);
        }
        targetStyleid = p.styleID;
        String temp = p.finalPrice;
        targetPrice = Double.parseDouble(temp.replaceAll("[\\D]", ""));
        sixPmPrice = (TextView) findViewById(R.id.priceOn6pm);


    }
    public void checkConnection() throws MalformedURLException, ExecutionException, InterruptedException {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Uri sixPmUri = new Uri.Builder().scheme("https").authority("api.6pm.com").path("Search").appendQueryParameter("term",targetProductId).appendQueryParameter("key", AUTH_KEY_6PM).appendQueryParameter("limit","25").build();
            new RestCallActivity().execute(sixPmUri.toString());
        } else {
            Toast.makeText(getApplicationContext(), "Please connect to internet and try again!", Toast.LENGTH_LONG).show();
        }
    }

    private class RestCallActivity extends AsyncTask<String, Void, JSONArray> {

        JSONObject jobj;
        JSONArray jarray;


        @Override
        protected JSONArray doInBackground(String... params) {

            try {
                jobj = sixPMCall(params[0]);
                jarray = jobj.getJSONArray("results");
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jbj = jarray.getJSONObject(i);
                    String productId = jbj.getString("productId");

                    if(productId.equals(targetProductId)){
                        similarProductFound = true;
                        String styleId = jbj.getString("styleId");
                        if(styleId.equals(targetStyleid)){
                            exactProductFound = true;
                        }
                        String price = jbj.getString("price");
                        finalPrice = Double.parseDouble(price.replaceAll("[$,]", ""));
                        if(finalPrice <= targetPrice){
                            productName = jbj.getString("productName");
                            productUrl = jbj.getString("productUrl");
                            break;
                        }
                    }
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
            if(exactProductFound == true){
                sixPmPrice.setText("Congratulations! This product is only for "+finalPrice+" on 6pm!! Checkout 6pm.com now!");
            }
            else if(similarProductFound == true){
                sixPmPrice.setText("6pm.com has similar products for a lesser price as low as $"+finalPrice+"! Check it out now!!");
            }
        }

        private JSONObject sixPMCall(String Zurl) throws IOException, JSONException {
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
                if(response!=200){
                    Toast.makeText(getApplicationContext(),"There was an issue with the request. Please restart the app.",Toast.LENGTH_LONG).show();
                }
                else {
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



