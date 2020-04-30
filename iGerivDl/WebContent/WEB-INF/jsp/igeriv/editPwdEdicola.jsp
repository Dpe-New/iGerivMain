<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="templateSuffix" value="'ftl'" />
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<s:form id="EditPwdEdicolaForm" action="edicole_savePwdEdicola.action" namespace="/" method="POST" theme="igeriv" validate="false" onsubmit="return (ray.ajax())">			
	<div id="mainDiv" style="width:100%">
		<fieldset class="filterBolla" style="text-align:left; width:100%"><legend style="font-size:100%"><s:text name="tableTitle"/></legend>
			<div class="tableFields" style="float:left; width: 500px">
				<div style="float:left; width:200px"><s:text name="dpe.login.dl.fieg.code.short" /></div>
				<div style="float:left; width:200px; color:black"><s:text name="edicola.codEdicolaDl" /></div>
			</div>
			<div class="tableFields" style="float:left; width: 500px">
				<div style="float:left; width:200px"><s:text name="dpe.login.dl.web.code.short" /></div>
				<div style="float:left; width:200px; color:black"><s:text name="edicola.codEdicolaWeb" /></div>
			</div>
			<div class="tableFields" style="float:left; width: 500px">
				<div style="float:left; width:200px"><s:text name="dpe.rag.sociale" /></div>
				<div style="float:left; width:200px; color:black"><s:text name="edicola.ragioneSociale1" /></div>
			</div>
			<div class="tableFields" style="float:left; width: 500px">
				<div style="float:left; width:200px"><s:text name="igeriv.profilo.utente" /></div>
				<div style="float:left; width:200px; color:black"><s:select name="idGruppo" id="idGruppo" list="listModuliDl" listKey="id" listValue="titolo" style="width:280px"/></div>
			</div>
			<div class="tableFields" style="float:left; width: 500px">
				<div style="float:left; width:200px"><s:text name="password" /></div>
				<div style="float:left; width:200px; color:black"><s:textfield label="" name="edicola.password" id="password" cssStyle="width: 200px" cssClass="tableFields"/></div>
			</div>
			<div class="tableFields" style="float:left; width: 500px">
				<div style="float:left; width:200px"><s:text name="dpe.invia.email.conferma.credenziali" /></div>
				<div style="float:left; width:200px"><s:checkbox name="inviaEmail" id="inviaEmail" cssClass="tableFields" value="false"/></div>
			</div>
			<div class="tableFields" style="float:left; width: 500px">
				<div style="float:left; width:200px"><s:text name="Controllo Consegne Tessere Abbonamenti" /></div>
				<div style="float:left; width:200px"><s:checkbox name="edicola.checkConsegneGazzetta" id="checkConsegne" cssClass="tableFields" /></div>
			</div>
			<s:if test="%{agenziaDl.isValidDataSospensione()}">
				<div class="tableFields" style="float:left; width:500px">
					<div style="float:left; width:200px;"><s:text name="igeriv.data.sospensione" /></div>
					<div style="float:left; width:200px"><s:textfield name="edicola.dataSospensioneString" id="strData" cssStyle="width:100px;" disabled="false"/></div>	
				</div>
			</s:if>  
			
			
		</fieldset>
		<div style="text-align:center; margin-left:auto; margin-right:auto; width:400px; margin-top:20px">
			<div style="width:100%; margin-top:0px;"><input type="button" name="igeriv.memorizza" id="memorizza" value="<s:text name='igeriv.memorizza'/>" align="center" class="tableFields" style="text-align:center; width:100px" onclick="javascript: return setFormAction('EditPwdEdicolaForm','edicole_savePwdEdicola.action', '', '', false, '', onSaved, '', false, '');"/></div>						
		</div>
	</div>		
	<s:hidden name="edicola.codEdicolaWeb"/>
	<s:hidden name="edicola.email"/>
</s:form>