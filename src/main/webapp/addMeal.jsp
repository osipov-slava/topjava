<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>Add new user</title>
    <jsp:useBean id="Meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
</head>
<body>

<form method="POST" action='MealController' name="frmAddUser">
    <label for="id">ID : </label>
    <input
        type="text" readonly="readonly" name="id" id="id"
        value="<c:out value="${Meal.id}" />" /> <br />

    <label for="dateTime">Дата/Время : </label>
    <input
        type="datetime-local" name="dateTime" id="dateTime">
        value="<c:out value="${Meal.dateTime}" />" /> <br />

    <label for="description">Описание : </label>
    <input
        type="text" name="description" id="description"
        value="<c:out value="${Meal.description}" />" /> <br />

    <label for="calories">Калории : </label>
    <input
        type="text" name="calories" id="calories"
        value="<c:out value="${Meal.calories}" />" /> <br />
    <p><input type="submit"></p>

</form>
</body>
</html>