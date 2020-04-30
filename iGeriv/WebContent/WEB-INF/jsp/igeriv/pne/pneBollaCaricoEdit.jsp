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
<s:form id="BollaCaricoEditForm"
	action="pneBollaCarico_editBolla.action" namespace="/" method="POST"
	validate="false" onsubmit="return (ray.ajax())">
	<div id="mainDiv" style="width: 100%">
		<fieldset class="filterBolla" style="text-align: left; width: 100%">
			<legend style="font-size: 100%">
				<s:text name="tableTitle" />
			</legend>
			<div class="tableFields">
				<table cellspacing="3">
					<tr>
						<td><s:text name="igeriv.fornitore" /></td>
						<td>
							<table>
								<tr>
									<td><s:select name="bolla.codiceFornitore"
											id="fornitoriEdit" listKey="codFornitore" listValue="nome"
											list="fornitori" cssStyle="width:220px" /></td>
									<td><img src="/app_img/supplier.png" id="addFornitore"
										alt="<s:text name="igeriv.aggiungi.fornitore"/>" border="0"
										title="<s:text name="igeriv.aggiungi.fornitore"/>"
										style="cursor: pointer" onclick="javascript: addForn();" /></td>
								</tr>
							</table>
						</td>
						<td><s:text name="igeriv.data.documento" /></td>
						<td><s:textfield label="" name="bolla.dataDocumento"
								id="dataDocumentoEdit" cssStyle="width:220px">
								<s:param name="value">
									<s:date name="bolla.dataDocumento" format="dd/MM/yyyy" />
								</s:param>
							</s:textfield></td>
					</tr>
					<tr>
						<td><s:text name="igeriv.numero.documento" /></td>
						<td><s:textfield label="" name="bolla.numeroDocumento"
								id="numeroDocumentoEdit" cssStyle="width:220px" /></td>
						<td><s:text name="igeriv.causale" /></td>
						<td><s:select name="codiceCausale" id="causaliEdit"
								listKey="codiceCausale" listValue="descrizione"
								list="%{#application['listCausali']}" cssStyle="width:220px" />
						</td>
					</tr>
				</table>
			</div>
		</fieldset>
		<div class="tableFields">
			<dpe:table tableId="BollaCaricoEditTab" items="dettagli"
				var="dettagli"
				action="${pageContext.request.contextPath}${ap}/pneBollaCarico_editBolla.action"
				imagePath="/app_img/table/*.gif" rowsDisplayed="1000"
				view="buttonsOnBottom"
				extraToolButton='&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="/app_img/insert.gif" id="addRow" alt="${requestScope.aggiungiRiga}" border="0" title="${requestScope.aggiungiRiga}" style="cursor:pointer" onclick="javascript: addTableRow();"/>'
				extraToolButtonStyle="width:50%; text-align:right;"
				extraToolButton1='&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="/app_img/remove.jpg" id="deleteRow" alt="${requestScope.rimuoviRiga}" border="0" title="${requestScope.rimuoviRiga}" style="cursor:pointer" onclick="javascript: removeTableRow();"/>'
				extraToolButton1Style="width:50%; text-align:left;"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.ProdottiNonEditorialiCaricoBollaAction"
				styleClass="extremeTableFields" style="font-size:14px"
				form="BollaCaricoEditForm"
				theme="eXtremeTable bollaScrollDivContentMediumTall"
				showPagination="false" id="bollaCaricoDiv"
				toolbarClass="eXtremeTable" footerStyle="height:30px;"
				filterable="false">
				<dpe:exportPdf fileName="bolla_carico_prodotti.pdf"
					tooltip="plg.esporta.pdf"
					headerTitle="<fo:block text-align='center'>${titolo}</fo:block><fo:block></fo:block><fo:block text-align-last='justify' font-size='11pt'>${codEdicola}<fo:leader leader-pattern='space'/>${codFornitore}</fo:block><fo:block text-align-last='justify' font-size='11pt'>${ragSocEdicola}<fo:leader leader-pattern='space'/>${ragSocFornitore}</fo:block><fo:block text-align-last='justify' font-size='11pt'>${indirizzoEdicola}<fo:leader leader-pattern='space'/>${indirizzoFornitore}</fo:block><fo:block text-align-last='justify' font-size='11pt'>${pivaEdicola}<fo:leader leader-pattern='space'/>${pivaFornitore}</fo:block>"
					headerColor="black" headerBackgroundColor="#b6c2da"
					regionBeforeExtentInches="1.5" marginTopInches="1.3"
					repeatColumnHeaders="false" isLandscape="false" fontSize="12px"
					headFontSize="12px" totalsFontSize="12px" />
				<ec:exportXls fileName="bolla_carico_prodotti.xls"
					tooltip="plg.esporta.excel" />
				<dpe:row interceptor="marker">
					<dpe:column property="codiceProdottoFornitore" width="8%"
						title="igeriv.codice.prodotto.fornitore" filterable="false"
						sortable="false" style="font-size: 100%; text-align: left;"
						size="6" maxlength="10" headerStyle="text-align:center"
						idField="codiceProdottoFornitore"
						sessionVarName="dettagli[index].codiceProdottoFornitore"
						hasHiddenPkField="true" cell="textDifferenza"
						pkName="pk.progressivo"
						hiddenPkName="dettagli[index].pk.progressivo" hiddenPkId="prog"
						exportStyle="text-align:center" />
					<dpe:column property="fake" width="25%"
						title="igeriv.ricerca.prodotto.desc.barcode" filterable="false"
						sortable="false"
						style="font-size: 100%; text-align: left; white-space: nowrap;"
						size="20" maxlength="20" headerStyle="text-align:center"
						idField="barcode"
						sessionVarName="dettagli[index].prodotto.barcode"
						hasHiddenPkField="true"
						hiddenPkName="dettagli[index].prodotto.codProdottoInterno"
						hiddenPkFieldName="prodotto.codProdottoInterno"
						hiddenPkId="prodInterno" cell="textDifferenza"
						pkName="pk.progressivo" exportStyle="text-align:center"
						viewsDenied="pdf,xls" />
					<dpe:column property="prodotto.descrizioneProdottoHtml" width="32%"
						title="igeriv.descrizione" filterable="false" sortable="false" />
					<dpe:column property="quantita" width="9%" title="igeriv.quantita"
						filterable="false" sortable="false"
						style="font-size: 100%; text-align: left;" size="3" maxlength="10"
						headerStyle="text-align:center" idField="quant"
						sessionVarName="dettagli[index].quantita" validateIsNumeric="true"
						hasHiddenPkField="false" cell="textDifferenza"
						pkName="pk.progressivo" exportStyle="text-align:right" />
					<dpe:column property="prezzo" width="9%" title="igeriv.prezzo"
						hasCurrencySign="true" numberFormat="###,##0.00"
						filterable="false" sortable="false"
						style="font-size: 100%; text-align: right;" size="8"
						maxlength="10" headerStyle="text-align:right" idField="prezzo"
						sessionVarName="dettagli[index].prezzo" validateIsNumeric="true"
						hasHiddenPkField="false" cell="textDifferenza"
						pkName="pk.progressivo" exportStyle="text-align:right" />
					<dpe:column property="importo" width="9%" title="igeriv.importo"
						calc="total" calcTitle="column.calc.total" hasCurrencySign="true"
						totalFormat="###,##0.00" totalCellStyle="text-align:right"
						filterable="false" sortable="false"
						style="font-size: 100%; text-align: right;" size="8"
						numberFormat="###,##0.00" maxlength="10"
						headerStyle="text-align:right" idField="importo"
						sessionVarName="dettagli[index].importo" validateIsNumeric="true"
						hasHiddenPkField="false" cell="textDifferenza"
						pkName="pk.progressivo" exportStyle="text-align:right" />
					<dpe:column property="prodotto.aliquota" alias="aliquota"
						cell="dpeCombo" width="9%" title="dpe.iva" filterable="false"
						sortable="false"
						sessionVarName="dettagli[index].prodotto.aliquota"
						optionKeys="${keys}" optionValues="${values}"
						pkName="pk.progressivo" fieldName="prodotto.aliquota"
						hasHiddenPkField="false"
						style="font-size:12px; width:60px; text-align:center"
						exportStyle="text-align:center" />
				</dpe:row>
			</dpe:table>
		</div>
		<div>
			<s:if test="disabled eq false">
				<s:if test='isNew != true'>
					<div
						style="text-align: center; width: 280px; margin-left: auto; margin-right: auto;">
						<div style="float: left; text-align: center; width: 100px;">
							<input type="button" name="igeriv.memorizza" id="meBollaCarico"
								value="<s:text name='igeriv.memorizza'/>" align="center"
								class="tableFields" style="text-align: center; width: 100px"
								onclick="javascript: setTimeout(function(){return (validateFieldsBollaCarico(true) && setFormAction('BollaCaricoEditForm','pneBollaCarico_saveBolla.action', '', 'messageDiv', false, '', '', '', true, function(){setTableFields();}));},10);" />
						</div>
						<div style="float: left; text-align: center; width: 150px;">
							<input type="button" name="cancella" id="cancella"
								value="<s:text name='dpe.contact.form.elimina.bolla'/>"
								align="center" class="tableFields"
								style="text-align: center; width: 150px"
								onclick="javascript: doDelete();" />
						</div>
					</div>
				</s:if>
				<s:else>
					<div
						style="text-align: center; width: 100px; margin-left: auto; margin-right: auto;">
						<div style="float: left; text-align: center; width: 100px;">
							<input type="button" name="igeriv.memorizza" id="meBollaCarico"
								value="<s:text name='igeriv.memorizza'/>" align="center"
								class="tableFields" style="text-align: center; width: 100px"
								onclick="javascript: setTimeout(function() {return (validateFieldsBollaCarico(true) && setFormAction('BollaCaricoEditForm','pneBollaCarico_saveBolla.action', '', 'messageDiv'));},10);" />
						</div>
					</div>
				</s:else>
			</s:if>
		</div>
	</div>
	<input type="hidden" name="bolla.edicola.codEdicola"
		value="<s:text name="authUser.codEdicolaMaster"/>" />
	<s:hidden name="bolla.idDocumento" id="idDocumento" />
	<s:hidden name="isNew" />
</s:form>