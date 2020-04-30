<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/rte_<s:text name="igeriv.version.timestamp"/>.css" />
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:form id="MessageForm" name="MessageForm" action="msgDpe_saveMessage.action" method="POST" validate="false" onsubmit="return (ray.ajax())">			
	<div id="mainDiv" style="width:100%">	
		<fieldset class="filterBolla" style="text-align:left; width:700px"><legend style="font-size:100%"><s:property value="tableTitle" /></legend>
			<div style="width:540px;">
				<div id="innerDiv"></div>											
			</div>	
			<div id="rivenditeDiv" class="required">
				<div style="float:left; width:150px;"><s:text name="label.print.Table.Title" /></div>
				<div><s:textfield name="messaggio.titolo" id="messaggio.titolo" cssClass="tableFields" cssStyle="width:450px"/></div>				
			</div>
			<div id="rivenditeDiv" class="required">
				<div style="float:left; width:150px;"><s:text name="dpe.url" /></div>
				<div><s:textfield name="messaggio.url" id="messaggio.url" cssClass="tableFields" cssStyle="width:450px"/></div>				
			</div>
			<div id="rivenditeDiv" class="required">
				<div style="float:left; width:150px;"><s:text name="igeriv.invia.notifica.dl" /></div>
				<div><s:checkbox name="messaggio.inviaNotificaDl" id="inviaNotificaDl" cssClass="tableFields" value="true"/></div>				
			</div>	
			<div id="rivenditeDiv" class="required">
				<div style="float:left; width:150px;"><s:text name="igeriv.attivo" /></div>
				<div><s:checkbox name="messaggio.abilitato" id="abilitato" cssClass="tableFields" value="true"/></div>				
			</div>
			<div id="rivenditeDiv" class="required">
				<div style="float:left; width:150px;"><s:text name="igeriv.tipo.messaggio" /></div>
				<div><s:select name="messaggio.priorita" id="messaggio.priorita" cssClass="tableFields" cssStyle="width:50px" list="#{'1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}"/></div>				
			</div>	
		</fieldset>	
		<div style="width:100%; text-align:center">	
			<input type="button" value="<s:text name='igeriv.memorizza'/>" name="igeriv.memorizza" id="memorizza" class="tableFields" style="width:100px; text-align:center" onclick="javascript: return (synchEditor() && setFormAction('MessageForm','msgDpe_saveMessage.action', '', ''));"/>
			<s:if test="messaggio.codice > 0">
				&nbsp;&nbsp;
				<input type="button" value="<s:text name='dpe.contact.form.reset'/>" name="dpe.contact.form.reset" id="cancella" class="tableFields" style="width:150px; text-align:center" onclick="javascript: if (window.confirm('<s:text name="gp.cancellare.dati"/>')) { setFormAction('MessageForm','msgDpe_deleteMessage.action', '', '') };"/>
			</s:if>
		</div>
	</div>		
	<s:hidden name="messaggio.codice"/>	
	<s:hidden name="messaggio.data"/>
	<input type="hidden" name="messaggio.testo" id="message" value="<s:property value="messageContent"/>"/>
</s:form>