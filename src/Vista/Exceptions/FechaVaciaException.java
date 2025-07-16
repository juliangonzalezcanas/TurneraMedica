package Vista.Exceptions;

public class FechaVaciaException extends Exception {
    public FechaVaciaException() {
        super("Error: La fecha no puede estar vacía.");
    }

    public FechaVaciaException(String message) {
        super(message);
    }
    
}
