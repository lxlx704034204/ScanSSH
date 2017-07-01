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
        <h1>Result ssh!</h1>




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
