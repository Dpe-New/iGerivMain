<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.mancanze != null && !#request.mancanze.isEmpty()}">
	<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
	<s:if test='#context["struts.actionMapping"].namespace == "/"'>
		<s:set name="ap" value="" />
	</s:if>
	<s:form id="MancanzeForm"
		action="mancanzeBolla_showMancanzeBolla.action" method="POST"
		theme="simple" validate="false" onsubmit="return (ray.ajax())">
		<div style="width: 100%">
			<dpe:table tableId="MancanzeTab" items="mancanze" var="mancanze"
				action="${pageContext.request.contextPath}${ap}/mancanzeBolla_showMancanzeBolla.action"
				imagePath="/app_img/table/*.gif" title="${tableTitle}"
				rowsDisplayed="1000" view="buttonsOnBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.MancanzeAction"
				styleClass="extremeTableFields" form="MancanzeForm"
				theme="eXtremeTable bollaScrollDivTall" showPagination="false"
				id="MancanzeDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false">
				<dpe:exportPdf fileName="mancanze_in_bolla.pdf"
					tooltip="plg.esporta.pdf"
					headerTitle="igeriv.report.mancanze.bolla" headerColor="black"
					headerBackgroundColor="#b6c2da" isLandscape="true" />
				<ec:exportXls fileName="mancanze_in_bolla.xls"
					tooltip="plg.esporta.excel" />
				<dpe:row highlightRow="true" interceptor="marker"
					href="#?idtn=idtn&dtBolla=dtBollaFormat"
					style="cursor:pointer; height:25px">
					<dpe:column property="dtBolla" cell="date" dateFormat="dd/MM/yyyy"
						width="8%" title="igeriv.data.bolla" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="tipoBolla" width="8%"
						title="igeriv.tipo.bolla" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="codicePubblicazione" width="8%"
						title="igeriv.cpu" filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="titolo" width="25%"
						title="label.print.Table.Title" filterable="false"
						style="text-align:left" headerStyle="text-align:left" />
					<dpe:column property="dtUscita" cell="date" width="8%"
						title="igeriv.data.uscita" dateFormat="dd/MM/yyyy"
						styleClass="extremeTableFieldsSmaller" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="numero" width="8%"
						title="igeriv.report.numero" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="tipo" width="8%" title="igeriv.tipo"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="mancanze" width="8%" title="igeriv.mancanze"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="mancanzeAccreditate" width="8%"
						title="igeriv.mancanze.accreditate" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
				</dpe:row>
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


