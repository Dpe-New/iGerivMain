<script>		 		
	$('#arrowLeft').click(function() {
		$('#moduliSelected option:selected').each(function() {						             
	        var userClone = $(this).clone(true);
	        $('#moduli').append(userClone);
			$(this).remove();
		});			
	});
	
	$('#arrowRight').click(function() {
		$('#moduli option:selected').each(function() {						             
	        var userClone = $(this).clone(true);
	        $('#moduliSelected').append(userClone);
			$(this).remove();
		});	
	});
	
	$('#memorizza').click(function() {
		$("#moduliSelected option").attr("selected","selected");   
	});
</script>