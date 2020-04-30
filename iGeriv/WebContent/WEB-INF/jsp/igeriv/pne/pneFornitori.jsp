<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.fornitori != null && !#request.fornitori.isEmpty()}">
	<s:set name="an" value="%{actionName}" />
	<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
	<s:if test='#context["struts.actionMapping"].namespace == "/"'>
		<s:set name="ap" value="" />
	</s:if>
	<s:form id="FornitoriForm" action="pneFornitori_showFornitori.action"
		method="POST" theme="simple" validate="false"
		onsubmit="return (ray.ajax())">
		<div style="width: 100%">
			<dpe:table tableId="FornitoriTab" items="fornitori" var="fornitori"
				action="${pageContext.request.contextPath}${ap}/${an}"
				imagePath="/app_img/table/*.gif" rowsDisplayed="1000"
				view="buttonsOnBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.ProdottiNonEditorialiFornitoriAction"
				styleClass="extremeTableFields" form="FornitoriForm"
				theme="eXtremeTable bollaScrollDivTall" showPagination="false"
				id="FornitoriDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false">
				<dpe:exportPdf fileName="fornitori.pdf" tooltip="plg.esporta.pdf"
					headerTitle="igeriv.edicole" headerColor="black"
					headerBackgroundColor="#b6c2da" isLandscape="true" />
				<ec:exportXls fileName="fornitori.xls" tooltip="plg.esporta.excel" />
				<dpe:row highlightRow="true" interceptor="marker"
					href="#?codFornitore=pk.codFornitore"
					style="cursor:pointer; height:30px">
					<ec:column property="nome" width="15%" title="dpe.rag.sociale"
						filterable="false" />
					<ec:column property="tipoLocalita.descrizione" width="5%"
						title="dpe.tipo.localita" filterable="false" />
					<ec:column property="indirizzo" width="15%" title="dpe.indirizzo"
						filterable="false" />
					<ec:column property="localita.descrizione" width="15%"
						title="dpe.localita" filterable="false" />
					<ec:column property="provincia.descrizione" width="5%"
						title="dpe.provincia" filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="piva" width="15%" title="dpe.piva"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="telefono" width="10%" title="dpe.telefono"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="email" width="15%" title="dpe.email"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
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


