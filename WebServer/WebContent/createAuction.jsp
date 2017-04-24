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

    <title>IBEI - Create Auction</title>

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
    
    <div id="top">
        <jsp:include page="top.jsp"/>
        <div class="separador">
        </div>
    </div>
        
    <s:actionerror/>
    <s:actionmessage/>
    
    <div class="container">
    <div class="col-xs-6">
    	<div class="panel panel-primary">
        	<div class="panel-heading">
          		<h3 class="panel-title">Create Auction</h3>
        	</div>
	        <div class="panel-body">
		    <form class="form-signin" role="form" action="createAuction" method="POST">
		        <label for="code" class="sr-only">Insert Code:</label>
		        <input type="code" id="code" class="form-control" placeholder="Code" name="code" required>
		        
		        <label for="title" class="sr-only">Insert Title:</label>
		        <input type="title" id="title" class="form-control" placeholder="Title" name="title" required>
		        
		        <label for="description" class="sr-only">Insert Description:</label>
		        <input type="description" id="description" class="form-control" placeholder="Description" name="description" required>
		
		        <label for="deadline" class="sr-only">Insert DeadLine [yyyy-MM-dd HH-mm]:</label>
		        <input type="deadline" id="deadline" class="form-control" placeholder="DeadLine: yyyy-MM-dd HH-mm" name="deadline" required>
		
		        <label for="Price" class="sr-only">Insert maxPrice:</label>
		        <input type="Price" id="maxPrice" class="form-control" placeholder="Price" name="Price" required>
				<p></p>
		        <button class="btn btn-primary btn-sm" type="submit" method="execute" >Create</button>
		    </form>
			</div>
	    	</div>
      </div> 
      </div>
</body>
</html>