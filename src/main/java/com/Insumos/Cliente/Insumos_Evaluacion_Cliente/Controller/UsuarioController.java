package com.Insumos.Cliente.Insumos_Evaluacion_Cliente.Controller;

import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Result;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.TransaccionDTO;
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

        ResponseEntity<Result<List<TransaccionDTO>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<TransaccionDTO>>>() {
        }
        );

        // Manejo seguro de nulos y acceso al campo object
        List<TransaccionDTO> transacciones = Optional.ofNullable(response.getBody())
                .filter(result -> result.correct) // Solo si la respuesta es correcta
                .map(result -> result.object) // Accede al campo object directamente
                .orElse(Collections.emptyList()); // Valor por defecto

        model.addAttribute("listaTransacciones", transacciones);
        return "Contratos";
    }
}
