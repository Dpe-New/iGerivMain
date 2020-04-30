<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<div id="mainDiv" style="text-align: center">
	<div class="messaggioDiv"
		style="text-align: center; width: 800px; height: 460px">
		<s:text name="messaggioInviatoDaRivendita.messaggioEscape" />
	</div>
	<div style="text-align: left; width: 600px">
		<s:if
			test="messaggioInviatoDaRivendita.allegato != null && messaggioInviatoDaRivendita.allegato != ''">
			<div style="float: left; text-align: left; width: 150px">
				<s:text name="igeriv.allegati" />
				:
			</div>
			<div class="required" style="float: left; height: 20px; width: 150px">
				<a
					href='${pageContext.request.contextPath}${ap}/attachment.action?codRivendita=<s:text name="%{#request.codRivendita}"/>&fileName=<s:text name="messaggioInviatoDaRivendita.allegato"/>'
					target="new"><img src="/app_img/attachment.png" border="0px"
					style="border-style: none" title="<s:text name="igeriv.allegato"/>"
					alt="<s:text name="igeriv.allegato"/>" /></a>
			</div>
		</s:if>
	</div>
</div>

