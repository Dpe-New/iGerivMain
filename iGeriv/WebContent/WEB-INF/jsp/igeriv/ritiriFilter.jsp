<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 180px;
}
</style>
<s:set name="codCli" value="codCliente"></s:set>
<s:form id="RitiriFilterForm" action="ritiri_show.action"
	onsubmit="return (ray.ajax())" method="POST" theme="simple"
	validate="false">
	<div style="width: 100%; text-align: center;">
		<div class="required" id="errorDiv"
			style="width: 100%; align: center; text-align: center; height: 20px; margin-top: -10px">
			<font color='red'><s:actionerror /></font>
		</div>
		<fieldset class="filterBolla" style="width: 610px;">
			<legend style="font-size: 100%">
				<s:text name="tableTitle" />
			</legend>
			<div class="required" style="width: 550px;">
				<div style="float: left; width: 200px;">
					<s:text name="igeriv.data.ritiro" />
				</div>
				<div style="float: left; width: 330px;">
					<s:text name="igeriv.da" />
					&nbsp;&nbsp;
					<s:textfield name="strDataDa" id="strDataDa" cssStyle="width:78px;"
						disabled="false" />
					&nbsp;&nbsp;
					<s:text name="igeriv.a" />
					&nbsp;&nbsp;
					<s:textfield name="strDataA" id="strDataA" cssStyle="width:78px;"
						disabled="false" />
				</div>
			</div>
			<div class="required" style="align: center; width: 550px">
				<div style="float: left; width: 225px;">
					<s:text name="igeriv.titolo" />
				</div>
				<div style="float: left; width: 305px;">
					<s:textfield label="" name="titolo" id="titolo" cssClass="required"
						cssStyle="width:200px;" />
				</div>
			</div>
			<s:if test="authUser.tipoUtente == 1">
				<div class="required" style="align: center; width: 550px">
					<div style="float: left; width: 225px;">
						<s:text name="username.cliente" />
					</div>
					<div style="float: left; width: 305px;">
						<select name="codCliente" id="codCliente" style="width: 65%;">
							<option value="">&nbsp;</option>
							<optgroup label="<s:text name="igeriv.con.estratto.conto"/>">
								<s:iterator value="listClientiConEC" var="position">
									<option value="<s:property value="#position.codCliente"/>"><s:property
											value="#position.nomeCognomeEscaped" /></option>
								</s:iterator>
							</optgroup>
							<optgroup label="<s:text name="igeriv.senza.estratto.conto"/>">
								<s:iterator value="listClientiSenzaEC" var="position">
									<option value="<s:property value="#position.codCliente"/>"><s:property
											value="#position.nomeCognomeEscaped" /></option>
								</s:iterator>
							</optgroup>
						</select>
					</div>
				</div>
			</s:if>
			<div
				style="align: center; text-align: center; width: 100%; height: 50px;">
				<s:if test="authUser.tipoUtente == 1">
					<div
						style="float: left; width: 25%; align: center; text-align: center;">&nbsp;</div>
				</s:if>
				<div
					style="float:left; width:<s:if test="authUser.tipoUtente == 1">58%</s:if><s:else>610px</s:else>; align:center; text-align:center;">
					<input type="button" name="ricerca" id="ricerca"
						value="<s:text name="dpe.contact.form.ricerca"/>" align="center"
						style="align: center; width: 150px" />
				</div>
				<s:if test="authUser.tipoUtente == 1">
					<div
						style="float: left; width: 10%; align: center; text-align: center;">
						<img id="ritiriCanc"
							src="/app_img/<s:if test="hasRitiriCancellati eq true">Bin_full.png</s:if><s:else>Bin_empty.png</s:else>"
							style="cursor: pointer"
							alt="<s:text name="igeriv.ritiri.cancellati"/>" border="0"
							title="<s:text name="igeriv.ritiri.cancellati"/>" />
					</div>
				</s:if>
			</div>
		</fieldset>
	</div>
</s:form>