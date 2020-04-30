<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags" %> 

<%@ page trimDirectiveWhitespaces="true" %>

<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<style>
	div#filter { height:280px; }
	div#content1 { min-height: 400px; }
</style>
<div class="container">

	<s:form id="filterForm" action="profilazione_showEdicole.action" method="POST" theme="simple" validate="false" onsubmit="return (doSubmit() && ray.ajax())">	
		<fieldset class="form-group">
    	<legend><s:property value="tableTitle" /></legend>
		
		<div class="form-group">
				<label for="sel1"><s:text name="dpe.nome.dl" /></label>
		      	<s:select label=""
						    name="codDl"
						    id="codDl" 
						    listKey="key" 
						    listValue="value"
						    list="%{#session['tutti_dl']}"
						    emptyOption="true" 
						    cssStyle="width:400px"
						    cssClass="form-control"
						    
						    />	
				<br/>
				<label for="sel2"><s:text name="dpe.profili.dl" /></label>
		      	<s:select label=""
						    name="profiliDL"
						    id="profiliDL" 
						    listKey="key" 
						    listValue="value"
						    list="{}"
						    emptyOption="true" 
						    cssStyle="width:400px"
						    cssClass="form-control"
						    disabled="true"
						    />	
		      	<br/>
				<label for="sel3"><s:text name="igeriv.cod.rivendita.o.rag.soc" /><br><em>*</em>&nbsp;
								  <span style="font-size:70%; font-style:italic"><s:text name="dpe.primi.tre.caratteri" /></span>
				</label>
		      	<input type="text" id="edicolaLabel" name="edicolaLabel" style="width:400px" class="form-control"/>
		      	<br/>
		      	<input type="submit" name="ricerca" id="ricerca" class="btn btn-info" value="RICERCA" >	      	
	    </div>
		<s:hidden name="codEdicolaWebStr" id="codEdicolaWebStr"/>
		
		</fieldset>
	
	</s:form>
	
	
	
</div>
