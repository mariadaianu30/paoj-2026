package com.pao.laboratory09.exercise3;

import java.util.LinkedList;
import java.util.Queue;

// banda partajata (buffer)
public class CoadaTranzactii {

    // capacitate maxima este  5
    private final int CAPACITATE = 5;

    // coada interna
    private Queue<Tranzactie> coada = new LinkedList<>();

    // adauga tranzacție (producer)
    public synchronized void adauga(Tranzactie t) throws InterruptedException {

        // dacă banda este plina atunci producătorul așteaptă
        while (coada.size() == CAPACITATE) {
            System.out.println("[" + Thread.currentThread().getName() + "] astept loc...");
            wait(); // elibereaza lock-ul și așteapta
        }

        // adaugă tranzacția în coadă
        coada.add(t);

        // trezește toate firele care așteptau
        notifyAll();
    }

    // extrage tranzactia (consumatorul)
    public synchronized Tranzactie extrage() throws InterruptedException {

        // daca banda este goală atun consumatorul așteapta
        while (coada.isEmpty()) {
            wait();
        }

        // /scoate primul element
        Tranzactie t = coada.poll();

        // trezește toate firele
        notifyAll();

        return t;
    }

    // folosit la oprire
    public synchronized boolean esteGoala() {
        return coada.isEmpty();
    }
}