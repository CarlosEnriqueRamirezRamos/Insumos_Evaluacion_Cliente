package com.Insumos.Cliente.Insumos_Evaluacion_Cliente.Controller;

import com.gidis01.CRamirezProgramacionNCapasMarzo25.ML.ResultFile;
import com.gidis01.CRamirezProgramacionNCapasMarzo25.ML.Rol;
import com.gidis01.CRamirezProgramacionNCapasMarzo25.ML.Usuario;
import com.gidis01.CRamirezProgramacionNCapasMarzo25.ML.UsuarioDireccion;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/Usuario")
public class UsuarioControler {

    @GetMapping("/CargaMasiva")
    public String CargaMasiva(Model model) {
        model.addAttribute("archivoCorrecto", null);
        model.addAttribute("listaErrores", new ArrayList<ResultFile>());
        return "CargaMasiva";
    }

    @PostMapping("/CargaMasiva")
    public String CargaMasiva(@RequestParam MultipartFile archivo, Model model, HttpSession session) {
        try {
            if (archivo == null || archivo.isEmpty()) {
                model.addAttribute("archivoCorrecto", false);
                model.addAttribute("listaErrores",
                        Collections.singletonList(new ResultFile(0, "", "No se seleccionó archivo")));
                return "CargaMasiva";
            }

            String tipoArchivo = archivo.getOriginalFilename().split("\\.")[1];
            String root = System.getProperty("user.dir");
            String path = "src/main/resources/static/archivos";
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
            String absolutePath = root + "/" + path + "/" + fecha + archivo.getOriginalFilename();

            new File(root + "/" + path).mkdirs();
            archivo.transferTo(new File(absolutePath));

            List<UsuarioDireccion> listaUsuarios = tipoArchivo.equals("txt")
                    ? LecturaArchivoTXT(new File(absolutePath))
                    : LecturaArchivoExcel(new File(absolutePath));

            List<ResultFile> listaErrores = ValidarArchivo(listaUsuarios);

            model.addAttribute("archivoCorrecto", true);
            model.addAttribute("listaErrores", listaErrores);

            if (listaErrores.isEmpty()) {
                session.setAttribute("urlFile", absolutePath);
            }

        } catch (Exception ex) {
            model.addAttribute("archivoCorrecto", false);
            model.addAttribute("listaErrores",
                    Collections.singletonList(new ResultFile(0, "", "Error al procesar archivo")));
        }
        return "CargaMasiva";
    }

    @GetMapping("/CargaMasiva/procesar")
    public String procesarArchivo(HttpSession session) {
        String filePath = (String) session.getAttribute("urlFile");
        if (filePath != null) {
            try {
                List<UsuarioDireccion> usuarios = filePath.endsWith(".txt")
                        ? LecturaArchivoTXT(new File(filePath))
                        : LecturaArchivoExcel(new File(filePath));

                for (UsuarioDireccion usuario : usuarios) {
                    usuarioDAOImplementation.Add(usuario);
                }

                new File(filePath).delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            session.removeAttribute("urlFile");
        }
        return "redirect:/Usuario/CargaMasiva";
    }

    public List<UsuarioDireccion> LecturaArchivoExcel(File archivo) {

        List<UsuarioDireccion> listaUsuarios = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(archivo);) {

            for (Sheet sheet : workbook) {

                for (Row row : sheet) {

                    UsuarioDireccion usuarioDireccion = new UsuarioDireccion();

                    usuarioDireccion.usuario = new Usuario();

                    usuarioDireccion.usuario.setNombre(row.getCell(0).toString());

                    usuarioDireccion.usuario.setApellidoPaterno(row.getCell(1).toString());

                    usuarioDireccion.usuario.setApellidoMaterno(row.getCell(2).toString());

                    usuarioDireccion.usuario.setEmail(row.getCell(3).toString());

                    usuarioDireccion.usuario.Rol = new Rol();

                    usuarioDireccion.usuario.Rol.setIdRol(Integer.parseInt(row.getCell(4).toString()));

                    usuarioDireccion.usuario.Rol.equals(row.getCell(3) != null ? (int) row.getCell(3).getNumericCellValue() : 0);

                }

            }

        } catch (Exception ex) {

            System.out.println("Error al abrir el archivo");

        }

        return listaUsuarios;

    }

    public List<ResultFile> ValidarArchivo(List<UsuarioDireccion> listaUsuarios) {
        List<ResultFile> listaErrores = new ArrayList<>();

        if (listaUsuarios == null) {
            listaErrores.add(new ResultFile(0, "la lista es nula", "la lista es nula"));
        } else if (listaUsuarios.isEmpty()) {
            listaErrores.add(new ResultFile(0, "la lista es vacia", "la lista es vacia"));
        } else {
            int fila = 1;
            for (UsuarioDireccion usuarioDireccion : listaUsuarios) {
                if (usuarioDireccion.usuario.getNombre() == null || usuarioDireccion.usuario.getNombre().equals("")) {
                    listaErrores.add(new ResultFile(fila, usuarioDireccion.usuario.getNombre(), "El nombre es un campo oligatorio"));
                }
                if (usuarioDireccion.usuario.getApellidoPaterno() == null || usuarioDireccion.usuario.getApellidoPaterno().equals("")) {
                    listaErrores.add(new ResultFile(fila, usuarioDireccion.usuario.getApellidoPaterno(), "El Apellido Paterno es un campo oligatorio"));
                }
                if (usuarioDireccion.usuario.getApellidoMaterno() == null || usuarioDireccion.usuario.getApellidoMaterno().equals("")) {
                    listaErrores.add(new ResultFile(fila, usuarioDireccion.usuario.getApellidoMaterno(), "El Apellido Meterno es un campo obligatorio"));
                }
                fila++;
            }
        }
        return listaErrores;
    }
}
