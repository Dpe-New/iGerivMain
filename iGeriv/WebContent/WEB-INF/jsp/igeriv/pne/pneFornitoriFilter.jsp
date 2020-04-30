<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:form id="filterForm" action="pneFornitori_showFornitori.action"
	method="POST" theme="simple" validate="false"
	onsubmit="return (ray.ajax())">
	<div style="width: 100%; text-align: center">
		<fieldset class="filterBolla" style="height: 140px; width: 400px;">
			<legend style="font-size: 100%">
				<s:property value="tableTitle" />
			</legend>
			<div class="required">
				<div style="float: left; width: 200px;">
					<s:text name="dpe.rag.sociale" />
				</div>
				<div style="float: left; width: 100px;">
					<s:textfield label="" name="ragioneSociale" id="ragioneSociale" />
				</div>
				<div style="float: right; width: 100px;">
					<font color="red" size="1"><span id="err_ragioneSociale"><s:fielderror>
								<s:param>ragioneSociale</s:param>
							</s:fielderror></span></font>
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 200px;">
					<s:text name="dpe.piva" />
				</div>
				<div style="float: left; width: 100px;">
					<s:textfield label="" name="piva" id="piva" />
				</div>
				<div style="float: right; width: 100px;">
					<font color="red" size="1"><span id="err_piva"><s:fielderror>
								<s:param>piva</s:param>
							</s:fielderror></span></font>
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
