<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="actionName != null && actionName.contains('bollaResa') && !actionName.contains('showFilterLavorazioneResa') && !actionName.contains('showQuadraturaResa')">
	<style>
div#filter {
	height: 160px;
}
</style>
</s:if>
<s:else>
	<style>
div#filter {
	height: 130px;
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
	<s:form id="BollaResaForm" action="viewBollaResa_showBollaResa.action"
		method="POST" theme="simple" validate="false"
		onsubmit="return (ray.ajax())">
		<s:hidden name="exportOnlyResoValorizzato"
			id="exportOnlyResoValorizzato" />
		<s:hidden name="dataTipoBolla" />
		<s:set name="title" value="%{getText('igeriv.bolla.resa')}" />
		<s:set name="titleFileName"
			value="%{getText('igeriv.bolla.resa.file.name')}" />
		<div id="extraHidden"></div>
		<div style="width: 100%">
			<div style="background-color: #F4F4F4; height: 30px; margin-top: 0px">
				<div
					style="width: 30%; float: left; text-align: left; font-weight: bold"
					class="extremeTableFieldsLarger">
					<s:text name="column.calc.total"></s:text>
				</div>
				<div style="width: 20%; float: left; text-align: left;"
					class="extremeTableFieldsLarger" style="font-weight:bold">
					<s:text name="igeriv.reso"></s:text>
					:&nbsp;<span id="totalHeader_1_viewBollaResa" class="calcTitle" style="font-weight: bold"></span>
				</div>
				<div style="width: 20%; float: left; text-align: left;"
					class="extremeTableFieldsLarger" style="font-weight:bold">
					<s:text name="igeriv.importo.lordo"></s:text>
					:&nbsp;<span id="totalHeader_2_viewBollaResa" class="calcTitle"
						style="font-weight: bold"></span>
				</div>
				<div style="width: 20%; float: left; text-align: left;"
					class="extremeTableFieldsLarger" style="font-weight:bold">
					<s:text name="igeriv.importo.netto"></s:text>
					:&nbsp;<span id="totalHeader_3_viewBollaResa" class="calcTitle"
						style="font-weight: bold"></span>
				</div>
			</div>
			<dpe:table tableId="BollaResaTab" items="itensBolla" var="itensBolla"
				action="viewBollaResa_showBollaResa.action"
				imagePath="/app_img/table/*.gif" style="table-layout:fixed"
				view="buttonsOnTopAndBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.ViewBolleResaAction"
				styleClass="extremeTableFieldsSmaller" form="BollaResaForm"
				theme="eXtremeTable" showPagination="false" id="BollaScrollDiv"
				toolbarClass="eXtremeTable" footerStyle="height:30px;"
				filterable="false" autoIncludeParameters="false">
				<dpe:exportPdf fileName="${titleFileName}_${strData}_${tipo}.pdf"
					tooltip="plg.esporta.pdf"
					headerTitle="<fo:block text-align='center'>${title} Data: ${strData} Tipo ${tipo}</fo:block><br><fo:block text-align-last='justify'>${intestazioneAg}<fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block><fo:block text-align-last='justify'>${codAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block><fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block><fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block><fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block><fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>"
					headerColor="black" headerBackgroundColor="#b6c2da"
					isLandscape="true" regionBeforeExtentInches="1.5"
					marginTopInches="1.3" repeatColumnHeaders="true"
					logoImage="/app_img/rodis.gif"
					onSubmitFunction="${strOnSubmitFunction}" />
				<ec:exportXls fileName="bolla_resa.xls" tooltip="plg.esporta.excel" />
				<dpe:row highlightRow="true" interceptor="marker"
					style="height:30px" href="#?cpu=cpuDl">
					<ec:column property="rownum" width="2%" title="N."
						filterable="false" />
					<ec:column property="cpuDl" width="4%" title="igeriv.cpu"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="titolo" width="16%" title="igeriv.titolo"
						filterable="false" />
					<ec:column property="sottoTitolo" width="12%"
						title="igeriv.sottotitolo" filterable="false" />
					<dpe:column property="numeroPubblicazione"
						cell="copieBollaResaCell" isLink="true"
						href="#?idtn=idtn&periodicita=periodicitaPk" styleClass="poplight"
						rel="popup_name" width="4%" title="igeriv.numero"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="distribuito" width="5%"
						title="igeriv.distribuito" filterable="false"
						style="text-align:center" />
					<dpe:column property="reso" calc="total"
						calcTitle="column.calc.total" totalCellStyle="text-align:right"
						validateIsNumeric="true" maxlength="3" size="2" width="5%"
						title="igeriv.reso" filterable="false" cell="bollaResaCell"
						pkName="pk" styleClass="extremeTableFieldsSmaller"
						style="font-size:120%; font-weight:bold; text-align:right"
						sortable="false" exportStyle="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="giacenza" width="5%" title="igeriv.giacienza"
						filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<dpe:column property="dataUscita" cell="date" width="8%"
						title="igeriv.data.uscita" dateFormat="dd/MM/yyyy"
						styleClass="extremeTableFieldsSmaller" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
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
						totalCellStyle="font-size:11px; text-align:right" cell="currency"
						format="###,###,##0.00" width="7%" title="igeriv.importo.lordo"
						filterable="false" style="text-align:right"
						headerStyle="text-align:right" />
					<dpe:column property="importoNetto" calc="total"
						hasCurrencySign="true" totalFormat="###,###,##0.0000"
						totalCellStyle="font-size:11px; text-align:right" cell="currency"
						format="###,###,##0.0000" width="7%" title="igeriv.importo.netto"
						filterable="false" style="text-align:right"
						headerStyle="text-align:right" />
					<dpe:column property="tipoRichiamo" width="8%"
						title="igeriv.tipo.richiamo" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="immagine" title="igeriv.img"
						filterable="false" viewsDenied="pdf,xls" sortable="false"
						width="3%" style="text-align:center"
						cell="viewImageCell" >
<%-- 						<a href="/immagini/${pageScope.itensBolla.immagine}" --%>
<!-- 							rel="thumbnail"><img src="/app_img/camera.gif" width="15px" -->
<!-- 							height="15px" border="0" style="border-style: none" /></a> -->
					</ec:column>
				</dpe:row>
			</dpe:table>
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
