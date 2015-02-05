$( document ).ready(function() {

	var $mainContent = $(".main-content");

	function updateMainContent(element, index, array) {
		var characterInfo = document.createElement("div");
		var name = document.createElement("div");
		var classLevel = document.createElement("div");
		
		characterInfo.className = "character-info";
		
		name.className = "character-name";
		name.innerHTML = element.name;
		
		classLevel.className = "character-class-level";
		classLevel.innerHTML = element.level + " " + element.class;
		
		characterInfo.appendChild(name);
		characterInfo.appendChild(classLevel);
		
		$mainContent.append(characterInfo);
	}

	function logCharacterIn() {
	
		var characterName = this.firstChild.textContent;
	
		$.ajax({
				type: "POST",
				url: "/service/login/" + characterName,
				success: 
					function(data) {
						location = "/pages/map.html";
					}
			});
	}

	$.ajax({
		type: "GET",
		url: "/service/characters",
		success: 
			function(data) {
				$mainContent.html("");
				data.forEach(updateMainContent);
				$(".character-info").click(logCharacterIn);
			}
	});
});