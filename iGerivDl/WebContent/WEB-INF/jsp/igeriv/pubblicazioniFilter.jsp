<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>


<s:if test="actionName != null && (actionName.contains('rifornimenti') || actionName.contains('prenotazioneClienti') || actionName.contains('infoPubblicazioni') || actionName.contains('statisticaPubblicazioni') || actionName.contains('variazioni'))">
	<style>
		div#filter { 
		   height:220px;		  
		  } 
		  
		div#content1 { 
			height:450px;
		  } 
		  
		.required li{ 
			list-style: none; 
		}
	</style> 
</s:if>	
<s:set var="filterTitle" value="%{tableTitle}" />
<s:set name="templateSuffix" value="'ftl'" />
<s:if test="actionName != null && actionName.contains('prenotazioneClienti') && %{#context['struts.actionMapping'].namespace} == '/'">
	<s:set var="filterTitle" value="%{getText('igeriv.inserisci.prenotazioni.cliente')}" />
</s:if>

		<div style="width:750px; margin:0 auto;">
			<div class="required" style="align:left; text-align:center">
				<div style="float:left; width:100px;"><input type="radio" id="idOperazione_ricercaPubblicazione" name="flgOperazione" value="1" <s:if test="flgOperazione eq 1">checked</s:if>></input>&nbsp;&nbsp;</div>
				<div style="float:left; width:250px;"><font color="#000"><s:text name="dl.label.ricerca.pubblicazione" /></font></td></div>
				<div style="float:left; width:100px;"><input type="radio" id="idOperazione_inserisciBarcode" name="flgOperazione" value="0" <s:if test="flgOperazione eq 0">checked</s:if>></input></div>
				<div style="float:left; width:250px;"><font color="#000"><s:text name="dl.label.inserisci.barcode" /></font></div>
			</div>
		</div>
			
<div id="idOperazione_ricercaPubblicazione" automaticallyVisibleIfIdChecked="idOperazione_ricercaPubblicazione">
<s:form id="PubblicazioniForm" action="infoPubblicazioni_showPubblicazioni.action?flgOperazione=1" namespace="%{#context['struts.actionMapping'].namespace}" onsubmit="return (ray.ajax())" method="POST" theme="simple" validate="false">	
	<div id="pubMainDiv" style="width:100%;">
	<s:hidden name="dataTipoBolla" id ="dataTipoBolla"/>
	<s:hidden name="idCliente" id="idCliente"/>
	<div style="width:100%; text-align:left">
		<fieldset class="filterBolla" style="margin-top:0px; top:0px; width:750px; text-align:left"><legend style="font-size:100%"><s:text name="dl.label.ricerca.pubblicazione" /> &nbsp;<b><s:property value="nomeCliente"/></b></legend>
			<div class="required" style="align:left; text-align:center">
				<div style="float:left; width:100px;"><s:text name="igeriv.titolo"/></div>
				<div style="float:left; width:250px;"><s:textfield label="" name="titolo" id="titolo" cssClass="required" cssStyle="width:250px;"/></div>
				<div style="float:left; width:100px;"><s:text name="igeriv.sottotitolo" /></div>
				<div style="float:left; width:250px;"><s:textfield label="" name="sottotitolo" id="sottotitolo" cssClass="required" cssStyle="width:250px;"/></div>
			</div>
			
			<div class="required" style="align:left; text-align:center">
				<div style="float:left; width:100px;"><s:text name="igeriv.prezzo" /></div>
				<div style="float:left; width:250px;"><s:textfield label="" name="prezzo" id="prezzo" validateIsNumeric="true" cssClass="required" cssStyle="width:250px;"/></div>
				<div style="float:left; width:100px;">Cod. Pubb.</div>
				<div style="float:left; width:250px;"><s:textfield label="" name="codPubblicazione" id="codPubblicazione" cssClass="required" cssStyle="width:250px;"/></div>
			</div>
			<div class="required" style="align:left; text-align:center;">
				<div style="float:left; width:100px;">Cod. Barre</div>
				<div style="float:left; width:250px;"><s:textfield label="" name="codBarre" id="codBarre" cssClass="required" cssStyle="width:250px;"/></div>
				<div style="float:left; width:100px;">&nbsp;</div>
				<div style="float:left; width:250px;">&nbsp;</div>
			</div>
			<div class="required" style="align:left; text-align:center;">
				<div style="float:left; width:750px; align:center; text-align:center;">					
					<s:if test="actionName != null && (actionName.contains('rifornimenti') || actionName.contains('prenotazioneClienti') || actionName.contains('infoPubblicazioni') || actionName.contains('statisticaPubblicazioni') || actionName.contains('variazioni'))">
						<s:submit name="submitRicerca" id="submitRicerca" key="dpe.contact.form.ricerca" align="center" cssStyle="align:center; width:150px" />
						<s:if test="actionName.contains('rifornimenti')">
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" name="igeriv.modifica" id="modifica" value="<s:text name='igeriv.modifica.richieste.inserite'/>" align="center" class="required" style="text-align:center; width:200px"/>
						</s:if>
					</s:if>
					<s:else>
						<input type="button" name="dpe.contact.form.submit" id="ricerca" value="<s:text name='dpe.contact.form.ricerca'/>" align="center" class="required" style="text-align:center; width:150px" onclick="submitForm('PubblicazioniForm');"/>
						<input type="reset" name="dpe.contact.form.reset"  id="reset" value="<s:text name='dpe.contact.form.reset'/>" align="center" class="required" style="text-align:center; width:150px" />
					</s:else>
				</div>				
			</div>
			<div class="required" style="align:left; text-align:center;">
				<div style="float:left; width:700px;">
				<div class="required" id="errorDiv" style="width:100%; align:center; text-align:center; height:20px; margin-top:-10px">
				<font color='red'><s:actionerror /></font>
				</div>
				</div>
			</div>
			
			
							
		</fieldset>
	</div>
	</div>
	<s:hidden name="nomeCliente"/>
	<s:hidden name="stato" id="stato"/>
	<s:hidden name="tableTitle" id="tableTitle"/>
	
</s:form>
</div>

<div id="idOperazione_inserisciBarcode" automaticallyVisibleIfIdChecked="idOperazione_inserisciBarcode">

<s:form id="PubblicazioniForm" action="infoPubblicazioni_showPubblicazioniInserimentoBarcode.action?flgOperazione=0" namespace="%{#context['struts.actionMapping'].namespace}" onsubmit="return (ray.ajax())" method="POST" theme="simple" validate="false">	
	<div id="pubMainDiv" style="width:100%;">
	<s:hidden name="dataTipoBolla" id ="dataTipoBolla"/>
	<s:hidden name="idCliente" id="idCliente"/>
	<div style="width:100%; text-align:left">
		<fieldset class="filterBolla" style="margin-top:0px; top:0px; width:750px; text-align:left"><legend style="font-size:100%"><s:text name="dl.label.inserisci.barcode" /> &nbsp;<b><s:property value="nomeCliente"/></b></legend>
			<div class="required" style="align:left; text-align:center">
				<div style="float:left; width:100px;"><s:text name="igeriv.titolo"/></div>
				<div style="float:left; width:250px;"><s:textfield label="" name="titoloInsertBarcode" id="titoloInsertBarcode" cssClass="required" cssStyle="width:250px;"/></div>
				<div style="float:left; width:100px;"><s:text name="igeriv.sottotitolo" /></div>
				<div style="float:left; width:250px;"><s:textfield label="" name="sottotitoloInsertBarcode" id="sottotitoloInsertBarcode" cssClass="required" cssStyle="width:250px;"/></div>
			</div>
			
			<div class="required" style="align:left; text-align:center">
				<div style="float:left; width:100px;"><s:text name="igeriv.prezzo" /></div>
				<div style="float:left; width:250px;"><s:textfield label="" name="prezzoInsertBarcode" id="prezzoInsertBarcode" validateIsNumeric="true" cssClass="required" cssStyle="width:250px;"/></div>
				<div style="float:left; width:100px;">Cod. Pubb.</div>
				<div style="float:left; width:250px;"><s:textfield label="" name="codPubblicazioneInsertBarcode" id="codPubblicazioneInsertBarcode" cssClass="required" cssStyle="width:250px;"/></div>
			</div>
			<div class="required" style="align:left; text-align:center;">
				<div style="float:left; width:100px;">Cod. Barre</div>
				<div style="float:left; width:250px;"><s:textfield label="" name="codBarreInsertBarcode" id="codBarreInsertBarcode" cssClass="required" cssStyle="width:250px;"/></div>
				<div style="float:left; width:100px;">&nbsp;</div>
				<div style="float:left; width:250px;">&nbsp;</div>
			</div>
			<div class="required" style="align:left; text-align:center;">
				<div style="float:left; width:750px; align:center; text-align:center;">					
					<s:if test="actionName != null && (actionName.contains('rifornimenti') || actionName.contains('prenotazioneClienti') || actionName.contains('infoPubblicazioni') || actionName.contains('statisticaPubblicazioni') || actionName.contains('variazioni'))">
						<s:submit name="submitRicerca" id="submitRicerca" key="dpe.contact.form.ricerca" align="center" cssStyle="align:center; width:150px" />
						<s:if test="actionName.contains('rifornimenti')">
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" name="igeriv.modifica" id="modifica" value="<s:text name='igeriv.modifica.richieste.inserite'/>" align="center" class="required" style="text-align:center; width:200px"/>
						</s:if>
					</s:if>
					<s:else>
						<input type="button" name="dpe.contact.form.submit" id="ricerca" value="<s:text name='dpe.contact.form.ricerca'/>" align="center" class="required" style="text-align:center; width:150px" onclick="submitForm('PubblicazioniForm');"/>
						<input type="reset" name="dpe.contact.form.reset"  id="reset" value="<s:text name='dpe.contact.form.reset'/>" align="center" class="required" style="text-align:center; width:150px" />
					</s:else>
				</div>				
			</div>
			<div class="required" style="align:left; text-align:center;">
				<div style="float:left; width:700px;">
				<div class="required" id="errorDiv" style="width:100%; align:center; text-align:center; height:20px; margin-top:-10px">
				<font color='red'><s:actionerror /></font>
				</div>
				</div>
			</div>					
		</fieldset>
	</div>
	</div>
	<s:hidden name="nomeCliente"/>
	<s:hidden name="stato" id="stato"/>
	<s:hidden name="tableTitle" id="tableTitle"/>
	
</s:form>
</div>