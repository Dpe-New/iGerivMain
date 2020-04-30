<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="actionName != null && actionName.contains('bollaResa') && !actionName.contains('showFilterLavorazioneResa') && !actionName.contains('showQuadraturaResa')">
	<style>
div#filter {
	height: 165px;
}
</style>
</s:if>
<s:else>
	<style>
div#filter {
	height: 135px;
}
</style>
</s:else>
<s:if
	test="%{authUser.dlInforiv eq false && (#request.itensBolla == null || #request.itensBolla.isEmpty())}">
	<div class="tableFields"
		style="width: 100%; align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:if>
<s:else>
	<script>
	var msgEsportareSoloResoValorizzato = '<s:text name="gp.esportare.solo.reso.valorizzato"/>';
</script>
	<s:form id="BollaResaForm" action="bollaResa_save.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
		<s:hidden name="exportOnlyResoValorizzato" id="exportOnlyResoValorizzato" />
		<s:hidden name="dataTipoBolla" id="dtb" />
		<s:hidden name="autoincrement" />
		<s:hidden name="time" id="time" value="%{#request.time}" />
		<s:hidden name="hasBolleQuotidianiPeriodiciDivise" id="hasBolleQuotidianiPeriodiciDivise" />
		
		<!-- ESPORTAZIONE IN TIFF -->
		<s:if test="%{#session['isEnabledExportPDFToTIFF'] eq true}">
			<s:hidden name="type_return" id="type_return" value="TIFF"/>
		</s:if>
		
		<s:set name="title" value="%{getText('igeriv.bolla.resa')}" />
		<s:set name="titleFileName" value="%{getText('igeriv.bolla.resa.file.name')}" />
		<div id="extraHidden"></div>
		<div style="width: 100%">
			<div style="text-align: center">
				<fieldset class="filterBolla">
					<div id="quantita"
						style="margin-left: 50px; width: 100px; float: left; text-align: left; margin-top: 12px; white-space: nowrap;">
						<label for="qtaReso" style="width: 60px"
							class="extremeTableFieldsLarger"><s:text
								name="igeriv.quantita"></s:text></label>
						<s:textfield id="qtaReso" name="qtaReso" value="1"
							cssClass="extremeTableFieldsLarger" cssStyle="width:30px;" />
					</div>
					<div
						style="margin-left: 30px; width: 200px; float: left; text-align: left; margin-top: 12px; white-space: nowrap;">
						<label for="barcode" style="width: 100px"
							class="extremeTableFieldsLarger"><s:text
								name="igeriv.codice.barre"></s:text></label>
						<s:textfield id="barcode" name="barcode_reso"
							cssClass="extremeTableFieldsLarger" cssStyle="width:150px;" />
					</div>
				</fieldset>
			</div>
			<div id="lastRowId"
				style="text-align: center; width: 98%; height: 30px; margin-left: 0px; margin-top: 10px;">
				<fieldset class="filterBollaLarge"
					style="width: 100%; margin-left: 0px">
					<div class="extremeTableFieldsLarger"
						style="width: 100%; float: left; text-align: left; margin-top: -5px;">
						<div style="margin-left: 5px; width: 17%; float: left;">
							<s:text name="label.ultima.riga.aggiornata" />
							:
						</div>
						<div id="titoloDiv"
							style="width: 32%; float: left; font-size: 120%; font-weight: bold"></div>
						<div style="margin-left: 10px; width: 12%; float: left;">
							<s:text name="igeriv.numero" />
							&nbsp;&nbsp;<span id="numeroDiv"
								style="font-size: 120%; font-weight: bold"></span>
						</div>
						<div style="margin-left: 10px; width: 12%; float: left;">
							<s:text name="igeriv.copie" />
							&nbsp;&nbsp;<span id="copieDiv"
								style="font-size: 120%; font-weight: bold"></span>
						</div>
						<div style="margin-left: 10px; width: 12%; float: left;">
							<s:text name="igeriv.giacienza.ext" />
								&nbsp;&nbsp;<span id="giacDiv" style="font-size: 120%; font-weight: bold"></span>
								
							<s:if test="%{#session['codFiegDl'] eq #application['CDL_CODE']}">
								<!-- 15/12/2016 CDL - Gestione Ceste  	 -->	
								&nbsp;<s:text name="igeriv.cesta.ext" />&nbsp;&nbsp;<span id="cestaDiv" style="font-size: 120%;"></span>
							</s:if>
						</div>
					</div>
				</fieldset>
			</div>
			<div style="background-color: #F4F4F4; height: 30px; margin-top: 0px">
				<div
					style="width: 30%; float: left; text-align: left; font-weight: bold"
					class="extremeTableFieldsLarger">
					<s:text name="column.calc.total"></s:text>
				</div>
				<div style="width: 20%; float: left; text-align: left;"
					class="extremeTableFieldsLarger" style="font-weight:bold">
					<s:text name="igeriv.reso"></s:text>
					:&nbsp;<span id="totalHeader_1" class="calcTitle"
						style="font-weight: bold"></span>
				</div>
				<div style="width: 20%; float: left; text-align: left;"
					class="extremeTableFieldsLarger" style="font-weight:bold">
					<s:text name="igeriv.importo.lordo"></s:text>
					:&nbsp;<span id="totalHeader_2" class="calcTitle"
						style="font-weight: bold"></span>
				</div>
				<div style="width: 20%; float: left; text-align: left;"
					class="extremeTableFieldsLarger" style="font-weight:bold">
					<s:text name="igeriv.importo.netto"></s:text>
					:&nbsp;<span id="totalHeader_3" class="calcTitle"
						style="font-weight: bold"></span>
				</div>
			</div>
			
			<!-- 16/03/2017 SE CDL VIENE MOSTRATA LA GRIGLIA CON PERCENTUALI RIDOTTE PERCHE' 
			L'ESTRAZIONE DEL FILE PDF/TIFF VIENE FATTO IN VERTICALE isLandscape="false"-->
			<s:if test="%{#session['codFiegDl'] eq #application['CDL_CODE']}">
				
				<dpe:table tableId="BollaResaTab" items="itensBolla" var="itensBolla"
					action="bollaResa_showBollaResa.action"
					imagePath="/app_img/table/*.gif"
					style="table-layout:fixed; font-size:12px;"
					view="buttonsOnTopAndBottom"
					extraToolButton='<input type="button" value="${requestScope.inserisciNuovo}" name="plg.inserisci.nuovo.fuori.voce" id="butInserisciNuovoFuoriVoce" class="tableFields"/>'
					extraToolButtonStyle="width:25%; text-align:right; padding-right:10px"
					extraToolButton1='<input type="button" value="${requestScope.memorizza}" name="igeriv.memorizza" id="memorizza" title="${requestScope.memorizzaCtrlM}" class="buttonsBolleMemorizza" onclick="javascript: saveBollaResa();"/>'
					extraToolButton1Style="width:25%; text-align:right; padding-left:10px; padding-right:10px"
					extraToolButton2='<input type="button" value="${requestScope.memorizzaInvia}" name="igeriv.memorizza.invia" id="memorizzaInvia" title="${requestScope.memorizzaInviaCtrlI}" class="buttonsBolleMemorizzaInvia" onclick="javascript: saveAndSendBollaResa();"/>'
					extraToolButton2Style="width:25%; text-align:right; padding-left:10px"
					locale="${localeString}"
					state="it.dpe.igeriv.web.actions.BollaResaAction"
					styleClass="extremeTableFieldsSmaller" form="BollaResaForm"
					theme="eXtremeTable" showPagination="false" id="BollaScrollDiv"
					toolbarClass="eXtremeTable" footerStyle="height:30px;"
					filterable="false"
					filterRowsCallback="it.dpe.igeriv.web.extremecomponents.BollaResaFilterRowsCallback"
					autoIncludeParameters="false"
					beforeUnloadValidationScript="if (!validateFields('BollaResaForm')) {setTimeout(function() {unBlockUI();}, 100); return false;};">
					
					<!-- add 14/02/2017 - Gestione dell'esportazione in XLS e PDF (Gestione dei dati sensibili) -->
					<s:if test="%{#session['isEnabledExportPDF'] eq true}">
						<dpe:exportPdf fileName="${titleFileName}_${strData}_${tipo}.pdf"
							tooltip="plg.esporta.pdf"
							headerTitle="<fo:block text-align='center'>${title} Data ${strData} Tipo ${tipo}</fo:block><br><fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block><fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block><fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block><fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block><fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>"
							headerColor="black" headerBackgroundColor="#b6c2da"
							isLandscape="false" regionBeforeExtentInches="1.5"
							marginTopInches="1.3" repeatColumnHeaders="true"
							logoImage="/app_img/rodis.gif"
							onSubmitFunction="${strOnSubmitFunction}" />
					</s:if>
					
					<!-- ESPORTAZIONE IN TIFF -->
					<s:if test="%{#session['isEnabledExportPDFToTIFF'] eq true}">
						<dpe:exportPdf fileName="${titleFileName}_${strData}_${tipo}"
								tooltip="plg.esporta.tiff"
								headerTitle="<fo:block text-align='center'>${title} Data ${strData} Tipo ${tipo}</fo:block><br>
								<fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block>
								<fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block>
								<fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block>
								<fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block>
								<fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>"
								headerColor="black" headerBackgroundColor="#b6c2da"
								isLandscape="false" regionBeforeExtentInches="1.5"
								marginTopInches="1.3" repeatColumnHeaders="true"
								logoImage="/app_img/rodis.gif"
								onSubmitFunction="${strOnSubmitFunction}" 
								imageName="tiff"/>
					</s:if>
					<!-- ESPORTAZIONE IN EXCEL -->
					<s:if test="%{#session['isEnabledExportXLS'] eq true}">
						<ec:exportXls fileName="bolla_resa.xls" tooltip="plg.esporta.excel" />
					</s:if>
					
					<dpe:row highlightRow="true" interceptor="marker"
						style="height:30px" href="#?cpu=cpuDl">
						<ec:column property="rownum" width="3%" title="N."
							filterable="false" />
							<!-- add 14/02/2017 - Gestione dell'esportazione in XLS e PDF (Gestione dei dati sensibili) -->
							<s:if test="%{#session['isEnabledPKInExportXLS'] eq true && #session['isEnabledPKInExportPDF'] eq true}">
									<ec:column property="cpuDl" width="4%" title="igeriv.cpu" filterable="false" style="text-align:center" 
									headerStyle="text-align:center" />
							</s:if>
							<s:elseif test="%{#session['isEnabledPKInExportXLS'] eq true && #session['isEnabledPKInExportPDF'] eq false}">
									<ec:column property="cpuDl" width="4%" title="igeriv.cpu" filterable="false" style="text-align:center" 
									viewsDenied="pdf" headerStyle="text-align:center" />
							</s:elseif>
							<s:elseif test="%{#session['isEnabledPKInExportXLS'] eq false && #session['isEnabledPKInExportPDF'] eq true}">
									<ec:column property="cpuDl" width="4%" title="igeriv.cpu" filterable="false" style="text-align:center" 
									viewsDenied="xls" headerStyle="text-align:center" />
							</s:elseif>
							<s:else>
								<ec:column property="cpuDl" width="4%" title="igeriv.cpu" filterable="false" style="text-align:center" 
								viewsDenied="pdf,xls" headerStyle="text-align:center" />
							</s:else>
						
							<ec:column property="titolo" width="20%"
								title="igeriv.titolo.sottotitolo" filterable="false"
								cell="titoloBollaCell" />
							<dpe:column property="numeroPubblicazione"
								cell="copieBollaResaCell" isLink="true"
								href="#?idtn=idtn&periodicita=periodicitaPk" styleClass="poplight"
								rel="popup_name" width="4%" title="igeriv.numero"
								filterable="false" style="text-align:center; font-size:12px;"
								headerStyle="text-align:center" />
							<dpe:column property="distribuito" cell="copieBollaResaCell"
								isLink="true"
								href="#?idtn=idtn&coddl=pk.codFiegDl&periodicita=periodicitaPk"
								styleClass="poplight" rel="popup_name" width="3%"
								title="igeriv.distribuito" filterable="false"
								style="text-align:center; font-size:12px;" />
							<dpe:column property="reso" calc="total"
								calcTitle="column.calc.total" totalCellStyle="text-align:right"
								validateIsNumeric="true" maxlength="3" size="2" width="5%"
								title="igeriv.reso" filterable="false" cell="bollaResaCell"
								pkName="pk"
								style="font-size:120%; font-weight:bold; text-align:right"
								sortable="false" exportStyle="text-align:center"
								headerStyle="text-align:center" />
							<ec:column property="giacenza" width="3%" title="igeriv.giacienza"
								filterable="false" style="text-align:center"
								headerStyle="text-align:center" />
							<dpe:column property="dataUscita" cell="date" width="8%"
								title="igeriv.data.uscita" dateFormat="dd/MM/yyyy"
								filterable="false" style="text-align:center"
								headerStyle="text-align:center" viewsDenied="pdf"/>
							<dpe:column property="prezzoLordo" cell="currency"
								hasCurrencySign="true" format="###,###,##0.00" width="5%"
								title="igeriv.prezzo.lordo" filterable="false"
								style="text-align:right" headerStyle="text-align:right" />
							<dpe:column property="prezzoNetto" cell="currency"
								hasCurrencySign="true" format="###,###,##0.0000" width="5%"
								title="igeriv.prezzo.netto" filterable="false"
								style="text-align:right" headerStyle="text-align:right" />
							<dpe:column property="importoLordo" calc="total"
								hasCurrencySign="true" totalFormat="###,###,##0.00"
								totalCellStyle="text-align:right" cell="currency"
								format="###,###,##0.00" width="7%" title="igeriv.importo.lordo"
								filterable="false" style="text-align:right"
								headerStyle="text-align:right" viewsDenied="pdf,xls"/>
							<dpe:column property="importoNetto" calc="total"
								hasCurrencySign="true" totalFormat="###,###,##0.0000"
								totalCellStyle="text-align:right" cell="currency"
								format="###,###,##0.0000" width="7%" title="igeriv.importo.netto"
								filterable="false" style="text-align:right"
								headerStyle="text-align:right" />
							<dpe:column property="tipoRichiamo"
								cell="bollaResaTipoRichiamoCell" width="12%"
								title="igeriv.tipo.richiamo.note.ordini" filterable="false"
								style="text-align:left" headerStyle="text-align:left" />
						<s:if test="%{#session['codFiegDl'] eq #application['CDL_CODE']}">
							<!-- 15/12/2016 CDL - Gestione Ceste  	 -->
							<dpe:column property="cesta" width="3%" title="Cesta" cell="cdlGestioniCesta"  filterable="false"  
								headerStyle="text-align:center;background-color: #f2a730;" />
						</s:if>
						<ec:column property="immagine" title="igeriv.img"
							filterable="false" sortable="false" width="4%"
							style="text-align:center" viewsDenied="pdf,xls"
							cell="viewImageCell" >
						</ec:column>
					</dpe:row>
				</dpe:table>
			
			</s:if>
			
			<s:else>
			<!-- 16/03/2017 SE DIVERSO DA CDL VIENE MOSTRATA LA GRIGLIA CON PERCENTUALI DI DEFAULT PERCHE' 
			L'ESTRAZIONE DEL FILE PDF/TIFF VIENE FATTO IN ORIZZONTALE isLandscape="true" -->
				<dpe:table tableId="BollaResaTab" items="itensBolla" var="itensBolla"
					action="bollaResa_showBollaResa.action"
					imagePath="/app_img/table/*.gif"
					style="table-layout:fixed; font-size:12px;"
					view="buttonsOnTopAndBottom"
					extraToolButton='<input type="button" value="${requestScope.inserisciNuovo}" name="plg.inserisci.nuovo.fuori.voce" id="butInserisciNuovoFuoriVoce" class="tableFields"/>'
					extraToolButtonStyle="width:25%; text-align:right; padding-right:10px"
					extraToolButton1='<input type="button" value="${requestScope.memorizza}" name="igeriv.memorizza" id="memorizza" title="${requestScope.memorizzaCtrlM}" class="buttonsBolleMemorizza" onclick="javascript: saveBollaResa();"/>'
					extraToolButton1Style="width:25%; text-align:right; padding-left:10px; padding-right:10px"
					extraToolButton2='<input type="button" value="${requestScope.memorizzaInvia}" name="igeriv.memorizza.invia" id="memorizzaInvia" title="${requestScope.memorizzaInviaCtrlI}" class="buttonsBolleMemorizzaInvia" onclick="javascript: saveAndSendBollaResa();"/>'
					extraToolButton2Style="width:25%; text-align:right; padding-left:10px"
					locale="${localeString}"
					state="it.dpe.igeriv.web.actions.BollaResaAction"
					styleClass="extremeTableFieldsSmaller" form="BollaResaForm"
					theme="eXtremeTable" showPagination="false" id="BollaScrollDiv"
					toolbarClass="eXtremeTable" footerStyle="height:30px;"
					filterable="false"
					filterRowsCallback="it.dpe.igeriv.web.extremecomponents.BollaResaFilterRowsCallback"
					autoIncludeParameters="false"
					beforeUnloadValidationScript="if (!validateFields('BollaResaForm')) {setTimeout(function() {unBlockUI();}, 100); return false;};">
					
					<!-- add 14/02/2017 - Gestione dell'esportazione in XLS e PDF (Gestione dei dati sensibili) -->
					<!-- VITTORIO 05/12/2018 -->
					<!-- headerTitle="<fo:block text-align='center'>${title} Data ${strData} Tipo ${tipo}</fo:block><br><fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block><fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block><fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block><fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block><fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>" -->
					<s:if test="%{#session['isEnabledExportPDF'] eq true}">
						<dpe:exportPdf fileName="${titleFileName}_${strData}_${tipo}.pdf"
							tooltip="plg.esporta.pdf"
							headerTitle="<fo:block text-align='center'>${title} Data ${strData} Tipo ${tipo}</fo:block><br><fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block><fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block><fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block><fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block><fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>"
							headerColor="black" headerBackgroundColor="#b6c2da"
							isLandscape="true" regionBeforeExtentInches="1.5"
							marginTopInches="1.3" repeatColumnHeaders="true"
							logoImage="/app_img/rodis.gif"
							onSubmitFunction="${strOnSubmitFunction}" />
					</s:if>
					
					<!-- ESPORTAZIONE IN TIFF -->
					<s:if test="%{#session['isEnabledExportPDFToTIFF'] eq true}">
						<dpe:exportPdf fileName="${titleFileName}_${strData}_${tipo}"
								tooltip="plg.esporta.tiff"
								headerTitle="<fo:block text-align='center'>${title} Data ${strData} Tipo ${tipo}</fo:block><br>
								<fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block>
								<fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block>
								<fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block>
								<fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block>
								<fo:block text-align-last='justify'><fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>"
								headerColor="black" headerBackgroundColor="#b6c2da"
								isLandscape="true" regionBeforeExtentInches="1.5"
								marginTopInches="1.3" repeatColumnHeaders="true"
								logoImage="/app_img/rodis.gif"
								onSubmitFunction="${strOnSubmitFunction}" 
								imageName="tiff"/>
					</s:if>
					
					<s:if test="%{#session['isEnabledExportXLS'] eq true}">
						<ec:exportXls fileName="bolla_resa.xls" tooltip="plg.esporta.excel" />
					</s:if>
					
					<dpe:row highlightRow="true" interceptor="marker"
						style="height:30px" href="#?cpu=cpuDl">
						<ec:column property="rownum" width="3%" title="N."
							filterable="false" />
						<!-- add 14/02/2017 - Gestione dell'esportazione in XLS e PDF (Gestione dei dati sensibili) -->
						<s:if test="%{#session['isEnabledPKInExportXLS'] eq true && #session['isEnabledPKInExportPDF'] eq true}">
								<ec:column property="cpuDl" width="4%" title="igeriv.cpu" filterable="false" style="text-align:center" 
								headerStyle="text-align:center" />
						</s:if>
						<s:elseif test="%{#session['isEnabledPKInExportXLS'] eq true && #session['isEnabledPKInExportPDF'] eq false}">
								<ec:column property="cpuDl" width="4%" title="igeriv.cpu" filterable="false" style="text-align:center" 
								viewsDenied="pdf" headerStyle="text-align:center" />
						</s:elseif>
						<s:elseif test="%{#session['isEnabledPKInExportXLS'] eq false && #session['isEnabledPKInExportPDF'] eq true}">
								<ec:column property="cpuDl" width="4%" title="igeriv.cpu" filterable="false" style="text-align:center" 
								viewsDenied="xls" headerStyle="text-align:center" />
						</s:elseif>
						<s:else>
							<ec:column property="cpuDl" width="4%" title="igeriv.cpu" filterable="false" style="text-align:center" 
							viewsDenied="pdf,xls" headerStyle="text-align:center" />
						</s:else>
						
						<ec:column property="titolo" width="20%"
							title="igeriv.titolo.sottotitolo" filterable="false"
							cell="titoloBollaCell" />
						<dpe:column property="numeroPubblicazione"
							cell="copieBollaResaCell" isLink="true"
							href="#?idtn=idtn&periodicita=periodicitaPk" styleClass="poplight"
							rel="popup_name" width="4%" title="igeriv.numero"
							filterable="false" style="text-align:center; font-size:12px;"
							headerStyle="text-align:center" />
						<dpe:column property="distribuito" cell="copieBollaResaCell"
							isLink="true"
							href="#?idtn=idtn&coddl=pk.codFiegDl&periodicita=periodicitaPk"
							styleClass="poplight" rel="popup_name" width="5%"
							title="igeriv.distribuito" filterable="false"
							style="text-align:center; font-size:12px;" />
						<dpe:column property="reso" calc="total"
							calcTitle="column.calc.total" totalCellStyle="text-align:right"
							validateIsNumeric="true" maxlength="3" size="2" width="5%"
							title="igeriv.reso" filterable="false" cell="bollaResaCell"
							pkName="pk"
							style="font-size:120%; font-weight:bold; text-align:right"
							sortable="false" exportStyle="text-align:center"
							headerStyle="text-align:center" />
						<ec:column property="giacenza" width="5%" title="igeriv.giacienza"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" />
						<dpe:column property="dataUscita" cell="date" width="8%"
							title="igeriv.data.uscita" dateFormat="dd/MM/yyyy"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" />
						<dpe:column property="prezzoLordo" cell="currency"
							hasCurrencySign="true" format="###,###,##0.00" width="6%"
							title="igeriv.prezzo.lordo" filterable="false"
							style="text-align:right" headerStyle="text-align:right" />
						<dpe:column property="prezzoNetto" cell="currency"
							hasCurrencySign="true" format="###,###,##0.0000" width="6%"
							title="igeriv.prezzo.netto" filterable="false"
							style="text-align:right" headerStyle="text-align:right" />
						<dpe:column property="importoLordo" calc="total"
							hasCurrencySign="true" totalFormat="###,###,##0.00"
							totalCellStyle="text-align:right" cell="currency"
							format="###,###,##0.00" width="7%" title="igeriv.importo.lordo"
							filterable="false" style="text-align:right"
							headerStyle="text-align:right" />
						<dpe:column property="importoNetto" calc="total"
							hasCurrencySign="true" totalFormat="###,###,##0.0000"
							totalCellStyle="text-align:right" cell="currency"
							format="###,###,##0.0000" width="7%" title="igeriv.importo.netto"
							filterable="false" style="text-align:right"
							headerStyle="text-align:right" />
						<dpe:column property="tipoRichiamo"
							cell="bollaResaTipoRichiamoCell" width="15%"
							title="igeriv.tipo.richiamo.note.ordini" filterable="false"
							style="text-align:left" headerStyle="text-align:left" />

						<ec:column property="immagine" title="igeriv.img"
								filterable="false" sortable="false" width="4%"
								style="text-align:center" viewsDenied="pdf,xls"
								cell="viewImageCell" >
						</ec:column>
					</dpe:row>
				</dpe:table>
			
			
			</s:else>
			
			
			
			
			
			
			
			
			
			<s:iterator value="itensBolla" status="status">
				<input type="hidden" id="<s:property value='barcode'/>"
					value="<s:property value='pk'/>" />
				<input type="hidden" id="reso_old_<s:property value='pk'/>"
					value="<s:property value='reso'/>" />
				<input type="hidden" id="giacenza_old_<s:property value='pk'/>"
					value="<s:property value='giacenza'/>" />
				<input type="hidden" id="tipoRichiamoExt_<s:property value='pk'/>"
					value="<s:property value='tipoRichiamoExt'/>" />
				<input type="hidden" name="noteRivendita"
					id="noteRivendita<s:property value='pk'/>"
					value="<s:property value='note'/>" />
				<input type="hidden" name="noteRivenditaCpu"
					id="noteRivenditaCpu<s:property value='pk'/>"
					value="<s:property value='noteByCpu'/>" />
				<s:if test="idtnContoDeposito != null && idtnContoDeposito > 0">
					<input type="hidden" name="contoDeposito"
						id="contoDeposito_<s:property value='idtnContoDeposito'/>"
						value="<s:property value='idtnContoDeposito'/>" />
				</s:if>
			</s:iterator>
	</s:form>
	</div>
</s:else>
