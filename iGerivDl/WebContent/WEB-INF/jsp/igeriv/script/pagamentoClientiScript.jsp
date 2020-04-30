<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#strDataCompetenzaDa').datepicker();
		$('#strDataCompetenzaA').datepicker();
		
// 		$('#strDataCompetenzaDa').click(function() { 
// 		    show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
// 		$('#strDataCompetenzaA').click(function() { 
// 		    show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	

	});
	
	function checkFields() {
		var checked = false;
		$("input[type=checkbox][name=spunte]").each(function() {
			if ($(this).is(':checked')) {
				checked = true;
			}
		});
		if (!checked) {
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="igeriv.nessun.estratto.conto.selezionato"/>', attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
			});
			setTimeout(function() {
				unBlockUI();
			}, 100);
			return false;
		}
		return true;
	}
	
	function doRicerca() {
		$("#ricerca").trigger("click");
	}
	
	function showErrorMsg() {
		$.alerts.dialogClass = "style_1";
		jAlert('<s:text name="gp.errore.imprevisto"/>', attenzioneMsg.toUpperCase(), function() {
			$.alerts.dialogClass = null;
		});
		setTimeout(function() {
			unBlockUI();
		}, 100);
		return false;
	}
</script>		    