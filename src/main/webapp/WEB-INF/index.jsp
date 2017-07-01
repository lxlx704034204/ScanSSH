<%-- 
    Document   : index
    Created on : Jun 30, 2017, 1:52:58 AM
    Author     : Alex
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/fonts/font-awesome.min.css">
        <script src="${pageContext.request.contextPath}/js/jQuery-2.2.0.min.js"></script>
        <!-- Bootstrap 3.3.6 -->
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Index</h1>
        <div class="col-xs-12">
            <s:url value="UploadFile" var="UploadFile"/>
            <label>up load file</label>
            <a href="${UploadFile}">up load file</a>
        </div>
        <div class="col-xs-12">
            <s:url value="getListFile" var="getListFile"/>
            <label>get List File</label>
            <a href="${getListFile}">get List File</a>
        </div>
        <div class="col-xs-12">
            <s:url value="getListInfo" var="getListInfo"/>
            <label>get List Info</label>
            <a href="${getListInfo}">get List Info</a>
        </div>
        <div class="col-xs-12">
            <s:url value="CheckSsh" var="CheckSsh"/>
            <label>CheckSsh</label>
            <a href="${CheckSsh}">CheckSsh</a>
        </div>
        <div class="col-xs-12">
            <s:url value="ResultSSH" var="ResultSSH"/>
            <label>ResultSSH</label>
            <a href="${ResultSSH}">ResultSSH</a>
        </div>
    </body>
</html>
