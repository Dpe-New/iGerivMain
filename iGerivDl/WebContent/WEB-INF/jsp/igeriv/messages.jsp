<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
		div#footer { top:0px; }
</style>
<s:if test="%{#request.messaggi != null && !#request.messaggi.isEmpty()}">
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<s:form id="MessaggiForm" action="messages_showMessages.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
<div style="width:100%">		 
		<dpe:table
			tableId="MessaggiTab"
			items="messaggi"
			var="messaggi" 
			action="${pageContext.request.contextPath}${ap}/messages_showMessages.action"
			imagePath="/app_img/table/*.gif"		
			rowsDisplayed="1000"			
			view="buttonsOnBottom"
			locale="${localeString}"
			state="it.dpe.igeriv.web.actions.MessagesAction"
			styleClass="extremeTableFields" 
			form="MessaggiForm"
			theme="eXtremeTable bollaScrollDivLarge"			
			showPagination="false"
			id="MessaggiDiv"
			toolbarClass="eXtremeTable"
			footerStyle="height:30px;"
			filterable="false"
			>
			<dpe:exportPdf         
				fileName="messaggi.pdf"         
				tooltip="plg.esporta.pdf" 
				headerTitle="igeriv.messaggi" 
				headerColor="black"         
				headerBackgroundColor="#b6c2da"    
				isLandscape="true"
			/>
			<ec:exportXls     
				fileName="messaggi.xls"      
				tooltip="plg.esporta.excel"/>		
			<dpe:row highlightRow="true" interceptor="marker" style="height:30px; cursor:pointer" href="#?messagePk=pk">					
				<ec:column property="edicolaLabel" width="20%" title="igeriv.provenienza.evasione.edicola" filterable="false"/>
				<ec:column property="dtMessaggio" cell="date" format="dd/MM/yyyy HH:mm:ss" width="10%" title="igeriv.data.messaggio" filterable="false"/>  				
				<ec:column property="messaggioShort" width="35%" title="dpe.contact.form.message" filterable="false"/> 
				<ec:column property="statoMessaggioDesc" width="10%" title="igeriv.richiesta.rifornimenti.quantita.stato" filterable="false"/> 
				<ec:column property="tipoMessaggioDesc" width="10%" title="igeriv.tipo.messaggio" filterable="false"/>
				<ec:column property="attachmentName1" style="text-align:center" width="5%" title="igeriv.allegato1" filterable="false" sortable="false" viewsDenied="pdf,xls"/>  				
				<ec:column property="attachmentName2" style="text-align:center" width="5%" title="igeriv.allegato2" filterable="false" sortable="false" viewsDenied="pdf,xls"/> 
				<ec:column property="attachmentName3" style="text-align:center" width="5%" title="igeriv.allegato3" filterable="false" sortable="false" viewsDenied="pdf,xls"/> 
			</dpe:row>
		</dpe:table>
</div>		
</s:form>
</s:if>

