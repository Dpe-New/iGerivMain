<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {	
		addFadeLayerEvents();	
		document.getElementById('titolo').focus();
	});			
	
	function afterSuccessSave() {
		dojo.xhrGet({
			url: '${pageContext.request.contextPath}/pubblicazioniRpc_getProfiliMenu.action',			
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
			preventCache: true,
			handle: function(data,args) {	
				var list = '';		
	            for (i = 0; i < data.length; i++) {
	            	var strChecked = '';
	            	if (data[i].id == $("#idProfilo").val()) {
	            		strChecked = ' selected ';
	            	}
	            	list += '<option value="' + data[i].id + '" ' + strChecked + '>' +  data[i].label + '</option>';
	            }
	            $("#idProfilo").empty();
	            $("#idProfilo").html(list);
			}
	    });
		$("#close").trigger("click");
	}		
</script>