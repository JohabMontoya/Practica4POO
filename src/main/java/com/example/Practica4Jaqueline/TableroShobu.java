package com.example.Practica4Jaqueline;

public class TableroShobu {

    private PiedraShobu[][] piedras;

    public TableroShobu() {
        this.piedras = new PiedraShobu[4][4];
    }

    public boolean dentro(int x, int y) {
        return x >= 0 && x < 4 && y >= 0 && y < 4;
    }

    public PiedraShobu get(int x, int y) {
        if (!dentro(x, y)) return null;
        return piedras[x][y];
    }

    public boolean estaVacio(int x, int y) {
        return get(x, y) == null;
    }

    public boolean colocar(PiedraShobu p, int x, int y) {
        if (p == null) return false;
        if (!dentro(x, y)) return false;
        if (!estaVacio(x, y)) return false;

        piedras[x][y] = p;
        p.setPosX(x);
        p.setPosY(y);
        return true;
    }

    public boolean quitar(int x, int y) {
        if (!dentro(x, y)) return false;
        piedras[x][y] = null;
        return true;
    }

    public void limpiar() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                piedras[x][y] = null;
            }
        }
    }

    public boolean moverBasico(int fx, int fy, int tx, int ty) {
        if (!dentro(fx, fy) || !dentro(tx, ty)) return false;

        PiedraShobu p = piedras[fx][fy];
        if (p == null) return false;
        if (piedras[tx][ty] != null) return false;

        piedras[fx][fy] = null;
        piedras[tx][ty] = p;
        p.setPosX(tx);
        p.setPosY(ty);
        return true;
    }

    /**
     * Cuenta cuántas piedras de un jugador hay en un tablero
     * sirve para determinar el ganador
     */
    public int contarPiedras(int jugadorId) {
        int contador = 0;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (piedras[x][y] != null && piedras[x][y].getJugadorId() == jugadorId) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public int getAncho() { return 4; }
    public int getAlto()  { return 4; }
}