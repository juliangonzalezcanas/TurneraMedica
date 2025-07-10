package Vista.Exceptions;

public class ApellidoVacioException extends Exception {
    public ApellidoVacioException() {
        super("El apellido no puede estar vacío");
    }
    
}
