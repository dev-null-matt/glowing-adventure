var lastCheckedName = "";

$(document).ready(function() {

	characterCreationUrl = "/service/character-creation/";

	$name = $("#name");
	$nameTaken = $("#nameTaken");
	$create = $("#create");

	window.setInterval(validateName, 500);
	$create.click(submit);
});

function validateName() {

	var name = $name.val();

	if (name && lastCheckedName !== name) {
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
			},
			error : errorCallback
		});
	} else if (name === "") {
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
		},
		error : errorCallback
	});
}

function errorCallback(jqXHR) {
	if (jqXHR.status == "401") {
		location = "/index.html";
	}
}
