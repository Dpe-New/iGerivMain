<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>

<script>
	setDataTipoBolla("<s:property value='dataTipoBolla'/>", "dataTipoBolla");
	<s:if test="%{#request.itensBolla != null && !#request.itensBolla.isEmpty()}">
		$(document).ready(function() {			
			addFadeLayerEvents();
			setContentDivHeight(20);
		});
		
		$("#SonoInoltreUsciteTab_table tbody").find("td:not(:last-child)").click(function() {
				var popID = 'popup_name';	   
			    var popURL = $(this).parent().attr('divParam'); 
			 	var popWidth = 900;
			 	var popHeight = 500;
			    var query= popURL.split('?'); 
			    var dim= query[1].split('&');	   	    	    	  		  
			    var idtn = dim[0].split('=')[1];
			    var periodicita = '';
			    var coddl = '';
			    if (dim.length > 1) {
			    	periodicita = dim[1].split('=')[1];
			    	coddl = dim[2].split('=')[1];
			    }
			 	var url = "${pageContext.request.contextPath}/sonoInoltreUscite_showRichiesteRifornimenti.action?idtn=" + idtn + "&coddl=" + coddl;
			 	if (periodicita != '') {
			 		url += "&periodicita=" + periodicita;
			 	}
				openDiv(popID, popWidth, popHeight, url);
			}
		);
		
		$("table").find("td:last-child").click(function(){	
			var link = document.getElementById($(this).find('a').attr('id'));				
			thumbnailviewer.stopanimation();
			thumbnailviewer.loadimage(link);
			return false;
		});					
	</s:if>
</script>
