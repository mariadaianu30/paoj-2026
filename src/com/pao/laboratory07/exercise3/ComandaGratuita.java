package com.pao.laboratory07.exercise3;

final class ComandaGratuita extends Comanda {

    public ComandaGratuita(String nume, String client) {
        super(nume,client);

    }
    @Override
    public double pretFinal()
    {
        return 0.0;
    }


    @Override
    public String descriere() {
        return String.format("GIFT: %s, gratuit [PLACED] - client: %s", nume, client);
    }

    @Override
    public String getTip()
    {
        return "GRATUITA";
    }
}
