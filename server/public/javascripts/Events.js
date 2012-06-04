var Events = {
		
		apply: function (e) {
			//if (e==' ') return;
			var e = eval("(" + e + ")");
			console.log(e);
			//e = jQuery.parseJSON( e );

			if (Sidebar.activeTag==e.tag)
				Map.addMarker(e);

		}
		
		
};