<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/encoder.js"></script>
<script>
	$(document).ready(function() {	
		addFadeLayerEvents();
		initRTE("/app_img/", "${pageContext.request.contextPath}/html/", "");	
		writeRichText('rte1', $("#message").val(), 600, 250, true, false);
		setContentDivHeight(30); 
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#dataMessaggio').datepicker();
// 		$("#dataMessaggio").click(function() { 
//         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		$("#rte1").focus();
	});		 
	
	function synchEditor() {
		updateRTE('rte1');
		$("#message").val(document.forms['MessageForm'].rte1.value);
		return true;
	}
	
	function validateSave() {
		var data = $('#dataMessaggio').val().trim();
		if (data == '') {
			$.alerts.dialogClass = "style_1";
			jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.data" />'), attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				//$('#dataMessaggio').focus();
			});
			setTimeout(function() {
				unBlockUI();	
			}, 100);
			return false;
		}
		if (data != '' && !checkDate(data)) {
			$.alerts.dialogClass = "style_1";
			jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				//$('#dataMessaggio').focus();
			});
			setTimeout(function() {
				unBlockUI();	
			}, 100);		
			return false; 
		} 
		return true;
	}
</script>