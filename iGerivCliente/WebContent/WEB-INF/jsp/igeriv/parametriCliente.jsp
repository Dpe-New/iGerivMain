<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
		div#filter { height:0px;} 
		div#content1 { height:550px;} 
</style> 
<s:actionerror/>
<s:actionmessage/>
<s:form id="ParamEdicolaForm" action="params_saveParam.action" enctype="multipart/form-data" theme="igeriv" method="POST" validate="false">			
	<div id="mainDiv" style="width:100%;">
		<div id="mainDiv" style="width:80%; height:520px; overflow-x:hidden; overflow-y:scroll; margin-left:auto; margin-right:auto; ">	
			<fieldset style="text-align:left; height:760px; width:88%;"><legend style="font-size:100%"><s:text name="igeriv.params.edicola"/></legend>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.edicola.1" /></div>
					<div style="float:left; width:48%"><s:textfield value="%{authUser.email}" accesskey="%{#session.paramEdicola1.sqlType}" id="paramEdicola1" cssStyle="width:350px" cssClass="tableFields" disabled="true"/><img id="changeImg" src="/app_img/modify.png" style="vertical-align:top; cursor:pointer" alt="<s:text name="msg.email.lettore" />" border="0" title="<s:text name="igeriv.modifica"/>&nbsp;<s:text name="msg.email.lettore" />" /></div>
				</div>
				<br/>
				<br/>
				<br/>
				<div align="center" style="width:100%">
						<a href="<s:text name='#session["url_Pdf_Privacy"]'/>" target="_new"><span style="color: black;">
						<s:text name="igeriv.client.pdf.privacy" /></span></a>
						</b></font>
				</div>
				
				
			</fieldset>		
		</div>	
	</div>	
	<s:hidden name="codParametro" id="codParametro"/>
	<s:hidden name="valParametro" id="valParametro"/>
</s:form>


