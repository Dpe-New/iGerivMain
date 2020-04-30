<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<style>
div#filter {
	height: 20px;
}

div#content1 {
	height: 650px;
}
</style>
	<s:if test="authUser.edicoleVedonoMessaggiDpe eq true">
		<div style="width: 100%; text-align: center">
			<div style="float: left; width: 1%; text-align: center">&nbsp;</div>
			<div style="float: left; width: 98%; text-align: center">
				<div style="width: 100%; height: 80px; text-align: center">
					<!-- <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,5,0,175" width="468" height="60"><param name="movie" value="/banners/demo_banner.swf" /><param name=FlashVars value="clickTAG=http%3A//www.dpe.it" /><param name="menu" value="false" /><param name="scale" value="exactfit" /><param name="AllowScriptAccess" value="always" /><embed src="/banners/demo_banner.swf" FlashVars="clickTAG=http%3A//localhost:8082/iGeriv/banner.action%3FactionName=http%3A//www.dpe.it" width="468" height="60" menu="false" scale="exactfit" AllowScriptAccess="always" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" /></object> -->
					&nbsp;
				</div>
				<div style="width: 100%; text-align: center">
					<iframe
						src="${pageContext.request.contextPath}/msgDpe_viewActiveMessagesIFrame.action?abilitati=true"
						id="novita" width="950px" height="550px" align="center"
						frameBorder="0" marginwidth="0" marginheight="0" hspace="0"
						vspace="0" scrolling="no" allowtransparency="true"
						background-color:transparent> </iframe>
				</div>
			</div>
			<div style="float: left; width: 1%; text-align: center">&nbsp;</div>
		</div>
	</s:if>
</s:if>