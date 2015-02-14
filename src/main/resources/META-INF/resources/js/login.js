$(document).ready(function() {

	mainMenuUrl = "service/main-menu/";
	
	$("#login").click(function() {

		var username = $("#username").val();
		var password = $("#password").val();

		$.ajax({
			type : "PUT",
			url : mainMenuUrl + "login/-",
			headers : {
				"Authorization" : "Basic " + btoa(username + ":" + password)
			},
			success : function(data) {
				location = "pages/characterSelect.html";
			},
			error : function() {
				$("#loginError").removeClass("hidden");
			}
		});
	});
	
	$("#newUser").click(function() {
		location = "pages/newAccount.html";
	});
})