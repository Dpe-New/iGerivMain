<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		setContentDivHeight(70);
		
		var $rows = $("#AgenzieTab_table tbody tr");
		$rows.css("cursor","pointer").click(tableRowClick);
		$rows.tooltip({
			delay: 0,  
		    showURL: false,
		    bodyHandler: function() { 
		    	return "<b><s:text name='igeriv.inserisci.modifica.agenzia'/></b>";
		    } 
		}); 
		$rows.click(tableRowClick);
		addFadeLayerEvents();
		
		$("#codFiegDl").focus();
		
	});
	
	function tableRowClick(obj) {
		var popID = 'popup_name';
		var popURL = $(this).attr('divParam');
		var popWidth = 800; 
	 	var popHeight = 550;
	 	var query= popURL.split('?');
	 	var dim= query[1].split('&');
	    var pk = dim[0].split('=')[1];
	    var url = "agenzia_showAgenzia.action?codFiegDl=" + pk;
		
		openDiv(popID, popWidth, popHeight, url);
		return false;
	}
	
	function showAgenzia()
	{
		var popID = 'popup_name';
		var popWidth = 800; 
	 	var popHeight = 550;
	 	var url = "agenzia_showAgenzia.action";
		openDiv(popID, popWidth, popHeight, url);
		return false;
	}
</script>