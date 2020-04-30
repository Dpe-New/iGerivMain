<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:if
	test="authUser.tipoUtente == 1 && authUser.edicolaDeviettiTodis neq true && authUser.codFiegDl != #application['CDL_CODE'] && #request.sf neq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS">
	<div id="iconsDiv"
		style="text-align: center; white-space: nowrap; width: 300px;">
		<div style="float: left">
			<a
				href="<s:if test="authUser.codFiegDl eq constants.COD_FIEG_MENTA">http://www.igeriv.it/forum</s:if><s:else>/forum</s:else>"
				target="_new"><img id="forum"
				src="/app_img/forum_icon-small.gif"
				alt="<s:text name="igeriv.forum"/>" border="0"
				title="<s:text name="igeriv.accedi.forum"/>" /></a>
		</div>
		<div style="float: left; margin-left: 10px">
			<a href="http://igeriv.blogspot.com" target="_new"><img id="blog"
				src="/app_img/blog-icon-small.png"
				alt="<s:text name="igeriv.blog"/>" border="0"
				title="<s:text name="igeriv.accedi.blog"/>" /></a>
		</div>
		<div style="float: left; margin-left: 10px;" id="fb"
			title="<s:text name="igeriv.seguici.su.facebook"/>">
			<a href="http://www.facebook.com/iGeriv" target="_new"><img
				src="/app_img/facebook_32-small.png" border="0" /></a>
		</div>
		<div style="float: left; margin-left: 20px;" id="promemoria"
			title="<s:text name="igeriv.menu.83"/>">
			<img id="imgPromemoria" src="/app_img/promemoria.png" border="0"
				style="cursor: pointer; visibility: hidden"
				title="<s:text name="igeriv.menu.83"/>" />
		</div>
		<s:if
			test="campagnaInvitaColleghiAbilitata eq 1 && authUser.campagnaInvitaColleghiAbilitata eq true && authUser.isDlInforiv eq false && #session['hasProfiloEdicolaBaseNotTest'] eq true && authUser.edicolaStarter eq false">
			<div style="float: left; margin-left: 30px;" id="promo"
				title="<s:text name="igeriv.campagna.invita.un.collega"/>">
				<a href="${pageContext.request.contextPath}/cic_show.action"><img
					src="/app_img/promozioni.gif" border="0" /></a>
			</div>
		</s:if>
		
		<%-- 
		<s:if test="authUser.codFiegDl eq constants.COD_FIEG_MENTA">
			<s:url action="redirect_redirectToRtaeMenta.action"
				id="redirectRtaeMenta" />
			<div style="float: left; margin-left: 20px;">
				<a href="${redirectRtaeMenta}" target="_blank"><img id="rtaeImg"
					src="/app_img/rtae.png" alt="RTAE" border="0"
					title="Apri pagina RTAE" /></a>
			</div>
		</s:if>
		--%>
		
		<div id='dpeMessageDiv'
			style='float: left; margin-left: 20px; background: transparent'></div>
	</div>
</s:if>
<s:elseif
	test="authUser.tipoUtente == 1 && authUser.codFiegDl == #application['CDL_CODE']">
	<div id="iconsDiv" style="text-align: center; position: absolute;">
		<div style="float: right; margin-left: 10px">
			<a href="http://webeditori.cdlbo.it/ordini/" target="_new"><img
				id="store" src="/app_img/store_cdl.png" height="26px" width=120px
				" alt="<s:text name="igeriv.accedi.store"/>" border="0"
				title="<s:text name="igeriv.accedi.store"/>" /></a>
		</div>
		<div style="float: left; margin-left: 20px;" id="promemoria"
			title="<s:text name="igeriv.menu.83"/>">
			<img id="imgPromemoria" src="/app_img/promemoria.png" border="0"
				style="cursor: pointer; visibility: hidden"
				title="<s:text name="igeriv.menu.83"/>" />
		</div>
		<div style="float: left; margin-left: 20px;" id="marketing"
			title="<s:text name="igeriv.accedi.marketing"/>">
			<img id="imgMarketing" src="/app_img/marketing.png" border="0"
				style="cursor: pointer; visibility: hidden"
				title="<s:text name="igeriv.accedi.marketing"/>" />
		</div>
		<%-- RTAE CDL Febbraio 2017--%>
		<s:if test="authUser.rtaeAccessEnabled eq true"> 
			<s:url action="redirect_redirectToRtaeCDL.action" id="redirectRtaeCDL" />
			<div style="float: left; margin-left: 20px;">
				<a href="${redirectRtaeCDL}" target="_blank"><img id="rtaeImg"
					src="/app_img/rtae.png" alt="RTAE" border="0"
					title="Apri pagina RTAE" /></a>
			</div>
		</s:if>
		
		
	</div>
</s:elseif>

