<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		setContentDivHeight(30);
	});

	$("#GestioneClientiTab_table tbody tr[divParam]").click(function() { 	
	    var popURL = $(this).attr('divParam'); 
	    var query= popURL.split('?'); 
	    var dim= query[1].split('&');	   	    	     	  		  
	    var idCliente = dim[0].split('=')[1];	 
	    var $td = $(this).find("td:nth-child(1)");
	    var $td2 = $(this).find("td:nth-child(2)");
	    var nomeCliente = $td.html().trim() + " " + $td2.html().trim();
	 	var url = "${pageContext.request.contextPath}/estrattoContoVenditeClientiEdicola_showReportEstratto.action?codCliente=" + idCliente + "&nomeCliente=" + escape(nomeCliente.replace('°','&deg;'));
		openDiv('popup_name', 620, 480, url);
		return false;
	});
</script>