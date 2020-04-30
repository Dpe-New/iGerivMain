<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:if test="authUser.tipoUtente == 1 && authUser.codFiegDl != #session['CDL_CODE']">
	<div id="iconsDiv" style="text-align:center; white-space:nowrap; width:220px;">
		<div style="float:left"><a href="/forum" target="_new"><img id="forum" src="/app_img/forum_icon-small.gif" alt="<s:text name="igeriv.forum"/>" border="0" title="<s:text name="igeriv.accedi.forum"/>"/></a></div>
		<div style="float:left; margin-left:10px"><a href="http://igeriv.blogspot.com" target="_new"><img id="blog" src="/app_img/blog-icon-small.png" alt="<s:text name="igeriv.blog"/>" border="0" title="<s:text name="igeriv.accedi.blog"/>"/></a></div>
		<div style="float:left; margin-left:10px;" id="fb" title="<s:text name="igeriv.seguici.su.facebook"/>"><a href="http://www.facebook.com/iGeriv" target="_new"><img src="/app_img/facebook_32-small.png" border="0"/></a></div>
		<s:if test="campagnaInvitaColleghiAbilitata eq 1 && authUser.isDlInforiv eq false && #session['hasProfiloEdicolaBaseNotTest'] eq true && authUser.edicolaStarter eq false">
			<div style="float:left; margin-left:30px;" id="promo" title="<s:text name="igeriv.campagna.invita.un.collega"/>"><a href="${pageContext.request.contextPath}/cic_show.action"><img src="/app_img/promozioni.gif" border="0"/></a></div>
		</s:if>
	</div>
	<div id='dpeMessageDiv' style='float:left; margin-left:10px; background:transparent'></div>
</s:if>
