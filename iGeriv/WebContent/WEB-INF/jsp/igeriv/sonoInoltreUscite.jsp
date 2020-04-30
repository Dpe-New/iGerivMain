<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
div#filter {
	height: 120px;
}
</style>
<s:if
	test="%{#request.itensBolla != null && !#request.itensBolla.isEmpty()}">
	<s:form id="SonoInoltreUsciteForm"
		action="sonoInoltreUscite_showBollaSonoInoltreUscite.action"
		method="POST" theme="simple" validate="false"
		onsubmit="return (ray.ajax())">
		<div style="width: 100%">
			<dpe:table tableId="SonoInoltreUsciteTab" items="itensBolla"
				var="itensBolla"
				action="sonoInoltreUscite_showBollaSonoInoltreUscite.action"
				imagePath="/app_img/table/*.gif" view="buttonsOnTopAndBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
				styleClass="extremeTableFields" style="table-layout:fixed"
				form="SonoInoltreUsciteForm" theme="eXtremeTable"
				showPagination="false" id="SonoInoltreUsciteScrollDiv"
				toolbarClass="eXtremeTable" footerStyle="height:30px;"
				filterable="false">
				<dpe:exportPdf fileName="sono_inoltre_uscite.pdf"
					tooltip="plg.esporta.pdf"
					headerTitle="<fo:block text-align='center'>${title}</fo:block><br><fo:block text-align-last='justify'>${intestazioneAg}<fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block><fo:block text-align-last='justify'>${codAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block><fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block><fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block>"
					headerColor="black" headerBackgroundColor="#b6c2da"
					isLandscape="true" regionBeforeExtentInches="1.5"
					marginTopInches="1.2" logoImage="/app_img/rodis.gif" />
				<ec:exportXls fileName="sono_inoltre_uscite.xls"
					tooltip="plg.esporta.excel" />
				<dpe:row highlightRow="true" interceptor="marker"
					href="#?idtn=idtn&periodicita=periodicitaPk&coddl=pk.codFiegDl"
					style="cursor:pointer; height:25px">
					<ec:column property="rownum" width="5%" title="N."
						filterable="false" />
					<ec:column property="titolo" width="20%" title="igeriv.titolo"
						filterable="false" />
					<ec:column property="sottoTitolo" width="15%"
						title="igeriv.sottotitolo" filterable="false" />
					<ec:column property="numeroCopertina" width="5%"
						title="igeriv.numero" filterable="false" />
					<dpe:column property="prezzoCopertina" cell="currency"
						hasCurrencySign="true" format="###,###,##0.00" width="10%"
						title="igeriv.prezzo.lordo" filterable="false"
						style="text-align:right" />
					<ec:column property="argomento" width="12%"
						title="igeriv.argomento" filterable="false"
						style="text-align:left" />
					<ec:column property="periodicita" width="12%"
						title="igeriv.periodicita" filterable="false"
						style="text-align:left" />
					<dpe:column property="fake" cell="segnalazioniSonoInoltreUscite"
						width="15%" title="igeriv.segnalazioni" filterable="false"
						style="text-align:left" />
					<ec:column property="immagine" title="igeriv.img"
						filterable="false" width="5%" viewsDenied="pdf,xls"
						sortable="false" cell="viewImageCell" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%-- 					<a		id="${pageScope.itensBolla.immagine}" --%>
<%-- 							href="/immagini/${pageScope.itensBolla.immagine}" rel="thumbnail"><img --%>
<!-- 							src="/app_img/camera.gif" width="15px" height="15px" border="0" -->
<!-- 							style="border-style: none" /></a> -->
					</ec:column>
				</dpe:row>
			</dpe:table>
		</div>
	</s:form>
</s:if>
<s:elseif test="actionName.contains('showBollaSonoInoltreUscite')">
	<div class="tableFields"
		style="width: 100%; align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:elseif>
<s:form id="CopieFuoriCompetenzaForm"
	action="sonoInoltreUscite_saveCopieFuoriCompetenza.action"
	method="POST">
	<s:hidden name="pk" id="pkCopieFuoriCompetenza" />
	<s:hidden name="idtn" id="idtnCopieFuoriCompetenza" />
	<s:hidden name="quantita" id="quantitaCopieFuoriCompetenza" />
</s:form>
