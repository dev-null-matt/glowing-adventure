// Constants //////////////////////////////////////////////////////////////////
var mapUrl = "/service/map/";
var questLogUrl = "/service/quest-log/";

// Display ////////////////////////////////////////////////////////////////////
// Map
var map;

// Map markers
var youAreHereMarker;
var nextObjectiveMarker;
var availableQuestMarkers;

// Map info window
var questLog;

// Info window template
var questInfoTemplate;

// Currently open map info window
var currentInfoWindow;

// Model //////////////////////////////////////////////////////////////////////
// Quest lists
var pinnedQuest;
var inactiveQuests;

// Lat long
var currentLocation;

// Persistent options
var showAvailableMissions = false;

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

	$.get('/ui-elements/questInfo.html').then(function(responseData) {
		questInfoTemplate = responseData;
	});
});

function initialize() {
	if (navigator.geolocation) {
		map = new google.maps.Map(document.getElementById('map-canvas'), {zoom : 14});
		navigator.geolocation.watchPosition(showPosition, undefined, {enableHighAccuracy : true});
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

		if ($("#pinnedQuest")) {
			updatePinnedMission();
		}

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
			addAvailableQuestClickListener(markers[index], quest);
		});

		availableQuestMarkers = markers;
	}
}

function parseInactiveQuests(data) {

	var content = "";

	if (data.length) {
		data.forEach(function(element, index) {
			content = content + "<div data-quest-id='"+ element.questId +"' class='inactiveQuest'>" + element.questName + "</div>";
		});
	} else {
		content = "No inactive missions";
	}

	inactiveQuests = content;

	if ($("#inactiveQuests")) {
		updateInactiveMissions();
	}
}

// Controls ///////////////////////////////////////////////////////////////////
function showAvailableUpdated() {

	var $showAvailable = $("#showAvailable");

	if ($showAvailable.size() > 0) {
		showAvailableMissions = $showAvailable.prop('checked');
	}

	if (showAvailableMissions) {
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

function trackMission() {

	$.ajax({
		type: "PUT",
		url : questLogUrl + "track-quest/" + this.dataset.questId + "/",
		success: function() {
			readQuestObjectives();
			readInactiveQuests();
		}
	});
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

				closeOpenWindow();

				currentInfoWindow = questLog;
		    questLog.open(map, youAreHereMarker);
		    updatePinnedMission();
		    updateInactiveMissions();
		    $("#showAvailable").change(showAvailableUpdated);
		});

	} else {

		youAreHereMarker.setPosition(latLong);
	}

	map.setCenter(latLong);
	currentLocation = latLong;
}

//Helper functions ///////////////////////////////////////////////////////////
function addAvailableQuestClickListener(marker, quest) {

	var infowindow = new google.maps.InfoWindow({
		content : questInfoTemplate
	});

	google.maps.event.addListener(marker, 'click', function() {

		closeOpenWindow();

		currentInfoWindow = infowindow;
		infowindow.open(map, marker);
		$("#questName").html(quest.questName);
		$("#addToLog").click(function() {
			$.ajax({
				type : "POST",
				url : questLogUrl + "accept-quest/" + quest.questId + "/",
				success : function(message) {

					showAvailableUpdated();
					readInactiveQuests();

					closeOpenWindow();

					currentInfoWindow = new google.maps.InfoWindow({content : message});

					currentInfoWindow.open(map, youAreHereMarker);
				}
			});
		});
	});
}

function closeOpenWindow() {
	if (currentInfoWindow) {
		currentInfoWindow.close();
	}
}

function updatePinnedMission() {
	$("#pinnedQuest").html(pinnedQuest)
}

function updateInactiveMissions() {
	$("#inactiveQuests").html(inactiveQuests);
    $("#inactiveQuests .inactiveQuest").click(trackMission);
}

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
