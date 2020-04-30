<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv" style="margin-top: 0px">
	<s:if
		test="%{#request.listPubblicazioni != null && !#request.listPubblicazioni.isEmpty()}">
		<s:if test="tableTitle != null">
			<s:set var="title" value="%{tableTitle}" />
		</s:if>
		<div id="mainDiv" style="width: 100%">
			<s:form id="InfoPubblicazioniForm"
				action="infoPubblicazioni_showNumeriPubblicazioni.action"
				method="POST" theme="simple" validate="false"
				onsubmit="return (ray.ajax())">
				<div style="width: 850px">
					<dpe:table tableId="InfoPubblicazioniTab" items="listPubblicazioni"
						var="listPubblicazioni"
						action="${pageContext.request.contextPath}/infoPubblicazioni_showNumeriPubblicazioni.action"
						imagePath="/app_img/table/*.gif" 
						title="${title}"
						style="height:60px;" 
						view="buttonsOnBottom"
						locale="${localeString}"
						state="it.dpe.igeriv.web.actions.PubblicazioniAction"
						styleClass="extremeTableFields" 
						form="InfoPubblicazioniForm"
						theme="eXtremeTable bollaScrollSmallerDiv" 
						showPagination="false"
						id="PubblicazioniScrollDiv" 
						toolbarClass="eXtremeTable"
						showStatusBar="false" 
						showTitle="true" 
						showTooltips="false"
						footerStyle="height:30px; width:100%" 
						filterable="false">
						<dpe:exportPdf fileName="pubblicazioni.pdf" 	tooltip="plg.esporta.pdf" headerTitle="${title}"
							headerColor="black" headerBackgroundColor="#b6c2da"	isLandscape="true" />
						<ec:exportXls fileName="pubblicazioni.xls"
							tooltip="plg.esporta.excel" />
						<ec:row style="height:20px" interceptor="marker">
							<ec:column property="numeroCopertina" width="5%" title="igeriv.numero" filterable="false" sortable="false" />
							<ec:column property="sottoTitolo" width="20%" title="igeriv.sottotitolo" filterable="false" sortable="false" />
							<dpe:column property="dataUscita" width="8%" title="igeriv.data.uscita" cell="date" dateFormat="dd/MM/yyyy" filterable="false" sortable="false" style="text-align:center" />
							
							<dpe:column property="prezzoCopertina" width="5%" title="igeriv.prezzo" cell="currency" filterable="false" sortable="false" style="text-align:right" />
							<dpe:column property="prezzoEdicola"   width="5%" title="igeriv.prezzo.netto" cell="currency" filterable="false" style="text-align:right" format="###,###,##0.0000" />
							
							<dpe:column property="dataRichiamoResa" width="8%" title="igeriv.data.richiamo.resa" cell="date" dateFormat="dd/MM/yyyy" filterable="false" sortable="false" style="text-align:center" />
							<ec:column property="tipoRichiamoResa" width="20%" title="igeriv.tipo.richiamo.resa" filterable="false" sortable="false" />
							<ec:column property="barcode" width="14%" title="igeriv.barcode" filterable="false" sortable="false" />
							<ec:column property="note" width="10%" title="igeriv.fondo.bolla" filterable="false" sortable="false" style="text-align:center" />
							<ec:column property="immagine" title="igeriv.img"
								filterable="false" viewsDenied="pdf,xls" sortable="false"
								width="8%" style="text-align:center"
								cell="viewImageCell" >
<%-- 								<a href="/immagini/${pageScope.listPubblicazioni.immagine}" --%>
<!-- 									rel="thumbnail"><img src="/app_img/camera.gif" width="15px" -->
<!-- 									height="15px" border="0" style="border-style: none" /></a> -->
							</ec:column>
						</ec:row>
					</dpe:table>
				</div>
			</s:form>
		</div>
	</s:if>
	<s:else>
		<div class="tableFields"
			style="width: 100%; align: center; text-align: center; margin-top: 50px">
			<s:property value="nessunRisultato" />
		</div>
	</s:else>
</div>