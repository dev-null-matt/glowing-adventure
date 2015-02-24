// Constants //////////////////////////////////////////////////////////////////
var mapUrl = "/service/map/";
var questLogUrl = "/service/quest-log/";

// Display ////////////////////////////////////////////////////////////////////
// Map
var map;

//Map markers
var youAreHereMarker;
var nextObjectiveMarker;
var availableQuestMarkers;

// Map info window
var questLog;

// Lat Long
var currentLocation;

// Model //////////////////////////////////////////////////////////////////////
var pinnedQuest;
var inactiveQuests;

// Setup //////////////////////////////////////////////////////////////////////
$(document).ready(function() {
	google.maps.event.addDomListener(window, 'load', initialize);
	readQuestObjectives();
	readInactiveQuests();
	window.setInterval(sendLocationToServer, 4500);
	
	$.get('/ui-elements/questLog.html').then(function(responseData) {
		questLog = new google.maps.InfoWindow({
		    content: responseData
		});
	});
});

function initialize() {
	if (navigator.geolocation) {
		map = new google.maps.Map(document.getElementById('map-canvas'), {zoom : 14});
		navigator.geolocation.watchPosition(showPosition);
	} else {
		alert("Geolocation is not supported by this browser.");
	}
}

// Ajax response handlers /////////////////////////////////////////////////////
function pinNextObjective(data) {

	if (data) {

		var latLong = new google.maps.LatLng(data.nextObjective.latitude, data.nextObjective.longitude);

		if (!nextObjectiveMarker) {

			nextObjectiveMarker = createMarker(latLong, 'Next objective', '/images/icons/flag.png');

		} else {

			nextObjectiveMarker.setPosition(latLong);
		}
		
		pinnedQuest = data.questName;
		
	} else {
		// TODO: Remove nextObjective marker
	}
}

function pinAvailableQuests(data) {

	if (data) {
		
		var markers = [];
		clearMarkers(availableQuestMarkers);
		
		data.forEach(function(quest, index) {
			var latLong = new google.maps.LatLng(quest.nextObjective.latitude, quest.nextObjective.longitude);
			markers[index] = createMarker(latLong, quest.name, '/images/icons/question.png');
		});
		
		availableQuestMarkers = markers;
	}
}

function parseInactiveQuests(data) {
	
	var content = "";
	
	if (data.length) {
		data.forEach(function(element, index) {
			content = content + "<div>" + element.questName + "</div>";
		});
	} else {
		content = "No inactive missions";
	}
	
	inactiveQuests = content;
}

// Controls ///////////////////////////////////////////////////////////////////
function showAvailableUpdated() {
	if ($("#showAvailable").prop('checked')) {
		$.ajax({
			type: "GET",
			url: questLogUrl + "available-quests",
			success: pinAvailableQuests
		});
	} else {
		clearMarkers(availableQuestMarkers);
		availableQuestMarkers = [];
	}
}

// Periodic update functions //////////////////////////////////////////////////
function readQuestObjectives() {
	$.ajax({
		type : "GET",
		url : questLogUrl + "quest-status",
		success : pinNextObjective
	});
}

function readInactiveQuests() {
	$.ajax({
		type: "GET",
		url: questLogUrl + "inactive-quests",
		success: parseInactiveQuests
	});
}

function sendLocationToServer() {

	if (currentLocation) {
	
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
}

function showPosition(position) {

	var latLong = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

	if (!youAreHereMarker) {

		youAreHereMarker = createMarker(latLong, 'You are here', '/images/icons/user.png');

		google.maps.event.addListener(youAreHereMarker, 'click', function() {
		    questLog.open(map, youAreHereMarker);
		    $("#pinnedQuest").html(pinnedQuest);
		    $("#inactiveQuests").html(inactiveQuests);
		    $("#showAvailable").change(showAvailableUpdated);
		});
		
	} else {

		youAreHereMarker.setPosition(latLong);
	}

	map.setCenter(latLong);
	currentLocation = latLong;
}

//Helper functions ///////////////////////////////////////////////////////////
function clearMarkers(markers) {
	
	if (!markers) {
		return;
	}
	
	markers.forEach(function(element) {
		element.setMap(null);
	});
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