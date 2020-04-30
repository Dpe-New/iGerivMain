<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#breadcrumb {
	height: 30px;
}

div#filter {
	height: 35px;
	margin-top: 10px;
	background: #cccccc;
}

div#page {
	height: 450px;
}

div#content1 {
	height: 420px;
}

div#footer {
	margin-top: 0px
}
</style>
<div style="width: 100%; text-align: center;">
	<span style="float: left; width: 42%; display: block;">&nbsp;</span> <span
		style="float: left; width: 14%; cursor: pointer;"
		onclick="javascript: chiudiConto(false, 0);"> <img
		id="finalizza" src="/app_img/barcode_finalizza.png"
		alt="<s:text name="igeriv.barcode.chiudi.conto"/>" border="0"
		title="<s:text name="igeriv.barcode.chiudi.conto"/>" />
	</span> <span
		style="float: left; width: 42%; text-align: center; display: block;">
		<s:if
			test="authUser.tipoUtente == 1 && authUser.edicolaDeviettiTodis eq true">
			<div id="iconsDiv" style="text-align: center; position: absolute;">
				<div style="float: right; margin-left: 10px">
					<a href="/client_vendite/client_vendite.zip" target="_new"><img
						id="downloadClientVendite" src="/app_img/client_vendite.png"
						alt="<s:text name="igeriv.accedi.store"/>" border="0"
						title="<s:text name="igeriv.download.client.vendite"/>" /></a>
				</div>
			</div>
		</s:if>
	</span>
</div>