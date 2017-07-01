<%-- 
    Document   : TableSSH
    Created on : Jul 1, 2017, 7:55:50 AM
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
        <c:if test="${not empty tongssh}">tongssh : ${tongssh}</c:if>
        <c:if test="${not empty sshdacheck}">sshdacheck : ${sshdacheck}</c:if>
        <c:if test="${not empty sshlive}">sshlive : ${sshlive}</c:if>

        <c:if test="${not empty listsInfo}">
            <table id="example2" class="table table-bordered table-hover">
                <thead>
                    <tr>
                        <th>stt</th>
                        <th>host</th>
                        <th>user</th>
                        <th>pass</th>
                        <th>country</th>
                    </tr>
                </thead>
                <tbody>

                    <c:forEach var="lists" items="${listsInfo}" varStatus="status">
                        <c:set value="${status.index}" var="index"/>
                        <tr>

                            <td>
                                ${status.index}
                            </td>

                            <td>
                                ${lists.host}
                            </td>
                            <td>
                                ${lists.username}
                            </td>
                            <td>
                                ${lists.password}
                            </td>
                            <td>
                                ${lists.country}
                            </td>



                        </tr>
                    </c:forEach>
                </tbody>

            </table>
        </c:if>


    </body>
</html>
