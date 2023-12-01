package org.iesvdm.jsp_servlet_jdbc.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesvdm.jsp_servlet_jdbc.dao.SocioDAO;
import org.iesvdm.jsp_servlet_jdbc.dao.SocioDAOImpl;
import org.iesvdm.jsp_servlet_jdbc.model.Socio;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "EditarSociosServlet", value = "/EditarSociosServlet")
public class EditaSociosServlet extends HttpServlet {

    //EL SERVLET TIENE INSTANCIADO EL DAO PARA ACCESO A BBDD A LA TABLA SOCIO
    //                                  |
    //                                  V
    private SocioDAO socioDAO = new SocioDAOImpl();

    //HTML5 SÓLO SOPORTA GET Y POST
    //FRENTE A API REST UTLIZANDO CÓDIGO DE CLIENTE JS HTTP: GET, POST, PUT, DELETE, PATCH

    //MÉTODO PARA RUTAS GET /EditarSociosServlet
    //PARA LA RUTA /EditarSociosServlet VA A MOSTRAR LA JSP DE formularioEditarSocio.jsp
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean valido = true;
        int id = -1;
        String error = null;

        // Validacion
        try {

            id = Integer.parseInt(request.getParameter("codigo"));

        }catch (Exception e){

            e.printStackTrace();
            valido = false;
            error = "error en la validacion";
        }

        // Si id es valido
        if(valido){

            Optional<Socio> socioOptional = socioDAO.find(id);

            // Si se encuentra socio
            if (socioOptional.isPresent()){

                // inserto datos en request
                Socio socio = socioOptional.get();

                request.setAttribute("socioID", socio.getSocioId());
                request.setAttribute("socioNombre", socio.getNombre());
                request.setAttribute("socioEdad", socio.getEdad());
                request.setAttribute("socioEstatura", socio.getEstatura());
                request.setAttribute("socioLocalidad", socio.getLocalidad());

            } else{

                error = "socio no encontrado";
            }
        }

        // no hay ningun error
        if(error != null){

            request.setAttribute("error", error);
        }

        //SE TRATA DE UNA REDIRECCIÓN INTERNA EN EL SERVIDOR
        //FIJÉMONOS QUE LA RUTA DE LA JSP HA CAMBIADO A DENTRO DE /WEB-INF/
        //POR LO TANTO NO ES ACCESIBLE DIRECTAMENTE, SÓLO A TRAVÉS DE SERVLET
        //MEDIANTE UN RequestDispatcher ----------------v
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/formularioEditarSocio.jsp");

        //SIEMPRE QUE HACEMOS UN RequestDispatcher DEBE MATERIALIZARSE EN UN forward
        //             --------------------------------------------------------|
        //            V      v---------v-----SE LE PASAN LOS OBJETOS request Y response PARA HACER EFECTIVA
        dispatcher.forward(request, response); // LA REDIRECCIÓN INTERNA EN EL SERVIDOR A UNA JSP O VISTA.

    }


    //MÉTODO PARA RUTAS POST /GrabarSociosServlet
    //PARA LA RUTA POST /GrabarSociosServlet HAY 2 OPCIONES DE REDIRECCIÓN INTERNA A JSP
    //1a CASO DE QUE SE VALIDE CORRECTAMENTE --> pideNumeroSocio.jsp
    //2o CASO DE QUE NO SE VALIDE CORRECTAMENTE --> formularioSocio.jsp CON INFORME DE ERROR
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher dispatcher = null;

        //CÓDIGO DE VALIDACIÓN ENCAPSULADO EN UN MÉTODO DE UTILERÍA
        // SI OK ==> OPTIONAL CON SOCIO                 |
        // SI FAIL ==> EMPTY OPTIONAL                   |
        //                                              V
        Optional<Socio> optionalSocio = UtilServlet.validaGrabar(request);

        //SI OPTIONAL CON SOCIO PRESENTE <--> VALIDA OK
        if (optionalSocio.isPresent()) {

            //ACCEDO AL VALOR DE OPTIONAL DE SOCIO
            Socio socio = optionalSocio.get();

            //PERSITO EL SOCIO NUEVO EN BBDD
            this.socioDAO.create(socio);

            //CARGO TODO EL LISTADO DE SOCIOS DE BBDD CON EL NUEVO
            List<Socio> listado = this.socioDAO.getAll();

            //PREPARO ATRIBUTO EN EL ÁMBITO DE REQUEST PARA PASAR A JSP EL LISTADO
            //A RENDERIZAR. UTILIZO EL ÁMBITO DEL REQUEST DADO QUE EN EL FORWARD A
            //LA JSP SIGUE "VIVO" Y NO NECESITO ACCEDER AL ÁMBITO DE SESIÓN QUE REQUERIRÍA
            //DE UN CONTROL DE BORRADO DEL ATRIBUTO DESPUÉS DE SU USO.
            //EN request HAY UN Map<String, Object> DONDE PREPARO EL ATRIBUTO PARA LA VISTA JSP
            //                                  |
            //                                  V
            request.setAttribute("listado", listado);

            //ESTABLEZCO EL ATRIBUTO DE newSocioID EN EL ÁMBITO DE REQUEST
            //PARA LANZAR UN MODAL Y UN EFECTO SCROLL EN LA VISTA JSP
            request.setAttribute("newSocioID", socio.getSocioId() );

            //POR ÚLTIMO, REDIRECCIÓN INTERNA PARA LA URL /GrabarSocioServlet A pideNumeroSocio.jsp
            //                                                                      |
            //                                                                      V
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/listadoSociosB.jsp");
        } else {

            //El OPTIONAL ESTÁ VACÍO (EMPTY)
            //PREPARO MENSAJE DE ERROR EN EL ÁMBITO DEL REQUEST PARA LA VISTA JSP
            //                                |
            //                                V
            request.setAttribute("error", "Error de validación!");

            //POR ÚLTIMO, REDIRECCIÓN INTERNA PARA LA URL /GrabarSocioServlet A formularioSocio.jsp
            //                                                                      |
            //                                                                      V
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/formularioSocioB.jsp");
        }


        //SIEMPRE PARA HACER EFECTIVA UNA REDIRECCIÓN INTERNA DEL SERVIDOR
        //TENEMOS QUE HACER FORWARD CON LOS OBJETOS request Y response
        dispatcher.forward(request,response);

    }
}
