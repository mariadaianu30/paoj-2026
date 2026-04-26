package com.pao.project.interfaces;

import com.pao.project.model.Consultatie;
import com.pao.project.model.Pacient;
import com.pao.project.model.Reteta;

public interface Consultabil {
    Consultatie consulta(Pacient pacient, String simptome);
    Reteta emiteReteta(Consultatie consultatie, String[] medicamente);
    boolean esteDisponibil();
}