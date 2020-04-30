<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.ritiriCanc != null && !#request.ritiriCanc.isEmpty()}">
	<s:form id="RitiriCancForm" action="ritiri_showRitiriCanc.action"
		method="POST" theme="simple" validate="false"
		onsubmit="return (ray.ajax())">
		<div id="mainDiv" style="width: 100%">
			<dpe:table tableId="RitiriCancTab" items="ritiriCanc"
				var="ritiriCanc" action="ritiri_show.action"
				imagePath="/app_img/table/*.gif"
				style="table-layout:auto; height: 350px" view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.RitiriAction"
				styleClass="extremeTableFields" form="RitiriCancForm"
				theme="eXtremeTable bollaScrollDiv" showPagination="false"
				id="RitiriScrollDiv" toolbarClass="eXtremeTable"
				showStatusBar="false" showTitle="false" showTooltips="false"
				footerStyle="height:30px;" filterable="false" showExports="true">
				<dpe:row>
					<dpe:column property="fake" width="3%" title=" "
						cell="recuperaRitiriClienteSpunta" pkName="pk"
						hasHiddenPkField="true" style="text-align:center"
						filterable="false" sortable="false" viewsDenied="pdf,xls" />
					<ec:column property="dataVendita" width="10%"
						title="igeriv.data.ritiro" cell="date"
						format="dd/MM/yyyy HH:mm:ss" filterable="false" sortable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="tipoRitiro" width="10%"
						title="igeriv.tipo.ritiro" filterable="false" sortable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<dpe:column property="dataECFattura" width="10%"
						title="igeriv.data.estratto.conto.o.fattura" cell="date"
						dateFormat="dd/MM/yyyy" filterable="false" sortable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="titolo" width="12%" title="igeriv.titolo"
						filterable="false" sortable="false" />
					<ec:column property="sottoTitolo" width="12%"
						title="igeriv.sottotitolo" filterable="false" sortable="false" />
					<ec:column property="numeroCopertina" width="10%"
						title="igeriv.numero" filterable="false" sortable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<dpe:column property="prezzoCopertina" width="10%"
						title="igeriv.prezzo.lordo" hasCurrencySign="true"
						format="###,###,##0.00" cell="currency" filterable="false"
						sortable="false" style="text-align:right" />
					<dpe:column property="quantita" width="10%" title="igeriv.quantita"
						filterable="false" sortable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<dpe:column property="importoTotale" width="9%"
						title="igeriv.importo" calc="total" calcTitle="column.calc.total"
						totalCellStyle="text-align:right" hasCurrencySign="true"
						format="###,###,##0.00" totalFormat="###,###,##0.00"
						cell="currency" filterable="false" sortable="false"
						style="text-align:right" />
					<dpe:column property="immagine" title="igeriv.img"
						filterable="false" viewsDenied="pdf,xls" width="3%"
						sortable="false" style="text-align:center"
						headerStyle="text-align:center"
						cell="viewImageCell" >
<%-- 						<a href="/${pageScope.ritiriCanc.dirAlias}/${pageScope.ritiriCanc.immagine}" --%>
<!-- 							rel="thumbnail"><img src="/app_img/camera.gif" width="15px" -->
<!-- 							height="15px" border="0" style="border-style: none" /></a> -->
					</dpe:column>
				</dpe:row>
			</dpe:table>
		</div>
		<div style="text-align: center; margin-top: 15px;">
			<input type="button" value="<s:text name='igeriv.ripristina'/>"
				name="butRipristinaRitiri" id="butRipristinaRitiri"
				class="tableFields" style="width: 200px; text-align: center;"
				onclick="javascript: restoreRitiri();" />
		</div>
	</s:form>
</s:if>