<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	var prodErrMsg = '<s:actionmessage />';
	var actionErrors = '<s:actionerror />';
	var confirmDeleteCategoriaMsg = '<s:text name="msg.confirm.delete.categoria" />';
	var confirmDeleteSottoCategoriaMsg = '<s:text name="msg.confirm.delete.sottocategoria" />';
	var prodottoNonModificabileMsg = '<s:text name="msg.prodotto.non.modificabile" />';
	var categoriaNonModificabileMsg = '<s:text name="msg.categoria.non.modificabile" />';
	var sottocategoriaNonModificabileMsg = '<s:text name="msg.sottocategoria.non.modificabile" />';
	var msgMieiProdotti = '<s:text name="igeriv.msg.tooltip.miei.prodotti"/>';
	var msgProdottiDisponibili = '<s:text name="igeriv.msg.tooltip.prodotti.disponibili"/>';
	var msgCancellareProdotto = '<s:text name="gp.cancellare.prodotto"/>'; 
	var msgCancellareCategoria = '<s:text name="gp.cancellare.categoria"/>';
	var msgCancellareSottocategoria = '<s:text name="gp.cancellare.sottocategoria"/>';
	var msgProdottoEsclusoVendite = '<s:text name="igeriv.prodotto.escluso..dalle.vendite"/>';
	var conditionHasActionMessages = <s:if test="hasActionMessages()">true</s:if><s:else>false</s:else>;
	var conditionHasActionErrors = <s:if test="hasActionErrors()">true</s:if><s:else>false</s:else>;
	var lastProdotto = '<s:property value="lastProdotto"/>';
	var prevProdotto = '<s:property value="prevProdotto"/>';
	var lastSubcategory = '<s:property value="lastSubcategory"/>';
	var isProdottiEdicolaEmpty = <s:if test="listProdottiEdicola.get(0).isEmpty()">true</s:if><s:else>false</s:else>;
	var appContext = "${pageContext.request.contextPath}"; 
</script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/prodottoEdicolaTemplate-min_<s:text name="igeriv.version.timestamp"/>.js"></script>