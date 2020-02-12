<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html>
<head>
    <title>Show All Meals</title>
    <jsp:useBean id="Meals" scope="request" type="java.util.List"/>
</head>
<body>
<table>
    <thead>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th colspan=2>Действие</th>
    </tr>
    </thead>
    <tbody>

    <c:forEach var="meal" items="${Meals}">
        <tr style="color: ${meal.excess ? 'red' : 'green'}">
            <td>${meal.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="MealController?action=edit&mealId=<c:out value="${meal.id}"/>">Update</a></td>
            <td><a href="MealController?action=delete&mealId=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<p><a href="MealController?action=insert">Add Meal</a></p>
</body>
</html>