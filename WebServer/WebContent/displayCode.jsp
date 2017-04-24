<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>IBEI - Search</title>

    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap theme -->
    <link href="css/bootstrap-theme.min.css" rel="stylesheet">
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="theme.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="assets/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    
    <script type="text/javascript">
        var intervalo = setInterval(function() { $('#top').load('top.jsp'); }, 60000);
    </script>
  </head>
<body>      
    <c:choose>
        <c:when test="${session.loggedin == true}">
        </c:when>
        <c:otherwise>
            <c:redirect url="/index.jsp" />
        </c:otherwise>
    </c:choose>
    
    <div id="tudo">
        <div id="top">
            <jsp:include page="top.jsp"/>
            <div class="separador">
            </div>
        </div>
     </div>
       
        <s:actionerror/>
        <s:actionmessage/>
        
        <div class="container">
        <div class="row">
        <div class="col-xs-4">
    	<div class="panel panel-primary">
        	<div class="panel-heading">
          		<h3 class="panel-title">Search by Code</h3>
        	</div>
	        <div class="panel-body">
	        	<form class="form-signin" role="form" action="searchCodeAuction" method="POST">   
				     <label for="code" class="sr-only">Insert Code:</label>
				     <input type="code" id="code" class="form-control" placeholder="Code" name="code" required>
		   
			     	<p></p>
			     	<button class="btn btn-primary btn-sm" type="submit" method="execute" >Search</button>
				</form>
	        </div>
      	</div>
    	</div>
    	</div>
    	
    	<div class="row">
    	<div class="col-xs-8">
    	<div class="panel panel-primary">
        	<div class="panel-heading">
          		<h3 class="panel-title">Results</h3>
        	</div>
	        <div class="panel-body">
	          	<table id="lista_searchC" class="table table-striped">
					<thead>
		    			<tr>
					        <th>ID</th>
					        <th>Code</th>
					        <th>Title</th>
					        <th>Details</th>
					        <th></th>
		    			</tr>
					</thead>
					<tbody>
		
					<s:iterator value="auctionsCode">
					<s:set var="idLeilao"> <s:property value="id"/></s:set>
					
					<tr>
					    <td><s:property value="id"/></td>
					    <td><s:property value="code"/></td>
					    <td><s:property value="title"/></td>
					    <td>
					    	<form class="form-signin" role="form" action="searchIdAuction" method="POST">
					            <s:hidden name="id" value="%{idLeilao}"/>
					            <button class="btn btn-info btn-sm" type="submit" method="execute">+info</button>
			        		</form>
				    	</td>
					</tr>
		
					</s:iterator>
	
					</tbody>
			</table>
        </div>
        </div>
      </div>
    </div>
    </div>
</body>
</html>