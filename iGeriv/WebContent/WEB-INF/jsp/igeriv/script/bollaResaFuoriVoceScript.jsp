<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/bollaResaFuoriVoce-min_<s:text name="igeriv.version.timestamp"/>.js"></script>
<script>
<s:if test="%{disableForm eq true}">	
	disableAllFormFields('dataTipoBolla','submitFilter');
</s:if>	
</script>