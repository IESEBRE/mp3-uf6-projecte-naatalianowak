package org.example.model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class DAOException extends Exception{
    /**
     * Mapa de missatges d'error
     */

    private static final Map<Integer, String> missatges = new HashMap<>();
    //num i retorna string, el map
    static {
        missatges.put(0, "Error al connectar a la BD!!");
        missatges.put(1, "Restricció d'integritat violada - clau primària duplicada");
        missatges.put(2, "Aquest camp té que començar en majúscula");
        missatges.put(34, "Aquest cap no pot contindre números");
        missatges.put(35, "Aquest cap no pot contindre lletres");
        missatges.put(36, "Canço és null!!");
        missatges.put(60, "Arxiu no trobat!");
        missatges.put(101, "Error de lectura i escriptura!!");
        missatges.put(904, "Nom de columna no vàlid");
        missatges.put(936, "Falta expressió en l'ordre SQL");
        missatges.put(942, "La taula o la vista no existeix");
        missatges.put(1403, "No s'ha trobat cap dada");
        missatges.put(1747, "El nombre de columnes de la vista no coincideix amb el nombre de columnes de les taules subjacents");
        missatges.put(6502, "Error numèric o de valor durant l'execució del programa");
        missatges.put(2292, "S'ha violat la restricció d'integritat -  s'ha trobat un registre fill");

    }

    //atribut
    private int tipo;

    //constructor al q pasem tipo
    public DAOException(int tipo){
        this.tipo=tipo;
    }

    //sobreescrivim el get message
        @Override
    public String getMessage(){
        return missatges.get(this.tipo); //el missatge del tipo
    }

    public int getTipo() {
        return tipo;
    }
}
