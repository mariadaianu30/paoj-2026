package com.pao.laboratory09.exercise3;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        /// /BANDA COMUNA INTRE TOATE THREAD-URILE
        CoadaTranzactii coada = new CoadaTranzactii();

        // cream cele 3 ATM-uri
        ATMThread atm1 = new ATMThread(1, coada);
        ATMThread atm2 = new ATMThread(2, coada);
        ATMThread atm3 = new ATMThread(3, coada);

        // consumaatorul
        ProcessorThread processor = new ProcessorThread(coada);

        // consumatorul rulează pe fir separat
        Thread processorThread = new Thread(processor);

        processorThread.start();
        atm1.start();
        atm2.start();
        atm3.start();

        // firul principal așteaptă terminarea ATM-urilor
        atm1.join();
        atm2.join();
        atm3.join();

        // spunem consumatorului să se oprească dupa ce toate atm-urile au terminat
        processor.activ = false;
        synchronized (coada) {
            coada.notifyAll();
        }

        processorThread.join();

        System.out.println(
                "Toate tranzactiile procesate. Total: "
                        + processor.getTotalProcesate()
        );
    }
}