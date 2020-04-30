<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 220px;
}

div#footer {
	margin-top: 10px;
}
</style>
<s:form id="InventarioPresuntoFilterForm"
	action="inventariop_showInventario.action"
	onsubmit="return (ray.ajax())" method="POST" theme="simple"
	validate="false">
	<div style="width: 100%; text-align: center">
		<fieldset class="filterBolla" style="height: 210px; width: 520px">
			<div class="required" style="float: left; margin-top: 20px">
				<div
					style="float: left; text-align: left; width: 240px; margin-left: 10px;">
					<s:text name="igeriv.conto.deposito" />
				</div>
				<div style="float: left; width: 250px">
					<s:select name="filtroContoDeposito" id="filtroContoDeposito"
						list="listFiltroContoDeposito" listKey="key" listValue="value"
						emptyOption="false" cssStyle="width:230px; height:25px">
					</s:select>
				</div>
			</div>
			<div class="required" style="float: left; margin-top: 20px">
				<div
					style="float: left; text-align: left; width: 240px; margin-left: 10px;">
					<s:text name="igeriv.mostra.pubblicazioni" />
				</div>
				<div style="float: left; width: 250px">
					<s:select name="filtroTipoPubblicazioni"
						id="filtroTipoPubblicazioni" list="tipoPubblicazioni"
						listKey="key" listValue="value" emptyOption="false"
						cssStyle="width:230px; height:25px">
					</s:select>
				</div>
			</div>
			<div class="required" style="float: left; margin-top: 20px">
				<div
					style="float: left; text-align: left; width: 240px; margin-left: 10px;">
					<s:text name="igeriv.escludi.pubblicazioni.senza.prezzo" />
				</div>
				<div style="float: left; width: 250px">
					<s:checkbox name="escludiPubblicazioniSenzaPrezzo"
						id="escludiPubblicazioniSenzaPrezzo" cssClass="tableFields"
						value="escludiPubblicazioniSenzaPrezzo" />
				</div>
			</div>
			<div class="required"
				style="float: left; margin-top: 20px; width: 500px; text-align: center">
				<div>
					<s:submit name="submitFilter" id="submitFilter"
						key="igeriv.esegui.inventario" align="center"
						cssStyle="align:center; width:150px" />
				</div>
			</div>
		</fieldset>
	</div>
</s:form>