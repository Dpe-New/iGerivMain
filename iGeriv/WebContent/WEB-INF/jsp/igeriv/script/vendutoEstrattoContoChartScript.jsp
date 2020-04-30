<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		var hasSuddivisioneQuotidianiPeriodiciReportVenduto = <s:if test="authUser.suddivisioneQuotidianiPeriodiciReportVenduto eq true">true</s:if><s:else>false</s:else>;
		var date = new Array();
	    var vendutoQuotidiani = new Array();
	    var vendutoPeriodici = new Array();
	    var venduto = new Array();
	    
	    if (hasSuddivisioneQuotidianiPeriodiciReportVenduto) {
		    var count = 0;
		    $("#EstrattoContoTab_table tbody tr td:nth-child(1)").not(':last').each(function() {
		    	date[count++] = $(this).text().trim();
		    });
		    count = 0;
		    $("#EstrattoContoTab_table tbody tr td:nth-child(4)").not(':last').each(function() {
		    	vendutoQuotidiani[count++] = ($(this).text().trim() == '') ? 0 : $(this).text().trim();
		    });
		    count = 0;
		    $("#EstrattoContoTab_table tbody tr td:nth-child(7)").not(':last').each(function() {
		    	vendutoPeriodici[count++] = ($(this).text().trim() == '') ? 0 : $(this).text().trim();
		    });
		    count = 0;
		    $("#EstrattoContoTab_table tbody tr td:nth-child(8)").not(':last').each(function() {
		    	venduto[count++] = ($(this).text().trim() == '') ? 0 : $(this).text().trim();
		    });
		    date = date.join('|');
		    vendutoQuotidiani = vendutoQuotidiani.join('|');
		    vendutoPeriodici = vendutoPeriodici.join('|');
		    venduto = venduto.join('|');
		    var url = "${pageContext.request.contextPath}/viewChart_getVendutoEstrattoContoChart.action?date=" + date + "&vendutoQuotidiani=" + vendutoQuotidiani + "&vendutoPeriodici=" + vendutoPeriodici + "&venduto=" + venduto + "&hasSuddivisioneQuotidianiPeriodiciReportVenduto=" + hasSuddivisioneQuotidianiPeriodiciReportVenduto;
	    } else {
	    	var count = 0;
		    $("#EstrattoContoTab_table tbody tr td:nth-child(1)").not(':last').each(function() {
		    	date[count++] = $(this).text().trim();
		    });
		    count = 0;
		    $("#EstrattoContoTab_table tbody tr td:nth-child(4)").not(':last').each(function() {
		    	venduto[count++] = ($(this).text().trim() == '') ? 0 : $(this).text().trim();
		    });
		    date = date.join('|');
		    vendutoQuotidiani = venduto.join('|');
		    vendutoPeriodici = venduto.join('|');
		    venduto = venduto.join('|');
		    var url = "${pageContext.request.contextPath}/viewChart_getVendutoEstrattoContoChart.action?date=" + date + "&vendutoQuotidiani=" + vendutoQuotidiani + "&vendutoPeriodici=" + vendutoPeriodici + "&venduto=" + venduto + "&hasSuddivisioneQuotidianiPeriodiciReportVenduto=" + hasSuddivisioneQuotidianiPeriodiciReportVenduto;
	    }
	    
	    
	    $("#vendutoChart").attr("src", url);
	    $("#vendutoChart").contextMenu({ menu: 'printImgMenu', yTop: 0, xLeft: 20 }, 
	    	function(action, el, pos) {
	    		if (action == "print") {
	    			if (getBrowser().indexOf("FIREFOX") == -1) {
	    				$('#vendutoChart').printElement({printMode:'popup'});
	    			} else {
	    				$('#vendutoChart').printElement();
	    			}
	    		}
	    });	
	    addFadeLayerEvents();
	});	
	
</script>