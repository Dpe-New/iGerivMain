<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
		div#filter { height:0px;} 
		div#content1 { height:580px;} 
</style> 
<s:form id="CommandForm" action="params_esegui.action" method="POST" validate="false">			
</s:form>
<s:form id="ParamAgenziaForm" action="params_saveParam.action" method="POST" validate="false">			
	<div id="mainDiv" style="width:100%;">
		<div id="mainDiv" style="width:80%; height:520px; overflow-x:hidden; overflow-y:scroll; margin-left:auto; margin-right:auto; ">	
			<fieldset style="text-align:left; height:760px; width:88%;"><legend style="font-size:100%"><s:text name="igeriv.params.agenzia"/></legend>
				<s:if test="hasInterfacciaJmsProdottiVari eq true">
					<div style="width:100%">
						<div style="float:left; width:50%"><s:text name="igeriv.esegui.interfaccia.esportazione.prodotti.vari" /></div>
						<div style="float:left; width:48%"><input type="button" name="esegui" id="esegui" value="<s:text name="plg.esegui"/>" style="width:150px"/></div>
					</div>
				</s:if>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.1" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.ragioneSocialeDlPrimaRiga" id="paramAgenzia1" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.2" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.ragioneSocialeDlSecondaRiga" id="paramAgenzia2" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.3" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.indirizzoDlPrimaRiga" id="paramAgenzia3" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.4" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.indirizzoDlSecondaRiga" id="paramAgenzia4" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.5" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.localitaDlPrimaRiga" id="paramAgenzia5" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.6" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.localitaDlSecondaRiga" id="paramAgenzia6" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.7" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.siglaProvincia" id="paramAgenzia7" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.8" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.cap" id="paramAgenzia8" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.9" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.telefono" id="paramAgenzia9" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.10" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.fax" id="paramAgenzia10" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.11" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.email" id="paramAgenzia11" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.22" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.url" id="paramAgenzia22" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="margin-top:5px; width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.12" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.ftpServerGestionaleAddress" id="paramAgenzia12" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.13" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.ftpServerGestionaleUser" id="paramAgenzia13" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.14" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.ftpServerGestionalePwd" id="paramAgenzia14" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.15" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.ftpServerGestionaleDir" id="paramAgenzia15" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.16" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.numMaxCpuResaDimeticata" id="paramAgenzia16" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.20" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.hasButtonCopiaDifferenze" id="paramAgenzia20" cssClass="tableFields"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.21" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.hasResaAnticipata" id="paramAgenzia21" cssClass="tableFields"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.17" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.visualizzaSemaforoGiacenza" id="paramEdicola17" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.18" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.emailReplyToInstantMessages" id="paramEdicola18" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.23" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.returnReceiptTo" id="paramEdicola23" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.19" /></div>
					<div style="float:left; width:48%"><s:select name="agenzia.giornoSettimanaPermessaResaDimenticata" id="paramAgenzia19" list="#application.giorniSettimana" listKey="keyInt" listValue="value" cssClass="tableFields" emptyOption="true" cssStyle="width:300px"/></div>
				</div>
			</fieldset>		
		</div>
		<div style="width:100%">
			<div style="text-align:center"><input type="button" value="<s:text name='igeriv.memorizza'/>" name="igeriv.memorizza" id="memorizza" style="width:100px; text-align:center" onclick="javascript: doSubmit();"/></div>  
		</div>	
	</div>	
	<s:hidden name="agenzia.codFiegDl" />
</s:form>
