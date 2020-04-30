<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/igeriv_combined<s:if test="%{#request.sf != ''}"><s:text name="%{#request.sf}"/></s:if>_<s:text name="igeriv.version.timestamp"/>.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/jquery-ui_<s:text name="igeriv.version.timestamp"/>.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/jquery_cookiebar_<s:text name="igeriv.version.timestamp"/>.css" />
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
	} else if (getBrowser().indexOf("FIREFOX") != -1) {
		loadjscssfile("${pageContext.request.contextPath}/css/igeriv_menu_ie8_<s:text name="igeriv.version.timestamp"/>.css", "css");
		loadjscssfile("${pageContext.request.contextPath}/css/igeriv_tbody_scroll_ff_<s:text name="igeriv.version.timestamp"/>.css", "css");
	} else if (getBrowser().indexOf("CHROME") != -1) {
		loadjscssfile("${pageContext.request.contextPath}/css/igeriv_menu_ie7_<s:text name="igeriv.version.timestamp"/>.css", "css");
		loadjscssfile("${pageContext.request.contextPath}/css/igeriv_tbody_scroll_ie_<s:text name="igeriv.version.timestamp"/>.css", "css");
	} else {
		loadjscssfile("${pageContext.request.contextPath}/css/igeriv_menu_ie7_<s:text name="igeriv.version.timestamp"/>.css", "css");
		loadjscssfile("${pageContext.request.contextPath}/css/igeriv_tbody_scroll_ie_<s:text name="igeriv.version.timestamp"/>.css", "css");
	}

	var style_suff = '<s:if test="%{#request.sf != ''}"><s:text name="%{#request.sf}"/></s:if>';
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
	var attenzioneMsg = '<s:text name="msg.alert.attenzione"/>';
	var infoCopertina = '<s:text name="msg.info.copertina"/>';
	
	var allowBeep = "<s:text name='#session.mapParamsEdicola.paramEdicola1.paramValue'/>";
	var allowNetworkDetection = "<s:text name='#session.mapParamsEdicola.paramEdicola4.paramValue'/>";
	var allowSpuntaBolla = "<s:text name='#session.mapParamsEdicola.paramEdicola7.paramValue'/>";
	var si = "<s:text name='igeriv.si'/>";
	var no = "<s:text name='igeriv.no'/>";
	var annulla = "<s:text name='plg.annulla'/>";
	var esci = "<s:text name='igeriv.esci'/>";
</script>