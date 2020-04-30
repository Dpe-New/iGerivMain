<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.itensBolla != null && !#request.itensBolla.isEmpty()}">
	<fieldset
		style="margin-left: 5%; width: 90%; height: 140px; text-align: center; margin-top: 12px;">
		<legend style="font-size: 100%;">
			<s:text name="igeriv.pubblicazione" />
			&nbsp;(
			<s:text name="igeriv.ricerca.barcode.titolo.prezzo" />
			)
		</legend>
		<div
			style="width: 60%; margin-left: auto; margin-right: auto; margin-top: 10px; white-space: nowrap;">
			<img name="findPub" id="findPub" src="/app_img/find.png"
				alt="<s:text name="dpe.contact.form.modalita.ricerca"/>" border="0"
				title="<s:text name="dpe.contact.form.modalita.ricerca"/>"
				style="cursor: pointer" width="22px" height="22px"
				onclick="javascript:setModalita(MODALITA_RICERCA)" />&nbsp;
			
			<s:textfield name="qta" id="qta"
				cssStyle="width:10%; text-align:right; font-size:150%;" value="1"  />
			<span style="font-size: 150%">&nbsp;X&nbsp;</span>
			<s:textfield name="barcode" id="barcode"
				cssStyle="width:80%; font-size:150%;" />
		</div>
		<div id="spuntaOutput" class="eXtremeTable"
			style="margin-top: 18px; width: 100%;"></div>
	</fieldset>
	<s:form id="BollaControlloForm"
		action="bollaRivenditaSpunta_save.action" method="POST" theme="simple"
		validate="false" onsubmit="return (ray.ajax())">
		<s:iterator value="itensBolla" status="status">
			<input type="hidden" id="<s:property value='barcode'/>" value="<s:property value='pk'/>" />
			<input type="hidden" id="idtn_<s:property value='barcode'/>" value="<s:property value='idtn'/>" />
			<input type="hidden" name="pkNoteRivendita" id="pkNoteRivendita<s:property value='idtn'/>" value="<s:property value='idtn'/>">
			<input type="hidden" name="noteRivendita" id="noteRivendita<s:property value='idtn'/>" value="<s:property value='note'/>">
			<input type="hidden" name="noteRivenditaCpu" id="noteRivenditaCpu<s:property value='codicePubblicazione'/>" value="<s:property value='noteByCpu'/>" />
		</s:iterator>
		
		<!-- ESPORTAZIONE IN TIFF -->
		<s:if test="%{#session['isEnabledExportPDFToTIFF'] eq true}">
			<s:hidden name="type_return" id="type_return" value="TIFF"/>
		</s:if>
		
		
		<s:set name="title" value="%{getText('igeriv.bolla.consegna')}" />
		<s:set name="titleFileName"
			value="%{getText('igeriv.bolla.consegna.file.name')}" />
		<div>
			<dpe:table tableId="BollaControlloTab" items="itensBolla"
				var="itensBolla" action="bollaRivenditaSpunta_showBolla.action"
				imagePath="/app_img/table/*.gif" style="table-layout:fixed;"
				view="buttonsOnBottom"
				extraToolButton='<input type="button" value="${requestScope.memorizza}" name="igeriv.memorizza" id="memorizza" class="tableFields" onclick="javascript: setTimeout(jConfirm(memorizzaConfirm, attenzioneMsg, function(r) { if (r) { $(\'#dataTipoBolla1\').val($(\'#dataTipoBolla\').val()); setFormAction(\'BollaControlloForm\',\'bollaRivenditaSpunta_save.action\', \'\', \'messageDiv\', false, \'\', function() {resetModifiedFields();}, function() {return validateFieldsSpuntaBolla(); unBlockUI();}, true, function() {unBlockUI();});} else { unBlockUI();}}),10);"/>'
				extraToolButtonStyle="width:35%; text-align:center; padding-right:10px; font-size:12px;"
				extraToolButton2='<input type="button" value="${requestScope.memorizzaInvia}" name="igeriv.memorizza.invia" id="memorizzaInvia" title="${requestScope.memorizzaInviaCtrlI}" class="buttonsBolleMemorizzaInvia" onclick="javascript: saveAndSendBollaSpunta();"/>'
				extraToolButton2Style="width:35%; text-align:center; padding-right:10px; font-size:12px;"
				extraToolButton3='<img src="/app_img/clients.gif" width="28px" height="28px" id="imgClients" alt="${requestScope.esportaOrdini}" border="0" title="${requestScope.esportaOrdini}" style="cursor:pointer" onclick="javascript: esportaOrdini()"/>'
				extraToolButton3Style="width:30%; text-align:right; padding-right:15px"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
				styleClass="extremeTableFieldsSmaller" form="BollaControlloForm"
				theme="eXtremeTable bollaScrollSmallerDiv" showPagination="false"
				id="BollaSpuntaDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false" showStatusBar="false"
				autoIncludeParameters="false" sortable="false"
				beforeUnloadValidationScript="if (hasFieldsToSave()) {memorizzaPrimaDiUscire(); setTimeout(function() {unBlockUI();}, 100); return false;} ">
				
				<!-- add 14/02/2017 - Gestione dell'esportazione in XLS e PDF (Gestione dei dati sensibili) -->
				<s:if test="%{#session['isEnabledExportPDF'] eq true}">
					<dpe:exportPdf fileName="${titleFileName}_${strData}_${tipo}.pdf"
						tooltip="plg.esporta.pdf"
						headerTitle="<fo:block text-align='center'>${title} Data ${strData} Tipo ${tipo}</fo:block><br><fo:block text-align-last='justify'>${intestazioneAg}<fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block><fo:block text-align-last='justify'>${codAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block><fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block><fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block><fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>"
						headerColor="black" headerBackgroundColor="#b6c2da"
						isLandscape="false" regionBeforeExtentInches="1.5"
						marginTopInches="1.3" repeatColumnHeaders="true"
						logoImage="/app_img/rodis.gif" />
				</s:if>
				<!-- ESPORTAZIONE IN TIFF -->
				<s:if test="%{#session['isEnabledExportPDFToTIFF'] eq true}">
					<dpe:exportPdf fileName="${titleFileName}_${strData}_${tipo}"
						tooltip="plg.esporta.tiff"
						headerTitle="<fo:block text-align='center'>${title} Data ${strData} Tipo ${tipo}</fo:block><br><fo:block text-align-last='justify'>${intestazioneAg}<fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block><fo:block text-align-last='justify'>${codAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block><fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block><fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block><fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>"
						headerColor="black" headerBackgroundColor="#b6c2da"
						isLandscape="false" regionBeforeExtentInches="1.5"
						marginTopInches="1.3" repeatColumnHeaders="true"
						logoImage="/app_img/rodis.gif" 
						imageName="tiff"/>
				</s:if>
				
				
				<s:if test="%{#session['isEnabledExportXLS'] eq true}">
					<ec:exportXls fileName="${titleFileName}_${strData}_${tipo}.xls" tooltip="plg.esporta.excel" />
				</s:if>
				
				<dpe:row highlightRow="true" interceptor="marker"
					href="#?idtn=idtn&periodicita=periodicitaPk&coddl=pk.codFiegDl"
					style="height:35px">
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
						
			
					<ec:column property="titolo" width="20%" title="igeriv.titolo"
						filterable="false" />
					<ec:column property="sottoTitolo" width="16%"
						title="igeriv.sottotitolo" filterable="false" viewsDenied="pdf,xls"/>
					<dpe:column property="numeroPubblicazione" width="5%"
						title="igeriv.numero" filterable="false" escapeAutoFormat="true"
						style="text-align:center" headerStyle="text-align:center" />
					<dpe:column property="prezzoLordo" cell="currency"
						hasCurrencySign="true" format="###,###,##0.00"
						style="text-align:right" headerStyle="text-align:right" width="6%"
						title="igeriv.prezzo.lordo" filterable="false" />
					<dpe:column property="quantitaConsegnata"
						cell="copieBollaSpuntaCell" isLink="true"
						href="#?idtn=idtn&periodicita=periodicitaPk" styleClass="poplight"
						rel="popup_name" width="5%" title="igeriv.copie.bolla"
						filterable="false"
						style="text-align:center; font-weight:bold; font-size:120%"
						headerStyle="text-align:center" />
					<dpe:column property="quantitaSpuntata" cell="copieBollaSpuntaCell"
						width="5%" title="igeriv.copie.lette" filterable="false"
						style="text-align:center; font-weight:bold; font-size:120%"
						headerStyle="text-align:center" />
					<dpe:column property="differenze" width="5%"
						title="igeriv.differenza" filterable="false"
						style="text-align:center; font-weight:bold; font-size:160%"
						headerStyle="text-align:center" />
					<dpe:column property="confermaDifferenze" maxlength="4" size="2"
						width="10%" title="conferma.differenze"
						sessionVarName="differenze" hasHiddenPkField="true"
						filterable="false" headerCell="copiaDifferenze"
						cell="spuntaBollaDifferenza" pkName="pk"
						styleClass="extremeTableFieldsSmaller"
						exportStyle="text-align:center"
						style="font-size:130%; font-weight:bold; text-align:left"
						headerStyle="text-align:center" sortable="false" />
					<ec:column property="tipoFondoBolla" width="10%"
						title="igeriv.fondo.bolla" filterable="false" sortable="false" />
					<dpe:column property="ordini" isLink="true" href="#?idtn=idtn"
						styleClass="poplight" rel="popup_name" width="6%"
						title="igeriv.ordini" filterable="false"
						style="text-align:center; font-size:110%; font-weight:bold;"
						headerStyle="text-align:center;" />
					<ec:column property="immagine" title="igeriv.img"
						filterable="false" viewsDenied="pdf,xls" sortable="false"
						width="4%" style="text-align:center;"
						headerStyle="text-align:center"
						cell="viewImageCell" >
<%-- 						<a href="/immagini/${pageScope.itensBolla.immagine}" --%>
<!-- 							rel="thumbnail"><img src="/app_img/camera.gif" width="15px" -->
<!-- 							height="15px" border="0" -->
<!-- 							style="border-style: none; text-align: center" /></a> -->
					</ec:column>
				</dpe:row>
			</dpe:table>
			<s:hidden name="importoLordo" id="importoLordo" />
			<s:hidden name="downloadToken" id="downloadToken" />
			<s:hidden name="spunte" id="spunte" />
			<s:hidden name="dataTipoBolla" id="dataTipoBolla1" />
	</s:form>
	<s:form id="ReportOrdiniClientiForm"
		action="report_esportaOrdiniClientiPdf.action" target="_blank"
		method="POST" theme="simple" validate="false">
	</s:form>
	</div>
</s:if>
<s:elseif test="actionName.contains('bollaRivenditaSpunta_showBolla')">
	<div class="tableFields"
		style="align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:elseif>
