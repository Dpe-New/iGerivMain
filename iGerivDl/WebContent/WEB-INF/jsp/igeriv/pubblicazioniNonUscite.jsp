<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv">
<s:if test="%{#request.itemsNonUscite != null && !#request.itemsNonUscite.isEmpty()}">
	<s:form id="PubblicazioniNonUsciteDetailForm" action="pubblicazioniNonUscite_showElencoPubblicazioni.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">		
		<div style="width:100%">					
		<dpe:table
				tableId="PubbNonUsciteTable" 
				items="itemsNonUscite"
				var="itemsNonUscite" 
				action="pubblicazioniNonUscite_showElencoPubblicazioni.action"
				imagePath="/app_img/table/*.gif"
				style="table-layout:fixed;"		 
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.PubblicazioniNonUsciteAction"
				styleClass="extremeTableFieldsSmaller"
				form="PubblicazioniNonUsciteDetailForm"
				theme="eXtremeTable bollaScrollDivLarge"			
				showPagination="false"
				id="PubbNonUsciteDiv"
				toolbarClass="eXtremeTable"
				footerStyle="height:50px;"
				filterable="false"	
				showStatusBar="false"
				autoIncludeParameters="false"
				sortable="true"
				>
				<dpe:exportPdf         
					fileName="pubblicazioni_non_uscite.pdf"         
					tooltip="plg.esporta.pdf" 
					headerTitle="igeriv.edicole" 
					headerColor="black"         
					headerBackgroundColor="#b6c2da"    
					isLandscape="true"
				/>
				<ec:exportXls     
					fileName="pubblicazioni_non_uscite.xls"      
					tooltip="plg.esporta.excel"/>
				<dpe:row highlightRow="true" interceptor="marker" href="#?idtn=idtn&periodicita=periodicitaPk&coddl=pk.codFiegDl" style="height:35px">					
					<ec:column property="cpuDl" width="5%" title="igeriv.cpu" filterable="false" style="text-align:center" headerStyle="text-align:center"/>         
					<ec:column property="titolo" width="30%" title="igeriv.titolo" filterable="false" />         
					<dpe:column property="numeroPubblicazione" width="10%" title="igeriv.numero" filterable="false" escapeAutoFormat="true" style="text-align:center" headerStyle="text-align:center"/>
					<dpe:column property="storicoCopertineVo.dataUscita" alias="dataUscita" width="10%" title="igeriv.data.uscita" cell="date" format="dd/MM/yyyy" style="text-align:center" headerStyle="text-align:center" exportStyle="text-align:center"/>
					<dpe:column property="pubblicazioneNonUscita" width="5%" title="igeriv.pubbNonUscite" cell="pubbNonUsciteSpunta" pkName="pk" filterable="false" escapeAutoFormat="true" style="text-align:center" headerStyle="text-align:center" value="pk" sortable="false"/>
				</dpe:row>
			</dpe:table>
		</div>
		<div style="width:100%;">						
			<div style="text-align:center;"><input type="button" value="<s:text name='igeriv.memorizza'/>" name="igeriv.memorizza" id="memorizza" style="width:100px; text-align:center" onclick="javascript: (setFormAction('PubblicazioniNonUsciteDetailForm','pubblicazioniNonUscite_save.action', '', 'messageDiv'));"/></div>
		</div>
		<s:hidden name="selectedDataTipoBolla"/>	
	</s:form>
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:property value="nessunRisultato"/>
	</div>
</s:else>
</div>