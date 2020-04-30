<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {	
		
	});
	
	function doSubmit() { 
		$("#LibriScolasticiVenditaForm").submit();
	};
	function doSubmitSave() {
		//libriScolasticiVendita_showFilter.action
		spuntaSave();
	};
	function doSubmitReport() { 
		spuntaReport();
	};
	function doRicercaCliente() { 
		ricercaCliente();
	};
	
</script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js-transient/ordiniCliente-min_<s:text name="igeriv.version.timestamp"/>.js"></script>