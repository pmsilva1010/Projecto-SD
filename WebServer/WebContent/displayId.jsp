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
        
        <!-- Primeiro painel para o search -->
        <div class="container">
        <div class="row">
        <div class="col-xs-4">
    	<div class="panel panel-primary">
        	<div class="panel-heading">
          		<h3 class="panel-title">Search by Id</h3>
        	</div>
	        <div class="panel-body">
	        	<form class="form-signin" role="form" action="searchIdAuction" method="POST">   
				     <label for="id" class="sr-only">Insert Code:</label>
				     <input type="number" id="id" class="form-control" placeholder="ID" name="id" required>
		   
			     	<p></p>
			     	<button class="btn btn-primary btn-sm" type="submit" method="execute" >Search</button>
				</form>
	        </div>
      	</div>
    	</div>
    	</div> 	
    	
    	<s:set var="idLeilao"> <s:property value="id"/> </s:set>
    	
    	<!-- Painel para mostrar os resultados -->
    	<div class="row">
    	<div class="col-xs-6">
    	<div class="panel panel-primary">
        	<div class="panel-heading">
          		<h3 class="panel-title">Results</h3>
        	</div>
	        <div class="panel-body">
	        	<table id="lista_searchI" class="table table-striped">
	        	<tbody>
	        		<tr>
	        			<td>Title</td>
	        			<td><s:property value="auctionId.title"/></td>
	        		</tr>
	        		<tr>
	        			<td>Code</td>
	        			<td><s:property value="auctionId.code"/></td>
	        		</tr>
	        		<tr>
	        			<td>Description</td>
	        			<td><s:property value="auctionId.description"/></td>
	        		</tr>
	        		<tr>
	        			<td>Dead Line</td>
	        			<td><s:property value="auctionId.deadline"/></td>
	        		</tr>
	        		<tr>
	        			<td>Nr. Bids</td>
	        			<td><s:property value="auctionId.nrBids"/></td>
	        		</tr>
	        		<tr><td></td><td></td></tr>
	        		<tr>
	        			<td>Best Bid</td>
	        			<td><s:property value="auctionId.bestBid"/></td>
	        		</tr>
	        		<tr>
	        			<td>User</td>
	        			<td><s:property value="auctionId.userBestBid"/></td>
	        		</tr>
	        	</tbody>
	        	</table>
	        	
	        	<!-- Make Bid no fundo dos results -->
	        	<form class="form-signin" role="form" action="makeBid" method="POST">   
				<label for="value" class="sr-only">Insert Bid:</label>
				<table>
				<tbody>
				<tr>
				<td><button class="btn btn-primary btn-sm" type="submit" method="execute" >Make Bid</button></td>
				<td><input type="text" id="value" class="form-control" placeholder="Value" name="value" required>
				<s:hidden name="idBid" value="%{idLeilao}"/></td>			    
			    </tr>
			    </tbody>
			    </table>
			</form>          	
        </div>
        </div>
      </div>    
      
      <!-- Painel com o mural -->
      <div class="col-xs-6">
    	<div class="panel panel-primary">
        	<div class="panel-heading">
          		<h3 class="panel-title">Mural</h3>
        	</div>
	        <div class="panel-body">
	        <form class="form-signin" role="form" action="sendMessage" method="POST">   
				<label for="msg" class="sr-only">Insert Message:</label>
				<table>
				<tbody>
				<tr>
				<td><button class="btn btn-primary btn-sm" type="submit" method="execute" >Send</button></td>
				<td><input type="text" id="msg" class="form-control" placeholder="Insert Message" name="msg" required>
				<s:hidden name="idMsg" value="%{idLeilao}"/></td>			    
			    </tr>
			    </tbody>
			    </table>
			</form>
	    	<p></p>
	        <table id="lista_mural" class="table table-striped">
				<thead>
	    			<tr>
				        <th>User</th>
				        <th>Text</th>
				        <th></th>
	    			</tr>
				</thead>
				<tbody>
	
				<s:iterator value="auctionId.mural">
	
				<tr>
				    <td><s:property value="user"/></td>
				    <td><s:property value="text"/></td>
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