<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:form id="EditProfiloForm" action="gestioneProfili_saveModuliProfilo.action" method="POST" theme="simple" onsubmit="return (ray.ajax())" validate="false">			
	<div id="mainDiv" style="width:100%">	
		<fieldset class="filterBolla" style="width:800px; text-align:left;"><legend style="font-size:100%"><s:property value="tableTitle" /></legend>
		<div style="width:780px; height:250px; margin-top:30px; margin-left:20px; float:left">	
				<div style="float:left; width:340px; height:200px; font-size:14px;">
					<select name="moduli" id="moduli" mutliple="true" size="12" style="width:300px; font-size:14px;"></select>
				</div>		
				<div style="float:left; width:35px; margin-left:0px; margin-top:50px;">
					<div style="float:left;">
						<img id="arrowRight" src="/app_img/button-arrows-r.png" style="border-style: none; cursor:pointer;" title="<s:text name="igeriv.aggiungi.menu"/>"/>
					</div>
					<div style="float:left;">
						<img id="arrowLeft" src="/app_img/button-arrows-l.png" style="border-style: none; cursor:pointer;" title="<s:text name="igeriv.togli.menu"/>"/>
					</div>	
				</div>				
				<div style="float:right; width:350px; height:200px; font-size:14px;">
					<select name="moduliSelected" size="12" id="moduliSelected" style="width:300px; font-size:14px;" multiple="multiple"></select>					
				</div>															
		</div>			
		<div style="width:800px; margin-top:80px;">
			<div style="float:left; width:780px; margin-top:0px">				
				<div style="width:780px; text-align:center">
					<input type="button" name="igeriv.memorizza" id="memorizza" value="<s:text name="igeriv.memorizza"/>" align="center" style="text-align:center; width:150px; font-size:14px;"/>
				</div>						
			</div>			
		</div>
	</div>	
	<s:hidden name="idProfilo" id="idProfiloH"/>
</s:form>
<!-- Right Click Menu -->
<ul id="myMenu" class="contextMenu">
	<li class="edit"><a href="#edit"><s:text name="igeriv.modifica"/></a></li>
	<li class="insert"><a href="#insert"><s:text name="plg.inserisci"/></a></li>
    <li class="delete"><a href="#delete"><s:text name="dpe.contact.form.elimina"/></a></li>        	             
</ul>
