-- Cabinet Medical — Schema baza de date
-- Rulati cu: mysql -u root -p cabinet_medical < schema.sql
-- sau: SOURCE schema.sql; (in mysql console, dupa USE cabinet_medical;)

DROP TABLE IF EXISTS medicamente_reteta;
DROP TABLE IF EXISTS retete;
DROP TABLE IF EXISTS analize;
DROP TABLE IF EXISTS consultatii;
DROP TABLE IF EXISTS programari;
DROP TABLE IF EXISTS asistenti;
DROP TABLE IF EXISTS pacienti;
DROP TABLE IF EXISTS medici;

CREATE TABLE medici (
    id           INT           AUTO_INCREMENT PRIMARY KEY,
    parafa       VARCHAR(20)   NOT NULL UNIQUE,
    cnp          VARCHAR(13)   NOT NULL UNIQUE,
    nume         VARCHAR(100)  NOT NULL,
    prenume      VARCHAR(100)  NOT NULL,
    adresa       VARCHAR(255),
    id_angajat   VARCHAR(50),
    salariu      DECIMAL(10,2) NOT NULL,
    program      VARCHAR(20),
    specialitate VARCHAR(50)   NOT NULL,
    disponibil   BOOLEAN       NOT NULL DEFAULT TRUE
);

CREATE TABLE pacienti (
    id             INT          AUTO_INCREMENT PRIMARY KEY,
    cod_pacient    VARCHAR(8)   NOT NULL UNIQUE,
    cnp            VARCHAR(13)  NOT NULL UNIQUE,
    nume           VARCHAR(100) NOT NULL,
    prenume        VARCHAR(100) NOT NULL,
    adresa         VARCHAR(255),
    data_inscriere DATE         NOT NULL,
    tip_asigurare  VARCHAR(10)  NOT NULL
);

CREATE TABLE asistenti (
    id         INT           AUTO_INCREMENT PRIMARY KEY,
    cnp        VARCHAR(13)   NOT NULL UNIQUE,
    nume       VARCHAR(100)  NOT NULL,
    prenume    VARCHAR(100)  NOT NULL,
    adresa     VARCHAR(255),
    id_angajat VARCHAR(50),
    salariu    DECIMAL(10,2) NOT NULL,
    program    VARCHAR(20),
    grad       VARCHAR(50),
    certificat VARCHAR(100)
);

CREATE TABLE programari (
    id            INT         AUTO_INCREMENT PRIMARY KEY,
    id_programare VARCHAR(50) NOT NULL UNIQUE,
    id_pacient    INT         NOT NULL,
    id_medic      INT         NOT NULL,
    data_ora      DATETIME    NOT NULL,
    motiv         TEXT,
    status        VARCHAR(20) NOT NULL DEFAULT 'PROGRAMAT',
    FOREIGN KEY (id_pacient) REFERENCES pacienti(id) ON DELETE CASCADE,
    FOREIGN KEY (id_medic)   REFERENCES medici(id)   ON DELETE CASCADE
);

CREATE TABLE consultatii (
    id         INT      AUTO_INCREMENT PRIMARY KEY,
    id_medic   INT      NOT NULL,
    id_pacient INT      NOT NULL,
    data_ora   DATETIME NOT NULL,
    simptome   TEXT,
    diagnostic TEXT,
    FOREIGN KEY (id_medic)   REFERENCES medici(id),
    FOREIGN KEY (id_pacient) REFERENCES pacienti(id) ON DELETE CASCADE
);

CREATE TABLE retete (
    id             INT         AUTO_INCREMENT PRIMARY KEY,
    id_reteta      VARCHAR(50) NOT NULL UNIQUE,
    id_medic       INT         NOT NULL,
    id_pacient     INT         NOT NULL,
    id_consultatie INT,
    data_emiterii  DATE        NOT NULL,
    data_expirarii DATE        NOT NULL,
    FOREIGN KEY (id_medic)       REFERENCES medici(id),
    FOREIGN KEY (id_pacient)     REFERENCES pacienti(id) ON DELETE CASCADE,
    FOREIGN KEY (id_consultatie) REFERENCES consultatii(id)
);

CREATE TABLE medicamente_reteta (
    id         INT          AUTO_INCREMENT PRIMARY KEY,
    id_reteta  INT          NOT NULL,
    medicament VARCHAR(200) NOT NULL,
    FOREIGN KEY (id_reteta) REFERENCES retete(id) ON DELETE CASCADE
);

CREATE TABLE analize (
    id           INT          AUTO_INCREMENT PRIMARY KEY,
    id_pacient   INT          NOT NULL,
    nume_analiza VARCHAR(200) NOT NULL,
    rezultat     TEXT,
    FOREIGN KEY (id_pacient) REFERENCES pacienti(id) ON DELETE CASCADE
);
