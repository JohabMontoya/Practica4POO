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

    private boolean esLineaValida(int dx, int dy) {
        return dx == 0 || dy == 0 || Math.abs(dx) == Math.abs(dy);
    }

    public boolean esMovimientoPasivoValido(int fx, int fy, int tx, int ty, int jugadorId) {
        if (!dentro(fx, fy) || !dentro(tx, ty)) return false;
        PiedraShobu p = getPiedraEn(fx, fy);
        if (p == null || p.getJugadorId() != jugadorId) return false;
        if (!estaVacio(tx, ty)) return false;

        int dx = tx - fx;
        int dy = ty - fy;

        if (!esLineaValida(dx, dy)) return false;

        int distancia = Math.max(Math.abs(dx), Math.abs(dy));
        if (distancia < 1 || distancia > 2) return false;

        int stepX = 0;
        int stepY = 0;

        if (dx > 0) stepX = 1;
        if (dx < 0) stepX = -1;

        if (dy > 0) stepY = 1;
        if (dy < 0) stepY = -1;

        for (int i = 1; i < distancia; i++) {
            int x = fx + stepX * i;
            int y = fy + stepY * i;
            if (!estaVacio(x, y)) return false;
        }

        return true;
    }

    public boolean moverPasivo(int fx, int fy, int tx, int ty, int jugadorId) {
        if (!esMovimientoPasivoValido(fx, fy, tx, ty, jugadorId)) return false;

        PiedraShobu p = piedras[fx][fy];
        piedras[fx][fy] = null;
        piedras[tx][ty] = p;
        p.setPosX(tx);
        p.setPosY(ty);
        return true;
    }

    public boolean esMovimientoAgresivoValido(int fx, int fy, int tx, int ty, int jugadorId) {
        if (!dentro(fx, fy) || !dentro(tx, ty)) return false;

        PiedraShobu p = getPiedraEn(fx, fy);
        if (p == null || p.getJugadorId() != jugadorId) return false;

        int dx = tx - fx;
        int dy = ty - fy;
        if (!esLineaValida(dx, dy)) return false;

        int distancia = Math.max(Math.abs(dx), Math.abs(dy));
        if (distancia < 1 || distancia > 2) return false;

        int stepX = 0;
        int stepY = 0;

        if (dx > 0) stepX = 1;
        if (dx < 0) stepX = -1;

        if (dy > 0) stepY = 1;
        if (dy < 0) stepY = -1;

        if (distancia == 1) {
            PiedraShobu destino = getPiedraEn(tx, ty);
            if (destino == null) return true;
            if (destino.getJugadorId() == jugadorId) return false;

            int pushX = tx + stepX;
            int pushY = ty + stepY;
            if (!dentro(pushX, pushY)) return true;
            return estaVacio(pushX, pushY);
        }

        int ix = fx + stepX;
        int iy = fy + stepY;
        PiedraShobu intermedia = getPiedraEn(ix, iy);
        PiedraShobu destino = getPiedraEn(tx, ty);

        if (intermedia != null) {
            if (intermedia.getJugadorId() == jugadorId) return false;
            int pushX = tx + stepX;
            int pushY = ty + stepY;
            if (!dentro(pushX, pushY)) return true;
            return estaVacio(pushX, pushY);
        }

        if (destino == null) {
            return true;
        }

        if (destino.getJugadorId() == jugadorId) return false;

        int pushX = tx + stepX;
        int pushY = ty + stepY;
        if (!dentro(pushX, pushY)) return true;

        return estaVacio(pushX, pushY);
    }

    public boolean moverAgresivo(int fx, int fy, int tx, int ty, int jugadorId) {
        if (!esMovimientoAgresivoValido(fx, fy, tx, ty, jugadorId)) return false;

        int dx = tx - fx;
        int dy = ty - fy;

        int stepX = 0;
        int stepY = 0;

        if (dx > 0) stepX = 1;
        if (dx < 0) stepX = -1;

        if (dy > 0) stepY = 1;
        if (dy < 0) stepY = -1;

        int distancia = Math.max(Math.abs(dx), Math.abs(dy));

        if (distancia == 1) {
            PiedraShobu destino = getPiedraEn(tx, ty);
            if (destino != null) {
                int pushX = tx + stepX;
                int pushY = ty + stepY;
                if (dentro(pushX, pushY)) {
                    piedras[pushX][pushY] = destino;
                    destino.setPosX(pushX);
                    destino.setPosY(pushY);
                }
            }
        } else {
            int ix = fx + stepX;
            int iy = fy + stepY;
            PiedraShobu intermedia = getPiedraEn(ix, iy);
            PiedraShobu destino = getPiedraEn(tx, ty);

            if (intermedia != null) {
                int pushX = tx + stepX;
                int pushY = ty + stepY;
                if (dentro(pushX, pushY)) {
                    piedras[pushX][pushY] = intermedia;
                    intermedia.setPosX(pushX);
                    intermedia.setPosY(pushY);
                }
                piedras[ix][iy] = null;
            } else if (destino != null) {
                int pushX = tx + stepX;
                int pushY = ty + stepY;
                if (dentro(pushX, pushY)) {
                    piedras[pushX][pushY] = destino;
                    destino.setPosX(pushX);
                    destino.setPosY(pushY);
                }
                piedras[tx][ty] = null;
            }
        }

        PiedraShobu p = piedras[fx][fy];
        piedras[fx][fy] = null;
        piedras[tx][ty] = p;
        p.setPosX(tx);
        p.setPosY(ty);
        return true;
    }

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