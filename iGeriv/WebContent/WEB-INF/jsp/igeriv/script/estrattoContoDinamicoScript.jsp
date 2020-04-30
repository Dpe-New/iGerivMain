<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		var $tab = $("#EstrattoContoDinamicoTab_table tbody");
		var $row = $tab.find("tr.calcTitleRed").first();
		var $rowHtml = "<tr class=\"calcTitleRed\" style=\"height:25px\">" + $row.html() + "</tr>";
		$row.remove();
		$($rowHtml).insertAfter($tab.find("tr.calcRow").first());
	});
</script>