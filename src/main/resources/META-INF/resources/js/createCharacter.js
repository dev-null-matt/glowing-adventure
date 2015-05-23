var lastCheckedName = "";

$(document).ready(function() {

	characterCreationUrl = "/service/character-creation/";

	$name = $("#name");
	$nameTaken = $("#nameTaken");
	$create = $("#create");

	//$name.bind("change keyup input", validateName);
	window.setInterval(validateName, 500);
	$create.click(submit);
});

function validateName() {

	var name = $name.val();

	if (lastCheckedName !== name) {
		$.ajax({
			type : "GET",
			url : characterCreationUrl + "isNameAvailable/" + name + "/",
			success : function (data) {
				if (data) {
					$nameTaken.addClass("hidden");
					$create.removeClass("inactive");
					$create.removeAttr("disabled");
				} else {
					$nameTaken.removeClass("hidden");
					$create.addClass("inactive");
					$create.attr("disabled", "disabled");
				}

				lastCheckedName = name;
			}
		});
	} else {
		$create.addClass("inactive");
		$create.attr("disabled", "disabled");
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
