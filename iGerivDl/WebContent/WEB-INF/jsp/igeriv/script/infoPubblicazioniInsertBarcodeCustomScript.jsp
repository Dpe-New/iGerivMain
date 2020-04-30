<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		addFadeLayerEvents();
		thumbnailviewer.init();
		$("#InfoPubblicazioniTab_table tbody tr td:nth-child(7)").each(function() {	
			var $row = $(this).closest("tr");
			var $iv = $row.attr('iv');
			if ($iv && $iv == 2) {
                str = '<span style="float:left; width:50px; text-align:center"><img src="/app_img/conto_deposito.gif" border="0px" style="border-style: none" title="<s:text name="igeriv.conto.deposito"/>" alt="<s:text name="igeriv.conto.deposito"/>"" />';
                $(this).html(str);
                $(this).find("img").tooltip({
                    delay: 0,
                    showURL: false
                });
            }
			
		});
		
		
		$("#memorizzaBarcodeNew").click(function() {
			setTimeout(function() {
				jConfirm(msg_associazioneNuoviBarcodeConfirm, attenzioneMsg, function(r) { 
					if (r) {
						
						
						return (setTimeout(function() {return ( 
								setFormAction('InfoPubblicazioniForm','infoPubblicazioni_saveListNewBarcode.action', '', 'messageDiv'));},10));
						
// 						return (setTimeout(function() {return (validateFields('InfoPubblicazioniForm') && 
// 							setFormAction('InfoPubblicazioniForm','infoPubblicazioni_saveListNewBarcode.action', '', 'messageDiv', false, '', '', '', false, 
// 									function() {$('input:text[name*=nuovoBarcodeMap]').not(':disabled').first().focus();}));},10));

					}else {
						unBlockUI();
						return false;
					};
				}, true, false);},10);
		});
		
		
		
	
	});
	
	
	
	
	function openJAlertBarcode(bar){
		var msgBarcode = "Codice a Barre Precedente : "+bar;
		$.alerts.dialogClass = "style_1";
		jAlert(msgBarcode, attenzioneMsg.toUpperCase(), function() {
			$.alerts.dialogClass = null;
			
		});
	}
	
	
	function afterSuccessSave() {
		$("#close").trigger('click');
	
		$.alerts.dialogClass = "style_1";
		jAlert(igeriv.messaggio.associazione.nuovi.barcode.save.ok, attenzioneMsg.toUpperCase(), function() {
			$.alerts.dialogClass = null;
			
		});
	}
	
	
	
</script>