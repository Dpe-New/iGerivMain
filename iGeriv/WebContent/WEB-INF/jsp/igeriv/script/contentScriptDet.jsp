<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>


<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/common-min_<s:text name="igeriv.version.timestamp"/>.js"></script>
<s:include value="/WEB-INF/jsp/igeriv/common/dojo_include.jsp"></s:include>
<script type="text/javascript">		
	$("img[src*='pdf.gif'],img[src*='xls.gif']").closest("form").each(function() {	
		if ( $('#popup_name_det').is(':visible') ) {
			if ( $('#popup_name_det').html().indexOf($(this).html()) > 0) {	
				addHiddenInput($(this).attr("id"), "downloadToken2");
			}
		}
	});
	setFocusedFieldStyle();
	chiudiLabel = "<s:text name='igeriv.chiudi'/>";		
</script>
