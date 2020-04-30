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
		    var popWidth = 850;
		    var popHeight = 370;
		 	var url = '${pageContext.request.contextPath}/avvisoLivellamenti_showLivellamenti.action?listIdsLivellamenti=' + listIdLivellamenti ;
			openDiv(popID, popWidth, popHeight, url);
			$("#parentMessageDiv").hide();
		}
	);
</script>
