package Vista.Exceptions;

public class ObraSocialVaciaException extends Exception {
    public ObraSocialVaciaException() {
        super("La obra social no puede estar vacía");
    }

}
