<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		<s:if test="%{loginExecuted eq true && #session.alertSospensione != null}">
			jAlert('<s:property value="%{#session.alertSospensione}"/>', msgAvviso.toUpperCase());
		</s:if>
		<s:if test="%{loginExecuted eq true && #request.emailValido == false}">
			promptForEmail('${pageContext.request.contextPath}/<s:text name="actionName"/>');			
		</s:if>
		<s:if test="%{loginExecuted eq true && #request.daysLeft > 0 && #request.daysLeft <= 7}">
			var msg = <s:if test="authUser.edicolaStarter eq true">'<s:text name="msg.giorni.alla.fine.periodo.prova.starter"/>'</s:if><s:else>'<s:text name="msg.giorni.alla.sospensione.edicola.1"/>'</s:else>;
			jAlert(msg.replace('{0}','<s:text name="%{#request.daysLeft}"/>'), msgAvviso.toUpperCase());
		</s:if>
		/*
		<s:if test="loginExecuted eq true && authUser.codFiegDl eq constants.COD_FIEG_MENTA && authUser.fotoEdicolaInserita eq false">
			jConfirm('<s:text name="igeriv.confirm.inserisci.foto.anagrafica"/>', attenzioneMsg, function(r) {
			    if (r) { 
			    	<s:url action="anagraficaRivendita_show.action?showInsertImg=true" var="urlTag"/>
					window.location = '<s:property value="#urlTag" />';
			    }
			}, true, true);
		</s:if>
		*/
		
		/*
		var filePrivacy = '/pdf/PRIVACY_POLICY_'+'<s:text name="%{#request.codFiegDl}" />'+'.pdf';
		
		$.cookieBar({
			fixed: true,
			message: 'Utilizziamo i cookie per motivi esclusivamente tecnici ai fine di offrire un servizio piu\' affidabile e sicuro ',
			acceptText: 'Tutto chiaro!!!',
			acceptButton: true,
			declineButton: false,
			declineText: 'Disable Cookies',
			policyButton: true,
			policyText: 'Privacy Policy',
			policyURL: filePrivacy,
			autoEnable: true,
			acceptOnContinue: false,
			expireDays: 365,
			forceShow: false,
			effect: 'slide',
			element: 'body',
			append: false,
			bottom: false,
			zindex: '',
			redirect: '/',
			domain: 'www.example.com',
			referrer: 'www.example.com'
		}); 
		*/
	});
</script>