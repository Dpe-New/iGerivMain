<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		thumbnailviewer.init();
		
		$('#data').click(function() { 
        	show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});
		$("#data").focus();
		setContentDivHeight(100);
	});
</script>