package Entidades;
import java.time.LocalDateTime;


public class Medico extends Usuario{

    private Float precioConsulta;
    private String especialidad;


    public Medico(Integer id, String nombre, String apellido, Integer dni, String email, String obraSocial, Float precioConsulta, String especialidad, String password) {
        super(id, nombre, apellido, dni, email, obraSocial, password);
        this.precioConsulta = precioConsulta;
        this.especialidad = especialidad;
        
    }

    public Medico(String nombre, String apellido, Integer dni, String email, String obraSocial, Float precioConsulta, String especialidad, String password) {
        super(null, nombre, apellido, dni, email, obraSocial, password);
        this.precioConsulta = precioConsulta;
        this.especialidad = especialidad;
        
    }
    

    public Float getPrecioConsulta() {
        return precioConsulta;
    }

    public void setPrecioConsulta(Float precioConsulta) {
        this.precioConsulta = precioConsulta;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    

    public Float calcularTotalConsultas(LocalDateTime d1, LocalDateTime d2) {
        Float total = 0f;
        for (Turno t : super.getTurnos()) {
            if (t.getFecha().isAfter(d1) && t.getFecha().isBefore(d2)) {
                total += precioConsulta;
            }
        }
        return total;
    }

    @Override
    public String toString() {
        return getNombre() + " - " + getEspecialidad(); 
    }


}
