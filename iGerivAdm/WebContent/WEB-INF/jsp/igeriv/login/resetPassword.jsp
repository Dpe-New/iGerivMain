<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:form name="security_form" id="security_form"
	action="/loginResetPwd_resetPwdAdm.action" theme="simple"
	onsubmit="return (validateFieldsReset() && ray.ajax());">	
	<fieldset><legend><s:text name="gp.gestione.sito"/> - <s:text name="password.dimenticata" /></legend>
		<div class="required" style="float:left">
			<div style="float:left; width:160px;">
				<label for="codEdicola"><s:text name="username.adm"/><em>*</em></label> 
			</div>
			<div style="float:left; width:160px;">
				<s:textfield id="codEdicola" name="codEdicola" key="codEdicola" />
			</div>
			<div style="float:left; width:160px;">
				<font color="red"><span id="err_codEdicola"><s:fielderror><s:param>codEdicola</s:param></s:fielderror></span></font>		
			</div>
		</div>	
		<div class="required" style="float:left">
			<div style="float:left; width:160px;">
				<label for="emailEdicola"><s:text name="msg.email.adm"/><em>*</em></label> 
			</div>
			<div style="float:left; width:160px;">
				<s:textfield id="emailEdicola" name="emailEdicola" key="emailEdicola" />
			</div>
			<div style="float:left; width:160px;">
				<font color="red"><span id="err_emailEdicola"><s:fielderror><s:param>emailEdicola</s:param></s:fielderror></span></font>		
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
		<div style="float:left">		
			<div style="float:left; width:160px;">
				&nbsp;
			</div>
			<div style="float:left;">
				<div><s:submit key="dpe.contact.form.pwd.send" align="center"/></div>
				<s:url id="url" action="login" />	
				<div><input type="button" value="<s:text name='dpe.contact.form.torna.login'/>" onclick="javascript: window.location = '<s:text name='url'/>'" align="center"/></div>				
			</div>
		</div>	
	</fieldset>
	<br>	
	<div class="textLinks" style="color:red; margin-left:50px">		
		<s:property value="%{exception.message}"/>
		<s:actionerror/>
	</div>	
</s:form>