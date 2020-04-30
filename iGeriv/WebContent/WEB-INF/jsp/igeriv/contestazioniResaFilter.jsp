<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 200px;
}

div#content1 {
	height: 500px;
}
</style>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<s:form id="MancanzeFilterForm"
	action="contestazioniResa_showContestazioniResa.action"
	onsubmit="return (ray.ajax())" method="POST" theme="simple"
	validate="false">
	<div style="width: 100%; text-align: center;">
		<div class="required" id="errorDiv"
			style="width: 100%; align: center; text-align: center; height: 20px;">
			<font color='red'><s:actionerror /></font>
		</div>
		<fieldset class="filterBolla" style="width: 450px; text-align: center">
			<legend style="font-size: 100%">
				<s:text name="filterTitle" />
			</legend>
			<div class="required"
				style="margin-left: auto; margin-right: auto; align: center; text-align: center; width: 450px">
				<div style="float: left; width: 80px;">
					<s:text name="igeriv.data.resa" />
				</div>
				<div style="float: left; width: 320px;">
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
			<div class="required"
				style="margin-left: auto; margin-right: auto; align: center; text-align: center; width: 450px">
				<div style="float: left; width: 100px;">
					<s:text name="igeriv.titolo" />
				</div>
				<div style="float: left; width: 300px;">
					<s:textfield label="" name="titolo" id="titolo" cssClass="required"
						cssStyle="width:196px;" />
				</div>
			</div>
			<div class="required"
				style="margin-left: auto; margin-right: auto; align: center; text-align: center; width: 450px">
				<div style="float: left; width: 100px;">
					<s:text name="igeriv.richiesta.rifornimenti.quantita.stato" />
				</div>
				<div style="float: left; width: 300px;">
					<s:select label="" name="stato" id="stato" listKey="key"
						listValue="value" list="%{#application['statiContestazioniResa']}"
						emptyOption="true" cssStyle="width: 200px" cssClass="required" />
				</div>
			</div>
			<div class="required" style="align: center; text-align: center;">
				<div
					style="float: left; margin-top: 20px; width: 450px; text-align: center;">
					<s:submit name="ricerca" id="ricerca"
						key="dpe.contact.form.ricerca" align="center"
						cssStyle="align:center; width:150px" />
				</div>
			</div>
		</fieldset>
	</div>
</s:form>