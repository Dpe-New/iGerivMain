<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %> 
<style>
	div#filter { height:80px; }
	div#footer { margin-top:10px; }
</style>
<div style="text-align:center">
	<fieldset class="filterBolla" style="height:80px"><legend style="font-size:120%;"><s:property value="filterTitle" /></legend>
		<div class="required" style="float:left; margin-top:20px">
			<div style="float:left; text-align:left; width:130px; margin-left:20px; font-size:120%;">
				<s:text name="igeriv.data.inventario" />
			</div>
			<div style="float:left; width:300px">
				<s:select 
					name="strDataInventario" 
					id="strDataInventario" 
					list="listInventarioDto"
					listKey="keyStringForSelectBox"
					listValue="valueStringForSelectBox"
					emptyOption="true"
					cssStyle="width:150px; font-size:150%">
				</s:select>			
			</div>
			<div style="float:left; text-align:left; width:150px">&nbsp;</div>
		</div>
	</fieldset>
</div>
