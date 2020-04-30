<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 140px;
}
</style>


<s:form id="filterConsegnaEdicolaForm" action="%{actionName}" method="POST"	theme="simple" validate="false" onsubmit="return (ray.ajax())">
	<div style="width: 100%; text-align: center">
		<div class="required" id="errorDiv"
			style="width: 100%; align: center; text-align: center; height: 20px; margin-top: -10px">
			<font color='red'><s:actionerror /></font>
		</div>

			<fieldset class="filterBolla" style="height: 180px; width: 750px">
				<legend style="font-size: 100%">${tableTitle}</legend>
				<div id="ricercaCliente">

					
					<div class="required">
						<div style="float: left; width: 300px;">
							<s:text name="dpe.ordine.numero.collo" />
						</div>
						<div style="float: left; width: 200px;">
							<s:textfield label="" name="numeroCollo" id="numeroCollo" validateIsNumeric="true" cssClass="required"  />
						</div>
						<div style="float: right; width: 200px;">
							<font color="red" size="1"><span id="err_numeroCollo"><s:fielderror>
										<s:param>numeroCollo</s:param>
									</s:fielderror></span></font>
						</div>
					</div>
					<div class="required">
						<div style="float: left; width: 750px; margin-top: 10px">
							<s:submit name="ricerca" id="ricerca"
								key="dpe.contact.form.ricerca" align="center"
								cssStyle="align:center; width:80px" />
						</div>
						<s:if test="%{#request.listResLibri != null && !#request.listResLibri.isEmpty()}">
						<input type="button" name="igeriv.memorizza"
							id="memorizzaFuoriVoce" value="<s:text name='igeriv.memorizza'/>"
							align="center" class="tableFields"
							style="align: center; width: 150px; text-align: center"
							onclick="javascript: setTimeout(function() {return (setFormAction('LibriScolasticiConsegnaEdicolaForm','libriScolasticiConsegnaEdicola_saveArrivoInEdicolaCollo.action', '', '', false, '', function() {$('#ricerca').trigger('click')}, '', false, '', false));},10);" />	
						</s:if>
					</div>
				</div>
			</fieldset>
		
	</div>
</s:form>
