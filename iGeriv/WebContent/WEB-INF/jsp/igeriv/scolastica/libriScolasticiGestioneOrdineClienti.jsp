<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="an" value="%{actionName}" />
<s:if
	test="%{#request.risricercalibri != null && !#request.risricercalibri.isEmpty()}">
	<s:form id="GestioneRisultatiRicercaLibriForm" action="%{actionName}"
		method="POST" theme="simple" validate="false"
		onsubmit="return (ray.ajax())">
		<div style="width: 100%">
			<dpe:table tableId="LibriScolasticiClientiTab"
				items="risricercalibri" var="risricercalibri"
				action="${pageContext.request.contextPath}/${an}"
				imagePath="/app_img/table/*.gif" 
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.LibriScolasticiClientiAction"
				styleClass="extremeTableFields"
				form="GestioneRisultatiRicercaLibriForm"
				theme="eXtremeTable bollaScrollDiv" 
				showPagination="false"
				rowsDisplayed="15" id="BollaScrollDiv1" 
				toolbarClass="eXtremeTable"
				footerStyle="height:30px;" 
				filterable="false">
<%-- 				<dpe:exportPdf fileName="clienti.pdf" tooltip="plg.esporta.pdf" --%>
<%-- 					headerTitle="gp.clienti.edicola" headerColor="black" --%>
<%-- 					headerBackgroundColor="#b6c2da" isLandscape="true" /> --%>
<%-- 				<ec:exportXls fileName="clienti.xls" tooltip="plg.esporta.excel" /> --%>
				<dpe:row style="height:30px; cursor:pointer">
<%-- 					<ec:column property="SKU" width="10%" --%>
<%-- 						title="dpe.dettaglio.libro.sku" filterable="false" /> --%>
					<ec:column property="BARCODE" width="15%"
						title="dpe.dettaglio.libro.barcode" filterable="false" />
					<ec:column property="TITOLO" width="15%"
						title="dpe.dettaglio.libro.titolo" filterable="false" />
					<ec:column property="VOLUME" width="5%"
						title="dpe.dettaglio.libro.volume" filterable="false" />
					<ec:column property="AUTORI" width="15%"
						title="dpe.dettaglio.libro.autori" filterable="false" />
					<ec:column property="EDITORE" width="15%"
						title="dpe.dettaglio.libro.editore" filterable="false" />
					<ec:column property="PREZZO" width="15%"
						title="dpe.dettaglio.libro.prezzo" filterable="false" />
					<ec:column property="URL" title="igeriv.img" filterable="false"
						sortable="false" width="4%" style="text-align:center"
						viewsDenied="pdf,xls">
						<a href="${pageScope.risricercalibri.URL}" rel="thumbnail"><img
							src="/app_img/camera-active.png" width="15px" height="15px" border="0"
							style="border-style: none" /></a>
					</ec:column>
					<ec:column property="dettaglioLibroScolastico"
						cell="dpeOpenDetailLibroScolaticoCell"
						title="dpe.view.anagrafica.dettaglio" filterable="false"
						width="20%" style="text-align:center;"
						headerStyle="text-align:center" />



				</dpe:row>
			</dpe:table>
		</div>
		<s:hidden name="idNumeroOrdine" id="idNumeroOrdine" 	value="%{#request.numeroOrdine}" />
		
		<s:hidden name="keyguid" id="keyguid"
			value="%{#request.paramricercalibri.GUID}" />
		

	</s:form>
</s:if>
<s:else>
	<div class="tableFields"
		style="width: 100%; align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>
