package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.*;

import java.util.Stack;

public class Order {

    private StareComanda stareCurenta;
    private Stack<StareComanda> istoric = new Stack<>();

    public Order(StareComanda stareInitiala) {
        this.stareCurenta = stareInitiala;
    }

    public void nextState() throws OrderIsAlreadyFinalException {
        if (stareCurenta.esteFinala()) {
            throw new OrderIsAlreadyFinalException();
        }

        istoric.push(stareCurenta);
        stareCurenta = stareCurenta.next();
        System.out.println("Order state updated to: "+ stareCurenta);
    }

    public void cancel() throws CannotCancelFinalOrderException {
        if (stareCurenta.esteFinala()) {
            throw new CannotCancelFinalOrderException();
        }

        istoric.push(stareCurenta);
        stareCurenta = stareCurenta.cancel();
        System.out.println("Order has been canceled.");
    }

    public void undoState() throws CannotRevertInitialOrderStateException {
        if (istoric.isEmpty()) {
            throw new CannotRevertInitialOrderStateException();
        }

        stareCurenta = istoric.pop();
        System.out.println("Order state reverted to: "+ stareCurenta);
    }
}