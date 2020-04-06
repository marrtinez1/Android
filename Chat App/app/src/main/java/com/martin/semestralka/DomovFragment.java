package com.martin.semestralka;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Fragment pre domovsku obrazovku.
 */
public class DomovFragment extends Fragment implements View.OnClickListener {

    private Button button;


    public DomovFragment() {

    }

    /** Metoda ktora pomocou inflatera vytvori layout a na button sa naviaze listener.*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_domov, container, false);

        button = (Button) view.findViewById(R.id.menu_vstupit);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), com.martin.semestralka.LoginActivity.class);
        startActivity(intent);
    }
}
