<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
		div#filter { height:35px; background:#cccccc; }
		div#page { height:450px; }
		div#content1 { height:420px; }
		div#footer { margin-top:0px }
</style>
<div style="width:100%; text-align:center"><span style="cursor:pointer" onclick="javascript: chiudiConto(false, false);"><img id="finalizza" src="/app_img/barcode_finalizza.png" alt="<s:text name="igeriv.barcode.chiudi.conto"/>" border="0" title="<s:text name="igeriv.barcode.chiudi.conto"/>"/></span></div>