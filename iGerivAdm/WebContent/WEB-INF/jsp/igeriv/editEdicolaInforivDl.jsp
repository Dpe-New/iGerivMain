<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set var="disabledCond" value="edicola.codEdicolaWeb != null && edicola.codEdicolaWeb > 0"/>
<s:form id="EdicolaInforivDlForm" action="edicoleInforivDl_saveEdicolaInforivDl.action" method="POST" validate="false">			
	<div id="mainDiv" style="width:100%; overflow-x:hidden; overflow-y:scroll; font-size:14px;">
		<fieldset style="margin-left:0px; text-align:left; width:100%;"><legend style="font-size:100%"><s:if test="edicola.codEdicolaWeb != null && edicola.codEdicolaWeb > 0"><s:text name="igeriv.visualizza.edicola.inforiv.dl"/></s:if><s:else><s:text name="igeriv.inserisci.nuova.edicola.inforiv.dl"/></s:else></legend>
			<div style="float:left; width:100%">
				<div style="float:left; width:20%"><s:text name="igeriv.dl.1" /></div>
				<div style="float:left; width:25%"><s:select name="edicola.codDl" id="codDl" list="#session.tutti_dl" listKey="key" listValue="value" cssClass="tableFields" cssStyle="width:200px" emptyOption="true" disabled="#disabledCond"/></div>
				<div style="margin-left:10px; float:left; width:20%"><s:text name="igeriv.cod.edicola.dl.1" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.codEdicolaDl" id="codEdicolaDl" cssClass="tableFields" cssStyle="width:200px" maxlength="5" disabled="#disabledCond"/></div>
			</div>
			<div style="float:left; width:100%">
				<div style="float:left; width:20%"><s:text name="igeriv.dl.2" /></div>
				<div style="float:left; width:25%"><s:select name="edicola.codDl2" id="codDl2" list="#session.tutti_dl" listKey="key" listValue="value" cssClass="tableFields" cssStyle="width:200px" emptyOption="true" disabled="#disabledCond"/></div>
				<div style="float:left; margin-left:10px; width:20%"><s:text name="igeriv.cod.edicola.dl.2" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.codEdicolaDl2" id="codEdicolaDl2" cssClass="tableFields" cssStyle="width:200px" maxlength="5" disabled="#disabledCond"/></div>
			</div>
			<div style="float:left; width:100%">
				<div style="float:left; width:20%"><s:text name="igeriv.dl.3" /></div>
				<div style="float:left; width:25%"><s:select name="edicola.codDl3" id="codDl3" list="#session.tutti_dl" listKey="key" listValue="value" cssClass="tableFields" cssStyle="width:200px" emptyOption="true" disabled="#disabledCond"/></div>
				<div style="float:left; margin-left:10px; width:20%"><s:text name="igeriv.cod.edicola.dl.3" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.codEdicolaDl3" id="codEdicolaDl3" cssClass="tableFields" cssStyle="width:200px" maxlength="5" disabled="#disabledCond"/></div>
			</div>
			<div style="float:left; width:100%">
				<div style="float:left; width:20%"><s:text name="dpe.rag.sociale" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.ragioneSociale1" id="ragioneSociale1" cssClass="tableFields" cssStyle="width:200px" maxlength="32" disabled="#disabledCond"/></div>
				<div style="float:left; margin-left:10px; width:20%"><s:text name="dpe.rag.sociale.2" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.ragioneSociale2" id="ragioneSociale2" cssClass="tableFields" cssStyle="width:200px" maxlength="32" disabled="#disabledCond"/></div>
			</div>
			<div style="float:left; width:100%">
				<div style="float:left; width:20%"><s:text name="dpe.indirizzo" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.indirizzo" id="indirizzo" cssClass="tableFields" cssStyle="width:200px" maxlength="32" disabled="#disabledCond"/></div>
				<div style="float:left; margin-left:10px; width:20%"><s:text name="dpe.localita" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.localita" id="localita" cssClass="tableFields" cssStyle="width:200px" maxlength="32" disabled="#disabledCond"/></div>
			</div>
			<div style="float:left; width:100%">
				<div style="float:left; width:20%"><s:text name="dpe.provincia" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.provincia" id="provincia" cssClass="tableFields" cssStyle="width:200px" maxlength="3" disabled="#disabledCond"/></div>
				<div style="float:left; margin-left:10px; width:20%">&nbsp;</div>
				<div style="float:left; width:25%">&nbsp;</div>
			</div>
			<div style="float:left; width:100%">
				<div style="float:left; width:20%"><s:text name="dpe.email" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.email" id="email" cssClass="tableFields" cssStyle="width:200px" maxlength="50" disabled="#disabledCond"/></div>
				<div style="float:left; margin-left:10px; width:20%"><s:text name="dpe.cap" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.cap" id="cap" cssClass="tableFields" cssStyle="width:200px" maxlength="6" disabled="#disabledCond"/></div>
			</div>
			<div style="float:left; width:100%">
				<div style="float:left; width:20%"><s:text name="dpe.telefono" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.telefono" id="telefono" cssClass="tableFields" cssStyle="width:200px" maxlength="24" disabled="#disabledCond"/></div>
				<div style="float:left; margin-left:10px; width:20%"><s:text name="dpe.fax" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.fax" id="fax" cssClass="tableFields" cssStyle="width:200px" maxlength="24" disabled="#disabledCond"/></div>
			</div>
			<div style="float:left; width:100%">
				<div style="float:left; width:20%"><s:text name="dpe.piva" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.piva" id="piva" cssClass="tableFields" cssStyle="width:200px" maxlength="11" disabled="#disabledCond"/></div>
				<div style="float:left; margin-left:10px; width:20%"><s:text name="dpe.codice.fiscale.3" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.codFiscale" id="codFiscale" cssClass="tableFields" cssStyle="width:200px" maxlength="25" disabled="#disabledCond"/></div>
			</div>
			<div style="float:left; width:100%">
				<div style="float:left; width:20%"><s:text name="igeriv.latitudine" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.latitudine" id="latitudine" cssClass="tableFields" cssStyle="width:200px" maxlength="19" disabled="#disabledCond"/></div>
				<div style="float:left; margin-left:10px; width:20%"><s:text name="igeriv.longitudine" /></div>
				<div style="float:left; width:25%"><s:textfield name="edicola.longitudine" id="longitudine" cssClass="tableFields" cssStyle="width:200px" maxlength="19" disabled="#disabledCond"/></div>
			</div>
		</fieldset>		
		<s:if test="edicola == null || edicola.codEdicolaWeb == null || edicola.codEdicolaWeb == 0">
			<div style="width:100%">
				<div style="text-align:center"><input type="button" value="<s:text name='igeriv.memorizza'/>" name="igeriv.memorizza" id="memorizza" style="width:100px; text-align:center" onclick="doSubmitEdit();"/></div>  
			</div>	
		</s:if>
	</div>	
	<s:hidden name="edicola.codEdicolaWeb" />
</s:form>
