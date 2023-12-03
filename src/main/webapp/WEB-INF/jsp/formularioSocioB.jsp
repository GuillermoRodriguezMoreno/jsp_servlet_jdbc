<%@ page import="org.iesvdm.jsp_servlet_jdbc.model.Socio" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
  <link rel="stylesheet" type="text/css" href="estilos.css" />
</head>
<body class="bg-light">

<%
  boolean actualizar = false;
  Socio socio = (Socio) request.getAttribute("socio");
  if(request.getAttribute("actualizar") != null) {
    actualizar = true;

  }else{
    socio = new Socio();
  }
%>

<div id="formularioCrear">
  <div class="container bg-white">
    <div class="row border-bottom">
      <div class="col-12 h2">Introduzca los datos del nuevo socio</div>
    </div>
  </div>
  <div class="container bg-light">
    <form method="post" action="GrabarSociosServlet">
      <div class="row body mt-2">
        <div class="col-md-6 align-self-center">Nombre</div>
        <div class="col-md-6 align-self-center"><input type="text" name="nombre"/></div>
      </div>
      <div class="row body mt-2">
        <div class="col-md-6 align-self-center">Estatura</div>
        <div class="col-md-6 align-self-center"><input type="text" name="estatura"/></div>
      </div>
      <div class="row body mt-2">
        <div class="col-md-6 align-self-center">Edad</div>
        <div class="col-md-6 align-self-center"><input type="text" name="edad"/></div>
      </div>
      <div class="row body mt-2">
        <div class="col-md-6 align-self-center">Localidad</div>
        <div class="col-md-6 align-self-center"><input type="text" name="localidad"/></div>
      </div>
      <div class="row mt-2">
        <div class="col-md-6">
          &nbsp;
        </div>
        <div class="col-md-6 align-self-center">
          <input class="btn btn-primary" type="submit" value="Aceptar">
        </div>
      </div>
    </form>
</div>
</div>
<div id="formularioEditar">
  <div class="container bg-white">
    <div class="row border-bottom">
      <div class="col-12 h2">Introduzca los datos del Socio a editar</div>
    </div>
  </div>
  <div class="container bg-light">
    <form method="post" action="EditarSociosServlet">
      <div class="row body mt-2">
        <div class="col-md-6 align-self-center">Editando Socio con ID (<%=socio.getSocioId()%>):</div>
        <div class="col-md-6 align-self-center"><input type="text" value="<%=socio.getSocioId()%>" hidden="hidden" name="codigo"/></div>
      </div>
      <div class="row body mt-2">
        <div class="col-md-6 align-self-center">Nombre (actual: <%=socio.getNombre()%>)</div>
        <div class="col-md-6 align-self-center"><input type="text" name="nombre"/></div>
      </div>
      <div class="row body mt-2">
        <div class="col-md-6 align-self-center">Estatura (actual: <%=socio.getEstatura()%>)</div>
        <div class="col-md-6 align-self-center"><input type="text" name="estatura"/></div>
      </div>
      <div class="row body mt-2">
        <div class="col-md-6 align-self-center">Edad (actual: <%=socio.getEdad()%>)</div>
        <div class="col-md-6 align-self-center"><input type="text" name="edad"/></div>
      </div>
      <div class="row body mt-2">
        <div class="col-md-6 align-self-center">Localidad (actual: <%=socio.getLocalidad()%>)</div>
        <div class="col-md-6 align-self-center"><input type="text" name="localidad"/></div>
      </div>
      <div class="row mt-2">
        <div class="col-md-6">
        </div>
        <div class="col-md-6 align-self-center">
          <input class="btn btn-primary" type="submit" value="Aceptar">
        </div>
      </div>
    </form>

  <%
    //                                                 v---- RECOGER MENSAJE DE ERROR DEL ÁMBITO request
    String error = (String) request.getAttribute("error");
//            v---- SI ESTÁ PRESENTE INFORMAR DEL ERROR
    if (error != null) {
  %>
  <div class="row mt-2">
    <div class="col-6">
        <div class="alert alert-danger alert-dismissible fade show">
          <strong>Error!</strong> <%=error%>
          <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
      </div>
  </div>
  <%
    }
  %>
</div>
</div>
<script type="text/javascript">

  if(<%=actualizar%> == true){

    let formCrear = document.getElementById("formularioCrear");
    formCrear.setAttribute("hidden", "hidden");

  }else{

    let formEditar = document.getElementById("formularioEditar");
    formEditar.setAttribute("hidden", "hidden");
  }

</script>
<script src="js/bootstrap.bundle.js" ></script>
</body>
</html>