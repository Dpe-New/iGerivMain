<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {	
		<s:if test="actionName != null && !actionName.equals('gestioneClienti_showClienti.action')">
			addWaitingLayerEventOnTableRows('GestioneClientiTab_table');
		</s:if> 
		<s:if test="actionName != null && actionName.contains('gestioneClienti_showClientiViewPrenotazioni')">
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
	
	<s:if test="actionName != null && actionName.equals('gestioneClienti_showClienti.action')">
		$("#butNuovo").click(function() {
				var popID = 'popup_name';	   	     		    	  
			    var popWidth = 900;
			    var popHeight = 550;
			 	var url = "${pageContext.request.contextPath}/gestioneClienti_showCliente.action";
				openDiv(popID, popWidth, popHeight, url);
			}
		);
	</s:if>
	
	<s:if test="actionName != null && actionName.equals('gestioneClienti_showClienti.action')">
		$("#GestioneClientiTab_table tbody tr[divParam]").click(function() {
				var popID = 'popup_name';
			    var popURL = $(this).attr('divParam'); 
			    var popWidth = 900;
			    var popHeight = 550;
			    var query= popURL.split('?'); 
			    var dim= query[1].split('&');	   	    	    	  		  
			    var idCliente = dim[0].split('=')[1];
			 	var url = "gestioneClienti_showCliente.action?idCliente=" + idCliente;
				openDiv(popID, popWidth, popHeight, url);
			}
		);
	</s:if>
	<s:else>
		$("#GestioneClientiTab_table tbody tr[divParam]").click(function() { 		  		
		    var popURL = $(this).attr('divParam'); 
		    var query= popURL.split('?'); 
		    var dim= query[1].split('&');	   	    	     	  		  
		    var idCliente = dim[0].split('=')[1];	  		    	  						   
		    var nomeCliente = $(this).find("td:first-child").text().trim() + " " + $(this).find("td:first-child").next().text().trim();
		    $("#GestioneClientiForm").append('<input type="hidden" name="idCliente" id="idCliente" value="' + idCliente + '"/><input type="hidden" name="nomeCliente" id="nomeCliente" value="' + nomeCliente + '"/>');
		    $("#GestioneClientiForm").attr("action", "prenotazioneClienti_showFilter.action");
		    $("input:hidden[name='ec_eti']").val('');
		    $("#GestioneClientiForm").submit();
		    return false;		
		});
	</s:else>

</script>