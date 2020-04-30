<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.bolle != null && !#request.bolle.isEmpty()}">
	<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
	<s:if test='#context["struts.actionMapping"].namespace == "/"'>
		<s:set name="ap" value="" />
	</s:if>
	<s:form id="BolleProdottiVariForm"
		action="pneBollaCarico_showBolle.action" method="POST" theme="simple"
		validate="false" onsubmit="return (ray.ajax())">
		<div style="width: 100%">
			<dpe:table tableId="BolleProdottiVariTab" items="bolle" var="bolle"
				action="${pageContext.request.contextPath}${ap}/pneBollaCarico_showBolle.action"
				imagePath="/app_img/table/*.gif" rowsDisplayed="1000"
				view="buttonsOnBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.ProdottiNonEditorialiCaricoBollaAction"
				styleClass="extremeTableFields" form="BolleProdottiVariForm"
				theme="eXtremeTable bollaScrollDivTall" showPagination="false"
				id="BolleProdottiVariDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false">
				<dpe:exportPdf fileName="bolle_prodotti_vari.pdf"
					tooltip="plg.esporta.pdf"
					headerTitle="igeriv.carico.bolla.prodotti.vari" headerColor="black"
					headerBackgroundColor="#b6c2da" isLandscape="true" />
				<ec:exportXls fileName="bolle_prodotti_vari.xls"
					tooltip="plg.esporta.excel" />
				<dpe:row highlightRow="true" interceptor="marker"
					href="#?idDocumento=idDocumento"
					style="cursor:pointer; height:30px">
					<ec:column property="idDocumento" width="10%"
						title="igeriv.id.documento" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="dataDocumento" cell="date" format="dd/MM/yyyy"
						width="15%" title="igeriv.data.documento" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="numeroDocumento" width="15%"
						title="igeriv.numero.documento" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="nomeFornitore" width="20%"
						title="igeriv.fornitore" filterable="false" />
					<ec:column property="tipoDocumento" width="15%"
						title="igeriv.tipo.documento" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="numeroOrdine" width="10%"
						title="igeriv.richiesta.rifornimenti.numero.ordine"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="dataRegistrazione" cell="date"
						format="dd/MM/yyyy" width="10%"
						title="igeriv.pne.report.magazzino.data.registrazione"
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


