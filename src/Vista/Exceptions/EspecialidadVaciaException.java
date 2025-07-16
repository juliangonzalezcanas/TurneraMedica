package Vista.Exceptions;

public class EspecialidadVaciaException extends Exception {
    public EspecialidadVaciaException() {
        super("La especialidad no puede estar vacía.");
    }
    
}
