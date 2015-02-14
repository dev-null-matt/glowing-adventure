$(document).ready(function() {

	mainMenuUrl = "/service/main-menu/";
	
	var $mainContent = $(".mainContent");

	function updateMainContent(element, index, array) {
		var controlContainer = document.createElement("div");
		var characterInfo = document.createElement("div");
		var name = document.createElement("div");
		var classLevel = document.createElement("div");

		controlContainer.className = "controlContainer";
		characterInfo.className = "characterInfo";

		name.className = "characterName";
		name.innerHTML = element.name;

		classLevel.className = "characterClassLevel";
		classLevel.innerHTML = element.level + " " + element.class;

		characterInfo.appendChild(name);
		characterInfo.appendChild(classLevel);

		controlContainer.appendChild(characterInfo);

		$mainContent.append(controlContainer);
	}

	function logCharacterIn() {

		var characterName = this.firstChild.textContent;

		$.ajax({
			type : "PUT",
			url : mainMenuUrl + "login/" + characterName,
			success : function(data) {
				location = "/pages/map.html";
			}
		});
	}

	$.ajax({
		type : "GET",
		url : mainMenuUrl + "characters",
		success : function(data) {
			$mainContent.html("");
			data.forEach(updateMainContent);
			$(".characterInfo").click(logCharacterIn);
		}
	});
});