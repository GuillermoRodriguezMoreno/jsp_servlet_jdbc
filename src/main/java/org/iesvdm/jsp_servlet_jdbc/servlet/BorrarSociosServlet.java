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

@WebServlet(name = "BorrarSociosServlet", value = "/BorrarSociosServlet")
public class BorrarSociosServlet extends HttpServlet {

    //EL SERVLET TIENE INSTANCIADO EL DAO PARA ACCESO A BBDD A LA TABLA SOCIO
    //                                  |
    //                                  V
    private SocioDAO socioDAO = new SocioDAOImpl();

    //HTML5 SÓLO SOPORTA GET Y POST
    //FRENTE A API REST UTLIZANDO CÓDIGO DE CLIENTE JS HTTP: GET, POST, PUT, DELETE, PATCH


    //MÉTODO PARA RUTAS POST /BorrarSociosServlet
    //PARA LA RUTA POST /BorrarSociosServlet HAY 2 OPCIONES DE REDIRECCIÓN INTERNA A JSP
    //1a CASO DE QUE SE VALIDE CORRECTAMENTE --> informa de borrado correcto y posteriormente pideNumeroSocio.jsp
    //2o CASO DE QUE NO SE VALIDE CORRECTAMENTE --> informa del error y posteriormente pideNumeroSocio.jsp
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher dispatcher = null;

        int socioId = -1;
        boolean valida = true;

        // Válida id
        try {

            socioId = Integer.parseInt(request.getParameter("codigo"));

        } catch (Exception e) {

            e.printStackTrace();
        }

        if (!valida) {

            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/listadoSociosB.jsp.jsp");

        } else{

            Optional<Socio> optionalSocio = socioDAO.find(socioId);

            //SI OPTIONAL CON SOCIO PRESENTE <--> VALIDA OK
            if (optionalSocio.isPresent()) {

                //ACCEDO AL VALOR DE OPTIONAL DE SOCIO
                Socio socio = optionalSocio.get();

                //borro EL SOCIO EN BBDD
                this.socioDAO.delete(socio.getSocioId());

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
                request.setAttribute("deletedSocioID", socio.getSocioId());

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
            dispatcher.forward(request, response);

        }
    }
}
