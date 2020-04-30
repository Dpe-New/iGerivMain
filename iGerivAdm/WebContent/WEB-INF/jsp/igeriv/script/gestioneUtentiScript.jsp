<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {		
		addFadeLayerEvents();
	});
	
	$("#butNuovo").click(function() {
			var popID = 'popup_name';	 
		    var popWidth = 600;
		    var popHeight = 450;
		 	var url = "gestioneUtenti_showUtente.action";
			openDiv(popID, popWidth, popHeight, url);
		}
	);
</script>
<s:if test="%{#request.utenti != null && !#request.utenti.isEmpty()}">
<script>
	$("#GestioneUtentiTab_table tbody tr[divParam]").click(function() {
			var popID = 'popup_name'; //Get Popup Name
		    var popURL = $(this).attr('divParam'); 
		    var popWidth = 600;
		    var popHeight = 450;
		 	  
		    var query= popURL.split('?'); 
		    var dim= query[1].split('&');	   	    	    	  		  
		    var idUtente = dim[0].split('=')[1];
		 	var url = "gestioneUtenti_showUtente.action?codUtente=" + idUtente;
			openDiv(popID, popWidth, popHeight, url);
		}
	);
</script>
</s:if>

