package Entidades;

import java.time.LocalDate;

public class Atencion {
    private Integer id;
    private Medico medico;
    private Consultorio consultorio;
    private LocalDate desde;
    private LocalDate hasta;

    public Atencion(Integer id, Medico medico, Consultorio consultorio, LocalDate desde, LocalDate hasta) {
        this.id = id;   
        this.medico = medico;
        this.consultorio = consultorio;
        this.desde = desde;
        this.hasta = hasta;
    }

    public Atencion( Medico medico, Consultorio consultorio, LocalDate desde, LocalDate hasta) {
        this.id = null;   
        this.medico = medico;
        this.consultorio = consultorio;
        this.desde = desde;
        this.hasta = hasta;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Consultorio getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }

    public LocalDate getDesde() {
        return desde;
    }

    public void setDesde(LocalDate desde) {
        this.desde = desde;
    }

    public LocalDate getHasta() {
        return hasta;
    }

    public void setHasta(LocalDate hasta) {
        this.hasta = hasta;
    }

    public boolean consultorioOcupado(LocalDate fecha, Consultorio consultorio) {
        return (consultorio == this.consultorio) && ((fecha.isEqual(desde) || fecha.isAfter(desde)) &&
               (fecha.isEqual(hasta) || fecha.isBefore(hasta)));
    }
}
