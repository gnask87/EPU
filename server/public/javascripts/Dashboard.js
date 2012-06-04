$(function() {
	
	$( "#priority-slider" ).slider({
			min: 1,
			max: 10,
			range: "min",
			slide: function( event, ui ) {
				$( "#priority-val" ).html( ui.value );
			}
		});
		
	$("#error-dialog").hide();
		
	$('#newpost-btt').click(function() {
		$('#newtag-div').modal({show: true});
	});
		
		
	$("#newpost-submit").click(function() {
		
		$.ajax({
			url : "/add",
			dataType : "json",
			data : ({
					tag : $('#newtag-name').val(),
					priority: $('#priority-val').html()
					}),
			type : "GET",
			success : function(response) {
					$('#newpost-title').val('');
					$('#newtag-div').modal('hide');
					
					Sidebar.load();
					
					
					
			},
			error:function (xhr, ajaxOptions, thrownError){
				$("#error-dialog").fadeIn();
				$("#error-dialog").append(xhr.responseText+"<br>");

			}    
		});
		
	});	
		
		
	Map.load();
	Sidebar.load();


		
});