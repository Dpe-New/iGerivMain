<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
	div#content1 { height:300px; }
</style>
<s:form id="filterForm" action="edicoleInforivDl_showEdicoleInforivDl.action" method="POST" theme="simple" validate="false" onsubmit="return (doSubmit() && ray.ajax())">	
	<div style="width:100%; text-align:center">
		<fieldset class="filterBolla" style="width:700px; height:140px;"><legend style="font-size:100%"><s:property value="tableTitle" /></legend>
			<div class="required" style="height:30px;">
				<div style="float:left; width:220px;"><s:text name="igeriv.cod.rivendita.o.rag.soc" /><br><em>*</em>&nbsp;<span style="font-size:70%; font-style:italic"><s:text name="dpe.primi.tre.caratteri" /></span></div>
				<div style="float:left; width:400px;">
					<s:textfield id="edicolaLabel" name="edicolaLabel" cssStyle="width:400px" cssClass="tableFields"/>
				</div>				
			</div>
			<div class="required" style="width:100%; text-align:center; margin-top:30px">
				<div style="width:320px; margin-left:auto; margin-right:auto; text-align:center;">	
					<div style="float:left; width:100px;">		
						<input type="button" name="ricerca" id="ricerca" value="<s:text name="dpe.contact.form.ricerca"/>" align="center" style="align:center; width:80px" onclick="$('#filterForm').submit();"/>
					</div>
					<div style="float:left; width:200px;">		
						<input type="button" name="nuovo" id="nuovo" value="<s:text name="plg.inserisci.nuovo"/>" align="center" style="align:center; width:150px" onclick="insertNew()"/>
					</div>
				</div>
			</div>
		</fieldset>	
	</div>
	<s:hidden name="codEdicolaWebStr" id="codEdicolaWebStr"/>
</s:form>
