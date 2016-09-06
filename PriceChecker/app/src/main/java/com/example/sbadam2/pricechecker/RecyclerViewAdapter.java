package com.example.sbadam2.pricechecker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;


/**
 * Created by sbadam2 on 8/29/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ProductViewHolder> {

    ArrayList<Product> products = new ArrayList<>();
    private Context context;

    RecyclerViewAdapter(Context context, ArrayList<Product> products) {
        this.products = products;
        this.context = context;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, null);
        ProductViewHolder pvh = new ProductViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int i) {
        holder.discount.setText(products.get(i).discount);
        holder.productName.setText(products.get(i).productName);
        holder.brandName.setText(products.get(i).brandName);
        holder.origPrice.setText(products.get(i).origPrice);
        holder.finalPrice.setText(products.get(i).finalPrice);
        holder.productImage.setImageBitmap(products.get(i).photoId);
        holder.origPrice.setPaintFlags(holder.origPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        final String pName = (String) holder.productName.getText();
        final int j = i;
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProductPage.class);
                intent.putExtra("product",products.get(j));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView productName;
        TextView brandName;
        TextView origPrice;
        TextView finalPrice;
        TextView discount;
        ImageView productImage;

        ProductViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            productName = (TextView) itemView.findViewById(R.id.productName);
            brandName = (TextView) itemView.findViewById(R.id.brandName);
            origPrice = (TextView) itemView.findViewById(R.id.origPrice);
            finalPrice = (TextView) itemView.findViewById(R.id.finalPrice);
            discount = (TextView) itemView.findViewById(R.id.discount);

           /* itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(v.getContext(),ProductPage.class);
                    intent.putExtra("productName",productName.getText());
                    context.startActivity(intent);
                }

            });*/



        }
    }


}
