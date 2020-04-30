<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %> 
<style>
	div#filter { 
   		height:120px;		  
  	} 
</style>
<s:form id="AgenzieForm" action="agenzia_showAgenzie.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">	
	<!-- <div style="text-align:center">
			<div style="align:center; text-align:center; margin-top:20px;">
				<div style="float:left; width:610px; align:center; text-align:center;">					
					<input type="button" name="ricerca" id="ricerca" value="<s:text name="dpe.contact.form.ricerca"/>" align="center" style="align:center; width:150px" onclick="doSubmit();"/>
				</div>				
			</div>	
	</div> -->	
</s:form>