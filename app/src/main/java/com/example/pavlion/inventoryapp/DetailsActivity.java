package com.example.pavlion.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavlion.inventoryapp.data.StoreContract.StoreEntry;


public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


        private static final int EXISTING_INVENTORY_LOADER = 0;
        private Uri mCurrentProductUri;

        private TextView mProductNameViewText;
        private TextView mProductPriceViewText;
        private TextView mProductQuantityViewText;
        private TextView mProductSupplierNameViewText;
        private TextView mProductSupplierPhoneNumberViewText;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_details);

            mProductNameViewText = findViewById(R.id.product_name);
            mProductPriceViewText = findViewById(R.id.product_price);
            mProductQuantityViewText = findViewById(R.id.product_quantity);
            mProductSupplierPhoneNumberViewText = findViewById(R.id.product_supplier_phone_number);
            mProductSupplierNameViewText=findViewById(R.id.product_supplier_name);


            Intent intent = getIntent();
            mCurrentProductUri = intent.getData();
            if (mCurrentProductUri == null) {
                invalidateOptionsMenu();
            } else {
                getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
            }
            Log.d("message", "onCreate ViewActivity");

            TextView bookType = findViewById(R.id.bookType2);
            TextView tp = findViewById(R.id.bookType);
            TypeClass.productType = String.valueOf(tp);
            bookType.setText(TypeClass.productType);

            }

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            String[] projection = {
                    StoreEntry._ID,
                    StoreEntry.COLUMN_PRODUCT_NAME,
                    StoreEntry.COLUMN_PRODUCT_PRICE,
                    StoreEntry.COLUMN_PRODUCT_QUANTITY,
                    StoreEntry.COLUMN_SUPPLIER_NAME,
                    StoreEntry.COLUMN_SUPPLIER_PHONE_NUMBER
            };
            return new CursorLoader(this,
                    mCurrentProductUri,
                    projection,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor == null || cursor.getCount() < 1) {
                return;
            }
            if (cursor.moveToFirst()) {

                final int idColumnIndex = cursor.getColumnIndex(StoreEntry._ID);
                int nameColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_NAME);
                int priceColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_PRICE);
                int quantityColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_QUANTITY);
                int supplierNameColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_NAME);
                int supplierPhoneColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

                String currentName = cursor.getString(nameColumnIndex);
                final int currentPrice = cursor.getInt(priceColumnIndex);
                final int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                final int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

                mProductNameViewText.setText(currentName);
                mProductPriceViewText.setText(Integer.toString(currentPrice) + getResources().getString(R.string.currency));
                mProductQuantityViewText.setText(Integer.toString(currentQuantity));
                mProductSupplierNameViewText.setText(currentSupplierName);
                mProductSupplierPhoneNumberViewText.setText(Integer.toString(currentSupplierPhone));

                Button productDecreaseButton = findViewById(R.id.sub_btn);
                productDecreaseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        decreaseCount(idColumnIndex, currentQuantity);
                    }
                });

                Button productIncreaseButton = findViewById(R.id.add_btn);
                productIncreaseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        increaseCount(idColumnIndex, currentQuantity);
                    }
                });

                ImageView productDeleteButton = findViewById(R.id.delete);
                productDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeleteConfirmationDialog();
                    }
                });

                ImageView phoneButton = findViewById(R.id.phone);
                phoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phone = String.valueOf(currentSupplierPhone);
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        if (intent.resolveActivity(getPackageManager()) != null) {

                            startActivity(intent);

                        }

                    }

                });


            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }

        public void decreaseCount(int productID, int productQuantity) {
            productQuantity = productQuantity - 1;
            if (productQuantity >= 0) {
                updateProduct(productQuantity);
                Toast.makeText(this, getString(R.string.quantity_changed_msg), Toast.LENGTH_SHORT).show();

                Log.d("Log msg", " - productID " + productID + " - quantity " + productQuantity + " , decreaseCount has been called.");
            } else {
                Toast.makeText(this, getString(R.string.quantity_finished_msg), Toast.LENGTH_SHORT).show();
            }
        }

        public void increaseCount(int productID, int productQuantity) {
            productQuantity = productQuantity + 1;
            if (productQuantity >= 0) {
                updateProduct(productQuantity);
                Toast.makeText(this, getString(R.string.quantity_changed_msg), Toast.LENGTH_SHORT).show();

                Log.d("Log msg", " - productID " + productID + " - quantity " + productQuantity + " , decreaseCount has been called.");
            }
        }


        private void updateProduct(int productQuantity) {
            Log.d("message", "updateProduct at ViewActivity");

            if (mCurrentProductUri == null) {
                return;
            }
            ContentValues values = new ContentValues();
            values.put(StoreEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);

            if (mCurrentProductUri == null) {
                Uri newUri = getContentResolver().insert(StoreEntry.CONTENT_URI, values);
                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.insert_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.insert_successful),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
                if (rowsAffected == 0) {
                    Toast.makeText(this, getString(R.string.update_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.insert_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }

        private void deleteProduct() {
            if (mCurrentProductUri != null) {
                int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
                if (rowsDeleted == 0) {
                    Toast.makeText(this, getString(R.string.delete_product_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.delete_product_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }
            finish();
        }

        private void showDeleteConfirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.delete_dialog_msg);
            builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    deleteProduct();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

