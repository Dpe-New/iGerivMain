<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 180px;
}

div#content1 {
	height: 500px;
}
</style>
<s:form id="configurazioniFilterForm"
	action="configurazioni_showMenuSceltaRapida.action" method="POST"
	theme="simple" validate="false" onsubmit="return (ray.ajax())">
	<div style="width: 100%; text-align: center">
		<fieldset class="filterBolla" style="height: 180px;">
			<legend style="font-size: 100%">${tableTitle}</legend>
			<div class="required">
				<div style="float: left; width: 200px; text-align: left">
					<s:text name="dpe.tipo.pubblicazione" />
				</div>
				<div style="float: left; width: 200px;">
					<s:iterator value="%{#session['listTipoPubblicazione']}"
						status="status">
						<input type="radio" name="tipoPubblicazione"
							value='<s:text name="key"/>'
							<s:if test="key eq tipoPubblicazione">checked</s:if>>&nbsp;&nbsp;<s:text
							name="value" />&nbsp;
					</s:iterator>
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 200px; text-align: left">
					<s:text name="igeriv.periodo" />
				</div>
				<div style="float: left; width: 80px;">
					<s:select name="periodo" id="periodo"
						list="%{#application['listPeriodoGiorni']}" cssStyle="width:80px" />
				</div>
				<div style="float: left; width: 100px;">
					<s:text name="igeriv.giorni" />
				</div>
			</div>
			<div class="required">
				<div
					style="float: left; width: 450px; margin-top: 20px; margin-left: 40px">
					<input type="button" name="ricercaBase" id="ricercaBase"
						value="<s:text name="dpe.contact.form.ricerca.pubblicazioni.piu.vendute"/>"
						align="center" style="align: center; width: 250px"
						onclick="javascript: $('#configurazioniFilterForm').submit();" />
				</div>
				&nbsp;&nbsp;
				<div
					style="float: left; width: 450px; margin-top: 20px; margin-left: 40px">
					<input type="button" id="butScegliAltraPubblicazione"
						name="butScegliAltraPubblicazione"
						value="<s:text name="dpe.contact.form.ricerca.manuale.pubblicazioni"/>"
						align="center" style="align: center; width: 250px" />
				</div>
			</div>
		</fieldset>
	</div>
</s:form>
