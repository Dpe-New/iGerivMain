<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	var msgAliquotaIvaCambiata = '<s:text name="gp.confirm.aliquota.prodotto.non.editoriale.cambiata"/>'; 
	var msgCodiceProdottoFornitoreCambiato = '<s:text name="gp.confirm.codice.prodotto.fornitore.cambiato"/>';
	$(document).ready(function() {	
		$.ctrl('A', function() {
			addTableRow();
		});
		$.ctrl('D', function() {
			removeTableRow();
		});
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#dataDocumento').datepicker();
// 		$("#dataDocumento").click(function() { 
// 	    	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		$("#butNuovo").click(function() { 
			if ($("#codFornitore").val() == null || $("#codFornitore").val().length == 0) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="gp.nessun.fornitore"/>', '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
					$("#codFornitore").focus();
				});
				return false;
			}
			if ($("#codFornitore").val() != '<s:text name="authUser.codFiegDl"/>') {
				var url = "${pageContext.request.contextPath}/pneBollaCarico_editBolla.action?codFornitore=" + $("#codFornitore").val();
				openDiv("popup_name", 900, 480, url);
			}
			return false;
		});
		 
		$("#BolleProdottiVariTab_table tbody tr").click(function() {
			var url = "${pageContext.request.contextPath}/pneBollaCarico_editBolla.action?idDocumento=" + $(this).find("td").first().text().trim();
			openDiv("popup_name", 900, 480, url);
			return false;
		});
		$("#codFornitore").focus();
	});	
</script>
