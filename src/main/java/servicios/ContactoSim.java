package servicios;

import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import modelo.Punto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContactoSim implements InterfazContactoSim {
    private final List<DatosSolicitud> solicitudesProvisionales = new ArrayList<>();

    @Override
    public int solicitarSimulation(DatosSolicitud sol) {
        this.solicitudesProvisionales.add(sol);
        return new Random().nextInt(10000);
    }

    @Override
    public List<Entidad> getEntities() {
        List<Entidad> entidades = new ArrayList<>();
        entidades.add(new Entidad(1, "Planta Solar Central",""));
        entidades.add(new Entidad(2, "Refinería Hidrógeno",""));
        entidades.add(new Entidad(3, "Subestación Eléctrica",""));
        return entidades;
    }

    @Override
    public DatosSimulation descargarDatos(int ticket) {
        DatosSimulation ds = new DatosSimulation();
        Map<Integer, List<Punto>> mapaPuntos = new HashMap<>();
        int maxTiempo = 0;

        try {
            io.swagger.client.api.ResultadosApi api = new io.swagger.client.api.ResultadosApi();
            api.getApiClient().setBasePath("http://localhost:8080");

            io.swagger.client.model.ResultsResponse respuesta = api.resultadosPost("UsuarioPrueba",ticket);

            String textoDatos = respuesta.getData();

            if (textoDatos != null && !textoDatos.isEmpty()) {
                String[] lineas = textoDatos.split("\n");

                int ancho = Integer.parseInt(lineas[0].trim());
                ds.setAnchoTablero(ancho);

                for (int i = 1; i < lineas.length; i++) {
                    String lineaActual = lineas[i].trim();
                    if (lineaActual.isEmpty()) continue;

                    String[] partes = lineaActual.split(",");

                    int tiempo = Integer.parseInt(partes[0].trim());
                    int y = Integer.parseInt(partes[1].trim());
                    int x = Integer.parseInt(partes[2].trim());
                    String color = partes[3].trim();

                    if (tiempo > maxTiempo) {
                        maxTiempo = tiempo;
                    }

                    modelo.Punto nuevoPunto = new modelo.Punto(x, y, color);

                    mapaPuntos.putIfAbsent(tiempo, new ArrayList<>());

                    mapaPuntos.get(tiempo).add(nuevoPunto);
                }

                ds.setPuntos(mapaPuntos);
                ds.setMaxSegundos(maxTiempo);
            }
        } catch (Exception e) {
            System.err.println("Error al conectar con la VM: " + e.getMessage());
        }

        return ds; // Devolvemos el objeto lleno de datos listos para la web
    }

    @Override
    public boolean isValidEntityId(int id) {
        return true;
    }
}