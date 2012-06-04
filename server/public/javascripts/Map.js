var Map = {
		
		map:null,
		load: function() {
			var myLatlng = new google.maps.LatLng(46.06773554,11.15138974);
			  var myOptions = {
			    zoom: 14,
			    center: myLatlng,
			    zoomControl: false,
			    streetViewControl: false,
			    mapTypeId: google.maps.MapTypeId.ROADMAP
			  }
			  Map.map = new google.maps.Map(document.getElementById("mapC"), myOptions);
		},
		
		
		
		markersArray: [],
		deleteOverlays: function () {
			  if (Map.markersArray) {
			    for (i in Map.markersArray) {
			    	Map.markersArray[i].setMap(null);
			    }
			    Map.markersArray.length = 0;
			  }
		},
		
		addMarker: function (e) {
			
			var location = new google.maps.LatLng(e.Lat,e.Lng);
			var url = e.src;
			var thumb = url+"_min";
			
			
			
			console.log(e);
			var image = new google.maps.MarkerImage(thumb,
				      new google.maps.Size(32, 32),
				      new google.maps.Point(0,0),
				      new google.maps.Point(0, 32),
				      new google.maps.Size(32, 32));
			
			  marker = new google.maps.Marker({
			    position: location,
			    map: Map.map,
			    animation: google.maps.Animation.DROP,
			    icon: image
			  });
			  
			  google.maps.event.addListener(marker, 'click', function() {
				  
				    $('#photo-desc').html(e.desc)
				    $( "#photo-dialog" ).attr("title", e.op + " - "+e.time);
					$( "#photo-container" ).attr("src", url);
					$( "#photo-dialog" ).dialog({
						height: 500,
						width: 500,
						hide: {effect: "fadeOut"},
						show: {effect: "fadeIn"},
						modal: true
					});
			    });
			  
			  Map.markersArray.push(marker);
		}
		
		
}