package com.pao.laboratory05.biblioteca;

import java.util.*;
public class BibliotecaService {
    
    private Carte[] carti;

     private BibliotecaService() {
        carti = new Carte[0];
    }

    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return Holder.INSTANCE;
    }

    public void addCarte(Carte carte)
    {
        
        Carte[] newCarti = new Carte[carti.length + 1];

        for (int i = 0; i < carti.length; i++) {
            newCarti[i] = carti[i];
        }

        newCarti[carti.length] = carte;
        carti = newCarti;

        System.out.println("Carte adaugata: " + carte);
    }

    public void listSortedByRating()
    {
        Carte[] copie=carti.clone();
        Arrays.sort(copie);

        for(Carte c: copie)
        {
            System.out.println(c);
        }
    }

    public void listSortedBy(Comparator<Carte> comparator)
    {
        Carte[] copie=carti.clone();
        Arrays.sort(copie,comparator);
        
        for(Carte c: copie)
        {
            System.out.println(c);
        }

    }

}
