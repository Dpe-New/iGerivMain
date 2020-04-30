<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {	
		addFadeLayerEvents();	
	});		
	
	
	
	function openDetailLibroScolastico(sku){
		var idNumeroOrdine = $("#idNumeroOrdine").val();
		var guid = $("#keyguid").val();
		//openDiv('popup_name', 900, 620, 'libriScolasticiClienti_showDettaglioLibro.action?sku=' +
		//openDiv('popup_name', 900, 620, 'libriScolasticiClienti_showDettaglioLibroClesp.action?sku=' + sku+'&guid='+guid+'&idNumeroOrdine='+idNumeroOrdine, '', '', '', function() {
	    openDiv('popup_name', 900, 620, 'libriScolasticiClienti_showDettaglioLibroClesp.action?sku=' + sku+'&idNumeroOrdine='+idNumeroOrdine, '', '', '', function() {
	    	validateFieldsClienteEdicola = function() {
				return validateFieldsClienteBase(true);
			};
			clienteEdicolaSuccessCallback = function() {
				unBlockUI();
				doCloseLayer();
	    	};
		});
	}
	
	
	
	function openDetailOrdine(){
		var idNumeroOrdine = $("#idNumeroOrdine").val();
		openDiv('popup_name', 900, 550, 'libriScolasticiClienti_showDettaglioOrdine.action?idNumeroOrdine='+idNumeroOrdine, '', '', '', function() {
	    	validateFieldsClienteEdicola = function() {
				return validateFieldsClienteBase(true);
			};
			clienteEdicolaSuccessCallback = function() {
				unBlockUI();
				doCloseLayer();
	    	};
		});
	}
	
	
	
	
</script>