package com.example.Practica4Jaqueline;

public class TurnoShobu {
    private final MovimientoShobu pasivo;
    private final MovimientoShobu agresivo;

    public TurnoShobu(MovimientoShobu pasivo, MovimientoShobu agresivo) {
        this.pasivo = pasivo;
        this.agresivo = agresivo;
    }

    public MovimientoShobu getPasivo() {
        return pasivo;
    }
    public MovimientoShobu getAgresivo() {
        return agresivo;
    }
}