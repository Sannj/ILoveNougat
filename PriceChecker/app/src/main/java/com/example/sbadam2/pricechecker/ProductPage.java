package com.example.sbadam2.pricechecker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class ProductPage extends AppCompatActivity {

    public final static String AUTH_KEY_6PM = "524f01b7e2906210f7bb61dcbe1bfea26eb722eb";
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
    boolean similarProductFound = false;
    boolean exactProductFound = false;
    Button navigateButton;
    String targetUrlOn6pm;
    String zapposProductUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Intent intent = getIntent();
        p = intent.getParcelableExtra("product");
        pName = (TextView) findViewById(R.id.Title);
        navigateButton = (Button) findViewById(R.id.navigateButton);
        bName = (TextView) findViewById(R.id.BrandNameValue);
        oPrice = (TextView) findViewById(R.id.OrigPriceValue);
        disc = (TextView) findViewById(R.id.DiscountValue);
        fPrice = (TextView) findViewById(R.id.FinalPriceValue);
        pImage = (ImageView) findViewById(R.id.PImage);
        targetProductId = p.getProductId();
        pName.setText(p.getProductName());
        pImage.setImageBitmap(p.getPhotoId());
        bName.setText(p.getBrandName());
        zapposProductUrl = p.getProductUrl();
        String dis = p.getDiscount();
        try {
            checkConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dis = dis.substring(0, 1);
        int discount = Integer.parseInt(dis);
        TextView OrigPriceLabel = (TextView) findViewById(R.id.pOrigPrice);
        TextView DiscountLabel = (TextView) findViewById(R.id.pDiscount);
        TextView FinalPriceLabel = (TextView) findViewById(R.id.pFinalPrice);
        if (discount == 0) {
            OrigPriceLabel.setText(getResources().getString(R.string.original_price_as_final_price_label_product_page_activity));
            DiscountLabel.setVisibility(View.GONE);
            FinalPriceLabel.setVisibility(View.GONE);
            oPrice.setText(p.getOrigPrice());
        } else {
            OrigPriceLabel.setText(getResources().getString(R.string.original_price_label_product_page_activity));
            DiscountLabel.setVisibility(View.VISIBLE);
            FinalPriceLabel.setVisibility(View.VISIBLE);
            oPrice.setText(p.getOrigPrice());
            disc.setText(p.getDiscount());
            fPrice.setText(p.getFinalPrice());
        }
        targetStyleid = p.getStyleID();
        String temp = p.getFinalPrice();
        targetPrice = Double.parseDouble(temp.replaceAll("[\\D]", ""));
        sixPmPrice = (TextView) findViewById(R.id.priceOn6pm);
    }

    public void navigateMe(View view) {
        Uri uriUrl = Uri.parse(targetUrlOn6pm);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


    public void shareMe(View view) {
        Intent intent2 = new Intent();
        intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_product_url_text_product_page_activity, zapposProductUrl));
        startActivity(Intent.createChooser(intent2, getResources().getString(R.string.share_via_product_page_activity)));

    }


    public void checkConnection() throws MalformedURLException, ExecutionException, InterruptedException {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Uri sixPmUri = new Uri.Builder().scheme("https").authority("api.6pm.com").path("Search").appendQueryParameter("term", targetProductId).appendQueryParameter("key", AUTH_KEY_6PM).appendQueryParameter("limit", "25").build();
            new RestCallActivity().execute(sixPmUri.toString());
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_toast), Toast.LENGTH_LONG).show();
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

                    if (productId.equals(targetProductId)) {
                        targetUrlOn6pm = jbj.getString("productUrl");
                        similarProductFound = true;
                        String styleId = jbj.getString("styleId");
                        if (styleId.equals(targetStyleid)) {
                            exactProductFound = true;

                        }
                        String price = jbj.getString("price");
                        finalPrice = Double.parseDouble(price.replaceAll("[$,]", ""));
                        if (finalPrice <= targetPrice) {
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
            if (exactProductFound) {
                sixPmPrice.setText(getResources().getString(R.string.exact_product_found_label_product_page_activity,finalPrice));
            } else if (similarProductFound) {
                sixPmPrice.setText(getResources().getString(R.string.similar_product_found_product_page_activity,finalPrice));
            }
            if (!exactProductFound && !similarProductFound) {
                navigateButton.setVisibility(View.GONE);
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



