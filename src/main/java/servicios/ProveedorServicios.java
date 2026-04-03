package servicios;

import org.springframework.stereotype.Service;
import io.swagger.client.ApiClient;
import io.swagger.client.Configuration;
import io.swagger.client.api.EmailApi;
import io.swagger.client.api.SolicitudApi;
import io.swagger.client.model.EmailResponse;

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