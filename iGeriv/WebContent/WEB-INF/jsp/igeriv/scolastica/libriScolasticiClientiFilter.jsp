<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter { height: 220px;}
div#content1 { height:500px;}
</style>


<s:form id="filterForm" action="%{actionName}" method="POST"
	theme="simple" validate="false" onsubmit="return (ray.ajax())">
	<div style="width: 100%; text-align: center">
		<div class="required" id="errorDiv"
			style="width: 100%; align: center; text-align: center; height: 20px; margin-top: -10px">
			<font color='red'><s:actionerror /></font>
		</div>

		<s:if
			test="actionName != null && actionName.contains('libriScolasticiClienti_showClienti')">

			<fieldset class="filterBolla" style="height: 180px; width: 750px">
				<legend style="font-size: 100%">${tableTitle}</legend>
				<div id="ricercaCliente">

					<div class="required">
						<div style="float: left; width: 300px;">
							<s:text name="dpe.nome" />
						</div>
						<div style="float: left; width: 200px;">
							<s:textfield label="" name="nome" id="nome" />
						</div>
						<div style="float: right; width: 200px;">
							<font color="red" size="1"><span id="err_nome"><s:fielderror>
										<s:param>nome</s:param>
									</s:fielderror></span></font>
						</div>
					</div>
					<div class="required">
						<div style="float: left; width: 300px;">
							<s:text name="dpe.cognome" />
						</div>
						<div style="float: left; width: 200px;">
							<s:textfield label="" name="cognome" id="cognome" />
						</div>
						<div style="float: right; width: 200px;">
							<font color="red" size="1"><span id="err_cognome"><s:fielderror>
										<s:param>cognome</s:param>
									</s:fielderror></span></font>
						</div>
					</div>
					<div class="required">
						<div style="float: left; width: 300px;">
							<s:text name="dpe.codice.codice.cliente" />
						</div>
						<div style="float: left; width: 200px;">
							<s:textfield label="" name="codCliente" id="codCliente" />
						</div>
						<div style="float: right; width: 200px;">
							<font color="red" size="1"><span id="err_codCliente"><s:fielderror>
										<s:param>codCliente</s:param>
									</s:fielderror></span></font>
						</div>
					</div>
					<div class="required">
						<div style="float: left; width: 300px;">
							<s:text name="dpe.codice.numero.ordine" />
						</div>
						<div style="float: left; width: 200px;">
							<s:textfield label="" name="numOrdineTxt" id="numOrdineTxt" />
						</div>
						<div style="float: right; width: 200px;">
							<font color="red" size="1"><span id="err_numOrdineTxt"><s:fielderror>
										<s:param>numOrdineTxt</s:param>
									</s:fielderror></span></font>
						</div>
					</div>
					<div class="required">
						<div style="float: left; width: 500px; margin-top: 10px">
							<s:submit name="ricerca" id="ricerca"
								key="dpe.contact.form.ricerca" align="center"
								cssStyle="align:center; width:80px" />
							&nbsp;&nbsp;
							<input type="button" id="butNuovoGestioneClienti" name="butNuovoGestioneClienti" value="<s:text name='plg.inserisci.nuovo'/>" align="center"	cssStyle="align:center; width:80px" />
						
						</div>
						<div style="float: left; width: 200px; margin-top: 0px">
							MANUALE OPERATIVO
							<a href="/pdf/Manuale_Operativo_Scolastica.pdf" title="DOWNLOAD MANUALE OPERATIVO" target="_blank">
							<img src="/app_img/pdf_book.png" width="30px" height="30px" border="0" style="border-style: none"/>
							</a>
						</div>
					</div>
				</div>
			</fieldset>
		</s:if>
		<s:if
			test="actionName != null && actionName.contains('libriScolasticiClienti_showOrdiniClienti')">
			<fieldset class="filterBolla" style="height: 180px; width: 750px">
				<legend style="font-size: 100%">${tableTitle}</legend>
				<div id="ricercaCliente">

					<div class="required">
						<div style="float: left; width: 300px;">
							<s:text name="dpe.nome" />
						</div>
						<div style="float: left; width: 200px;">
							<s:textfield label="" name="nome" id="nome" />
						</div>
						<div style="float: right; width: 200px;">
							<font color="red" size="1"><span id="err_nome"><s:fielderror>
										<s:param>nome</s:param>
									</s:fielderror></span></font>
						</div>
					</div>
					<div class="required">
						<div style="float: left; width: 300px;">
							<s:text name="dpe.cognome" />
						</div>
						<div style="float: left; width: 200px;">
							<s:textfield label="" name="cognome" id="cognome" />
						</div>
						<div style="float: right; width: 200px;">
							<font color="red" size="1"><span id="err_cognome"><s:fielderror>
										<s:param>cognome</s:param>
									</s:fielderror></span></font>
						</div>
					</div>
					<div class="required">
						<div style="float: left; width: 300px;">
							<s:text name="dpe.codice.codice.cliente" />
						</div>
						<div style="float: left; width: 200px;">
							<s:textfield label="" name="codCliente" id="codCliente" />
						</div>
						<div style="float: right; width: 200px;">
							<font color="red" size="1"><span id="err_codCliente"><s:fielderror>
										<s:param>codCliente</s:param>
									</s:fielderror></span></font>
						</div>
					</div>
					<div class="required">
						<div style="float: left; width: 750px; margin-top: 10px">
							<s:submit name="ricerca" id="ricerca"
								key="dpe.contact.form.ricerca" align="center"
								cssStyle="align:center; width:80px" />
							&nbsp;&nbsp;
							<%-- 						<s:if test="actionName != null && actionName.equals('libriScolasticiClienti_showOrdiniClienti.action')"> --%>
							<%-- 							<input type="button" id="butNuovo" value="<s:text name='plg.inserisci.nuovo'/>" align="center" cssStyle="align:center; width:80px"/> --%>
							<%-- 						</s:if> --%>
						</div>
					</div>
				</div>
			</fieldset>
		</s:if>




	</div>
</s:form>
