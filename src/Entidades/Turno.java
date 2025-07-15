package Entidades;
import java.time.LocalDateTime;

public class Turno {
    private Integer id;
    private LocalDateTime fechaHora;
    private Medico medico;
    private Paciente paciente;
    private Float precioConsulta;

    public Turno(Integer id, LocalDateTime fechaHora, Medico medico, Paciente paciente) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.medico = medico;
        this.paciente = paciente;
        this.precioConsulta = calcularPrecioConsulta();
    }

    public Turno( LocalDateTime fechaHora, Medico medico, Paciente paciente) {
        this.id = null;
        this.fechaHora = fechaHora;
        this.medico = medico;
        this.paciente = paciente;
        this.precioConsulta = calcularPrecioConsulta();
    }

    public LocalDateTime getFecha() {
        return fechaHora;
    }

    public void setFecha(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Float getPrecioConsulta() {
        return precioConsulta;
    }

    public void setPrecioConsulta(Float precioConsulta) {
        this.precioConsulta = precioConsulta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float calcularPrecioConsulta(){
        if(this.medico.getObraSocial() == this.paciente.getObraSocial()){
            return this.medico.getPrecioConsulta() * 0.5f; // 50% de descuento
        } else {
            return this.medico.getPrecioConsulta();
        }
        
    }

    @Override
    public String toString() {
        return "Turno " + id + " - " + fechaHora.toLocalDate() + " " + fechaHora.toLocalTime();
    }

}
