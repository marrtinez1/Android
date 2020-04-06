package com.martin.semestralka;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**Aktivita pre prihlásenie uzivatela s moznostou registracie ak este nema ucet.*/
public class LoginActivity extends AppCompatActivity {

    /** UI premenne.*/
    private AutoCompleteTextView emailView;
    private EditText hesloView;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Priradenie textovych poli na zadanie emailu a hesla do premennych
        emailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        hesloView = (EditText) findViewById(R.id.login_heslo);

        hesloView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                    pokusOPrihlasenie();
                    return true;
                }
                return false;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
    }

    /** Metoda ktora vola metodu pokusOPrihlasenie a ktora sa vykona ked sa stlaci button Prihlasit sa.*/
    public void prihlasenieExistujucehoUzivatela(View v) {
        pokusOPrihlasenie();
    }

    /** Metoda ktora sa vykona po stlaceni tlacidla Registrovat. Spusti sa nova aktivita s nazvom RegisterActivity.*/
    public void registraciaNovehoUzivatela(View v) {
        Intent intent = new Intent(this, com.martin.semestralka.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    /** Metoda na prihlasenie uzivatela. Na zaciatku prebehne kontrola ci uzivatel nenechal meno alebo heslo prazdne.*/
    private void pokusOPrihlasenie() {

        String email = emailView.getText().toString();
        String heslo = hesloView.getText().toString();

        if (email.equals("") || heslo.equals("")) return;
        Toast.makeText(this, "Prebiaha prihlasovanie...", Toast.LENGTH_SHORT).show();

        // Prihlasenie do databazy pomocou metody signInWithEmailAndPassword ktora je z triedy Firebase.auth.
        // Metoda addOnCompleteListener vrati Task objekt pomocou ktoreho sa zisti ci prihlasnie prebehlo uspesne.
        firebaseAuth.signInWithEmailAndPassword(email, heslo).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.d("FlashChat", "pokusOPrihlasenie onComplete: " + task.isSuccessful());

                if (!task.isSuccessful()) {
                    zobrazChybnyDialog("Nastala chyba pri prihlasovani");
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }

            }
        });
    }

    /** Dialog pre uzivatela ak pri prihlasovani nastala chyba*/
    private void zobrazChybnyDialog(String sprava) {

        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(sprava)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

