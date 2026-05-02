package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

public class TranzactieRecord {
    int id;
    double suma;
    String data;
    TipTranzactie tip;
    Status status;

    public TranzactieRecord(int id, double suma, String data, TipTranzactie tip) {
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.tip = tip;
        this.status = Status.PENDING;
    }
}