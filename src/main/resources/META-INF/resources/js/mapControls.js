$(document).ready(
		function() {
			var map;
			var youAreHere;
			var nextObjective;

			function initialize() {
				if (navigator.geolocation) {
					var mapOptions = {
						zoom : 14
					};
					map = new google.maps.Map(document
							.getElementById('map-canvas'), mapOptions);
					getLocation();
				} else {
					alert("Geolocation is not supported by this browser.");
				}
			}

			function getLocation() {
				if (navigator.geolocation) {
					navigator.geolocation.getCurrentPosition(showPosition);
				} else {
					alert("Geolocation is not supported by this browser.");
				}
			}

			function showPosition(position) {

				var latLong = new google.maps.LatLng(position.coords.latitude,
						position.coords.longitude);

				if (!youAreHere) {
					youAreHere = new google.maps.Marker({
						position : latLong,
						map : map,
						title : 'You are here'
					});
				} else {
					youAreHere.setPosition(latLong);
				}

				map.setCenter(latLong);
			}

			function pinNextObjective(data) {
				if (data) {
					var latLong = new google.maps.LatLng(
							data.nextObjective.latitude,
							data.nextObjective.longitude);
					if (!nextObjective) {
						nextObjective = new google.maps.Marker({
							position : latLong,
							map : map,
							title : 'Next objective'
						});
					} else {
						nextObjective.setPosition(latLong);
					}
				} else {
					// TODO: Remove nextObjective marker
				}
			}

			function readQuestObjectives() {
				$.ajax({
					type : "GET",
					url : "/service/quest-status",
					success : pinNextObjective
				});
			}

			google.maps.event.addDomListener(window, 'load', initialize);
			$("#center").click(getLocation);
			readQuestObjectives();
		});