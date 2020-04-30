<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {	
		<s:if test="actionName != null && !actionName.equals('libriScolasticiClienti_showOrdiniClienti.action')">
			addWaitingLayerEventOnTableRows('GestioneClientiTab_table');
		</s:if> 
		<s:if test="actionName != null && actionName.contains('libriScolasticiClienti_showClientiViewPrenotazioni')">
			switchTipoRicercaDiv();
			$("input[name=tipoRicerca]").change(function() {switchTipoRicercaDiv()});
		</s:if>
		<s:else>
			$("#ricercaPubblicazione").hide();
		</s:else>
		addFadeLayerEvents();
		setContentDivHeight(30);
		$("#nome").focus();
		
		
	});		
	
	<s:if test="actionName != null && actionName.contains('libriScolasticiClienti_showClientiViewPrenotazioni')">
	function switchTipoRicercaDiv() {
		switch(Number($("input[@name=tipoRicerca]:checked").val())) {
			case 0:
				$("#ricercaCliente").show();
				$("#ricercaPubblicazione").hide();
				$("#nome").focus();
				break;
			case 1:
				$("#ricercaCliente").hide();
				$("#ricercaPubblicazione").show();
				$("#titolo").focus();
				break;
			default:
				$("#ricercaCliente").show();
				$("#ricercaPubblicazione").hide();
				$("#nome").focus();
			break;
		}
	}
	</s:if>
	
	$("#butNuovo").click(function() {
			var popID = 'popup_name';	   	     		    	  
		    var popWidth = 900;
		    var popHeight = 550;
		 	var url = "${pageContext.request.contextPath}/libriScolasticiClienti_showCliente.action";
			openDiv(popID, popWidth, popHeight, url);
		}
	);
	
	
	
	
	function openDetailAnagrafica(idC){
		openDiv('popup_name', 900, 550, 'libriScolasticiClienti_showClienteReadOnly.action?idCliente=' + idC, '', '', '', function() {
	    	validateFieldsClienteEdicola = function() {
				return validateFieldsClienteBase(true);
			};
			clienteEdicolaSuccessCallback = function() {
				unBlockUI();
				doCloseLayer();
	    	};
		});
	}
	
	function openDetailOrdini(idC){
		openDiv('popup_name', 900, 550, 'libriScolasticiClienti_showCliente.action?idCliente=' + idC, '', '', '', function() {
	    	validateFieldsClienteEdicola = function() {
				return validateFieldsClienteBase(true);
			};
			clienteEdicolaSuccessCallback = function() {
				unBlockUI();
				doCloseLayer();
	    	};
		});
	}
	
	function openNuovoOrdine(idC){
	 	$("#GestioneClientiForm").append('<input type="hidden" name="idCliente" id="idCliente" value="' + idC + '"/>');
	    $("#GestioneClientiForm").attr("action", "libriScolasticiClienti_apriNuovoOrdineCliente.action");
	    $("#GestioneClientiForm").submit();
	    return false;		
	}
	
	function continuaOrdine(idC){
	 	$("#GestioneClientiForm").append('<input type="hidden" name="idCliente" id="idCliente" value="' + idC + '"/>');
	    $("#GestioneClientiForm").attr("action", "libriScolasticiClienti_continuaOrdineCliente.action");
	    $("#GestioneClientiForm").submit();
	    return false;		
	}
	
</script>