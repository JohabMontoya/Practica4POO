package com.example.Practica4Jaqueline;

public class TableroShobu {

    private PiedraShobu[][] piedras;

    public TableroShobu() {
        this.piedras = new PiedraShobu[4][4];
    }

    public boolean dentro(int x, int y) {
        return x >= 0 && x < 4 && y >= 0 && y < 4;
    }

    public PiedraShobu getPiedraEn(int x, int y) {
        if (!dentro(x, y)) return null;
        return piedras[x][y];
    }

    public boolean esPiedraDel(int x, int y, int jugadorId) {
        PiedraShobu p = getPiedraEn(x, y);
        if (p == null) return false;
        return p.getJugadorId() == jugadorId;
    }

    public boolean estaVacio(int x, int y) {
        return getPiedraEn(x, y) == null;
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

    public int[] calcularDireccion(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return new int[]{dx, dy};
    }

    public boolean esMovimientoValido(int fx, int fy, int tx, int ty) {
        if (!dentro(fx, fy) || !dentro(tx, ty)) return false;
        if (getPiedraEn(fx, fy) == null) return false;
        if (!estaVacio(tx, ty)) return false;

        int[] dir = calcularDireccion(fx, fy, tx, ty);
        int distancia = Math.max(Math.abs(dir[0]), Math.abs(dir[1]));

        return distancia > 0 && distancia <= 2;
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