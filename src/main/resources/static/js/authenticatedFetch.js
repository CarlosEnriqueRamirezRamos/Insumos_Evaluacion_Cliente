// authenticatedFetch.js
// URL base de tu backend (asegúrate de que coincida con el puerto de tu servidor Spring Boot)
const BACKEND_BASE_URL = 'http://localhost:8081'; // Ajusta esto si tu backend corre en un puerto diferente

/**
 * Realiza una petición fetch a un endpoint protegido del backend.
 * Añade automáticamente el token JWT al encabezado Authorization.
 * Maneja respuestas 401 (Unauthorized) y 403 (Forbidden) redirigiendo al login.
 *
 * @param {string} url El path del endpoint (ej. '/api/User-info', '/api/paginado').
 * @param {object} options Opciones estándar de fetch (method, body, etc.).
 * @returns {Promise<Response>} La promesa de la respuesta fetch.
 */
window.authenticatedFetch = async function(url, options = {}) {
    const jwtToken = localStorage.getItem('jwtToken');

    // Si no hay token, redirigir al login (usuario no autenticado)
    if (!jwtToken) {
        console.error('No se encontró token JWT. Redirigiendo al login...');
        // Redirige a la página de login de tu frontend
        window.location.href = '/contratos'; // Ajusta esta ruta si es diferente, por ejemplo, '/Login.html'
        throw new Error('No authenticated token found.'); // Lanza un error para detener la ejecución
    }

    // Asegurarse de que el objeto headers exista
    options.headers = {
        ...options.headers, // Mantener cualquier encabezado existente
        'Authorization': `Bearer ${jwtToken}`, // Añadir el token JWT
        // 'Content-Type': 'application/json' // Generalmente útil para APIs REST, pero no siempre necesario para GET
    };

    try {
        const response = await fetch(`${BACKEND_BASE_URL}${url}`, options);

        // Manejo de errores de autenticación/autorización
        if (response.status === 401) {
            console.error('Petición no autorizada (401). Token inválido o expirado. Redirigiendo al login...');
            localStorage.removeItem('jwtToken'); // Limpiar token inválido
            window.location.href = '/contratos'; // Redirige al login
            throw new Error('Unauthorized access.');
        } else if (response.status === 403) {
            console.error('Petición prohibida (403). No tienes permisos para acceder a este recurso.');
            // Puedes redirigir a una página de "Acceso Denegado" o mostrar un mensaje
            alert('Acceso denegado. No tienes los permisos necesarios para esta acción.'); // Usar una modal personalizada en lugar de alert
            throw new Error('Forbidden access.');
        }

        return response; // Devolver la respuesta para que el llamador la procese
    } catch (error) {
        console.error('Error en la petición autenticada:', error);
        throw error; // Relanzar el error para que el llamador lo maneje
    }
};
