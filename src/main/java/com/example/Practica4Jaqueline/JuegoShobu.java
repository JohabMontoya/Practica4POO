package com.example.Practica4Jaqueline;

public class JuegoShobu {
    private final TableroShobu[] tableros = new TableroShobu[4];
    private final JugadorShobu[] jugadores = new JugadorShobu[2];

    private int jugadorActual = 0;

    public JuegoShobu() {
        jugadores[0] = new JugadorShobu(0);
        jugadores[1] = new JugadorShobu(1);

        for (int i = 0; i < 4; i++) {
            tableros[i] = new TableroShobu();
        }
    }

    public TableroShobu getTablero(int id) { return tableros[id]; }
    public int getJugadorActual() { return jugadorActual; }
}