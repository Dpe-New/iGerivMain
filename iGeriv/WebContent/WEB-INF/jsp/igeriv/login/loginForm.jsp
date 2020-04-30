<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:if
	test="%{#request.sf neq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS}">
	<s:form name="security_form" id="security_form"
		action="/j_spring_security_check" theme="simple"
		onsubmit="return (validateFieldsLogin() && ray.ajax());">
		<fieldset>
			<legend>
				<s:text name="gp.gestione.minicard" />
				-
				<s:text name="gp.titolo.login" />
			</legend>
			<div style="float: left; text-align: left; width: 670px;">
				<div style="float: left; text-align: left; width: 330px;">
					<div class="required" style="width: 330px;">
						<label for="j_username"><s:text name="username" /><em>*</em></label>
						<s:textfield id="j_username" name="j_username" key="username" />
					</div>
					<div class="required" style="width: 330px;">
						<label for="j_password"><s:text name="password"></s:text><em>*</em></label>
						<s:password id="j_password" name="j_password" key="password" />
					</div>
				</div>
				<div style="float: left; text-align: center; width: 70px;">
					<div>
						<img id="forum" src="/app_img/ok50x50.png"
							alt="<s:text name="igeriv.entra"/>" border="0"
							title="<s:text name="igeriv.entra"/>"
							onclick="javascript: doSubmit();"
							style="cursor: pointer; text-align: center" />
					</div>
				</div>
				<div style="float: left; text-align: center; width: 200px;">
					<s:if
						test="%{#session.SPRING_SECURITY_LAST_EXCEPTION != null && #session.SPRING_SECURITY_LAST_EXCEPTION.toString().contains('EdicolaSospesaException')}">
						<div style="text-align: center;">
							<span id="infomessage"> <font color="red"><s:text
										name="%{#session.SPRING_SECURITY_LAST_EXCEPTION.localizedMessage}"></s:text></font>
							</span>
						</div>
					</s:if>
					<s:elseif
						test="%{#session.SPRING_SECURITY_LAST_EXCEPTION != null && #session.SPRING_SECURITY_LAST_EXCEPTION.toString().contains('LockedException')}">
						<div style="text-align: center;">
							<span id="infomessage"> <font color="red"><s:text
										name="%{#session.SPRING_SECURITY_LAST_EXCEPTION.localizedMessage}"></s:text></font>
							</span>
						</div>
					</s:elseif>
					<s:elseif test="#parameters['authfailed'] != null">
						<div style="text-align: center;">
							<span id="infomessage"> <font color="red"><s:text
										name="gp.login.failed"></s:text></font></span>
						</div>
					</s:elseif>
				</div>
			</div>
			<div style="float: left; text-align: center; width: 670px;">
				<div id="pwdDim"
					style="font-size: 10px; color: red; text-decoration: none; cursor: pointer">
					<s:text name="password.dimenticata.2" />
					</a>
				</div>
			</div>
		</fieldset>
		<br />
		<s:if test="%{pwdSent != null}">
			<div class="textLinks" style="width: 500px">
				<s:text name="password.reset.message" />
			</div>
		</s:if>
		<s:elseif test="%{pwdChanged != null}">
			<div class="textLinks" style="width: 500px">
				<s:text name="password.changed.message" />
			</div>
		</s:elseif>
	</s:form>
	<div style="float: right; text-align: right; width: 500px;">
		<fieldset style="width: 300px; float: right">
			<legend>
				<img id="openIdImg" src="/app_img/openid/openidico.png"
					title="OpenID" />
			</legend>
			<div style="width: 100%; margin-top: 10px; text-align: center">
				<div
					style="float: left; margin-left: 10px; width: 28%; height: 30px;">
					<img class="openid" title="Google"
						src="/app_img/openid/googleW.png" style="cursor: pointer"
						onclick="signOpenId('https://www.google.com/accounts/o8/id','G')"></img>
				</div>
				<div
					style="float: left; margin-left: 10px; width: 28%; height: 30px;">
					<img class="openid" title="Yahoo" src="/app_img/openid/yahooW.png"
						style="cursor: pointer"
						onclick="signOpenId('https://me.yahoo.com/','Y')"></img>
				</div>
				<div
					style="float: left; margin-left: 10px; width: 28%; height: 30px;">
					<img class="openid" title="AOL" src="/app_img/openid/aolW.png"
						style="cursor: pointer"
						onclick="signOpenId('http://openid.aol.com','A')"></img>
				</div>
			</div>
		</fieldset>
	</div>
	<s:url var="openIDLoginUrl" value="/j_spring_openid_security_check" />
	<form id="openIdForm" action="${openIDLoginUrl}" method="get">
		<input id="openId" name="openid_identifier" type="hidden" value="" />
		<input id="providerType" name="providerType" type="hidden" value="" />
	</form>
	<br />
	<s:if test="env eq 'DPE'">
		<div
			style="float: right; text-align: right; width: 500px; margin-top: 20px">
			<div style="float: right; text-align: right; width: 100%;">
				<div style="float: right; width: 40%">
					<div style="float: left">
						<a
							href="<s:if test="authUser.codFiegDl eq constants.COD_FIEG_MENTA">http://www.igeriv.it/forum</s:if><s:else>/forum</s:else>"
							target="_new"><img id="forum" src="/app_img/forum_icon.gif"
							alt="<s:text name="igeriv.forum"/>" border="0"
							title="<s:text name="igeriv.accedi.forum"/>" /></a>
					</div>
					<div style="float: left; margin-left: 20px">
						<a href="http://igeriv.blogspot.com" target="_new"><img
							id="blog" src="/app_img/blog-icon.png"
							alt="<s:text name="igeriv.blog"/>" border="0"
							title="<s:text name="igeriv.accedi.blog"/>" /></a>
					</div>
					<div style="float: left; margin-left: 20px;" id="fb"
						title="Seguici su facebook">
						<a href="http://www.facebook.com/iGeriv" target="_new"><img
							src="/app_img/facebook_32.png" border="0" /></a>
					</div>
				</div>
			</div>
		</div>
		<br/>
		<br/>
<!-- 		<P style="color: orange;font-size:18px;">iGeriv &egrave; nato il 01 Giugno 2000</P> -->
<!-- 		<P style="color: orange;font-size:16px;line-height: 3px;">Ad oggi &egrave; alla sua 3° Edizione </P> -->
<!-- 		<P style="color: orange;font-size:16px;line-height: 3px;">Da allora sono trascorsi: <SPAN ID="countdown"></SPAN></P> -->
	
		
		
	</s:if>
</s:if>
<s:else>
	<div align="center">
		<s:form name="security_form" id="security_form"
			action="/j_spring_security_check" theme="simple"
			onsubmit="return (validateFieldsLogin() && ray.ajax());">
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
					<td background="/app_img/03.jpg" width="312" height="250"
						align="center">
						<table width="300" height="230" border="0" cellpadding="0"
							cellspacing="0">
							<tr>
								<td width="300" valign="top">
									<table width="300" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td align="center"><s:text name="gp.gestione.minicard" />
												<hr width="280" size="1" color="#888888">
												<br></td>
										</tr>
									</table>
									<table width="300" height="130" border="0" cellpadding="0"
										cellspacing="0">
										<tr>
											<td width="180" align="right">
												<div class="required">
													<label for="j_username"><s:text name="username" /><em>*</em></label>
													<br>
													<s:textfield id="j_username" name="j_username"
														key="username" />
												</div>
												<div class="required">
													<label for="j_password"><s:text name="password" /><em>*</em></label>
													<br>
													<s:password id="j_password" name="j_password"
														key="password" />
												</div>
											</td>
											<td width="120" align="center" VALIGN="bottom"><img
												id="forum" src="/app_img/ok.png" alt="Entra" border="0"
												title="Entra" onclick="javascript: doSubmit();"
												style="cursor: pointer; text-align: center" /></td>
										</tr>
										<tr>
											<td colspan="2" align="center"><br>
												<hr width="280" size="1" color="#888888">
												<div id="pwdDim"
													style="font-size: 10px; color: red; text-decoration: none; cursor: pointer">
													<s:text name="password.dimenticata.2" />
													</a>
												</div> <s:if test="%{pwdSent != null}">
													<div
														style="font-size: 10px; color: red; text-decoration: none; cursor: pointer">
														<s:text name="password.reset.message" />
													</div>
												</s:if> <s:elseif test="%{pwdChanged != null}">
													<div
														style="font-size: 10px; color: red; text-decoration: none; cursor: pointer">
														<s:text name="password.changed.message" />
													</div>
												</s:elseif></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					<td><img src="/app_img/04.jpg" width="294" height="250"
						alt="edismart"></td>
				</tr>
			</table>
			<br>
			<s:text name="msgDpe" />	
		</s:form>
		<br/>
<!-- 		<P style="color: orange;font-size:14px;">iGeriv &egrave; nato il 01 Giugno 2000 </P> -->
<!-- 		<P style="color: orange;font-size:12px;line-height: 3px;">Ad oggi &egrave; alla sua 3° Edizione </P> -->
<!-- 		<P style="color: orange;font-size:12px;line-height: 3px;">Da allora sono trascorsi: <SPAN ID="countdown"></SPAN></P> -->
	
	</div>
</s:else>

