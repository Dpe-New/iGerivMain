<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		loadFunctions();
		$('#PubblicazioniTab_table').height(60);		
		thumbnailviewer.init();
		if ($("#titolo")) {
			$("#titolo").focus();
		}	
	});
		
	$("table[id*='_table'] tbody").find("td:not(:last-child)").click(function() {
			var popID = 'popup_name';	   
		    var popURL = $(this).parent().attr('divParam'); 
		 	var popWidth = 900;
		 	var popHeight = 350;
		    var query= popURL.split('?'); 
		    var dim= query[1].split('&');
		    var idtn = dim[0].split('=')[1];
		    var cpu = dim[1].split('=')[1];
		    var coddl = dim[2].split('=')[1];
		    var url = "${pageContext.request.contextPath}/client/clientiEdicola_showPrenotazioniClienteEdicola.action?idtn=" + idtn + "&cpu=" + cpu + "&coddl=" + coddl;
			openDiv(popID, popWidth, popHeight, url);
		}
	);
	
	function loadFunctions() {
		addFadeLayerEvents();
	}		
	
	
	function addPubblicazioniCloseButtonToLayer(layerId, imgSrc, closeLabel) {
		var str = '<div id="close" style="z-index:9999"><a href="#" class="btn_close"><img id="imgClose" src="' + imgSrc + '" style="border-style: none" border="0px" class="btn_close" title="' + closeLabel + '" alt="' + closeLabel + '"/></a></div>';													
		$('#' + layerId).prepend(str);			
	}
	
</script>