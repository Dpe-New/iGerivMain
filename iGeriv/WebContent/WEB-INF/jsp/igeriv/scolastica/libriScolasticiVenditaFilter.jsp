<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %> 
<style>
	div#filter {
		height: 140px;
	}
</style>
<s:form id="LibriScolasticiVenditaForm" action="libriScolasticiVendita_showVendite.action" method="POST" theme="simple" validate="true" onsubmit="return (ray.ajax())">	
	<div style="text-align:center">
		<fieldset class="filterBolla" ><legend style="font-size: 100%">
			<s:text name="igeriv.scolastica.consegna.libri.cliente" /></legend>
			<table border="0"> 
			<tr>	
			<td style="float:center;">
				<div class="required" style="float:left">
					<div style="float:left; text-align:right; width:150px">
							<s:text name="dpe.codice.numero.ordine"/>	
					</div>
				</div>
				<div style="float:left; width:300px">
					<s:textfield  name="ordineFornitore"/>				
				</div>
				<div style="align:center; text-align:center; margin-top:20px;">
					<div style="float:left; width:600px; align:center; text-align:center;">					
						<input type="button" name="ricerca" id="ricerca" value="<s:text name="Ricerca Ordine"/>" align="center" style="align:center; width:150px" onclick="doSubmit();"/>
					</div>
				<div>				
			</td>
			<!-- 
			<td style="float:center;">
				<table border="0">
				<tr>
					<td class="required">
						<div style="float: left; width: 200px;">
							<s:text name="dpe.nome" />
						</div>
						<div style="float: left; width: 200px;">
							<s:textfield label="" name="nome" id="nome" />
						</div>
					</td>
					<td class="required">
						<div style="float: left; width: 200px;">
							<s:text name="dpe.cognome" />
						</div>
						<div style="float: left; width: 200px;">
							<s:textfield label="" name="cognome" id="cognome" />
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="align:center; text-align:center; margin-top:20px;">
						<div style="float:left; width:300px; align:center; text-align:center;">					                                                                                         
							<input type="button" name="buttonRicercaCliente" id="buttonRicercaCliente" value="<s:text name="Ricerca Cliente"/>" align="center" style="align:center; width:150px" onclick="doRicercaCliente();"/>
						</div>
					</td>
				</tr>
				</table>				
			</td>
			 -->
			</tr>
			</table>		
		</fieldset>
	</div>
</s:form>
