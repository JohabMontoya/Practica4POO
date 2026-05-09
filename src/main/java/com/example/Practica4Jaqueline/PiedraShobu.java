package com.example.Practica4Jaqueline;

public class PiedraShobu {
    private int posX;
    private int posY;
    private int jugadorId; // 0 o 1

    public PiedraShobu(int posX, int posY, int jugadorId) {
        this.posX = posX;
        this.posY = posY;
        this.jugadorId = jugadorId;
    }

    public int getPosX() { return posX; }
    public int getPosY() { return posY; }
    public int getJugadorId() { return jugadorId; }

    public void setPosX(int posX) { this.posX = posX; }
    public void setPosY(int posY) { this.posY = posY; }
}