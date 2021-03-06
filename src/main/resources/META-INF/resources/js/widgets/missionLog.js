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

    Array.prototype.slice.call(container.querySelector('#questLogTabControls').children).forEach(
      function(controlDiv) {
        controlDiv.onclick = function() {
          missionLog.openTabbedContent(this.dataset.content);
        }
      }
    );

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

  // Displays completed mission information
  this.displayCompletedMissions = function displayCompletedMissions(completedMissionData) {

    var content = "";

    if (completedMissionData.length) {
      completedMissionData.forEach(function(element, index) {
        content = content + "<div data-quest-id='"+ element.questId +"' class='inactiveQuest'>" + element.questName + "</div>";
      });
    } else {
      content = "No completed missions";
    }

    missionLogUi.content.querySelector("#completedMissions").innerHTML = content;
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

  this.openTabbedContent = function openTabbedContent(tabId) {
    Array.prototype.slice.call(missionLogUi.content.querySelector('#questLogTabControls').children).forEach(
      function(controlDiv) {
        if (controlDiv.dataset.content === tabId) {
          controlDiv.classList.add('selected');
        } else {
          controlDiv.classList.remove('selected');
        }
      }
    );
    Array.prototype.slice.call(missionLogUi.content.querySelector('#tabContent').children).forEach(
      function(contentDiv) {
        if (contentDiv.id === tabId) {
          contentDiv.classList.remove('hidden');
        } else {
          contentDiv.classList.add('hidden');
        }
      }
    );
  }

  function sortMissionsByName(a, b) {
    if (a.questName === b.questName) {
      return 0;
    } else {
      return a.questName < b.questName ? -1 : 1;
    }
  }
}
