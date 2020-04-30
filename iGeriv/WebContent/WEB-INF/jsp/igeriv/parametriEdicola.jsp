<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
div#filter {
	height: 0px;
}

div#content1 {
	height: 550px;
}
</style>
<s:actionerror />
<s:actionmessage />
<s:form id="ParamEdicolaForm" action="params_saveParam.action"
	enctype="multipart/form-data" theme="igeriv" method="POST"
	validate="false">
	<div id="mainDiv" style="width: 100%;">
		<div id="mainDiv"
			style="width: 80%; height: 520px; overflow-x: hidden; overflow-y: scroll; margin-left: auto; margin-right: auto;">
			<fieldset style="text-align: left; height: 950px; width: 88%;">
				<legend style="font-size: 100%">
					<s:text name="igeriv.params.edicola" />
				</legend>
				<div style="width: 100%">
					<div style="float: left; width: 50%">
						<s:text name="igeriv.params.edicola.1" />
					</div>
					<div style="float: left; width: 48%">
						<s:checkbox id="paramEdicola1" cssClass="tableFields"
							value="%{#session.mapParamsEdicola.paramEdicola1.paramValue}"
							accesskey="%{#session.mapParamsEdicola.paramEdicola1.sqlType}" />
					</div>
				</div>

				<div style="width: 100%">
					<div style="float: left; width: 50%">
						<s:text name="igeriv.params.edicola.27" />
					</div>
					<div style="float: left; width: 48%">
						<s:checkbox id="paramEdicola27" cssClass="tableFields"
							value="%{#session.mapParamsEdicola.paramEdicola27.paramValue}"
							accesskey="%{#session.mapParamsEdicola.paramEdicola27.sqlType}" />
					</div>
				</div>


				<s:if test="authUser.hasVendite eq true">
					<div style="width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.3" />
						</div>
						<div style="float: left; width: 48%">
							<s:checkbox id="paramEdicola3" cssClass="tableFields"
								value="%{#session.mapParamsEdicola.paramEdicola3.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola3.sqlType}" />
						</div>
					</div>
					<!-- //Ticket 0000371 -->
					<div style="width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.28" />
						</div>
						<div style="float: left; width: 48%">
							<s:checkbox id="paramEdicola28" cssClass="tableFields"
								value="%{#session.mapParamsEdicola.paramEdicola28.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola28.sqlType}" />
						</div>
					</div>
				</s:if>
				<div style="width: 100%">
					<div style="float: left; width: 50%">
						<s:text name="igeriv.params.edicola.4" />
					</div>
					<div style="float: left; width: 48%">
						<s:checkbox id="paramEdicola4" cssClass="tableFields"
							value="%{#session.mapParamsEdicola.paramEdicola4.paramValue}"
							accesskey="%{#session.mapParamsEdicola.paramEdicola4.sqlType}" />
					</div>
				</div>
				<div style="width: 100%">
					<div style="float: left; width: 50%">
						<s:text name="igeriv.params.edicola.5" />
					</div>
					<div style="float: left; width: 48%">
						<s:checkbox id="paramEdicola5" cssClass="tableFields"
							value="%{#session.mapParamsEdicola.paramEdicola5.paramValue}"
							accesskey="%{#session.mapParamsEdicola.paramEdicola5.sqlType}" />
					</div>
				</div>
				<s:if
					test="authUser.hasBolle eq true && authUser.codFiegDl neq constants.COD_FIEG_MENTA">
					<div style="width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.7" />
						</div>
						<div style="float: left; width: 48%">
							<s:checkbox id="paramEdicola7" cssClass="tableFields"
								value="%{#session.mapParamsEdicola.paramEdicola7.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola7.sqlType}" />
						</div>
					</div>
				</s:if>
				<div style="width: 100%">
					<div style="float: left; width: 50%">
						<s:text name="igeriv.params.edicola.26" />
					</div>
					<div style="float: left; width: 48%">
						<s:checkbox id="paramEdicola26" cssClass="tableFields"
							value="%{#session.mapParamsEdicola.paramEdicola26.paramValue}"
							accesskey="%{#session.mapParamsEdicola.paramEdicola26.sqlType}" />
					</div>
				</div>
				
				<div style="width: 100%">
					<div style="float: left; width: 50%">
						<s:text name="igeriv.params.edicola.29" />
					</div>
					<div style="float: left; width: 48%">
						<s:checkbox id="paramEdicola29" cssClass="tableFields"
							value="%{#session.mapParamsEdicola.paramEdicola29.paramValue}"
							accesskey="%{#session.mapParamsEdicola.paramEdicola29.sqlType}" />
					</div>
				</div>
				
				
				
				<s:if test="%{authUser.edicolaDeviettiTodis neq true}">
					<div style="width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.22" />
						</div>
						<div style="float: left; width: 48%">
							<s:checkbox id="paramEdicola22" cssClass="tableFields"
								value="%{#session.mapParamsEdicola.paramEdicola22.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola22.sqlType}" />
						</div>
					</div>
					<div style="width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.24" />
						</div>
						<div style="float: left; width: 48%">
							<s:checkbox id="paramEdicola24" cssClass="tableFields"
								value="%{#session.mapParamsEdicola.paramEdicola24.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola24.sqlType}"
								disabled="true" />
						</div>
					</div>
				</s:if>
				<s:if test="authUser.hasVendite eq true">
					<div style="width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.9" />
						</div>
						<div style="float: left; width: 48%">
							<s:checkbox id="paramEdicola9" cssClass="tableFields"
								value="%{#session.mapParamsEdicola.paramEdicola9.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola9.sqlType}" />
						</div>
					</div>
					<s:if test="authUser.hasPrenotazioni eq true">
						<div style="width: 100%">
							<div style="float: left; width: 50%">
								<s:text name="igeriv.params.edicola.10" />
							</div>
							<div style="float: left; width: 48%">
								<s:checkbox id="paramEdicola10" cssClass="tableFields"
									value="%{#session.mapParamsEdicola.paramEdicola10.paramValue}"
									accesskey="%{#session.mapParamsEdicola.paramEdicola10.sqlType}" />
							</div>
						</div>
					</s:if>
					<div style="width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.11" />
						</div>
						<div style="float: left; width: 48%">
							<s:checkbox id="paramEdicola11" cssClass="tableFields"
								value="%{#session.mapParamsEdicola.paramEdicola11.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola11.sqlType}" />
						</div>
					</div>
					<div style="width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.19" />
						</div>
						<div style="float: left; width: 48%">
							<s:checkbox id="paramEdicola19" cssClass="tableFields"
								value="%{#session.mapParamsEdicola.paramEdicola19.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola19.sqlType}" />
						</div>
					</div>
				</s:if>
				<s:if test="authUser.hasBolle eq true">
					<div style="width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.14" />
						</div>
						<div style="float: left; width: 48%">
							<s:checkbox id="paramEdicola14" cssClass="tableFields"
								value="%{#session.mapParamsEdicola.paramEdicola14.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola14.sqlType}" />
						</div>
					</div>
				</s:if>
				<s:if test="authUser.hasVendite eq true">
					<div style="width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.20" />
						</div>
						<div style="float: left; width: 48%">
							<s:checkbox id="paramEdicola20" cssClass="tableFields"
								value="%{#session.mapParamsEdicola.paramEdicola20.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola20.sqlType}" />
						</div>
					</div>
					<div style="width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.21" />
						</div>
						<div style="float: left; width: 48%">
							<s:checkbox id="paramEdicola21" cssClass="tableFields"
								value="%{#session.mapParamsEdicola.paramEdicola21.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola21.sqlType}" />
						</div>
					</div>
				</s:if>
				<div style="width: 100%">
					<div style="float: left; width: 50%">
						<s:text name="igeriv.params.edicola.25" />
					</div>
					<div style="float: left; width: 48%">
						<s:checkbox id="paramEdicola25" cssClass="tableFields"
							value="%{#session.mapParamsEdicola.paramEdicola25.paramValue}"
							accesskey="%{#session.mapParamsEdicola.paramEdicola25.sqlType}" />
					</div>
				</div>
				<s:if test="authUser.hasProdottiVari eq true">
					<div style="margin-top: 5px; width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.2" />
						</div>
						<div style="float: left; width: 48%">
							<s:textfield
								value="%{#session.mapParamsEdicola.paramEdicola2.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola2.sqlType}"
								id="paramEdicola2" cssStyle="width:50px" cssClass="tableFields" />
						</div>
					</div>
					<div style="width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.17" />
						</div>
						<div style="float: left; width: 48%">
							<s:textfield
								value="%{#session.mapParamsEdicola.paramEdicola17.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola17.sqlType}"
								id="paramEdicola17" cssClass="tableFields" />
						</div>
					</div>
					<div style="width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.18" />
						</div>
						<div style="float: left; width: 48%">
							<s:textfield
								value="%{#session.mapParamsEdicola.paramEdicola18.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola18.sqlType}"
								id="paramEdicola18" cssClass="tableFields" />
						</div>
					</div>
					<div style="margin-top: 5px; width: 100%">
						<div style="float: left; width: 50%">
							<span style="width: 100%"> <s:text
									name="igeriv.params.edicola.16">
									<s:param name="value" value="valoreMarcaBollo" />
								</s:text>
							</span> <span
								style="float: left; width: 100%; font-size: 9px; text-align: right"><s:text
									name="igeriv.0.per.disabilitare" /></span>
						</div>
						<div style="float: left; width: 48%">
							<s:textfield
								value="%{#session.mapParamsEdicola.paramEdicola16.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola16.sqlType}"
								id="paramEdicola16" cssStyle="width:50px" cssClass="tableFields" />
						</div>
					</div>
				</s:if>
				<!-- Ticket : 0000120 richiesta CDL -->
				<%-- 				<s:if test="authUser.codFiegDl != #application['CDL_CODE']"> --%>
				<div style="width: 100%">
					<div style="float: left; width: 50%">
						<s:text name="igeriv.params.edicola.8" />
					</div>
					<div style="float: left; width: 48%">
						<s:textfield value="%{authUser.email}"
							accesskey="%{#session.mapParamsEdicola.paramEdicola8.sqlType}"
							id="paramEdicola8" cssStyle="width:250px" cssClass="tableFields"
							disabled="true" />
						<img id="changeImg" src="/app_img/modify.png"
							style="vertical-align: top; cursor: pointer"
							alt="<s:text name="msg.email.rivendita" />" border="0"
							title="<s:text name="igeriv.modifica"/>&nbsp;<s:text name="msg.email.rivendita" />" />
					</div>
				</div>
				<%-- 				</s:if> --%>
				<div style="margin-top: 5px; width: 100%; height: 60px;">
					<div
						style="float: left; width: 50%; height: 60px; vertical-align: middle;">
						<s:text name="igeriv.params.edicola.15" />
					</div>
					<div
						style="float: left; width: 30%; height: 60px; vertical-align: middle;">
						<s:file
							accesskey="%{#session.mapParamsEdicola.paramEdicola15.sqlType}"
							id="paramEdicola15" name="attachment1"
							cssStyle="background-color:#dfe4e8;" cssClass="tableFields"
							size="13" label="File" />
					</div>
					<div
						style="float: left; width: 10%; height: 60px; vertical-align: middle;">
						<img id="imgProd"
							title="<s:text name="%{#session.mapParamsEdicola.paramEdicola15.paramValue}"/>"
							src="/immagini_miniature_edicola/<s:text name="%{#session.mapParamsEdicola.paramEdicola15.paramValue}"/>"
							border="1" width="50" height="50" />
					</div>
				</div>
				<s:if test="authUser.hasProdottiVari eq true">
					<div style="margin-top: 5px; width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.12" />
						</div>
						<div style="float: left; width: 48%">
							<span style="float: left; width: 55%; height: 100px;"> <select
								id="paramEdicola12" style="width: 200px" class="tableFields">
									<option fileName="" value="-1"></option>
									<s:iterator value="registratoriCassa" status="status">
										<option
											fileName="<s:property value='fileNameManualeInstallazione'/>"
											value="<s:property value='codRegCassa'/>"><s:property
												value='modello' /></option>
									</s:iterator>
							</select>
							</span> <span style="float: left; width: 45%; height: 100px;"> <span
								id="fileNameManualeSpan" style="width: 100%; display: block;"
								class="tableFields_blu"></span> <span id="testJavaLinkSpan"
								style="width: 100%; display: block; margin-top: 5px;"
								class="tableFields_red"></span>
							</span>
						</div>
					</div>
					<div style="width: 100%">
						<div style="float: left; width: 50%">
							<s:text name="igeriv.params.edicola.23" />
						</div>
						<div style="float: left; width: 48%">
							<s:checkbox id="paramEdicola23" cssClass="tableFields"
								value="%{#session.mapParamsEdicola.paramEdicola23.paramValue}"
								accesskey="%{#session.mapParamsEdicola.paramEdicola23.sqlType}"
								disabled="true" />
						</div>
					</div>
				</s:if>
				<div style="margin-top: 5px; width: 100%">
					<div style="float: left; width: 50%">
						<s:text name="igeriv.params.edicola.6" />
					</div>
					<div style="float: left; width: 48%">
						<s:textarea rows="3" cols="80"
							value="%{#session.mapParamsEdicola.paramEdicola6.paramValue}"
							accesskey="%{#session.mapParamsEdicola.paramEdicola6.sqlType}"
							id="paramEdicola6" cssStyle="width:250px" cssClass="tableFields" />
					</div>
				</div>
			</fieldset>
		</div>
	</div>
	<s:hidden name="codParametro" id="codParametro" />
	<s:hidden name="valParametro" id="valParametro" />
</s:form>
