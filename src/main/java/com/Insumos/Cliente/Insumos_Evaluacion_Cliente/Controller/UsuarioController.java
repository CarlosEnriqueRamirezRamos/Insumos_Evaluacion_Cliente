package com.Insumos.Cliente.Insumos_Evaluacion_Cliente.Controller;

import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.AuthenticationResponse;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Contrato;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Login;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Registro;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Result;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Transaccion;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/contratos")
public class UsuarioController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String AUTH_API_BASE_URL = "http://localhost:8081/api/auth";
    private static final String RESOURCES_API_BASE_URL = "http://localhost:8081/Api";

    private HttpEntity<?> buildRequestWithToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

    private <T> HttpEntity<T> buildRequestWithBodyAndToken(T body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(body, headers);
    }

    @GetMapping
    public String login() {
        return "Login";
    }

    @GetMapping("/register-page")
    public String showRegisterPage() {
        return "Register";
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody Login loginRequest, HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Login> requestEntity = new HttpEntity<>(loginRequest, headers);

        try {
            ResponseEntity<AuthenticationResponse> response = restTemplate.exchange(
                    AUTH_API_BASE_URL + "/authenticate",
                    HttpMethod.POST,
                    requestEntity,
                    AuthenticationResponse.class
            );

            if (response.getBody() != null) {
                session.setAttribute("jwtToken", response.getBody().getToken());
            }

            return response;

        } catch (HttpClientErrorException e) {
            System.err.println("Error al autenticar: " + e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> register(@RequestBody Registro registerRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Registro> requestEntity = new HttpEntity<>(registerRequest, headers);

        try {
            ResponseEntity<AuthenticationResponse> response = restTemplate.exchange(
                    AUTH_API_BASE_URL + "/register",
                    HttpMethod.POST,
                    requestEntity,
                    AuthenticationResponse.class
            );
            return response;
        } catch (HttpClientErrorException e) {
            System.err.println("Error al registrar: " + e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/Index")
    public String index(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("jwtToken");
        if (token == null || token.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tu sesión ha expirado. Inicia sesión de nuevo.");
            return "redirect:/contratos";
        }

        HttpEntity<?> entity = buildRequestWithToken(token);
        try {
            ResponseEntity<Result<List<Transaccion>>> response = restTemplate.exchange(
                    RESOURCES_API_BASE_URL,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<List<Transaccion>>>() {}
            );

            Result<List<Transaccion>> result = response.getBody();
            if (result != null && result.correct) {
                model.addAttribute("listaTransacciones", result.object);
            } else {
                model.addAttribute("error", result != null ? result.errorMessage : "Error desconocido");
            }

        } catch (Exception e) {
            model.addAttribute("error", "Error inesperado al cargar transacciones: " + e.getMessage());
        }

        return "Transacciones";
    }

    @GetMapping("/Usuarios")
    public String usuarios(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("jwtToken");
        if (token == null || token.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tu sesión ha expirado. Inicia sesión de nuevo.");
            return "redirect:/contratos";
        }

        HttpEntity<?> entity = buildRequestWithToken(token);
        try {
            ResponseEntity<Result<List<Usuario>>> response = restTemplate.exchange(
                    RESOURCES_API_BASE_URL + "/Usuarios",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<List<Usuario>>>() {}
            );

            Result<List<Usuario>> result = response.getBody();
            if (result != null && result.correct) {
                model.addAttribute("listaUsuarios", result.object);
            } else {
                model.addAttribute("error", result != null ? result.errorMessage : "Error desconocido");
            }

        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar usuarios: " + e.getMessage());
        }

        return "Usuarios";
    }

    @GetMapping("/Contratos/{IdUsuario}")
    public String contratoByIdUsuario(
            @PathVariable int IdUsuario,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        String token = (String) session.getAttribute("jwtToken");
        if (token == null || token.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tu sesión ha expirado. Inicia sesión de nuevo.");
            return "redirect:/contratos";
        }

        HttpEntity<?> entity = buildRequestWithToken(token);
        try {
            ResponseEntity<Result<List<Contrato>>> response = restTemplate.exchange(
                    RESOURCES_API_BASE_URL + "/Contratos/" + IdUsuario,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<List<Contrato>>>() {}
            );

            Result<List<Contrato>> result = response.getBody();
            if (result != null && result.correct) {
                model.addAttribute("contratos", result.object);
                model.addAttribute("idUsuario", IdUsuario);
            } else {
                model.addAttribute("error", result != null ? result.errorMessage : "Error desconocido");
            }

        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener contratos: " + e.getMessage());
        }

        return "Contratos";
    }
}
