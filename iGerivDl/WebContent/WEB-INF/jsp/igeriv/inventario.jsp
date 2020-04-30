<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:url id="smdUrlVendite" namespace="/" action="VenditeCardRpc.action"/>
<s:url id="smdUrlInventario" namespace="/" action="InventarioRpc.action"/>
<fieldset style="margin-left:5%; width:90%; height:180px; text-align:center; margin-top:12px;"><legend style="font-size:100%;"><s:text name="igeriv.pubblicazione"/>&nbsp;(<s:text name="igeriv.ricerca.barcode.titolo"/>)</legend>
	<div style="width:60%; margin-left:auto; margin-right:auto; margin-top:10px; white-space:nowrap;"> 
		<img name="findPub" id="findPub" src="/app_img/find.png" alt="<s:text name="dpe.contact.form.modalita.ricerca"/>" border="0" title="<s:text name="dpe.contact.form.modalita.ricerca"/>" style="cursor:pointer" width="22px" height="22px" onclick="javascript:setModalita(MODALITA_RICERCA)"/>&nbsp;<s:textfield name="qta" id="qta" cssStyle="width:10%; text-align:right; font-size:150%;" value="1"/><span style="font-size:150%">&nbsp;X&nbsp;</span><s:textfield name="inputText" id="inputText" cssStyle="width:80%; font-size:150%;"/>
	</div>		
	<div id="rowOutput" class="eXtremeTable" style="margin-top:18px; width:100%; height:100px"></div>	
</fieldset>
<fieldset style="margin-left:5%; width:90%; height:50px; text-align:center; margin-top:10px;">
	<div style="width:60%; margin-left:auto; margin-right:auto; margin-top:10px; white-space:nowrap; font-size:150%"><s:text name="igeriv.totale.inventario"/>&nbsp;:&nbsp;<s:text name="constants.EURO_SIGN_DECIMAL"/>&nbsp;<span id="totaleInventario"></span></div> 
</fieldset>
<div style="width:100%; margin-top:50px; text-align:center; text-align:center;">
	<div style="width:50%; margin-left:auto; margin-right:auto;">	
		<div style="float:left; width:25%"><img name="add" id="add" src="/app_img/add_48x48.gif" alt="<s:text name="igeriv.crea.nuovo.inventario"/>" border="0" title="<s:text name="igeriv.crea.nuovo.inventario"/>" style="cursor:pointer" onclick="javascript:addInventario()"/></div>
		<div style="float:left; width:25%"><img name="delete" id="delete" src="/app_img/delete_48x48.png" alt="<s:text name="igeriv.cancella.inventario"/>" border="0" title="<s:text name="igeriv.cancella.inventario"/>" style="cursor:pointer" onclick="javascript:deleteInventario()"/></div>
		<div style="float:left; width:25%"><img name="pdf" id="pdf" src="/app_img/pdf_download_48.png" alt="<s:text name="igeriv.crea.pdf.inventario"/>" border="0" title="<s:text name="igeriv.crea.pdf.inventario"/>" style="cursor:pointer" onclick="javascript:reportInventario('<s:text name="constants.PDF"/>')"/></div>
		<div style="float:left; width:25%"><img name="xls" id="xls" src="/app_img/excel.gif" alt="<s:text name="igeriv.crea.excel.inventario"/>" border="0" title="<s:text name="igeriv.crea.excel.inventario"/>" style="cursor:pointer" onclick="javascript:reportInventario('<s:text name="constants.EXCEL"/>')"/></div>
	</div>
</div>
<div id="dialogContent" class="eXtremeTable popup_block" style="font-size:110%; z-index:99999; background-color:#cccccc">
	<div style="width:640px; height:520px; z-index:9">
		<table dojoType="dojox.grid.DataGrid" id="pubInventarioTable" 
			   jsid="idtn" class="extremeTableFields" 
			   style="font-size:100%; width:630px; text-align:left; margin-left:0px;" cellpadding="0px" cellspacing="0px">
			<thead> 
				<tr>
					<th class="tableHeader" field="titolo" width="20%" ><s:text name="igeriv.titolo"/></th>
					<th class="tableHeader" field="sottoTitolo" width="20%"><s:text name="igeriv.sottotitolo"/></th>
					<th class="tableHeader" field="numeroCopertina" width="10%" styles='text-align: center;'><s:text name="igeriv.numero"/></th>
					<th class="tableHeader" field="prezzoEdicolaFormat" width="10%" styles='text-align: right;'><s:text name="igeriv.prezzo.netto"/></th>
					<th class="tableHeader" field="prezzoCopertinaFormat" width="10%" styles='text-align: right;'><s:text name="igeriv.prezzo.lordo"/></th>
					<th class="tableHeader" field="dataUscitaFormat" width="14%" styles='text-align: center;'><s:text name="igeriv.data.uscita"/></th>
					<th class="tableHeader" field="giacenzaSP" width="8%" styles='text-align: center;'><s:text name="igeriv.giacienza"/></th>
					<th class="tableHeader" field="isContoDeposito" width="8%" styles='text-align: right;'><s:text name="igeriv.conto.deposito.iniziali"/></th>
				</tr>
			</thead>
		</table>
	</div>		
</div>
<s:form id="InventarioForm">
	<s:hidden name="idInventario" id="idInventario"/>
	<s:hidden name="tipo" id="tipo"/>
</s:form>