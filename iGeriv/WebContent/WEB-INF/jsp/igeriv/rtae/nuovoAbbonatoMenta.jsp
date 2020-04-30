<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:form id="clienteEditForm" action="clientiM_saveCliente.action"
	method="POST" theme="simple" onsubmit="return ray.ajax()"
	validate="false">
	<s:hidden name="abbonato.idAbbonato" id="idAbbonato" />
	<div style="width: 98%; margin-left: auto; margin-right: auto;">
		<fieldset
			style="margin: 0 0 0 0;  padding: 0 0 0 0; width: <s:property value="%{#application.divWidth}"/>; height:300px; margin-top:30px">
			<legend>
				<s:text name="rtae.menu.nuovo.cliente" />
			</legend>
			<div style="width: 100%; height: 30px;">
				<div style="float: left; width: 49%;">
					<div style="float: left; width: 30%">
						<s:text name="cliente.nome" />
						&nbsp;
						<s:text name="cliente.un.asterisco" />
					</div>
					<div style="float: left; width: 34%">
						<s:textfield name="abbonato.ragioneSocialePrimaParte"
							id="abbonato.ragioneSocialePrimaParte"
							cssClass="extremeTableFieldsLong" maxlength="40" />
					</div>
					<div style="float: left; width: 33%">
						<font color="red"><span
							id="err_abbonato.ragioneSocialePrimaParte" class="error"
							style="width: 140px; display: block;"><s:fielderror>
									<s:param>abbonato.ragioneSocialePrimaParte</s:param>
								</s:fielderror></span></font>
					</div>
				</div>
				<div style="float: left; width: 49%">
					<div style="float: left; width: 30%">
						<s:text name="cliente.cognome" />
						&nbsp;
						<s:text name="cliente.un.asterisco" />
					</div>
					<div style="float: left; width: 34%">
						<s:textfield name="abbonato.ragioneSocialeSecondaParte"
							id="abbonato.ragioneSocialeSecondaParte"
							cssClass="extremeTableFieldsLong" maxlength="40" />
					</div>
					<div style="float: left; width: 33%">
						<font color="red"><span
							id="err_abbonato.ragioneSocialeSecondaParte" class="error"
							style="width: 140px; display: block;"><s:fielderror>
									<s:param>abbonato.ragioneSocialeSecondaParte</s:param>
								</s:fielderror></span></font>
					</div>
				</div>
			</div>
			<div style="width: 100%; height: 30px;">
				<div style="float: left; width: 49%">
					<div style="float: left; width: 30%">
						<s:text name="cliente.data.nascita" />
						&nbsp;
						<s:text name="cliente.un.asterisco" />
					</div>
					<div style="float: left; width: 34%">
						<s:textfield name="abbonato.dataNascita" id="abbonato.dataNascita"
							cssClass="extremeTableFieldsLong">
							<s:param name="value">
								<s:date name="abbonato.dataNascita" format="dd/MM/yyyy" />
							</s:param>
						</s:textfield>
					</div>
					<div style="float: left; width: 33%">
						<font color="red"><span id="err_abbonato.dataNascita"
							class="error" style="width: 140px; display: block;"><s:fielderror>
									<s:param>abbonato.dataNascita</s:param>
								</s:fielderror></span></font>
					</div>
				</div>
				<div style="float: left; width: 49%;">
					<div style="float: left; width: 30%">
						<s:text name="cliente.cf" />
						&nbsp;
						<s:text name="cliente.un.asterisco" />
					</div>
					<div style="float: left; width: 34%">
						<s:textfield name="abbonato.codFiscale" id="abbonato.codFiscale"
							cssClass="extremeTableFieldsLong" maxlength="16" />
					</div>
					<div style="float: left; width: 33%">
						<font color="red"><span id="err_abbonato.codFiscale"
							class="error" style="width: 140px; display: block;"><s:fielderror>
									<s:param>abbonato.codFiscale</s:param>
								</s:fielderror></span></font>
					</div>
				</div>
			</div>
			<div style="width: 100%; height: 30px;">
				<div style="float: left; width: 49%">
					<div style="float: left; width: 30%">
						<s:text name="cliente.indirizzo" />
						&nbsp;
						<s:text name="cliente.un.asterisco" />
					</div>
					<div style="float: left; width: 34%">
						<s:textfield name="abbonato.indirizzo" id="abbonato.indirizzo"
							cssClass="extremeTableFieldsLong" maxlength="40" />
					</div>
					<div style="float: left; width: 33%">
						<font color="red"><span id="err_abbonato.indirizzo"
							class="error" style="width: 140px; display: block;"><s:fielderror>
									<s:param>abbonato.indirizzo</s:param>
								</s:fielderror></span></font>
					</div>
				</div>
				<div style="float: left; width: 49%">
					<div style="float: left; width: 30%">
						<s:text name="cliente.localita" />
						&nbsp;
						<s:text name="cliente.un.asterisco" />
					</div>
					<div style="float: left; width: 34%">
						<s:textfield name="abbonato.localita" id="abbonato.localita"
							cssClass="extremeTableFieldsLong" maxlength="40" />
					</div>
					<div style="float: left; width: 33%">
						<font color="red"><span id="err_abbonato.localita"
							class="error" style="width: 140px; display: block;"><s:fielderror>
									<s:param>abbonato.localita</s:param>
								</s:fielderror></span></font>
					</div>
				</div>
			</div>
			<div style="width: 100%; height: 30px;">
				<div style="float: left; width: 49%">
					<div style="float: left; width: 30%">
						<s:text name="cliente.cap" />
						&nbsp;
						<s:text name="cliente.un.asterisco" />
					</div>
					<div style="float: left; width: 34%">
						<s:textfield name="abbonato.cap" id="abbonato.cap"
							cssClass="extremeTableFieldsLong" maxLength="5" />
					</div>
					<div style="float: left; width: 33%">
						<font color="red"><span id="err_abbonato.cap" class="error"
							style="width: 140px; display: block;"><s:fielderror>
									<s:param>abbonato.cap</s:param>
								</s:fielderror></span></font>
					</div>
				</div>
				<div style="float: left; width: 49%">
					<div style="float: left; width: 30%">
						<s:text name="cliente.telefono" />
						&nbsp;
						<s:text name="cliente.due.asterischi" />
					</div>
					<div style="float: left; width: 34%">
						<s:textfield name="abbonato.telefono" id="abbonato.telefono"
							cssClass="extremeTableFieldsLong" maxlength="16" />
					</div>
					<div style="float: left; width: 33%">
						<font color="red"><span id="err_abbonato.telefono"
							class="error" style="width: 140px; display: block;"><s:fielderror>
									<s:param>abbonato.telefono</s:param>
								</s:fielderror></span></font>
					</div>
				</div>
			</div>
			<div style="width: 100%; height: 30px;">
				<div style="float: left; width: 49%">
					<div style="float: left; width: 30%">
						<s:text name="cliente.cellulare" />
						&nbsp;
						<s:text name="cliente.due.asterischi" />
					</div>
					<div style="float: left; width: 34%">
						<s:textfield name="abbonato.telefono2" id="abbonato.cellulare"
							cssClass="extremeTableFieldsLong" maxlength="16" />
					</div>
					<div style="float: left; width: 33%">
						<font color="red"><span id="err_abbonato.cellulare"
							class="error" style="width: 140px; display: block;"><s:fielderror>
									<s:param>abbonato.cellulare</s:param>
								</s:fielderror></span></font>
					</div>
				</div>
				<div style="float: left; width: 49%">
					<div style="float: left; width: 30%">
						<s:text name="cliente.email" />
						&nbsp;
						<s:text name="cliente.un.asterisco" />
					</div>
					<div style="float: left; width: 34%">
						<s:textfield name="abbonato.email" id="abbonato.email"
							cssClass="extremeTableFieldsLong" maxlength="255" />
					</div>
					<div style="float: left; width: 33%">
						<font color="red"><span id="err_abbonato.email"
							class="error" style="width: 140px; display: block;"><s:fielderror>
									<s:param>abbonato.email</s:param>
								</s:fielderror></span></font>
					</div>
				</div>
			</div>
			<div style="width: 100%; height: 30px;">
				<div style="float: left; width: 49%">
					<div style="float: left; width: 30%">
						<s:text name="cliente.sesso" />
						&nbsp;
						<s:text name="cliente.un.asterisco" />
					</div>
					<div style="float: left; width: 34%">
						<s:select name="abbonato.sesso" id="abbonato.sesso"
							list="#{'M':'M', 'F':'F'}" cssClass="extremeTableFieldsLong"
							emptyOption="false" />
					</div>
					<div style="float: left; width: 33%">
						<font color="red"><span id="err_abbonato.sesso"
							class="error" style="width: 140px; display: block;"><s:fielderror>
									<s:param>abbonato.sesso</s:param>
								</s:fielderror></span></font>
					</div>
				</div>
				<div style="float: left; width: 49%">
					<div style="float: left; width: 30%">
						<s:text name="cliente.prodotto" />
						&nbsp;
						<s:text name="cliente.un.asterisco" />
					</div>
					<div style="float: left; width: 34%">
						<s:select name="abbonato.idProdotto" id="idProdotto"
							listKey="idProdotto" listValue="titoloPrimaParte" list="prodotti"
							cssClass="extremeTableFieldsLong" emptyOption="true" />
					</div>
					<div style="float: left; width: 33%">
						<font color="red"><span id="err_abbonato.idProdotto"
							class="error" style="width: 140px; display: block;"><s:fielderror>
									<s:param>abbonato.idProdotto</s:param>
								</s:fielderror></span></font>
					</div>
				</div>
			</div>
			<div style="width: 100%; height: 30px; margin-top: 20px;">
				<div style="float: left; width: 98%">
					<div style="float: left; width: 60%">
						<span id="regolamento" class="extremeTableFields"></span>
					</div>
					<div style="float: left; width: 19%">
						<s:checkbox name="privacy" id="privacy" cssClass="tableFields"
							value="true" />
					</div>
					<div style="float: left; width: 19%">
						<font color="red"><span id="err_abbonato.privacy"
							class="error" style="width: 140px; display: block;"><s:fielderror>
									<s:param>abbonato.privacy</s:param>
								</s:fielderror></span></font>
					</div>
				</div>
			</div>
		</fieldset>
		<br>
		<div style="width: 100%; height: 50px;">
			<div style="width: 120px; margin-left: auto; margin-right: auto">
				<input type="button" value="<s:text name='igeriv.memorizza'/>"
					align="center" class="extremeTableFieldsLong btn primary"
					width="120px" style="text-align: center; font-size: 12px"
					onclick="doSubmit();" />
			</div>
		</div>
		<div style="width: 100%; height: 30px;">
			<s:text name="cliente.campi.obbligatori.menta" />
		</div>
	</div>
	<s:hidden name="abbonato.codLocalita" id="abbonato.codLocalita" />
	<s:hidden name="abbonato.codProvincia" id="abbonato.codProvincia" />
</s:form>

