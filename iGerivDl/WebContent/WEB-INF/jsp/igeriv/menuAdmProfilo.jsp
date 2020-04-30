<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:form id="EditProfiliForm" action="gestioneProfili_saveProfilo.action" method="POST" theme="igeriv" validate="false" onsubmit="return (ray.ajax())">			
	<div id="mainDiv">	
		<fieldset class="filterBolla" style="text-align:left;"><legend style="font-size:100%"><s:text name="tableTitle"/></legend>
			<div class="tableFields" style="float:left; width: 500px">
				<div style="float:left; width:120px"><s:text name="label.print.Table.Title" /></div>
				<div style="float:left; color:black"><s:textfield label="" name="profilo.titolo" id="titolo" cssStyle="width:300px; text-transform:uppercase;" cssClass="tableFields"/></div>
			</div>
			<div class="tableFields" style="float:left; width: 500px">
				<div style="float:left; width:120px"><s:text name="igeriv.descrizione" /></div>
				<div style="float:left; color:black"><s:textfield label="" name="profilo.descrizione" id="descrizione" cssStyle="width:300px; text-transform:uppercase;" cssClass="tableFields"/></div>
			</div>
		</fieldset>		
		<div style="text-align:center; margin-left:auto; margin-right:auto; width:400px; margin-top:20px">
			<div width:100px; margin-top:0px;"><input type="button" name="igeriv.memorizza" id="memorizza" value="<s:text name='igeriv.memorizza'/>" align="center" class="tableFields" style="text-align:center; width:100px" onclick="javascript: ray.ajax(); return setFormAction('EditProfiliForm','gestioneProfili_saveProfilo.action', '', '', false, '', function() {unBlockUI(); reloadProfili(); doCloseLayer();}, function() {return validateMenuProfilo();}, false, '');"/></div>						
		</div>			
	</div>		
	<s:hidden name="profilo.id"/>
</s:form>