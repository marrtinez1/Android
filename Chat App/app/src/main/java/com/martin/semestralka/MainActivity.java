package com.martin.semestralka;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**Hlavna aktivita v ktorej je posielanie sprav*/
public class MainActivity extends AppCompatActivity {

    /** UI premenne.*/
    private String menoUzivatela;
    private ListView chatListView;
    private EditText inputText;
    private ImageButton buttonNaOdoslanie;
    private DatabaseReference databaseReference;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zobrazMenoUzivatela();
        //Referencia (odkaz) na Firebase databazu.
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Priradenie Views v activity_main layoute do premennych.
        inputText = (EditText) findViewById(R.id.spravaInput);
        buttonNaOdoslanie = (ImageButton) findViewById(R.id.posliSpravuButton);
        chatListView = (ListView) findViewById(R.id.chat_list_view);

        // Listener ktory zabezpeci poslanie spravy po stlaceniy enter
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                poslatSpravu();
                return true;
            }
        });

        // OnClickListener naviazany na tlacido poslat spravu.
        buttonNaOdoslanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poslatSpravu();
            }
        });
    }

    /** Ziska meno uzivatela z pamate telefonu.*/
    private void zobrazMenoUzivatela(){

        SharedPreferences pref = getSharedPreferences(RegisterActivity.SHARED_PREFS, MODE_PRIVATE);

        menoUzivatela = pref.getString(RegisterActivity.SHARED_PREFS_UZIVATEL, null);

        if (menoUzivatela == null) menoUzivatela = "Anonymny";
    }

    /** Metoda na poslanie spravy. Vytvori sa instancia triedy Sprava do ktorej sa ulozi meno pouzivatela a text ktory zadal.
     Metoda child() zabezpeci aby sa vsetky spravy ukladali v "messages".
     Metoda push() vrati referenciu na "messages".
     Metoda setValue() ulozi data zo Spravy do databazy.*/
    private void poslatSpravu() {

        String input = inputText.getText().toString();
        if (!input.equals("")) {
            Sprava sprava = new Sprava(input, menoUzivatela);
            databaseReference.child("messages").push().setValue(sprava);
            inputText.setText("");
        }
    }

    /** Spojenie Adaptera s ListView.*/
    @Override
    public void onStart() {
        super.onStart();
        chatAdapter = new ChatAdapter(this, databaseReference, menoUzivatela);
        chatListView.setAdapter(chatAdapter);
    }

    /** Odstrani listener z adaptera ked aplikacia prejde do stavu onStop.*/
    @Override
    public void onStop() {
        super.onStop();

        chatAdapter.odstranFirebaseListener();
    }

}
