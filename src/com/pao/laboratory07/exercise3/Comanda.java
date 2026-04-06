package  com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise1.*;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected StareComanda stare;
    protected String client;

    public Comanda(String nume, String client) {
        this.nume = nume;
        this.stare = StareComanda.PLACED;
        this.client=client;
    }
    public abstract double pretFinal();
    public abstract String descriere();

    public abstract String getTip();
}