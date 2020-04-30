<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="an" value="%{actionName}" />

<s:if test="%{#request.listResLibri != null && !#request.listResLibri.isEmpty()}">
	
	<s:form id="LibriScolasticiConsegnaEdicolaForm" action="%{actionName}"	method="POST" theme="simple" validate="false"	onsubmit="return (ray.ajax())">
		<div style="width: 100%">
			<dpe:table 
				tableId="LibriScolasticiConsegnaEdicolaTab"
				items="listResLibri" 
				var="listResLibri"
				action="${pageContext.request.contextPath}/${an}"
				imagePath="/app_img/table/*.gif" 
				view="buttonsOnBottom"
				
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.LibriScolasticiConsegnaEdicolaAction"
				styleClass="extremeTableFields"
				form="LibriScolasticiTrackingForm"
				theme="eXtremeTable bollaScrollDiv" 
				showPagination="false"
				rowsDisplayed="15" 
				id="BollaScrollDiv1" 
				toolbarClass="eXtremeTable"
				footerStyle="height:30px;" 
				filterable="false">
			
				<dpe:row style="height:30px; cursor:pointer">
					<ec:column property="ordine.numeroOrdineTxt" width="10%"		title="dpe.ordine.numero.ordine" 	filterable="false" />
					<ec:column property="ordine.codCliente" width="10%"				title="dpe.ordine.codice.cliente" 	filterable="false" />
					<ec:column property="listResLibri.ordine.cliente.nome" 	width="10%"			title="dpe.ordine.nome.cognome" 	filterable="false" />
					<ec:column property="barcode" width="15%"						title="dpe.dettaglio.libro.barcode" filterable="false" />
					<ec:column property="titolo" width="15%"						title="dpe.dettaglio.libro.titolo" 	filterable="false" />
					<ec:column property="autore" width="15%"						title="dpe.dettaglio.libro.autori" 	filterable="false" />
					<ec:column property="editore" width="15%"						title="dpe.dettaglio.libro.editore" filterable="false" />
					<ec:column property="prezzoCopertina" width="10%"				cell="currency" title="dpe.dettaglio.libro.prezzo" 	filterable="false" />
					
					<ec:column property="urlImmagineCopertina" title="igeriv.img" filterable="false"		sortable="false" width="4%" style="text-align:center"	viewsDenied="pdf,xls">
						<a href="${pageScope.listResLibri.urlImmagineCopertina}" rel="thumbnail"><img	src="/app_img/camera.gif" width="15px" height="15px" border="0"  style="border-style: none" /></a>
					</ec:column>
					
				</dpe:row>
			</dpe:table>

		</div>
<%-- 		<s:hidden name="numeroCollo" id="numeroCollo" /> --%>
	</s:form>
</s:if>
<s:if test="%{#request.listResLibriRiepilogo != null && !#request.listResLibriRiepilogo.isEmpty()}">
		<div style="width: 100%">
			<dpe:table 
				tableId="LibriScolasticiConsegnaEdicolaRiepilogoTab"
				items="listResLibriRiepilogo" 
				var="listResLibriRiepilogo"
				action="${pageContext.request.contextPath}/${an}"
				imagePath="/app_img/table/*.gif" 
				view="buttonsOnBottom"
				
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.LibriScolasticiConsegnaEdicolaAction"
				styleClass="extremeTableFields"
				form="LibriScolasticiTrackingForm"
				theme="eXtremeTable bollaScrollDiv" 
				showPagination="false"
				rowsDisplayed="15" 
				id="BollaScrollDiv1" 
				toolbarClass="eXtremeTable"
				footerStyle="height:30px;" 
				filterable="false">
			
				<dpe:row style="height:30px; cursor:pointer">
					<ec:column property="ordine.numeroOrdineTxt" width="10%"		title="dpe.ordine.numero.ordine" 	filterable="false" />
					<ec:column property="ordine.codCliente" width="10%"				title="dpe.ordine.codice.cliente" 	filterable="false" />
					<ec:column property="ordine.cliente.nome" width="10%"			title="dpe.ordine.nome.cognome" 	filterable="false" />
					<ec:column property="barcode" width="15%"						title="dpe.dettaglio.libro.barcode" filterable="false" />
					<ec:column property="titolo" width="15%"						title="dpe.dettaglio.libro.titolo" 	filterable="false" />
					<ec:column property="dataArrivoRivendita" 	width="15%"			cell="date" format="dd/MM/yyyy"		title="dpe.dettaglio.libro.data.consegna.edicola" 	filterable="false" />
					<ec:column property="urlImmagineCopertina" title="igeriv.img" filterable="false"		sortable="false" width="4%" style="text-align:center"	viewsDenied="pdf,xls">
						<a href="${pageScope.listResLibriRiepilogo.urlImmagineCopertina}" rel="thumbnail"><img	src="/app_img/camera.gif" width="15px" height="15px" border="0"  style="border-style: none" /></a>
					</ec:column>
					
				</dpe:row>
			</dpe:table>

		</div>
</s:if>
<s:else>
	<div class="tableFields" style="width: 100%; align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>
