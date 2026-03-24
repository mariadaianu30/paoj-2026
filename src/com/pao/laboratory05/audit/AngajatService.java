package com.pao.laboratory05.audit;

import java.time.LocalDateTime;
import java.util.*;

public class AngajatService {
    
    private Angajat[] angajati;
    private AuditEntry[] auditLog;
    private AngajatService() {
        angajati = new Angajat[0];
        auditLog= new AuditEntry[0];
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    private void logAction(String action, String target)
    {
        AuditEntry entry = new AuditEntry(action,target,LocalDateTime.now().toString());
        AuditEntry[] newAudit= new AuditEntry[auditLog.length+1];

        for(int i=0;i<auditLog.length;i++)
                newAudit[i]=auditLog[i];
        
        newAudit[auditLog.length]=entry;
        auditLog=newAudit;

    }

    public void addAngajat(Angajat a)
    {
        Angajat[] angajatiNoi= new Angajat[angajati.length+1];

        for(int i=0;i<angajati.length;i++)
                angajatiNoi[i]=angajati[i];
        
        angajatiNoi[angajati.length]=a;
        angajati=angajatiNoi;
        logAction("ADD", a.getNume());
        System.out.println("Angajat nou adaugat cu succes!");
    }

    public void printAll()
    {
        for(Angajat a:angajati)
            System.out.println(a);
    }

     public void listBySalary() {
        Angajat[] copie = angajati.clone();
        Arrays.sort(copie);

        for (Angajat a : copie) {
            System.out.println(a);
        }
    }

    public void findByDepartament(String numeDept)
    {
        boolean gasit=false;

        for(Angajat angajat: angajati)
        {
            if(angajat.getDepartament().nume().equalsIgnoreCase(numeDept))
               { gasit=true;
                System.out.println(angajat);
                logAction("FIND_BY_DEPT", numeDept);
               }

        }

        if(!gasit)
            System.out.println("Niciun angajat în departamentul: "+ numeDept);
    }

    public void printAuditLog()
    {
        for(AuditEntry a: auditLog)
            System.out.println(a);
    }

}

