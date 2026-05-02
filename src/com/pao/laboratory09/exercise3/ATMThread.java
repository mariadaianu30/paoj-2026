package com.pao.laboratory09.exercise3;

public class ATMThread extends Thread {

    private int atmId;
    private CoadaTranzactii coada;

    // generator simplu de id-uri
    private static int nextId = 1;

    public ATMThread(int atmId, CoadaTranzactii coada) {
        this.atmId = atmId;
        this.coada = coada;
        setName("ATM-" + atmId);
    }

    @Override
    public void run() {

        try {
            // fiecare ATM produce 4 tranzacții
            for (int i = 0; i < 4; i++) {

                // secțiune sincronizată pentru incrementarea id-ului global
                int id;
                synchronized (ATMThread.class) {
                    id = nextId++;}

                // sumă generata simplu
                double suma = 100 + Math.random() * 900;
                String data = "2026-05-02";

                Tranzactie t = new Tranzactie(id, suma, data);

                System.out.println("[ATM-" + atmId + "] trimite: " + t);

                // pun in banda
                coada.adauga(t);

                // pauza intre tranzactii
                Thread.sleep(50);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}