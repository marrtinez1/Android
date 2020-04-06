package com.martin.bitcointicker;

/** Všetky potrebné importy */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/** Hlavná tieda a aktivita v aplikácií */
public class EthereumAcivity extends AppCompatActivity {


    /** Premenná v ktorej je uložená adresa na ktorú posielam requesty */
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/ETH";

    TextView mPriceTextView;

    /** Metóda ktora sa volá pri spustení aplikácie a v ktorej sa inicializujú komponenty */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change);

        mPriceTextView = findViewById(R.id.priceLabel);
        Spinner spinner = findViewById(R.id.currency_spinner);
        Button changeButton = findViewById(R.id.buttonVzad);

        /** Inicializácia listenera na buttton ktorý pomocou intentu zmení aktivitu */
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EthereumAcivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        /** Nový ArrayAdapter ktorý používa pole stringov */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        /** Adaptér na layout keď sa klikne na spinner */
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner.setAdapter(adapter);

        /** Listener na spinner. Ak bola nejaká položka vybraná zavolá sa metóda getApi().
        *  Listener na spinner musí mať aj metódu reaguje na udalosť že nič nebolo vybrané*/
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Bitcoin", "" + parent.getItemAtPosition(position));
                String finalUrl = BASE_URL + parent.getItemAtPosition(position);
                getApi(finalUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Bitcoin", "Nic nebolo oznacene");
            }
        });

    }

    /** Metóda na získanie dát z internetu. Využíva dve externé knižnice pre prácu s http requestami.
     * V metóde sa využíva JSON formát z ktorého vyberám pre aplikáciu najpodstatnejšiu vec a to údaj last ktorý udáva najaktuálnejšiu cenu. */
    private void getApi(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Bitcoin", "JSON: " + response.toString());

                try {
                    String price = response.getString("last");
                    mPriceTextView.setText(price);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                Log.e("ERROR", e.toString());
            }
        });
    }
}

