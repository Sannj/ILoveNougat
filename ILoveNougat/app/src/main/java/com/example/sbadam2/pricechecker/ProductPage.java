package com.example.sbadam2.pricechecker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class ProductPage extends AppCompatActivity {

    String URL_PROTOCOL_6PM;
    String AUTH_KEY_6PM;
    String URL_AUTHORITY_6PM;
    String URL_PATH;
    String URL_QUERY_PARAMETER_TERM;
    String URL_QUERY_PARAMETER_KEY;
    String URL_QUERY_PARAMETER_LIMIT;
    String URL_QUERY_PARAMETER_LIMIT_VALUE;
    String productName;
    double finalPrice;
    String productUrl;
    TextView pName;
    TextView bName;
    TextView oPrice;
    TextView disc;
    TextView fPrice;
    TextView f6pmPrice;
    ImageView pImage;
    Product p;
    String targetStyleId;
    String targetProductId;
    double targetPrice;
    boolean similarProductFound = false;
    boolean exactProductFound = false;
    Button navigateButton;
    String targetUrlOn6pm;
    String zapposProductUrl;
    private static final String TAG = "ProductPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Log.v(TAG,"Entered Product Page ACtivity");
        try {
            ActivityInfo ai = getPackageManager()
                    .getActivityInfo(this.getComponentName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            AUTH_KEY_6PM = bundle.getString("AUTH_KEY_6PM");
            URL_PROTOCOL_6PM = bundle.getString("URL_PROTOCOL_6PM");
            URL_AUTHORITY_6PM = bundle.getString("URL_AUTHORITY_6PM");
            URL_PATH = bundle.getString("URL_PATH");
            URL_QUERY_PARAMETER_TERM = bundle.getString("URL_QUERY_PARAMETER_TERM");
            URL_QUERY_PARAMETER_KEY = bundle.getString("URL_QUERY_PARAMETER_KEY");
            URL_QUERY_PARAMETER_LIMIT = bundle.getString("URL_QUERY_PARAMETER_LIMIT");
            URL_QUERY_PARAMETER_LIMIT_VALUE = bundle.getString("URL_QUERY_PARAMETER_LIMIT_VALUE");
            Log.v(TAG,"fetched values from manifest");
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            e.getLocalizedMessage();
        }
        Intent intent = getIntent();
        p = intent.getParcelableExtra("product");
        Log.v(TAG,"Got the product details");
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
        targetStyleId = p.getStyleId();
        String temp = p.getFinalPrice();
        targetPrice = Double.parseDouble(temp.replaceAll("[$,]", ""));
        f6pmPrice = (TextView) findViewById(R.id.priceOn6pm);
        Log.v(TAG,"Displaying the product details");
        try {
            checkConnection();
        } catch (MalformedURLException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        dis = dis.substring(0, 1);
        int discount = Integer.parseInt(dis);
        TextView OrigPriceLabel = (TextView) findViewById(R.id.pOrigPrice);
        TextView DiscountLabel = (TextView) findViewById(R.id.pDiscount);
        TextView FinalPriceLabel = (TextView) findViewById(R.id.pFinalPrice);
        if (discount == 0) {
            OrigPriceLabel.setText(getResources().getString(R.string.
                    original_price_as_final_price_label_product_page_activity));
            DiscountLabel.setVisibility(View.GONE);
            FinalPriceLabel.setVisibility(View.GONE);
            oPrice.setText(p.getOrigPrice());
        } else {
            OrigPriceLabel.setText(getResources().getString(
                    R.string.original_price_label_product_page_activity));
            DiscountLabel.setVisibility(View.VISIBLE);
            FinalPriceLabel.setVisibility(View.VISIBLE);
            oPrice.setText(p.getOrigPrice());
            disc.setText(p.getDiscount());
            fPrice.setText(p.getFinalPrice());
        }

    }


    public void navigateMe(View view) {
        Log.v(TAG,"Opening the web browser");
        Uri uriUrl = Uri.parse(targetUrlOn6pm);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


    public void shareMe(View view) {
        Log.v(TAG,"Sharing the product details");
        Intent intent2 = new Intent();
        intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_TEXT, getResources().getString(
                R.string.share_product_url_text_product_page_activity, zapposProductUrl));
        startActivity(Intent.createChooser(intent2, getResources().getString(
                R.string.share_via_product_page_activity)));
    }


    public void checkConnection() throws MalformedURLException, ExecutionException, InterruptedException {
        Log.v(TAG,"Checking if there is an active internet connection");
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Uri sixPmUri = new Uri.Builder().scheme(URL_PROTOCOL_6PM).authority(URL_AUTHORITY_6PM).path(URL_PATH)
                    .appendQueryParameter(URL_QUERY_PARAMETER_TERM, targetProductId)
                    .appendQueryParameter(URL_QUERY_PARAMETER_KEY, AUTH_KEY_6PM)
                    .appendQueryParameter(URL_QUERY_PARAMETER_LIMIT, URL_QUERY_PARAMETER_LIMIT_VALUE)
                    .build();
            new RestCallActivity().execute(sixPmUri.toString());
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(
                    R.string.no_internet_toast), Toast.LENGTH_LONG).show();
        }
    }

    private class RestCallActivity extends AsyncTask<String, Void, JSONArray> {

        JSONObject jObj;
        JSONArray jArray;


        @Override
        protected JSONArray doInBackground(String... params) {
            Log.v(TAG,"Starting async task, making a call to 6pm for the exact product.");
            try {
                jObj = sixPMCall(params[0]);
                jArray = jObj.getJSONArray("results");
                Log.v(TAG,"Comparing the poducts and their prices");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jTempObj = jArray.getJSONObject(i);
                    String productId = jTempObj.getString("productId");

                    if (productId.equals(targetProductId)) {
                        targetUrlOn6pm = jTempObj.getString("productUrl");
                        similarProductFound = true;
                        String styleId = jTempObj.getString("styleId");
                        if (styleId.equals(targetStyleId)) {
                            exactProductFound = true;

                        }
                        String price = jTempObj.getString("price");
                        finalPrice = Double.parseDouble(price.replaceAll("[$,]", ""));
                        if (finalPrice <= targetPrice) {
                            productName = jTempObj.getString("productName");
                            productUrl = jTempObj.getString("productUrl");
                            break;
                        }
                    }
                }
            } catch (IOException | JSONException ex) {
                ex.getLocalizedMessage();
            }
            return jArray;
        }

        @Override
        protected void onPostExecute(JSONArray jArray) {
            Log.v(TAG,"Displaying appropriate results");
            if (exactProductFound) {
                f6pmPrice.setText(getResources().getString(
                        R.string.exact_product_found_label_product_page_activity, finalPrice));
            } else if (similarProductFound) {
                navigateButton.setVisibility(View.VISIBLE);
                f6pmPrice.setText(getResources().getString(
                        R.string.similar_product_found_product_page_activity, finalPrice));
            }
            if (!exactProductFound && !similarProductFound) {
                navigateButton.setVisibility(View.INVISIBLE);
            }

        }

        private JSONObject sixPMCall(String zUrl) throws IOException, JSONException {
            InputStream is = null;
            JSONObject jObj = null;
            try {
                Log.v(TAG,"Making the rest call to 6pm");
                URL url = new URL(zUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.connect();
                int response = conn.getResponseCode();
                if (response != 200) {
                    Toast.makeText(getApplicationContext(), getResources().getString(
                            R.string.rest_call_failed), Toast.LENGTH_LONG).show();
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
            } catch (Exception e) {
                e.printStackTrace();
                e.getLocalizedMessage();
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            return jObj;
        }
    }
}
