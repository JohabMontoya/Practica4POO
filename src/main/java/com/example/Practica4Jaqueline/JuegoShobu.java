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
        inicializar();
    }


    private void inicializar() {
        for (int t = 0; t < 4; t++) {
            tableros[t].limpiar();

            for (int x = 0; x < 4; x++) {
                tableros[t].colocar(new PiedraShobu(0), x, 0);
                tableros[t].colocar(new PiedraShobu(1), x, 3);
            }
        }
    }

    public void cambiarJugador() {
        jugadorActual = (jugadorActual == 0) ? 1 : 0;
    }



    public TableroShobu getTablero(int id) { return tableros[id]; }
    public int getJugadorActual() { return jugadorActual; }
}