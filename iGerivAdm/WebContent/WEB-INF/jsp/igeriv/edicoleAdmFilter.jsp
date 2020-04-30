<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
	div#content1 { height:500px; }
</style>
<s:form id="filterForm" action="edicoleAdm_showEdicole.action" method="POST" theme="simple" validate="false" onsubmit="return (doSubmit() && ray.ajax())">	
	<div style="width:100%; text-align:center">
		<fieldset class="filterBolla" style="width:700px; height:140px;"><legend style="font-size:100%"><s:property value="tableTitle" /></legend>
			<div class="required" style="height:30px; margin-top:10px">
				<div style="float:left; width:220px;"><s:text name="dpe.nome.dl" /></div>		
				<div style="float:left; width:400px;">
					<s:select label=""
					    name="codDl"
					    id="codDl" 
					    listKey="key" 
					    listValue="value"
					    list="%{#session['tutti_dl']}"
					    emptyOption="true" 
					    cssStyle="width:400px"
					    cssClass="tableFields"
					    />	
				</div>
			</div>
			<div class="required" style="height:30px;">
				<div style="float:left; width:220px;"><s:text name="igeriv.cod.rivendita.o.rag.soc" /><br><em>*</em>&nbsp;<span style="font-size:70%; font-style:italic"><s:text name="dpe.primi.tre.caratteri" /></span></div>
				<div style="float:left; width:400px;">
					<s:textfield id="edicolaLabel" name="edicolaLabel" cssStyle="width:400px" cssClass="tableFields"/>
				</div>				
			</div>
			<div class="required" style="width:100%; text-align:center; margin-top:10px">
				<div style="width:200px; margin-left:auto; margin-right:auto">		
					<s:submit name="ricerca" id="ricerca" key="dpe.contact.form.ricerca" align="center" cssStyle="align:center; width:80px" />
				</div>
			</div>
		</fieldset>	
	</div>
	<s:hidden name="codEdicolaWebStr" id="codEdicolaWebStr"/>
</s:form>
