<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:form id="filterForm" action="%{actionName}" method="POST"
	theme="simple" validate="false" onsubmit="return (ray.ajax())">
	<div style="width: 100%; text-align: center">
		<fieldset class="filterBolla">
			<legend style="font-size: 100%">
				<s:property value="tableTitle" />
			</legend>
			<div class="required" style="float: left">
				<div style="float: left; text-align: left; width: 240px">
					<s:text name="igeriv.data.tipo.estratto.conto" />
				</div>
				<div style="float: left; width: 200px">
					<s:select name="dataEstrattoConto" id="dataEstrattoConto"
						list="%{#session['listDateEstrattoConto']}" listKey="key"
						listValue="dataEstrattoContoStr" />
				</div>
			</div>
			<div style="width: 100%; text-align: center; float: left">
				<div>
					<s:submit name="submitFilter" id="submitFilter"
						key="dpe.contact.form.submit" align="center"
						cssStyle="align:center; width:80px" />
				</div>
			</div>
		</fieldset>
	</div>
</s:form>
