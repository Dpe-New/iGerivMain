<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
div#filter {
	height: 140px;
}

.tableHeadClass {
	text-align: center;
	background: #6a6272;
	color: #eae2f2;
	margin: 0 auto;
	font-size: 24px;
}

.tableHeadClass.stuck {
	position: fixed;
	top: 0;
	box-shadow: 0 2px 4px rgba(0, 0, 0, .3);
}
</style>
<s:url id="smdUrl" namespace="/" action="VenditeCardRpc.action" />
<s:if
	test="%{#request.itensBolla != null && !#request.itensBolla.isEmpty()}">
	<s:form id="BollaControlloForm" action="bollaRivendita_save.action"
		method="POST" theme="simple" validate="false"
		onsubmit="return (ray.ajax())">
		<s:iterator value="itensBolla" status="status">
			<input type="hidden" id="<s:property value='barcode'/>"	value="<s:property value='pk'/>" />
			<input type="hidden" name="pkNoteRivendita"	id="pkNoteRivendita<s:property value='idtn'/>" value="<s:property value='idtn'/>" />
			<input type="hidden" name="noteRivendita"	id="noteRivendita<s:property value='idtn'/>"   value="<s:property value='note'/>" />
			<input type="hidden" name="noteRivenditaCpu"	id="noteRivenditaCpu<s:property value='codicePubblicazione'/>"	value="<s:property value='noteByCpu'/>" />
		</s:iterator>
		
		<!-- ESPORTAZIONE IN TIFF -->
		<s:if test="%{#session['isEnabledExportPDFToTIFF'] eq true}">
			<s:hidden name="type_return" id="type_return" value="TIFF"/>
		</s:if>
		
		<s:hidden name="dataTipoBolla" id="dtb" />
		<s:hidden name="soloRigheSpuntare" />
		<s:set name="title" value="%{getText('igeriv.bolla.consegna')}" />
		<s:set name="titleFileName" value="%{getText('igeriv.bolla.consegna.file.name')}" />

		<div>
			<div style="text-align: center">
				<fieldset class="filterBolla">
					<s:if test="authUser.hasEdicoleAutorizzateAggiornaBarcode eq true">
						<div
							style="margin-left: 0px; float: left; text-align: left; margin-top: 12px;">
							<div
								style="margin-left: 0px; float: left; text-align: left; white-space: nowrap">
								<label for="barcode" style="width: 120px"
									class="extremeTableFieldsLarger"><s:text
										name="igeriv.codice.barre"></s:text></label>
								<s:if test="#request.modalitaInforiv eq true">
									<s:textfield id="barcode" name="barcode_copieLette"
										cssClass="extremeTableFieldsLarger" cssStyle="width:160px;" />
								</s:if>
								<s:else>
									<s:textfield id="barcode" name="barcode_differenze"
										cssClass="extremeTableFieldsLarger" cssStyle="width:160px;" />
								</s:else>
							</div>
							<div
								style="margin-left: 0px; float: left; text-align: left; white-space: nowrap">
								<label for="aggiornaBarcode" style="width: 120px"
									class="extremeTableFieldsLarger"><s:text
										name="dpe.aggiorna.barcode" /></label>
								<s:checkbox name="aggiornaBarcode" id="aggiornaBarcode"
									cssStyle="text-align:right;" />
							</div>
						</div>
					</s:if>
					<s:else>
						<div
							style="margin-left: 100px; width: 300px; float: left; text-align: left; margin-top: 12px;">
							<label for="barcode" style="width: 100px"
								class="extremeTableFieldsLarger"><s:text
									name="igeriv.codice.barre"></s:text></label>
							<s:if test="#request.modalitaInforiv eq true">
								<s:textfield id="barcode" name="barcode_copieLette"
									cssClass="extremeTableFieldsLarger" cssStyle="width:150px;" />
							</s:if>
							<s:else>
								<s:textfield id="barcode" name="barcode_differenze"
									cssClass="extremeTableFieldsLarger" cssStyle="width:150px;" />
							</s:else>
						</div>
					</s:else>
				</fieldset>
			</div>

			<s:if
				test="%{#request.messaggiBolla != null && !#request.messaggiBolla.isEmpty() && authUser.edicolaDeviettiTodis neq true}">
				<div style="width: 100%; text-align: center">
					<input type="button" id="toggleMsgs"
						value="<s:text name="igeriv.nascondi.msg.bolla"/>"
						style="width: 150px" />
					<s:if test="msgBollaLetti eq false">
						<span id="msgBollaLettiSpan">&nbsp;&nbsp;<s:checkbox
								name="msgBollaLetti" id="msgBollaLetti" value="false" />&nbsp;<s:text
								name="igeriv.msg.bolla.gia.letti" /></span>
					</s:if>
				</div>
				<div id="docDisponibile"
					style="width: 100%; text-align: center; font-weight: bold;"></div>
				<div id="msgDiv">
					<dpe:table tableId="id2" items="messaggiBolla"
						action="bollaRivendita_showBolla.action" view="buttonsOnBottom"
						imagePath="/app_img/table/*.gif"
						style="height:150px; font-size:11px; float: left;"
						rowsDisplayed="1000" styleClass="eXtremeTableFixedLengthFont"
						theme="eXtremeTableFixedLengthFont bollaScrollDivSmallH"
						showPagination="false" showExports="true" showStatusBar="false"
						showTitle="false" showTooltips="true" filterable="false"
						autoIncludeParameters="false" id="BollaMessaggiDiv1"
						form="BollaControlloForm"
						toolbarClass=".eXtremeTableFixedLengthFont .toolbarMsg">
						
						<!-- add 14/02/2017 - Gestione dell'esportazione in XLS , PDF e TIFF (Gestione dei dati sensibili) -->
						<s:if test="%{#session['isEnabledExportPDF'] eq true}">
							<dpe:exportPdf
								fileName="Messaggi_${titleFileName}_${strData}_${tipo}.pdf"
								tooltip="plg.esporta.pdf"
								headerTitle="<fo:block text-align='center'>${title} Data ${strData} Tipo ${tipo}</fo:block><br><fo:block text-align='center'>${pdfTitleDocumentoDisponibile}</fo:block><br><fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block><fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block><fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block><fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block><fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>"
								headerColor="black" headerBackgroundColor="#b6c2da"
								isLandscape="false" regionBeforeExtentInches="1.7"
								marginTopInches="1.6" repeatColumnHeaders="true"
								logoImage="/app_img/rodis.gif" />
						</s:if>
						<!-- ESPORTAZIONE IN TIFF -->
						<s:if test="%{#session['isEnabledExportPDFToTIFF'] eq true}">
							<dpe:exportPdf
								fileName="Messaggi_${titleFileName}_${strData}_${tipo}"
								tooltip="plg.esporta.tiff"
								headerTitle="<fo:block text-align='center'>${title} Data ${strData} Tipo ${tipo}</fo:block><br><fo:block text-align='center'>${pdfTitleDocumentoDisponibile}</fo:block><br><fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block><fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block><fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block><fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block><fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>"
								headerColor="black" headerBackgroundColor="#b6c2da"
								isLandscape="false" regionBeforeExtentInches="1.7"
								marginTopInches="1.6" repeatColumnHeaders="true"
								logoImage="/app_img/rodis.gif" 
								imageName="tiff"/>
						</s:if>
						<dpe:row highlightRow="true" interceptor="marker"
							href="#?idtn=idtn&periodicita=periodicitaPk&coddl=pk.codFiegDl"
							style="cursor:pointer; height:35px">
							<dpe:column property="messaggio" title="igeriv.messaggi"
								width="100%" filterable="false" sortable="false"
								preserveBlankSpaces="true" style="white-space: nowrap;" />
						</dpe:row>




					</dpe:table>
				</div>
			</s:if>

			<div
				style="background-color: #F4F4F4; width: 90; height: 30px; font-weight: bold">
				<div
					style="width: 5%; float: left; text-align: left; font-weight: bold"
					class="extremeTableFieldsLarger">
					<s:text name="column.calc.totals"></s:text>
				</div>
				<div
					style="width: 35%; float: left; white-space: nowrap; text-align: center;"
					class="extremeTableFieldsLarger">
					<s:text name="igeriv.importo.netto"></s:text>
					:&nbsp;<span id="totalHeader_1" class="calcTitle"
						style="font-weight: bold"></span><span style="font-size: 80%">&nbsp;(<s:text
							name="column.calc.importo.bolla"></s:text>:&nbsp;<span
						id="totalHeader_3" class="calcTitle" style="font-weight: bold"></span>&nbsp;&nbsp;<s:text
							name="column.calc.importo.fondo.bolla"></s:text>:&nbsp;<span
						id="totalHeader_4" class="calcTitle" style="font-weight: bold"></span>)
					</span>
				</div>
				<div
					style="width: 35%; float: left; white-space: nowrap; text-align: center;"
					class="extremeTableFieldsLarger">
					<s:text name="igeriv.importo.lordo"></s:text>
					:&nbsp;<span id="totalHeader_2" class="calcTitle"
						style="font-weight: bold"></span><span style="font-size: 80%">&nbsp;(<s:text
							name="column.calc.importo.bolla"></s:text>:&nbsp;<span
						id="totalHeader_5" class="calcTitle" style="font-weight: bold"></span>&nbsp;&nbsp;<s:text
							name="column.calc.importo.fondo.bolla"></s:text>:&nbsp;<span
						id="totalHeader_6" class="calcTitle" style="font-weight: bold"></span>)
					</span>
				</div>
				<div
					style="width: 10%; float: left; white-space: nowrap; text-align: center;"
					class="extremeTableFieldsLarger">
					<s:text name="igeriv.totale.mancanze"></s:text>
					:&nbsp;<span id="totalHeader_7" class="calcTitle"
						style="font-weight: bold"></span>
				</div>
				<div
					style="width: 10%; float: left; white-space: nowrap; text-align: center;"
					class="extremeTableFieldsLarger">
					<s:text name="igeriv.totale.eccedenze"></s:text>
					:&nbsp;<span id="totalHeader_8" class="calcTitle"
						style="font-weight: bold"></span>
				</div>
			</div>

			<s:if test="#request.modalitaInforiv eq true">
				<dpe:table tableId="BollaControlloTab" items="itensBolla"
					var="itensBolla" action="bollaRivendita_showBolla.action"
					imagePath="/app_img/table/*.gif"
					style="table-layout:fixed; font-size:12px;"
					view="buttonsOnTopAndBottom"
					extraToolButton1='<input type="button" value="${requestScope.memorizza}" name="igeriv.memorizza" id="memorizza" title="${requestScope.memorizzaCtrlM}" class="buttonsBolleMemorizza" onclick="javascript: saveBolla();"/>'
					extraToolButton1Style="width:35%; text-align:center; padding-right:10px; font-size:12px;"
					extraToolButton2='<input type="button" value="${requestScope.memorizzaInvia}" name="igeriv.memorizza.invia" id="memorizzaInvia" title="${requestScope.memorizzaInviaCtrlI}" class="buttonsBolleMemorizzaInvia" onclick="javascript: saveAndSendBolla();"/>'
					extraToolButton2Style="width:35%; text-align:center; padding-right:10px; font-size:12px;"
					extraToolButton3='<img src="/app_img/clients_detail.gif" width="28px" height="28px" id="imgClientsDetail" alt="${requestScope.esportaOrdiniDettaglio}" border="0" title="${requestScope.esportaOrdiniDettaglio}" style="cursor:pointer" onclick="javascript: esportaOrdini(2)"/>'
					extraToolButton3Style="width:2%; text-align:right; padding-right:15px"
					extraToolButton4='<img src="/app_img/clients.gif" width="28px" height="28px" id="imgClients" alt="${requestScope.esportaOrdini}" border="0" title="${requestScope.esportaOrdini}" style="cursor:pointer" onclick="javascript: esportaOrdini(1)"/>'
					extraToolButton4Style="width:2%; text-align:right; padding-right:15px"
					extraToolButton5='<span id="prenotazioniFisseNonEvaseDiv"></span>'
					extraToolButton5Style="width:2%; text-align:left; padding-right:15px"
					state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
					styleClass="extremeTableFieldsSmaller" form="BollaControlloForm"
					theme="eXtremeTable" showPagination="false" id="BollaScrollDiv"
					locale="${localeString}" toolbarClass="eXtremeTable"
					footerStyle="height:30px;" filterable="false"
					autoIncludeParameters="false"
					beforeUnloadValidationScript="if (!validateFields('BollaControlloForm')) {setTimeout(function() {unBlockUI();}, 100); return false;};">

					<!-- add 14/02/2017 - Gestione dell'esportazione in XLS e PDF (Gestione dei dati sensibili) -->
					<s:if test="%{#session['isEnabledExportPDF'] eq true}">
						<dpe:exportPdf fileName="${titleFileName}_${strData}_${tipo}.pdf"
							tooltip="plg.esporta.pdf"
							headerTitle="<fo:block text-align='center'>${title} Data ${strData} Tipo ${tipo}</fo:block><br><fo:block text-align='center'>${pdfTitleDocumentoDisponibile}</fo:block><br><fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block><fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block><fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block><fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block><fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>"
							headerColor="black" headerBackgroundColor="#b6c2da"
							isLandscape="false" regionBeforeExtentInches="1.7"
							marginTopInches="1.6" repeatColumnHeaders="true"
							logoImage="/app_img/rodis.gif" />
					</s:if>
					<!-- ESPORTAZIONE IN TIFF -->
					<s:if test="%{#session['isEnabledExportPDFToTIFF'] eq true}">
						<dpe:exportPdf fileName="${titleFileName}_${strData}_${tipo}"
							tooltip="plg.esporta.tiff"
							headerTitle="<fo:block text-align='center'>${title} Data ${strData} Tipo ${tipo}</fo:block><br>
							<fo:block text-align='center'>${pdfTitleDocumentoDisponibile}</fo:block><br>
							<fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block>
							<fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block>
							<fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block>
							<fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block>
							<fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>"
							headerColor="black" headerBackgroundColor="#b6c2da"
							isLandscape="false" regionBeforeExtentInches="1.7"
							marginTopInches="1.6" repeatColumnHeaders="true"
							logoImage="/app_img/rodis.gif" 
							imageName="tiff"/>
					</s:if>
					
					
					
					<s:if test="%{#session['isEnabledExportXLS'] eq true}">
					<ec:exportXls fileName="${titleFileName}_${strData}_${tipo}.xls"
						tooltip="plg.esporta.excel" />
					</s:if>	
						
						
					<dpe:row highlightRow="true" interceptor="marker"
						href="#?idtn=idtn&periodicita=periodicitaPk&coddl=pk.codFiegDl"
						style="cursor:pointer; height:35px">
						<ec:column property="rownum" width="2%" title="N."
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" />
						
						<!-- add 14/02/2017 - Gestione dell'esportazione in XLS e PDF (Gestione dei dati sensibili) -->
						<s:if test="%{#session['isEnabledPKInExportXLS'] eq true && #session['isEnabledPKInExportPDF'] eq true}">
								<ec:column property="codicePubblicazione" width="5%"
										title="igeriv.cpu" filterable="false" style="text-align:center"
										headerStyle="text-align:center" />
						</s:if>
						<s:elseif test="%{#session['isEnabledPKInExportXLS'] eq true && #session['isEnabledPKInExportPDF'] eq false}">
								<ec:column property="codicePubblicazione" width="5%"
										title="igeriv.cpu" filterable="false" style="text-align:center" viewsDenied="pdf" 
										headerStyle="text-align:center" />
						</s:elseif>
						<s:elseif test="%{#session['isEnabledPKInExportXLS'] eq false && #session['isEnabledPKInExportPDF'] eq true}">
								<ec:column property="codicePubblicazione" width="5%"
										title="igeriv.cpu" filterable="false" style="text-align:center" viewsDenied="xls" 
										headerStyle="text-align:center" />
						</s:elseif>
						<s:else>
							<ec:column property="codicePubblicazione" width="5%"
										title="igeriv.cpu" filterable="false" style="text-align:center" viewsDenied="pdf,xls" 
										headerStyle="text-align:center" />
						</s:else>
						
						
						
						<ec:column property="titolo" width="18%"
							title="igeriv.titolo.sottotitolo" filterable="false"
							cell="titoloBollaCell" />
						<dpe:column property="numeroPubblicazione" width="5%"
							title="igeriv.numero" filterable="false" escapeAutoFormat="true"
							style="text-align:center" headerStyle="text-align:center" />
						<dpe:column property="prezzoLordo" cell="currency"
							hasCurrencySign="true" format="###,###,##0.00"
							style="text-align:right" headerStyle="text-align:right"
							width="6%" title="igeriv.prezzo.lordo" filterable="false" />
						<dpe:column property="sconto" cell="currency"
							format="###,###,##0.00" style="text-align:right"
							headerStyle="text-align:right" width="5%" title="igeriv.sconto"
							filterable="false" allowZeros="true" viewsDenied="pdf,xls"/>
						<dpe:column property="percentualeIva" cell="currency"
							format="###,###,##0.00" style="text-align:right" width="5%"
							title="igeriv.defiscalizzazione" filterable="false"
							headerStyle="text-align:right" viewsDenied="pdf,xls"/>
						<dpe:column property="prezzoNetto" cell="currency"
							hasCurrencySign="true" format="###,###,##0.0000"
							style="text-align:right" headerStyle="text-align:right"
							width="6%" title="igeriv.prezzo.netto" filterable="false" />
						<dpe:column property="quantitaConsegnata" cell="copieBollaCell"
							isLink="true" href="#?idtn=idtn&periodicita=periodicitaPk"
							styleClass="poplight" rel="popup_name" width="4%"
							title="igeriv.copie" filterable="false"
							style="text-align:center; font-weight:bold; font-size:120%"
							headerStyle="text-align:center" allowZeros="true" />
						<dpe:column property="importo" calc="total" hasCurrencySign="true"
							calcTitle="column.calc.total" cell="currency"
							format="###,###,##0.0000" totalFormat="###,###,##0.00"
							style="text-align:right" headerStyle="text-align:right"
							width="7%" title="igeriv.importo" filterable="false"
							totalCellStyle="text-align:right" allowZeros="true" />
						<dpe:column property="spunta" width="4%" title="igeriv.spunta"
							filterable="false" sortable="false" cell="selectedSpunta"
							headerCell="buttonSpuntaAutomatica" pkName="pk"
							hasHiddenPkField="false" style="text-align:center" />
						<dpe:column property="differenze" width="10%"
							title="igeriv.copie.lette.diff" size="2" maxlength="18"
							filterable="false" cell="bollaRivenditaCell" pkName="pk"
							styleClass="extremeTableFieldsSmaller" sortable="false"
							exportStyle="text-align:center"
							style="font-size:120%; font-weight:bold; text-align:left; display:inline; white-space:nowrap; height:25px;"
							headerStyle="text-align:center" modalitaInforiv="true"
							allowZeros="true" />
						<ec:column property="tipoFondoBolla" width="10%"
							title="igeriv.fondo.bolla" filterable="false" />
						<dpe:column property="ordini" isLink="true" href="#?idtn=idtn"
							styleClass="poplight" rel="popup_name" width="5%"
							title="igeriv.ordini" filterable="false"
							style="text-align:center; font-size:110%; font-weight:bold;"
							headerStyle="text-align:center;" viewsDenied="pdf,xls"/>
							
						
						<!-- add 14/02/2017 - Gestione dell'esportazione in XLS e PDF (Gestione dei dati sensibili) -->
<%-- 						<s:if test="%{#session['isEnabledPKInExportXLS'] eq true}"> --%>
<%-- 							<dpe:column property="barcode" width="5%" --%>
<%-- 								title="igeriv.barcode" filterable="false" escapeAutoFormat="true" --%>
<%-- 								style="text-align:center" headerStyle="text-align:center" viewsAllowed="xls" /> --%>
<%-- 						</s:if> --%>
<%-- 						<s:else> --%>
<%-- 							<dpe:column property="barcode" width="5%" --%>
<%-- 								title="igeriv.barcode" filterable="false" escapeAutoFormat="true" --%>
<%-- 								style="text-align:center" headerStyle="text-align:center" viewsDenied="pdf,xls" /> --%>
<%-- 						</s:else>	 --%>
							
							
							
							
						<ec:column property="immagine" title="igeriv.img"
							filterable="false" viewsDenied="pdf,xls" sortable="false"
							width="5%" style="text-align:center;"
							headerStyle="text-align:center"
							cell="viewImageCell" >
							
						
							
							
<%-- 							

<a href="/immagini/${pageScope.itensBolla.immagine}" --%>
<!-- 								rel="thumbnail"><img src="/app_img/camera.gif" width="15px" -->
<!-- 								height="15px" border="0" -->
<!-- 								style="border-style: none; text-align: center" /></a> -->
						</ec:column>
					</dpe:row>
				</dpe:table>
			</s:if>
			<s:else>
				<dpe:table tableId="BollaControlloTab" items="itensBolla"
					var="itensBolla" action="bollaRivendita_showBolla.action"
					imagePath="/app_img/table/*.gif"
					style="table-layout:fixed; font-size:12px;"
					view="buttonsOnTopAndBottom"
					extraToolButton1='<input type="button" value="${requestScope.memorizza}" name="igeriv.memorizza" id="memorizza" title="${requestScope.memorizzaCtrlM}" class="buttonsBolleMemorizza" onclick="javascript: saveBolla();"/>'
					extraToolButton1Style="width:35%; text-align:center; padding-right:10px; font-size:12px;"
					extraToolButton2='<input type="button" value="${requestScope.memorizzaInvia}" name="igeriv.memorizza.invia" id="memorizzaInvia" title="${requestScope.memorizzaInviaCtrlI}" class="buttonsBolleMemorizzaInvia" onclick="javascript: saveAndSendBolla();"/>'
					extraToolButton2Style="width:35%; text-align:center; padding-right:10px; font-size:12px;"
					extraToolButton3='<img src="/app_img/clients_detail.gif" width="28px" height="28px" id="imgClientsDetail" alt="${requestScope.esportaOrdiniDettaglio}" border="0" title="${requestScope.esportaOrdiniDettaglio}" style="cursor:pointer" onclick="javascript: esportaOrdini(2)"/>'
					extraToolButton3Style="width:2%; text-align:right; padding-right:15px"
					extraToolButton4='<img src="/app_img/clients.gif" width="28px" height="28px" id="imgClients" alt="${requestScope.esportaOrdini}" border="0" title="${requestScope.esportaOrdini}" style="cursor:pointer" onclick="javascript: esportaOrdini(1)"/>'
					extraToolButton4Style="width:2%; text-align:right; padding-right:15px"
					extraToolButton5='<span id="prenotazioniFisseNonEvaseDiv"></span>'
					extraToolButton5Style="width:2%; text-align:left; padding-right:15px"
					state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
					styleClass="extremeTableFieldsSmaller" form="BollaControlloForm"
					theme="eXtremeTable" showPagination="false" id="BollaScrollDiv"
					locale="${localeString}" toolbarClass="eXtremeTable"
					footerStyle="height:30px;" filterable="false"
					autoIncludeParameters="false"
					beforeUnloadValidationScript="if (!validateFields('BollaControlloForm')) {setTimeout(function() {unBlockUI();}, 100); return false;};">
					
					<!-- add 14/02/2017 - Gestione dell'esportazione in XLS e PDF (Gestione dei dati sensibili) -->
					<s:if test="%{#session['isEnabledExportPDF'] eq true}">
						<dpe:exportPdf fileName="${titleFileName}_${strData}_${tipo}.pdf"
							tooltip="plg.esporta.pdf"
							headerTitle="<fo:block text-align='center'>${title} Data ${strData} Tipo ${tipo}</fo:block><br>
							<fo:block text-align='center'>${pdfTitleDocumentoDisponibile}</fo:block><br>
							<fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block>
							<fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block>
							<fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block>
							<fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block>
							<fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>"
							headerColor="black" headerBackgroundColor="#b6c2da"
							isLandscape="false" regionBeforeExtentInches="1.7"
							marginTopInches="1.6" repeatColumnHeaders="true"
							logoImage="/app_img/rodis.gif" />
					</s:if>
					
					<!-- ESPORTAZIONE IN TIFF -->
					<s:if test="%{#session['isEnabledExportPDFToTIFF'] eq true}">
						<dpe:exportPdf fileName="${titleFileName}_${strData}_${tipo}"
							tooltip="plg.esporta.tiff"
							headerTitle="<fo:block text-align='center'>${title} Data ${strData} Tipo ${tipo}</fo:block><br>
							<fo:block text-align='center'>${pdfTitleDocumentoDisponibile}</fo:block><br>
							<fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block>
							<fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block>
							<fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block>
							<fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block>
							<fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>"
							headerColor="black" headerBackgroundColor="#b6c2da"
							isLandscape="false" regionBeforeExtentInches="1.7"
							marginTopInches="1.6" repeatColumnHeaders="true"
							logoImage="/app_img/rodis.gif" 
							imageName="tiff"/>
					</s:if>
					
					
					
					
					
					
					
					<s:if test="%{#session['isEnabledExportXLS'] eq true}">
						<ec:exportXls fileName="${titleFileName}_${strData}_${tipo}.xls" tooltip="plg.esporta.excel" />
					</s:if>
					
					<dpe:row highlightRow="true" interceptor="marker"
						href="#?idtn=idtn&periodicita=periodicitaPk&coddl=pk.codFiegDl"
						style="cursor:pointer; height:35px">
						<ec:column property="rownum" width="2%" title="N."
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" />
						
						
						<!-- add 14/02/2017 - Gestione dell'esportazione in XLS e PDF (Gestione dei dati sensibili) -->
						<s:if  test="%{#session['isEnabledPKInExportXLS'] eq true && #session['isEnabledPKInExportPDF'] eq true}">
								<ec:column property="codicePubblicazione" width="5%"
										title="igeriv.cpu" filterable="false" style="text-align:center"
										headerStyle="text-align:center" />
						</s:if>
						<s:elseif test="%{#session['isEnabledPKInExportXLS'] eq true && #session['isEnabledPKInExportPDF'] eq false}">
								<ec:column property="codicePubblicazione" width="5%"
										title="igeriv.cpu" filterable="false" style="text-align:center" viewsDenied="pdf" 
										headerStyle="text-align:center" />
						</s:elseif>
						<s:elseif test="%{#session['isEnabledPKInExportXLS'] eq false && #session['isEnabledPKInExportPDF'] eq true}">
								<ec:column property="codicePubblicazione" width="5%"
										title="igeriv.cpu" filterable="false" style="text-align:center" viewsDenied="xls" 
										headerStyle="text-align:center" />
						</s:elseif>
						<s:else>
							<ec:column property="codicePubblicazione" width="5%"
										title="igeriv.cpu" filterable="false" style="text-align:center" viewsDenied="pdf,xls" 
										headerStyle="text-align:center" />
						</s:else>
						
						
						
							
						<ec:column property="titolo" width="19%"
							title="igeriv.titolo.sottotitolo" filterable="false"
							cell="titoloBollaCell" />
						<dpe:column property="numeroPubblicazione" width="5%"
							title="igeriv.numero" filterable="false" escapeAutoFormat="true"
							style="text-align:center" headerStyle="text-align:center" />
						<dpe:column property="prezzoLordo" cell="currency"
							hasCurrencySign="true" format="###,###,##0.00"
							style="text-align:right" headerStyle="text-align:right"
							width="6%" title="igeriv.prezzo.lordo" filterable="false" />
						<dpe:column property="sconto" cell="currency"
							format="###,###,##0.00" style="text-align:right"
							headerStyle="text-align:right" width="5%" title="igeriv.sconto"
							filterable="false" allowZeros="true" viewsDenied="pdf,xls"/>
						<dpe:column property="percentualeIva" cell="currency"
							format="###,###,##0.00" style="text-align:right" width="5%"
							title="igeriv.defiscalizzazione" filterable="false"
							headerStyle="text-align:right" viewsDenied="pdf,xls"/>
						<dpe:column property="prezzoNetto" cell="currency"
							hasCurrencySign="true" format="###,###,##0.0000"
							style="text-align:right" headerStyle="text-align:right"
							width="6%" title="igeriv.prezzo.netto" filterable="false" />
						<dpe:column property="quantitaConsegnata" cell="copieBollaCell"
							isLink="true" href="#?idtn=idtn&periodicita=periodicitaPk"
							styleClass="poplight" rel="popup_name" width="4%"
							title="igeriv.copie" filterable="false"
							style="text-align:center; font-weight:bold; font-size:120%"
							headerStyle="text-align:center" allowZeros="true" />
						<dpe:column property="importo" calc="total" hasCurrencySign="true"
							calcTitle="column.calc.total" cell="currency"
							format="###,###,##0.0000" totalFormat="###,###,##0.00"
							style="text-align:right" headerStyle="text-align:right"
							width="7%" title="igeriv.importo" filterable="false"
							totalCellStyle="text-align:right" allowZeros="true" />
						<dpe:column property="spunta" width="5%" title="igeriv.spunta"
							filterable="false" sortable="false" cell="selectedSpunta"
							headerCell="buttonSpuntaAutomatica" pkName="pk"
							hasHiddenPkField="false" style="text-align:center" />
						<dpe:column property="differenze" width="5%"
							title="igeriv.differenza" size="2" maxlength="18"
							filterable="false" cell="bollaRivenditaCell" pkName="pk"
							styleClass="extremeTableFieldsSmaller" sortable="false"
							exportStyle="text-align:center"
							style="font-size:120%; font-weight:bold; text-align:left"
							headerStyle="text-align:center" />
						<ec:column property="tipoFondoBolla" width="13%"
							title="igeriv.fondo.bolla" filterable="false" />
						<dpe:column property="ordini" isLink="true" href="#?idtn=idtn"
							styleClass="poplight" rel="popup_name" width="5%"
							title="igeriv.ordini" filterable="false"
							style="text-align:center; font-size:110%; font-weight:bold;"
							headerStyle="text-align:center;"  viewsDenied="pdf,xls"/>
							
						<!-- add 14/02/2017 - Gestione dell'esportazione in XLS e PDF (Gestione dei dati sensibili) -->
<%-- 						<s:if test="%{#session['isEnabledPKInExportXLS'] eq true}"> --%>
							
<%-- 							<dpe:column property="barcode" width="5%"  --%>
<%-- 								title="igeriv.barcode" filterable="false" escapeAutoFormat="true" --%>
<%-- 								style="text-align:center" headerStyle="text-align:center" viewsAllowed="xls" /> --%>
<%-- 						</s:if> --%>
<%-- 						<s:else> --%>
<%-- 							<dpe:column property="barcode" width="5%" --%>
<%-- 								title="igeriv.barcode" filterable="false" escapeAutoFormat="true" --%>
<%-- 								style="text-align:center" headerStyle="text-align:center" viewsDenied="pdf,xls" /> --%>
<%-- 						</s:else> --%>
						
						
						
						<ec:column property="immagine" title="igeriv.img"
							filterable="false" viewsDenied="pdf,xls" sortable="false"
							width="5%" style="text-align:center;"
							headerStyle="text-align:center"
							cell="viewImageCell" >
<%-- 							<a href="/immagini/${pageScope.itensBolla.immagine}" --%>
<!-- 								rel="thumbnail"><img src="/app_img/camera.gif" width="15px" -->
<!-- 								height="15px" border="0" -->
<!-- 								style="border-style: none; text-align: center" /></a> -->
						</ec:column>
					</dpe:row>
				</dpe:table>
			</s:else>
			<s:hidden name="importoLordo" id="importoLordo" />
			<s:hidden name="downloadToken" id="downloadToken" />
			<s:hidden name="time" id="time" value="%{#request.time}" />
	</s:form>
	<s:if
		test="%{#request.messaggiBolla != null && !#request.messaggiBolla.isEmpty()}">
		</br>
		<dpe:table tableId="id1" items="messaggiBolla"
			imagePath="/app_img/table/*.gif"
			style="height:150px; font-size:11px; float: left;"
			rowsDisplayed="1000" styleClass="eXtremeTableFixedLengthFont"
			theme="eXtremeTableFixedLengthFont bollaScrollDivSmallH"
			showPagination="false" showExports="false" showStatusBar="false"
			showTitle="false" showTooltips="false" filterable="false"
			id="BollaMessaggiDiv">
			<ec:row style="height:25px;">
				<dpe:column property="messaggio" title="igeriv.messaggi"
					width="100%" filterable="false" sortable="false"
					preserveBlankSpaces="true" style="white-space: nowrap;" />
			</ec:row>
		</dpe:table>
	</s:if>
	<s:form id="ReportOrdiniClientiForm"
		action="report_esportaOrdiniClientiPdf.action" target="_blank"
		method="POST" theme="simple" validate="false">
	</s:form>
	</div>
</s:if>
<s:elseif test="actionName.contains('bollaRivendita_showBolla')">
	<div class="tableFields"
		style="align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:elseif>

<div id="dialogContentBarcode" class="eXtremeTable popup_block"
	style="font-size: 110%; z-index: 99999; background-color: #cccccc">
	<div style="width: 640px; height: 520px; z-index: 9">
		<table dojoType="dojox.grid.DataGrid" id="pubblicazioniTableBarcode"
			multiple="true" alternateRows="true" valueField="idtn"
			class="extremeTableFields"
			style="font-size: 100%; width: 630px; text-align: left; margin-left: 0px;"
			cellpadding="0px" cellspacing="0px">
			<thead>
				<tr>
					<th class="tableHeader" field="titolo" dataType="String"
						width="26%" style="text-align: left"><s:text
							name="igeriv.titolo" /></th>
					<th class="tableHeader" field="sottoTitolo" dataType="String"
						width="26%" style="width: 26%; text-align: left"><s:text
							name="igeriv.sottotitolo" /></th>
					<th class="tableHeader" field="numeroCopertina" dataType="String"
						width="12%" style="text-align: center"><s:text
							name="igeriv.numero" /></th>
					<th class="tableHeader" field="dataUscitaFormat" dataType="String"
						width="16%" style="text-align: center"><s:text
							name="igeriv.data.uscita" /></th>
					<th class="tableHeader" field="prezzoCopertinaFormat"
						dataType="String" width="12%" style="width: text-align:center"><s:text
							name="igeriv.prezzo" /></th>
				</tr>
			</thead>
		</table>
	</div>
</div>
<div id="dialogContentRichiediBarcode" class="eXtremeTable popup_block"
	style="font-size: 110%; z-index: 99999; background-color: #cccccc">
	<div style="width: 640px; height: 520px; z-index: 9">
		<table dojoType="dojox.grid.DataGrid"
			id="pubblicazioniTableRichiediBarcode" multiple="true"
			alternateRows="true" valueField="idtn" class="extremeTableFields"
			style="font-size: 100%; width: 630px; text-align: left; margin-left: 0px;"
			cellpadding="0px" cellspacing="0px">
			<thead>
				<tr>
					<th class="tableHeader" field="titolo" dataType="String"
						width="26%" style="text-align: left"><s:text
							name="igeriv.titolo" /></th>
					<th class="tableHeader" field="sottoTitolo" dataType="String"
						width="26%" style="width: 26%; text-align: left"><s:text
							name="igeriv.sottotitolo" /></th>
					<th class="tableHeader" field="numeroCopertina" dataType="String"
						width="12%" style="text-align: center"><s:text
							name="igeriv.numero" /></th>
					<th class="tableHeader" field="dataUscitaFormat" dataType="String"
						width="16%" style="text-align: center"><s:text
							name="igeriv.data.uscita" /></th>
					<th class="tableHeader" field="prezzoCopertinaFormat"
						dataType="String" width="12%" style="width: text-align:center"><s:text
							name="igeriv.prezzo" /></th>
				</tr>
			</thead>
		</table>
	</div>
</div>

