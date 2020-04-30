<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		
	});
	
	function nuovoProdottoSuccessCallback() {
		var prodId = $("#prodottoEdicola\\.codProdottoInterno", window.parent.document).val();
		var codCategoria = $("#codCategoria", window.parent.document).val();
		var codSottoCategoria = $("#codSottoCategoria", window.parent.document).val();
		$('#popup_name').hide(); 
		var prodUrl = '';
		if ((typeof(prodId) != 'undefined') && prodId != '') {
			prodUrl = '?lastProdotto=folder_2_' + prodId;
		} else if ((typeof(codCategoria) != 'undefined') && codCategoria != '' && (typeof(codSottoCategoria) != 'undefined') && codSottoCategoria != '') {
			prodUrl = '?lastSubcategory=sucategory_' + codCategoria + '_' + codSottoCategoria;
		}
		document.getElementById('prodottiEdicolaFrame').contentDocument.location.href = '${pageContext.request.contextPath}/pne_showProdotti.action' + prodUrl;	
	}
	
</script>
