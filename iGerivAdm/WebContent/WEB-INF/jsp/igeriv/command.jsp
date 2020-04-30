<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
		div#filter { height:0px;} 
		div#content1 { height:550px;} 
</style> 
<s:form id="CommandForm" action="command_esegui.action" method="POST" validate="false">			
	<div id="mainDiv" style="width:100%;">
		<div id="mainDiv" style="width:80%; height:500px; overflow-x:hidden; overflow-y:scroll; margin-left:auto; margin-right:auto; ">	
			<fieldset style="text-align:left; height:480px; width:88%;"><legend style="font-size:100%"><s:text name="igeriv.esecuzione.processi.batch"/></legend>
				<div style="margin-top:5px; width:100%">
					<div style="float:left; width:33%"><s:text name="igeriv.esegui.interfaccia.esportazione.prodotti.vari" /></div>
					<div style="float:left; width:33%">
						<span style="float:left; width:55%; height:100px;">
							<s:select label=""
							    name="codDl"
							    id="codDl" 
							    listKey="key" 
							    listValue="value"
							    list="%{#session['dl']}"
							    emptyOption="true" 
							    cssStyle="width:300px" 
							    cssClass="tableFields"
							/>	
						</span>
					</div> 
					<div style="float:left; width:32%"><input type="button" name="esegui" id="esegui" value="<s:text name="plg.esegui"/>" style="width:150px"/></div>
				</div>
			</fieldset>		
		</div>	
	</div>	
</s:form>

					
				