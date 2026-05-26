package com.pao.project.audit;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {

    private static volatile AuditService instance;
    private static final String AUDIT_FILE = "audit.csv";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) {
            synchronized (AuditService.class) {
                if (instance == null) {
                    instance = new AuditService();
                }
            }
        }
        return instance;
    }

    public synchronized void log(String actiune) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
            pw.println(actiune + "," + LocalDateTime.now().format(FMT));
        } catch (IOException e) {
            System.err.println("[AuditService] Eroare scriere audit.csv: " + e.getMessage());
        }
    }
}
