<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Pizza Delivery Service</title>
    <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_GOOGLE_MAPS_API_KEY&libraries=geometry"></script>
    <script>
        let map;
        let deliveryZone;
        const deliveryCoords = [
            {lat: 49.8, lng: 30.3}, // пд зх
            {lat: 49.8, lng: 30.7}, // пд сх
            {lat: 50.2, lng: 30.7}, // пн сх
            {lat: 50.2, lng: 30.3}  // пн зх
        ];
        function initMap()
        {
            map = new google.maps.Map(document.getElementById("map"), {
                center: {lat: 50.0, lng: 30.5},
                zoom: 11
            });
            deliveryZone = new google.maps.Polygon({
                paths: deliveryCoords,
                strokeColor: "#FF0000",
                strokeOpacity: 0.8,
                strokeWeight: 2,
                fillColor: "#FF0000",
                fillOpacity: 0.35
            });
            deliveryZone.setMap(map);
            map.addListener('click', (event) =>
            {
                checkDelivery(event.latLng);
            });
        }
        function checkDelivery(latLng)
        {
            const isInside = google.maps.geometry.poly.containsLocation(latLng, deliveryZone);
            const userLat = latLng.lat();
            const userLng = latLng.lng();
            fetch("order", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: `action=checkDelivery&latitude=${userLat}&longitude=${userLng}`
            })
                .then(response => response.json())
                .then(data =>
                {
                    if (data.deliveryPossible)
                    {
                        alert("Delivery is possible in this area!");
                    }
                    else
                    {
                        alert("Delivery is not available here.");
                    }
                });
        }
    </script>
    <style>
        #map
        {
            height: 400px;
            width: 100%;
        }
    </style>
</head>
<body onload="initMap()">
<h1>Pizza Delivery Service</h1>
<div id="map"></div>
<br>
<h2>Order Pizza</h2>
<form action="order" method="post">
    <label for="pizza">Select Pizza:</label>
    <select name="pizza" id="pizza" onchange="updateToppings()">
        <c:forEach var="pizza" items="${pizzaList}">
            <option value="${pizza.name}" data-toppings="${pizza.toppings}">${pizza.name}</option>
        </c:forEach>
    </select>
    <div id="toppings"></div>
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" required><br>
    <button type="submit">Order</button>
</form>
<h2>Create Your Pizza</h2>
<form action="order" method="post">
    <input type="hidden" name="action" value="create">
    <label for="customPizzaName">Pizza Name:</label>
    <input type="text" id="customPizzaName" name="customPizzaName" required><br>
    <button type="submit">Create Pizza</button>
</form>
</body>
</html>
