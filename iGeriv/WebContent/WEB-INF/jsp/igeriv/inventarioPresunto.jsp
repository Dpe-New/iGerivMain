<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv">
	<s:if test="listInventarioDto != null && !listInventarioDto.isEmpty()">
		<s:form id="InventarioPresuntoForm"
			action="inventariop_showInventario.action" method="POST"
			theme="simple" validate="false" onsubmit="return (ray.ajax())">
			<div style="width: 100%">
				<dpe:table tableId="InventarioPresuntoTab" items="listInventarioDto"
					var="listInventarioDto" action="inventariop_showInventario.action"
					imagePath="/app_img/table/*.gif"
					style="table-layout:auto; height: 350px" view="buttonsOnBottom"
					locale="${localeString}"
					state="it.dpe.igeriv.web.actions.InventarioPresuntoAction"
					styleClass="extremeTableFields" form="InventarioPresuntoForm"
					theme="eXtremeTable bollaScrollDiv" showPagination="false"
					id="InventarioPresuntoFormScrollDiv" toolbarClass="eXtremeTable"
					showStatusBar="false" showTitle="false" showTooltips="false"
					footerStyle="height:30px;" filterable="false" showExports="true">
					<dpe:exportPdf
						fileName="inventario_presunto_${data_inventario}.pdf"
						tooltip="plg.esporta.pdf" headerTitle="${title}"
						headerColor="black" headerBackgroundColor="#b6c2da"
						isLandscape="true" />
					<ec:exportXls fileName="inventario_presunto_${data_inventario}.xls"
						tooltip="plg.esporta.excel" />
					<dpe:row>
						<ec:column property="cpu" width="4%"
							title="igeriv.codice.pubblicazione.title" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<ec:column property="titolo" width="22%" title="igeriv.titolo"
							filterable="false" />
						<ec:column property="sottoTitolo" width="16%"
							title="igeriv.sottotitolo" filterable="false" />
						<ec:column property="numeroCopertina" width="6%"
							title="igeriv.numero" filterable="false"
							style="text-align:center" />
						<dpe:column property="dataUscita" width="8%"
							title="igeriv.data.uscita" cell="date" dateFormat="dd/MM/yyyy"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" />
						<dpe:column property="quantitaCopieContoDeposito" width="8%"
							title="igeriv.qta.conto.deposito" calc="total"
							calcTitle="column.calc.total" filterable="false"
							style="text-align:center" headerStyle="text-align:center"
							totalCellStyle="text-align:center" />
						<ec:column property="prezzoCopertina" width="8%"
							title="igeriv.prezzo.lordo" cell="currency" filterable="false"
							style="text-align:right" headerStyle="text-align:right" />
						<ec:column property="prezzoEdicola" width="8%" format="###,###,##0.0000" 
							title="igeriv.prezzo.netto" cell="currency" filterable="false"
							style="text-align:right" headerStyle="text-align:right" />
						<dpe:column property="giacenzaSP" width="8%"
							title="igeriv.giacienza.ext" calc="total" filterable="false"
							style="text-align:center" headerStyle="text-align:center"
							totalCellStyle="text-align:center" />
						<dpe:column property="importo" width="8%" title="igeriv.importo"
							cell="currency" calc="total" format="###,###,##0.00"
							totalFormat="###,###,##0.00" filterable="false"
							style="text-align:right" headerStyle="text-align:right"
							totalCellStyle="text-align:right" />
						<ec:column property="nomeImmagine" title="igeriv.img"
							filterable="false" viewsDenied="pdf,xls" width="3%"
							sortable="false"
							cell="viewImageCell" >
<%-- 							<a href="/immagini/${pageScope.listInventarioDto.nomeImmagine}" --%>
<!-- 								rel="thumbnail"><img src="/app_img/camera.gif" width="15px" -->
<!-- 								height="15px" border="0" style="border-style: none" /></a> -->
						</ec:column>
					</dpe:row>
				</dpe:table>
			</div>
		</s:form>
	</s:if>
	<s:else>
		<div class="tableFields"
			style="width: 100%; align: center; text-align: center; margin-top: 50px">
			<s:property value="nessunRisultato" />
		</div>
	</s:else>
</div>