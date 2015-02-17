$(document).ready(function() {
	
	characterCreationUrl = "/service/character-creation/";
	
	$name = $("#name");
	$nameTaken = $("#nameTaken");
	$create = $("#create");
	
	$name.change(validateName);
	$create.click(submit);
});

function validateName() {
	var name = $name.val();
	
	if (name) {
		$.ajax({
			type : "GET",
			url : characterCreationUrl + "isNameAvailable/" + name + "/",
			success : function (data) {
				if (data) {
					$nameTaken.addClass("hidden");
				} else {
					$nameTaken.removeClass("hidden");
				}
			}
		});
	}
}

function submit() {
	$.ajax({
		type : "PUT",
		url : characterCreationUrl + "create-character/" + $name.val() + "/",
		success : function (data) {
			if (data) {
				window.location = "/pages/characterSelect.html";
			} else {
				alert("There was a problem creating your new character");
			}
 		}
	});
}