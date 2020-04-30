<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>

<s:form id="popupDettaglioOrdiniClientiForm" action="libriScolasticiClienti_showClienteDettaglioOrdini.action"	method="POST" theme="simple" validate="false"	onsubmit="return (ray.ajax())">


		<dpe:table tableId="DettaglioOrdiniClientiTab" 
			items="listOrdini"
			var="listOrdini"
			action="${pageContext.request.contextPath}/${an}"
			imagePath="/app_img/table/*.gif" 
			view="buttonsOnBottom"
			locale="${localeString}"
			state="it.dpe.igeriv.web.actions.LibriScolasticiClientiAction"
			styleClass="extremeTableFields"
			form="GestioneDettaglioOrdiniClientiForm"
			theme="eXtremeTable bollaScrollDiv" 
			showPagination="false"
			rowsDisplayed="15" id="BollaScrollDiv1" 
			toolbarClass="eXtremeTable"
			footerStyle="height:30px;" 
			filterable="false">


			<dpe:row style="height:30px; cursor:pointer">
				<ec:column property="numeroOrdineTxt" 		width="10%" title="dpe.ordine.codice.ordine" filterable="false" />
				<ec:column property="dataAperturaOrdine" 	width="10%" cell="date" format="dd/MM/yyyy" title="dpe.ordine.data.inizio.ordine" filterable="false" />
				<ec:column property="dataChiusuraOrdine" 	width="10%" cell="date" format="dd/MM/yyyy" title="dpe.ordine.data.fine.ordine" filterable="false" />
				<ec:column property="prezzoTotale" 			width="10%" cell="currency" format="###,###,##0.00" title="dpe.ordine.totale.ordine" filterable="false" />
				
				<ec:column property="dettaglioTracking"  cell="dpeOpenTrackingOrdineCell"  title="dpe.view.tracking" filterable="false" width="15%" style="text-align:center;" headerStyle="text-align:center" />
				<ec:column property="dettaglioPageConsegnaLibri"  cell="dpeOpenVenditaLibriCell"    title="dpe.view.consegna.libri" filterable="false" width="15%" style="text-align:center;" headerStyle="text-align:center" />
<%-- 				<ec:column property="dettaglioTracking" title="dpe.view.tracking" filterable="false" sortable="false" width="4%" style="text-align:center" viewsDenied="pdf,xls"> --%>
<%-- 					<img src="/app_img/tracking_48.png" width="35px" height="35x" border="0" style="border-style: none" onclick="viewTracking(${pageScope.listOrdini.numeroOrdineTxt});" /> --%>
<%-- 				</ec:column> --%>
			
			</dpe:row>
		</dpe:table>

</s:form>
