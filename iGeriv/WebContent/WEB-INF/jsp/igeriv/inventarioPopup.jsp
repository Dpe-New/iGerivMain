<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<div id="mainDiv" style="width: 100%; text-align: center">
	<fieldset class="filterBolla" style="height: 100px; width: 50%">
		<legend style="font-size: 100%">
			<s:property value="tableTitle" escape="false" />
		</legend>
		<div class="required">
			<div
				style="margin-top: 30px; margin-left: auto; margin-right: auto; text-align: center; width: 350px; white-space: nowrap;">
				<s:text name="igeriv.data.inventario" />
				&nbsp;&nbsp;
				<s:textfield name="strNewDataInventario" id="strNewDataInventario"
					cssStyle="width:100px" />
			</div>
		</div>
	</fieldset>
</div>
<div style="width: 100%; margin-top: 50px;">
	<div
		style="text-align: center; margin-left: auto; margin-right: auto; width: 200px; height: 50px; margin-top: 10px;">
		<input type="button" value="<s:text name='igeriv.memorizza'/>"
			name="igeriv.memorizza" id="memorizza" class="tableFields"
			style="width: 100px; text-align: center"
			onclick="javascript: saveInventario();" />
	</div>
</div>
