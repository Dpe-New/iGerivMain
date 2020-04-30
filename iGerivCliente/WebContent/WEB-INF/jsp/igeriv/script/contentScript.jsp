<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js-transient/common-min_<s:text name="igeriv.version.timestamp"/>.js"></script>
<s:include value="/WEB-INF/jsp/igeriv/common/dojo_include.jsp"></s:include>
<script type="text/javascript">		
	dojo.require("dojo.io.iframe");
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
	
	$(window).ready(function() {
		ray = {
				ajax : function(st) {	
					if ($("#load", context).length > 0) {
						$("#load", context).remove();						
					}
					$("body", context).prepend('<div id="load" style="position:absolute;width:100%;height:150%;margin-left:auto;margin-right:auto;background-repeat: no-repeat;background-position: center;z-index:999999;left:0;background-image: url(/app_img/loading.gif);"></div>');
				},
				show : function(el) {
					this.getID(el).style.display = '';
				},
				getID : function(el) {
					return document.getElementById(el);
				}
		};	
	});
</script>		