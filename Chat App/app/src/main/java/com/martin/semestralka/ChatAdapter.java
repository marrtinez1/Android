package com.martin.semestralka;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**Trieda ktora sluzi na prepojenie sprav medzi databazou a ListView*/
public class ChatAdapter extends BaseAdapter {

    private Activity aktivita;
    private DatabaseReference referenciaNaDatabazu;
    private String menoUzivatela;

    /** Udaje ktory ziskavam z databazy su typu DataSnapshot*/
    private ArrayList<DataSnapshot> snapshotList;

    /** Listener z triedy Firebase ktory "pocuva" ci prisla nova sprava.
    Metoda onChildAdded prida spravu do arraylistu. Ostatne metody aplikacia nepotrebuje.*/
    private ChildEventListener firebaseListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            snapshotList.add(dataSnapshot);
            /**Sluzi na refresh ListView z dovodu pribudnutia novych dat.*/
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    /**Konstruktor*/
    public ChatAdapter(Activity activity, DatabaseReference ref, String name) {

        aktivita = activity;
        menoUzivatela = name;
        referenciaNaDatabazu = ref.child("messages");
        referenciaNaDatabazu.addChildEventListener(firebaseListener);

        snapshotList = new ArrayList<>();
    }

    /**Trieda v ktorej su vsetky Views v jednom raidku.*/
    private static class ViewHolder{
        TextView menoAutora;
        TextView telo;
        LinearLayout.LayoutParams parametre;
    }

    /** Metody ktore pozaduje trieda ChatAdapter
    Vrati pocet prvkov v ArrayListe.*/
    @Override
    public int getCount() {
        return snapshotList.size();
    }

    @Override
    public Sprava getItem(int position) {

        DataSnapshot snapshot = snapshotList.get(position);
        return snapshot.getValue(Sprava.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /** Vrati View na konkretnej pozicii.
    Na vytvorenie noveho View z XML sluzi LayoutInflater.
    Ak neexistuje riadok so spravou tak ho vytvorim a data ulozim v triede ViewHolder a pomocou setTag() tieto data spojim s vytvorenym riadkom.*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) aktivita.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_riadok, parent, false);

            final ViewHolder holder = new ViewHolder();
            holder.menoAutora = convertView.findViewById(R.id.autor);
            holder.telo = convertView.findViewById(R.id.sprava);
            holder.parametre = (LinearLayout.LayoutParams) holder.menoAutora.getLayoutParams();
            convertView.setTag(holder);

        }

        /**Nastavenie mena uzivatela a textu spravy pre jeden riadok.*/
        final Sprava sprava = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        boolean isMe = sprava.getAutor().equals(menoUzivatela);
        setChatRowAppearance(isMe, holder);

        String autor = sprava.getAutor();
        holder.menoAutora.setText(autor);

        String spr = sprava.getSprava();
        holder.telo.setText(spr);


        return convertView;
    }

    /**Sluzi na rozlisovanie sprav.
     * https://developer.android.com/studio/write/draw9patch*/
    private void setChatRowAppearance(boolean isItMe, ViewHolder holder) {

        if (isItMe) {

            holder.parametre.gravity = Gravity.END;
            holder.menoAutora.setTextColor(Color.GREEN);
            holder.telo.setBackgroundResource(R.drawable.bubble2);

        } else {
            holder.parametre.gravity = Gravity.START;
            holder.menoAutora.setTextColor(Color.BLUE);
            holder.telo.setBackgroundResource(R.drawable.bubble1);
        }

        holder.menoAutora.setLayoutParams(holder.parametre);
        holder.telo.setLayoutParams(holder.parametre);

    }


    /**Odstrani firebaseListener ktory je naviazany na databazu.*/
    void odstranFirebaseListener() {

        referenciaNaDatabazu.removeEventListener(firebaseListener);
    }
}
