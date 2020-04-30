<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {	
		if ($('#divClose').length > 0) {
			$('#divClose').click(function() {
				$('#parentMessageDiv').hide();
			});
		}
	});
	
	$('#textDiv').one("click", function() {
			if ($('#overlay').is(":visible")) {
				$('#overlay').fadeOut('fast');
			}
			var popID = 'popup_name';   	     		    	  
		    var popWidth = 620;
		    var popHeight = 480;
		 	var url = '${pageContext.request.contextPath}/avviso_showMessageEdicole.action?messagePk=<s:text name="messagePk"/>';
			openDiv(popID, popWidth, popHeight, url);
			$("#parentMessageDiv").hide();
		}
	);
</script>
