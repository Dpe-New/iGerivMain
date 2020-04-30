<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#dataDaStr').datepicker();
		$('#dataAStr').datepicker();
		
		
// 		$('#dataDaStr').click(function() { 
// 	    	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
// 		$('#dataAStr').click(function() { 
// 		    show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});
		
		$("img[name='imgPdf'],img[name='imgStorno']").tooltip({ 
			delay: 0,  
		    showURL: false
		});
		
		$("img[name='imgPdf']").click(function() {
			var popURL = $(this).closest("tr").attr('divParam'); 
		    var nomeFile = popURL.split('=')[1];
			window.open('edImg.action?fileName=' + nomeFile + '&type=7', '_blank');
		});
		
	});
	
	function finishDownloadCallback() {
		$("#ricerca").trigger("click");
	}
	
	function stornaFattura(codCliente, nomeCliente, numeroFattura) {
		jConfirm('<s:text name="confirm.emissione.storno.fattura"/>'.replace('{0}',numeroFattura).replace('{1}',nomeCliente), attenzioneMsg, function(r) {
		    if (r) { 
				//window.open('report_emettiStornoFattura.action?codCliente=' + codCliente + '&numFattura=' + numeroFattura + '&tipoDocumento=4&tipoProdottiInEstrattoConto=5', '_blank');
				blockUIForDownload($(this));
		    	document.forms.FattureEmesseForm.ec_eti.value='';
				$("#FattureEmesseForm").attr("action", "report_emettiStornoFattura.action"); 	
				$("#FattureEmesseForm").attr("target", "_blank"); 
				$("#codCliente").val(codCliente);
				$("#numFattura").val(numeroFattura);
				$("#tipoDocumento").val(4);
				$("#tipoProdottiInEstrattoConto").val(5);
				$("#FattureEmesseForm").submit();
		    }
		    unBlockUI();
		}, true, false);
		
	}
</script>