<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv" style="margin-top: 30px">
	<s:if
		test="%{#request.listPubblicazioni != null && !#request.listPubblicazioni.isEmpty()}">
		<s:if test="tableTitle != null">
			<s:set var="title" value="%{tableTitle}" />
		</s:if>
		<div id="mainDiv" style="width: 100%">
			<s:form id="InfoPubblicazioniForm"
				action="statisticaPubblicazioni_showNumeriPubblicazioniStatistica.action"
				method="POST" theme="simple" validate="false"
				onsubmit="return (ray.ajax())">
				<s:hidden name="titoloDet" id="titoloDet" />
				<div style="width: 850px">
					<dpe:table tableId="InfoPubblicazioniTab" items="listPubblicazioni"
						var="listPubblicazioni"
						action="${pageContext.request.contextPath}/statisticaPubblicazioni_showNumeriPubblicazioniStatistica.action"
						imagePath="/app_img/table/*.gif" title="${title}"
						style="height:60px;" view="buttonsOnBottom"
						locale="${localeString}"
						state="it.dpe.igeriv.web.actions.PubblicazioniAction"
						styleClass="extremeTableFields" form="InfoPubblicazioniForm"
						theme="eXtremeTable bollaScrollSmallerDiv" showPagination="false"
						id="PubblicazioniScrollDiv" toolbarClass="eXtremeTable"
						showStatusBar="false" showTitle="true" showTooltips="false"
						footerStyle="height:30px; width:100%" filterable="false">
						<dpe:exportPdf fileName="pubblicazioni.pdf"
							tooltip="plg.esporta.pdf" headerTitle="${title}"
							headerColor="black" headerBackgroundColor="#b6c2da"
							isLandscape="true" />
						<ec:exportXls fileName="pubblicazioni.xls"
							tooltip="plg.esporta.excel" />
						<dpe:row interceptor="marker" href="#?idtn=idtn"
							style="height:25px">
							<ec:column property="numeroCopertina" width="15%"
								title="igeriv.numero" filterable="false" sortable="false" />
							<s:if test="%{authUser.visualizzaResoRiscontratoStatistica}">
								<ec:column property="sottoTitolo" width="15%"
									title="igeriv.sottotitolo" filterable="false" sortable="false" />
							</s:if>
							<s:else>
								<ec:column property="sottoTitolo" width="20%"
									title="igeriv.sottotitolo" filterable="false" sortable="false" />
							</s:else>
							
							<dpe:column property="dataUscita" width="10%" title="igeriv.data.uscita" cell="date" dateFormat="dd/MM/yyyy" filterable="false" sortable="false" />
							
							<dpe:column property="prezzoCopertina" width="5%" title="igeriv.prezzo.lordo" cell="currency" filterable="false" sortable="false" style="text-align:right" />
							
							<!-- Modifica del 10/01/2016 Prezzo netto della pubblicazione -->
							<dpe:column property="prezzoEdicola"   
								width="5%" title="igeriv.prezzo.netto" format="###,###,##0.0000" 
								cell="currency" filterable="false" 
								sortable="false" style="text-align:right" />
								
							<%-- 	<ec:column property="prezzoCopertina" width="5%" title="igeriv.prezzo" cell="currency" filterable="false" sortable="false" style="text-align:right" /> --%>
							
							<ec:column property="fornito" width="5%" title="igeriv.fornito" filterable="false" sortable="false" style="cursor:pointer; text-align:center" />
							
							<s:if test="%{authUser.visualizzaResoRiscontratoStatistica}">
								<ec:column property="reso" width="5%"
									title="igeriv.reso.dichiarato" filterable="false"
									sortable="false" style="cursor:pointer; text-align:center" />
								<ec:column property="resoRiscontrato" width="5%"
									title="igeriv.reso.riscontrato" filterable="false"
									sortable="false" style="cursor:pointer; text-align:center" />
							</s:if>
							<s:else>
								<ec:column property="reso" width="5%" title="igeriv.reso"
									filterable="false" sortable="false"
									style="cursor:pointer; text-align:center"
									headerStyle="text-align:center" />
							</s:else>
							<ec:column property="respinto" width="5%"
								title="igeriv.reso.respinto" filterable="false" sortable="false"
								style="cursor:pointer; text-align:center" />
							<ec:column property="venduto" width="5%" title="igeriv.venduto"
								filterable="false" sortable="false" style="text-align:center" />
							<ec:column property="giacenza" width="5%"
								title="igeriv.giacienza.ext" filterable="false" sortable="false"
								style="text-align:center" />
							<ec:column property="vendite" width="5%"
								title="gp.report.vendite.minicard" filterable="false"
								sortable="false" style="cursor:pointer; text-align:center" />
							<ec:column property="immagine" title="igeriv.img"
								filterable="false" viewsDenied="pdf,xls" sortable="false"
								width="8%"
								cell="viewImageCell" >
<%-- 								<a href="/immagini/${pageScope.listPubblicazioni.immagine}" --%>
<!-- 									rel="thumbnail"><img src="/app_img/camera.gif" width="15px" -->
<!-- 									height="15px" border="0" style="border-style: none" /></a> -->
							</ec:column>
						</dpe:row>
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