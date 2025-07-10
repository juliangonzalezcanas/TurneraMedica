package Vista.Exceptions;

public class PrecioConsultaVacioException extends Exception {
    public PrecioConsultaVacioException() {
        super("El precio de la consulta no puede estar vacío");
    }

}
