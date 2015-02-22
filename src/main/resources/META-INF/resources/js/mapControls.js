var mapUrl = "/service/map/";
var questLogUrl = "/service/quest-log/";

var map;
var youAreHere;
var nextObjective;
var currentLocation;

$(document).ready(function() {
	google.maps.event.addDomListener(window, 'load', initialize);
	readQuestObjectives();
	window.setInterval(sendLocationToServer, 4500);
});

function initialize() {
	if (navigator.geolocation) {
		map = new google.maps.Map(document.getElementById('map-canvas'), {zoom : 14});
		navigator.geolocation.watchPosition(showPosition);
	} else {
		alert("Geolocation is not supported by this browser.");
	}
}

function readQuestObjectives() {
	$.ajax({
		type : "GET",
		url : questLogUrl + "quest-status",
		success : pinNextObjective
	});
}

function showPosition(position) {

	var latLong = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

	if (!youAreHere) {

		youAreHere = createMarker(latLong, 'You are here', '/images/icons/user.png');

	} else {

		youAreHere.setPosition(latLong);
	}

	map.setCenter(latLong);
	currentLocation = latLong;
}

function pinNextObjective(data) {

	if (data) {

		var latLong = new google.maps.LatLng(data.nextObjective.latitude, data.nextObjective.longitude);

		if (!nextObjective) {

			nextObjective = createMarker(latLong, 'Next objective', '/images/icons/flag.png');

		} else {

			nextObjective.setPosition(latLong);
		}
	} else {
		// TODO: Remove nextObjective marker
	}
}

function createMarker(latLong, label, imageUrl) {

	var image = {
		url : imageUrl,
		size : new google.maps.Size(32, 32),
		origin : new google.maps.Point(0, 0),
		anchor : new google.maps.Point(16, 32)
	};

	return new google.maps.Marker({
		position : latLong,
		map : map,
		title : label,
		icon : image
	});
}

function sendLocationToServer() {

	var data = {
		"latitude" : currentLocation.lat(),
		"longitude" : currentLocation.lng()
	};

	$.ajax({
		type : "PUT",
		url : mapUrl + "set-new-position",
		data : JSON.stringify(data),
		dataType : "application/json",
		contentType : "application/json"
	});
}
