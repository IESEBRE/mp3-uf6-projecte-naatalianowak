-- Poseu el codi dels procediments/funcions emmagatzemats, triggers, ..., usats al projecte
--Taula cançó
CREATE TABLE CANÇO (
    id NUMBER PRIMARY KEY,
    nom VARCHAR2(100),
    durada NUMBER
);
--Taula videoclip
CREATE TABLE CANÇO_VIDEOCLIP (
    id NUMBER PRIMARY KEY,
    canco_id NUMBER,
    pais_produccio VARCHAR2(255) NOT NULL,
    any_produccio VARCHAR2(200),
    FOREIGN KEY (canco_id) REFERENCES CANÇO(id)
);
--Se connecta, estan les dos taules creades
