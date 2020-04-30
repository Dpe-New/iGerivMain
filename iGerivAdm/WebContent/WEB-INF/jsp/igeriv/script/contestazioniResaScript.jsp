<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script type="text/javascript">			
	$(document).ready(function() {
		$('#strDataDa').click(function() { 
	    	show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});	
		
		$('#strDataA').click(function() { 
		    show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});	
	});
</script>		