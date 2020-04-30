<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<div style="width: 100%">
	<div id="prodottiEdicola" style="float: left; width: 100%;">
		<iframe
			src="${pageContext.request.contextPath}${ap}/pne_showProdotti.action"
			id="prodottiEdicolaFrame" width="100%" height="430px" align="center"
			frameborder="0" border="0" marginwidth="0" marginheight="0"
			hspace="0" vspace="0" scrolling="no" allowtransparency="true"
			background-color:transparent> </iframe>
	</div>
</div>

