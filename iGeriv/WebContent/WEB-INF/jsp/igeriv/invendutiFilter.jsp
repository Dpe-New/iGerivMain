<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 230px;
}
</style>
<s:form id="PubblicazioniInveduteFilterForm"
	action="invenduti_show.action" onsubmit="return (ray.ajax())"
	method="POST" theme="simple" validate="false">
	<div style="width: 100%; text-align: center;">
		<div id="errorDiv"
			style="width: 100%; align: center; text-align: center; height: 20px; margin-top: -10px">
			<font color='red'><s:actionerror /></font>
		</div>
		<fieldset class="filterBolla" style="width: 560px;">
			<legend style="font-size: 100%">
				<s:text name="tableTitle" />
			</legend>
			<div style="width: 560px;">
				<div style="float: left; width: 250px;">
					<s:text name="igeriv.pubb.uscite.ultimi.mesi" />
				</div>
				<div style="float: left; width: 280px;">
					<s:select name="numMesiDataUscitaDaInventario"
						id="numMesiDataUscitaDaInventario"
						list="#{'3':'3','6':'6','9':'9','12':'12'}" cssStyle="width:50px" />
					&nbsp;
					<s:text name="plg.calendario.mesi" />
				</div>
			</div>
			<div style="align: center; width: 560px">
				<div style="float: left; width: 250px;">
					<s:text name="igeriv.titolo" />
				</div>
				<div style="float: left; width: 280px;">
					<s:textfield label="" name="titolo" id="titolo"
						cssStyle="width:200px;" />
				</div>
			</div>
			<div style="align: center; width: 560px">
				<div style="float: left; width: 250px;">
					<s:text name="igeriv.basato.su" />
				</div>
				<div style="float: left; width: 280px;">
					<s:select label="" name="baseCalcolo" id="baseCalcolo"
						listKey="keyInt" listValue="value" list="listBaseCalcolo"
						emptyOption="false" />
				</div>
			</div>
			<div style="align: center; width: 100%; float: left; height: 30px;">
				<div style="float: left; width: 50%;">
					<s:text name="igeriv.escludi.conto.deposito" />
				</div>
				<div style="float: left; width: 45%;">
					<s:checkbox label="" name="escludiCD" id="escludiCD" />
				</div>
			</div>
			<div style="align: center; width: 100%; float: left; height: 30px;">
				<div style="float: left; width: 50%;">
					<s:text name="igeriv.escludi.quotidiani" />
				</div>
				<div style="float: left; width: 45%;">
					<s:checkbox label="" name="escludiQuotidiani"
						id="escludiQuotidiani" />
				</div>
			</div>
			<div style="align: center; text-align: center;">
				<div
					style="float: left; width: 560px; align: center; text-align: center;">
					<input type="button" name="ricerca" id="ricerca"
						value="<s:text name="dpe.contact.form.ricerca"/>" align="center"
						style="align: center; width: 150px" />
				</div>
			</div>
		</fieldset>
	</div>
</s:form>