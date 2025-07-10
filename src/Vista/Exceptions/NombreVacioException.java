package Vista.Exceptions;

public class NombreVacioException extends Exception {
    public NombreVacioException() {
        super("El nombre no puede estar vacío");
    }
    
}
