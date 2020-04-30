<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.listLivellamenti != null && !#request.listLivellamenti.isEmpty()}">
	<div id="mainDiv" style="width: 100%; text-align: center">
		<s:form id="LivellamentiForm"
			action="avvisoLivellamenti_saveAccettazioneLivellamenti.action"
			method="POST" theme="simple" validate="false"
			onsubmit="return (ray.ajax())">
			<div class="eXtremeTable title"
				style="text-align: center; font-weight: bold;">
				<s:text
					name="igeriv.conferma.propste.vendita.livellamenti.da.accettare" />
			</div>
			<div style="text-align: left;">
				<dpe:table tableId="LivellamentiTab" items="listLivellamenti"
					var="listLivellamenti"
					action="${pageContext.request.contextPath}/avvisoLivellamenti_saveAccettazioneLivellamenti.action"
					imagePath="/app_img/table/*.gif" style="height:60px;"
					view="buttonsOnBottom" locale="${localeString}"
					state="it.dpe.igeriv.web.actions.LivellamentiAction"
					styleClass="extremeTableFields" form="LivellamentiForm"
					theme="eXtremeTable bollaScrollDivContentMediumTall"
					showPagination="false" id="EstrattoContoScrollDiv"
					toolbarClass="eXtremeTable" showStatusBar="false" showTitle="false"
					showTooltips="false" footerStyle="height:30px;" filterable="false"
					sortable="false" autoIncludeParameters="false" showExports="false">
					<dpe:row highlightRow="true" interceptor="marker"
						style="height:30px;">
						<dpe:column
							property="richiesta.storicoCopertineVo.anagraficaPubblicazioniVo.titolo"
							width="20%" title="igeriv.titolo" />
						<%--<dpe:column property="richiesta.storicoCopertineVo.sottoTitolo" width="10%" title="igeriv.sottotitolo" /> --%>
						<dpe:column
							property="richiesta.storicoCopertineVo.numeroCopertina"
							width="8%" title="igeriv.numero" style="text-align:center"
							headerStyle="text-align:center" exportStyle="text-align:center" />
						<dpe:column property="richiesta.storicoCopertineVo.dataUscita"
							width="8%" title="igeriv.data.uscita" cell="date"
							format="dd/MM/yyyy" style="text-align:center"
							headerStyle="text-align:center" exportStyle="text-align:center" />
						<dpe:column property="quantita" width="10%" title="plg.quantita"
							style="text-align:center" headerStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="edicola.ragioneSocialeEdicolaPrimaRiga"
							width="15%" title="igeriv.edicola.venditrice" />
						<dpe:column property="edicola.indirizzoViaNumeroCitta" width="33%"
							title="dpe.indirizzo" />
						<dpe:column property="statoVendita" width="10%"
							title="igeriv.richiesta.rifornimenti.stato.richiesta" />
						<dpe:column property="fake" alias="spunta" width="5%"
							title="igeriv.accetta" cell="accettazionelivellamentiSpunta"
							headerStyle="text-align:center" style="text-align:center" />
					</dpe:row>
				</dpe:table>
			</div>
			<div style="width: 100%;">
				<div
					style="text-align: center; width: 250px; height: 50px; margin-top: 10px; margin-left: auto; margin-right: auto;">
					<input type="button" value="<s:text name='igeriv.memorizza'/>"
						name="igeriv.memorizza" id="memorizzaRifornimenti"
						class="tableFields" style="width: 100px; text-align: center"
						onclick="javascript: setTimeout(function() {return (setFormAction('LivellamentiForm','avvisoLivellamenti_saveAccettazioneLivellamenti.action', '', '', false, '', function() {$('#close').trigger('click')}, '', false, '', false));},10);" />
				</div>
			</div>
		</s:form>
	</div>
</s:if>

