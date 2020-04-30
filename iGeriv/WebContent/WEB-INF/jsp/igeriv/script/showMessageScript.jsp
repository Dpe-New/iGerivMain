<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>	
	$(document).ready(function() {
		addFadeLayerEvents();	
		$("#att1,#att2,#att3").tooltip({
			delay: 0,  
		    showURL: false
		}); 
	});	
	
	$('#letto').click(function() {		
		if ($('#letto').attr('checked') == true) {
			document.getElementById('msgLetto').value = '1';
			try {
				setFormAction('MessaggioForm1','messages_saveMessageEdicola.action', '', 'messageDiv1', false, '', '', '', false, '');
			} catch (e) {
				var req = service.saveMessageEdicola(Number($("#codFiegDl").val()), $("#dtMessaggio").val());
				req.addCallback(function() {
					$('#close').trigger('click');
				});	
			}
		}
	});		
	
	function afterSuccessSave() {						
		$("#close").trigger('click');
		$("#MessaggiForm").submit();
	}		
</script>

