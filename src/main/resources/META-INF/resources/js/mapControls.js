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

// Game menu
var gameMenu;

// Map info window
var missionLog;

// Info window template
var questInfoTemplate;

// Currently open map info window
var currentInfoWindow;

// Toast window
var toastWindow;

// Lat long
var currentLocation;

// Persistent options
var showAvailableMissions = false;

// Setup //////////////////////////////////////////////////////////////////////
$(document).ready(function() {

	google.maps.event.addDomListener(window, 'load', initialize);

	window.setInterval(sendLocationToServer, 4500);
	window.setInterval(readQuestObjectives, 5 * 60 * 1000);
	window.setInterval(readInactiveQuests, 5 * 60 * 1000)

	$.get('/ui-elements/questInfo.html').then(function(responseData) {
		questInfoTemplate = responseData;
	});
});

function initialize() {
	if (navigator.geolocation) {

		map = new google.maps.Map(document.getElementById('map-canvas'), {zoom : 14});
		navigator.geolocation.watchPosition(showPosition, undefined, {enableHighAccuracy : true});

		missionLog = new arrested.maps.MissionLog(map);
		toastWindow = new arrested.maps.ToastWindow(map);
		gameMenu = new arrested.maps.GameMenu(map);

		readQuestObjectives();
		readInactiveQuests();

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

		missionLog.displayPinnedMission(data);

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

function processEvent(data) {

	if (data.trackedObjectiveUpdated) {
		readQuestObjectives();
	}

	if (data.messages) {
		data.messages.forEach(
				function(currentValue) {
					toastWindow.queueMessage(currentValue);
				}
		);
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
		success: function(data) {
			missionLog.displayInactiveMissions(data, trackMission);
		}
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
			contentType : "application/json",
			success : processEvent
		});
	}
}

function showPosition(position) {

	var latLong = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

	if (!youAreHereMarker) {

		youAreHereMarker = createMarker(latLong, 'You are here', '/images/icons/user.png');

		google.maps.event.addListener(youAreHereMarker, 'click', function() {

			closeOpenWindow();

			currentInfoWindow = missionLog;
			missionLog.open();
			missionLog.setShowAvailabileChangeHandler(showAvailableUpdated);
		});

	} else {

		youAreHereMarker.setPosition(latLong);
	}

	map.setCenter(latLong);
	missionLog.setOriginMarker(youAreHereMarker);

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
