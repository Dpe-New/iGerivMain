<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://code.google.com/p/jcaptcha4struts2/taglib/2.0"
	prefix="jcaptcha"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/validation.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/utils.js"></script>
<s:set name="templateSuffix" value="'ftl'" />
<s:if
	test="%{#request.sf neq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS}">
	<s:form name="ChangePwdForm" id="ChangePwdForm"
		action="/changePwd_change.action" theme="igeriv" validate="false"
		onsubmit="return (ray.ajax())">
		<s:hidden id="codEdicola" name="codEdicola"></s:hidden>
		<s:hidden id="id" name="id"></s:hidden>
		<fieldset>
			<legend>
				<s:text name="gp.gestione.minicard" />
				-
				<s:text name="gp.cambio.password" />
			</legend>
			<div class="required" style="float: left">
				<div style="float: left; width: 160px;">
					<label for="oldPassword"><s:text
							name="dpe.login.old.password"></s:text><em>*</em></label>
				</div>
				<div style="float: left; width: 160px;">
					<s:password id="oldPassword" name="oldPassword" />
				</div>
				<div style="float: left; width: 160px;">
					<font color="red" size="2"><span id="err_oldPassword"><s:fielderror>
								<s:param>oldPassword</s:param>
							</s:fielderror></span></font>
				</div>
			</div>
			<div class="required" style="float: left">
				<div style="float: left; width: 160px;">
					<label for="password"><s:text name="dpe.login.new.password"></s:text><em>*</em></label>
				</div>
				<div style="float: left; width: 160px;">
					<s:password id="password" name="password" />
				</div>
				<div style="float: left; width: 160px;">
					<font color="red" size="2"><span id="err_password"><s:fielderror>
								<s:param>password</s:param>
							</s:fielderror></span></font>
				</div>
			</div>
			<div class="required" style="float: left">
				<div style="float: left; width: 160px;">
					<label for="confermaPassword"><s:text
							name="dpe.login.confermapassword"></s:text><em>*</em></label>
				</div>
				<div style="float: left; width: 160px;">
					<s:password id="confermaPassword" name="confermaPassword" />
				</div>
				<div style="float: left; width: 160px;">
					<font color="red" size="2"><span id="err_confermaPassword"><s:fielderror>
								<s:param>confermaPassword</s:param>
							</s:fielderror></span></font>
				</div>
			</div>
			<div class="required" style="float: left">
				<div style="float: left; width: 160px;">
					<br>
					<div style="font-weight: bold;">
						<s:text name="gp.captcha" />
						<em>*</em>
					</div>
					<br>
					<div class="textLinks">
						<a href="javascript: refreshimage();"><s:text
								name="igeriv.cambia.immagine" /></a>
					</div>
				</div>
				<div style="float: left">
					<img id="imgCaptcha"
						src="${pageContext.request.contextPath}/jcaptcha_image.action"
						width="170px" height="80px" />
					<div class="captchaError"">
						<s:fielderror>
							<s:param>jCaptchaResponse</s:param>
						</s:fielderror>
					</div>
				</div>
			</div>
			<div class="required" style="float: left">
				<div style="float: left; width: 160px;">
					<label for="jCaptchaResponse">&nbsp;</label>
				</div>
				<s:textfield name="jCaptchaResponse" />
			</div>
			<div class="submit" style="float: left">
				<div style="width: 16px;">&nbsp;</div>
				<div>
					<s:submit key="dpe.contact.form.submit" align="center"
						cssStyle="width:100px" />
				</div>
				<div>
					<input type="button"
						value="<s:text name='dpe.contact.form.torna.indietro'/>"
						align="center" cssStyle="width:150px"
						onclick="javascript: history.back();" />
				</div>
			</div>
		</fieldset>
		<br>
		<div style="color: red; margin-left: 50px">
			<s:property value="%{exception.message}" />
		</div>
		<input type="hidden" name="loginType"
			value="<s:text name='#context["struts.actionMapping"].namespace'/>" />
	</s:form>
</s:if>
<s:else>
	<div align="center">
		<s:form name="ChangePwdForm" id="ChangePwdForm"
			action="/changePwd_change.action" theme="igeriv" validate="false"
			onsubmit="return (ray.ajax())">
			<br>
			<table width="903" height="601" border="0" cellpadding="0"
				cellspacing="0">
				<tr>
					<td colspan="3"><img src="/app_img/01.jpg" width="903"
						height="301" alt="edismart"></td>
				</tr>
				<tr>
					<td><img src="/app_img/02.jpg" width="297" height="300"
						alt="edismart"></td>
					<td width="312" height="300" align="center">
						<div style="font-size: 16px;">
							<s:text name="gp.gestione.minicard" />
							-
							<s:text name="gp.cambio.password" />
						</div>
						<hr width="280" size="1" color="#888888">
						<div style="width: 300px; height: 25px">
							<div style="width: 120px; float: left; white-space: nowrap;">
								<s:text name="dpe.login.old.password" />
								<em>*</em>
							</div>
							<div style="float: right;">
								<s:password id="oldPassword" name="oldPassword"
									key="oldPassword" cssStyle="width:130px;" />
							</div>
						</div>
						<div style="width: 300px; height: 25px">
							<div style="width: 120px; float: left; white-space: nowrap;">
								<s:text name="dpe.login.new.password" />
								<em>*</em>
							</div>
							<div style="float: right;">
								<s:password id="password" name="password" key="password"
									cssStyle="width:130px;" />
							</div>
						</div>
						<div style="width: 300px; height: 25px">
							<div style="width: 120px; float: left; white-space: nowrap;">
								<s:text name="dpe.login.confermapassword" />
								<em>*</em>
							</div>
							<div style="float: right;">
								<s:password id="confermaPassword" name="confermaPassword"
									key="confermaPassword" cssStyle="width:130px;" />
							</div>
						</div>
						<div style="width: 300px; height: 50px;">
							<div style="width: 120px; float: left">
								<div>
									<s:text name="gp.captcha" />
									<em>*</em>
								</div>
								<div class="textLinks">
									<a href="javascript: refreshimage();"><s:text
											name="igeriv.cambia.immagine" /></a>
								</div>
							</div>
							<div style="width: 120px; float: left">
								<img id="imgCaptcha"
									src="${pageContext.request.contextPath}/jcaptcha_image.action"
									" width="170px" />
								<div style="margin-top: 5px;">
									<s:textfield name="jCaptchaResponse" id="jCaptchaResponse" />
								</div>
							</div>
						</div>
						<div style="width: 300px;">
							<div style="width: 50%; float: left; align: center">
								<s:submit key="dpe.contact.form.submit" align="center"
									cssStyle="width:100px" />
							</div>
							<div style="float: left; align: center">
								<s:url id="url" action="login" />
								<input type="button"
									value="<s:text name='dpe.contact.form.torna.login'/>"
									onclick="javascript: window.location = '<s:text name='url'/>'"
									align="center" />
							</div>
						</div>
						<div style="width: 300px; color: red;">
							<div style="text-align: center; width: 300px; float: left">
								<span id="err_oldPassword"><s:fielderror>
										<s:param>oldPassword</s:param>
									</s:fielderror></span>
							</div>
							<div style="text-align: center; width: 300px; float: left">
								<span id="err_password"><s:fielderror>
										<s:param>password</s:param>
									</s:fielderror></span>
							</div>
							<div style="text-align: center; width: 300px; float: left">
								<span id="err_confermaPassword"><s:fielderror>
										<s:param>confermaPassword</s:param>
									</s:fielderror></span>
							</div>
							<div style="text-align: center; width: 300px; float: left">
								<span id="err_jCaptchaResponse"><s:fielderror>
										<s:param>jCaptchaResponse</s:param>
									</s:fielderror></span>
							</div>
							<div style="text-align: center; width: 300px; float: left"
								id="err_exception_msg">
								<s:property value="%{exception.message}" escape="false" />
							</div>
							<div style="text-align: center; width: 300px; float: left"
								id="err_action">
								<s:actionerror />
							</div>
						</div>
					</td>
					<td><img src="/app_img/04.jpg" width="294" height="300"
						alt="edismart"></td>
				</tr>
			</table>
			<s:hidden id="codEdicola" name="codEdicola"></s:hidden>
			<s:hidden id="id" name="id"></s:hidden>
			<input type="hidden" name="loginType"
				value="<s:text name='#context["struts.actionMapping"].namespace'/>" />
		</s:form>
	</div>
</s:else>