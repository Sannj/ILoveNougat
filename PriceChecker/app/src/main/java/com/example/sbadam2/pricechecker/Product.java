package com.example.sbadam2.pricechecker;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by sbadam2 on 8/29/2016.
 */


public class Product implements Parcelable {
    String productName;
    String brandName;
    Bitmap photoId;
    String origPrice;
    String finalPrice;
    String discount;
    String productUrl;
    String imageUrl;
    String styleID;
    String productId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(productName);
        out.writeString(brandName);
        out.writeValue(photoId);
        out.writeString(origPrice);
        out.writeString(finalPrice);
        out.writeString(discount);
        out.writeString(productUrl);
        out.writeString(imageUrl);
        out.writeString(styleID);
        out.writeString(productId);
    }

    private Product(Parcel in) {
        this.productName = in.readString();
        this.brandName = in.readString();
        this.photoId = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        this.origPrice = in.readString();
        this.finalPrice = in.readString();
        this.discount = in.readString();
        this.productUrl = in.readString();
        this.imageUrl = in.readString();
        this.styleID = in.readString();
        this.productId = in.readString();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {

        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }

    };

    Product(String productName, String brandName, String origPrice, String finalPrice, String discount, Bitmap bmp, String productUrl, String imageUrl, String styleID, String productId) {
        this.productName = productName;
        this.brandName = brandName;
        this.photoId = bmp;
        this.origPrice = origPrice;
        this.finalPrice = finalPrice;
        this.discount = discount;
        this.productUrl = productUrl;
        this.imageUrl = imageUrl;
        this.styleID = styleID;
        this.productId = productId;

    }

}
