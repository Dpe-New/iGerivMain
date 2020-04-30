<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<div id="mainDiv" style="text-align: center">
	<div class="messaggioDiv"
		style="text-align: center; width: 800px; height: 460px">
		<s:text name="messaggio.messaggio" />
	</div>
	<div
		style="float: left; text-align: left; width: 800px; white-space: nowrap;">
		<s:if test="messaggio.allegato1 != null && messaggio.allegato1 != ''">
			<div style="float: left; text-align: left; width: 150px">
				<s:text name="igeriv.allegati" />
				:
			</div>
			<s:url id="att1" value="attachment.action" encode="true">
				<s:param name="codRivendita" value="%{#request.codRivendita}" />
				<s:param name="fileName" value="%{messaggio.allegato1}" />
			</s:url>
			<div class="required" style="float: left; height: 20px; width: 150px">
				<a href='<s:property value="att1"/>' target="new"><img
					src="/app_img/attachment.png" border="0px"
					style="border-style: none" title="<s:text name="igeriv.allegato"/>"
					alt="<s:text name="igeriv.allegato"/>" /></a>
			</div>
		</s:if>
		<s:if test="messaggio.allegato2 != null && messaggio.allegato2 != ''">
			<div style="float: left; text-align: left; width: 150px">
				<s:text name="igeriv.allegati" />
				:
			</div>
			<s:url id="att2" value="attachment.action" encode="true">
				<s:param name="codRivendita" value="%{#request.codRivendita}" />
				<s:param name="fileName" value="%{messaggio.allegato2}" />
			</s:url>
			<div class="required" style="float: left; height: 20px; width: 150px">
				<a href='<s:property value="att2"/>' target="new"><img
					src="/app_img/attachment.png" border="0px"
					style="border-style: none" title="<s:text name="igeriv.allegato"/>"
					alt="<s:text name="igeriv.allegato"/>" /></a>
			</div>
		</s:if>
		<s:if test="messaggio.allegato3 != null && messaggio.allegato3 != ''">
			<div style="float: left; text-align: left; width: 150px">
				<s:text name="igeriv.allegati" />
				:
			</div>
			<s:url id="att3" value="attachment.action" encode="true">
				<s:param name="codRivendita" value="%{#request.codRivendita}" />
				<s:param name="fileName" value="%{messaggio.allegato3}" />
			</s:url>
			<div class="required" style="float: left; height: 20px; width: 150px">
				<a href='<s:property value="att3"/>' target="new"><img
					src="/app_img/attachment.png" border="0px"
					style="border-style: none" title="<s:text name="igeriv.allegato"/>"
					alt="<s:text name="igeriv.allegato"/>" /></a>
			</div>
		</s:if>
	</div>
</div>

