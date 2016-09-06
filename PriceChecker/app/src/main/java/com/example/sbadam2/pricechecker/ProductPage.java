package com.example.sbadam2.pricechecker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ProductPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Intent intent = getIntent();
        Product p = intent.getParcelableExtra("product");
        Toast.makeText(getApplicationContext(),p.brandName,Toast.LENGTH_SHORT).show();
    }




}
