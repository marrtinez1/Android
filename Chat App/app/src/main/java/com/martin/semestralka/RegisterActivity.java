package com.martin.semestralka;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/** Aktivita pre registrovanie noveho uzivatela.*/
public class RegisterActivity extends AppCompatActivity {

    /** Konstanty ktore sluzia na ulozenie mena uzivatela.*/
    static final String SHARED_PREFS = "ChatPrefs";
    static final String SHARED_PREFS_UZIVATEL = "uzivatel";


    /** UI premenne.*/
    private AutoCompleteTextView emailView;
    private AutoCompleteTextView uzivatelView;
    private EditText hesloView;
    private EditText potvrdHesloView;

    /** Premenna pre Firebase*/
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Priradenie textovych poli, ktore sluszia na zadanie emailu a hesla do premennych
        emailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        hesloView = (EditText) findViewById(R.id.register_heslo);
        potvrdHesloView = (EditText) findViewById(R.id.register_potvrd_heslo);
        uzivatelView = (AutoCompleteTextView) findViewById(R.id.register_meno);

        potvrdHesloView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == 200 || id == EditorInfo.IME_NULL) {
                    registracia();
                    return true;
                }
                return false;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

    }

    /** Metoda ktora sa vykona ked uzivatel stlaci button Potvrdit. Prebehne registracia.*/
    public void potvrdenieRegistracie(View v) {
        registracia();
    }

    /** V metode sa skontroluju vstupne udaje od uzivatela. Ak su spravne tak sa vytvori novy uzivatel v databaze.*/
    private void registracia() {

        // Resetovanie chybovych oznameni.
        emailView.setError(null);
        hesloView.setError(null);

        String email = emailView.getText().toString();
        String heslo = hesloView.getText().toString();

        boolean pom = false;
        View zvyrazniView = null;

        // Kontrola ci nie je heslo prazdne alebo ci splna podmienky.
        if (TextUtils.isEmpty(heslo) || !kontrolaHesla(heslo)) {
            hesloView.setError(getString(R.string.error_zle_heslo));
            zvyrazniView = hesloView;
            pom = true;
        }

        // Kontrola ci email nie je prazdny alebo ci email je platny.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_nemoze_ostat_prazdne));
            zvyrazniView = emailView;
            pom = true;
        } else if (!kontrolaEmailu(email)) {
            emailView.setError(getString(R.string.error_zly_email));
            zvyrazniView = emailView;
            pom = true;
        }

        // Ak uzivatel zadal vsetky hodnoty spravne tak ho vytvor v databaze inak zvyrazni View v ktorom je chyba
        if (pom) {
            zvyrazniView.requestFocus();
        } else {
            vytvoreniePouzivatelaVoFirebase();
        }
    }

    /** Skontroluje ci uzivatel zadal emailovu adresu*/
    private boolean kontrolaEmailu(String email) {
        return email.contains("@");
    }

    /** Skontroluje ci uzivatel zadal rovnake heslo a ci je dostatocne dlhe*/
    private boolean kontrolaHesla(String heslo) {
        String potvrdeneHeslo = potvrdHesloView.getText().toString();
        return potvrdeneHeslo.equals(heslo) && heslo.length() > 5;
    }

    /** Vytvorenie noveho uzivatela vo FireBase databaze. Pouzivaju sa na to dve metody z triedy Firebase.
     CreateUserWithEmailAndPassword vrati objekt typu Task na ktory naviazem Listener a podla neho sa zisti ci bol uzivatel uspesne pridany.*/
    private void vytvoreniePouzivatelaVoFirebase() {

        String email = emailView.getText().toString();
        String heslo = hesloView.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, heslo).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("FlashChat", "uzivatel bol vytvoreny: " + task.isSuccessful());

                        if(!task.isSuccessful()){
                            zobrazChybnyDialog("Chyba pri registrácii.");
                        } else {
                            ulozMeno();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    }
                });
    }

    /** Ulozenie mena pouzivatela do zariadenia pomocou SharedPreferences.*/
    private void ulozMeno() {
        String meno = uzivatelView.getText().toString();
        SharedPreferences pref = getSharedPreferences(SHARED_PREFS, 0);
        pref.edit().putString(SHARED_PREFS_UZIVATEL, meno).apply();
    }


    /** Dialog pre uzivatela ak registracia bola neuspesna.*/
    private void zobrazChybnyDialog(String sprava){

        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(sprava)
                .setPositiveButton("ok", null)
                .show();
    }
}

