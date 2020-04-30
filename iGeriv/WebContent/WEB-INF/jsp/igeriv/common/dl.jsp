<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<div id="dlDiv" style="width: 220px; float: left; white-space: nowrap;">
	<div id="dlImgDiv"
		style="display: inline; width: 120px; text-align: left; height: 50px;">
		<s:if test="authUser.tipoUtente == 1 || authUser.tipoUtente == 3">
			<div style="float: left; width: 120px">
				<s:if
					test="%{#request.sf neq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS}">
					<s:if test="authUser.imgLogo != null && authUser.imgLogo != ''">
						<s:if test="authUser.urlDL != null && authUser.urlDL != ''">
							<a href="<s:text name="authUser.urlDL"/>" target="_new">
						</s:if>
						<img id="dlDivImg"
							src="/app_img/<s:property value="authUser.imgLogo"/>"
							style="vertical-align: top;"
							alt="<s:property value="authUser.ragioneSocialeDl" />" border="0"
							title="<s:property value="authUser.ragioneSocialeDl" /> - <s:property value="authUser.indirizzoAgenziaPrimaRiga" /> - <s:property value="authUser.localitaAgenziaPrimaRiga" />" />
						<s:if test="authUser.urlDL != null && authUser.urlDL != ''">
							</a>
						</s:if>
					</s:if>
				</s:if>
			</div>
			<div style="float: left; width: 60px">
				<s:if
					test="authUser.multiDl eq true && authUser.dlInforiv eq true && (actionName != null 
				 	&& !actionName.contains('bollaResa_') 
					&& !actionName.contains('vendite_') 
					&& !actionName.contains('infoPubblicazioni_')
					&& !actionName.contains('statisticaPubblicazioni_')
					&& !actionName.contains('rifornimenti_')
					&& !actionName.contains('viewRifornimenti_')
					&& !actionName.contains('variazioni_')
					&& !actionName.contains('viewVariazioni_')
					&& !actionName.contains('reportVendite_')
					&& !actionName.contains('gestioneClienti_')
					&& !actionName.contains('prenotazioneClienti_')
					&& !actionName.contains('configurazioni_')
					&& !actionName.contains('gestioneUtenti_')
					&& !actionName.contains('pne')
					&& !actionName.contains('messages_')
					&& !actionName.contains('venduto_')
					&& !actionName.contains('params_'))">
					<div
						style="float: left; width: 15%; text-align: center; margin-left: auto; margin-right: auto;">
						<s:if
							test="actionName != null && actionName.equals('sonoInoltreUscite_showBollaSonoInoltreUscite.action')">
							<s:form id="dlAction"
								action="bollaRivendita_showFilterSonoInoltreUscite.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:if>
						<s:elseif
							test="actionName != null && actionName.equals('bollaResa_showBollaResa.action')">
							<s:form id="dlAction" action="bollaResa_showFilter.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.contains('viewBollaResa_')">
							<s:form id="dlAction" action="viewBollaResa_showFilter.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.equals('estrattoConto_showEstratto.action')">
							<s:form id="dlAction" action="estrattoConto_showFilter.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.equals('infoPubblicazioni_reportContoDeposito.action')">
							<s:form id="dlAction"
								action="infoPubblicazioni_reportContoDepositoFilter.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.equals('infoPubblicazioni_showPubblicazioni.action')">
							<s:form id="dlAction"
								action="infoPubblicazioni_showFilter.action" method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.equals('statisticaPubblicazioni_showPubblicazioniStatistica.action')">
							<s:form id="dlAction"
								action="statisticaPubblicazioni_showFilterStatistica.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.equals('bollaResa_showQuadraturaResa.action')">
							<s:form id="dlAction"
								action="bollaResa_showQuadraturaResaFilter.action" method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.equals('mancanzeBolla_showMancanzeBolla.action')">
							<s:form id="dlAction"
								action="mancanzeBolla_showMancanzeBollaFilter.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.equals('emailInviati_showEmailInviati.action')">
							<s:form id="dlAction"
								action="emailInviati_showEmailInviatiFilter.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:else>
							<s:form id="dlAction" action="%{actionName}" method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:else>
					</div>
				</s:if>
				<s:elseif
					test="authUser.multiDl eq true 
					&& (actionName != null 
					&& (!actionName.contains('bollaResa_') || authUser.edicolaTestPerModifiche eq false) 
					&& !actionName.contains('vendite_') 
					&& !actionName.contains('reportVendite_')
					&& !actionName.contains('rifornimenti_')
					&& !actionName.contains('viewRifornimenti_')
					&& !actionName.contains('viewVariazioni_')
					&& !actionName.contains('prenotazioneClienti_')
					&& !actionName.contains('configurazioni_')
					&& !actionName.contains('gestioneUtenti_')
					&& !actionName.contains('pne')
					&& !actionName.contains('messages_')
					&& !actionName.contains('bollaRivenditaSpunta_')
					&& !actionName.contains('venduto_')
					&& !actionName.contains('params_'))">
					<div
						style="float: left; width: 15%; text-align: center; margin-left: auto; margin-right: auto;">
						<s:if
							test="actionName != null && actionName.equals('sonoInoltreUscite_showBollaSonoInoltreUscite.action')">
							<s:form id="dlAction"
								action="bollaRivendita_showFilterSonoInoltreUscite.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:if>
						<s:elseif
							test="actionName != null && actionName.equals('bollaResa_showBollaResa.action')">
							<s:form id="dlAction" action="home.action" method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.contains('viewBollaResa_')">
							<s:form id="dlAction" action="viewBollaResa_showFilter.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.equals('estrattoConto_showEstratto.action')">
							<s:form id="dlAction" action="estrattoConto_showFilter.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.equals('infoPubblicazioni_reportContoDeposito.action')">
							<s:form id="dlAction"
								action="infoPubblicazioni_reportContoDepositoFilter.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.equals('infoPubblicazioni_showPubblicazioni.action')">
							<s:form id="dlAction"
								action="infoPubblicazioni_showFilter.action" method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.equals('statisticaPubblicazioni_showPubblicazioniStatistica.action')">
							<s:form id="dlAction"
								action="statisticaPubblicazioni_showFilterStatistica.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.equals('bollaResa_showQuadraturaResa.action')">
							<s:form id="dlAction"
								action="bollaResa_showQuadraturaResaFilter.action" method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.equals('mancanzeBolla_showMancanzeBolla.action')">
							<s:form id="dlAction"
								action="mancanzeBolla_showMancanzeBollaFilter.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && actionName.equals('emailInviati_showEmailInviati.action')">
							<s:form id="dlAction"
								action="emailInviati_showEmailInviatiFilter.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && (actionName.equals('rifornimenti_showPubblicazioniRifornimenti.action') || actionName.equals('rifornimenti_showFilterRifornimenti.action'))">
							<s:form id="dlAction"
								action="rifornimenti_showFilterRifornimenti.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && (actionName.equals('variazioni_showPubblicazioniVariazioni.action') || actionName.equals('variazioni_showFilterVariazioni.action'))">
							<s:form id="dlAction"
								action="variazioni_showFilterVariazioni.action" method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:elseif
							test="actionName != null && (actionName.equals('gestioneClienti_showClienti.action') || actionName.equals('gestioneClienti_showFilter.action'))">
							<s:form id="dlAction" action="gestioneClienti_showFilter.action"
								method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:elseif>
						<s:else>
							<s:form id="dlAction" action="%{actionName}" method="POST">
								<s:select name="authUser.codFiegDl" id="dlSelect"
									listKey="codFiegDl" listValue="ragioneSocialeDlPrimaRiga"
									list="authUser.listDl" cssClass="comboDl" />
								<s:hidden name="coddlSelect" id="coddlSelect" />
							</s:form>
						</s:else>
					</div>
				</s:elseif>
			</div>
		</s:if>
	</div>
	<div id="alertMessageDiv"
		style="width: 50px; font-size: 80%; margin-left: 50px; margin-top: 10px; background: transparent; float: left; white-space: normal; text-align: center"></div>
</div>
