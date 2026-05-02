package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {

    static final String FILE = "output/lab09_ex2.bin";

    /// fiecare inregistrare are exact 32 de bytes
    static final int RECORD_SIZE = 32;


    //conversie int in little endian
    static void writeIntLE(DataOutputStream dos, int v) throws IOException {
        dos.write(ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(v).array());
    }

    //conversie double in little endian
    static void writeDoubleLE(DataOutputStream dos, double v) throws IOException {
        dos.write(ByteBuffer.allocate(8)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putDouble(v).array());
    }


    //scrie un sg rand
    static void writeRecord(DataOutputStream dos, TranzactieRecord t) throws IOException {

        //id are 4 bytes si suma are 8
        writeIntLE(dos, t.id);
        writeDoubleLE(dos, t.suma);

        /// data are 10 bytes fix + padding
        byte[] data = new byte[10];
        byte[] src = t.data.getBytes();
        System.arraycopy(src, 0, data, 0, Math.min(src.length, 10));
        for (int i = src.length; i < 10; i++) data[i] = ' ';
        dos.write(data);

        dos.writeByte(t.tip == TipTranzactie.CREDIT ? 0 : 1);
        dos.writeByte(t.status.code);

        dos.write(new byte[8]);
    }


    /// citeste un record de la pozitia index
    static TranzactieRecord readRecord(RandomAccessFile raf, int idx) throws IOException {
        /// sare direct la inregistrarea x
        raf.seek((long) idx * RECORD_SIZE);

        //citeste 32 bytes
        byte[] buffer = new byte[RECORD_SIZE];
        raf.readFully(buffer);

        /// /transforma bytes in valori Java
        ByteBuffer bb = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);

        int id = bb.getInt();
        double suma = bb.getDouble();

        byte[] dataBytes = new byte[10];
        bb.get(dataBytes);
        String data = new String(dataBytes).trim();

        byte tipByte = bb.get();
        byte statusByte = bb.get();

        /// reconstruiesc enuml-ul in functie de tipul de byte
        TipTranzactie tip = (tipByte == 0) ? TipTranzactie.CREDIT : TipTranzactie.DEBIT;
        Status status = Status.fromCode(statusByte);

        /// /reconstruiecs obiectul
        TranzactieRecord t = new TranzactieRecord(id, suma, data, tip);
        t.status = status;

        return t;
    }

    //dau update doar la status
    static void updateStatus(int idx, Status status) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(FILE, "rw");
       // pozitia exacta a statusului
        // idx*32+23 (sunt 23 de bytes inainte)

        raf.seek((long) idx * RECORD_SIZE + 23);
        // scriu un sg byte
        raf.writeByte(status.code);
        raf.close();
    }

    /// citeste statusul direct din fisier
    static Status readStatus(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE + 23);
        return Status.fromCode(raf.readByte());
    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        /// memorie temporara input
        List<TranzactieRecord> list = new ArrayList<>();

        //nr tranzactii
        int n = Integer.parseInt(sc.nextLine());

        /// citire tranzactii
        for (int i = 0; i < n; i++) {
            String[] p = sc.nextLine().split(" ");

            list.add(new TranzactieRecord(
                    Integer.parseInt(p[0]),
                    Double.parseDouble(p[1]),
                    p[2],
                    TipTranzactie.valueOf(p[3])
            ));
        }

        // WRITE FILE
        new File("output").mkdirs();
        /// scriere fisier binar
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(FILE))) {
            for (TranzactieRecord t : list) {
                writeRecord(dos, t);
            }
        }

        //deschid fisierul pr citire si update
        RandomAccessFile raf = new RandomAccessFile(FILE, "rw");


        //procesez comenzi pana la eof
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            //afisez o inregistrare
            if (line.startsWith("READ")) {
                int idx = Integer.parseInt(line.split(" ")[1]);
                TranzactieRecord t = readRecord(raf, idx);

                System.out.printf("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n",
                        idx, t.id, t.data, t.tip, t.suma, t.status);

                /// modific statusul inregistrarii cu indexul dat , modific doar un byte
            } else if (line.startsWith("UPDATE")) {

                String[] p = line.split(" ");
                int idx = Integer.parseInt(p[1]);
                Status status = Status.fromString(p[2]);

                updateStatus(idx, status);

                System.out.println("Updated [" + idx + "]: " + status);

            } else if (line.equals("PRINT_ALL")) {

                for (int i = 0; i < list.size(); i++) {
                    TranzactieRecord t = readRecord(raf, i);

                    System.out.printf("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n",
                            i, t.id, t.data, t.tip, t.suma, t.status);
                }
            }
        }

        raf.close();
    }
}