var mainMenuUrl = "/service/main-menu/";

var $mainContent = $(".mainContent");

$(document).ready(function() {

  document.getElementById("back").onclick = goBack;

  $.ajax({
    type : "GET",
    url : mainMenuUrl + "characters",
    success : function(data) {
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
	characterInfo.onclick = deleteCharacter;

	controlContainer.appendChild(characterInfo);

	$mainContent.children()[0].appendChild(controlContainer);
}

function deleteCharacter() {

	var characterName = this.firstChild.textContent;
}

function doDelete() {
	$.ajax({
		type : "PUT",
		url : mainMenuUrl + "login/" + characterName,
		success : function(data) {
			location = "/pages/map.html";
		},
		error : errorCallback
	});
}

function goBack() {
	location = "/pages/characterSelect.html";
}

function errorCallback(jqXHR) {
	if (jqXHR.status == "401") {
		location = "/index.html";
	}
}
