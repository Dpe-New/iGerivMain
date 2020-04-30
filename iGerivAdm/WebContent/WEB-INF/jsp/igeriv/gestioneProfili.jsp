<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:form id="EditProfiloForm" action="gestioneProfili_saveProfilo.action" method="POST" theme="simple" onsubmit="return (ray.ajax())" validate="false">			
	<div id="mainDiv" style="width:100%">	
		<fieldset class="filterBolla" style="width:600px; text-align:left;"><legend style="font-size:100%"><s:property value="tableTitle" /></legend>
		<div style="width:500px; margin-top:30px; margin-left:20px; float:left">	
				<div style="float:left; width:180px; font-size:14; margin-top:0px;">
					<s:select label=""
					    name="moduli"
					    id="moduli" 
					    listKey="id" 
					    listValue="titolo"
					    list="moduli"
					    emptyOption="false" 
					    cssStyle="width: 250px"
					    cssClass="tableFields"
					    mutliple="true"
					    size="9"
					    />
				</div>		
				<div style="float:left;  width:50px; margin-left:70px; margin-top:30px">
					<div style="float:none;">
						<img id="arrowLeft" src="/app_img/button-arrows-l.png" style="border-style: none"/>
					</div>	
					<div style="float:none;">
						<img id="arrowRight" src="/app_img/button-arrows-r.png" style="border-style: none"/>
					</div>
				</div>				
				<div style="float:right; width:180px; font-size:14;">
					<select name="moduliSelected" size="9" id="moduliSelected" class="tableFields" style="width: 250px" multiple="multiple">
						<s:iterator value="%{profilo.moduli}" status="status">													
							<option value="<s:property value='id'/>"><s:property value='titolo'/></option>												
						</s:iterator>
					</select>					
				</div>															
		</div>			
		<div style="width:500px; margin-top:80px;">
			<div style="float:left; width:400px; margin-top:0px">				
				<div style="width:600px; margin-top:30px; text-align:center">
					<s:submit name="igeriv.memorizza" id="memorizza" key="igeriv.memorizza" align="center" cssStyle="text-align:center; width:100px"/>
				</div>						
			</div>			
		</div>
	</div>	
	<s:hidden name="profilo.id"/>
</s:form>