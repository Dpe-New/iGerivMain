<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		$('#dataDaStr').click(function() {		
			show_cal(document.getElementById($(this).attr('id')));
		});			
		
		$('#dataAStr').click(function() { 
	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});	
		
		$("#imgChart").tooltip({
			delay: 0,  
		    showURL: false
		}); 
		
		$('#dataDaStr').focus();
	});	
	
	function openChart() {
		var popID = 'popup_name';   	     		    	  
	    var popWidth = 550;
	    var popHeight = 450;
	 	var url = "${pageContext.request.contextPath}/venduto_showVendutoChart.action";
		openDiv(popID, popWidth, popHeight, url);
	}
</script>