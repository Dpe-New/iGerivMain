<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.richiesteRifornimento}">
	<s:form id="RichiestaRifornimentoForm"
		action="sonoInoltreUscite_showRichiesteRifornimentiEdit.action"
		method="POST" theme="simple" validate="false"
		onsubmit="return (ray.ajax())">
		<div id="mainDiv" style="width: 100%">
			<dpe:table tableId="RichiestaRifornimentoTab"
				items="richiesteRifornimento" var="richiesteRifornimento"
				action="sonoInoltreUscite_showRichiesteRifornimentiEdit.action"
				imagePath="/app_img/table/*.gif" title="${filterTitle}"
				rowsDisplayed="1000" view="buttonsOnBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
				styleClass="extremeTableFields"
				style="height:250px; table-layout:auto"
				form="RichiestaRifornimentoForm" theme="eXtremeTable bollaScrollDiv"
				showPagination="false" id="RichiestaRifornimentoScrollDiv"
				toolbarClass="eXtremeTable" footerStyle="height:30px;"
				filterable="false">
				<dpe:exportPdf fileName="richieste_rifornimento.pdf"
					tooltip="plg.esporta.pdf" headerTitle="${filterTitle}"
					headerColor="black" headerBackgroundColor="#b6c2da"
					isLandscape="true" />
				<ec:exportXls fileName="richieste_rifornimento.xls"
					tooltip="plg.esporta.excel" />
				<dpe:row style="height:30px;">
					<dpe:column property="pk.dataOrdine" width="10%"
						title="igeriv.data.ordine" cell="date" format="dd/MM/yyyy"
						styleClass="extremeTableFields" style="text-align:center"
						headerStyle="text-align:center" filterable="false" />
					<dpe:column property="titolo" width="15%" title="igeriv.titolo"
						styleClass="extremeTableFields" filterable="false"
						sortable="false" />
					<dpe:column property="sottoTitolo" width="15%"
						title="igeriv.sottotitolo" styleClass="extremeTableFields"
						filterable="false" sortable="false" />
					<dpe:column property="numeroCopertina" width="5%"
						title="igeriv.numero" styleClass="extremeTableFields"
						filterable="false" sortable="false" escapeAutoFormat="true" />
					<dpe:column property="prezzoCopertina" cell="currency" width="5%"
						title="igeriv.prezzo.lordo" styleClass="extremeTableFields"
						filterable="false" sortable="false" style="text-align:right" />
					<dpe:column property="dataUscita" width="6%"
						title="igeriv.data.uscita" cell="date" format="dd/MM/yyyy"
						filterable="false" sortable="false" />
					<dpe:column property="giorniValiditaRichiesteRifornimento"
						width="6%" maxlength="3" size="3"
						title="igeriv.giorni.scadenza.richiesta"
						cell="textGiorniValiditaRifornimento"
						sessionVarName="dataScadenzaRichiesta" pkName="pk"
						hasHiddenPkField="false" styleClass="extremeTableFields"
						filterable="false" sortable="false" style="text-align:left"
						exportStyle="text-align:center" />
					<dpe:column property="quantitaRichiesta" validateIsNumeric="true"
						maxlength="3" size="4" width="11%"
						title="igeriv.richiesta.rifornimenti.quantita.richiesta"
						cell="richiestaRifornimentoTextViewCell"
						sessionVarName="quantitaRifornimentoMap" pkName="pk"
						hasHiddenPkField="true" enabledConditionFields="enabled"
						enabledConditionValues="true" styleClass="extremeTableFields"
						filterable="false" sortable="false" style="text-align:left"
						exportStyle="text-align:center" />
					<dpe:column property="noteVendita" maxlength="250" size="20"
						width="20%" title="igeriv.richiesta.rifornimenti.quantita.note"
						cell="textNoteRifornimento" sessionVarName="noteVenditaMap"
						pkName="pk" hasHiddenPkField="false"
						enabledConditionFields="enabled" enabledConditionValues="true"
						styleClass="extremeTableFields" filterable="false"
						sortable="false" />
					<dpe:column property="fornito" width="5%" title="igeriv.fornito"
						styleClass="extremeTableFields" filterable="false"
						sortable="false" style="text-align:center" />
				</dpe:row>
			</dpe:table>
			<div style="width: 100%;">
				<div style="float: left; text-align: center; margin-left: 400px;">
					<input type="button" value="<s:text name='igeriv.memorizza'/>"
						name="igeriv.memorizza" id="memorizza" class="tableFields"
						style="width: 100px; text-align: center"
						onclick="javascript: setTimeout(function() {return (validateFields('RichiestaRifornimentoForm') && setFormAction('RichiestaRifornimentoForm','sonoInoltreUscite_saveRichiestaRifornimentoModifica.action', '', 'messageDiv'));},10);" />
				</div>
			</div>
		</div>
	</s:form>
	<div id="one"></div>
</s:if>