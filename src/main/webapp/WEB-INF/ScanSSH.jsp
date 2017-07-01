<%-- 
    Document   : ScanSSH
    Created on : Jul 1, 2017, 5:57:19 AM
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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font-awesome.min.css">
        <script src="${pageContext.request.contextPath}/js/jQuery-2.2.0.min.js"></script>
        <!-- Bootstrap 3.3.6 -->
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Scan Result</h1>
        <h1>Check ssh</h1>
        <s:url value="CheckSsh" var="CheckSsh"/>

        <form:form method="POST" action="${CheckSsh}">
            <div class="col-xs-12">
                <div class="box box-warning">
                    <div class="box-body">
                        <div class="form-group">
                            <label>url cua trang web</label>
                            <input type="text" name="url"  />
                        </div>
                    </div>
                    <div class="box-body">
                        <div class="form-group">
                            <label>ten file range</label>
                            <input type="text" name="range"  />
                        </div>
                    </div>
                    <div class="box-body">
                        <div class="form-group">
                            <label>ten file user pass</label>
                            <input type="text" name="userpass"  />
                        </div>
                    </div>
                    <div class="box-body">
                        <div class="form-group">
                            <label>so luong thread</label>
                            <input type="number" name="thread"  />
                        </div>
                    </div>

                    <div class="box-footer clearfix">

                        <button type="submit">check ssh</button>
                        <c:if test="${not empty message}"> ${message}</c:if>
                        </div>
                    </div>
                </div>
        </form:form>



       
    </body>
</html>
