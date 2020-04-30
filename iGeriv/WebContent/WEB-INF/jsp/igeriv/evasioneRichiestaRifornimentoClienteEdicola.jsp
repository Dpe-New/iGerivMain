<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv">
	<s:set name="keys" value="%{risposteClientiSelectKeys}" />
	<s:set name="values" value="%{risposteClientiSelectValues}" />
	<s:if
		test="%{#request.richiesteRifornimento != null && !#request.richiesteRifornimento.isEmpty()}">
		<s:set name="an" value="%{actionName1}" />
		<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
		<s:if test='#context["struts.actionMapping"].namespace == "/"'>
			<s:set name="ap" value="" />
		</s:if>
		<s:form id="EvasioneClienteEdicolaForm"
			action="gestioneClienti_saveEvasionePrenotazioniClientiEdicola.action"
			method="POST" theme="simple" validate="false"
			onsubmit="return (ray.ajax())">
			<div style="width: 100%; text-align: center">
				<s:text name="igeriv.data.competenza.estratto.conto" />
				&nbsp;&nbsp;
				<s:textfield name="dataCompEC" id="dataCompEC" cssClass="required"
					cssStyle="width:90px;" disabled="false" />
			</div>
			<div style="width: 100%; text-align: center">
				<input type="button" value="<s:text name='igeriv.memorizza'/>"
					name="igeriv.memorizza" style="width: 100px; text-align: center"
					onclick="javascript: setTimeout(function() {afterSuccessSave = function() {setFieldsToSave(false)}; return (validateFields('EvasioneClienteEdicolaForm') && validateExtraFields(true) && setFieldsToSave(true) && submitForm('EvasioneClienteEdicolaForm'));},50);" />
			</div>
			<div id="mainDiv" style="width: 100%">
				<s:if test="spunte != null && !spunte.isEmpty()">
					<dpe:table tableId="RichiesteRifornimentiClienteEdicola"
						items="richiesteRifornimento" var="richiesteRifornimento"
						action="${pageContext.request.contextPath}/gestioneClienti_viewEvasionePrenotazioniClientiEdicola.action"
						imagePath="/app_img/table/*.gif" style="table-layout:fixed"
						rowsDisplayed="1000" view="buttonsOnBottom"
						locale="${localeString}"
						state="it.dpe.igeriv.web.actions.GestioneClientiAction"
						styleClass="extremeTableFields" form="EvasioneClienteEdicolaForm"
						theme="eXtremeTable bollaScrollSmallerDiv" showPagination="false"
						showExports="false" id="BollaScrollDiv"
						toolbarClass="eXtremeTable" footerStyle="height:30px;"
						filterable="false">
						<ec:row style="height:30px">
							<ec:column property="nomeCognomeCliente" width="18%"
								title="username.cliente" filterable="false" />
							<dpe:column property="dataOrdine" width="6%"
								title="igeriv.data.ordine" cell="date" format="dd/MM/yyyy"
								style="text-align:center" headerStyle="text-align:center"
								filterable="false" />
							<dpe:column property="titolo" width="20%" title="igeriv.titolo"
								filterable="false" />
							<dpe:column property="numeroCopertina" width="5%"
								title="igeriv.numero" filterable="false"
								style="text-align:center" headerStyle="text-align:center" />
							<dpe:column property="prezzoCopertina" width="5%"
								title="igeriv.prezzo" cell="currency" filterable="false"
								style="text-align:center;" headerStyle="text-align:center;" />
							<dpe:column property="quantitaDaEvadere" width="5%"
								title="igeriv.richiesta.rifornimenti.quantita.da.evadere"
								style="text-align:center" headerStyle="text-align:center"
								filterable="false" />
							<dpe:column property="quantitaEvasa" value="0"
								validateIsNumeric="true" maxlength="3" size="4" width="5%"
								title="igeriv.richiesta.rifornimenti.quantita.evasa"
								filterable="false" cell="textDifferenza" pkName="pk"
								sessionVarName="evaso" hasHiddenPkField="true" sortable="false"
								allowZeros="false" />
							<dpe:column property="codiceUltimaRisposta" cell="dpeCombo"
								width="15%" title="igeriv.messaggio.codificato"
								filterable="false" optionKeys="${keys}" optionValues="${values}"
								hasEmptyOption="true" pkName="pk"
								sessionVarName="ultimaRisposta" hasHiddenPkField="false"
								style="font-size:12px; width:220px; text-align:left"
								headerStyle="text-align:center" />
							<dpe:column property="messagioLibero" value="&nbsp;"
								maxlength="250" size="30" width="15%"
								title="igeriv.messaggio.libero" cell="textDifferenza"
								pkName="pk" sessionVarName="messLibero" hasHiddenPkField="false"
								filterable="false" sortable="false" style="text-align:center"
								headerStyle="text-align:center" />
						</ec:row>
					</dpe:table>
				</s:if>
				<s:else>
					<dpe:table tableId="RichiesteRifornimentiClienteEdicola"
						items="richiesteRifornimento" var="richiesteRifornimento"
						action="${pageContext.request.contextPath}/gestioneClienti_viewEvasionePrenotazioniClientiEdicola.action"
						imagePath="/app_img/table/*.gif" style="table-layout:fixed;"
						rowsDisplayed="1000" view="buttonsOnBottom"
						locale="${localeString}"
						state="it.dpe.igeriv.web.actions.GestioneClientiAction"
						styleClass="extremeTableFields" form="EvasioneClienteEdicolaForm"
						theme="eXtremeTable bollaScrollSmallerDiv" showPagination="false"
						showExports="false" id="BollaScrollDiv"
						toolbarClass="eXtremeTable" footerStyle="height:30px;"
						filterable="false">
						<ec:row style="height:30px">
							<dpe:column property="dataOrdine" width="8%"
								title="igeriv.data.ordine" cell="date" format="dd/MM/yyyy"
								style="text-align:center" headerStyle="text-align:center"
								filterable="false" />
							<dpe:column property="titolo" width="20%" title="igeriv.titolo"
								filterable="false" />
							<dpe:column property="numeroCopertina" width="8%"
								title="igeriv.numero" filterable="false"
								style="text-align:center" headerStyle="text-align:center" />
							<dpe:column property="prezzoCopertina" width="8%"
								title="igeriv.prezzo" cell="currency" filterable="false"
								style="text-align:center;" headerStyle="text-align:center;" />
							<dpe:column property="quantitaDaEvadere" width="8%"
								title="igeriv.richiesta.rifornimenti.quantita.da.evadere"
								style="text-align:center" headerStyle="text-align:center"
								filterable="false" />
							<dpe:column property="quantitaEvasa" value="0"
								validateIsNumeric="true" maxlength="3" size="4" width="8%"
								title="igeriv.richiesta.rifornimenti.quantita.evasa"
								filterable="false" cell="textDifferenza" pkName="pk"
								sessionVarName="evaso" hasHiddenPkField="true" sortable="false"
								allowZeros="false" />
							<dpe:column property="codiceUltimaRisposta" cell="dpeCombo"
								width="20%" title="igeriv.messaggio.codificato"
								filterable="false" optionKeys="${keys}" optionValues="${values}"
								hasEmptyOption="true" pkName="pk"
								sessionVarName="ultimaRisposta" hasHiddenPkField="false"
								style="font-size:12px; width:220px; text-align:left"
								headerStyle="text-align:center" />
							<dpe:column property="messagioLibero" value="&nbsp;"
								maxlength="250" size="35" width="20%"
								title="igeriv.messaggio.libero" cell="textDifferenza"
								pkName="pk" sessionVarName="messLibero" hasHiddenPkField="false"
								filterable="false" sortable="false" style="text-align:center"
								headerStyle="text-align:center" />
						</ec:row>
					</dpe:table>
				</s:else>
			</div>
			<div style="width: 100%; text-align: center">
				<input type="button" value="<s:text name='igeriv.memorizza'/>"
					name="igeriv.memorizza" style="width: 100px; text-align: center"
					onclick="javascript: setTimeout(function() {afterSuccessSave = function() {setFieldsToSave(false)}; return (validateFields('EvasioneClienteEdicolaForm') && validateExtraFields(true) && setFieldsToSave(true) && submitForm('EvasioneClienteEdicolaForm'));},50);" />
			</div>
			<s:if
				test="#request.hasGestioneWebClienti eq true && #request.emailClienteValido eq true">
				<div style="width: 100%; text-align: center">
					<s:text name="dpe.invia.email.conferma" />
					&nbsp;&nbsp;&nbsp;
					<s:checkbox name="inviaEmail" id="inviaEmail"
						cssClass="tableFields" value="true" />
				</div>
			</s:if>
			<s:hidden id="qtaDaEvadere" name="qtaDaEvadere" />
		</s:form>
		<div id="one"></div>
	</s:if>
	<s:else>
		<div class="tableFields"
			style="width: 100%; align: center; text-align: center; margin-top: 50px">
			<s:text name="igeriv.nessun.risultato" />
		</div>
	</s:else>
</div>