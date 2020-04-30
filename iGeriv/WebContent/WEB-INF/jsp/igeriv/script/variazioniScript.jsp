<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {	
		loadFunctions();
		thumbnailviewer.init();
		<s:if test="actionName != null && actionName.equals('variazioni_showPubblicazioniVariazioni.action')">
			clearPubblicazioniFilterFields();
		</s:if>
		$('#pubMainDiv').keydown(function(evt){ 
		    var evt = (evt) ? evt : ((event) ? event : null); 
		    	if (evt.keyCode == 13)  {		    		
		    		$('#ricerca, #submitRicerca').trigger('click');	    	
		    		return false;
		    	} 
		});
		if ($("#titolo").length > 0) {
			$("#titolo").focus(); 
		}				
		$("table[id*='_table'] tbody").find("td:not(:last-child)").click(function() {
				var popID = 'popup_name';   
			    var popURL = $(this).parent().attr('divParam'); 
			 	var popWidth = 900;
			 	var popHeight = 400;
			    var query = popURL.split('?'); 
			    var dim = query[1].split('&');	   	    	    	  		  
			    var idtn = dim[0].split('=')[1];
			    var periodicita = '';
			    var coddl = '';
			    if (dim.length > 1) {
			    	periodicita = dim[1].split('=')[1];
			    	coddl = dim[2].split('=')[1];
			    }
			 	var url = "${pageContext.request.contextPath}/sonoInoltreUscite_showVariazioni.action?idtn=" + idtn + "&coddl=" + coddl;
			 	if (periodicita != '') {
			 		url += "&periodicita=" + periodicita;
			 	}
			 	var titolo = $(this).parent().find("td").first().text();
			 	url += "&tableTitle=" + escape(titolo);
				openDiv(popID, popWidth, popHeight, url);
			}
		);
		setContentDivHeight(30);
	});
	
	function loadFunctions() {
	}		
	
	
	function addPubblicazioniCloseButtonToLayer(layerId, imgSrc, closeLabel) {
		var str = '<div id="close" style="z-index:99999"><a href="#" class="btn_close"><img id="imgClose" src="' + imgSrc + '" style="border-style: none" border="0px" class="btn_close" title="' + closeLabel + '" alt="' + closeLabel + '"/></a></div>';													
		$('#' + layerId).prepend(str);			
	}
	
</script>