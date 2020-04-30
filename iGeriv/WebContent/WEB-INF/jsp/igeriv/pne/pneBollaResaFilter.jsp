<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#breadCrumb {
	height: 40px;
}

div#filter {
	height: 190px;
}

div#footer {
	margin-top: 40px;
}
</style>
<s:form id="filterForm" action="pneResa_showBolle.action" method="POST"
	theme="simple" validate="false" onsubmit="return (ray.ajax())">
	<div class="required" id="errorDiv"
		style="width: 100%; align: center; text-align: center; height: 20px; margin-top: -10px">
		<font color='red'><s:actionerror escape="false" /></font>
	</div>
	<div style="width: 100%; text-align: center">
		<fieldset class="filterBolla"
			style="text-align: left; height: 160px; width: 300px;">
			<legend style="font-size: 100%">
				<s:property value="tableTitle" />
			</legend>
			<div class="required">
				<div style="float: left; width: 200px;">
					<s:text name="igeriv.fornitore" />
				</div>
				<div style="float: left; width: 100px;">
					<s:select name="codFornitore" id="codFornitore"
						listKey="codFornitore" listValue="nome" list="fornitori"
						cssStyle="width:170px" />
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 200px;">
					<s:text name="igeriv.data.documento" />
				</div>
				<div style="float: left; width: 100px;">
					<s:textfield label="" name="dataDocumento" id="dataDocumento" />
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 200px;">
					<s:text name="igeriv.numero.documento" />
				</div>
				<div style="float: left; width: 100px;">
					<s:textfield label="" name="numeroDocumento" id="numeroDocumento" />
				</div>
			</div>
			<div class="required">
				<div
					style="float: left; margin-left: auto; margin-right: auto; text-align: center; width: 500px; margin-top: 10px">
					<s:submit name="ricerca" id="ricerca"
						key="dpe.contact.form.ricerca" align="center"
						cssStyle="align:center; width:80px" />
					&nbsp;&nbsp; <input type="button" id="butNuovo"
						value="<s:text name='plg.inserisci.nuovo'/>" align="center"
						cssStyle="align:center; width:80px" />
				</div>
			</div>
		</fieldset>
	</div>
</s:form>
