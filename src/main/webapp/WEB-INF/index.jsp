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
        <br/><br/><br/>
        <div class="col-xs-12">

            <s:url value="CheckSsh" var="CheckSsh"/>
            <label>ScanSSH BY Range</label>
            <a href="${CheckSsh}">ScanSSH BY Range</a>
        </div>
        <div class="col-xs-12">
            <s:url value="ResultSSH" var="ResultSSH"/>
            <label>Result ScanSSH BY Range</label>
            <a href="${ResultSSH}">Result ScanSSH BY Range</a>
        </div>
        <br/><br/><br/>
        <div class="col-xs-12">
            <s:url value="checkSshVps" var="checkSshVps"/>
            <label>Check SSH VPS</label>
            <a href="${checkSshVps}">Check SSH VPS</a>
        </div>
        <div class="col-xs-12">
            <s:url value="ResultScanSHHVPS" var="ResultScanSHHVPS"/>
            <label>ResultScanSHHVPS</label>
            <a href="${ResultScanSHHVPS}">ResultScanSHHVPS</a>
        </div>
        <br/><br/><br/>
        <div class="col-xs-12">
            <s:url value="checkSshLive" var="checkSshLive"/>
            <label>checkSshLive</label>
            <a href="${checkSshLive}">checkSshLive</a>
        </div>
        <div class="col-xs-12">
            <s:url value="ResultCheckSshLive" var="ResultCheckSshLive"/>
            <label>ResultCheckSshLive</label>
            <a href="${ResultCheckSshLive}">ResultCheckSshLive</a>
        </div>
        <br/><br/><br/>
        <div class="col-xs-12">
            <s:url value="scanPort" var="scanPort"/>
            <label>ScanPort22</label>
            <a href="${scanPort}">scanPort</a>
        </div>
        <div class="col-xs-12">
            <s:url value="ResultScanPort" var="ResultScanPort"/>
            <label>ResultScanPort</label>
            <a href="${ResultScanPort}">ResultScanPort</a>
        </div>

    </body>
</html>
