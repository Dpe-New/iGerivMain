<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 150px;
}
</style>
<s:form id="filterForm" action="promemoria_showListPromemoria.action"
	method="POST" theme="simple" onsubmit="return (ray.ajax())"
	validate="false">
	<div style="width: 100%; text-align: center">
		<fieldset class="filterBolla" style="width: 620px; height: 120px;">
			<legend style="font-size: 100%">
				<s:property value="filterTitle" />
			</legend>
			<div style="height: 35px">
				<div style="float: left; width: 200px;">
					<s:text name="igeriv.intervallo.date" />
				</div>
				<div style="float: left; width: 400px; text-align: left">
					<s:text name="igeriv.da" />
					&nbsp;&nbsp;
					<s:textfield name="dataDaStr" id="dataDaStr" cssClass="tableFields"
						cssStyle="width:90px;" disabled="false" />
					&nbsp;&nbsp;
					<s:text name="igeriv.a" />
					&nbsp;&nbsp;
					<s:textfield name="dataAStr" id="dataAStr" cssClass="tableFields"
						cssStyle="width:90px;" disabled="false" />
				</div>
			</div>
			<div style="width: 620px; align: center; text-align: center;">
				<div style="width: 380px; margin-left: auto; margin-right: auto;">
					<div
						style="float: left; width: 180px; align: center; text-align: center;">
						<input type="button" name="ricerca" id="ricerca"
							value="<s:text name="dpe.contact.form.ricerca"/>" align="center"
							style="align: center; width: 150px" onclick="doSubmit();" />
					</div>
					<div
						style="float: left; width: 180px; align: center; text-align: center;">
						<input type="button" name="nuovo" id="nuovo"
							value="<s:text name="plg.inserisci.nuovo"/>" align="center"
							style="align: center; width: 150px" />
					</div>
				</div>
			</div>
		</fieldset>
	</div>
</s:form>
