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

    /**
     * Este método verifica que puedas colocar una piedra revisando
     * si la piedra existe, si está dentro del tablero y si la casilla
     * existe, modifica la posición de la piedra.
     */

    public boolean colocar(PiedraShobu p, int x, int y) {
        if (p == null) return false;
        if (!dentro(x, y)) return false;
        if (!estaVacio(x, y)) return false;


        piedra[x][y] = p;
        p.setPosX(x);
        p.setPosY(y);
        return true;
    }

    public boolean quitar(int x, int y) {
        if (!dentro(x, y)) return false;
        piedra[x][y] = null;
        return true;
    }

    /**
     * Recibe de parámetro dos coordenadas, una inicial y otra final
     * verifica si la segunda está ocupada por una piedra, si una piedra
     * está ocupándola, no hace nada, en caso de que sí, cambia los valores
     * de la posición inicial a null para decir que no tiene nada y los de
     * la segunda coordenada para asignar la piedra.
     */
    public boolean moverBasico(int fx, int fy, int tx, int ty) {
        if (!dentro(fx, fy) || !dentro(tx, ty)) return false;


        PiedraShobu p = piedra[fx][fy];
        if (p == null) return false;
        if (piedra[tx][ty] != null) return false;


        piedra[fx][fy] = null;
        piedra[tx][ty] = p;
        p.setPosX(tx);
        p.setPosY(ty);
        return true;
    }

    public int getAncho() { return 4; }
    public int getAlto()  { return 4; }
}