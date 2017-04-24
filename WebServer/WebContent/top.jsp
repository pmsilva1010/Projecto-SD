<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>



<div class="container">

      <!-- Fixed navbar -->
    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand">IBEY</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li><a href="<s:url action="home"/>">Home</a></li>
            <li><a href="<s:url action="myAuction"/>">My Auctions</a></li>
            <li><a href="<s:url action="offmessages"/>">Messages</a></li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Auction<span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="<s:url value="/createAuction.jsp"/>">Create</a></li>
                <li><a href="<s:url value="/editAuction.jsp"/>">Edit</a></li>
                <li role="separator" class="divider"></li>
                <li><a href="<s:url value="/displayCode.jsp"/>">Search by Code</a></li>
                <li><a href="<s:url value="/displayId.jsp"/>">Search by Id</a></li>
              </ul>
            </li>
            <li><a href="<s:url action="onlineUsers"/>">Online Users</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
              <li><a><c:set var="now" value="<%=new java.util.Date()%>" /> 
              		<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm" /> 
              </a></li>
              <li class="active"><a>Session: ${session.username}</a></li>
              <li><a href="<s:url action="logout" />">Logout</a></li>
            </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>
</div>

<jsp:include page="websocket.jsp"/>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="assets/js/vendor/jquery.min.js"><\/script>')</script>
    <script src="js/bootstrap.min.js"></script>
