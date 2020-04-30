<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
	div#filter { height:200px; } 
</style>
<s:form id="filterForm" action="estrattoContoClientiEdicola_showClientiEstrattoConto.action" method="POST" theme="simple" validate="false" onsubmit="return (checkFields() && ray.ajax())">	
	<div class="required" id="errorDiv" style="width:100%; align:center; text-align:center; height:20px;"><font color='red'><s:actionerror /></font></div>
	<div style="width:100%; text-align:center">
		<fieldset class="filterBolla" style="width:620px"><legend style="font-size:100%"><s:property value="tableTitle" escape="false"/></legend>
			<div>
				<div style="float:left; text-align:left; width:240px;">
					<s:text name="igeriv.data.competenza.estratto.conto" />
				</div>
				<div style="float:left; text-align:left; width:360px;">
					<s:textfield name="strDataCompetenza" id="strDataCompetenza" cssStyle="width:80px;" disabled="false"/>
				</div>
			</div>
			<div>
				<div style="float:left; text-align:left; width:240px;">
					<s:text name="igeriv.tipo.prodotti" />
				</div>
				<div style="float:left; text-align:left; width:360px;">
					<s:select 
						name="tipoProdottiInEstrattoConto" 
						id="tipoProdottiInEstrattoConto" 
						list="listTipiProdottoEstrattoConto"
						listKey="keyInt"
						listValue="value"
						emptyOption="false"
				        cssStyle="width:280px">
					</s:select>&nbsp;
				</div>
			</div>
			<div>
				<div style="float:left; text-align:left; width:240px;">
					<s:text name="igeriv.periodicita" />&nbsp;<s:text name="igeriv.data.estratto.conto" />
				</div>
				<div style="float:left; text-align:left; width:360px;">
					<s:select name="tipiEstrattoConto" 
						id="tipiEstrattoConto"
						list="listTipiEstrattoConto"
						listKey="key"
						listValue="value"
						emptyOption="false"
				        cssStyle="width:120px">
					</s:select>
				</div>
			</div>
			<div style="width:100%; text-align:center; float:left">		
				<div><s:submit key="dpe.contact.form.submit" name="ricerca" id="ricerca" cssClass="tableFields" cssStyle="width:80px; text-align:center; background: #ffffcc"/></div>
			</div>
		</fieldset>
	</div>
</s:form>
