package com.pao.laboratory07.exercise3;

final class ComandaRedusa extends Comanda {
    private double pret;
    private int discountProcent;

    public ComandaRedusa(String nume,String client, double pret, int discountProcent) {
        super(nume,client);
        this.pret = pret;
        this.discountProcent = discountProcent;
    }
    @Override
    public double pretFinal()
    {
        return pret * (1 - discountProcent / 100.0);
    }



    @Override
    public String descriere()
    {
        return String.format("DISCOUNTED: %s, pret: %.2f lei (-%d%%) [%s] - client: %s", nume, pretFinal(),discountProcent, stare, client);
    }

    @Override
    public String getTip()
    {
        return "REDUSA";
    }

    public int getDiscount() {
        return discountProcent;
    }
}
