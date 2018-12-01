package com.example.pavlion.inventoryapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pavlion.inventoryapp.data.StoreContract.StoreEntry;

public class InventoryCursorAdapter extends CursorAdapter {


    public InventoryCursorAdapter(Context context, Cursor c){
        super(context,c,0);
    }
    @Override
     public View newView(Context context, Cursor cursor, ViewGroup parent){

        return LayoutInflater.from(context).inflate(R.layout.itemlist,parent,false);


     }
     @Override
    public void bindView(final View view,final Context context,final Cursor cursor){

        Log.d("Position " + cursor.getPosition() + ":" , " bindView() has been called.");

        TextView productNameTextView = view.findViewById(R.id.productName);
        TextView productPriceDesc = view.findViewById(R.id.productPrice);
        TextView productQuantityDesc= view.findViewById(R.id.productQuantity);
        ImageView cartImage = view.findViewById(R.id.cartImage);
        ImageView editImage = view.findViewById(R.id.edit);

        final int columnIdIndex = cursor.getColumnIndex(StoreEntry._ID);
        int productNameColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_NAME);
        int productPriceColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_PRICE);
        int productQuantityColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_QUANTITY);

        final String productID = cursor.getString(columnIdIndex);
        String productName = cursor.getString(productNameColumnIndex);
        String productPrice = cursor.getString(productPriceColumnIndex);
        final String productQuantity = cursor.getString(productQuantityColumnIndex);

        cartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity Activity = (MainActivity) context;
                Activity.productSaleCount(Integer.valueOf(productID), Integer.valueOf(productQuantity));
            }
        });

        productNameTextView.setText(productID + " ) " + productName);
        productPriceDesc.setText( "Price: "+ productPrice + " $");
        productQuantityDesc.setText(productQuantity);



        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), EditorActivity.class);
                Uri mCurrentProductUri = ContentUris.withAppendedId(StoreEntry.CONTENT_URI, Long.parseLong(productID));
                intent.setData(mCurrentProductUri);
                context.startActivity(intent);
            }
        });


    }


}
