<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="an" value="%{actionName}" />

<s:if test="%{#request.elencoLibriTraking != null && !#request.elencoLibriTraking.isEmpty()}">
	
	<s:form id="LibriScolasticiTrackingForm" action="%{actionName}"	method="POST" theme="simple" validate="false"	onsubmit="return (ray.ajax())">
		<div style="width: 100%">
			<dpe:table 
				tableId="LibriScolasticiTrackingTab"
				items="elencoLibriTraking" 
				var="elencoLibriTraking"
				action="${pageContext.request.contextPath}/${an}"
				imagePath="/app_img/table/*.gif" 
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.LibriScolasticiTrackingClientAction"
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

					<ec:column property="barcode" width="15%"				title="dpe.dettaglio.libro.barcode" filterable="false" />
					<ec:column property="titolo" width="15%"				title="dpe.dettaglio.libro.titolo" filterable="false" />
					<ec:column property="autore" width="15%"				title="dpe.dettaglio.libro.autori" filterable="false" />
					<ec:column property="editore" width="15%"				title="dpe.dettaglio.libro.editore" filterable="false" />
					<ec:column property="prezzoCopertina" width="15%"		cell="currency"		title="dpe.dettaglio.libro.prezzo" filterable="false" />
					<ec:column property="descrizioneStato" width="15%"				title="dpe.dettaglio.libro.stato" filterable="false" />
					
					<ec:column property="urlImmagineCopertina" title="igeriv.img" filterable="false"		sortable="false" width="4%" style="text-align:center"	viewsDenied="pdf,xls">
						<a href="${pageScope.elencoLibriTraking.urlImmagineCopertina}" rel="thumbnail"><img	src="/app_img/camera.gif" width="15px" height="15px" border="0"  style="border-style: none" /></a>
					</ec:column>
					
				</dpe:row>
			</dpe:table>
		</div>
		

	</s:form>
</s:if>
<s:else>
	<div class="tableFields" style="width: 100%; align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>
