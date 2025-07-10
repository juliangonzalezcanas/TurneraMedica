package Vista.Exceptions;

public class MedicoVacioException extends Exception{
    public MedicoVacioException() {
        super("Debe seleccionar un medico");
    }
}
