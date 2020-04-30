<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.itensBollaResaFuoriVoce != null && !#request.itensBollaResaFuoriVoce.isEmpty()}">
	<s:form id="BollaResaFuoriVoceForm"
		action="bollaResa_saveBollaResaFuoriVoce.action" method="POST"
		theme="simple" validate="false" onsubmit="return (ray.ajax())">
		<input type="hidden" name="tipoControlloPubblicazioniRespinte"
			id="tipoControlloPubblicazioniRespinte"
			value="<s:text name="authUser.tipoControlloPubblicazioniRespinte"/>" />
		<s:hidden name="hasBolleQuotidianiPeriodiciDivise"
			id="hasBolleQuotidianiPeriodiciDivise1" />
		<s:hidden name="forzaNonRespingere" id="forzaNonRespingere" />
		<s:iterator value="itensBollaResaFuoriVoce" status="status">
			<input type="hidden" id="numeroInContoDeposito_<s:property value='pk'/>"
				value="<s:property value='numeroInContoDeposito'/>|<s:property value='permetteContoDeposito'/>|<s:property value='pubblicazionePresenteNelleSuccessiveBolleResa'/>" />
			<input type="hidden" id="titolo_<s:property value='pk'/>"
				value="<s:property value='titolo'/>" />
			<input type="hidden" id="numero_<s:property value='pk'/>"
				value="<s:property value='numeroPubblicazione'/>" />
			<input type="hidden" id="motivoResaRespinta_<s:property value='pk'/>"
				value="<s:property value='codMotivoRespinto'/>|<s:property value='motivoResaRespinta'/>" />
		</s:iterator>
		<div id="mainDiv" style="width: 100%;">
			<dpe:table tableId="BollaResaFuoriVoceTab"
				items="itensBollaResaFuoriVoce" var="itensBollaResaFuoriVoce"
				action="bollaResa_showBollaResaFuoriVoce.action"
				imagePath="/app_img/table/*.gif" title="${title}"
				style="height:100px; table-layout:auto" view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.BollaResaAction"
				styleClass="extremeTableFieldsSmaller" form="BollaResaFuoriVoceForm"
				theme="eXtremeTable bollaScrollDivSmall" showPagination="false"
				filterable="false" showStatusBar="false" showExports="true"
				footerStyle="height:30px;">
				<dpe:exportPdf fileName="resa_fuori_voce.pdf"
					tooltip="plg.esporta.pdf" headerTitle="${title}"
					headerColor="black" headerBackgroundColor="#b6c2da"
					isLandscape="true" />
				<ec:exportXls fileName="resa_fuori_voce.xls"
					tooltip="plg.esporta.excel" />
				<dpe:row interceptor="marker" style="height:30px"
					href="#?resaAnticipata=resaAnticipata">
					<ec:column property="titolo" width="20%" title="igeriv.titolo"
						filterable="false" sortable="false" />
					<ec:column property="sottoTitolo" width="20%"
						title="igeriv.sottotitolo" filterable="false" sortable="false" />
					<ec:column property="numeroPubblicazione" width="4%"
						title="igeriv.numero" filterable="false" sortable="false" />
					<ec:column property="distribuito" width="5%"
						title="igeriv.distribuito" filterable="false" sortable="false"
						style="text-align:center" />
					<dpe:column property="reso" maxlength="3" size="4" width="5%"
						title="igeriv.reso" filterable="false" cell="bollaResaCell"
						pkName="pk" hasHiddenPkField="true"
						styleClass="extremeTableFieldsSmaller" sortable="false"
						style="text-align:right" exportStyle="text-align:center" />
					<ec:column property="giacenza" width="4%" title="igeriv.giacienza"
						filterable="false" sortable="false" style="text-align:center" />
					<dpe:column property="dataUscita" cell="date" width="8%"
						title="igeriv.data.uscita" dateFormat="dd/MM/yyyy"
						filterable="false" sortable="false" style="text-align:center" />
					<ec:column property="prezzoLordo" cell="currency" width="8%"
						title="igeriv.prezzo.lordo" filterable="false" sortable="false"
						style="text-align:right" />
					<ec:column property="prezzoNetto" cell="currency"
						format="###,###,##0.0000" width="8%" title="igeriv.prezzo.netto"
						filterable="false" sortable="false" style="text-align:right" />
					<s:if test="%{#session['codFiegDl'] eq #application['CDL_CODE']}">
						<!-- 15/12/2016 CDL - Gestione Ceste  	 -->
						<ec:column property="cesta" width="5%" title="Cesta"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" />
					</s:if>
					<ec:column property="nomeImmagine" title="igeriv.img"
						filterable="false" viewsDenied="pdf,xls" sortable="false"
						width="5%" style="text-align:right"
						cell="viewImageCell" >
<%-- 						<a	href="/immagini/${pageScope.itensBollaResaFuoriVoce.nomeImmagine}" --%>
<!-- 							rel="thumbnail"><img src="/app_img/camera.gif" width="15px" -->
<!-- 							height="15px" border="0" style="border-style: none" /></a> -->
					</ec:column>
				</dpe:row>
			</dpe:table>
			<div style="width: 400px; margin-top: -20px; margin-left: 380px;">
				<div style="float: left; width: 100px; margin-top: 0px;">
					<input type="button" name="igeriv.memorizza"
						id="memorizzaFuoriVoce" value="<s:text name='igeriv.memorizza'/>"
						align="center" class="tableFields"
						style="align: center; width: 150px; text-align: center"
						onclick="javascript: setTimeout(function() {return (setFormAction('BollaResaFuoriVoceForm','bollaResa_saveBollaResaFuoriVoce.action', '', '', false, '', function() {afterSaveResaFuoriVoce();}, function() { return validateFields('BollaResaFuoriVoceForm') && customValidateFields() && validatePubblicazioneRespinta() && validatePubblicazioneContoDeposito() && showConfirmDialogs();}, false, ''));},10);" />
				</div>
			</div>
			<s:iterator value="itensBollaResaFuoriVoce" status="status">
				<input type="hidden" id="<s:property value='barcode'/>"
					value="<s:property value='pk'/>" />
				<input type="hidden" id="reso_old_<s:property value='pk'/>"
					value="<s:property value='reso'/>" />
				<input type="hidden" id="giacenza_old_<s:property value='pk'/>"
					value="<s:property value='giacenza'/>" />
			</s:iterator>
	</s:form>
	</div>
</s:if>
