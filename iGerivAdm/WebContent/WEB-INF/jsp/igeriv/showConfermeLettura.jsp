<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<div id="mainDocDiv">
	<div style="color: #444444;font-weight: bold;font-family: verdana, arial, helvetica, sans-serif;font-size: 15px; vertical-align: middle; text-align:center">
		<s:text name="filterTitle" />
	</div>
	<br>
	<s:if test="%{#request.conferme}">	
		<s:form id="ShowConfermeLetturaForm" action="messages_showConfermeLettura.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">			
			<div id="mainDiv1" style="width:600px">		
					<dpe:table
						tableId="ShowConfermeLetturaTab_table"
						items="conferme"
						var="conferme" 
						action="${pageContext.request.contextPath}${ap}/messages_showConfermeLettura.action"
						imagePath="/app_img/table/*.gif"				
						style="height:400px; width:600px;"
						rowsDisplayed="1000"
						view="buttonsOnBottom"
						locale="${localeString}"
						state="it.dpe.igeriv.web.actions.MessagesAction"
						styleClass="extremeTableFieldsSmall tablesorter"
						form="ShowConfermeLetturaForm"
						theme="eXtremeTable bollaScrollDivContentTall"			
						showPagination="false"
						id="BollaScrollDiv"
						toolbarClass="eXtremeTable"
						footerStyle="height:30px;"
						filterable="false"
						>
						<ec:exportPdf         
							fileName="conferme_lettura.pdf"         
							tooltip="plg.esporta.pdf" 
							headerTitle="${title}" 
							headerColor="black"         
							headerBackgroundColor="#b6c2da"    
						/>
						<ec:exportXls     
							fileName="conferme_lettura.xls"       
							tooltip="plg.esporta.excel"/>		
						<ec:row style="height:20px">			
							<ec:column property="codEdicola" width="10%" title="dpe.login.ed.user" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false"/>
							<ec:column property="ragioneSociale" width="30%" title="dpe.rag.sociale" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false"/>  
							<ec:column property="localita" width="20%" title="dpe.localita" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false"/>
							<ec:column property="messaggioLettoDesc" width="10%" title="igeriv.messaggio.letto" styleClass="extremeTableFieldsLarger" filterable="false" style="text-align:center" sortable="false"/>
						</ec:row>
					</dpe:table> 					
			</div>
		</s:form>	
	<div id="one"></div> 
	</s:if>
</div>	