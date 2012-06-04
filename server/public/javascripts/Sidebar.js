var Sidebar = {
		
		activeTag: null,
		
		load: function() {
			$.ajax({
				url : "/tags",
				dataType : "json",
				type : "GET",
				success : function(response) {
						//console.log(response);
						$("#tag-list").html('<ul class="nav nav-tabs nav-stacked">');
						
						response = response.tags;
						for (i in response) {
							
							$('<li><a onClick="Sidebar.updateEventListener(\''+response[i].label+'\')" class="tagp-'+response[i].priority+'">'+response[i].label+'</a></li>').hide()
						    .appendTo('#tag-list')
						    .fadeIn();
						}
						
						$("#tag-list").append('</ul>');
						
				}
			});
		},
		
		updateEventListener: function(tag) {
			
			Sidebar.activeTag = tag;
			$("#active-box").html("<h1>"+tag+"</h1>");
			Map.deleteOverlays();
			
			
			$.ajax({
				url : "/markers/"+tag,
				dataType : "json",
				type : "GET",
				success : function(response) {
					for (i=0;i<response.length;i++) {
						obj=response[i];
						
						Map.addMarker(obj);
						
					}
				}
			});
			

		}
		
}