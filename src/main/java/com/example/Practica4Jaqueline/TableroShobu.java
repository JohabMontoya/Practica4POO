package com.example.Practica4Jaqueline;

public class TableroShobu {

    private PiedraShobu[][] piedra;

    public TableroShobu() {
        this.piedra = new PiedraShobu[4][4];
    }

    public boolean dentro(int x, int y) {
        return x >= 0 && x < 4 && y >= 0 && y < 4;
    }

    public PiedraShobu get(int x, int y) {
        if (!dentro(x, y)) return null;
        return piedra[x][y];
    }

    public boolean estaVacio(int x, int y) {
        return get(x, y) == null;
    }

    public int getAncho() { return 4; }
    public int getAlto()  { return 4; }
}