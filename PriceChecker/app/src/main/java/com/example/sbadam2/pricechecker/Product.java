package com.example.sbadam2.pricechecker;

import android.graphics.Bitmap;

/**
 * Created by sbadam2 on 8/29/2016.
 */
public class Product {
    String productName;
    String brandName;
    Bitmap photoId;
    String origPrice;
    String finalPrice;
    String discount;
    String productUrl;

    Product(String productName, String brandName, String origPrice, String finalPrice, String discount, Bitmap bmp, String productUrl) {
        this.productName = productName;
        this.brandName = brandName;
        this.photoId = bmp;
        this.origPrice = origPrice;
        this.finalPrice = finalPrice;
        this.discount = discount;
        this.productUrl = productUrl;

    }


}
