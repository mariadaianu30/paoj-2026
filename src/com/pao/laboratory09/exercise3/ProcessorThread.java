package com.pao.laboratory09.exercise3;

// Consumator
public class ProcessorThread implements Runnable {

    private CoadaTranzactii coada;

    // toate thread-urile vad modificarea simultan
    public volatile boolean activ = true;

    // contor tranzactii procesate
    private int totalProcesate = 0;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    public int getTotalProcesate() {
        return totalProcesate;
    }

    @Override
    public void run() {

        try {
            // rulează cat e activ sau cat timp am elemente in coada
            while (activ || !coada.esteGoala()) {

                Tranzactie t;

                synchronized (coada) {

                    // dacă e goala si inca activ aatunci asteapta input
                    while (coada.esteGoala() && activ) {
                        coada.wait();
                    }

                    // dacă s-a oprit si coada e goala atunci se opreste
                    if (!activ && coada.esteGoala()) {
                        break;
                    }
                }

                // extrage tranzacția
                t = coada.extrage();
                Thread.sleep(80);  ///simulez procesarea
                System.out.println(
                        "[Processor] Factura #" + t.getId()
                                + " - "
                                + String.format("%.2f", t.getSuma())
                                + " RON | "
                                + t.getData()
                );

                totalProcesate++;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}