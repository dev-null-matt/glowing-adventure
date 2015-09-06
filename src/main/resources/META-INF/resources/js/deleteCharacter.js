var mainMenuUrl = "/service/main-menu/";

var $mainContent = $(".pageContent");

var characterName = "";

$(document).ready(function() {

  document.getElementById("back").onclick = goBack;
  document.getElementById("cancelButton").onclick = doCancel;
  document.getElementById("confirmButton").onclick = doDelete;

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

	characterName = this.firstChild.textContent;

  document.getElementById("deleteInstruction").innerHTML = "Type " + characterName + " to delete";
  document.getElementById("characterList").className = "hidden";
  document.getElementById("confirm").className = "";

  setInterval(validateDeleteButton, 500);
}

function doDelete() {
	$.ajax({
		type : "DELETE",
		url : mainMenuUrl + "delete?name=" + characterName,
		success : goBack,
		error : errorCallback
	});
}

function doCancel() {
  document.getElementById("confirm").className = "hidden";
  document.getElementById("characterList").className = "";
  document.getElementById("deleteInstruction").innerHTML = "";
  document.getElementById("confirmationText").value = "";
  clearInterval(validateDeleteButton);
}

function goBack() {
	location = "/pages/characterSelect.html";
}

function validateDeleteButton() {

  var deleteButton = document.getElementById("confirmButton");

  if (characterName && document.getElementById("confirmationText").value === characterName) {
    deleteButton.className = "delete";
    deleteButton.disabled = undefined;
  } else {
    deleteButton.className = "inactive";
    deleteButton.disabled = "disabled";
  }
}

function errorCallback(jqXHR) {
	if (jqXHR.status == "401") {
		location = "/index.html";
	}
}
