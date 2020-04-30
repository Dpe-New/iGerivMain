<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>

<div id="treeContiContainer" class="eXtremeTable popup_block">
	<div id="treePlaceHolder" class="contiScrollDiv">
		<div id="treeDiv"></div>
	</div>
</div>

<!-- Right Click Menu -->
<ul id="myMenu" class="contextMenu" style="width: 250px">
	<li class="delete"><a href="#delete"><s:text
				name="dpe.contact.form.reset" /></a></li>
	<li class="rifo"><a href="#rifo"><s:text
				name="igeriv.bolla.rifornimenti" /></a></li>
	<li class="rifo"><a href="#descValore"><s:text
				name="igeriv.vendita.valore.descrizione" /></a></li>
</ul>

<ul id="clientiMenu" class="contextMenu" style="width: 250px">
	<li class="edit"><a href="#edit"><s:text
				name="dpe.contact.form.aggiungi.cliente" /></a></li>
</ul>

<!-- Barcode Finalizza Menu -->
<ul id="barcodeMenu" class="contextMenu" style="width: 220px">
	<li class="download"><a href="#downloadPdfBarcode"><s:text
				name="igeriv.download.pdf.barcode" /></a></li>
</ul>

<div id="dialogContent" class="eXtremeTable popup_block"
	style="font-size: 110%; z-index: 99999; background-color: #cccccc">
	<div style="width: 640px; height: 520px; z-index: 9">
		<table dojoType="dojox.grid.DataGrid" id="pubblicazioniTable"
			jsid="idtn" class="extremeTableFields"
			style="font-size: 100%; width: 630px; text-align: left; margin-left: 0px;"
			cellpadding="0px" cellspacing="0px">
			<thead>
				<tr>
					<th class="tableHeader" field="titolo" width="26%"><s:text
							name="igeriv.titolo" /></th>
					<th class="tableHeader" field="sottoTitolo" width="26%"><s:text
							name="igeriv.sottotitolo" /></th>
					<th class="tableHeader" field="numeroCopertina" width="12%"
						styles='text-align: center;'><s:text name="igeriv.numero" /></th>
					<th class="tableHeader" field="dataUscitaFormat" width="16%"
						styles='text-align: center;'><s:text
							name="igeriv.data.uscita" /></th>
					<th class="tableHeader" field="prezzoCopertinaFormat" width="12%"
						styles='text-align: right;'><s:text name="igeriv.prezzo" /></th>
				</tr>
			</thead>
		</table>
	</div>
</div>

<div id="dialogContentBarcode" class="eXtremeTable popup_block"
	style="font-size: 110%; z-index: 99999; background-color: #cccccc">
	<div style="width: 640px; height: 520px; z-index: 9">
		<table dojoType="dojox.grid.DataGrid" id="pubblicazioniTableBarcode"
			multiple="true" alternateRows="true" valueField="idtn"
			class="extremeTableFields"
			style="font-size: 100%; width: 630px; text-align: left; margin-left: 0px;"
			cellpadding="0px" cellspacing="0px">
			<thead>
				<tr>
					<th class="tableHeader" field="titolo" dataType="String"
						width="26%" style="text-align: left"><s:text
							name="igeriv.titolo" /></th>
					<th class="tableHeader" field="sottoTitolo" dataType="String"
						width="26%" style="width: 26%; text-align: left"><s:text
							name="igeriv.sottotitolo" /></th>
					<th class="tableHeader" field="numeroCopertina" dataType="String"
						width="12%" style="text-align: center"><s:text
							name="igeriv.numero" /></th>
					<th class="tableHeader" field="dataUscitaFormat" dataType="String"
						width="16%" style="text-align: center"><s:text
							name="igeriv.data.uscita" /></th>
					<th class="tableHeader" field="prezzoCopertinaFormat"
						dataType="String" width="12%" style="width: text-align:center"><s:text
							name="igeriv.prezzo" /></th>
				</tr>
			</thead>
		</table>
	</div>
</div>

<div id="dialogContentRichiediBarcode" class="eXtremeTable popup_block"
	style="font-size: 110%; z-index: 99999; background-color: #cccccc">
	<div style="width: 640px; height: 520px; z-index: 9">
		<table dojoType="dojox.grid.DataGrid"
			id="pubblicazioniTableRichiediBarcode" multiple="true"
			alternateRows="true" valueField="idtn" class="extremeTableFields"
			style="font-size: 100%; width: 630px; text-align: left; margin-left: 0px;"
			cellpadding="0px" cellspacing="0px">
			<thead>
				<tr>
					<th class="tableHeader" field="titolo" dataType="String"
						width="26%" style="text-align: left"><s:text
							name="igeriv.titolo" /></th>
					<th class="tableHeader" field="sottoTitolo" dataType="String"
						width="26%" style="width: 26%; text-align: left"><s:text
							name="igeriv.sottotitolo" /></th>
					<th class="tableHeader" field="numeroCopertina" dataType="String"
						width="12%" style="text-align: center"><s:text
							name="igeriv.numero" /></th>
					<th class="tableHeader" field="dataUscitaFormat" dataType="String"
						width="16%" style="text-align: center"><s:text
							name="igeriv.data.uscita" /></th>
					<th class="tableHeader" field="prezzoCopertinaFormat"
						dataType="String" width="12%" style="width: text-align:center"><s:text
							name="igeriv.prezzo" /></th>
				</tr>
			</thead>
		</table>
	</div>
</div>

<div id="dialogCopieContent" class="eXtremeTable popup_block"
	style="font-size: 110%; z-index: 99999; background-color: #cccccc">
	<div
		style="width: 640px; height: 330px; overflow-y: scroll; z-index: 9"
		id="div1" class="dialogCopieValore">
		<table dojoType="dojox.grid.DataGrid" id="ricaricheCopieTable"
			jsid="codTipologiaMinicard" style="font-size: 100%;" canSort="false"
			escapeHTMLInData="false" cellOverClass="cursorPointer">
			<thead>
				<tr class="title">
					<th field="descrizioneTipologiaPerListino" width="55%"><s:text
							name="gp.tipologia.ricarica" /></th>
					<th field="importoRicaricaFormat" width="15%"
						styles='text-align: right;'><s:text
							name="igeriv.importo.ricarica" /></th>
					<th field="copieRicarica" width="15%" styles='text-align: right;'><s:text
							name="igeriv.copie.ricarica" /></th>
					<th field="durataGiorniRicarica" width="15%"
						styles='text-align: right;'><s:text
							name="igeriv.durata.gg.ricarica" /></th>
				</tr>
			</thead>
		</table>
	</div>
</div>

<div id="dialogValoreContent" class="eXtremeTable popup_block"
	style="font-size: 110%; z-index: 99999; background-color: #cccccc">
	<div
		style="width: 640px; height: 330px; overflow-y: scroll; z-index: 9"
		id="div2" class="dialogCopieValore">
		<table dojoType="dojox.grid.DataGrid" id="ricaricheValoreTable"
			jsid="codTipologiaMinicard" style="font-size: 100%;" canSort="false"
			escapeHTMLInData="false" cellOverClass="cursorPointer">
			<thead>
				<tr class="title">
					<th field="descrizioneTipologiaPerListino" width="70%"><s:text
							name="label.plg_barcode_input_module_listener.Recharge" /></th>
					<th field="importoRicaricaFormat" width="15%"
						styles='text-align: right;'><s:text
							name="igeriv.importo.ricarica" /></th>
					<th field="durataGiorniRicarica" width="15%"
						styles='text-align: right;'><s:text
							name="igeriv.durata.gg.ricarica" /></th>
				</tr>
			</thead>
		</table>
	</div>
</div>

<div id="dialogAbbonatiPlgContent" class="eXtremeTable popup_block"
	style="font-size: 110%; z-index: 99999; background-color: #cccccc">
	<div id="dialogAbbonatiPlgContentTitle" class="dialogTitle"></div>
	<div
		style="width: 640px; height: 330px; overflow-y: scroll; z-index: 9"
		id="div2" class="dialogCopieValore">
		<table dojoType="dojox.grid.DataGrid" id="venditeAbbonatiPlgTable"
			jsid="dataProdottoVenduto" style="font-size: 100%;"
			cellOverClass="cursorPointer" escapeHTMLInData="false">
			<thead>
				<tr class="title">
					<th class="tableHeader" field="dataProdottoVendutoFormat"
						dataType="String" width="50%" style="text-align: center"><s:text
							name="igeriv.data.vendita" /></th>
					<th class="tableHeader" field="copieConsegnate" width="50%"
						styles='text-align:center;'><s:text name="igeriv.copie" /></th>
				</tr>
			</thead>
		</table>
	</div>
</div>

<div id="dialogMinicardPlgContent" class="eXtremeTable popup_block"
	style="font-size: 110%; z-index: 99999; background-color: #cccccc">
	<div id="dialogMinicardPlgContentTitle" class="dialogTitle"></div>
	<div
		style="width: 640px; height: 330px; overflow-y: scroll; z-index: 9"
		id="div2" class="dialogCopieValore">
		<table dojoType="dojox.grid.DataGrid" id="venditeMinicardPlgTable"
			jsid="dataVendita" style="font-size: 100%;"
			cellOverClass="cursorPointer" escapeHTMLInData="false">
			<thead>
				<tr class="title">
					<th class="tableHeader" field="dataVenditaFormat" dataType="String"
						width="50%" style="text-align: center"><s:text
							name="igeriv.data.vendita" /></th>
					<th class="tableHeader" field="copieConsegnate" width="50%"
						styles='text-align:center;'><s:text name="igeriv.copie" /></th>
				</tr>
			</thead>
		</table>
	</div>
</div>

<div id="dialogRicarichePlgContent" class="eXtremeTable popup_block"
	style="font-size: 110%; z-index: 99999; background-color: #cccccc">
	<div id="dialogRicarichePlgContentTitle" class="dialogTitle"></div>
	<div
		style="width: 640px; height: 330px; overflow-y: scroll; z-index: 9"
		id="div2" class="dialogCopieValore">
		<table dojoType="dojox.grid.DataGrid" id="ricaricheMinicardPlgTable"
			style="font-size: 100%;"
			cellOverClass="cursorPointer" escapeHTMLInData="false">
			<thead>
				<tr class="title">
					<th class="tableHeader" field="importoRicaricaFormat" width="25%"
						styles='text-align:right;'><s:text name="igeriv.taglio.ricariche" /></th>
					<th class="tableHeader" field="numRicaricheFormat" width="25%"
						styles='text-align:right;'><s:text name="igeriv.numero.ricariche" /></th>
					<th class="tableHeader" field="importoLordoFormat" width="25%"
						styles='text-align:right;'><s:text name="igeriv.importo.lordo" /></th>
					<th class="tableHeader" field="importoNettoFormat" width="25%"
						styles='text-align:right;'><s:text name="igeriv.importo.netto" /></th>
				</tr>
			</thead>
		</table>
	</div>
</div>



<div id="venditeIGerivCard" style="background-color: #dfe4e8;">
	<div style="height: 10px;"
		class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
		<span><s:text name="igeriv.preferiti" /></span>
	</div>
	<div id="preferiti" style="height: 42%; overflow-y: scroll"></div>
	<div style="height: 10px"
		class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
		<span><s:text name="igeriv.suggerimenti" /></span>
	</div>
	<div id="suggerimenti" style="height: 42%; overflow-y: scroll"></div>
</div>