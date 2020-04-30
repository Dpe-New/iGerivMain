<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.prodotti != null && !#request.prodotti.isEmpty()}">
	<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
	<s:if test='#context["struts.actionMapping"].namespace == "/"'>
		<s:set name="ap" value="" />
	</s:if>
	<s:actionerror />
	<s:form id="ProdottiForm" action="pne_saveProdotto.action"
		method="POST" theme="simple" validate="false"
		onsubmit="return (ray.ajax())">
		<div style="width: 100%">
			<dpe:table tableId="ProdottiTab" items="prodotti" var="prodotti"
				action="${pageContext.request.contextPath}${ap}/pne_saveProdotto.action"
				imagePath="/app_img/table/*.gif" title="${tableTitle}"
				rowsDisplayed="1000" view="buttonsOnBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.AnagraficaProdottiNonEditorialiAction"
				styleClass="extremeTableFields" form="ProdottiForm"
				theme="eXtremeTable bollaScrollDivTall" showPagination="false"
				id="ProdottiDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false">
				<dpe:exportPdf fileName="prodotti_non_editoriali.pdf"
					tooltip="plg.esporta.pdf"
					headerTitle="igeriv.prodotti.non.editoriali" headerColor="black"
					headerBackgroundColor="#b6c2da" isLandscape="true" />
				<ec:exportXls fileName="prodotti_non_editoriali.xls"
					tooltip="plg.esporta.excel" />
				<ec:row style="height:30px">
					<ec:column property="codEdicolaWeb" width="5%"
						title="dpe.login.dl.fieg.code.short" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="password" width="15%" title="Password"
						filterable="false" />
					<ec:column property="codEdicolaDl" width="5%"
						title="dpe.login.dl.web.code.short" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="ragioneSociale" width="20%"
						title="dpe.rag.sociale" filterable="false" />
					<ec:column property="indirizzo" width="15%" title="dpe.indirizzo"
						filterable="false" />
					<ec:column property="localita" width="15%" title="dpe.localita"
						filterable="false" />
					<ec:column property="provincia" width="5%" title="dpe.provincia"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<dpe:column property="dataInserimento" cell="date"
						dateFormat="dd/MM/yyyy" width="8%"
						title="igeriv.richiesta.rifornimenti.data.inserimento"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<dpe:column property="dataSospensione" maxlength="10" size="10"
						width="8%" dateFormat="dd/MM/yyyy" title="igeriv.data.sospensione"
						sessionVarName="dateSospensione" hasHiddenPkField="true"
						filterable="false" cell="textDifferenza" pkName="codEdicolaDl"
						styleClass="extremeTableFields" sortable="true"
						exportStyle="text-align:center" style="text-align:center"
						headerStyle="text-align:center" />
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


