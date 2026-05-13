package com.example.Practica4Jaqueline;

public class MovimientoShobu {
    private final int tableroId;
    private final int fx;
    private final int fy;
    private final int tx;
    private final int ty;

    public MovimientoShobu(int tableroId, int fx, int fy, int tx, int ty) {
        this.tableroId = tableroId;
        this.fx = fx;
        this.fy = fy;
        this.tx = tx;
        this.ty = ty;
    }

    public int getTableroId() { return tableroId; }
    public int getFx() {
        return fx;
    }
    public int getFy() {
        return fy;
    }
    public int getTx() {
        return tx;
    }
    public int getTy() {
        return ty;
    }

    public int getDx() {
        return tx - fx;
    }
    public int getDy() {
        return ty - fy;
    }
    public int getDistancia() {
        return Math.max(Math.abs(getDx()), Math.abs(getDy()));
    }
}