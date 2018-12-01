package com.example.pavlion.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavlion.inventoryapp.data.StoreContract.StoreEntry;
import com.example.pavlion.inventoryapp.data.StoreDbHelper;

public class EditorActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri mCurrentProductUri;

    private EditText mProductNameText;
    private EditText mProductPriceText;
    private EditText mProductQuantityText;
    private EditText mProductSupplierNameText;
    private EditText mProductSupplierPhoneNumberText;
    public static String type;

    public static String productID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mProductNameText=findViewById(R.id.edit_product_name);
        mProductPriceText=findViewById(R.id.edit_product_price);
        mProductQuantityText=findViewById(R.id.edit_product_quantity);
        mProductSupplierNameText=findViewById(R.id.edit_product_supplier_name);
        mProductSupplierPhoneNumberText=findViewById(R.id.edit_product_supplier_phone_number);

        TextView mProductTypeText = findViewById(R.id.bookType);


        Intent i= getIntent();
        type= i.getStringExtra("Type");
        mProductTypeText.setText(type);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (mCurrentProductUri == null) {
            setTitle("Add Product");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit Product");
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

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


             productID = cursor.getString(idColumnIndex);
            String currentName = cursor.getString(nameColumnIndex);
            int currentPrice = cursor.getInt(priceColumnIndex);
            int currentQuantity = cursor.getInt(quantityColumnIndex);
            String currentSupplierName = cursor.getString(supplierNameColumnIndex);
            int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

            mProductNameText.setText(currentName);
            mProductPriceText.setText(Integer.toString(currentPrice));
            mProductQuantityText.setText(Integer.toString(currentQuantity));
            mProductSupplierNameText.setText(currentSupplierName);
            mProductSupplierPhoneNumberText.setText(Integer.toString(currentSupplierPhone));


        }
    }

        private void insertBook() {

            String nameString = mProductNameText.getText().toString().trim();
            String priceString = mProductPriceText.getText().toString().trim();
            int price = Integer.parseInt(priceString);
            String quantityString = mProductQuantityText.getText().toString().trim();
            int quantity = Integer.parseInt(quantityString);
            String suppNameString = mProductSupplierNameText.getText().toString().trim();
            String suppNumberString = mProductSupplierPhoneNumberText.getText().toString().trim();
            int suppNumber = Integer.parseInt(suppNumberString);



            if(mCurrentProductUri==null) {

            if (TextUtils.isEmpty(nameString)) {
                Toast.makeText(this, getResources().getString(R.string.productNameError), Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(priceString)) {
                Toast.makeText(this, getResources().getString(R.string.productPriceError), Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(quantityString )) {
                Toast.makeText(this, getResources().getString(R.string.productQuantityError), Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(suppNameString)) {
                Toast.makeText(this, getResources().getString(R.string.productSuppNameError), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(suppNumberString)) {
                Toast.makeText(this, getResources().getString(R.string.productSuppPhoneNoError), Toast.LENGTH_SHORT).show();
                return;
            }


            // Create database helper
            StoreDbHelper mDbHelper = new StoreDbHelper(this);
            // Gets the database in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            // Create a ContentValues object where column names are the keys,
            // and book attributes from the editor are the values.
            ContentValues values = new ContentValues();
            values.put(StoreEntry.COLUMN_PRODUCT_NAME, nameString);
            values.put(StoreEntry.COLUMN_PRODUCT_PRICE, price);
            values.put(StoreEntry.COLUMN_PRODUCT_QUANTITY, quantity);
            values.put(StoreEntry.COLUMN_SUPPLIER_NAME, suppNameString);
            values.put(StoreEntry.COLUMN_SUPPLIER_PHONE_NUMBER, suppNumber);

            // Insert a new row for book in the database, returning the ID of that new row.
            long newRowId = db.insert(StoreEntry.TABLE_NAME, null, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (newRowId == -1) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, getResources().getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
            } else {
                // For else condition, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, getResources().getString(R.string.savingMessage) + newRowId, Toast.LENGTH_SHORT).show();
            }
        }
         else {

            if (TextUtils.isEmpty(mProductNameText.getText().toString().trim())) {
                Toast.makeText(this, getResources().getString(R.string.productNameError), Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(mProductPriceText.getText().toString().trim())) {
                Toast.makeText(this, getResources().getString(R.string.productPriceError), Toast.LENGTH_SHORT).show();
                return;
            }


            if (TextUtils.isEmpty(mProductQuantityText.getText().toString().trim())) {
                Toast.makeText(this, getResources().getString(R.string.productQuantityError), Toast.LENGTH_SHORT).show();
                return;
            }


            if (TextUtils.isEmpty(mProductSupplierNameText.getText().toString().trim())) {
                Toast.makeText(this, getResources().getString(R.string.productSuppNameError), Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(mProductSupplierPhoneNumberText.getText().toString().trim())) {
                Toast.makeText(this, getResources().getString(R.string.productSuppPhoneNoError), Toast.LENGTH_SHORT).show();
            }

                StoreDbHelper mDbHelper = new StoreDbHelper(this);
                // Gets the database in write mode
                SQLiteDatabase db = mDbHelper.getWritableDatabase();


                ContentValues values = new ContentValues();

            values.put(StoreEntry.COLUMN_PRODUCT_NAME, nameString);
            values.put(StoreEntry.COLUMN_PRODUCT_PRICE, price);
            values.put(StoreEntry.COLUMN_PRODUCT_QUANTITY, quantity);
            values.put(StoreEntry.COLUMN_SUPPLIER_NAME, suppNameString);
            values.put(StoreEntry.COLUMN_SUPPLIER_PHONE_NUMBER, suppNumber);


                long eRowId = db.update(StoreEntry.TABLE_NAME,values,StoreEntry._ID + "=" + productID,null );

                if (eRowId  == 0) {
                Toast.makeText(this, getString(R.string.update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }

        }

        }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertBook();
                Intent in = new Intent(EditorActivity.this,MainActivity.class);
                startActivity(in);
                // Do nothing for now
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mProductNameText.setText("");
            mProductPriceText.setText("");
            mProductQuantityText.setText("");
            mProductSupplierPhoneNumberText.setText("");
            mProductSupplierNameText.setText("");
        }


        }