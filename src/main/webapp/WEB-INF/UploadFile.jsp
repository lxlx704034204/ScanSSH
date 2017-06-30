<%-- 
    Document   : UploadFile
    Created on : Jun 28, 2017, 11:17:58 PM
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
        <h1>Upload File</h1>
        <s:url value="UploadFile" var="UploadFile"/>

        <form:form method="POST" action="${UploadFile}" enctype="multipart/form-data">
            <div class="col-xs-12">
                <div class="box box-warning">
                    <div class="box-header with-border">
                    </div>
                    <div class="box-body">
                        <div class="form-group">
                            <label>File</label>
                            <input type="file" name="file" />
                        </div>
                    </div>
                    <div class="box-footer clearfix">

                        <button type="submit">gui</button>
                        <c:if test="${not empty message}"> ${message}</c:if>
                        </div>
                    </div>
                </div>
        </form:form>

        <br/> <br/> <br/> <br/>
        <h1>Check ssh</h1>
        <s:url value="CheckSsh" var="CheckSsh"/>

        <form:form method="GET" action="${CheckSsh}">
            <div class="col-xs-12">
                <div class="box box-warning">
                    <div class="box-body">
                        <div class="form-group">
                            <label>url cua trang web</label>
                            <input type="text" name="url"  />
                        </div>
                    </div>

                    <div class="box-footer clearfix">

                        <button type="submit">check ssh</button>
                        <c:if test="${not empty message}"> ${message}</c:if>
                        </div>
                    </div>
                </div>
        </form:form>




        <div class="col-xs-12">
            <label>Status</label>
            <div id="StatusCheck"></div>
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
