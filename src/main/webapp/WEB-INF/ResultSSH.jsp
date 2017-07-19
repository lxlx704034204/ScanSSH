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
        <h1>Result ssh!</h1>
        <c:if test="${not empty tongssh}">tongssh : ${tongssh}</c:if>
        <c:if test="${not empty sshdacheck}">sshdacheck : ${sshdacheck}</c:if>
        <c:if test="${not empty sshlive}">sshlive : ${sshlive}</c:if>
        <c:if test="${not empty threadactive}">tong thread dang active : ${threadactive}</c:if>
        <c:if test="${not empty message}">message : ${message}</c:if>
        <s:url value="AutoUpdate" var="AutoUpdate"/>

        <form:form method="get" action="${AutoUpdate}">
            <div class="box-body">
                <div class="form-group">
                    <label>url cua trang web</label>
                    <input type="text" name="url"  />
                </div>
                <button type="submit">update</button>
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

        <div class="col-xs-12">
            <label>Save ssh</label>
            <s:url value="SaveSsh" var="SaveSsh"/>
            <a href="${SaveSsh}">Save ssh on ftp server</a>

        </div>



        <div class="col-xs-12">
            <label>Status</label>
            <div id="StatusCheck">

            </div>
        </div>
        <script lang="javascript">
            function ajax_call() {
                var temp = "${sessionScope.url}";
                if (temp === "") {


                } else {
                    $.ajax({
                        type: "GET",
                        url: temp,
                        timeout: 100000,
                        success: function (data) {
                            console.log("SUCCESS: ", data);
                            display(data);
                        },
                        error: function (e) {
                            console.log("ERROR: ", e);
                            display(e);
                        }
                    });

                }


            }

            var interval = 2000; // set time 

            setInterval(ajax_call, interval);
            function display(data) {
                $('#StatusCheck').html(data);
            }
        </script>
    </body>
</html>
