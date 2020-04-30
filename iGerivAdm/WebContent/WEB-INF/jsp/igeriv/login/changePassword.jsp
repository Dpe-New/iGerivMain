<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %> 
<%@ taglib uri="http://code.google.com/p/jcaptcha4struts2/taglib/2.0" prefix="jcaptcha" %>                     
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/validation.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/utils.js"></script>
<s:set name="templateSuffix" value="'ftl'" />
<s:form name="ChangePwdForm" id="ChangePwdForm" action="/changePwd_changeAdm.action" theme="igeriv" validate="true"
	onsubmit="return (ray.ajax())">	
	<s:hidden id="codEdicola" name="codEdicola"></s:hidden>	
	<s:hidden id="id" name="id"></s:hidden>	
	<fieldset><legend><s:text name="gp.gestione.sito"/> - <s:text name="gp.cambio.password" /></legend>	
		<div class="required" style="float:left">
			<div style="float:left; width:160px;">
				<label for="oldPassword"><s:text name="dpe.login.old.password"></s:text><em>*</em></label>
			</div> 
			<div style="float:left; width:160px;">
				<s:password id="oldPassword" name="oldPassword" />
			</div>
			<div style="float:left; width:160px;">
				<font color="red" size="2"><span id="err_oldPassword"><s:fielderror><s:param>oldPassword</s:param></s:fielderror></span></font>							
			</div>
		</div>						
		<div class="required" style="float:left">
			<div style="float:left; width:160px;">
				<label for="password"><s:text name="dpe.login.new.password"></s:text><em>*</em></label> 
			</div>
			<div style="float:left; width:160px;">
				<s:password id="password" name="password" />
			</div>
			<div style="float:left; width:160px;">
				<font color="red" size="2"><span id="err_password"><s:fielderror><s:param>password</s:param></s:fielderror></span></font>							
			</div>
		</div>	
		<div class="required" style="float:left">
			<div style="float:left; width:160px;">
				<label for="confermaPassword"><s:text name="dpe.login.confermapassword"></s:text><em>*</em></label> 
			</div>
			<div style="float:left; width:160px;">
				<s:password id="confermaPassword" name="confermaPassword" />
			</div>
			<div style="float:left; width:160px;">
				<font color="red" size="2"><span id="err_confermaPassword"><s:fielderror><s:param>confermaPassword</s:param></s:fielderror></span></font>							
			</div>
		</div>		
		<div class="required" style="float:left">			
			<div style="float:left; width:160px;">
				<br>
				<div style="font-weight:bold;"><s:text name="gp.captcha"/><em>*</em></div>
				<br>			
				<div class="textLinks"><a href="javascript: refreshimage();"><s:text name="igeriv.cambia.immagine"/></a></div> 					
			</div>		
			<div style="float:left">
				<img id="imgCaptcha" src="${pageContext.request.contextPath}/jcaptcha_image.action" width="170px" height="80px"/>	
				<div class="captchaError""><s:fielderror><s:param>jCaptchaResponse</s:param></s:fielderror></div>								
			</div>			
		</div>		
		<div class="required" style="float:left">
			<div style="float:left; width:160px;">
				<label for="jCaptchaResponse">&nbsp;</label> 	
			</div>	
			<s:textfield name="jCaptchaResponse" />							
		</div>
		<div class="submit" style="float:left">		
			<div style="width:16px;">
				&nbsp;
			</div>
			<div><s:submit key="dpe.contact.form.submit" align="center" cssStyle="width:100px"/></div>
			<div><input type="button" value="<s:text name='dpe.contact.form.torna.indietro'/>" align="center" cssStyle="width:150px" onclick="javascript: history.back();"/></div>
		</div>		
	</fieldset>
	<br>	
	<div style="color:red; margin-left:50px">		
		<s:property value="%{exception.message}"/>
	</div>	
	<input type="hidden" name="loginType" value="<s:text name='#context["struts.actionMapping"].namespace'/>"/>
</s:form>