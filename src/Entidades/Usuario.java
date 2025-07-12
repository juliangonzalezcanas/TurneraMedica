package Entidades;
import java.util.ArrayList;

public abstract class Usuario {
    private Integer id;
    private String nombre;
    private String apellido;
    private Integer dni;
    private String email;
    private ArrayList<Turno> turnos;
    private String obraSocial;
    private String password;

    public Usuario(Integer id, String nombre, String apellido, Integer dni, String email, String obraSocial, String password) {
        
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.turnos = new ArrayList<>();
        this.obraSocial = obraSocial;
        this.password = password;

    }


    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Turno> getTurnos() {
        return turnos;
    }

    public void setTurnos(ArrayList<Turno> turnos) {
        this.turnos = turnos;
    }

    public void agregarTurno(Turno t) {
        this.turnos.add(t);
    }

    public String getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }

    public Integer getId() {
        return id;
    }   

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
