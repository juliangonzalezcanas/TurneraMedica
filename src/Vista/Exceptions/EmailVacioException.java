package Vista.Exceptions;

public class EmailVacioException extends Exception {
    public EmailVacioException() {
        super("El email no puede estar vacío");
    }
    
}
