package Persistencia.Archivo;

import java.util.List;

import Entidades.Turno;
import Persistencia.ICrud;

public class ArchivoTurnoDao implements ICrud<Turno> {

    @Override
    public void grabar(Turno t) {
        // Implementación para grabar un turno en un archivo
    }

    @Override
    public void modificar(Turno t) {
        // Implementación para modificar un turno en un archivo
    }

    @Override
    public List<Turno> leerTodos() {
        // Implementación para leer todos los turnos desde un archivo
        return null; // Retornar la lista de turnos leídos
    }

    @Override
    public void eliminar(Integer id) {
        // Implementación para eliminar un turno por ID en un archivo
    }

    @Override
    public Turno leer(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leer'");
    }
    
}
