<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
div#filter {
	height: 0px;
}

div#content1 {
	height: 1100px;
}
</style>
<s:form id="EditAnagraficaRivenditaForm"
	action="anagraficaRivendita_save.action" enctype="multipart/form-data"
	namespace="/" method="POST"
	onsubmit="return (validateForm() && ray.ajax())">
	<div id="mainDiv" style="width: 100%">
		<fieldset class="filterBolla" style="text-align: left; width: 100%">
			<div class="tableFields">
				<table id="anagraficaTable" width="810px" cellspacing="3">
					<tr>
						<td style="width: 50%;"><font color="#000"><b><s:text
										name="igeriv.aeca.tipo" /><em>*</em></b></font></td>
						<td style="width: 50%;"><font color="#000"><b><s:text
										name="igeriv.aeca.tipologia" /><em>*</em></b></font></td>
					</tr>
					<tr>
						<td style="width: 50%;"><input type="radio"
							name="anagraficaEdicolaAggiuntiviVo.tipo" value="1"
							<s:if test="anagraficaEdicolaAggiuntiviVo.tipo eq 1">checked</s:if>>&nbsp;&nbsp;<font
							color="#000"><s:text name="igeriv.aeca.chiosco" /></font></td>
						<td style="width: 50%;"><input type="radio"
							name="anagraficaEdicolaAggiuntiviVo.sottotipo" value="1"
							id="giornali"
							<s:if test="anagraficaEdicolaAggiuntiviVo.sottotipo eq 1">checked</s:if>>&nbsp;&nbsp;<font
							color="#000"><s:text name="igeriv.aeca.giornali" /></font></td>
					</tr>
					<tr>
						<td style="width: 50%;"><input type="radio"
							name="anagraficaEdicolaAggiuntiviVo.tipo" value="2"
							<s:if test="anagraficaEdicolaAggiuntiviVo.tipo eq 2">checked</s:if>>&nbsp;&nbsp;<font
							color="#000"><s:text name="igeriv.aeca.negozio" /></font></td>
						<td style="width: 50%;"><input type="radio"
							name="anagraficaEdicolaAggiuntiviVo.sottotipo" value="2"
							id="promiscuo"
							<s:if test="anagraficaEdicolaAggiuntiviVo.sottotipo eq 2">checked</s:if>>&nbsp;&nbsp;<font
							color="#000"><s:text name="igeriv.aeca.promiscuo" /></font></td>
					</tr>
					<tr height="20px">
						<td colspan="2" />
					</tr>
					<tr>
						<td style="width: 50%;"><font color="#000"><b><s:text
										name="igeriv.aeca.posizionamento" /></b></font></td>
						<td id="tdMqTitle" cssStyle="width: 50%;"><font color="#000"><b><s:text
										name="igeriv.aeca.mq.superficie" /></b></font></td>
					</tr>
					<tr>
						<td style="width: 50%;"><s:select label=""
								name="anagraficaEdicolaAggiuntiviVo.posizionamento"
								id="posizionamento" listKey="key" listValue="value"
								list="posizionamentoMap" emptyOption="true"
								cssStyle="width: 250px" /></td>
						<td id="tdMq" style="width: 50%;"></td>
					</tr>
					<tr height="20px">
						<td colspan="2" />
					</tr>
					<tr>
						<td colspan="2"><font color="#000"><b><s:text
										name="igeriv.aeca.variabili.merchandising" /></b></font></td>
					</tr>
					<tr>
						<td colspan="2">
							<table width="100%">
								<tr>
									<td><input type="checkBox"
										name="anagraficaEdicolaAggiuntiviVo.vmPresenzaRicevitoria"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.vmPresenzaRicevitoria">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.presenza.ricevitoria" /></font></td>
									<td><input type="checkBox"
										name="anagraficaEdicolaAggiuntiviVo.vmPresenzaStampaEstera"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.vmPresenzaStampaEstera">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.presenza.stampa.estera" /></font></td>
									<td><input type="checkBox"
										name="anagraficaEdicolaAggiuntiviVo.vmRivenditaInformatizzata"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.vmRivenditaInformatizzata">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.rivendita.informatizzata" /></font></td>
								</tr>
								<tr>
									<td><input type="checkBox"
										name="anagraficaEdicolaAggiuntiviVo.vmRegistratoreCassa"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.vmRegistratoreCassa">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.registratore.cassa" /></font></td>
									<td />
									<td />
								</tr>
							</table>
						</td>
					</tr>
					<tr height="20px">
						<td colspan="2" />
					</tr>
					<tr>
						<td colspan="2"><font color="#000"><b><s:text
										name="igeriv.aeca.stagionalita" /><em>*</em></b></font></td>
					</tr>
					<tr>
						<td style="width: 50%;"><s:select label=""
								name="anagraficaEdicolaAggiuntiviVo.stagionalita"
								id="stagionalita" listKey="key" listValue="value"
								list="stagionalitaMap" emptyOption="false"
								cssStyle="width: 250px" /></td>
						<td style="width: 50%;" />
					</tr>
					<tr height="20px">
						<td colspan="2" />
					</tr>
					<tr>
						<td colspan="2"><font color="#000"><b><s:text
										name="igeriv.aeca.merceologia.prevalente" /><em>*</em></b></font></td>
					</tr>
					<tr>
						<td colspan="2">
							<table width="100%">
								<tr>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpAlimentari"
										id="mpAlimentari" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpAlimentari">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.alimentari" /></font></td>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpAbbigliamento"
										id="mpAbbigliamento" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpAbbigliamento">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.abbigliamento" /></font></td>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpArredamento"
										id="mpArredamento" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpArredamento">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.arredamento" /></font></td>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpArticoliSportivi"
										id="mpArticoliSportivi" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpArticoliSportivi">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.articoli.sportivi" /></font></td>
								</tr>
								<tr>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpCarburanti"
										id="mpCarburanti" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpCarburanti">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.carburanti" /></font></td>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpCartolibreriaGiocattoli"
										id="mpCartolibreriaGiocattoli" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpCartolibreriaGiocattoli">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.cartolibreria.giocattoli" /></font></td>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpElettronicaConsumo"
										id="mpElettronicaConsumo" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpElettronicaConsumo">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.elettronica.consumo" /></font></td>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpHomeEntertainment"
										id="mpHomeEntertainment" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpHomeEntertainment">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.home.entertainment" /></font></td>
								</tr>
								<tr>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpTabacchi"
										id="mpTabacchi" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpTabacchi">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.tabacchi" /></font></td>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpMerceria"
										id="mpMerceria" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpMerceria">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.merceria" /></font></td>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpNessuna" id="mpNessuna"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpNessuna">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.nessuna" /></font></td>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpOrtofrutta"
										id="mpOrtofrutta" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpOrtofrutta">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.ortofrutta" /></font></td>
								</tr>
								<tr>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpFerramenta"
										id="mpFerramenta" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpFerramenta">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.ferramenta" /></font></td>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpPanificio"
										id="mpPanificio" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpPanificio">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.panificio" /></font></td>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpProfumeria"
										id="mpProfumeria" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpProfumeria">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.profumeria" /></font></td>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpFiorista"
										id="mpFiorista" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpFiorista">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.fiorista" /></font></td>
								</tr>
								<tr>
									<td><input type="checkBox" class="merc"
										name="anagraficaEdicolaAggiuntiviVo.mpProdottiBar"
										id="mpProdottiBar" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.mpProdottiBar">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.prodotti.bar" /></font></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr height="20px">
						<td colspan="2" />
					</tr>
					<tr>
						<td colspan="2"><font color="#000"><b><s:text
										name="igeriv.aeca.poli.attrazione" /><em>*</em></b></font></td>
					</tr>
					<tr>
						<td colspan="2">
							<table width="100%">
								<tr>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paAeroporto" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paAeroporto">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.aeroporto" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paCentroCommerciale"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paCentroCommerciale">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.centro.commerciale" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paStazioneFerroviaria"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paStazioneFerroviaria">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.stazione.ferroviaria" /></font></td>
								</tr>
								<tr>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paPorto" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paPorto">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.porto" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paOspedale" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paOspedale">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.ospedali.centri.cura.terme" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paFermataMezziPubblici"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paFermataMezziPubblici">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.fermata.mezzi.pubblici" /></font></td>
								</tr>
								<tr>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paMercatoRionaleDiurno"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paMercatoRionaleDiurno">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.mercato.rionale.diurno" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paStrutturaFieristica"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paStrutturaFieristica">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.struttura.fieristica" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paCentroDirezionale"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paCentroDirezionale">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.centro.direzionale" /></font></td>
								</tr>
								<tr>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paLuogoCulto" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paLuogoCulto">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.luogo.culto" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paMunicipio" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paMunicipio">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.municipio" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paPoste" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paPoste">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.poste" /></font></td>
								</tr>
								<tr>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paAsl" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paAsl">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.asl" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paQuestura" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paQuestura">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.questura" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paCaserma" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paCaserma">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.caserma" /></font></td>
								</tr>
								<tr>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paUniversita" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paUniversita">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.universita" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paScuolaMediaSuperiore"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paScuolaMediaSuperiore">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.scuola.media.superiore" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paScuolaMediaInferioreElementareAsili"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paScuolaMediaInferioreElementareAsili">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.scuola.media.inferiore.elementare.asili" /></font></td>
								</tr>
								<tr>
									<td colspan="3"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paMuseo" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paMuseo">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.museo.luogo.interesse.pubblico" /></font></td>
									<td colspan="3"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paStadio" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paStadio">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.stadio.luoghi.manifestazioni.sportive" /></font></td>
								</tr>
								<tr>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paCinema" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paCinema">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.cinema" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paTeatro" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paTeatro">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.teatro" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paPalestra" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paPalestra">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.palestra" /></font></td>
								</tr>
								<tr>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paBanca" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paBanca">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.banca" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paNessuna" id="paNessuna"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paNessuna">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.nessuno" /></font></td>
									<td colspan="2"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paAltriPuntiVenditaProdottiEditoriali"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paAltriPuntiVenditaProdottiEditoriali">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.altri.punti.vendita.editoriali" /></font></td>
								</tr>
								<tr>
									<td colspan="3"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paGdoGda" value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paGdoGda">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text name="igeriv.aeca.gdo.gda" /></font></td>
									<td colspan="3"><input type="checkBox" class="pattr"
										name="anagraficaEdicolaAggiuntiviVo.paStruttureRicettiveTuristi"
										value="true"
										<s:if test="anagraficaEdicolaAggiuntiviVo.paStruttureRicettiveTuristi">checked</s:if>>&nbsp;&nbsp;<font
										color="#000"><s:text
												name="igeriv.aeca.strutture.ricettive.turisti" /></font></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr height="20px">
						<td colspan="2" />
					</tr>
					<tr>
						<td colspan="2"><font color="#000"><b><s:text
										name="igeriv.aeca.localizzazione" /><em>*</em></b></font></td>
					</tr>
					<tr>
						<td style="width: 50%;"><s:select label=""
								name="anagraficaEdicolaAggiuntiviVo.localizzazione"
								id="localizzazione" listKey="key" listValue="value"
								list="localizzazioneMap" emptyOption="false"
								cssStyle="width: 300px" /></td>
						<td style="width: 50%;" />
					</tr>
					<tr>
						<td colspan="2"><font color="#000"><b><s:text
										name="igeriv.aeca.note" /><em>*</em></b></font></td>
					</tr>
					<tr>
						<td colspan="2" style="width: 50%;"><s:textarea
								name="anagraficaEdicolaAggiuntiviVo.note" id="note" cols="50"
								rows="5" cssStyle="width:600px" maxlength="3999" /></td>
					</tr>
					<tr>
						<td colspan="2"><font color="#000"><b><s:text
										name="igeriv.foto.edicola.negozio" /></b></font></td>
					</tr>
					<tr>
						<td style="width: 30%;"><s:file name="foto1" label="foto1" /></td>
						<td style="width: 70%;">
							<div style="float: left; width: 100px;">
								<img title="<s:text name="igeriv.foto.edicola.negozio"/>"
									src="/immagini_anagrafica_edicole/<s:text name="anagraficaEdicolaAggiuntiviVo.immagine1.nomeImmagine"/>?<s:property value="radomInt"/>"
									border="1" width="50" height="50" />
							</div>
							<div style="float: left; width: 220px;">
								<font color="red"><span id="err_foto1"><s:fielderror>
											<s:param>foto1</s:param>
										</s:fielderror></span></font>
							</div>
						</td>
					</tr>
					<tr height="20px">
						<td colspan="2" />
					</tr>
					<tr>
						<td colspan="2" style="text-align: center;"><input
							type="checkBox" name="condUsoAccettate" id="condUsoAccettate"
							value="true" <s:if test="condUsoAccettate">checked</s:if>>&nbsp;&nbsp;<font
							color="#000"><b>
							
							<a href="/pdf/PRIVACY_POLICY_<s:property value="codDl"/>.pdf" target="_new"><span style="color: black;">
							<s:text name="igeriv.aeca.accetto.condizioni.uso" /></span></a>
							</b></font>
					</tr>
				</table>
			</div>
		</fieldset>
		<div style="width: 100%; margin-top: 20px; text-align: center">
			<s:submit key="igeriv.memorizza" id="memorizza"
				cssClass="tableFields" cssStyle="width:150px; text-align:center" />
		</div>
	</div>
	<s:hidden name="idCliente" id="idCliente" />
	<s:hidden name="cliente.codEdicola" />
	<s:hidden name="cliente.gruppoModuliVo.id" />
	<s:hidden id="codLocalita" name="cliente.localita.codLocalita" />
	<s:hidden id="codPaese" name="cliente.paese.codPaese" value="1" />
	<s:hidden id="clientDeleted" name="clientDeleted" />
	<s:hidden name="anagraficaEdicolaAggiuntiviVo.mqSuperficie"
		id="mqSuperficie" />
	<s:hidden name="anagraficaEdicolaAggiuntiviVo.mtlineariPerGiornali"
		id="mtlineariPerGiornali" />
</s:form>
