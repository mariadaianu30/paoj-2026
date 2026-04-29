package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Adresa;

public class Student implements Cloneable {
    private String nume;
    private int varsta;
    private com.pao.laboratory08.exercise1.Adresa adresa;

    public Student(String nume, int varsta, com.pao.laboratory08.exercise1.Adresa adresa) {
        this.nume = nume;
        this.varsta = varsta;
        this.adresa = adresa;
    }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public int getVarsta() { return varsta; }
    public void setVarsta(int varsta) { this.varsta = varsta; }

    public com.pao.laboratory08.exercise1.Adresa getAdresa() { return adresa; }
    public void setAdresa(com.pao.laboratory08.exercise1.Adresa adresa) { this.adresa = adresa; }

    @Override
    public String toString() {
        return "Student{nume='" + nume + "', varsta=" + varsta + ", adresa=" + adresa + "}";
    }

    // Shallow clone refoloseste aceeasi referinta la Adresa
    public Student shallowClone() throws CloneNotSupportedException {
        return (Student) super.clone();
    }

    // Deep clone cloneaza si obiectul Adresa
    public Student deepClone() throws CloneNotSupportedException {
        Student clona = (Student) super.clone();
        clona.setAdresa((Adresa) this.adresa.clone());
        return clona;
    }

    // clone() implicit (shallow) necesar pentru interfata Cloneable
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}