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
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<s:form id="filterForm" action="pneGiacenza_showGiacenza.action"
	method="POST" theme="simple"
	onsubmit="return (doSubmit() && ray.ajax())" validate="false">
	<div class="required" id="errorDiv"
		style="width: 100%; align: center; text-align: center; height: 20px;">
		<font color='red'><s:actionerror /></font>
	</div>
	<div style="width: 100%; text-align: center; margin-top: 10px;">
		<fieldset class="filterBolla" style="width: 600px; height: 200px;">
			<legend style="font-size: 100%">
				<s:property value="tableTitle" />
			</legend>
			<div style="width: 550px;">
				<div style="float: left; width: 250px;">
					<s:text name="igeriv.categoria" />
					<s:select name="codCategoria" id="categoria" listKey="codCategoria"
						listValue="descrizioneNoHtml" list="categorie"
						cssStyle="width:250px;" emptyOption="true" />
				</div>
				<div
					style="float: left; width: 240px; margin-left: 20px; height: 80px;">
					<s:text name="igeriv.sotto.categoria" />
					<select name="codSottoCategoria" id="sottocategoria"
						class="required" style="width: 250px;">
					</select>
				</div>
			</div>
			<div style="width: 570px;">
				<div style="float: left; width: 240px;">
					<s:text name="igeriv.ricerca.cod.prodotto.desrizione" />
					<br>
					<em>*</em>&nbsp;<span style="font-size: 70%; font-style: italic"><s:text
							name="dpe.primi.tre.caratteri" /></span>
				</div>
				<div style="float: left; width: 300px;">
					<s:textfield id="prodottoLabel" name="prodottoLabel"
						cssStyle="width: 300px" cssClass="tableFields" />
				</div>
			</div>
			<div style="float: left; width: 550px; margin-top: 0px">
				<s:submit name="ricerca" id="ricerca" key="dpe.contact.form.ricerca"
					align="center" cssStyle="align:center; width:80px" />
			</div>
		</fieldset>
	</div>
	<s:hidden name="codProdotto" id="codProdotto" />
</s:form>
