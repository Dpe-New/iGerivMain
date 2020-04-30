<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
			
	});	
	
	$('#strDataVendita').click(function() {		
		clearFilter2();			
		show_cal(document.getElementById($(this).attr('id')));
	});			
	
	$('#strDataMessaggioDa').click(function() { 
		clearFilter1();
        show_cal(document.getElementById($(this).attr('id')));              			            		    		
	});	
	
	$('#strDataMessaggioA').click(function() { 
		clearFilter1();
        show_cal(document.getElementById($(this).attr('id')));              			            		    		
	});
	
	function clearFilter1() {
		$("#strDataVendita").val("");
		$("#strDataVendita").attr("readonly",true);	
	}
	
	function clearFilter2() {		
		$("#strDataMessaggioDa").val("");	
		$("#strDataMessaggioA").val("");
	}
	
	function openChart() {
		var popID = 'popup_name';   	     		    	  
	    var popWidth = 550;
	    var popHeight = 450;
	 	var url = "${pageContext.request.contextPath}/vendite_showVendutoChart.action";
		openDiv(popID, popWidth, popHeight, url);
	}
</script>