package com.Insumos.Cliente.Insumos_Evaluacion_Cliente.Controller;

import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.AuthenticationResponse;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Result;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Transaccion;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Usuario;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Contrato;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Login;
import com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML.Registro;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// CAMBIO CLAVE: Cambiamos @RequestHeader por @RequestParam para leer el token de la URL
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException; // Para manejar 4xx errores
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Para mensajes flash en redirecciones

@Controller
@RequestMapping("/contratos")
public class UsuarioController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String AUTH_API_BASE_URL = "http://localhost:8081/api/auth";
    private static final String RESOURCES_API_BASE_URL = "http://localhost:8081/Api";

    @GetMapping
    public String login(Model model) {
        return "Login";
    }
    
  
    
    

    @GetMapping("/register-page") // Mapea a /contratos/register-page, devuelve Register.html
    public String showRegisterPage(Model model) {
        return "Register"; // Asegúrate de que "Register" coincida con el nombre de tu archivo Register.html
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody Login loginRequest) {
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
            return response;
        } catch (HttpClientErrorException e) {
            System.err.println("Error al autenticar: " + e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            System.err.println("Error de conexión/inesperado al autenticar: " + e.getMessage());
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
            System.err.println("Error de conexión/inesperado al registrar: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // --- MÉTODOS EXISTENTES: AHORA RECIBEN EL JWT COMO @RequestParam Y LO REENVÍAN ---
    @GetMapping("/Index")
    public String index(
            Model model,
            @RequestParam(value = "token", required = false) String tokenFromQuery, // <-- ¡RECIBE EL TOKEN DE LA URL!
            RedirectAttributes redirectAttributes // Para mensajes en redirecciones
    ) {
        String url = RESOURCES_API_BASE_URL;

        HttpHeaders headers = new HttpHeaders();
        if (tokenFromQuery != null && !tokenFromQuery.isEmpty()) {
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenFromQuery); // <-- ¡CONSTRUYE EL HEADER!
            model.addAttribute("jwtToken", tokenFromQuery); // Pasa el token a la vista para otros enlaces
        } else {
            // Si no hay token, significa que no está autenticado o la sesión expiró
            redirectAttributes.addFlashAttribute("errorMessage", "Tu sesión ha expirado o no tienes permisos. Por favor, inicia sesión de nuevo.");
            return "redirect:/contratos"; // Redirige al login
        }
        HttpEntity<String> entity = new HttpEntity<>(headers); // Envía los headers en la solicitud

        try {
            ResponseEntity<Result<List<Transaccion>>> response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    entity, // <-- ¡USA LA ENTIDAD CON LOS HEADERS!
                    new ParameterizedTypeReference<Result<List<Transaccion>>>() {
            }
            );

            List<Transaccion> transacciones = Optional.ofNullable(response.getBody())
                    .filter(result -> result.correct)
                    .map(result -> result.object)
                    .orElse(Collections.emptyList());

            model.addAttribute("listaTransacciones", transacciones);
            return "Transacciones"; // Devuelve la vista HTML
        } catch (HttpClientErrorException e) {
            // Manejo de errores 4xx de la API principal
            System.err.println("Error al cargar transacciones: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            if (e.getStatusCode() == org.springframework.http.HttpStatus.UNAUTHORIZED || e.getStatusCode() == org.springframework.http.HttpStatus.FORBIDDEN) {
                redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. Tu sesión ha expirado o no tienes permisos.");
                return "redirect:/contratos"; // Redirige al login en caso de error de autorización
            }
            model.addAttribute("error", "Error al cargar transacciones: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return "Transacciones"; // O puedes redirigir a una página de error genérica
        } catch (Exception e) {
            System.err.println("Error inesperado al cargar transacciones: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error inesperado al cargar transacciones: " + e.getMessage());
            return "Transacciones";
        }
    }

    @GetMapping("/Usuarios")
    public String usuario(
            Model model,
            @RequestParam(value = "token", required = false) String tokenFromQuery, // <-- ¡RECIBE EL TOKEN DE LA URL!
            RedirectAttributes redirectAttributes
    ) {
        String url = RESOURCES_API_BASE_URL + "/Usuarios";

        HttpHeaders headers = new HttpHeaders();
        if (tokenFromQuery != null && !tokenFromQuery.isEmpty()) {
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenFromQuery); // <-- ¡CONSTRUYE EL HEADER!
            model.addAttribute("jwtToken", tokenFromQuery); // Pasa el token a la vista
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Tu sesión ha expirado o no tienes permisos. Por favor, inicia sesión de nuevo.");
            return "redirect:/contratos";
        }
        HttpEntity<String> entity = new HttpEntity<>(headers); // Envía los headers en la solicitud

        try {
            ResponseEntity<Result<List<Usuario>>> response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    entity, // <-- ¡USA LA ENTIDAD CON LOS HEADERS!
                    new ParameterizedTypeReference<Result<List<Usuario>>>() {
            }
            );

            List<Usuario> usuarios = Optional.ofNullable(response.getBody())
                    .filter(result -> result.correct)
                    .map(result -> result.object)
                    .orElse(Collections.emptyList());
            model.addAttribute("listaUsuarios", usuarios);
            return "Usuarios";
        } catch (HttpClientErrorException e) {
            System.err.println("Error al cargar usuarios: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            if (e.getStatusCode() == org.springframework.http.HttpStatus.UNAUTHORIZED || e.getStatusCode() == org.springframework.http.HttpStatus.FORBIDDEN) {
                redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. Tu sesión ha expirado o no tienes permisos.");
                return "redirect:/contratos";
            }
            model.addAttribute("error", "Error al cargar usuarios: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return "Usuarios";
        } catch (Exception e) {
            System.err.println("Error inesperado al cargar usuarios: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error inesperado al cargar usuarios: " + e.getMessage());
            return "Usuarios";
        }
    }

    @GetMapping("/Contratos/{IdUsuario}")
    public String ContratoByIdUsuario(
            @PathVariable int IdUsuario,
            Model model,
            @RequestParam(value = "token", required = false) String tokenFromQuery, // <-- ¡RECIBE EL TOKEN DE LA URL!
            RedirectAttributes redirectAttributes
    ) {
        String url = RESOURCES_API_BASE_URL + "/Contratos/" + IdUsuario;

        HttpHeaders headers = new HttpHeaders();
        if (tokenFromQuery != null && !tokenFromQuery.isEmpty()) {
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenFromQuery); // <-- ¡CONSTRUYE EL HEADER!
            model.addAttribute("jwtToken", tokenFromQuery); // Pasa el token a la vista
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Tu sesión ha expirado o no tienes permisos. Por favor, inicia sesión de nuevo.");
            return "redirect:/contratos";
        }
        HttpEntity<String> entity = new HttpEntity<>(headers); // Envía los headers en la solicitud

        try {
            ResponseEntity<Result<List<Contrato>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity, // <-- ¡USA LA ENTIDAD CON LOS HEADERS!
                    new ParameterizedTypeReference<Result<List<Contrato>>>() {
            }
            );

            Result<List<Contrato>> result = response.getBody();

            if (result != null && result.correct) {
                model.addAttribute("contratos", result.object);
                model.addAttribute("idUsuario", IdUsuario);
            } else {
                String errorMsg = result != null
                        ? (result.errorMessage != null ? result.errorMessage : "Error desconocido")
                        : "Respuesta vacía de la API";
                model.addAttribute("error", errorMsg);
            }

        } catch (HttpClientErrorException e) {
            System.err.println("Error al obtener contratos: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            if (e.getStatusCode() == org.springframework.http.HttpStatus.UNAUTHORIZED || e.getStatusCode() == org.springframework.http.HttpStatus.FORBIDDEN) {
                redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. Tu sesión ha expirado o no tienes permisos.");
                return "redirect:/contratos";
            }
            model.addAttribute("error", "Error al obtener contratos: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado al obtener contratos: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error inesperado al obtener contratos: " + e.getMessage());
        }

        return "Contratos";
    }

}
