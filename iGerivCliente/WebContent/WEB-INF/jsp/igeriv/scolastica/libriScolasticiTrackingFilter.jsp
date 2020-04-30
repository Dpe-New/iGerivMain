<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 140px;
}
</style>


<s:form id="filterTrackingForm" action="%{actionName}" method="POST"	theme="simple" validate="false" onsubmit="return (ray.ajax())">
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
							<s:text name="dpe.codice.numero.ordine" />
						</div>
						<div style="float: left; width: 200px;">
							<s:textfield label="" name="numOrdineTxtTracking" id="numOrdineTxtTracking" />
						</div>
						<div style="float: right; width: 200px;">
							<font color="red" size="1"><span id="err_numOrdineTxtTracking"><s:fielderror>
										<s:param>numOrdineTxtTracking</s:param>
									</s:fielderror></span></font>
						</div>
					</div>
					<div class="required">
						<div style="float: left; width: 750px; margin-top: 10px">
							<s:submit name="ricerca" id="ricerca"
								key="dpe.contact.form.ricerca" align="center"
								cssStyle="align:center; width:80px" />
						</div>
					</div>
					<s:if test="%{#request.clienteEdicola != null}">
						<div style="float: left; width: 750px; margin-top: 10px">
								<strong>Cliente : </strong>${clienteEdicola.nome}&nbsp;${clienteEdicola.cognome}
						</div>
					</s:if>
				</div>
			</fieldset>
		
	</div>
</s:form>
