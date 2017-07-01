<%-- 
    Document   : ListFile
    Created on : Jun 30, 2017, 1:38:56 AM
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
        <h1>List file</h1>
        <c:if test="${not empty message}"> ${message}</c:if>
            <table id="example2" class="table table-bordered table-hover">
                <thead>
                    <tr>
                        <th>Kiểu</th>
                        <th>Tên File</th>

                    </tr>
                </thead>
                <tbody>

                <c:forEach var="listsFile" items="${listsFile}" varStatus="status">
                    <c:set value="${status.index}" var="index"/>
                    <tr>

                        <td>
                            ${status.index}
                        </td>

                        <td>
                            ${listsFile}
                        </td>



                    </tr>
                </c:forEach>
            </tbody>

        </table>

    </body>
</html>
