package org.example.model.entities;

import java.util.Collection;
import java.util.TreeSet;

public class Canço {
    /**
     * Classe que representa una cançó
     */

    private Long id;
    private String nom;
    private double durada;
   // private boolean single;

    private Collection<Videoclip> videoclips;

    public Canço(){}

    public Canço(String nom, double durada, Collection<Videoclip> videoclips) {
        this.nom = nom;
        this.durada = durada;
       // this.single = single;
        this.videoclips=videoclips;
    }

    public Canço(Long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Canço(long id, String nom, double durada) {
        this.id = id;
        this.nom = nom;
        this.durada = durada;
    }

    public Canço(long id, String nom, double durada, TreeSet<Videoclip> videoclips) {
        this.id = id;
        this.nom = nom;
        this.durada = durada;
        this.videoclips = videoclips;
    }

    public Collection<Canço.Videoclip> getVideoclips() {
        return videoclips;
    }

    private void setVideoclips(Collection<Videoclip> videoclips) {
        this.videoclips = videoclips;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDurada() {
        return durada;
    }

    public void setDurada(double durada) {
        this.durada = durada;
    }


    public static class Videoclip implements Comparable<Videoclip>{
        /**
         * Classe interna que representa un videoclip
         */

        @Override
        public int compareTo(Videoclip o) {
            return this.modul.compareTo(o.getModul());
        }

        public static enum Modul {
            /**
             * Enumeració de mòduls
             */
            Espanya(""), Portugal(""), França(""), Itàlia(""),
            Alemanya(""), RegneUnit(""), Suïssa(""), Holanda(""),
            Bèlgica(""), Luxemburg(""), Irlanda(""), Dinamarca(""),
            Grècia(""), Suècia(""), Finlàndia(""), Noruega(""),
            Islàndia(""), Malta(""), Xipre(""), Liechtenstein(""),
            Mònaco(""), Vaticà(""), Andorra("");

            private String nom;

            private Modul(String nom) {
                this.nom = nom;
            }

            public String getNom() {
                return nom;
            }

            @Override
            public String toString() {
                return this.name()+ " " + nom;
            }
        }

        private Videoclip.Modul modul;
        private int any;

        public Videoclip(Videoclip.Modul modul, int any) {
            this.modul = modul;
            this.any = any;
        }

        public Videoclip.Modul getModul() {
            return modul;
        }

        public void setModul(Videoclip.Modul modul) {
            this.modul = modul;
        }

        public int getAny() {
            return any;
        }

        public void setAny(int nota) {
            this.any = any;
        }
    }


}

