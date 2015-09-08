var arrested = arrested || {};
arrested.maps = arrested.maps || {};

// A log for displaying the currently tracked mission as
// well as accepted, but untracked missions.
arrested.maps.MissionLog = function constructor(map) {

  // The parent map
  var parent = map;

  // The origin for the mission window
  var origin = undefined;

  // The actual info window
  var missionLogUi = new google.maps.InfoWindow();

  $.get('/ui-elements/questLog.html').then(function(responseData) {

    var container = document.createElement("div");
    container.innerHTML = responseData;

    missionLogUi.setContent(container);
  });

  // Opens the mission log, anchored to the marker supplied to the constructor
  this.open = function open() {
    if (origin) {
      missionLogUi.open(parent, origin);
    }
  }

  // Closes the mission log
  this.close = function close() {
    missionLogUi.close();
  }

  // Displays pinned mission information
  this.displayPinnedMission = function displayPinnedMission(pinnedMissionData) {
  	missionLogUi.content.querySelector("#pinnedQuest").innerHTML = pinnedMissionData.questName;
  }

  // Displays inactive mission information
  this.displayInactiveMissions = function displayInactiveMissions(inactiveMissionData, callback) {

  	var content = "";
  	inactiveMissionData.sort(sortMissionsByName);

  	if (inactiveMissionData.length) {
      inactiveMissionData.forEach(function(element, index) {
  			content = content + "<div data-quest-id='"+ element.questId +"' class='inactiveQuest'>" + element.questName + "</div>";
  		});
  	} else {
  		content = "No inactive missions";
  	}

    missionLogUi.content.querySelector("#inactiveQuests").innerHTML = content;
    Array.prototype.slice.call(missionLogUi.content.querySelectorAll("#inactiveQuests .inactiveQuest")).forEach(
      function(element) {
        element.onclick = callback;
      });
  }

  this.setOriginMarker = function setOriginMarker(originMarker) {
    origin = originMarker;
  }

  // Sets the callback for when show available is checked or unchecked
  this.setShowAvailabileChangeHandler = function setShowAvailabileChangeHandler(callback) {
    if (missionLogUi) {
      missionLogUi.content.querySelector('#showAvailable').onchange = callback;
    }
  }

  function sortMissionsByName(a, b) {
    if (a.questName === b.questName) {
      return 0;
    } else {
      return a.questName < b.questName ? -1 : 1;
    }
  }
}
