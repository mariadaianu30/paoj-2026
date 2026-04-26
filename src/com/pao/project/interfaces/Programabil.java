package com.pao.project.interfaces;

import com.pao.project.model.Programare;

public interface Programabil {
    void adaugaProgramare(Programare programare);
    void anuleazaProgramare(String idProgramare);
    boolean areConflictOrar(Programare programare);
}
