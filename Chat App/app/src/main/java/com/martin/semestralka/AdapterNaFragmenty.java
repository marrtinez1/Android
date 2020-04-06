package com.martin.semestralka;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/** Trieda ktora sluzi ako adapter na Fragmenty.
 * https://www.youtube.com/watch?v=3Am-iad_Gkg*/
public class AdapterNaFragmenty extends FragmentPagerAdapter {

    public AdapterNaFragmenty(FragmentManager fm) {
        super(fm);
    }

    /** Metoda vrati Fragment na vybranej pozicii.*/
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                DomovFragment domovFragment = new DomovFragment();
                return domovFragment;

            case 1:
                InfoFragment infoFragment = new InfoFragment();
                return infoFragment;

            default:
                return null;
        }
    }

    /** Metoda vrati pocet Fragmentov.*/
    @Override
    public int getCount() {
        return 2;
    }

    /** Vrati nadpisy Fragmentov.*/
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position)
        {
            case 0:
                return  "Domov";
            case 1:
                return  "Informácie";

            default:
                return null;
        }
    }
}
