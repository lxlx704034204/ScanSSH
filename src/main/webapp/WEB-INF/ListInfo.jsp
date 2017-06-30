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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font-awesome.min.css">
        <script src="${pageContext.request.contextPath}/js/jQuery-2.2.0.min.js"></script>
        <!-- Bootstrap 3.3.6 -->
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Check info</h1>
        <s:url value="getListInfo" var="getListInfo"/>
        <c:if test="${not empty message}"> ${message}</c:if>
        <form:form method="POST" action="${getListInfo}">
            <div class="col-xs-12">
                <div class="box box-warning">
                    <div class="box-body">
                        <div class="form-group">
                            <label>ten file</label>
                            <input type="text" name="name"  />
                        </div>
                    </div>

                    <div class="box-footer clearfix">

                        <button type="submit">check name</button>
                        <c:if test="${not empty message}"> ${message}</c:if>
                        </div>
                    </div>
                </div>
        </form:form>
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
