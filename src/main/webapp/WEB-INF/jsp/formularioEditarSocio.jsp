<%--
  Created by IntelliJ IDEA.
  User: guillermorodriguez
  Date: 29/11/23
  Time: 10:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h2>Introduzca los datos del socio a editar:</h2>
    <form method="post" action="EditarSociosServlet">

        Nombre <input type="text" name="nombre"/></br>
        Estatura <input type="text" name="estatura"/></br>
        Edad <input type="text" name="edad"/></br>
        Localidad <input type="text" name="localidad"/></br>
        <input type="submit" value="Aceptar">

    </form>

    <%
        //                                v---- RECOGER MENSAJE DE ERROR DEL ÁMBITO request
        String error = (String) request.getAttribute("error");
    //           v---- SI ESTÁ PRESENTE INFORMAR DEL ERROR
        if (error != null) {
    %>
    <div style="color: red"><%=error%></div>
    <%
        }
    %>
</body>
</html>
