<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="templateSuffix" value="'ftl'" />
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<s:set name="keys" value="%{comboIvaKeys}" />
<s:set name="values" value="%{comboIvaValues}" />
<s:form id="BollaResaEditForm" action="pneResa_editBolla.action"
	namespace="/" method="POST" validate="false"
	onsubmit="return (ray.ajax())">
	<div id="mainDiv" style="width: 100%">
		<fieldset class="filterBolla" style="text-align: left; width: 100%">
			<legend>
				<s:text name="tableTitle" />
			</legend>
			<div class="tableFields">
				<table cellspacing="3">
					<tr>
						<td><s:text name="igeriv.fornitore" /></td>
						<td>
							<table>
								<tr>
									<td><s:select name="resa.codiceFornitore"
											id="fornitoriEdit" listKey="codFornitore" listValue="nome"
											list="fornitori" cssStyle="width:220px" disabled="true" />
									</td>
								</tr>
							</table>
						</td>
						<td><s:text name="igeriv.data.documento" /></td>
						<td><s:textfield label="" name="resa.dataDocumento"
								id="dataDocumentoEdit" cssStyle="width:220px">
								<s:param name="value">
									<s:date name="resa.dataDocumento" format="dd/MM/yyyy" />
								</s:param>
							</s:textfield></td>
					</tr>
					<tr>
						<td><s:text name="igeriv.numero.documento" /></td>
						<td><s:textfield label="" name="resa.numeroDocumento"
								id="numeroDocumentoEdit" cssStyle="width:220px" disabled="true" />
						</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</table>
			</div>
		</fieldset>
		<div class="tableFields">
			<dpe:table tableId="BollaResaEditTab" items="dettagli" var="dettagli"
				action="${pageContext.request.contextPath}${ap}/pneResa_editBolla.action"
				imagePath="/app_img/table/*.gif" rowsDisplayed="1000"
				view="buttonsOnBottom"
				extraToolButton='&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="/app_img/insert.gif" id="addRow" alt="${requestScope.aggiungiRiga}" border="0" title="${requestScope.aggiungiRiga}" style="cursor:pointer" onclick="javascript: addTableRow();"/>'
				extraToolButtonStyle="width:50%; text-align:right;"
				extraToolButton1='&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="/app_img/remove.jpg" id="deleteRow" alt="${requestScope.rimuoviRiga}" border="0" title="${requestScope.rimuoviRiga}" style="cursor:pointer" onclick="javascript: removeTableRow();"/>'
				extraToolButton1Style="width:50%; text-align:left;"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.ProdottiNonEditorialiResaAction"
				styleClass="extremeTableFields" style="font-size:14px"
				form="BollaResaEditForm"
				theme="eXtremeTable bollaScrollDivContentMediumTall"
				showPagination="false" id="bollaResaDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false"
				autoIncludeParameters="false">
				<dpe:exportPdf fileName="bolla_resa_prodotti.pdf"
					tooltip="plg.esporta.pdf"
					headerTitle="<fo:block text-align='center'>${titolo}</fo:block><fo:block></fo:block><fo:block text-align-last='justify' font-size='11pt'>${codEdicola}<fo:leader leader-pattern='space'/>${codFornitore}</fo:block><fo:block text-align-last='justify' font-size='11pt'>${ragSocEdicola}<fo:leader leader-pattern='space'/>${ragSocFornitore}</fo:block><fo:block text-align-last='justify' font-size='11pt'>${indirizzoEdicola}<fo:leader leader-pattern='space'/>${indirizzoFornitore}</fo:block><fo:block text-align-last='justify' font-size='11pt'>${pivaEdicola}<fo:leader leader-pattern='space'/>${pivaFornitore}</fo:block>"
					headerColor="black" headerBackgroundColor="#b6c2da"
					regionBeforeExtentInches="1.5" marginTopInches="1.3"
					repeatColumnHeaders="false" isLandscape="false" fontSize="12px"
					headFontSize="12px" totalsFontSize="12px" />
				<ec:exportXls fileName="bolla_resa_prodotti.xls"
					tooltip="plg.esporta.excel" />
				<dpe:row interceptor="marker">
					<dpe:column property="codiceProdottoFornitore" width="8%"
						title="igeriv.codice.prodotto.fornitore.abbrev" filterable="false"
						sortable="false" style="text-align: left;" size="6" maxlength="10"
						headerStyle="text-align:center" idField="codiceProdottoFornitore"
						sessionVarName="dettagli[index].codiceProdottoFornitore"
						hasHiddenPkField="true" cell="textDifferenza"
						pkName="pk.progressivo"
						hiddenPkName="dettagli[index].pk.progressivo" hiddenPkId="prog"
						exportStyle="text-align:center" fieldEnabled="false" />
					<dpe:column property="fake" width="14%"
						title="igeriv.ricerca.prodotto.desc.barcode" filterable="false"
						sortable="false" style="text-align: left; white-space: nowrap;"
						size="23" maxlength="20" headerStyle="text-align:center"
						idField="barcode"
						sessionVarName="dettagli[index].prodotto.barcode"
						hasHiddenPkField="true"
						hiddenPkName="dettagli[index].prodotto.codProdottoInterno"
						hiddenPkFieldName="prodotto.codProdottoInterno"
						hiddenPkId="prodInterno" cell="textDifferenza"
						pkName="pk.progressivo" exportStyle="text-align:left"
						viewsDenied="pdf,xls" />
					<dpe:column property="prodotto.descrizioneProdottoHtml" width="35%"
						title="igeriv.descrizione" filterable="false" sortable="false"
						style="text-align:left" exportStyle="text-align:left;" />
					<dpe:column property="quantita" width="8%" title="igeriv.reso"
						filterable="false" sortable="false" style="text-align:left;"
						size="3" maxlength="10" headerStyle="text-align:left"
						idField="quant" sessionVarName="dettagli[index].quantita"
						validateIsNumeric="true" hasHiddenPkField="false"
						cell="textDifferenza" pkName="pk.progressivo"
						exportStyle="text-align:left" />
					<dpe:column property="prezzo" width="8%" title="igeriv.prezzo"
						numberFormat="###,##0.00" hasCurrencySign="true"
						filterable="false" sortable="false" style="text-align: right;"
						size="8" maxlength="10" headerStyle="text-align:right"
						idField="prezzo" sessionVarName="dettagli[index].prezzo"
						validateIsNumeric="true" hasHiddenPkField="false"
						cell="textDifferenza" pkName="pk.progressivo"
						exportStyle="text-align:right" fieldEnabled="false" />
					<dpe:column property="importo" width="8%" title="igeriv.importo"
						calc="total" calcTitle="column.calc.total" hasCurrencySign="true"
						totalFormat="###,##0.00" totalCellStyle="text-align:right"
						filterable="false" sortable="false" style="text-align: right;"
						size="8" numberFormat="###,##0.00" maxlength="10"
						headerStyle="text-align:right" idField="importo"
						sessionVarName="dettagli[index].importo" validateIsNumeric="true"
						hasHiddenPkField="false" cell="textDifferenza"
						pkName="pk.progressivo" exportStyle="text-align:right"
						fieldEnabled="false" />
					<dpe:column property="prodotto.aliquota" alias="aliquota"
						cell="dpeCombo" width="8%" title="dpe.iva" filterable="false"
						sortable="false"
						sessionVarName="dettagli[index].prodotto.aliquota"
						optionKeys="${keys}" optionValues="${values}"
						pkName="pk.progressivo" fieldName="prodotto.aliquota"
						hasHiddenPkField="false"
						style="font-size:12px; width:60px; text-align:center"
						exportStyle="text-align:center" fieldEnabled="false" />
					<dpe:column property="prodotto.giacenzaProdotto"
						alias="giacenzaProdotto" width="8%" title="igeriv.giacienza.ext"
						filterable="false" sortable="false" style="text-align:center;"
						size="2" headerStyle="text-align:center"
						idField="giacenzaProdotto"
						sessionVarName="dettagli[index].giacenza" hasHiddenPkField="false"
						cell="textDifferenza" pkName="pk.progressivo"
						exportStyle="text-align:center" fieldEnabled="false"
						viewsDenied="pdf,xls" />
				</dpe:row>
			</dpe:table>
		</div>
		<div>
			<div id="butDiv"
				style="text-align: center; width: 600px; height: 40px; margin-left: auto; margin-right: auto;">
				<s:if test="bollaInviata eq false">
					<div
						style="display: inline-block; text-align: center; width: 160px;">
						<input type="button" name="memorizza" id="meBollaResa"
							value="<s:text name='igeriv.memorizza'/>" align="center"
							class="buttonsBolleMemorizza"
							style="text-align: center; width: 100px"
							onclick="javascript: memorizzaBolla();" />
					</div>
					<s:if test="hasMemorizzaInvia eq true">
						<div
							style="display: inline-block; text-align: center; width: 160px; margin-left: 20px;">
							<input type="button" name="memorizzaInvia" id="meInvBollaResa"
								value="<s:text name='igeriv.memorizza.invia'/>" align="center"
								class="buttonsBolleMemorizzaInvia"
								style="text-align: center; width: 150px"
								onclick="javascript: memorizzaInviaBolla()" />
						</div>
					</s:if>
					<s:if test="isNew neq true">
						<div
							style="float: left; text-align: center; width: 160px; margin-left: 20px;">
							<input type="button" name="cancella" id="cancella"
								value="<s:text name='dpe.contact.form.elimina.bolla'/>"
								align="center" class="tableFields"
								style="font-weight: bold; text-align: center; width: 150px"
								onclick="javascript: doDelete();" />
						</div>
					</s:if>
				</s:if>
				<s:elseif test="hasMemorizzaInvia eq true">
					<div
						style="display: inline-block; text-align: center; width: 160px;">
						<a
							href="/bolle_resa_prodotti_vari/<s:text name="authUser.codFiegDl"/>/<s:text name="authUser.codEdicolaMaster"/>/<s:property value="resa.documentoResa.nomeFile"/>"
							id="printLink" target="_blank"><img id="imgPrint"
							src="/app_img/print_48x48.png"
							title="<s:text name="tooltip.main_frame.Print.Bolla.Resa"/>"
							alt="<s:text name="tooltip.main_frame.Print.Bolla.Resa"/>"
							border="0" /></a>
					</div>
				</s:elseif>
			</div>
		</div>
	</div>
	<input type="hidden" name="resa.edicola.codEdicola"
		value="<s:text name="authUser.codEdicolaMaster"/>" />
	<s:hidden name="resa.idDocumento" id="codResa" />
	<s:hidden name="resa.dataDocumento" id="dataDocumentoOld">
		<s:param name="value">
			<s:date name="resa.dataDocumento" format="dd/MM/yyyy" />
		</s:param>
	</s:hidden>
	<s:hidden name="isNew" id="isNew" />
	<s:hidden name="tipoDocumento" value="7" />
	<s:hidden name="downloadToken" id="downloadToken1" />
	<s:if test="isNew eq true">
		<s:hidden name="idDocumento" id="idDocumento" />
	</s:if>
</s:form>
<div id="printDiv"
	style="visibility: hidden; display: inline-block; text-align: center; width: 160px;">
	<a
		href="/bolle_resa_prodotti_vari/<s:text name="authUser.codFiegDl"/>/<s:text name="authUser.codEdicolaMaster"/>/<s:property value="resa.documento.nomeFile"/>"
		id="printLink" target="_blank"><img id="imgPrint"
		src="/app_img/print_48x48.png"
		title="<s:text name="tooltip.main_frame.Print.Bolla.Resa"/>"
		alt="<s:text name="tooltip.main_frame.Print.Bolla.Resa"/>" border="0" /></a>
</div>