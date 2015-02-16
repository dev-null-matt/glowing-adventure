$(document).ready(function() {

	mainMenuUrl = "/service/main-menu/";
	
	var $mainContent = $(".mainContent");

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

		controlContainer.appendChild(characterInfo);

		$mainContent.append(controlContainer);
	}

	function addCreateNew() {
		$.ajax({
			type : "GET",
			url : mainMenuUrl + "can-create-character",
			success : function(data) {
				
				if(data) {
				
					var controlContainer = document.createElement("div");
					var createNew = document.createElement("div");
					var text = document.createElement("div");
					
					controlContainer.className = "controlContainer";
					createNew.className = "controlTextContainer createNew";
					text.className = "emphasized";
					
					text.innerHTML = "Create New Character";
					
					createNew.appendChild(text);
					controlContainer.appendChild(createNew);
					
					createNew.onclick = goToCharacterCreation;
					
					$mainContent.append(controlContainer);
				}
			}
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
			}
		});
	}

	$.ajax({
		type : "GET",
		url : mainMenuUrl + "characters",
		success : function(data) {
			
			data.forEach(updateMainContent);			
			addCreateNew();
			
			$(".characterInfo").click(logCharacterIn);
		}
	});
});