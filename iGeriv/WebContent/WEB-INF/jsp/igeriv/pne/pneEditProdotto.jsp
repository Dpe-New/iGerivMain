<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="templateSuffix" value="'ftl'" />
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<s:form id="PNEEditProdottoForm" action="pne_saveProdottoEdicola.action"
	enctype="multipart/form-data" theme="igeriv" method="POST"
	validate="false">
	<div id="mainDiv">
		<fieldset class="filterBolla" style="text-align: left; width: 100%">
			<legend style="font-size: 100%">
				<s:text name="tableTitle" />
			</legend>
			<div style="float: left;" class="rowDiv">
				<div style="float: left; width: 150px">
					<s:text name="igeriv.descrizione" />
				</div>
				<div style="float: left; color: black">
					<s:textfield label="" name="prodottoEdicola.descrizioneProdottoA"
						id="prodottoEdicola.descrizioneProdottoA" cssStyle="width: 400px"
						cssClass="tableFields" />
				</div>
			</div>
			<div style="float: left;" class="rowDiv">
				<div style="float: left; width: 150px">
					<s:text name="igeriv.descrizione.2" />
				</div>
				<div style="float: left; color: black">
					<s:textfield label="" name="prodottoEdicola.descrizioneProdottoB"
						id="prodottoEdicola.descrizioneProdottoB" cssStyle="width: 400px"
						cssClass="tableFields" />
				</div>
			</div>
			<div style="float: left;" class="rowDiv">
				<div style="float: left; width: 150px">
					<s:text name="igeriv.categoria" />
				</div>
				<div style="float: left; color: black">
					<s:select label="" name="prodottoEdicola.codCategoria"
						id="codCategoria" listKey="codCategoria"
						listValue="descrizioneNoHtml" list="categorie" emptyOption="false"
						cssStyle="width: 400px" />
				</div>
			</div>
			<div style="float: left;" class="rowDiv">
				<div style="float: left; width: 150px">
					<s:text name="igeriv.sotto.categoria" />
				</div>
				<div style="float: left; color: black">
					<select name="sottoCategoria" id="sottoCategoria"
						style="width: 400px">
					</select>
				</div>
			</div>
			<div style="float: left;" class="rowDiv">
				<div style="float: left; width: 150px">
					<s:text name="igeriv.codice.barre" />
				</div>
				<div style="float: left; color: black">
					<s:textfield label="" name="prodottoEdicola.barcode"
						id="prodottoEdicola.barcode" cssStyle="width: 400px"
						cssClass="tableFields" />
				</div>
			</div>
			<s:if
				test="prodottoEdicola.categoria == null || (prodottoEdicola.categoria.descrizione != #request.prodottiReparto)">
				<div style="float: left;" class="rowDiv">
					<div style="float: left; width: 150px; margin-top: 30px">
						<s:text name="igeriv.prezzo.vendita.iva.inclusa" />
					</div>
					<div style="float: left; color: black" id="prezziDiv">
						<dpe:table tableId="PrezziTab" items="prezzi" var="prezzi"
							action="${pageContext.request.contextPath}${ap}/pne_saveProdottoEdicola.action"
							imagePath="/app_img/table/*.gif" rowsDisplayed="1000"
							view="noButtons" locale="${localeString}"
							state="it.dpe.igeriv.web.actions.ProdottiNonEditorialiAction"
							styleClass="extremeTableFields" style="width:400px"
							form="PNEEditProdottoForm"
							theme="eXtremeTable bollaScrollDivVerySmall"
							showPagination="false" id="PrezziDiv" footerStyle="height:0px;"
							filterable="false">
							<ec:row style="height:20px">
								<dpe:column property="prezzoLisitino" alias="prezziSelected"
									width="40%" cell="currency" hasCurrencySign="true"
									title="label.print.Table.Price" filterable="false"
									styleClass="extremeTableFields" sortable="false"
									style="text-align:right" headerStyle="text-align:right" />
								<dpe:column property="dataValiditaIniziale"
									alias="validitaSelected" width="40%" cell="date"
									dateFormat="dd/MM/yyyy" title="igeriv.valido.da"
									filterable="false" styleClass="extremeTableFields"
									sortable="false" style="text-align:center"
									headerStyle="text-align:center" />
								<ec:column property="fake" width="20%" title=" "
									filterable="false" sortable="false" />
							</ec:row>
						</dpe:table>
					</div>
				</div>
			</s:if>
			<div style="float: left;" class="rowDiv">
				<div style="float: left; width: 150px">
					<s:text name="igeriv.aliquota.iva" />
				</div>
				<div style="float: left; color: black; width: 400px">
					<s:select label="" name="prodottoEdicola.aliquota" id="aliquotaIva"
						listKey="aliquota" listValue="aliquota"
						list="#application.listAliquoteIva" emptyOption="false"
						cssStyle="width: 60px" />
				</div>
			</div>
			<div style="float: left;" class="rowDiv">
				<div style="float: left; width: 150px">
					<s:text name="igeriv.causale.iva" />
				</div>
				<div style="float: left; color: black">
					<select name="causaleIva" id="causaleIva" style="width: 400px"></select>
				</div>
			</div>
			<div style="float: left;" class="rowDiv">
				<div style="float: left; width: 150px;">
					<s:text name="igeriv.escludi.prodotto.dalle.vendite" />
				</div>
				<div style="float: left; color: black; width: 400px;">
					<s:checkbox name="prodottoEdicola.escludiDalleVendite"
						id="escludiVendite" />
				</div>
			</div>
			<div style="float: left;" class="rowDiv">
				<div style="float: left; width: 150px; margin-top: 15px">
					<s:text name="plg.immagine" />
				</div>
				<div style="float: left; color: black; margin-top: 15px">
					<s:file id="attachment1" name="attachment1" label="File" />
				</div>
				<div style="float: left; margin-left: 50px">
					<img id="imgProd"
						title="<s:text name="prodottoEdicola.descrizioneProdottoA"/>"
						src="/immagini_miniature_edicola_prodotti_vari/<s:text name="prodottoEdicola.nomeImmagine"/>"
						border="0" width="60" height="60" />
				</div>
			</div>
		</fieldset>
		<div
			style="text-align: center; margin-left: auto; margin-right: auto; width: 200px;"
			class="rowDiv">
			<div style="width: 200px; margin-top: 0px;">
				<input type="button" name="igeriv.memorizza" id="memEditProdotto"
					value="<s:text name='igeriv.memorizza'/>" align="center"
					style="text-align: center; width: 100px"
					onclick="javascript: if (doValidationProdotti($('#newProduct').val())){ray.ajax(); return (formSubmitMultipartAjax('PNEEditProdottoForm','html', function(){nuovoProdottoSuccessCallback()}, null, false));}" />
			</div>
		</div>
	</div>
	<s:hidden name="prodottoEdicola.edicola.codEdicola" />
	<s:hidden name="prodottoEdicola.codProdottoInterno"		id="prodottoEdicola.codProdottoInterno" />
	<s:hidden name="prodottoEdicola.nomeImmagine" id="nomeImmagine" />
	<s:hidden name="prodottoEdicola.codSottoCategoria"	id="codSottoCategoria" />
	<s:hidden name="prodottoEdicola.posizione" id="posizione" />
	<s:hidden name="prezziSelected" id="prezziSelected" />
	<s:hidden name="validitaSelected" id="validitaSelected" />
	<s:hidden name="descrizioneProdottoAOriginale" />
	<s:hidden name="newProduct" id="newProduct" />
	<s:hidden name="prodottoEdicola.prodottoDl" id="prodottoDl" />
	
	<input type="hidden" name="prodottoDigitale" id="prodottoDigitale" value="<s:property value="prodottoEdicola.isProdottoDigitale"/>"  />
	<input type="hidden" id="causaleIvaHidden"	value="<s:property value="prodottoEdicola.causaleIva.pk"/>" />
</s:form>
