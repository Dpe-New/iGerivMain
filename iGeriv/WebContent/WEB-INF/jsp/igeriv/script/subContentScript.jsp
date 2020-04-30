<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/common-min_<s:text name="igeriv.version.timestamp"/>.js"></script>
<script type="text/javascript">		
	var context = window.document;
	$("img[src*='pdf.gif'],img[src*='xls.gif']").closest("form").each(function() {	
		if ( $('#popup_name').is(':visible') ) {
			if ( $('#popup_name').html().indexOf($(this).html()) > 0) {	
				addHiddenInput($(this).attr("id"), "downloadToken1");
			}
		}
	});
	setFocusedFieldStyle();
	chiudiLabel = "<s:text name='igeriv.chiudi'/>";		
</script>
