<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:if test="authUser.tipoUtente == 1">
	<s:if test="authUser.rtaeAccessEnabled eq true">
		<div>
			<span style="font-size: 110%; font-family:century"><a href="redirect_redirectToRtae.action" alt="<s:text name="msg.passa.a.rtae"/>" title="<s:text name="msg.passa.a.rtae"/>" target="new"><img id="rtaeImg" width="50px" height="30px" border="0" src="/app_img/rtae.png" title='<s:text name="msg.passa.a.rtae"/>'></a></span>
		</div>
	</s:if>
</s:if>
