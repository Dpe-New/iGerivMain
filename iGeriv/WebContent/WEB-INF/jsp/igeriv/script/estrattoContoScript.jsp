<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		setContentDivHeight(20);
		<s:if test="%{#request.tipo eq 1 && #request != null}">
			if ($("#pdfEC").length > 0) {
				$("#pdfEC").click(function() {
					window.open('edImg.action?fileName=<s:text name="#request.nomeFile"/>&type=<s:text name="constants.COD_TIPO_IMMAGINE_PDF_ESTRATTO_CONTO"/>', '_blank');
				}).tooltip({ 
					delay: 0,  
				    showURL: false
				});
			}
		</s:if>
	});
</script>