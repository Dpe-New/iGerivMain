<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {		
		setContentDivHeight(30);
		<s:if test="actionName != null && actionName.contains('gestioneClienti_showClientiViewPrenotazioni')">
			switchTipoRicercaDiv();
			$("input[name=tipoRicerca]").change(function() {switchTipoRicercaDiv()});
		</s:if>
		<s:else>
			$("#ricercaPubblicazione").hide();
		</s:else>
	});
	
	<s:if test="actionName != null && actionName.contains('gestioneClienti_showClientiViewPrenotazioni')">
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
	
	$("#GestioneClientiTab_table tbody tr[divParam]").click(function() { 		  		
	    var popID = 'popup_name';
	    var popURL = $(this).attr('divParam'); 
	    var popWidth = 900;
	    var popHeight = 400;
	 	  
	    var query= popURL.split('?'); 
	    var dim= query[1].split('&');	
	    var idCliente = dim[0].split('=')[1].split(',')[0].trim();
	    var nomeCliente = $(this).find("td:first-child").text().trim() + " " + $(this).find("td:first-child").next().text().trim();
	    
	    $("input:hidden[name='ec_eti']").val('');
	    
	    $("#GestioneClientiForm").append('<input type="hidden" name="idCliente" id="idCliente" value="' + idCliente + '"/><input type="hidden" name="nomeCliente" id="nomeCliente" value="' + nomeCliente + '"/>');
	    
	    $("#GestioneClientiForm").attr("action", "viewClienteEdicola_viewPrenotazioniClientiEdicola.action");
	    $("#GestioneClientiForm").submit();		    	    
	    
	    return false;		
	});
	

</script>