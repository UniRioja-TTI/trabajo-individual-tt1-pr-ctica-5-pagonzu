package servicios;

import org.springframework.stereotype.Service;
import utilidades.ApiClient;
import utilidades.Configuration;
import utilidades.api.EmailApi;
import utilidades.api.SolicitudApi;
import utilidades.model.EmailResponse;

@Service
public class ProveedorServicios {

    private final EmailApi emailApi;
    private final SolicitudApi solicitudApi;

    public ProveedorServicios() {
        ApiClient cliente = Configuration.getDefaultApiClient();
        cliente.setBasePath("http://localhost:8080");

        this.emailApi = new EmailApi(cliente);
        this.solicitudApi = new SolicitudApi(cliente);
    }

    public boolean mandarNotificacion(String destino, String texto) {
        try {
            EmailResponse respuesta = emailApi.emailPost(destino, texto);
            return respuesta.isDone();
        } catch (Exception e) {
            System.err.println("Error al conectar con la VM: " + e.getMessage());
            return false;
        }
    }
}