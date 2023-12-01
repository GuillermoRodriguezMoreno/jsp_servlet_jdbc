package org.iesvdm.jsp_servlet_jdbc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import org.iesvdm.jsp_servlet_jdbc.model.Socio;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;

public class UtilServlet {

    public static Optional<Socio> validaGrabar(HttpServletRequest request) {

        String nombre = null;
        int estatura = -1;
        int edad = -1;
        String localidad = null;
        String flag = null;

        try {

            flag = "nombre";
            //UTILIZO LOS CONTRACTS DE LA CLASE Objects PARA LA VALIDACIÓN
            //             v---- LANZA NullPointerException SI EL PARÁMETRO ES NULL
            Objects.requireNonNull(request.getParameter("nombre"));
            //CONTRACT nonBlank..
            //UTILIZO isBlank SOBRE EL PARÁMETRO DE TIPO String PARA CHEQUEAR QUE NO ES UN PARÁMETRO VACÍO "" NI CADENA TODO BLANCOS "    "
            //          |                                EN EL CASO DE QUE SEA BLANCO LO RECIBIDO, LANZO UNA EXCEPCIÓN PARA INVALIDAR EL PROCESO DE VALIDACIÓN
            //          -------------------------v                      v---------------------------------------|
            if (request.getParameter("nombre").isBlank())
                throw new RuntimeException("Parámetro vacío o todo espacios blancos.");
            nombre = request.getParameter("nombre");

            flag = "estatura";
            estatura = Integer.parseInt(request.getParameter("estatura"));

            flag = "edad";
            edad = Integer.parseInt(request.getParameter("edad"));

            flag = "localidad";
            //UTILIZO LOS CONTRACTS DE LA CLASE Objects PARA LA VALIDACIÓN
            //             v---- LANZA NullPointerException SI EL PARÁMETRO ES NULL
            Objects.requireNonNull(request.getParameter("localidad"));
            //CONTRACT nonBlank
            //UTILIZO isBlank SOBRE EL PARÁMETRO DE TIPO String PARA CHEQUEAR QUE NO ES UN PARÁMETRO VACÍO "" NI CADENA TODO BLANCOS "    "
            //          |                                EN EL CASO DE QUE SEA BLANCO LO RECIBIDO, LANZO UNA EXCEPCIÓN PARA INVALIDAR EL PROCESO DE VALIDACIÓN
            //          -------------------------v                      v---------------------------------------|
            if (request.getParameter("localidad").isBlank())
                throw new RuntimeException("Parámetro vacío o todo espacios blancos.");
            localidad = request.getParameter("localidad");

            return Optional.of(new Socio(-1, nombre, estatura, edad, localidad));

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", "error de validadcion en el campo " + flag);
            //FIN CÓDIGO DE VALIDACIÓN
            return Optional.empty();
        }
    }

    public static Optional<Socio> validaEditar(HttpServletRequest request){

        // Valido ID de Socio
        int socioID = -1;
        boolean valido = true;
        Optional<Socio> socioOptional = Optional.empty();


        try {

            socioID = Integer.parseInt(request.getParameter("codigo"));

            // valido resto parametros
            socioOptional = validaGrabar(request);

        }catch (Exception e){

            e.printStackTrace();
            valido = false;
            // Setteo error de id
            request.setAttribute("error", "error en la validacion de ID");
        }

        // Si ID y Socio son correctos
        if(valido && socioOptional.isPresent()){

            // obtengo socio
            Socio socio = socioOptional.get();
            // Setteo id
            socio.setSocioId(socioID);
            // Vuelvo a envolver
            socioOptional = Optional.of(socio);
            return socioOptional;

        }else{

            return socioOptional;
        }
    }

}
