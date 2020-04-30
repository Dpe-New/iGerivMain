<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:if
	test="%{#request.sf neq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS}">
	<s:form name="security_form" id="security_form"
		action="/loginResetPwd_resetPwdEdicola.action" theme="simple"
		onsubmit="return (validateFieldsReset() && ray.ajax());">
		<fieldset>
			<legend>
				<s:text name="gp.gestione.minicard" />
				-
				<s:text name="password.dimenticata" />
			</legend>
			<div class="required" style="float: left">
				<div style="float: left; width: 160px;">
					<label for="codEdicola"><s:text name="username" /><em>*</em></label>
				</div>
				<div style="float: left; width: 160px;">
					<s:textfield id="codEdicola" name="codEdicola" key="codEdicola" />
				</div>
				<div style="float: left; width: 160px;">
					<font color="red"><span id="err_codEdicola"><s:fielderror>
								<s:param>codEdicola</s:param>
							</s:fielderror></span></font>
				</div>
			</div>
			<div class="required" style="float: left">
				<div style="float: left; width: 160px;">
					<label for="emailEdicola"><s:text
							name="msg.email.rivendita" /><em>*</em></label>
				</div>
				<div style="float: left; width: 160px;">
					<s:textfield id="emailEdicola" name="emailEdicola"
						key="emailEdicola" />
				</div>
				<div style="float: left; width: 160px;">
					<font color="red"><span id="err_emailEdicola"><s:fielderror>
								<s:param>emailEdicola</s:param>
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
				<s:textfield name="jCaptchaResponse" id="jCaptchaResponse" />
			</div>
			<div style="float: left">
				<div style="float: left; width: 160px;">&nbsp;</div>
				<div style="float: left;">
					<div>
						<s:submit key="dpe.contact.form.pwd.send" align="center" />
					</div>
					<s:url id="url" action="login" />
					<div>
						<input type="button"
							value="<s:text name='dpe.contact.form.torna.login'/>"
							onclick="javascript: window.location = '<s:text name='url'/>'"
							align="center" />
					</div>
				</div>
			</div>
		</fieldset>
		<br>
		<div class="textLinks" style="color: red; margin-left: 50px">
			<s:property value="%{exception.message}" />
			<s:actionerror />
		</div>
	</s:form>
</s:if>
<s:else>
	<div align="center">
		<s:form name="security_form" id="security_form"
			action="/loginResetPwd_resetPwdEdicola.action" theme="simple"
			onsubmit="return (validateFieldsReset() && ray.ajax());">
			<br>
			<table width="903" height="601" border="0" cellpadding="0"
				cellspacing="0">
				<tr>
					<td colspan="3"><img src="/app_img/01.jpg" width="903"
						height="351" alt="edismart"></td>
				</tr>
				<tr>
					<td><img src="/app_img/02.jpg" width="297" height="250"
						alt="edismart"></td>
					<td width="312" height="250" align="center">
						<div style="font-size: 16px;">
							<s:text name="gp.gestione.minicard" />
							-
							<s:text name="password.dimenticata" />
						</div>
						<hr width="280" size="1" color="#888888">
						<div style="width: 300px; height: 30px;">
							<div style="width: 120px; float: left;">
								<s:text name="username" />
								<em>*</em>
							</div>
							<div style="float: left;">
								<s:textfield id="codEdicola" name="codEdicola" key="codEdicola" />
							</div>
						</div>
						<div style="width: 300px; height: 25px;">
							<div style="width: 120px; float: left;">
								<s:text name="msg.email.rivendita" />
								<em>*</em>
							</div>
							<div style="float: left;">
								<s:textfield id="emailEdicola" name="emailEdicola"
									key="emailEdicola" />
							</div>
						</div>
						<div style="width: 300px; height: 70px;">
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
									" width="170px" height="60px" />
								<div style="margin-top: 5px;">
									<s:textfield name="jCaptchaResponse" id="jCaptchaResponse" />
								</div>
							</div>
						</div>
						<div style="width: 300px; height: 30px;">
							<div style="width: 50%; float: left; align: center">
								<s:submit key="dpe.contact.form.pwd.send" align="center" />
							</div>
							<div style="float: left; align: center">
								<s:url id="url" action="login" />
								<input type="button"
									value="<s:text name='dpe.contact.form.torna.login'/>"
									onclick="javascript: window.location = '<s:text name='url'/>'"
									align="center" />
							</div>
						</div>
						<div style="width: 300px; color: red; margin-top: 20px;">
							<div
								style="position: absolute; text-align: center; width: 300px;">
								<span id="err_codEdicola"><s:fielderror>
										<s:param>codEdicola</s:param>
									</s:fielderror></span>
							</div>
							<div
								style="position: absolute; text-align: center; width: 300px;">
								<span id="err_emailEdicola"><s:fielderror>
										<s:param>emailEdicola</s:param>
									</s:fielderror></span>
							</div>
							<div
								style="position: absolute; text-align: center; width: 300px;">
								<span id="err_jCaptchaResponse"><s:fielderror>
										<s:param>jCaptchaResponse</s:param>
									</s:fielderror></span>
							</div>
							<div
								style="position: absolute; text-align: center; width: 300px;"
								id="err_exception_msg">
								<s:property value="%{exception.message}" escape="false" />
							</div>
							<div
								style="position: absolute; text-align: center; width: 300px;"
								id="err_action">
								<s:actionerror />
							</div>
						</div>
					</td>
					<td><img src="/app_img/04.jpg" width="294" height="250"
						alt="edismart"></td>
				</tr>
			</table>
		</s:form>
	</div>
</s:else>