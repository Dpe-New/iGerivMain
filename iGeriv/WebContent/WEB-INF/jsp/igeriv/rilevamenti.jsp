<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
div#filter {
	height: 25px;
}

div#content1 {
	height: 480px;
}
</style>
<s:if
	test="%{#request.rilevamenti != null && !#request.rilevamenti.isEmpty()}">
	<s:form id="RilevamentiForm"
		action="rilevamenti_showRilevamenti.action" method="POST"
		theme="simple" validate="false" onsubmit="return (ray.ajax())">
		<div style="text-align: center">
			<fieldset class="filterBolla">
				<div
					style="margin-left: 100px; width: 300px; text-align: center; margin-top: 12px; font-weight: bold;">
					<s:property value="titleRilevamenti" />
				</div>
			</fieldset>
		</div>
		<div>
			<dpe:table tableId="RilevamentiTab" items="rilevamenti"
				var="rilevamenti" action="rilevamenti_showRilevamenti.action"
				imagePath="/app_img/table/*.gif" style="table-layout:fixed;"
				view="buttonsOnTopAndBottom"
				extraToolButton='<input type="button" value="${requestScope.memorizzaInvia}" name="igeriv.memorizza.invia" id="memorizzaInvia" class="tableFields" style="width:150px; text-align:center;" onclick="javascript: setTimeout(jConfirm(memorizzaRilevamentiConfirm, attenzioneMsg, function(r) { if (r) { return (setFormAction(\'RilevamentiForm\',\'rilevamenti_saveRilevamenti.action\', \'\', \'messageDiv\', false, \'\', function() { $(\'#RilevamentiForm\').attr(\'action\',\'rilevamenti_showRilevamenti.action\'); $(\'#RilevamentiForm\').submit();}))} unBlockUI();}),10);"/>'
				extraToolButtonStyle="text-align:right;" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.RilevamentiAction"
				styleClass="extremeTableFieldsSmaller" form="RilevamentiForm"
				theme="eXtremeTable bollaScrollDivTall" showPagination="false"
				id="BollaScrollDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false"
				autoIncludeParameters="false">
				<dpe:exportPdf fileName="rilevamenti.pdf" tooltip="plg.esporta.pdf"
					headerTitle="${titleRilevamenti}" headerColor="black"
					headerBackgroundColor="#b6c2da" />
				<ec:exportXls fileName="rilevamenti.xls" tooltip="plg.esporta.excel" />
				<dpe:row highlightRow="true">
					<ec:column property="tipoBolla" width="5%" title="igeriv.tipo"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="titolo" width="20%" title="igeriv.titolo"
						filterable="false" />
					<ec:column property="sottoTitolo" width="15%"
						title="igeriv.sottotitolo" filterable="false" />
					<dpe:column property="numeroCopertina" width="8%"
						title="igeriv.numero" filterable="false" escapeAutoFormat="true"
						style="text-align:center" headerStyle="text-align:center" />
					<dpe:column property="dtBolla" width="8%" title="igeriv.data"
						cell="date" dateFormat="dd/MM/yyyy" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<dpe:column property="prezzoCopertina" cell="currency"
						hasCurrencySign="true" format="###,###,##0.00"
						style="text-align:right" headerStyle="text-align:right" width="7%"
						title="igeriv.prezzo.copertina" filterable="false" />
					<dpe:column property="distribuito" style="text-align:center"
						headerStyle="text-align:center" width="7%" title="igeriv.fornito"
						filterable="false" />
					<dpe:column property="rilevamento" width="7%"
						title="igeriv.rilevamento" filterable="false"
						cell="rilevamentoCell" sortable="false"
						exportStyle="text-align:center" headerStyle="text-align:center" />
					<ec:column property="nomeImmagine" title="igeriv.img"
						filterable="false" viewsDenied="pdf,xls" sortable="false"
						width="9%" style="text-align:center;"
						headerStyle="text-align:center"
						cell="viewImageCell" >
<%-- 						<a href="/immagini/${pageScope.rilevamenti.nomeImmagine}" --%>
<!-- 							rel="thumbnail"><img src="/app_img/camera.gif" width="15px" -->
<!-- 							height="15px" border="0" -->
<!-- 							style="border-style: none; text-align: center" /></a> -->
					</ec:column>
				</dpe:row>
			</dpe:table>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields"
		style="align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>
