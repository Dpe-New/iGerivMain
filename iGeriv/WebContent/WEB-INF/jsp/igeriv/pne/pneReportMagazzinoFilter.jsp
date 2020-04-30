<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 260px;
}

div#content1 {
	height: 550px;
}
</style>
<s:set name="templateSuffix" value="'ftl'" />
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<s:form id="filterForm" action="pneReportMagazzino_showReport.action"
	method="POST" theme="simple"
	onsubmit="return (validateFields(true) && doSubmit() && ray.ajax())"
	validate="false">
	<div class="required" id="errorDiv"
		style="width: 100%; align: center; text-align: center; height: 20px;">
		<font color='red'><s:actionerror /></font>
	</div>
	<div style="width: 100%; text-align: center; margin-top: 10px;">
		<fieldset class="filterBolla" style="width: 800px; height: 260px;">
			<legend style="font-size: 100%">
				<s:property value="tableTitle" />
			</legend>
			<div class="required">
				<div style="float: left; width: 150px;">
					<s:text name="igeriv.pne.report.magazzino.data.registrazione" />
				</div>
				<div style="float: left; width: 400px; text-align: left">
					<s:text name="igeriv.da" />
					&nbsp;&nbsp;
					<s:textfield name="dataIniziale" id="dataIniziale"
						cssClass="tableFields" cssStyle="width:90px;" disabled="false" />
					&nbsp;&nbsp;
					<s:text name="igeriv.a" />
					&nbsp;&nbsp;
					<s:textfield name="dataFinale" id="dataFinale"
						cssClass="tableFields" cssStyle="width:90px;" disabled="false" />
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 250px;">
					<span id="labelForn"><s:text
							name="igeriv.pne.report.magazzino.conto" /></span>
					<s:select name="codiceConto" id="codiceConto"
						listKey="codFornitore" listValue="nome" list="conti"
						cssStyle="width:250px;height:80px;" multiple="true" />
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 250px;">
					<s:text name="igeriv.categoria" />
					<s:select name="codCategoria" id="codCategoria"
						listKey="codCategoria" listValue="descrizioneNoHtml"
						list="categorie" cssStyle="width:250px;height:80px;"
						multiple="true" />
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 250px;">
					<s:text name="igeriv.causale" />
					<s:select name="codiceCausale" id="codiceCausale"
						listKey="codiceCausale" listValue="descrizione"
						list="%{#application['listCausali']}"
						cssStyle="width:250px;height:80px;" multiple="true" />
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 200px;">
					<s:text name="igeriv.cod.prodotto.dl.pne.o.descr" />
					<br>
					<em>*</em>&nbsp;<span style="font-size: 70%; font-style: italic"><s:text
							name="dpe.primi.tre.caratteri" /></span>
				</div>
				<div style="float: left; width: 300px;">
					<s:textfield id="prodottoLabel" name="prodottoLabel"
						cssStyle="width: 300px" cssClass="tableFields" />
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 800px; margin-top: 0px">
					<s:submit name="ricerca" id="ricerca"
						key="dpe.contact.form.ricerca" align="center"
						cssStyle="align:center; width:80px" />
					&nbsp;&nbsp; <input type="button" name="azzeraCriteri"
						id="azzeraCriteri"
						value='<s:text name="igeriv.azzera.criteri.ricerca"/>'
						style="align: center; width: 150px" />
				</div>
			</div>
		</fieldset>
	</div>
	<s:hidden name="codProdotto" id="codProdotto" />
</s:form>
