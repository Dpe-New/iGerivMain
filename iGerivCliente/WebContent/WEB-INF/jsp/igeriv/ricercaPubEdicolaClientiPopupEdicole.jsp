<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv">
<div id="mainDiv" style="width:100%; margin-top:10px;">		
	<div class="tableFields" id="pubblicazioneDiv" style="font-size:12px; width:100%; text-align:left; font-weight:bold; float:left;">
		<div style="float:left;"><s:text name="igeriv.titolo"/>:&nbsp;&nbsp;&nbsp;<span style="color: black;"><s:text name="#request.pubblicazione.titolo"/></span></div>
		<div style="float:left; margin-left:50px;"><s:text name="igeriv.sottotitolo"/>:&nbsp;&nbsp;&nbsp;<span style="color: black;"><s:text name="#request.pubblicazione.sottoTitolo"/></span></div>
		<br />
		<div style="float:left;"><s:text name="igeriv.numero"/>:&nbsp;&nbsp;&nbsp;<span style="color: black;"><s:text name="#request.pubblicazione.numeroCopertina"/></span></div>
		<div style="float:left; margin-left:50px;"><s:text name="igeriv.prezzo"/>:&nbsp;&nbsp;&nbsp;<span style="color: black;"><s:text name="#request.pubblicazione.prezzoCopertinaFormat"/></span></div>
		<div style="float:left; margin-left:50px;"><s:text name="igeriv.data.uscita"/>:&nbsp;&nbsp;&nbsp;<span style="color: black;"><s:text name="#request.pubblicazione.dataUscitaFormat"/></span></div>
	</div>			
</div>
<br /><br />
<s:if test="%{#request.listEdicole != null && !#request.listEdicole.isEmpty()}">
	<s:form id="EdicoleDetailForm" action="%{actionName}" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
		<div style="width:100%;">					
			<dpe:table
				tableId="EdicoleTab" 
				items="listEdicole"
				var="listEdicole" 
				action="pubblicazioniClienti_showPubblicazioneEdicola.action"		
				imagePath="/app_img/table/*.gif"				
				style="table-layout:auto; height: 250px"		
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.PubblicazioniClientiAction"
				styleClass="extremeTableFields"	
				form="EdicoleDetailForm"		
				theme="eXtremeTable bollaScrollDiv"			
				showPagination="false"
				id="EdicoleScrollDiv"
				toolbarClass="eXtremeTable"
				showStatusBar="false"
				showTitle="false"
				showTooltips="false"
				footerStyle="height:30px;"
				filterable="false"
				showExports="true"
				>
				<dpe:exportPdf         
					fileName="edicole.pdf"         
					tooltip="plg.esporta.pdf" 
					headerTitle="igeriv.edicole" 
					headerColor="black"         
					headerBackgroundColor="#b6c2da"    
					isLandscape="true"
				/> 
				<ec:exportXls     
					fileName="edicole.xls"      
					tooltip="plg.esporta.excel"/>		
				<dpe:row highlightRow="true" interceptor="marker" style="height:30px; cursor:pointer">				
					<ec:column property="anagraficaEdicolaVo.ragioneSocialeEdicolaPrimaRiga" width="24%" title="dpe.nome.edicola" filterable="false"/>         
					<ec:column property="anagraficaEdicolaVo.indirizzoEdicolaPrimaRiga" width="24%" title="dpe.indirizzo" filterable="false"/> 
					<ec:column property="anagraficaEdicolaVo.localitaEdicolaPrimaRiga" width="20%" title="dpe.localita" filterable="false"/>
					<ec:column property="anagraficaEdicolaVo.siglaProvincia" width="10%" title="dpe.provincia" filterable="false" style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="anagraficaEdicolaVo.cap" width="10%" title="dpe.cap" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<ec:column property="anagraficaEdicolaVo.telefono" width="10%" title="dpe.telefono" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
				</dpe:row>
			</dpe:table>
		</div>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>
</div>