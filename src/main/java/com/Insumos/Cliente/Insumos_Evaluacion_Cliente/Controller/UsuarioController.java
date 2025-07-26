package com.Insumos.Cliente.Insumos_Evaluacion_Cliente.Controller;

import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Result;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Transaccion;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Usuario;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Contrato;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/contratos")
public class UsuarioController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public String index(Model model) {
        String url = "http://localhost:8081/Api";

        ResponseEntity<Result<List<Transaccion>>> response = restTemplate.exchange(url,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Transaccion>>>() {
        }
        );

        // Manejo seguro de nulos y acceso al campo object
        List<Transaccion> transacciones = Optional.ofNullable(response.getBody())
                .filter(result -> result.correct) // Solo si la respuesta es correcta
                .map(result -> result.object) // Accede al campo object directamente
                .orElse(Collections.emptyList()); // Valor por defecto

        model.addAttribute("listaTransacciones", transacciones);
        return "Transacciones";
    }

    @GetMapping("/Usuarios")
    public String usuario(Model model) {
        String url = "http://localhost:8081/Api/Usuarios";

        ResponseEntity<Result<List<Usuario>>> response = restTemplate.exchange(url,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Usuario>>>() {
        }
        );

        List<Usuario> usuarios = Optional.ofNullable(response.getBody())
                .filter(result -> result.correct) // Solo si la respuesta es correcta
                .map(result -> result.object) // Accede al campo object directamente
                .orElse(Collections.emptyList()); // Valor por defecto
        model.addAttribute("listaUsuarios", usuarios);
        return "Usuarios";
    }

    @GetMapping("/Contratos/{IdUsuario}")
    public String ContratoByIdUsuario(@PathVariable int IdUsuario, Model model) {
        String url = "http://localhost:8081/Api/Contratos/" + IdUsuario;

        try {
            RestTemplate restTemplate = new RestTemplate();

            // Usa ParameterizedTypeReference para el tipo específico
            ResponseEntity<Result<List<Contrato>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Result<List<Contrato>>>() {
            }
            );

            Result<List<Contrato>> result = response.getBody();

            // Verificación nula y correctitud
            if (result != null && result.correct) {  // Accede directamente al campo 'correct'
                model.addAttribute("contratos", result.object);  // Accede al campo 'object'
                model.addAttribute("idUsuario", IdUsuario);
            } else {
                String errorMsg = result != null
                        ? (result.errorMessage != null ? result.errorMessage : "Error desconocido")
                        : "Respuesta vacía de la API";
                model.addAttribute("error", errorMsg);
            }

        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener contratos: " + e.getMessage());
            e.printStackTrace();
        }

        return "Contratos";
    }
}
