<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>

<script type="text/javascript">
	var chiudiMsg = "<s:text name='msg.chiudi'/>"; 
	var loadingMsg = "<s:text name='msg.loading'/>";
</script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/combined-min-stable_<s:text name="igeriv.version.timestamp"/>.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/combined-min_<s:text name="igeriv.version.timestamp"/>.js"></script>
<s:include value="/WEB-INF/jsp/igeriv/common/dojo_include.jsp"></s:include>
<script type="text/javascript">		
	var context = window.document;
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
