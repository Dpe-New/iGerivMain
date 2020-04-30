<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 220px;
}
</style>
<s:form id="ProposteReteEdicolaReportFilterForm"
	action="livellamenti_showProposte.action" method="POST" theme="simple"
	onsubmit="return (ray.ajax())" validate="false">
	<div id="pubMainDiv" style="width: 100%;">
		<div style="width: 100%; text-align: left">
			<div class="required" id="errorDiv"
				style="width: 100%; height: 15px; align: center; text-align: center">
				<font color='red'><s:actionerror /></font>
			</div>
			<fieldset class="filterBolla"
				style="margin-top: 0px; top: 0px; width: 650px; text-align: left">
				<legend style="font-size: 100%">
					<s:text name="igeriv.proposte.vendita.rete.edicola" />
				</legend>
				<div style="width: 650px" id="filtroVendita2">
					<div class="required">
						<div style="float: left; width: 280px; margin-top: 10px">
							<s:text name="igeriv.intervallo.date" />
						</div>
						<div
							style="float: left; width: 350px; text-align: left; margin-top: 10px">
							<s:text name="igeriv.da" />
							&nbsp;&nbsp;
							<s:textfield name="strDataDa" id="strDataDa"
								cssClass="extremeTableFields" cssStyle="width:80px;"
								disabled="false" />
							&nbsp;&nbsp;
							<s:text name="igeriv.a" />
							&nbsp;&nbsp;
							<s:textfield name="strDataA" id="strDataA"
								cssClass="extremeTableFields" cssStyle="width:80px;"
								disabled="false" />
						</div>
					</div>
					<div style="width: 650px" id="filtroVendita1">
						<div class="required">
							<div style="float: left; width: 307px; margin-top: 10px">
								<s:text name="igeriv.richiesta.rifornimenti.stato.proposta" />
							</div>
							<div
								style="float: left; width: 300px; text-align: left; margin-top: 10px">
								<s:select name="stato" id="stato" listKey="keyInt"
									listValue="value"
									list="#application.listStatiPropostaReteEdicola"
									cssClass="extremeTableFields" cssStyle="width:180px" />
							</div>
						</div>
					</div>
					<div class="required" style="align: left; text-align: left;">
						<div
							style="float: left; width: 650px; align: center; text-align: center; margin-top: 20px">
							<s:submit name="submitRicerca" id="ricerca"
								key="dpe.contact.form.ricerca" align="center"
								cssClass="required" cssStyle="text-align:center; width:150px" />
						</div>
					</div>
				</div>
			</fieldset>
		</div>
	</div>
</s:form>