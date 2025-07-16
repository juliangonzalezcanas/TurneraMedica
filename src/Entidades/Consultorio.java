package Entidades;

public class Consultorio {
    private Integer id;
    private String nombre;
    private String direccion;

    public Consultorio(int id, String nombre, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public Consultorio(String nombre, String direccion) {
        this.id = null;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    @Override
    public String toString() {
        return nombre + " - " + direccion;
    }
}
