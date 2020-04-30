<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<s:set name="templateSuffix" value="'ftl'" />
<s:form id="EditLibriScolasticiClienteOrdineForm" action="libriScolasticiClienti_saveCliente.action" namespace="/" method="POST" theme="igeriv" validate="false" onsubmit="return (ray.ajax())">			
	<div id="mainDiv" style="width:100%">	
		<fieldset class="filterBolla" style="text-align:left; width:100%"><legend style="font-size:100%">${tableTitle}</legend>
		<div class="tableFields">
		<table id="clientiTable" width="810px" cellspacing="3">
			<tr>
				<td><s:text name="dpe.nome" />&nbsp;<font color="#000"><em>*</em></font></td>
				<td><s:textfield label="" name="clienteScolastica.nome" id="clienteScolastica.nome" cssStyle="width: 200px" cssClass="tableFields" maxlength="32"/></td>
				<td><font color="red" size="1"><span id="err_clienteScolastica.nome"><s:fielderror><s:param>clienteScolastica.nome</s:param></s:fielderror></span></font></td>
				<td><s:text name="dpe.cognome" /></td>
				<td><s:textfield label="" name="clienteScolastica.cognome" id="clienteScolastica.cognome" cssStyle="width: 200px" cssClass="tableFields" maxlength="32"/></td>
				<td><font color="red" size="1"><span id="err_clienteScolastica.cognome"><s:fielderror><s:param>clienteScolastica.cognome</s:param></s:fielderror></span></font></td>
			</tr>
			<tr>
				<td><s:text name="dpe.indirizzo" /></td>
				<td>
					<s:select label=""
					    name="clienteScolastica.tipoLocalita.tipoLocalita"
					    id="tipoLocalita" 
					    listKey="tipoLocalita" 
					    listValue="descrizione"
					    list="tipiLocalita"
					    emptyOption="false" 
					    cssStyle="width: 60px"
					    cssClass="tableFields"
					    /><s:textfield label="" name="clienteScolastica.indirizzo" id="clienteScolastica.indirizzo" cssStyle="width: 135px" cssClass="tableFields" maxlength="100"/>
				</td>
				<td>
					<font color="red" size="1"><span id="err_dpe.indirizzo">&nbsp;</span></font>
				</td>
				<td>
					<s:text name="igeriv.numero.civico" />
				</td>
				<td>
					<s:textfield label="" name="clienteScolastica.numeroCivico" id="clienteScolastica.numeroCivico" cssStyle="width: 60px" cssClass="tableFields" maxlength="4"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="igeriv.estensione" />&nbsp;&nbsp;&nbsp;<s:textfield label="" name="clienteScolastica.estensione" id="clienteScolastica.estensione" cssStyle="width: 60px" cssClass="tableFields" maxlength="50"/>
				</td>
				<td><font color="red" size="1"><span id="err_dpe.numeroCivico"><s:fielderror><s:param>clienteScolastica.numeroCivico</s:param></s:fielderror></span></font></td>
			</tr>
			<tr>
				<td><s:text name="dpe.localita" /></em></td>
				<td>
					<s:textfield id="autocomplete" name="clienteScolastica.localita.descrizione" cssStyle="width: 200px" cssClass="tableFields"/>
				</td>
				<td style="float:left; width:100px;"><font color="red" size="1"><span id="err_clienteScolastica.autocomplete"><s:fielderror><s:param>autocomplete</s:param></s:fielderror></span></font></td>			
				<td><s:text name="dpe.provincia" /></td>
				<td style="float:left; width:100px;">
					<s:select 
						label=""
					        name="clienteScolastica.provincia.codProvincia"
					        id="clienteScolastica.provincia" 
					        listKey="codProvincia" 
					        listValue="descrizione"
					        list="province"
					        emptyOption="true"
					        cssStyle="width: 200px"	
					        cssClass="tableFields"					       				       
					        />
				</td>
				<td style="float:left; width:100px;"><font color="red" size="1"><span id="err_clienteScolastica.provincia.codProvincia"><s:fielderror><s:param>clienteScolastica.provincia.codProvincia</s:param></s:fielderror></span></font></td>
			</tr>
			<tr>			
				<td><s:text name="dpe.nazione" /></td>
				<td>
					<s:textfield label="" name="descPaese" id="descPaese" cssStyle="width: 200px" cssClass="tableFields" value="ITALIA" disabled="true"/>					
				</td>
				<td style="float:left; width:100px;"><font color="red" size="1"><span id="err_clienteScolastica.paese"><s:fielderror><s:param>clienteScolastica.paese</s:param></s:fielderror></span></font></td>			
				<td><s:text name="dpe.cap" /></td>
				<td><s:textfield label="" name="clienteScolastica.cap" id="clienteScolastica.cap" cssStyle="width: 200px" cssClass="tableFields" maxlength="6"/></td>
				<td><font color="red" size="1"><span id="err_clienteScolastica.cap"><s:fielderror><s:param>clienteScolastica.cap</s:param></s:fielderror></span></font></td>
			</tr>
		
		<tr>
			<td><s:text name="dpe.codice.fiscale" /></td>
			<td><s:textfield label="" name="clienteScolastica.codiceFiscale" id="clienteScolastica.codiceFiscale" cssStyle="width: 200px" cssClass="tableFields" maxlength="25"/></td>
			<td><font color="red" size="1"><span id="err_codiceFiscale"><s:fielderror><s:param>codiceFiscale</s:param></s:fielderror></span></font></td>		
			<td><s:text name="dpe.piva" /></td>
			<td><s:textfield label="" name="clienteScolastica.piva" id="clienteScolastica.piva" cssStyle="width: 200px" cssClass="tableFields" maxlength="20"/>
			<td><font color="red" size="1"><span id="err_clienteScolastica.piva"><s:fielderror><s:param>clienteScolastica.piva</s:param></s:fielderror></span></font>
		</tr>			
		<tr>
			<td><s:text name="dpe.telefono" /></td>
			<td><s:textfield label="" name="clienteScolastica.telefono" id="clienteScolastica.telefono" cssStyle="width: 200px" cssClass="tableFields" maxlength="24"/></td>
			<td><font color="red" size="1"><span id="err_clienteScolastica.telefono"><s:fielderror><s:param>clienteScolastica.telefono</s:param></s:fielderror></span></font></td>		
			<td><s:text name="dpe.fax" /></td>
			<td><s:textfield label="" name="clienteScolastica.fax" id="clienteScolastica.fax" cssStyle="width: 200px" cssClass="tableFields" maxlength="24"/></td>
			<td><font color="red" size="1"><span id="err_clienteScolastica.fax"><s:fielderror><s:param>clienteScolastica.fax</s:param></s:fielderror></span></font></td>
		</tr>
		<tr>
			<td><s:text name="dpe.email" /></td>
			<td><s:textfield label="" name="clienteScolastica.email" id="clienteScolastica.email" cssStyle="width: 200px" cssClass="tableFields" maxlength="100"/></td>
			<td><font color="red" size="1"><span id="err_clienteScolastica.email"><s:fielderror><s:param>clienteScolastica.email</s:param></s:fielderror></span></font></td>		
			<td><s:text name="dpe.contact.form.cell" /></td>
			<td><s:textfield label="" name="clienteScolastica.cellulare" id="clienteScolastica.cellulare" cssStyle="width: 200px" cssClass="tableFields" maxlength="50"/></td>
			<td><font color="red" size="1"><span id="err_clienteScolastica.cellulare"><s:fielderror><s:param>clienteScolastica.cellulare</s:param></s:fielderror></span></font></td>
		</tr>
		<tr>
			<td><s:text name="igeriv.tipo.estratto.conto" /></td>
			<td>
				<s:select label=""
				    name="clienteScolastica.tipoEstrattoConto"
				    id="tipoEstrattoConto" 
				    listKey="keyInt" 
				    listValue="value"
				    list="%{#application['listTipiEstrattoConto']}"
				    emptyOption="false" 
				    cssStyle="width: 200px"
				    />
			</td>	
			<td><font color="red" size="1"><span id="err_clienteScolastica.tipoEstrattoConto"><s:fielderror><s:param>clienteScolastica.tipoEstrattoConto</s:param></s:fielderror></span></font></td>		
			<td><s:text name="igeriv.banca.appoggio.html" /></td>
			<td>
				<s:select label=""
				    name="clienteScolastica.banca.codBanca"
				    id="banca" 
				    listKey="codBanca" 
				    listValue="nome"
				    list="%{#application['listBanche']}"
				    emptyOption="true" 
				    cssStyle="width: 200px"
				    />
			</td>	
			<td><font color="red" size="1"><span id="err_clienteScolastica.banca.codBanca"><s:fielderror><s:param>clienteScolastica.banca.codBanca</s:param></s:fielderror></span></font></td>
		</tr>	
		<tr>
			<td><s:text name="igeriv.cc" /></td>
			<td><s:textfield label="" name="clienteScolastica.contoCorrente" id="clienteScolastica.contoCorrente" cssStyle="width: 200px" cssClass="tableFields" maxlength="100"/></td>
			<td><font color="red" size="1"><span id="err_clienteScolastica.contoCorrente"><s:fielderror><s:param>clienteScolastica.contoCorrente</s:param></s:fielderror></span></font></td>
			<td><s:text name="igeriv.iban" /></td>
			<td><s:textfield label="" name="clienteScolastica.iban" id="clienteScolastica.iban" cssStyle="width: 200px" cssClass="tableFields" maxlength="100"/></td>
			<td><font color="red" size="1"><span id="err_clienteScolastica.iban"><s:fielderror><s:param>clienteScolastica.iban</s:param></s:fielderror></span></font></td>
		</tr>
		<tr>
			<td><s:text name="igeriv.tipo.pagamento" /></td>
			<td>
				<s:select label=""
				    name="clienteScolastica.tipoPagamento.codMetodoPagamento"
				    id="tipoPagamento" 
				    listKey="codMetodoPagamento" 
				    listValue="descrizione"
				    list="%{#application['listMetodiPagamentoCliente']}"
				    emptyOption="false" 
				    cssStyle="width: 200px"
				    />
			</td>	
			<td><font color="red" size="1"><span id="err_clienteScolastica.tipoEstrattoConto"><s:fielderror><s:param>clienteScolastica.tipoEstrattoConto</s:param></s:fielderror></span></font></td>
			<td><s:text name="igeriv.giorni.scadenza" /></td>
			<td><s:select label=""
				    name="clienteScolastica.giorniScadenzaPagamento"
				    id="giorniScadenzaPagamento" 
				    listKey="keyInt"
				    listValue="value"
				    list="%{#application['listNumberVo0to100']}"
				    emptyOption="false" 
				    cssStyle="width: 200px"
				    /></td>
			<td><font color="red" size="1"><span id="err_clienteScolastica.giorniScadenzaPagamento"><s:fielderror><s:param>clienteScolastica.giorniScadenzaPagamento</s:param></s:fielderror></span></font></td>
		</tr>
		<s:if test="#request.hasGestioneWebClienti eq true">
			<tr>
				<td><s:text name="dpe.utente" /></td>
				<td><s:textfield label="" name="clienteScolastica.codCliente" id="clienteScolastica.codCliente" cssStyle="width: 200px" cssClass="tableFields" disabled="true" /></td>
				<td><font color="red" size="1"><span id="err_clienteScolastica.codCliente"><s:fielderror><s:param>clienteScolastica.codCliente</s:param></s:fielderror></span></font></td>		
				<td><s:text name="password" /></td>
				<td><s:textfield label="" name="clienteScolastica.password" id="clienteScolastica.password" cssStyle="width: 200px" cssClass="tableFields" maxlength="100"/></td>
				<td><font color="red" size="1"><span id="err_clienteScolastica.password"><s:fielderror><s:param>clienteScolastica.password</s:param></s:fielderror></span></font></td>
			</tr>
			<tr>						
				<td><s:text name="dpe.invia.email.conferma.credenziali" /></td>
				<td><s:checkbox name="inviaEmail" id="inviaEmail" cssClass="tableFields" value="true"/></td>
				<td><font color="red" size="1"><span id="err_inviaEmail"><s:fielderror><s:param>inviaEmail</s:param></s:fielderror></span></font></td>		
				<td><s:text name="igeriv.sospensione.prenotazioni" /></td>
				<td>
				<s:text name="igeriv.da" />&nbsp;<s:textfield label="" name="clienteScolastica.dtSospensionePrenotazioniDa" id="dataSospensioneDa" cssStyle="width:75px; font-size:85%" cssClass="tableFields" maxlength="10">
					<s:param name="value">
						<s:date name="clienteScolastica.dtSospensionePrenotazioniDa" format="dd/MM/yyyy"/>
					</s:param>
				</s:textfield>&nbsp;<s:text name="igeriv.a" />&nbsp;<s:textfield label="" name="clienteScolastica.dtSospensionePrenotazioniA" id="dataSospensioneA" cssStyle="width:75px; font-size:85%" cssClass="tableFields" maxlength="10">
					<s:param name="value">
						<s:date name="clienteScolastica.dtSospensionePrenotazioniA" format="dd/MM/yyyy"/>
					</s:param>
				</s:textfield>
				</td>
				<td><font color="red" size="1"><span id="err_dataSospensioneDa"><s:fielderror><s:param>dataSospensioneDa</s:param></s:fielderror></span></font></td>
			</tr>
		</s:if>
		<s:else>
			<tr>
				<td><s:text name="dpe.utente" /></td>
				<td><s:textfield label="" name="clienteScolastica.codCliente" id="clienteScolastica.codCliente" cssStyle="width: 200px" cssClass="tableFields" disabled="true" /></td>
				<td><font color="red" size="1"><span id="err_clienteScolastica.codCliente"><s:fielderror><s:param>clienteScolastica.codCliente</s:param></s:fielderror></span></font></td>						
				<td><s:text name="igeriv.sospensione.prenotazioni" /></td>
				<td>
				<s:text name="igeriv.da" />&nbsp;<s:textfield label="" name="clienteScolastica.dtSospensionePrenotazioniDa" id="dataSospensioneDa" cssStyle="width:75px; font-size:85%" cssClass="tableFields" maxlength="10">
					<s:param name="value">
						<s:date name="clienteScolastica.dtSospensionePrenotazioniDa" format="dd/MM/yyyy"/>
					</s:param>
				</s:textfield>&nbsp;<s:text name="igeriv.a" />&nbsp;<s:textfield label="" name="clienteScolastica.dtSospensionePrenotazioniA" id="dataSospensioneA" cssStyle="width:75px; font-size:85%" cssClass="tableFields" maxlength="10">
					<s:param name="value">
						<s:date name="clienteScolastica.dtSospensionePrenotazioniA" format="dd/MM/yyyy"/>
					</s:param>
				</s:textfield>
				</td>
				<td><font color="red" size="1"><span id="err_dataSospensioneDa"><s:fielderror><s:param>dataSospensioneDa</s:param></s:fielderror></span></font></td>
			</tr>
			<tr>						
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</s:else>
		<tr>
			<td colspan="1"><s:text name="igeriv.aeca.note" /></td>
			<td colspan="3"><s:textarea name="clienteScolastica.note" id="note" cols="25" rows="2" cssStyle="width:250px" maxlength="3999"/></td>
			<td colspan="2"><font color="red" size="1"><span id="err_clienteScolastica.contoCorrente"><s:fielderror><s:param>clienteScolastica.note</s:param></s:fielderror></span></font></td>
		</tr>
		<!-- 
		<tr>						
			<td><s:text name="plg.vendita.igeriv.card" /></td>
			<td><img id="igerivCardImg" src="/app_img/igeriv_icon.gif" width="25px" height="25px" style="cursor:pointer; vertical-align:top;" alt="<s:text name="plg.vendita.igeriv.card"/>" border="0" title="<s:text name="plg.vendita.igeriv.card.associare" />" onclick="javascript: doAssociaIGerivCard();"/></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		 -->
		</table>
		</div>
		</fieldset>	
		
		<s:if test="actionName != null && !actionName.equals('libriScolasticiClienti_showClienteReadOnly.action')">	
		<div style="width:100%; margin-top:0px;">
			<div style="float:left; width:400px; margin-left:320px">
				<div style="float:left; width:100px; margin-top:0px;"><input type="button" name="igeriv.memorizza" id="memorizza" value="<s:text name='igeriv.memorizza'/>" align="center" class="tableFields" style="text-align:center; width:100px" onclick="javascript: saveCliente(function() {clienteEdicolaSuccessCallback();})"/></div>						
				<div style="float:left; width:100px; margin-left:20px; margin-top:0px;"><input type="button" name="cancella" id="cancella" value="<s:text name='dpe.contact.form.elimina.cliente'/>" align="center" class="tableFields" style="text-align:center; width:150px" onclick="javascript:doDelete();"/></div>				
			</div>			
		</div>
		</s:if>
	</div>		
	<s:hidden name="idCliente" id="idCliente" value="clienteScolastica.codCliente"/>
	<s:hidden id="codCliente" name="codCliente" value="clienteScolastica.codCliente"/>
	<s:hidden name="clienteScolastica.codEdicola"/>
	<s:hidden name="clienteScolastica.gruppoModuliVo.id"/>
	<s:hidden id="codLocalita" name="clienteScolastica.localita.codLocalita"/>	
	<s:hidden id="codPaese" name="clienteScolastica.paese.codPaese" value="1"/>
	<s:hidden id="clientDeleted" name="clientDeleted"/>
</s:form>
<div id="one"></td>  