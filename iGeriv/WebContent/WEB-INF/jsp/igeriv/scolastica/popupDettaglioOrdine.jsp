<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="an" value="%{actionName}" />

<s:form id="popupDettaglioOrdineForm" action="libriScolasticiClienti_showDettaglioOrdine.action"	method="POST" theme="simple" validate="false"	onsubmit="return (ray.ajax())">




	<div style="width: 100%">
		<table id="infoHeaderPopUpDettaglioOrdine" width="810px"
			cellspacing="3">
			<tr>
				<td style="text-align: right;">
<%-- 				<s:text name="dpe.ordine.codice.ordine" /> --%>
				</td>
				<td>
<%-- 				<strong><s:text	name="%{#request.infoordine.numeroOrdine}" /></strong> --%>
				</td>
				<td style="text-align: right;"><s:text
						name="dpe.ordine.codice.cliente" /></td>
				<td><strong><s:text
							name="%{#request.infoordine.codCliente}" /></strong></td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td style="text-align: right;"><s:text
						name="dpe.ordine.nome.cognome" /></td>
				<td><strong><s:text
							name="%{#request.anagraficacliente.nome}" />&nbsp;<s:text
							name="%{#request.anagraficacliente.cognome}" /></strong></td>
			</tr>
			<tr>
				<td colspan="2"><input type="button" name="eliminaOrdine"
					id="eliminaOrdine"
					value="<s:text name='dpe.ordine.elimina.intero.ordine'/>"
					align="center" class="tableFields"
					style="text-align: center; width: 170px"
					/>
				</div></td>
				<td></td>
				<td></td>
			</tr>
		</table>


		<dpe:table tableId="LibriScolasticiOrdineTab" 
		    items="dettaglioordine"
			var="dettaglioordine"
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


			<dpe:row style="height:30px; cursor:pointer">
					<ec:column property="barcode" width="10%" title="dpe.dettaglio.libro.barcode" filterable="false" />
					<ec:column property="titolo" width="25%" title="dpe.dettaglio.libro.titolo" filterable="false" />
					<ec:column property="autore" width="15%" title="dpe.dettaglio.libro.autori" filterable="false" />
					<ec:column property="editore" width="15%" title="dpe.dettaglio.libro.editore" filterable="false" />
					<ec:column property="prezzoCopertina" width="10%" title="dpe.dettaglio.libro.prezzo" filterable="false" cell="currency" format="###,###,##0.00" />
					<ec:column property="prezzoCopertinaCliente" width="10%" title="dpe.dettaglio.copertina.prezzo" filterable="false" cell="currency" format="###,###,##0.00" />
					<ec:column property="seqordine" cell="viewOperazioniCarrello" title="igeriv.op.grid" filterable="false" sortable="false" width="15%" style="text-align:right;" viewsDenied="pdf,xls">
						<!-- Tasti funzione -->
					</ec:column>
					
			</dpe:row>
		</dpe:table>

		<table id="infoFooterPopUpDettaglioOrdine" width="810px" cellspacing="3">
			<tr>
				<td colspan="2">
					<input type="button" value="<s:text name='dpe.ordine.conferma.intero.ordine'/>"
					name="confermaOrdine" id="confermaOrdine" class="tableFields"
					style="width: 170px; height: 35px; text-align: center" /></td>
				<td style="font-size: 12px; "><s:text	name="dpe.ordine.totale.prezzo.libri" /> <s:text	name="%{#request.totalePrezzoLibri}" /></td>
				<td style="font-size: 12px; "><s:text	name="dpe.ordine.totale.prezzo.copertina" /> <s:text	name="%{#request.totalePrezzoCopertine}" /></td>
				<td style="font-size: 12px; text-align: right;"><s:text	name="dpe.ordine.totale.ordine" /></td>
				<td style="font-size: 12px;"><strong><s:text name="%{#request.totaleDovuto}" /></strong></td>
			</tr>
		</table>
	</div>
	<s:hidden name="idNumeroOrdine" id="idNumeroOrdine"
		value="%{#request.numeroOrdine}" />

</s:form>
