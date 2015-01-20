$( document ).ready(function() {
	$("#login").click(
		function() {
		
			var username = $("#username").val();
			var password = $("#password").val();
		
			$.ajax({
				type: "GET",
				url: "/service/login/-",
				headers: {
					"Authorization": "Basic " + btoa(username + ":" + password)
				},
				success: 
					function(data) {
						location = "pages/characterSelect.html";
					},
				error: 
					function() {
						$("#loginError").removeClass("hidden");
					}
			});
		}
	);
})