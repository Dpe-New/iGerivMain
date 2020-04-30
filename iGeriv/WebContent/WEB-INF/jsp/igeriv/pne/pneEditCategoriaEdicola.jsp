<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:form id="PNEEditCategoriaForm"
	action="pne_saveCategoriaEdicola.action" enctype="multipart/form-data"
	theme="igeriv" method="POST" validate="false" onsubmit="return false">
	<div id="mainDiv">
		<fieldset class="filterBolla" style="text-align: left; width: 100%">
			<legend style="font-size: 100%">
				<s:text name="tableTitle" />
			</legend>
			<div class="tableFields" style="float: left;">
				<div style="float: left; width: 150px">
					<s:text name="igeriv.descrizione" />
				</div>
				<div style="float: left; color: black">
					<s:textfield label="" name="categoria.descrizione" id="descrizione"
						cssStyle="width: 400px" cssClass="tableFields" />
				</div>
			</div>
			<div class="tableFields" style="float: left;">
				<div style="float: left; width: 150px; margin-top: 15px">
					<s:text name="plg.immagine" />
				</div>
				<div style="float: left; color: black; margin-top: 15px">
					<s:file name="attachment1" label="File" />
				</div>
				<div style="float: left; margin-left: 50px">
					<img id="imgProd" title="<s:text name="categoria.descrizione"/>"
						src="/immagini_miniature_edicola_prodotti_vari/<s:text name="categoria.immagine"/>"
						border="0" />
				</div>
			</div>
		</fieldset>
		<div
			style="text-align: center; margin-left: auto; margin-right: auto; width: 200px; margin-top: 20px">
			<div style="width: 200px; margin-top: 0px;">
				<input type="button" name="igeriv.memorizza" id="mem"
					value="<s:text name='igeriv.memorizza'/>" align="center"
					class="tableFields" style="text-align: center; width: 100px"
					onclick="javascript: if (doValidationCategoria()) {ray.ajax(); return (formSubmitMultipartAjax('PNEEditCategoriaForm','html', function(){nuovoProdottoSuccessCallback()}, null, false));};" />
			</div>
		</div>
	</div>
	<s:hidden name="categoria.pk.codCategoria" id="codCategoria" />
</s:form>
