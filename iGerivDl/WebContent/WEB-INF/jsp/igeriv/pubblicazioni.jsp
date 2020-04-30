<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv">
<s:set var="showExports" value="%{'true'}" />
<s:if test="actionName != null && (actionName.contains('rifornimenti') || actionName.contains('variazioni'))">
	<s:set var="href" value="%{'#?idtn=idtn&periodicita=periodicitaPk&coddl=coddl'}" />	
</s:if>
<s:elseif test="actionName != null && (actionName.contains('prenotazioneClienti'))">
	<s:set var="href" value="%{'#?idtn=idtn&cpu=codicePubblicazione&coddl=coddl'}" />
</s:elseif>
<s:else>                             
	<s:set var="href" value="%{'#?cpu=codicePubblicazione&periodicita=periodicitaPk&coddl=coddl&crivw=crivw'}" />
	<s:set var="showExports" value="%{'false'}" />
</s:else>


	
<s:if test="%{#request.listPubblicazioni != null && !#request.listPubblicazioni.isEmpty()}">
	<s:set name="an" value="%{actionName}"/>
	<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
	<s:if test='#context["struts.actionMapping"].namespace == "/"'>
		<s:set name="ap" value="" />
	</s:if>	
	<s:form id="PubblicazioniDetailForm" action="%{actionName}" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">	
		<div style="width:100%">					
			<dpe:table
				tableId="PubblicazioniTab" 
				items="listPubblicazioni"
				var="listPubblicazioni" 
				action="${pageContext.request.contextPath}${ap}/${an}"						
				imagePath="/app_img/table/*.gif"				
				style="table-layout:auto; height: 350px"		
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.PubblicazioniAction"
				styleClass="extremeTableFields"	
				form="PubblicazioniDetailForm"		
				theme="eXtremeTable bollaScrollDiv"			
				showPagination="false"
				id="PubblicazioniScrollDiv"
				toolbarClass="eXtremeTable"
				showStatusBar="false"
				showTitle="false"
				showTooltips="false"
				footerStyle="height:30px;"
				filterable="false"
				showExports="true"
				>
				<dpe:exportPdf         
					fileName="pubblicazioni.pdf"         
					tooltip="plg.esporta.pdf" 
					headerTitle="igeriv.pubblicazioni" 
					headerColor="black"         
					headerBackgroundColor="#b6c2da"    
					isLandscape="true"
				/> 
				<ec:exportXls     
					fileName="pubblicazioni.xls"      
					tooltip="plg.esporta.excel"/>		
				<dpe:row highlightRow="true" interceptor="marker" style="height:30px; cursor:pointer" href="${href}">				
					<ec:column property="titolo" width="18%" title="igeriv.titolo" filterable="false"/>         
					<ec:column property="sottoTitolo" width="18%" title="igeriv.sottotitolo" filterable="false"/> 
					<ec:column property="codicePubblicazione" width="4%" title="igeriv.codice.pubblicazione.title" filterable="false" style="text-align:center"/>
					<ec:column property="numeroCopertina" width="5%" title="igeriv.numero" filterable="false" style="text-align:center"/>
					<ec:column property="argomento" width="18%" title="igeriv.argomento" filterable="false"/>
					<ec:column property="periodicita" width="12%" title="igeriv.periodicita" filterable="false"/>
					<ec:column property="prezzoCopertina" width="5%" title="igeriv.prezzo.lordo" cell="currency" filterable="false" style="text-align:right"/>
					<ec:column property="prezzoEdicola" width="5%" title="igeriv.prezzo.netto" cell="currency" filterable="false" style="text-align:right"/>              
					<dpe:column property="dataUscita" width="8%" title="igeriv.data.uscita" cell="date" dateFormat="dd/MM/yyyy" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
<%-- 					<ec:column property="note" width="4%" title="igeriv.fondo.bolla" filterable="false"/>                    --%>
					<ec:column property="immagine" title="igeriv.img" filterable="false" viewsDenied="pdf,xls" width="7%" sortable="false">
						<a href="/immagini/${pageScope.listPubblicazioni.immagine}" rel="thumbnail"><img src="/app_img/camera.gif" width="15px" height="15px" border="0" style="border-style:none"/></a>
					</ec:column>   	
				</dpe:row>
			</dpe:table>
<%-- 			<s:iterator value="listPubblicazioni"> --%>
<%-- 				<input type="hidden" name="noteRivendita" id="noteRivendita<s:property value='idtn'/>" value="<s:property value='note'/>" /> --%>
<%-- 				<input type="hidden" name="noteRivenditaCpu" id="noteRivenditaCpu<s:property value='codicePubblicazione'/>" value="<s:property value='noteByCpu'/>" /> --%>
<%-- 			</s:iterator> --%>
		</div>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:property value="nessunRisultato"/>
	</div>
</s:else>
</div>