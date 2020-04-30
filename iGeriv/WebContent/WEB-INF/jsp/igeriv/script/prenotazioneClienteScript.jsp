<script>
	$(document).ready(function() {
		addFadeLayerEvents();
		$('input:text[name=quantitaRifornimento]').first().focus();
	});
	
	function enableField() {
		$('input:text[name=quantitaRifornimento]').each(function() {
			$(this).attr("disabled", false);
		});
		return true;
	}
	
	function afterSuccessSave() {						
		$("#close").trigger('click');								
	}
</script>