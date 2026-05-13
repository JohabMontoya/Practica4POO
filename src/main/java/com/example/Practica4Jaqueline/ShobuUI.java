package com.example.Practica4Jaqueline;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ShobuUI extends BorderPane {

    private final JuegoShobu juego;
    private final Map<String, Celda> celdas = new HashMap<>();
    private final List<MovimientoShobu> movimientosMostrados = new ArrayList<>();

    private boolean esperandoPasivo = true;
    private int dxRequerido = 0;
    private int dyRequerido = 0;
    private int distRequerida = 0;
    private boolean colorPasivoOscuro = false;

    private boolean modoVsMaquina = false;
    private final Label estadoLabel = new Label();

    public ShobuUI() {
        juego = new JuegoShobu();
        setPadding(new Insets(20));
        mostrarPantallaInicio();
    }

    private void mostrarPantallaInicio() {
        celdas.clear();
        movimientosMostrados.clear();
        esperandoPasivo = true;

        VBox inicio = new VBox(20);
        inicio.setAlignment(Pos.CENTER);

        Label titulo = new Label("Shobu");
        titulo.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");

        Button uno = new Button("1 Jugador");
        Button dos = new Button("2 Jugadores");

        uno.setStyle("-fx-font-size: 20px; -fx-padding: 12 30 12 30;");
        dos.setStyle("-fx-font-size: 20px; -fx-padding: 12 30 12 30;");

        uno.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                modoVsMaquina = true;
                iniciarJuego();
            }
        });

        dos.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                modoVsMaquina = false;
                iniciarJuego();
            }
        });

        inicio.getChildren().addAll(titulo, uno, dos);
        setCenter(inicio);
        setBottom(null);
    }

    private void iniciarJuego() {
        GridPane tableroGrid = construirTableros();
        setCenter(tableroGrid);
        setBottom(estadoLabel);

        actualizarEstado("Turno: Jugador (negras) - Movimiento pasivo");
        refrescarTableros();
    }

    private GridPane construirTableros() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        for (int tableroId = 0; tableroId < 4; tableroId++) {
            GridPane tablero = crearTablero(tableroId);
            int row = tableroId < 2 ? 0 : 1;
            int col = tableroId % 2;
            grid.add(tablero, col, row);
        }

        return grid;
    }

    private GridPane crearTablero(int tableroId) {
        GridPane tablero = new GridPane();
        tablero.setAlignment(Pos.CENTER);

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                Celda celda = new Celda(tableroId, x, y);
                tablero.add(celda, x, y);
                celdas.put(key(tableroId, x, y), celda);
            }
        }

        return tablero;
    }

    private void onCeldaClick(Celda celda) {
        if (juego.juegoTerminado()) return;
        if (modoVsMaquina && juego.getJugadorActual() == 0) return;

        if (esperandoPasivo) {
            manejarClickPasivo(celda);
        } else {
            manejarClickAgresivo(celda);
        }
    }

    private void manejarClickPasivo(Celda celda) {
        MovimientoShobu movimiento = buscarMovimientoEnDestino(celda);
        if (movimiento != null) {
            juego.moverPasivo(movimiento.getTableroId(), movimiento.getFx(), movimiento.getFy(),
                    movimiento.getTx(), movimiento.getTy(), juego.getJugadorActual());

            dxRequerido = movimiento.getDx();
            dyRequerido = movimiento.getDy();
            distRequerida = movimiento.getDistancia();
            colorPasivoOscuro = juego.esTableroOscuro(movimiento.getTableroId());

            esperandoPasivo = false;
            movimientosMostrados.clear();

            actualizarEstado("Movimiento agresivo: misma dirección y distancia");
            refrescarTableros();
            return;
        }

        if (juego.esTableroPropio(celda.tableroId, juego.getJugadorActual())) {
            TableroShobu t = juego.getTablero(celda.tableroId);
            if (t.esPiedraDel(celda.x, celda.y, juego.getJugadorActual())) {
                List<MovimientoShobu> movs = juego.obtenerMovimientosPasivosParaPiedra(
                        celda.tableroId, celda.x, celda.y, juego.getJugadorActual());

                List<MovimientoShobu> movsFiltrados = filtrarPasivosConAgresivo(movs);
                mostrarMovimientos(movsFiltrados);
            }
        }
    }

    private void manejarClickAgresivo(Celda celda) {
        MovimientoShobu movimiento = buscarMovimientoEnDestino(celda);
        if (movimiento != null) {
            juego.moverAgresivo(movimiento.getTableroId(), movimiento.getFx(), movimiento.getFy(),
                    movimiento.getTx(), movimiento.getTy(), juego.getJugadorActual());

            movimientosMostrados.clear();
            esperandoPasivo = true;

            int ganador = juego.obtenerGanador();
            if (ganador != -1) {
                mostrarFinPartida(nombreJugador(ganador));
                return;
            }

            juego.cambiarJugador();
            actualizarEstado("Turno: " + nombreJugador(juego.getJugadorActual()) + " - Movimiento pasivo");
            refrescarTableros();

            if (modoVsMaquina && juego.getJugadorActual() == 0) {
                ejecutarTurnoMaquinaConPausa();
            }
            return;
        }

        boolean mismoColor = juego.esTableroOscuro(celda.tableroId) == colorPasivoOscuro;
        if (!mismoColor) {
            TableroShobu t = juego.getTablero(celda.tableroId);
            if (t.esPiedraDel(celda.x, celda.y, juego.getJugadorActual())) {
                int tx = celda.x + dxRequerido;
                int ty = celda.y + dyRequerido;
                List<MovimientoShobu> movs = new ArrayList<>();
                if (t.esMovimientoAgresivoValido(celda.x, celda.y, tx, ty, juego.getJugadorActual())) {
                    movs.add(new MovimientoShobu(celda.tableroId, celda.x, celda.y, tx, ty));
                }
                mostrarMovimientos(movs);
            }
        }
    }

    private List<MovimientoShobu> filtrarPasivosConAgresivo(List<MovimientoShobu> pasivos) {
        List<MovimientoShobu> validos = new ArrayList<>();
        for (MovimientoShobu pasivo : pasivos) {
            boolean colorOscuro = juego.esTableroOscuro(pasivo.getTableroId());
            List<MovimientoShobu> agresivos = juego.obtenerMovimientosAgresivos(
                    juego.getJugadorActual(),
                    pasivo.getDx(),
                    pasivo.getDy(),
                    pasivo.getDistancia(),
                    colorOscuro
            );
            if (!agresivos.isEmpty()) {
                validos.add(pasivo);
            }
        }
        return validos;
    }

    private void mostrarMovimientos(List<MovimientoShobu> movs) {
        movimientosMostrados.clear();
        movimientosMostrados.addAll(movs);
        refrescarTableros();
    }

    private MovimientoShobu buscarMovimientoEnDestino(Celda celda) {
        for (MovimientoShobu mov : movimientosMostrados) {
            if (mov.getTableroId() == celda.tableroId && mov.getTx() == celda.x && mov.getTy() == celda.y) {
                return mov;
            }
        }
        return null;
    }

    private void ejecutarTurnoMaquinaConPausa() {
        TurnoShobu turno = juego.generarTurnoAleatorio(juego.getJugadorActual());
        if (turno == null) {
            mostrarFinPartida("Jugador (negras)");
            return;
        }

        MovimientoShobu pasivo = turno.getPasivo();
        MovimientoShobu agresivo = turno.getAgresivo();

        PauseTransition pausa1 = new PauseTransition(Duration.seconds(1.0));
        PauseTransition pausa2 = new PauseTransition(Duration.seconds(1.0));

        pausa1.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                juego.moverPasivo(pasivo.getTableroId(), pasivo.getFx(), pasivo.getFy(),
                        pasivo.getTx(), pasivo.getTy(), juego.getJugadorActual());
                refrescarTableros();
                pausa2.play();
            }
        });

        pausa2.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                juego.moverAgresivo(agresivo.getTableroId(), agresivo.getFx(), agresivo.getFy(),
                        agresivo.getTx(), agresivo.getTy(), juego.getJugadorActual());

                int ganador = juego.obtenerGanador();
                if (ganador != -1) {
                    mostrarFinPartida(nombreJugador(ganador));
                    return;
                }

                juego.cambiarJugador();
                actualizarEstado("Turno: Jugador (negras) - Movimiento pasivo");
                refrescarTableros();
            }
        });

        pausa1.play();
    }

    private void mostrarFinPartida(String ganador) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Fin de partida");
        alerta.setHeaderText("Ganador: " + ganador);
        alerta.setContentText("¿Deseas volver a la pantalla de inicio?");

        ButtonType volver = new ButtonType("Volver");
        ButtonType quedarse = new ButtonType("Quedarse");
        alerta.getButtonTypes().setAll(volver, quedarse);

        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == volver) {
            juego.inicializar();
            mostrarPantallaInicio();
        }
    }

    private void refrescarTableros() {
        for (Celda celda : celdas.values()) {
            celda.actualizar();
        }

        for (MovimientoShobu mov : movimientosMostrados) {
            Celda celda = celdas.get(key(mov.getTableroId(), mov.getTx(), mov.getTy()));
            if (celda != null) {
                celda.setHighlight(true);
            }
        }
    }

    private void actualizarEstado(String texto) {
        estadoLabel.setText(texto);
    }

    private String nombreJugador(int id) {
        return id == 1 ? "Jugador (negras)" : "Máquina (blancas)";
    }

    private String key(int tableroId, int x, int y) {
        return tableroId + "-" + x + "-" + y;
    }

    private class Celda extends StackPane {
        private final int tableroId;
        private final int x;
        private final int y;

        public Celda(int tableroId, int x, int y) {
            this.tableroId = tableroId;
            this.x = x;
            this.y = y;

            setPrefSize(60, 60);
            setAlignment(Pos.CENTER);
            setStyle(baseStyle());

            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    onCeldaClick(Celda.this);
                }
            });
        }

        public void actualizar() {
            getChildren().clear();
            setStyle(baseStyle());

            PiedraShobu p = juego.getTablero(tableroId).getPiedraEn(x, y);
            if (p != null) {
                Circle circle = new Circle(20);
                if (p.getJugadorId() == 0) {
                    circle.setFill(Color.web("#f2f2f2"));
                    circle.setStroke(Color.web("#333333"));
                } else {
                    circle.setFill(Color.web("#1a1a1a"));
                    circle.setStroke(Color.web("#f2f2f2"));
                }
                getChildren().add(circle);
            }
        }

        public void setHighlight(boolean highlight) {
            if (highlight) {
                setStyle("-fx-background-color: #8bc34a; -fx-border-color: #4caf50; -fx-border-width: 2;");
            } else {
                setStyle(baseStyle());
            }
        }

        private String baseStyle() {
            boolean oscuro = (tableroId == 0 || tableroId == 2);
            String color = oscuro ? "#d0d0d0" : "#ffffff";
            String borde = oscuro ? "#888888" : "#aaaaaa";

            return "-fx-background-color: " + color +
                    "; -fx-border-color: " + borde +
                    "; -fx-border-width: 1;";
        }
    }
}