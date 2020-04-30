<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
	.dojoTree {
		font: caption;
		font-size: 100%;
		font-weight: normal;
		overflow-y: scroll;
		overflow-x: hidden;
		height:450px;
	}
	
	
	.dojoTreeNodeLabelTitle {
		padding-left: 5px;
		color: WindowText;
	}
	
	.dojoTreeNodeLabel {
		cursor:hand;
		cursor:pointer;
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
	
	.mostSoldBarImg { cursor: pointer; }
	
	.pneImg { cursor: pointer;}
	
	.pneImg:hover { background: silver; cursor:pointer;}
	
	#sidebarL {
	    line-height: 300px;
	    text-align: center;
	    background-color:#dfe4e8; 
	    border:2px solid #669999;
	    display: none;
	    width: 1%;
	}
	
	#sidebar {
	    line-height: 300px;
	    text-align: center;
	    background-color:#dfe4e8; 
	    border:2px solid #669999;
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
	
	.sidebar-at-left-L #sidebar {margin-right: 0px;} 
	.sidebar-at-right-L #sidebar {margin-left: 0px;}
	
	.sidebar-at-left-L #content, .use-sidebar-L.sidebar-at-right #sidebar, .sidebar-at-right #separatorL {float: left;}
	.sidebar-at-right-L #content, .use-sidebar-L.sidebar-at-left #sidebar, .sidebar-at-left #separatorL {float: left;}
	
	.sidebar-at-left #sidebar {margin-right: 0px;} 
	.sidebar-at-right #sidebar {margin-left: 0px;}
	  
	.sidebar-at-left #content, .use-sidebar.sidebar-at-right #sidebar, .sidebar-at-right #separatorR {float: right;}
	.sidebar-at-right #content, .use-sidebar.sidebar-at-left #sidebar, .sidebar-at-left #separatorR {float: left;}
	 
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
</style>
<s:url id="smdUrl" namespace="/" action="VenditeCardRpc.action"/>
<s:url id="vendutoGiornalieroUrl" namespace="/" action="VenditeCardRpc.action"/>
<div style="width:100%; height:400px; margin-top:0px; background-color:#cccccc">
	<div id="topDiv" style="width:100%; height:28px; ">
		<div style="float:left; width:18%; display:block">&nbsp;</div>
		<div id="contoCliente" style="float:left; width:61%; text-align:center; font-size:100%; font-weight:bold;">&nbsp;</div>
		<div id="arrowUpDiv" style="float:right; width:18%; display:block; text-align:center; cursor:pointer"><img id="arrUp" alt="" src="/app_img/arrow_up.gif" border="0"></div>
	</div>
	<div class="use-sidebar sidebar-at-right use-sidebar-L sidebar-at-right-L" id="main">
		<div id="sidebarL" style="float:left; border:2px solid #3366CC;" class="bollaScrollDiv">
			<ul id="ulButtons" class="ui-helper-reset">
				<s:iterator value="listPubblicazioniBarraSceltaRapida">
					<div style="float:left"><img id="<s:property value='codicePubblicazione'/>" title="<s:property value='titolo'/>&nbsp;<s:text name="igeriv.numero"/>&nbsp;<s:property value='numeroCopertina'/>&nbsp;(&euro;&nbsp;<s:property value='prezzoCopertinaFormat'/>)" style="display: inline-block; width: <s:text name="venditeIconWidth"/>px; height: <s:text name="venditeIconHeight"/>px;" alt="" class="mostSoldBarImg" src="/<s:property value='ImmagineDirAlias'/>/<s:property value='nomeImmagine'/>" border="1" width="<s:text name="venditeIconWidth"/>px" height="<s:text name="venditeIconHeight"/>px" onclick="javascript: vendiProdottoDaBarraSceltaRapida('<s:property value='barcode'/>','<s:property value='idtn'/>')"></div>
				</s:iterator> 	
			</ul>
		</div> 
	    <a href="#" id="separatorL"></a>
		<div id="content">  
			<div id="venditeDiv" style="height:180px; width:100%; text-align:center; border:1px solid black; background-color:#fff">
				<table dojoType="dojox.grid.DataGrid" id="venditeTable" jsid="progressivo" style="font-size:100%;">  
					<thead>
						<tr class="titleRow">
							<th class="tableHeader" field="quantita" width="4%" styles='text-align: center;'><s:text name="igeriv.quantita.short"/></th>
							<th class="tableHeader" field="titolo" width="28%"><s:text name="igeriv.titolo"/></th>
							<th class="tableHeader" field="sottoTitolo" width="21%"><s:text name="igeriv.sottotitolo"/></th>
							<th class="tableHeader" field="numeroCopertina" width="10%" styles='text-align: center;'><s:text name="igeriv.numero"/></th>
							<th class="tableHeader" field="prezzoCopertinaFormat" width="9%" styles='text-align: right;'><s:text name="igeriv.prezzo"/></th>
							<th class="tableHeader" field="importoFormat" width="10%" styles='text-align: right;'><s:text name="igeriv.importo"/></th>
							<th class="tableHeader" field="giacenzaIniziale" width="8%" styles='text-align: center;'><s:text name="igeriv.giacienza"/></th>
						</tr>
					</thead>
				</table>
			</div>
			<div style="width:100%; text-align:center; height:200px; text-align:center; margin-left:auto; margin-right:auto; background-color:#cccccc;">
				<div style="float:left; width:35%;">										
					<fieldset style="width:98%; height:140px;"><legend style="font-size:80%"><s:text name="igeriv.prodotto" /></legend>
						<s:form id="VenditeForm" action="#" method="POST" theme="simple" validate="false" onsubmit="return (verifyInputContent() && ray.ajax())">
							<div style="text-align:left; font-size:80%">								
								<div style="width:100%; font-size:120%">
									<s:textfield name="qta" id="qta" maxlength="4" cssStyle="text-align:right; width:10%" value="1"/>&nbsp;X&nbsp;<s:textfield name="inputText" id="inputText" cssStyle="width:65%;"/>			
								</div>
								<div style="width:100%; font-size:100%;">
									<div style="float:left; text-align:left; white-space:nowrap;">
										<s:text name="username.cliente" />&nbsp;
										<select name="idCliente" id="idCliente" style="width:65%;">
											<option value="-1"><s:text name="username.anomimo"/></option>
											<optgroup label="<s:text name="igeriv.con.estratto.conto"/>">
												<s:iterator value="listClientiConEC">
													<option value="<s:property value="codCliente"/>"><s:property value="nomeCognomeEscaped"/></option>
												</s:iterator>
											</optgroup>
											<optgroup label="<s:text name="igeriv.senza.estratto.conto"/>">
												<s:iterator value="listClientiSenzaEC">
													<option value="<s:property value="codCliente"/>"><s:property value="nomeCognomeEscaped"/></option>
												</s:iterator>
											</optgroup>
										</select>
							    	</div>
								</div>
								<div style="width:100%;">
									<div style="float:left; width:80%; text-align:left"><s:text name="dpe.mostra.tutte.uscite" /></div>
									<div style="float:left; text-align:left"><s:checkbox name="mostraTutteUscite" id="mostraTutteUscite" cssStyle="text-align:left;"/></div>
								</div>
								<div style="width:100%; ">
									<div style="float:left; width:80%; text-align:left"><s:text name="dpe.ricerca.prodotti.vari" /></div>
									<div style="float:left; text-align:left"><s:checkbox name="ricercaProdottiVari" id="ricercaProdottiVari" cssStyle="text-align:right;"/></div>
								</div>
								<s:if test="authUser.hasEdicoleAutorizzateAggiornaBarcode eq true">
									<div style="width:100%;">
										<div style="float:left; width:80%; text-align:left"><s:text name="dpe.aggiorna.barcode" /></div>
										<div style="float:left; text-align:left"><s:checkbox name="aggiornaBarcode" id="aggiornaBarcode" cssStyle="text-align:right;"/></div>
									</div>
								</s:if>
								<s:else>
									<div style="width:100%;">
										<div style="float:left; width:80%; text-align:left">&nbsp;</div>
										<div style="float:left; text-align:left">&nbsp;</div>
									</div>
								</s:else>
							</div>
							<s:hidden name="closedAccount" id="closedAccount"/>				
							<s:hidden name="idConto" id="idConto"/>
						</s:form>	
					</fieldset>
					<fieldset id="aiutoDiv" style="height:59px; width:98%; font-size:70%; background-color:#99ccff;">
						<div style="width:100%; text-align:center;"><input type="button" id="butVendutoGiornaliero" value="<s:text name="igeriv.venduto"/>&nbsp;(Alt+'V')" align="center" onclick="javascript: vendutoGiornaliero();"/></div>
						<div style="width:100%; text-align:center;"><input type="button" id="butAzzeraConto" value="<s:text name="plg.reimposta"/>&nbsp;(Alt+'A')" align="center" onclick="javascript: doAzzeraConto();"/></div>
						<div style="width:100%; text-align:center;"><input type="button" id="butSpazio" value="<s:text name="igeriv.chiudi.conto"/>&nbsp;(<s:text name="label.main_frame.help.Close_Bill.key"/>)" align="center" onclick="javascript: doCloseAccount();"/></div>
					</fieldset>
				</div>
				<div style="float:left; width:45%; background-color:#cccccc;">
					<fieldset style="width:98%; height:200px;"><legend style="font-size:80%"><s:text name="column.calc.totals" /></legend>
						<div style="float:left; width:100%; margin-top:3px; font-size:80%">			
							<div style="float:left; width:100%">
								<div style="float:left; width:30%; text-align:center"><s:text name="column.calc.totale.pezzi" /></div>
								<div style="float:left; width:70%;"><s:textfield name="totalePezzi" id="totalePezzi" readonly="true" cssStyle="text-align:right; height:20px; width:90%; font-size:140%"/></div>			
							</div>			
						</div>
						<div style="float:left; width:100%; margin-top:3px; font-size:80%">			
							<div style="float:left; width:100%">
								<div style="float:left; width:30%; text-align:center">
								<s:if test="registratoreCassa != null">
									<s:text name="column.calc.totals" />
								</s:if>
								<s:else>
									<s:text name="column.calc.total" />
								</s:else>
								</div>
								<div style="float:left; width:70%;">
								<s:if test="registratoreCassa != null">
									<input type="text" name="totalePubb" id="totalePubb" style="text-align:right; height:20px; width:25%; font-size:100%; font-weight:bold; border:1px solid #3366CC;" title="<s:text name="column.calc.total.pubblicazioni"/>"/>
									<input type="text" name="totaleScontrino" id="totaleScontrino" style="text-align:right; height:20px; width:25%; font-size:100%; font-weight:bold; border:1px solid #339966;" title="<s:text name="column.calc.total.scontrino"/>" />
									<input type="text" name="totale" id="totale" style="text-align:right; height:20px; width:32%; font-size:140%; font-weight:bold; border:1px solid red;" title="<s:text name="column.calc.grand.total"/>"/>
								</s:if>
								<s:else>
									<s:textfield name="totale" id="totale" cssStyle="text-align:right; height:20px; width:90%; font-size:160%; font-weight:bold; "/>
								</s:else>
								</div>			
							</div>			
						</div>
						<div style="float:left; width:100%; margin-top:3px; font-size:80%">			
							<div style="float:left; width:100%"> 
								<div style="float:left; width:30%; text-align:center"><s:text name="dpe.contanti" /></div>
								<div style="float:left; width:70%;"><s:textfield name="contanti" id="contanti" cssStyle="text-align:right; height:20px; width:90%; font-size:140%; color:green"/></div>			
							</div>			
						</div>
						<div style="float:left; width:100%; margin-top:3px; font-size:80%">			
							<div style="float:left; width:100%">
								<div style="float:left; width:30%; text-align:center"><s:text name="dpe.resto" /></div>
								<div style="float:left; width:70%;"><s:textfield name="resto" id="resto" cssStyle="text-align:right; height:20px; width:90%; font-size:140%; color:red"/></div>			
							</div>			
						</div>
						<div style="float:left; width:100%; margin-top:3px; font-size:80%">			
							<div style="float:left; width:100%">
								<div style="float:left; width:30%; text-align:center"><s:text name="dpe.residuo.debito" /></div>
								<div style="float:left; width:70%;"><s:textfield label="" name="debito" id="debito" cssStyle="text-align:right; height:20px; width:90%; font-size:140%; color:red"/></div>			
							</div>			
						</div>
						<div style="float:left; width:100%; margin-top:5px; font-size:80%">			
							<div style="float:left; width:100%">
								<div style="float:left; width:33%; text-align:center"><s:text name="dpe.calcolo.resto" /></div>
								<div style="float:left; width:30%; text-align:left;"><s:checkbox name="abilitaResto" id="abilitaResto" cssStyle="text-align:right;"/></div>
								<div style="float:right; width:15%; margin-right:5px; text-align:right;">
									<a href="#" onclick="javascript:print();"><img id="imgPrint" src="/app_img/print_48x48.png" width="25" height="25" title="<s:text name="tooltip.main_frame.Print"/>" alt="<s:text name="tooltip.main_frame.Print"/>" border="0"/></a>
								</div>			
							</div>			
						</div>	
						<div style="float:left; width:100%; margin-top:3px; margin-left:0px; font-size:80%">			
							<div style="float:left; width:100%">
								<s:if test="registratoreCassa != null">
									<div style="float:left; width:33%; text-align:center;"><s:text name="dpe.includi.pubblicazioni.scontrino" /></div>
									<div style="float:left; width:30%; text-align:left;"><s:checkbox name="includiPubblicazioniScontrino" id="includiPubblicazioniScontrino" cssStyle="text-align:right;"/></div>
								</s:if>
								<s:else>
									<div style="float:left; width:33%; text-align:center;">&nbsp;</div>
									<div style="float:left; width:30%; text-align:left;">&nbsp;</div>
								</s:else>
								<div style="float:left; width:15%; margin-right:5px; text-align:center;">
									<input type="button" value="<s:text name='label.menu_bar.Last.Bills'/>" align="center" onclick="javascript: showConti();"/>
								</div>
							</div>	
						</div>		
					</fieldset>
				</div>
				<div id="abbDiv" style="float:left; width:20%; margin-top:0px; background-color:#cccccc;">									
					<fieldset style="width:98%; height:200px;"><legend style="font-size:80%"><s:text name="igeriv.abbonamenti" /></legend>
						<div style="width:100%; text-align:center; font-size:160%">
							&nbsp;	
						</div>
						<div style="width:100%; height:50px; text-align:center;">	
							<!-- <input type="button" id="butRicarica" value="<s:text name='dpe.contact.form.modalita.ricarica'/>" align="center" class="modalitaRicaricaBut" onclick="javascript: setModalita('MODALITA_RICARICA');" title="<s:text name="entra.in.modalita.ricarica"/>"/>  -->
						</div>
						<div style="width:100%; height:50px; text-align:center;">
							<!-- <input type="button" id="butStato" value="<s:text name='dpe.contact.form.modalita.stato.tessera'/>" align="center" class="modalitaStatoBut" onclick="javascript: setModalita('LETTURA_STATO');" title="<s:text name="entra.in.modalita.stato"/>"/>  -->
						</div> 
					</fieldset>
				</div> 
			</div>
			<div id="dialogContent" class="eXtremeTable popup_block" style="font-size:110%; z-index:99999; background-color:#cccccc">
				<div style="width=640px; height:520px; z-index:9">
					<table dojoType="dojox.grid.DataGrid" id="pubblicazioniTable" 
						   jsid="idtn" class="extremeTableFields" 
						   style="font-size:100%; width:630px; text-align:left; margin-left:0px;" cellpadding="0px" cellspacing="0px">
						<thead> 
							<tr>
								<th class="tableHeader" field="titolo" width="26%" ><s:text name="igeriv.titolo"/></th>
								<th class="tableHeader" field="sottoTitolo" width="26%"><s:text name="igeriv.sottotitolo"/></th>
								<th class="tableHeader" field="numeroCopertina" width="12%" styles='text-align: center;'><s:text name="igeriv.numero"/></th>
								<th class="tableHeader" field="dataUscitaFormat" width="16%" styles='text-align: center;'><s:text name="igeriv.data.uscita"/></th>
								<th class="tableHeader" field="prezzoCopertinaFormat" width="12%" styles='text-align: right;'><s:text name="igeriv.prezzo"/></th>
							</tr>
						</thead>
					</table>
				</div>		
			</div>
			<div id="dialogContentBarcode" class="eXtremeTable popup_block" style="font-size:110%; z-index:99999; background-color:#cccccc">
				<div style="width=640px; height:520px; z-index:9">
					<table dojoType="dojox.grid.DataGrid" id="pubblicazioniTableBarcode" 
						   multiple="true" alternateRows="true" valueField="idtn" class="extremeTableFields" 
						   style="font-size:100%; width:630px; text-align:left; margin-left:0px;" cellpadding="0px" cellspacing="0px">
						<thead> 
							<tr>
								<th class="tableHeader" field="titolo" dataType="String" width="26%" style="text-align:left"><s:text name="igeriv.titolo"/></th>
								<th class="tableHeader" field="sottoTitolo" dataType="String" width="26%" style="width:26%;text-align:left"><s:text name="igeriv.sottotitolo"/></th>
								<th class="tableHeader" field="numeroCopertina" dataType="String" width="12%" style="text-align:center"><s:text name="igeriv.numero"/></th>
								<th class="tableHeader" field="dataUscitaFormat" dataType="String" width="16%" style="text-align:center"><s:text name="igeriv.data.uscita"/></th>
								<th class="tableHeader" field="prezzoCopertinaFormat" dataType="String" width="12%" style="width:text-align:center"><s:text name="igeriv.prezzo"/></th>
							</tr>
						</thead>
					</table>
				</div>		
			</div>
			<div id="dialogContentRichiediBarcode" class="eXtremeTable popup_block" style="font-size:110%; z-index:99999; background-color:#cccccc">
				<div style="width=640px; height:520px; z-index:9">
					<table dojoType="dojox.grid.DataGrid" id="pubblicazioniTableRichiediBarcode" 
						   multiple="true" alternateRows="true" valueField="idtn" class="extremeTableFields" 
						   style="font-size:100%; width:630px; text-align:left; margin-left:0px;" cellpadding="0px" cellspacing="0px">
						<thead> 
							<tr>
								<th class="tableHeader" field="titolo" dataType="String" width="26%" style="text-align:left"><s:text name="igeriv.titolo"/></th>
								<th class="tableHeader" field="sottoTitolo" dataType="String" width="26%" style="width:26%;text-align:left"><s:text name="igeriv.sottotitolo"/></th>
								<th class="tableHeader" field="numeroCopertina" dataType="String" width="12%" style="text-align:center"><s:text name="igeriv.numero"/></th>
								<th class="tableHeader" field="dataUscitaFormat" dataType="String" width="16%" style="text-align:center"><s:text name="igeriv.data.uscita"/></th>
								<th class="tableHeader" field="prezzoCopertinaFormat" dataType="String" width="12%" style="width:text-align:center"><s:text name="igeriv.prezzo"/></th>
							</tr>
						</thead>
					</table>
				</div>		
			</div>
		</div>
		<div id="sidebar" class="bollaScrollDiv" syle="border:2px solid #339966">
			<div id="pneDiv" style="float:left"></div>
		</div> 
	    <a href="#" id="separatorR"></a>
	    <div class="clearer">&nbsp;</div>
    </div>
    
	<div id="treeContiContainer" class="eXtremeTable popup_block">
		<div id="treePlaceHolder" class="contiScrollDiv">
			<div id="treeDiv"></div>
		</div>
	</div>
	
	<!-- Right Click Menu --> 
	<ul id="myMenu" class="contextMenu" style="width:180px">
	    <li class="delete"><a href="#delete"><s:text name="dpe.contact.form.reset"/></a></li>  
	</ul>
	
	<!-- Barcode Finalizza Menu --> 
	<ul id="barcodeMenu" class="contextMenu" style="width:220px">
	    <li class="download"><a href="#downloadPdfBarcode"><s:text name="igeriv.download.pdf.barcode"/></a></li>  
	</ul>
	
</div>

<s:form id="ReportVenditeForm" action="report_reportVenditePdf.action" method="POST" theme="simple" validate="true" onsubmit="return (ray.ajax())">	
	<input type="hidden" id="downloadToken"/>
	<input type="hidden" id="downloadToken1"/>
</s:form>

<div id="dialogContentRicariche" class="eXtremeTable popup_block" style="font-size:120%; z-index:99999; background-color:#cccccc">
	<div style="width:640px; height:330px; z-index:9">
		<table dojoType="dojox.grid.DataGrid" id="ricaricheTable" 
			   multiple="true" alternateRows="true" valueField="codTipologiaMinicard" class="extremeTableFields"
			   style="background:#99cccc; cursor:pointer; font-size:120%; width:630px; text-align:left; margin-left:0px;" cellpadding="5px" cellspacing="0px">
			<thead>
				<tr>
					<th class="tableHeader" field="tipologiaMinicardEditore" dataType="String" align="center" width="8%" style="text-align:center"><s:text name="gp.codice.tipologia.ricarica"/></th>
					<th class="tableHeader" field="descrizioneTipologia" dataType="String" align="left" width="68%" style="width:26%;text-align:left"><s:text name="gp.tipologia.ricarica"/></th>
					<th class="tableHeader" field="importoRicaricaFormat" dataType="String" width="8%" align="right" style="text-align:right"><s:text name="igeriv.importo.ricarica"/></th>
					<th class="tableHeader" field="copieRicarica" dataType="Integer" width="8%" align="center" style="text-align:center"><s:text name="igeriv.copie.ricarica"/></th>
					<th class="tableHeader" field="durataGiorniRicarica" dataType="Integer" align="center" width="8%" style="width:text-align:center"><s:text name="igeriv.durata.gg.ricarica"/></th>
				</tr>
			</thead>
		</table>
	</div>
</div>

<div id="venditeIGerivCard" style="background-color:#dfe4e8;">
	<div style="height:10px;" class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><span><s:text name="igeriv.preferiti"/></span></div>
	<div id="preferiti" style="height:42%; overflow-y:scroll"></div>
	<div style="height:10px" class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><span><s:text name="igeriv.suggerimenti"/></span></div>
	<div id="suggerimenti" style="height:42%; overflow-y:scroll"></div>
</div>

<s:if test="registratoreCassa != null">
	<applet id="regCassaApplet" code="it.dpe.igeriv.web.applet.regcassa.RegistratoreCassaApplet" archive="regCassaApplet<s:text name="igeriv.version.timestamp"/>.jar, json-lib-2.3.jar, ezmorph-1.0.3.jar, commons-collections-3.1.jar, commons-lang-2.0.jar, commons-logging-1.1.1.jar, commons-beanutils-1.8.0.jar, spring-security-core-3.0.5.RELEASE.jar, activation-1.1.1.jar, mail-1.4.jar" name="<s:text name="igeriv.reg.cassa"/>" width="0" height="0">
		<param name="type" value="application/x-java-applet;version=1.5">
	    <param name="codebase" value="${pageContext.request.contextPath}${appletDir}">
	    <param name="scriptable" value="true">
	    <param name="toSendDir" value="${registratoreCassa.pathLocaleFile}">
	    <param name="userRegCassaLocalDir" value="<s:property value="%{#session.userRegCassaLocalDir}"/>">
	    <param name="scontriniRegCassaLocalDir" value="<s:property value="%{#session.scontriniRegCassaLocalDir}"/>">
	    <param name="fileNamePrefix" value="${registratoreCassa.fileNamePrefix}">
	    <param name="fileNameDigitLength" value="${registratoreCassa.fileNameDigitLength}">
	    <param name="codRegCassa" value="${registratoreCassa.codRegCassa}">
	    <param name="millsTaskTimeout" value="${registratoreCassa.millsTaskTimeout}">
	    <param name="nomeProcesso" value="${registratoreCassa.nomeProcesso}">
	    <param name="iniFileContent" value="${registratoreCassa.contentFileIni}">
	    <param name="abilitaLog" value="${registratoreCassa.abilitaLog}">
	    <param name="mailFrom" value="${registratoreCassa.mailFrom}">
	    <param name="mailTo" value="${registratoreCassa.mailTo}">
	    <param name="logFileName" value="${registratoreCassa.logFileName}">
	    <param name="logFileSize" value="${registratoreCassa.logFileSize}">
	    <param name="smtpHost" value="${registratoreCassa.smtpHostC}">
	    <param name="smtpUser" value="${registratoreCassa.smtpUserC}">
	    <param name="smtpPwd" value="${registratoreCassa.smtpPwdC}">
	    <param name="supportedOS" value="${registratoreCassa.sistemiOperativiSupportati}">
	    <param name="binaryUnixLinux" value="${registratoreCassa.binaryUnixLinux}">
	    <param name="binaryMac" value="${registratoreCassa.binaryMac}">
	    <param name="binarySolaris" value="${registratoreCassa.binarySolaris}">
	    <param name="millsTaskInterval" value="<s:property value="%{#session.millsTaskInterval}"/>">
	</applet>
</s:if>

