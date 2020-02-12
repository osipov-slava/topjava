<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html>
<head>
    <title>Meals</title>
    <jsp:useBean id="Meals" scope="request" type="java.util.List"/>
</head>
<body>
    <table>
        <tr>
            <th>Дата/Время</th>
            <th>Описание</th>
            <th>Калории</th>
        </tr>

        <c:forEach var="meal" items="${Meals}">
            <tr style="color:
                ${meal.excess ? 'green' : 'red'}">
                <td>${meal.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
