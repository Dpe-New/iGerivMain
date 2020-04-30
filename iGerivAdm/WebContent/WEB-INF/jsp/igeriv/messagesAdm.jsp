<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.messaggi != null && !#request.messaggi.isEmpty()}">
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<s:form id="MessaggiForm" action="msgDpe_showMessages.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
<div style="width:100%">		 
		<dpe:table
			tableId="MessaggiTab"
			items="messaggi"
			var="messaggi" 
			action="${pageContext.request.contextPath}${ap}/msgDpe_showMessages.action"
			imagePath="/app_img/table/*.gif"		
			rowsDisplayed="1000"			
			view="buttonsOnBottom"
			locale="${localeString}"
			state="it.dpe.igeriv.web.actions.MessagesDpeAction"
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
				fileName="messaggi_dpe.pdf"         
				tooltip="plg.esporta.pdf" 
				headerTitle="igeriv.messaggi" 
				headerColor="black"         
				headerBackgroundColor="#b6c2da"    
				isLandscape="true"
			/>
			<ec:exportXls     
				fileName="messaggi_dpe.xls"      
				tooltip="plg.esporta.excel"/>		
			<dpe:row highlightRow="true" interceptor="marker" style="height:30px; cursor:pointer" href="#?codMessaggio=codice">					
				<ec:column property="data" cell="date" format="dd/MM/yyyy HH:mm:ss" width="8%" title="igeriv.data.messaggio" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
				<ec:column property="titoloShort" width="20%" title="label.print.Table.Title" filterable="false"/>  				
				<ec:column property="messaggioShort" width="40%" title="dpe.contact.form.message" filterable="false"/> 
				<ec:column property="urlShort" width="20%" title="dpe.url" filterable="false"  style="text-align:center" headerStyle="text-align:center"/> 
				<ec:column property="priorita" width="5%" title="igeriv.tipo.messaggio" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
				<ec:column property="abilitatoDesc" width="5%" title="igeriv.abilitato" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
			</dpe:row>
		</dpe:table>
</div>		
</s:form>
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato"/>
	</div>
</s:else>


