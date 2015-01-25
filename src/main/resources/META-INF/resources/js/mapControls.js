$( document ).ready(function() {
      var map;
      var youAreHere;
    
      function initialize() {
      	if (navigator.geolocation) {
	      	var mapOptions = {
	          zoom: 14
	        };
	        map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
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
	  
	  	  var latLong = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
	  
	  	  if (!youAreHere) {
	  	  	youAreHere = new google.maps.Marker({
	  	  		position: latLong,
	  	  		map: map,
	  	  		title: 'You are here'});
	  	  } else {
	  	  	youAreHere.setPosition(latLong);
	  	  }
	  	  
	  	  map.setCenter(latLong);
	  }
      
      google.maps.event.addDomListener(window, 'load', initialize);
      $("#center").click(getLocation);
});