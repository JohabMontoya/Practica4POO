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

    public boolean juegoTerminado() {
        for (int i = 0; i < 4; i++) {
            if (tableros[i].contarPiedras(0) == 0 || tableros[i].contarPiedras(1) == 0) {
                return true;
            }
        }
        return false;
    }

    public int getTableroOpuesto(int tableroId) {
        if (tableroId == 0) return 2; // Dark del jugador 0 → Dark del jugador 1
        if (tableroId == 1) return 3; // Light del jugador 0 → Light del jugador 1
        if (tableroId == 2) return 0; // Dark del jugador 1 → Dark del jugador 0
        if (tableroId == 3) return 1; // Light del jugador 1 → Light del jugador 0
        return -1; // Inválido
    }

    public TableroShobu getTablero(int id) { return tableros[id]; }
    public int getJugadorActual() { return jugadorActual; }
}