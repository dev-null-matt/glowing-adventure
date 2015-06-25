var mainMenuUrl = "/service/main-menu/";
var characterCreationUrl = "/service/character-creation/";

var $mainContent = $(".mainContent");

$(document).ready(function() {

	document.getElementById("accountSettings").onclick = goAccountSettings;
	document.getElementById("delete").onclick = goDelete;
	document.getElementById("exitGame").onclick = exitGame;

	addCreateNew();

	$.ajax({
		type : "GET",
		url : mainMenuUrl + "characters",
		success : function(data) {

			if (data.length > 0) {
					document.getElementById("delete").className = "controlContainer";
			}

			data.forEach(updateMainContent);
		},
		error : errorCallback
	});
});

function updateMainContent(element, index, array) {
	var controlContainer = document.createElement("div");
	var characterInfo = document.createElement("div");
	var name = document.createElement("div");
	var classLevel = document.createElement("div");

	controlContainer.className = "controlContainer";
	characterInfo.className = "controlTextContainer characterInfo";

	name.className = "emphasized";
	name.innerHTML = element.name;

	classLevel.innerHTML = element.level + " " + element.class;

	characterInfo.appendChild(name);
	characterInfo.appendChild(classLevel);
	characterInfo.onclick = logCharacterIn;

	controlContainer.appendChild(characterInfo);

	$mainContent.children()[0].appendChild(controlContainer);
}

function addCreateNew() {
	$.ajax({
		type : "GET",
		url : characterCreationUrl + "can-create-character",
		success : function(data) {
			if(data) {
				var $createNew = $("#createNew");
				$createNew.removeClass("hidden");
				$createNew.on("click", goToCharacterCreation);
			}
		},
		error : errorCallback
	});
}

function goToCharacterCreation() {
	location = "/pages/createCharacter.html";
};

function logCharacterIn() {

	var characterName = this.firstChild.textContent;

	$.ajax({
		type : "PUT",
		url : mainMenuUrl + "login/" + characterName,
		success : function(data) {
			location = "/pages/map.html";
		},
		error : errorCallback
	});
}

function goAccountSettings() {
	location = "/pages/accountSettings.html";
}

function goDelete() {
	location = "/pages/deleteCharacter.html";
}

function exitGame() {
	$.ajax({
		type : "PUT",
		url : mainMenuUrl + "exit",
		success : function() {
			location = "/index.html";
		},
		error : errorCallback
	});
}

function errorCallback(jqXHR) {
	if (jqXHR.status == "401") {
		location = "/index.html";
	}
}
