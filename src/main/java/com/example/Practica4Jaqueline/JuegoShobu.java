package com.example.Practica4Jaqueline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class JuegoShobu {
    private final TableroShobu[] tableros = new TableroShobu[4];
    private final JugadorShobu[] jugadores = new JugadorShobu[2];

    private int jugadorActual = 1;
    private final Random random = new Random();

    public JuegoShobu() {
        jugadores[0] = new JugadorShobu(0);
        jugadores[1] = new JugadorShobu(1);

        for (int i = 0; i < 4; i++) {
            tableros[i] = new TableroShobu();
        }
        inicializar();
    }

    public void inicializar() {
        for (int t = 0; t < 4; t++) {
            tableros[t].limpiar();
            for (int x = 0; x < 4; x++) {
                tableros[t].colocar(new PiedraShobu(0), x, 0);
                tableros[t].colocar(new PiedraShobu(1), x, 3);
            }
        }
        jugadorActual = 1;
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

    public int obtenerGanador() {
        for (int i = 0; i < 4; i++) {
            if (tableros[i].contarPiedras(0) == 0) return 1;
            if (tableros[i].contarPiedras(1) == 0) return 0;
        }
        return -1;
    }

    public boolean esTableroOscuro(int tableroId) {
        return tableroId == 0 || tableroId == 2;
    }

    public boolean esTableroPropio(int tableroId, int jugadorId) {
        if (jugadorId == 0) {
            return tableroId == 0 || tableroId == 1;
        } else {
            return tableroId == 2 || tableroId == 3;
        }
    }

    public boolean esMovimientoPasivoValido(int tableroId, int fx, int fy, int tx, int ty, int jugadorId) {
        if (!esTableroPropio(tableroId, jugadorId)) return false;
        return tableros[tableroId].esMovimientoPasivoValido(fx, fy, tx, ty, jugadorId);
    }

    public boolean moverPasivo(int tableroId, int fx, int fy, int tx, int ty, int jugadorId) {
        if (!esMovimientoPasivoValido(tableroId, fx, fy, tx, ty, jugadorId)) return false;
        return tableros[tableroId].moverPasivo(fx, fy, tx, ty, jugadorId);
    }

    public boolean moverAgresivo(int tableroId, int fx, int fy, int tx, int ty, int jugadorId) {
        return tableros[tableroId].moverAgresivo(fx, fy, tx, ty, jugadorId);
    }

    public List<MovimientoShobu> obtenerMovimientosPasivosParaPiedra(int tableroId, int fx, int fy, int jugadorId) {
        List<MovimientoShobu> movimientos = new ArrayList<>();
        if (!esTableroPropio(tableroId, jugadorId)) return movimientos;

        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                if (dx == 0 && dy == 0) {
                    // nada
                } else {
                    boolean linea = (dx == 0 || dy == 0 || Math.abs(dx) == Math.abs(dy));
                    if (linea) {
                        int tx = fx + dx;
                        int ty = fy + dy;
                        int dist = Math.max(Math.abs(dx), Math.abs(dy));
                        if (dist >= 1 && dist <= 2) {
                            if (esMovimientoPasivoValido(tableroId, fx, fy, tx, ty, jugadorId)) {
                                movimientos.add(new MovimientoShobu(tableroId, fx, fy, tx, ty));
                            }
                        }
                    }
                }
            }
        }
        return movimientos;
    }

    public List<MovimientoShobu> obtenerMovimientosPasivos(int jugadorId) {
        List<MovimientoShobu> movimientos = new ArrayList<>();
        for (int tableroId = 0; tableroId < 4; tableroId++) {
            if (esTableroPropio(tableroId, jugadorId)) {
                TableroShobu t = tableros[tableroId];

                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) {
                        if (t.esPiedraDel(x, y, jugadorId)) {
                            movimientos.addAll(obtenerMovimientosPasivosParaPiedra(tableroId, x, y, jugadorId));
                        }
                    }
                }
            }
        }
        return movimientos;
    }

    public List<MovimientoShobu> obtenerMovimientosAgresivos(int jugadorId, int dx, int dy, int dist, boolean colorOscuroPasivo) {
        List<MovimientoShobu> movimientos = new ArrayList<>();
        for (int tableroId = 0; tableroId < 4; tableroId++) {
            boolean mismoColor = esTableroOscuro(tableroId) == colorOscuroPasivo;
            if (!mismoColor) {
                TableroShobu t = tableros[tableroId];
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) {
                        if (t.esPiedraDel(x, y, jugadorId)) {
                            int tx = x + dx;
                            int ty = y + dy;
                            int distActual = Math.max(Math.abs(dx), Math.abs(dy));
                            if (distActual == dist) {
                                if (t.esMovimientoAgresivoValido(x, y, tx, ty, jugadorId)) {
                                    movimientos.add(new MovimientoShobu(tableroId, x, y, tx, ty));
                                }
                            }
                        }
                    }
                }
            }
        }
        return movimientos;
    }

    public TurnoShobu generarTurnoAleatorio(int jugadorId) {
        List<MovimientoShobu> pasivos = obtenerMovimientosPasivos(jugadorId);
        Collections.shuffle(pasivos, random);

        for (MovimientoShobu pasivo : pasivos) {
            boolean colorOscuro = esTableroOscuro(pasivo.getTableroId());
            List<MovimientoShobu> agresivos = obtenerMovimientosAgresivos(
                    jugadorId,
                    pasivo.getDx(),
                    pasivo.getDy(),
                    pasivo.getDistancia(),
                    colorOscuro
            );
            if (!agresivos.isEmpty()) {
                MovimientoShobu agresivo = agresivos.get(random.nextInt(agresivos.size()));
                return new TurnoShobu(pasivo, agresivo);
            }
        }
        return null;
    }

    public TableroShobu getTablero(int id) {
        return tableros[id];
    }
    public int getJugadorActual() {
        return jugadorActual;
    }
}