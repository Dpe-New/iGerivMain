<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		addFadeLayerEvents();
		thumbnailviewer.init();
		$("#InfoPubblicazioniTab_table tbody tr td:nth-child(7)").each(function() {	
			var $row = $(this).closest("tr");
			var $iv = $row.attr('iv');
			if ($iv && $iv == 2) {
                str = '<span style="float:left; width:50px; text-align:center"><img src="/app_img/conto_deposito.gif" border="0px" style="border-style: none" title="<s:text name="igeriv.conto.deposito"/>" alt="<s:text name="igeriv.conto.deposito"/>"" />';
                $(this).html(str);
                $(this).find("img").tooltip({
                    delay: 0,
                    showURL: false
                });
            }
		});
	});
</script>