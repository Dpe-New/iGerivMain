<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {	
		$("#butNuovo").click(function() {
			var url = "${pageContext.request.contextPath}/pneFornitori_editFornitore.action";
			openDiv("popup_name", 900, 350, url);
			return false;
		});
		$("#FornitoriTab_table tbody tr").click(function() {
			var popURL = $(this).attr('divParam'); 
			var query= popURL.split('?'); 
		    var dim= query[1].split('&');	   	    	    	  		  
		    var codFornitore = dim[0].split('=')[1];
			var url = "${pageContext.request.contextPath}/pneFornitori_editFornitore.action?codFornitore=" + codFornitore;
			openDiv("popup_name", 900, 350, url);
			return false;
		});
		addFadeLayerEvents();
		$("#ragioneSociale").focus();
	});	
</script>
