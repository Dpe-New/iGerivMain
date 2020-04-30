<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<link rel="shortcut icon"
	href="${pageContext.request.contextPath}/favicon.ico" />

<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/igeriv_combined_226_<s:text name="igeriv.version.timestamp"/>.css" /> --%>


<s:if test="authUser.codFiegDl eq constants.COD_FIEG_MENTA">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/igeriv_combined_<s:text name="constants.COD_FIEG_MENTA"/>_<s:text name="igeriv.version.timestamp"/>.css" />
</s:if>
<s:elseif test="authUser.codFiegDl == #application['CDL_CODE']">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/igeriv_combined_<s:text name="#application['CDL_CODE']"/>_<s:text name="igeriv.version.timestamp"/>.css" />
</s:elseif>
<s:else>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/igeriv_combined<s:if test="%{#request.sf != ''}"><s:text name="%{#request.sf}"/></s:if>_<s:text name="igeriv.version.timestamp"/>.css" />
</s:else>

<!-- <link rel="stylesheet" type="text/css" -->
<%-- 	href="${pageContext.request.contextPath}/css/igeriv_combined<s:if test="authUser.codFiegDl eq constants.COD_FIEG_MENTA">_<s:text name="constants.COD_FIEG_MENTA"/></s:if><s:if test="%{#request.sf != ''}"><s:text name="%{#request.sf}"/></s:if>_<s:text name="igeriv.version.timestamp"/>.css" /> --%>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/jquery-ui_<s:text name="igeriv.version.timestamp"/>.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/ui.theme_<s:text name="igeriv.version.timestamp"/>.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/rich_calendar_<s:text name="igeriv.version.timestamp"/>.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/jquery_cookiebar_<s:text name="igeriv.version.timestamp"/>.css" />

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/redmond/jquery-ui-1.8.18.custom.css" />
<style>
<!-- /* COLOR RED WEEK */
.ui-datepicker-week-end a {
	background-image: none;
	background-color: red;
}

.ui-datepicker-week-end a {
	color: red !important;
}
-->
</style>


<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script>
	function getBrowser() {
		return navigator.userAgent.toUpperCase();	
	}

	function loadjscssfile(filename, filetype) {
		if (filetype == "js") {
			var fileref = document.createElement('script');
			fileref.setAttribute("type", "text/javascript");
			fileref.setAttribute("src", filename);
		} else if (filetype == "css") {
			var fileref = document.createElement("link");
			fileref.setAttribute("rel", "stylesheet");
			fileref.setAttribute("type", "text/css");
			fileref.setAttribute("href", filename);
		}
		if (typeof fileref != "undefined")
			document.getElementsByTagName("head")[0].appendChild(fileref);
	}

	if (getBrowser().indexOf("MSIE") != -1) {
		loadjscssfile("${pageContext.request.contextPath}/css/igeriv_menu_ie7_<s:text name="igeriv.version.timestamp"/>.css", "css");
		loadjscssfile("${pageContext.request.contextPath}/css/igeriv_tbody_scroll_ie_<s:text name="igeriv.version.timestamp"/>.css", "css");
	} else {
		loadjscssfile("${pageContext.request.contextPath}/css/igeriv_menu_ie8_<s:text name="igeriv.version.timestamp"/>.css", "css");
		loadjscssfile("${pageContext.request.contextPath}/css/igeriv_tbody_scroll_ff_<s:text name="igeriv.version.timestamp"/>.css", "css");
	}
	
	var codPagina = 0;
	var style_suff = '<s:if test="%{#request.sf != ''}"><s:text name="%{#request.sf}"/></s:if>';
	var generalRpcService = null;
	var appContext = "${pageContext.request.contextPath}";
	var namespace = "${ap}";
	var chiudiMsg = "<s:text name='msg.chiudi'/>"; 
	var loadingMsg = "<s:text name='msg.loading'/>";
	var stileLabel = '<s:text name="igeriv.stile"/>';
	var carattereLabel = '<s:text name="igeriv.carattere"/>';
	var dimensioneLabel = '<s:text name="igeriv.dimensione"/>';
	var avvisoMsg = '<s:text name="msg.avviso"/>';
	var confermaMsg = '<s:text name="conferma"/>';
	var confermaProdottiDigitali = '<s:text name="conferma.prodotti.digitali"/>';
	var richiestaInfoMsg = '<s:text name="dpe.contact.form.reason1"/>'; 
	var allowBeep = "<s:text name='#session.mapParamsEdicola.paramEdicola1.paramValue'/>"; 
	var allowNetworkDetection = "<s:text name='#session.mapParamsEdicola.paramEdicola4.paramValue'/>";
	var allowSpuntaBolla = "<s:text name='#session.mapParamsEdicola.paramEdicola7.paramValue'/>";
	var campiBollaInvalidiError = "<s:text name='igeriv.campi.bolla.invalidi.error'/>";
	var si = "<s:text name='igeriv.si'/>";
	var no = "<s:text name='igeriv.no'/>";
	var annulla = "<s:text name='plg.annulla'/>";
	var memorizza = "<s:text name='igeriv.memorizza'/>";
	var esci = "<s:text name='igeriv.esci'/>";
	var uploadImmagineTitle = "<s:text name='igeriv.upload.immagine.title'/>";
	var uploadImmagineMsg = "<s:text name='igeriv.upload.immagine.msg'/>";
	var BOLLA_CONSEGNA = 1;
	var BOLLA_RESA = 2;
	var tipoBolla = <s:if test="actionName.contains('bollaRivendita')">BOLLA_CONSEGNA;</s:if><s:elseif test="actionName.contains('bollaResa')">BOLLA_RESA;</s:elseif><s:else>0;</s:else>
	var showImagesInWinPopup = <s:if test="%{#session.mapParamsEdicola.paramEdicola25 != null && #session.mapParamsEdicola.paramEdicola25.paramValue == 'true'}">true</s:if><s:else>false</s:else>;
</script>