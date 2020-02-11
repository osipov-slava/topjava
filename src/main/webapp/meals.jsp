<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: skybr
  Date: 2/10/2020
  Time: 12:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
    <table>
        <tr>
            <th>Дата/Время</th>
            <th>Описание</th>
            <th>Калории</th>
        </tr>
        <c:forEach var="num" items="${Meals}">
            <tr style="color:
                    <c:if test="${num.excess == true}">
                        green
                    </c:if>
                    <c:if test="${num.excess == false}">
                        red
                    </c:if>;">
                <td>${num.dateTime}<td/>
                <td>${num.description}</td>
                <td>${num.calories}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
