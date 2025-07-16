package Entidades;

public class Consultorio {
    private int id;
    private String nombre;
    private String direccion;

    public Consultorio(int id, String nombre, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }

    @Override
    public String toString() {
        return nombre + " - " + direccion;
    }
}
