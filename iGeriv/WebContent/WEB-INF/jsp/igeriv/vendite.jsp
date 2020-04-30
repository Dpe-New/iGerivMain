<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
div#content1 {
	height: 600px;
}

div#footer {
	height: 0px;
}

.overlay {
	background-color: #000;
	display: none;
	opacity: 0.5;
	filter: alpha(opacity = 50); /* IE7 & 8 */
	z-index: 99;
}

.topPubb {
	text-align: center;
	font-weight: bold;
	font-style: italic;
}

.bottomScrollDiv {
	margin: 0;
	padding: 0;
	height: 168px;
	overflow-x: hidden;
	overflow-y: scroll;
}

.dojoTree {
	font: caption;
	font-size: 100%;
	font-weight: normal;
	overflow-y: scroll;
	overflow-x: hidden;
	height: 450px;
}

.dojoTreeNodeLabelTitle {
	padding-left: 5px;
	color: WindowText;
}

.dojoTreeNodeLabel {
	cursor: hand;
	cursor: pointer;
}

.dojoTreeNodeLabelTitle:hover {
	text-decoration: none;
}

.dojoTreeNodeLabelSelected {
	background-color: Highlight;
	color: HighlightText;
}

.dojoTree div {
	white-space: nowrap;
}

.dojoTree img, .dojoTreeNodeLabel img {
	vertical-align: middle;
}

.mostSoldBarImg {
	cursor: pointer;
}

.pneImg {
	cursor: pointer;
}

.pneImg:hover {
	background: silver;
	cursor: pointer;
}

#sidebarL {
	line-height: 300px;
	text-align: center;
	background-color: #dfe4e8;
	border: 2px solid #669999;
	display: none;
	width: 1%;
}

#sidebar {
	line-height: 300px;
	text-align: center;
	background-color: #dfe4e8;
	border: 2px solid #669999;
	display: none;
	width: 1%;
}

.use-sidebar-L #sidebarL {
	display: block;
	width: 18%;
	height: 380px;
}

.use-sidebar #content {
	width: 61%;
}

.use-sidebar #sidebar {
	display: block;
	width: 18%;
	height: 380px;
}

.sidebar-at-left-L #sidebar {
	margin-right: 0px;
}

.sidebar-at-right-L #sidebar {
	margin-left: 0px;
}

.sidebar-at-left-L #content, .use-sidebar-L.sidebar-at-right #sidebar,
	.sidebar-at-right #separatorL {
	float: left;
}

.sidebar-at-right-L #content, .use-sidebar-L.sidebar-at-left #sidebar,
	.sidebar-at-left #separatorL {
	float: left;
}

.sidebar-at-left #sidebar {
	margin-right: 0px;
}

.sidebar-at-right #sidebar {
	margin-left: 0px;
}

.sidebar-at-left #content, .use-sidebar.sidebar-at-right #sidebar,
	.sidebar-at-right #separatorR {
	float: right;
}

.sidebar-at-right #content, .use-sidebar.sidebar-at-left #sidebar,
	.sidebar-at-left #separatorR {
	float: left;
}

#separatorL {
	background-color: #EEE;
	border: 1px solid #ffffcc;
	display: block;
	outline: none;
	width: 8px;
	height: 380px;
}

#separatorR {
	background-color: #EEE;
	border: 1px solid #ffffcc;
	display: block;
	outline: none;
	width: 8px;
	height: 380px;
}

.use-sidebar-L #separatorL {
	background: url('/app_img/vertical-separator.gif') repeat-y center top;
	border-color: #336699;
}

.use-sidebar #separatorR {
	background: url('/app_img/vertical-separator.gif') repeat-y center top;
	border-color: #336699;
}

#separatorL:hover {
	border-color: #336699;
	background: #ffffcc;
}

#separatorR:hover {
	border-color: #336699;
	background: #ffffcc;
}

.dialogTitle {
	font-size: 16px;
	font-weight: bold;
}
</style>
<s:url id="smdUrl" namespace="/" action="VenditeCardRpc.action" />
<s:url id="vendutoGiornalieroUrl" namespace="/"
	action="VenditeCardRpc.action" />
<div id="mainVenditeDiv"
	style="width: 100%; height: 400px; margin-top: 0px; background-color: #cccccc">
	<div id="topDiv" style="width: 100%; height: 45px; margin-top: 20px;">
		<div style="float: left; width: 18%; display: inline-block;">&nbsp;</div>
		<div id="contoCliente"
			style="float: left; display: inline-block; width: 61%; text-align: center; font-size: 100%; font-weight: bold;">&nbsp;</div>
		<div
			style="float: right; width: 18%; display: inline-block; text-align: center;">
			<img src="/app_img/barra_prod_vari.png" id="imgProdVari"
				style="display: none; cursor: pointer;"
				onclick="showBarraProdottiVari()"
				title='<s:text name="igeriv.mostra.barra.scelta.rapida.prodotti.vari"/>'
				border="0" />
		</div>
	</div>
	<div
		class="use-sidebar sidebar-at-right use-sidebar-L sidebar-at-right-L"
		id="main">
		<div id="sidebarL" style="float: left; border: 2px solid #3366CC;"
			class="bollaScrollDiv">
			<ul id="ulButtonsL" class="ui-helper-reset">
				<s:iterator value="listPubblicazioniBarraSceltaRapidaSinistra">
					<div style="float: left">
						<img id="<s:property value='codicePubblicazione'/>"
							title="<s:property value='titolo'/>&nbsp;<s:text name="igeriv.numero"/>&nbsp;<s:property value='numeroCopertina'/>&nbsp;(&euro;&nbsp;<s:property value='prezzoCopertinaFormat'/>)"
							style="display: inline-block; width: <s:text name="venditeIconWidth"/>px; height: <s:text name="venditeIconHeight"/>px;"
							alt="" class="mostSoldBarImg"
							src="/<s:property value='ImmagineDirAlias'/>/<s:property value='nomeImmagine'/>"
							border="1" width="<s:text name="venditeIconWidth"/>px"
							height="<s:text name="venditeIconHeight"/>px"
							onclick="javascript: vendiProdottoDaBarraSceltaRapida('<s:property value='codicePubblicazione'/>','<s:property value='codFiegDl'/>')">
					</div>
				</s:iterator>
			</ul>
		</div>
		<a href="#" id="separatorL"></a>
		<div id="content">
			<div id="venditeDiv"
				style="height: 180px; width: 100%; text-align: center; border: 1px solid black; background-color: #fff">
				<table dojoType="dojox.grid.DataGrid" id="venditeTable"
					jsid="progressivo" style="font-size: 100%;">
					<thead>
						<tr class="titleRow">
							<th class="tableHeader" field="quantita" width="4%"
								styles='text-align: center;'><s:text
									name="igeriv.quantita.short" /></th>
							<th class="tableHeader" field="titolo" width="24%"><s:text
									name="igeriv.titolo" /></th>
							<th class="tableHeader" field="sottoTitolo" width="15%"><s:text
									name="igeriv.sottotitolo" /></th>
							<th class="tableHeader" field="dataUscitaFormat" width="12%"><s:text
									name="igeriv.data.uscita" /></th>
							<th class="tableHeader" field="numeroCopertina" width="10%"
								styles='text-align: center;'><s:text name="igeriv.numero" /></th>
							<th class="tableHeader" field="prezzoCopertinaFormat" width="10%"
								styles='text-align: right;'><s:text name="igeriv.prezzo" /></th>
							<th class="tableHeader" field="importoFormat" width="10%"
								styles='text-align: right;'><s:text name="igeriv.importo" /></th>
							<th class="tableHeader" field="giacenzaIniziale" width="10%"
								styles='text-align: center;'><s:text
									name="igeriv.giacienza" /></th>
						</tr>
					</thead>
				</table>
			</div>
			<div
				style="width: 100%; text-align: center; height: 200px; text-align: center; margin-left: auto; margin-right: auto; background-color: #cccccc;">
				<div style="float: left; width: 35%;">
					<fieldset style="width: 98%; height: 140px;">
						<legend style="font-size: 80%">
							<s:text name="igeriv.prodotto" />
						</legend>
						<s:form id="VenditeForm" action="#" method="POST" theme="simple"
							validate="false"
							onsubmit="return (verifyInputContent() && ray.ajax())">
							<div style="text-align: left; font-size: 80%">
								<div style="width: 100%; font-size: 120%">
									<s:textfield name="qta" id="qta" maxlength="4"
										cssStyle="text-align:right; width:10%" value="1" />
									&nbsp;X&nbsp;
									<s:textfield name="inputText" id="inputText"
										cssStyle="width:65%;" />
								</div>
								<div style="width: 100%; font-size: 100%;">
									<div
										style="float: left; text-align: left; white-space: nowrap;">
										<s:text name="username.cliente" />
										&nbsp; <select name="idCliente" id="idCliente"
											style="width: 65%;">
											<option value="-1"><s:text name="username.anomimo" /></option>
											<s:if test="authUser.hasLivellamenti eq true">
												<option value="-2"><s:text
														name="igeriv.rete.edicole" /></option>
											</s:if>
											<optgroup label="<s:text name="igeriv.con.estratto.conto"/>">
												<s:iterator value="listClientiConEC">
													<option value="<s:property value="codCliente"/>"><s:property
															value="nomeCognomeEscaped" /></option>
												</s:iterator>
											</optgroup>
											<optgroup
												label="<s:text name="igeriv.senza.estratto.conto"/>">
												<s:iterator value="listClientiSenzaEC">
													<option value="<s:property value="codCliente"/>"><s:property
															value="nomeCognomeEscaped" /></option>
												</s:iterator>
											</optgroup>
										</select>
									</div>
								</div>
								<div style="width: 100%;">
									<div style="float: left; width: 80%; text-align: left">
										<s:text name="dpe.mostra.tutte.uscite" />
									</div>
									<div style="float: left; text-align: left">
										<s:checkbox name="mostraTutteUscite" id="mostraTutteUscite"
											cssStyle="text-align:left;" />
									</div>
								</div>
								<div style="width: 100%;">
									<div style="float: left; width: 80%; text-align: left">
										<s:text name="dpe.ricerca.prodotti.vari" />
									</div>
									<div style="float: left; text-align: left">
										<s:checkbox name="ricercaProdottiVari"
											id="ricercaProdottiVari" cssStyle="text-align:right;" />
									</div>
								</div>
								<s:if
									test="authUser.hasEdicoleAutorizzateAggiornaBarcode eq true">
									<div style="width: 100%;">
										<div style="float: left; width: 80%; text-align: left">
											<s:text name="dpe.aggiorna.barcode" />
										</div>
										<div style="float: left; text-align: left">
											<s:checkbox name="aggiornaBarcode" id="aggiornaBarcode"
												cssStyle="text-align:right;" />
										</div>
									</div>
								</s:if>
								<s:else>
									<div style="width: 100%;">
										<div style="float: left; width: 80%; text-align: left">&nbsp;</div>
										<div style="float: left; text-align: left">&nbsp;</div>
									</div>
								</s:else>
							</div>
							<s:hidden name="closedAccount" id="closedAccount" />
							<s:hidden name="idConto" id="idConto" />
						</s:form>
					</fieldset>
					<fieldset id="aiutoDiv"
						style="height: 59px; width: 98%; font-size: 70%; background-color: #99ccff;">
						<div
							style="width: 100%; height: 50%; vertical-align: middle; text-align: center; margin-top: 3px;">
							<div style="width: 50%; text-align: center; float: left">
								<input type="button" id="butVendutoGiornaliero"
									value="<s:text name="igeriv.venduto"/>&nbsp;(Alt+'V')"
									style="width: 90%; height: 25px; padding: 0px 0px;"
									align="center" onclick="javascript: vendutoGiornaliero();" />
							</div>
							<div style="width: 50%; text-align: center; float: left">
								<input type="button" id="butAzzeraConto"
									value="<s:text name="plg.reimposta"/>&nbsp;(Alt+'A')"
									style="width: 90%; height: 25px; padding: 0px 0px;"
									align="center" onclick="javascript: doAzzeraConto();" />
							</div>
						</div>
						<div
							style="width: 100%; height: 50%; vertical-align: middle; text-align: center;">
							<input type="button" id="butSpazio"
								value="<s:text name="igeriv.chiudi.conto"/>&nbsp;(<s:text name="label.main_frame.help.Close_Bill.key"/>)"
								style="width: 95%; height: 25px; padding: 0px 0px;"
								align="center" onclick="javascript: doCloseAccount();" />
						</div>
					</fieldset>
				</div>
				<div style="float: left; width: 45%; background-color: #cccccc;">
					<fieldset style="width: 98%; height: 200px;">
						<legend style="font-size: 80%">
							<s:text name="column.calc.totals" />
						</legend>
						<div
							style="float: left; width: 100%; margin-top: 3px; font-size: 80%">
							<div style="float: left; width: 100%">
								<div style="float: left; width: 30%; text-align: center">
									<s:text name="column.calc.totale.pezzi" />
								</div>
								<div style="float: left; width: 70%;">
									<s:textfield name="totalePezzi" id="totalePezzi"
										readonly="true"
										cssStyle="text-align:right; height:20px; width:90%; font-size:140%" />
								</div>
							</div>
						</div>
						<div
							style="float: left; width: 100%; margin-top: 3px; font-size: 80%">
							<div style="float: left; width: 100%">
								<div style="float: left; width: 30%; text-align: center">
									<s:if test="registratoreCassa != null">
										<s:text name="column.calc.totals" />
									</s:if>
									<s:else>
										<s:text name="column.calc.total" />
									</s:else>
								</div>
								<div style="float: left; width: 70%;">
									<s:if test="registratoreCassa != null">
										<input type="text" name="totalePubb" id="totalePubb"
											style="text-align: right; height: 20px; width: 25%; font-size: 100%; font-weight: bold; border: 1px solid #3366CC;"
											title="<s:text name="column.calc.total.pubblicazioni"/>" />
										<input type="text" name="totaleScontrino" id="totaleScontrino"
											style="text-align: right; height: 20px; width: 25%; font-size: 100%; font-weight: bold; border: 1px solid #339966;"
											title="<s:text name="column.calc.total.scontrino"/>" />
										<input type="text" name="totale" id="totale"
											style="text-align: right; height: 20px; width: 32%; font-size: 140%; font-weight: bold; border: 1px solid red;"
											title="<s:text name="column.calc.grand.total"/>" />
									</s:if>
									<s:else>
										<s:textfield name="totale" id="totale"
											cssStyle="text-align:right; height:20px; width:90%; font-size:160%; font-weight:bold; " />
									</s:else>
								</div>
							</div>
						</div>
						<div
							style="float: left; width: 100%; margin-top: 3px; font-size: 80%">
							<div style="float: left; width: 100%">
								<div style="float: left; width: 30%; text-align: center">
									<s:text name="dpe.contanti" />
								</div>
								<div style="float: left; width: 70%;">
									<s:textfield name="contanti" id="contanti"
										cssStyle="text-align:right; height:20px; width:90%; font-size:140%; color:green" />
								</div>
							</div>
						</div>
						<div
							style="float: left; width: 100%; margin-top: 3px; font-size: 80%">
							<div style="float: left; width: 100%">
								<div style="float: left; width: 30%; text-align: center">
									<s:text name="dpe.resto" />
								</div>
								<div style="float: left; width: 70%;">
									<s:textfield name="resto" id="resto"
										cssStyle="text-align:right; height:20px; width:90%; font-size:140%; color:red" />
								</div>
							</div>
						</div>
						<div
							style="float: left; width: 100%; margin-top: 3px; font-size: 80%">
							<div style="float: left; width: 100%">
								<div style="float: left; width: 30%; text-align: center">
									<s:text name="dpe.residuo.debito" />
								</div>
								<div style="float: left; width: 70%;">
									<s:textfield label="" name="debito" id="debito"
										cssStyle="text-align:right; height:20px; width:90%; font-size:140%; color:red" />
								</div>
							</div>
						</div>
						<div
							style="float: left; width: 100%; margin-top: 5px; font-size: 80%">
							<div style="float: left; width: 100%">
								<div style="float: left; width: 30%; text-align: center">
									<s:text name="dpe.calcolo.resto" />
								</div>
								<div style="float: left; width: 10%; text-align: left;">
									<s:checkbox name="abilitaResto" id="abilitaResto"
										cssStyle="text-align:left;" />
								</div>
								<div style="float: left; width: 30%; text-align: center">
									<s:if test="registratoreCassa != null">
										<s:text name="dpe.scontrino.dettagliato" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</div>
								<div style="float: left; width: 10%; text-align: left;">
									<s:if test="registratoreCassa != null">
										<s:checkbox name="scontrinoDet" id="scontrinoDet"
											cssStyle="text-align:left;" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</div>
								<div
									style="float: right; width: 10%; margin-right: 5px; text-align: right;">
									<a href="#" onclick="javascript:print();"><img
										id="imgPrint" src="/app_img/print_48x48.png" width="25"
										height="25" title="<s:text name="tooltip.main_frame.Print"/>"
										alt="<s:text name="tooltip.main_frame.Print"/>" border="0" /></a>
								</div>
							</div>
						</div>
						<div
							style="float: left; width: 100%; margin-top: 3px; margin-left: 0px; font-size: 80%">
							<div style="float: left; width: 100%">
								<div style="float: left; width: 55%; text-align: center;">
									<s:if test="registratoreCassa != null">
										<s:text name="dpe.includi.pubblicazioni.scontrino" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</div>
								<div style="float: left; width: 10%; text-align: left;">
									<s:if test="registratoreCassa != null">
										<s:checkbox name="includiPubblicazioniScontrino"
											id="includiPubblicazioniScontrino"
											cssStyle="text-align:right;" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</div>
								<div
									style="float: left; width: 15%; margin-right: 5px; text-align: center;">
									<input type="button"
										value="<s:text name='label.menu_bar.Last.Bills'/>"
										style="height: 20px; font-size: 90%" align="center"
										onclick="javascript: showConti();" />
								</div>
							</div>
						</div>
					</fieldset>
				</div>
				<div id="abbDiv"
					style="float: left; width: 20%; margin-top: 0px; background-color: #cccccc;">
					<fieldset style="width: 98%; height: 200px;">
						<legend style="font-size: 80%">
							<s:text name="igeriv.abbonamenti" />
						</legend>
						<%-- RTAE CDL Febbraio 2017--%>
						<s:if test="authUser.rtaeAccessEnabled eq true">
							
							<s:if test="authUser.codFiegDl eq constants.COD_FIEG_MENTA 
										|| authUser.codFiegDl eq constants.COD_FIEG_CDL">
								<div id="ricaricaDiv"
									style="float: left; width: 100%; margin-top: 9%; text-align: center;">
									<input type="button" id="butRicarica"
										style="width: 90%; height: 30px"
										value="<s:text name='dpe.contact.form.modalita.ricarica.2'/>"
										align="center" class="modalitaRicaricaBut"
										onclick="javascript: setModalita('LISTINO_RICARICHE_COPIA');"
										title="<s:text name="entra.in.modalita.ricarica"/>" />
								</div>
								<div id="statoDiv"
									style="float: left; width: 100%; margin-top: 9%; text-align: center;">
									<input type="button" id="butStato"
										style="width: 90%; height: 30px"
										value="<s:text name='dpe.contact.form.modalita.stato.tessera.2'/>"
										align="center" class="modalitaStatoBut"
										onclick="javascript: setModalita('LETTURA_STATO');"
										title="<s:text name="entra.in.modalita.stato"/>" />
								</div>
								<div id="reportDiv"
									style="float: left; width: 100%; margin-top: 9%; text-align: center;">
									<input type="button" id="butReport"
										style="width: 90%; height: 30px"
										value="<s:text name='dpe.contact.form.report.vendite.ricariche'/>"
										align="center" class="modalitaReportBut"
										onclick="javascript: openReportVenditeRicaricheDialog();"
										title="<s:text name="mostra.report.vendite.ricariche"/>" />
								</div>
							</s:if>
							<s:else>
								<div
									style="margin-left: auto; margin-right: auto; width: 80%; height: 50px; text-align: center; margin-top: 20px;">
									<input type="button" id="butAbbonamento"
										value="<s:text name='dpe.contact.form.modalita.abbonamento'/>"
										align="center" class="modalitaAbbonamentoBut"
										onclick="javascript: setModalita('LISTINO_RICARICHE_COPIA');"
										title="<s:text name="entra.in.modalita.ricarica"/>" />
								</div>
								<div
									style="margin-left: auto; margin-right: auto; width: 80%; height: 50px; text-align: center;">
									<input type="button" id="butRicarica"
										value="<s:text name='dpe.contact.form.modalita.ricarica'/>"
										align="center" class="modalitaRicaricaBut"
										onclick="javascript: setModalita('LISTINO_RICARICHE_VALORE');"
										title="<s:text name="entra.in.modalita.ricarica"/>" />
								</div>
								<div
									style="margin-left: auto; margin-right: auto; width: 80%; height: 50px; text-align: center;">
									<input type="button" id="butStato"
										value="<s:text name='dpe.contact.form.modalita.stato.tessera'/>"
										align="center" class="modalitaStatoBut"
										onclick="javascript: setModalita('LETTURA_STATO');"
										title="<s:text name="entra.in.modalita.stato"/>" />
								</div>
							</s:else>
						</s:if>
						<s:else>
							<div style="width: 100%; text-align: center; font-size: 160%">
								&nbsp;</div>
							<div style="width: 100%; height: 50px; text-align: center;">
							</div>
							<div style="width: 100%; height: 50px; text-align: center;">
							</div>
						</s:else>
					</fieldset>
				</div>
			</div>
		</div>
		<div id="sidebar" class="bollaScrollDiv"
			syle="border:2px solid #339966">
			<ul id="ulButtonsR" class="ui-helper-reset">
				<s:iterator value="listPubblicazioniBarraSceltaRapidaDestra">
					<div style="float: left">
						<img id="<s:property value='codicePubblicazione'/>"
							title="<s:property value='titolo'/>&nbsp;<s:text name="igeriv.numero"/>&nbsp;<s:property value='numeroCopertina'/>&nbsp;(&euro;&nbsp;<s:property value='prezzoCopertinaFormat'/>)"
							style="display: inline-block; width: <s:text name="venditeIconWidth"/>px; height: <s:text name="venditeIconHeight"/>px;"
							alt="" class="mostSoldBarImg"
							src="/<s:property value='ImmagineDirAlias'/>/<s:property value='nomeImmagine'/>"
							border="1" width="<s:text name="venditeIconWidth"/>px"
							height="<s:text name="venditeIconHeight"/>px"
							onclick="javascript: vendiProdottoDaBarraSceltaRapida('<s:property value='codicePubblicazione'/>','<s:property value='codFiegDl'/>')">
					</div>
				</s:iterator>
			</ul>
		</div>
		<a href="#" id="separatorR"></a>
		<div class="clearer">&nbsp;</div>
	</div>
	<div id="bottomDiv"
		style="display:<s:if test="positionSizeDto eq null || (positionSizeDto neq null && positionSizeDto.barraProdottiVariVisible eq true)">block</s:if><s:else>none</s:else>; float:left; width:100%; height:170px; border:2px solid #3366CC; background-color:#cccccc;">
		<div id="arrowUpL" class="overlay"
			style="float: left; width: 5%; height: 100%; display: inline-block; text-align: center; cursor: pointer;">
			<div id="arrUpL"
				style="width: 100%; height: 100%; background-image: url(/app_img/arrow_up1.png); background-repeat: no-repeat; background-position: center;"></div>
		</div>
		<div id="pneDiv"
			style="float: left; width: 90%; height: 100%; display: inline-block;"
			class="bottomScrollDiv"></div>
		<div id="arrowUpR" class="overlay"
			style="float: right; width: 5%; height: 100%; display: inline-block; text-align: center; cursor: pointer;">
			<span id='closePneDiv' onclick='hideBarraProdottiVari()'
				title='<s:text name="igeriv.nascondi.barra.scelta.rapida.prodotti.vari"/>'>x</span>
			<div id="arrUpR"
				style="width: 100%; height: 100%; background-image: url(/app_img/arrow_up1.png); background-repeat: no-repeat; background-position: center;"></div>
		</div>
	</div>
</div>

<s:form id="ReportVenditeForm" action="report_reportVenditePdf.action"
	method="POST" theme="simple" validate="true"
	onsubmit="return (ray.ajax())">
	<input type="hidden" id="downloadToken" />
	<input type="hidden" id="downloadToken1" />
</s:form>

<%@include file="venditeMenus.jsp"%>

<s:if test="registratoreCassa != null">
	<%@include file="venditeApplet.jsp"%>
</s:if>

