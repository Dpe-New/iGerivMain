<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.statisticheUtilizzo != null && !#request.statisticheUtilizzo.isEmpty()}">
	<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
	<s:if test='#context["struts.actionMapping"].namespace == "/"'>
		<s:set name="ap" value="" />
	</s:if>
	<s:form id="StatisticaUtilizzoForm"
		action="statisticaUtilizzo_showStatisticaUtilizzo.action"
		method="POST" theme="simple" validate="false"
		onsubmit="return (ray.ajax())">
		<div style="width: 100%">
			<dpe:table tableId="StatisticaUtilizzoTab"
				items="statisticheUtilizzo" var="statisticheUtilizzo"
				action="${pageContext.request.contextPath}${ap}/statisticaUtilizzo_showStatisticaUtilizzo.action"
				imagePath="/app_img/table/*.gif" rowsDisplayed="1000"
				view="buttonsOnBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.StatisticaUtilizzoAction"
				styleClass="extremeTableFields" form="StatisticaUtilizzoForm"
				theme="eXtremeTable bollaScrollDiv" showPagination="false"
				id="StatisticaUtilizzoDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false">
				<dpe:exportPdf fileName="statistiche_utilizzo.pdf"
					tooltip="plg.esporta.pdf" headerTitle="igeriv.statistiche.utilizzo"
					headerColor="black" headerBackgroundColor="#b6c2da"
					isLandscape="true" />
				<ec:exportXls fileName="statistiche_utilizzo.xls"
					tooltip="plg.esporta.excel" />
				<ec:row style="height:25px;">
					<dpe:column property="codRivenditaDl" width="10%"
						title="igeriv.statistiche.utilizzo.codice.rivendita.dl"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<dpe:column property="codRivenditaWeb" width="10%"
						title="igeriv.statistiche.utilizzo.codice.rivendita.web"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<dpe:column property="nomeRivendita" width="35%"
						title="igeriv.statistiche.utilizzo.nome.rivendita"
						filterable="false" />
					<dpe:column property="numBolle" width="8%"
						title="igeriv.statistiche.utilizzo.numero.bolle"
						filterable="false" allowZeros="true" style="text-align:center"
						headerStyle="text-align:center" />
					<dpe:column property="numRese" width="8%"
						title="igeriv.statistiche.utilizzo.numero.rese" filterable="false"
						allowZeros="true" style="text-align:center"
						headerStyle="text-align:center" />
					<dpe:column property="numVendite" width="8%"
						title="igeriv.statistiche.utilizzo.numero.vendite"
						filterable="false" allowZeros="true" style="text-align:center"
						headerStyle="text-align:center" />
					<dpe:column property="numRifornimenti" width="8%"
						title="igeriv.statistiche.utilizzo.numero.rifornimenti"
						filterable="false" allowZeros="true" style="text-align:center"
						headerStyle="text-align:center" />
					<dpe:column property="numVariazioni" width="8%"
						title="igeriv.statistiche.utilizzo.numero.variazioni"
						filterable="false" allowZeros="true" style="text-align:center"
						headerStyle="text-align:center" />
				</ec:row>
			</dpe:table>
		</div>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields"
		style="align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>
