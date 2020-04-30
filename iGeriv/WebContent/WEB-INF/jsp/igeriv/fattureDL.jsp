<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
div#filter {
	height: 100px;
}
</style>
<div id="contentDiv" style="margin-top: 30px">
	<s:if test="%{#request.nomeFile != null}">
		<div
			style="width: 300px; margin-top: 50px; margin-right: auto; margin-left: auto; text-align: center">
			<img id="pdfFatt" alt='<s:text name="igeriv.download.fatDL"/>'
				title='<s:text name="igeriv.download.fatture"/>'
				src='/app_img/pdf_download_48.png' style='cursor: pointer'
				border="0">
		</div>
	</s:if>
	<s:else>
		<div class="tableFields"
			style="width: 100%; align: center; text-align: center; margin-top: 50px">
			<s:property value="nessunRisultato" />
		</div>
	</s:else>
</div>