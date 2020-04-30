<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv" style="margin-top: 30px">
	<s:if test="tableTitle != null">
		<s:set var="title" value="%{tableTitle}" />
	</s:if>
	<div id="mainDiv" style="width: 100%">
		<s:form id="StatisticaDettaglioForm"
			action="statisticaPubblicazioni_showStatisticaDettaglioVenduto.action"
			method="POST" theme="simple" validate="false"
			onsubmit="return (ray.ajax())">
			<dpe:table tableId="StatisticaDettaglioTab"
				items="listStatisticaDettaglio" var="listStatisticaDettaglio"
				action="${pageContext.request.contextPath}/statisticaPubblicazioni_showStatisticaDettaglioVenduto.action"
				imagePath="/app_img/table/*.gif" title="${title}"
				style="height:60px; width:450px" view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.PubblicazioniAction"
				styleClass="extremeTableFields" form="StatisticaDettaglioForm"
				theme="eXtremeTable bollaScrollSmallerDiv" showPagination="false"
				id="PubblicazioniScrollDiv" toolbarClass="eXtremeTable"
				showStatusBar="false" showTitle="true" showTooltips="false"
				footerStyle="height:30px; width:100%" filterable="false">
				<dpe:exportPdf fileName="dettaglio_venduto_statistica.pdf"
					tooltip="plg.esporta.pdf" headerTitle="${title}"
					headerColor="black" headerBackgroundColor="#b6c2da" />
				<ec:exportXls fileName="dettaglio_venduto_statistica.xls"
					tooltip="plg.esporta.excel" />
				<ec:row style="height:25px">
					<dpe:column property="data" width="38%" title="igeriv.data.vendita"
						cell="date" format="dd/MM/yyyy HH:mm:ss" filterable="false"
						sortable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<dpe:column property="copie" width="38%" title="igeriv.copie"
						filterable="false" sortable="false" style="text-align:center"
						headerStyle="text-align:center" />
				</ec:row>
			</dpe:table>
		</s:form>
	</div>
</div>