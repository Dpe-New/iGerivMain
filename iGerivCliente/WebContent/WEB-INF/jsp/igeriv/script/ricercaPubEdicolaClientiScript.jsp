<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		thumbnailviewer.init();
		
		$("#RicercaPubEdicolaForm").keydown(function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.charCode);  
			if (keycode == 13) {
				$("#ricerca").trigger("click");
			}
		});
		
		if ($("#titolo").length > 0) {
			$("#titolo").focus(); 
		}
		
		var $rows = $("table[id*='_table'] tbody").find("td:not(:last-child)");
		$rows.tooltip({
			delay: 0,  
		    showURL: false,			    
		    bodyHandler: function() {
		    	return '<b><s:text name="igeriv.mostra.edicole.con.pubblicazione.in.giacenza"/></b>';
		    }
		});
		
		$rows.click(function() {
			var popID = 'popup_name'; //Get Popup Name	   
		    var popURL = $(this).parent().attr('divParam'); 
		 	var popWidth = 900;
		 	var popHeight = 500;
		    var query = popURL.split('?'); 
		    var dim = query[1].split('&');	   	    	    	  		  
		    var idtn = dim[0].split('=')[1];
		    var coddl = '';
		    if (dim.length > 1) {
		    	coddl = dim[1].split('=')[1];
		    }
		 	var url = "${pageContext.request.contextPath}/pubblicazioniClienti_showPubblicazioneEdicola.action?idtn=" + idtn + "&coddl=" + coddl;
			openDiv(popID, popWidth, popHeight, url);
		});
		
	});
	
	function submitForm() {
		$("#RicercaPubEdicolaForm").submit();
	}
</script>