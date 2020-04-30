<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 200px;
}
</style>


<s:form id="filterForm" action="%{actionName}" method="POST"
	theme="simple" validate="false" onsubmit="return (ray.ajax())">
	<div style="width: 100%; text-align: center">
		<div class="required" id="errorDiv"
			style="width: 100%; align: center; text-align: center; height: 20px; margin-top: -10px">
			<font color='red'><s:actionerror /></font>
		</div>
		<fieldset class="filterBolla" style="height: 180px; width: 750px">
			<legend style="font-size: 100%">${tableTitle}</legend>
			<div id="ricercaCliente">
				
				<div class="required">
					<div style="float: left; width: 100px;">
						<s:text name="dpe.ricerca.testo" />
					</div>
					<div style="float: left; width: 520px;">
						<s:textfield label="" name="testoRicerca" id="testoRicerca"	cssStyle="width: 300px" />
					</div>
				</div>
				<div class="required">
					<div
						style="float: left; width: 650px; height: 45px; margin-top: 10px">
						<s:submit name="ricerca" id="ricerca"
							key="dpe.contact.form.ricerca" align="center"
							cssStyle="align:center; width:180px" />
						&nbsp;&nbsp;
					</div>
					<div style="float: left; width: 50px; height: 45px; margin-top: 10px">
						<a href="#" onclick="javascript:openDetailOrdine();" >
						<img src="/app_img/cat_3d_64px.png" width="45px" height="45px"
							border="0" style="border-style: none"
							 /></a>
					</div>
				</div>
				<div class="required">
					<div style="float: left; width: 200px;">
<%-- 						<s:text name="%{#request.msgOrdine}" /> --%>
					</div>
					<div style="float: right; width: 100px;">
						<s:text name="%{#request.countLibriCarrello}" />
					</div>
				</div>
			</div>
		</fieldset>
	</div>

	<s:hidden name="idNumeroOrdine" id="idNumeroOrdine" 	value="%{#request.numeroOrdine}" />

</s:form>
