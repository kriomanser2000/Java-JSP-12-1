<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Order Confirmation</title>
</head>
<body>
<h1>Order Confirmation</h1>
<c:if test="${not empty errorMessage}">
    <p style="color: red;">${errorMessage}</p>
</c:if>
<c:if test="${not empty message}">
    <p style="color: green;">${message}</p>
</c:if>
<c:if test="${empty errorMessage}">
    <p>Thank you for your order, ${name}!</p>
    <p>You ordered: ${pizza}</p>
    <p>Selected toppings:</p>
    <ul>
        <c:forEach var="topping" items="${selectedToppings}">
            <li>${topping}</li>
        </c:forEach>
    </ul>
    <p>Your contact details:</p>
    <ul>
        <li>Phone: ${phone}</li>
        <li>Email: ${email}</li>
        <li>Address: ${address}</li>
    </ul>
</c:if>
</body>
</html>
