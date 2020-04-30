<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		<s:if test="%{#session.alertSospensione != null}">
			jAlert('<s:property value="%{#session.alertSospensione}"/>', msgAvviso.toUpperCase());
		</s:if>
		<s:if test="%{#request.emailValido == false}">
			promptForEmail('${pageContext.request.contextPath}/<s:text name="actionName"/>');			
		</s:if>
		<s:if test="%{#request.daysLeft > 0 && #request.daysLeft <= 7}">
			var msg = <s:if test="authUser.edicolaStarter eq true">'<s:text name="msg.giorni.alla.fine.periodo.prova.starter"/>'</s:if><s:else>'<s:text name="msg.giorni.alla.sospensione.edicola.1"/>'</s:else>;
			jAlert(msg.replace('{0}','<s:text name="%{#request.daysLeft}"/>'), msgAvviso.toUpperCase());
		</s:if>
		<s:if test="%{#session.showMsgScaricoDati eq true}">
			jAlert('<s:text name="igeriv.dl.msg.scarico.dati.avviso"/>', msgAvviso.toUpperCase());
		</s:if>
		
		$.cookieBar({
			fixed: true,
			message: 'Utilizziamo i cookie per motivi esclusivamente tecnici ai fine di offrire un servizio piu\' affidabile e sicuro ',
			acceptText: 'Tutto chiaro!!!',
			acceptButton: true,
			declineButton: false,
			declineText: 'Disable Cookies',
			policyButton: true,
			policyText: 'Privacy Policy',
			policyURL: '/pdf/LA_PRIVACY_POLICY_DI_QUESTO_SITO.pdf',
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
		
		
	});
	
	/*
	* TEST PER ESPORTAZIONE FILE INFORIV
	*/
	function exportInforiv() {
		dojo.xhrGet({
			url: '${pageContext.request.contextPath}/pubblicazioniRpc_exportInforiv.action',			
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
			preventCache: true,
			handle: function(data,args) {
				alert(data);
			}	
		});
	}
</script>