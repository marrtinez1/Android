package com.martin.semestralka;

/**Trieda ktora nesie meno autora spravy a samotny text*/
public class Sprava {

    private String sprava;
    private String autor;

    public Sprava(String sprava, String autor) {
        this.sprava = sprava;
        this.autor = autor;
    }

    /**Firebase pozaduje aj bezparametricky konstruktor*/
    public Sprava() {
    }

    public String getSprava() {
        return sprava;
    }

    public String getAutor() {
        return autor;
    }
}
