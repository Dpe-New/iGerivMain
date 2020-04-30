<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.contestazioniResa != null && !#request.contestazioniResa.isEmpty()}">
	<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
	<s:if test='#context["struts.actionMapping"].namespace == "/"'>
		<s:set name="ap" value="" />
	</s:if>
	<s:form id="ContestazioniResaForm"
		action="contestazioniResa_showContestazioniResa.action" method="POST"
		theme="simple" validate="false" onsubmit="return (ray.ajax())">
		<div style="width: 100%">
			<dpe:table tableId="ContestazioniResaTab" items="contestazioniResa"
				var="contestazioniResa"
				action="${pageContext.request.contextPath}${ap}/contestazioniResa_showContestazioniResa.action"
				imagePath="/app_img/table/*.gif" title="${tableTitle}"
				rowsDisplayed="1000" view="buttonsOnBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.MancanzeAction"
				styleClass="extremeTableFields" form="ContestazioniResaForm"
				theme="eXtremeTable bollaScrollDivTall" showPagination="false"
				id="ContestazioniResaDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false">
				<dpe:exportPdf fileName="contestazioni_resa.pdf"
					tooltip="plg.esporta.pdf" headerTitle="${title}"
					headerColor="black" headerBackgroundColor="#b6c2da"
					isLandscape="true" />
				<ec:exportXls fileName="contestazioni_resa.xls"
					tooltip="plg.esporta.excel" />
				<ec:row style="height:25px">
					<dpe:column property="dataResa" cell="date" dateFormat="dd/MM/yyyy"
						width="7%" title="igeriv.data.resa" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="tipoResa" width="7%" title="igeriv.tipo.resa"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="titolo" width="18%" title="igeriv.titolo"
						filterable="false" style="text-align:left"
						headerStyle="text-align:left" />
					<dpe:column property="numero" width="7%" title="igeriv.numero"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="statoDesc" width="10%"
						title="igeriv.richiesta.rifornimenti.quantita.stato"
						filterable="false" style="text-align:left"
						headerStyle="text-align:left" />
					<ec:column property="copieDichiarate" width="7%"
						title="igeriv.copie.dichiarate" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<dpe:column property="copieApprovate" width="7%"
						title="igeriv.copie.approvate" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<dpe:column property="importoDichiarato" hasCurrencySign="true"
						cell="currency" width="7%" title="igeriv.importo.dichiarate"
						filterable="false" style="text-align:right"
						headerStyle="text-align:right" />
					<dpe:column property="importoApprovato" hasCurrencySign="true"
						cell="currency" width="7%" title="igeriv.importo.approvate"
						filterable="false" style="text-align:right"
						headerStyle="text-align:right" />
					<ec:column property="note" width="20%" title="igeriv.fondo.bolla"
						filterable="false" style="text-align:left"
						headerStyle="text-align:left" />
				</ec:row>
			</dpe:table>
		</div>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields"
		style="width: 100%; align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>


