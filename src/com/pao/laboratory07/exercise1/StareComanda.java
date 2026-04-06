package com.pao.laboratory07.exercise1;
import java.util.*;
public enum StareComanda {
    PLACED, PROCESSED, SHIPPED, DELIVERED, CANCELED;


    public boolean esteFinala()
    {
        return this==DELIVERED || this==CANCELED;
    }

    public boolean esteInitiala()
    {
        return this==PLACED || this==CANCELED;
    }

    public StareComanda next() throws IllegalStateException{
        if(this.esteFinala()) throw new IllegalStateException("Comanda este in stare finala!");
        return switch(this)
        {
            case PLACED -> PROCESSED;
            case PROCESSED -> SHIPPED;
            case SHIPPED -> DELIVERED;
            default -> this;
        };
    }

    public StareComanda cancel() throws IllegalStateException{
        if(this.esteFinala()) throw new IllegalStateException("Comanda este in stare finala!");
        return CANCELED;
    }

}
