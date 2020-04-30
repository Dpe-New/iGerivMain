<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		addFadeLayerEvents();
		var titolo = '<s:property value="%{#request.titolo}"/>';

		<s:if test="#request.nomiClientiConEC != null && #request.nomiClientiConEC.size() > 0">
			var clienti = '<ul>';
			<s:iterator value="#request.nomiClientiConEC">
				clienti += '<li><s:property escape="false" /></li>';
			</s:iterator>
			clienti += '</ul>';
			var msg = $.validator.format('<s:text name="igeriv.ordini.clienti.non.evadibili"/>', [titolo, clienti]); 
			$.alerts.dialogClass = "style_1";
			jAlert(msg, attenzioneMsg, function() {
				$.alerts.dialogClass = null;
			});
		</s:if>
		
		evadi = function() {
			var listCodClienti = $("#listCodClienti").val().split(",");
			var arrPk = new Array();
			var arrCodCliente = new Array();
			var arrEvaso = new Array();
			var arrQuantitaDaEvadere = new Array();
			var arrUltimaRisposta = new Array();
			var arrMessLibero = new Array();
			$("#BollaOrdiniTab_table tbody tr").each(function() {
				var $tr = $(this);
				var param = $tr.attr('divParam');
				var query = param.split('?')[1];
				var dim = query.split('&');	  
				var pk = dim[0].split('=')[1];
				var codCliente = dim[1].split('=')[1];
				var evaso = dim[2].split('=')[1];
				var quantitaDaEvadere = dim[3].split('=')[1];
				if ($.inArray(codCliente, listCodClienti) != -1) {
					arrPk.push(pk);
					arrCodCliente.push(codCliente);
					arrEvaso.push(evaso);
					arrQuantitaDaEvadere.push(quantitaDaEvadere);
					arrUltimaRisposta.push(" ");
					arrMessLibero.push(" ");
				}
			});
			$("#pk").val(arrPk.toString());
			$("#evaso").val(arrEvaso.toString());
			$("#qtaDaEvadere").val(arrQuantitaDaEvadere.toString());
			$("#ultimaRisposta").val(arrUltimaRisposta.toString());
			$("#messLibero").val(arrMessLibero.toString());
			$("#spunte").val(arrCodCliente.toString());
			
			jConfirm($.validator.format('<s:text name="igeriv.confirm.evasione.ordini.bolla"/>', [titolo]), attenzioneMsg.toUpperCase(), function(r) {
				if (r) {
					dojo.xhrPost({
						form: "EvasioneOrdiniBollaForm",			
						handleAs: "json",		
						handle: function(data,args) {
							unBlockUI();
							if (typeof(data) !== 'undefined' && typeof(data.success) !== 'undefined') {
								jAlert('<s:text name="igeriv.ordini.clienti.evasi"/>', informazioneMsg.toUpperCase(), function() {
									doCloseLayer();
								});
							} else if (typeof(data) !== 'undefined' && typeof(data.error) !== 'undefined') {
								$.alerts.dialogClass = "style_1";
								jAlert(data.error, attenzioneMsg, function() {
									$.alerts.dialogClass = null;
								});
							} else {
								$.alerts.dialogClass = "style_1";
								jAlert('<s:text name="gp.errore.imprevisto"/>', attenzioneMsg.toUpperCase(), function() {
									$.alerts.dialogClass = null;
								});
							}
						}		
					});
				}
			}, true, true);
		};
	});
</script>