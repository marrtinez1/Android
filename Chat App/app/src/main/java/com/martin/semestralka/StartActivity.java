package com.martin.semestralka;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**Trieda sluzi ako uvodna domovska aktivita aplikácie.*/
public class StartActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AdapterNaFragmenty adapterNaFragmenty;

    /** V metode sa inicializuju Fragmenty.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        tabLayout = (TabLayout) findViewById(R.id.nadpisy_fragmentov);
        viewPager = (ViewPager) findViewById(R.id.telo_fragmentov);

        adapterNaFragmenty = new AdapterNaFragmenty(getSupportFragmentManager());
        viewPager.setAdapter(adapterNaFragmenty);

        tabLayout.setupWithViewPager(viewPager);

    }
}
