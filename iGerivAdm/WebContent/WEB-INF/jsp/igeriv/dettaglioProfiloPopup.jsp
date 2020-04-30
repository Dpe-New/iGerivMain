<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>

<style>
<!--
td, th {
    padding: 2px;
}
.fieldsetDettProf {
    font-size: 90%;
    min-width: 400px; 
    margin-right: auto;
}
.alignRight {
    text-align: right;
}
.alignLeft {
    text-align: left;
}
-->
</style>


<div id="contentDiv">
<s:if test="%{#request.dettProfiloEdicola != null}">
	
	<s:form id="filterForm_info_generali" action="profilazione_showEdicole.action" method="POST" theme="simple" validate="false" onsubmit="return (doSubmit() && ray.ajax())">	
		<fieldset class="fieldsetDettProf" style="text-align: left; width: 100%">
			<legend style="font-size: 100%">INFO</legend>
				<div class="form-group">
					<table style="padding: 10px;">
						<tr>
							<td width="20%" style="text-align: right;"><strong><s:text name="dpe.nome.dl" /></strong></td>
							<td width="40%" style="text-align: left;"><s:property value="dettProfiloEdicola.anagraficaAgenziaVo.ragioneSocialeDlPrimaRiga"/></td>
							<td width="20%" style="text-align: right;"><strong><s:text name="igeriv.richiesta.rifornimenti.data.inserimento" /></strong></td>
							<td width="20%" style="text-align: left;"><input type="text" id="dett_dataInserimento" name="dett_dataInserimento" class="form-control input-sm" value="<s:property value="dettProfiloEdicola.dataInserimento_format"/>" /></td>
						</tr>
						<tr>
							<td class="alignRight"><strong><s:text name="dpe.login.dl.fieg.code.short" /></strong></td>
							<td class="alignLeft"><s:property value="dettProfiloEdicola.codEdicolaDl"/></td>
							<td class="alignRight"><strong><s:text name="igeriv.data.sospensione" /></strong></td>
							<td class="alignLeft"><input type="text" id="dett_dataSospensione" name="dett_dataSospensione" class="form-control input-sm" value="<s:property value="dettProfiloEdicola.dataSospensione_format"/>" />
							</td>
						</tr>
						<tr>
							<td class="alignRight"><strong><s:text name="dpe.login.dl.web.code.short" /></strong></td>
							<td><s:property value="dettProfiloEdicola.codDpeWebEdicola"/></td>
							<td class="alignRight">&nbsp;</td>
							<td colspan="1" align="center" style="vertical-align: top;">
									<input id="button_aggiorna_date" class="btn btn-primary" type="button" value="AGGIORNA DATE">
							</td>
						</tr>
						<tr>
							<td class="alignRight"><strong><s:text name="dpe.rag.sociale" /></strong></td>
							<td class="alignLeft"><s:property value="dettProfiloEdicola.anagraficaEdicolaVo.ragioneSocialeEdicolaPrimaRiga"/>&ensp;
				      			<s:property value="dettProfiloEdicola.anagraficaEdicolaVo.ragioneSocialeEdicolaSecondaRiga"/></td>
				      		<td class="alignRight">&nbsp;</td>
				      		<td class="alignLeft">&nbsp;</td>
						</tr>

					</table>
		    	</div>
		</fieldset>
		<input type="hidden" id="codEdicolaWeb_hid" name="codEdicolaWeb_hid" value="<s:property value="dettProfiloEdicola.codDpeWebEdicola"/>"/>
		<input type="hidden" id="codFiegDl_hid" name="codFiegDl_hid" value="<s:property value="dettProfiloEdicola.anagraficaAgenziaVo.codFiegDl"/>"/>
		
		
		
	</s:form>
	
	<s:form id="filterForm_aggiungi_profilazione" action="profilazione_showEdicole.action" method="POST" theme="simple" validate="false" onsubmit="return (doSubmit() && ray.ajax())">	
		<fieldset class="fieldsetDettProf" style="text-align: left; width: 100%">
			<legend style="font-size: 100%">AGGIUNGI PROFILAZIONE</legend>
			<div class="form-group">
				<table style="padding: 10px;">

						<tr>
							<td width="10%"><strong><s:text name="igeriv.tipo.profilo" /></strong></td>
							<td width="40%"><select id="dett_select_profili" name="dett_select_profili" class="form-control input-sm"></select></td>
							<td></td>
							<td><strong><s:text name="igeriv.edicola.profilo.test" /></strong> 
									<input type="checkbox" id="dett_flag_test" name="dett_flag_test"/>
									</td>
						</tr>
						<tr>
							<td width="10%"><strong><s:text name="igeriv.edicola.profilo.date.attivita" /></strong></td>
							<td width="40%"><input type="text" id="dett_data_da" class="form-control input-sm" placeholder="Data inizio validit&agrave;"></td>
							
							<td width="10%"></td>
							<td width="40%">
									<strong><s:text name="igeriv.edicola.profilo.promo" /></strong>
										<input type="checkbox" id="dett_flag_promo" name="dett_flag_promo" />
									<strong><s:text name="igeriv.edicola.profilo.plus" /></strong>
										<input type="checkbox" id="dett_flag_plus" name="dett_flag_plus" />
							</td>
						
						</tr>
						<tr>
							<td width="10%"></td>
							<td width="40%"><input type="text" id="dett_data_a" class="form-control input-sm" placeholder="Data fine validit&agrave;"></td>
							<td width="10%"></td>
							<td width="40%"><input id="button_aggiungi_profilazione" class="btn btn-primary" type="button" value="AGGIUNGI PROFILAZIONE"></td>
						
						</tr>
<!-- 						<tr> -->
<!-- 						<td colspan="4" align="center"><input id="button_aggiungi_profilazione" class="btn btn-primary" type="button" value="AGGIUNGI PROFILAZIONE"></td> -->
<!-- 						</tr> -->
						
				</table>
	    </div>
		</fieldset>
	</s:form>
	<s:form id="filterForm_dettaglio_pianificazione" action="profilazione_showEdicole.action" method="POST" theme="simple" validate="false" onsubmit="return (doSubmit() && ray.ajax())">	
		<fieldset class="fieldsetDettProf" style="text-align: left; width: 100%">
			<legend style="font-size: 100%">DETTAGLIO PIANIFICAZIONE PROFILO</legend>
		
					<dpe:table
						tableId="dettaglioPianoProfilazione"
						items="listPianoProfiliEdicola"
						var="listPianoProfiliEdicola" 
						action="${pageContext.request.contextPath}${ap}/profilazione_showEdicole.action"
						imagePath="/app_img/table/*.gif"
						rowsDisplayed="1000"			
						view="buttonsOnBottom"
						locale="${localeString}"
						state="it.dpe.igeriv.web.actions.ProfilazioneAdmAction"
						styleClass="extremeTableFields" 
						form="formDettaglioPianoProfilazione"
						theme="eXtremeTable"			
						showPagination="false"
						id="listPianoProfiliEdicola"
						toolbarClass="eXtremeTable"
						footerStyle="height:30px;"
						filterable="false"
						autoIncludeParameters="false"
						>
	
				<dpe:row style="height:30px">					
					<ec:column 	property="codDpeWebEdicola" 			width="10%" title="dpe.login.dl.web.code.short" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<dpe:column property="dtAttivazioneProfiloEdicola" 	cell="date" width="10%" title="igeriv.richiesta.rifornimenti.data.inserimento" dateFormat="dd/MM/yyyy"	filterable="false" style="text-align:center" headerStyle="text-align:center" />
					<dpe:column property="dtSospensioneProfiloEdicola" 	cell="date" width="10%" title="igeriv.richiesta.rifornimenti.data.inserimento" dateFormat="dd/MM/yyyy"	filterable="false" style="text-align:center" headerStyle="text-align:center" />
					<ec:column 	property="codiceGruppoVo.titolo"    	width="10%" title="dpe.login.dl.web.code.short" filterable="false" style="text-align:left" headerStyle="text-align:center"/>
					<dpe:column property="edicolaTest" 	cell="boolean" width="5%" title="igeriv.edicola.profilo.test" filterable="false" />
					<dpe:column property="edicolaPromo" cell="boolean" width="5%" title="igeriv.edicola.profilo.promo" filterable="false" />
					<dpe:column property="edicolaPlus" 	cell="boolean" width="5%" title="igeriv.edicola.profilo.plus" filterable="false" />
				</dpe:row>
			</dpe:table>
		</fieldset>
	</s:form>
</s:if>

</div>


