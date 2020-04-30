<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		setContentDivHeight(30);
	});

	$("#GestioneClientiTab_table tbody tr[divParam]").find("td:not(:first-child)").click(function() { 	
		var popURL = $(this).parent().attr('divParam');
		var query= popURL.split('?'); 
	    var dim= query[1].split('&');	
	    var idCliente = dim[0].split('=')[1];	 
	    var $td = $(this).parent().find("td:nth-child(2)");
	    var nomeCliente = $td.text().trim() + " " + $td.next().text().trim();
	    $("input:hidden[name='ec_eti']").val('');
	    $("#GestioneClientiForm").append('<input type="hidden" name="idCliente" id="idCliente" value="' + idCliente + '"/><input type="hidden" name="nomeCliente" id="nomeCliente" value="' + nomeCliente + '"/>');
	    $("#GestioneClientiForm").attr("action", "gestioneClienti_viewEvasionePrenotazioniClientiEdicola.action"); 
	    $("#GestioneClientiForm").submit();		    	    
	    return false;		
	});
	
	function evadiClientiSelezionati() {
		$("#GestioneClientiForm").attr("action", "gestioneClienti_viewEvasionePrenotazioniClientiEdicola.action"); 
	    $("#GestioneClientiForm").submit();		
	}
</script>