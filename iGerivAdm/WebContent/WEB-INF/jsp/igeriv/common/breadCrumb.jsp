<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<div style="position:relative; width:100%; z-index:0; text-align:left; margin-left:5px; margin-top:5px;">
	<div id="breadcrumbDiv" class="text" style="float:left; text-align:left; font-size:11px; width:42%; white-space:nowrap;">
		<s:iterator value="%{#session['com.strutsschool.interceptors.breadcrumbs']}" status="status">
			<s:set name="size" value="#status.index"/>				
		</s:iterator>
		<s:iterator value="%{#session['com.strutsschool.interceptors.breadcrumbs']}" status="status">
			<s:if test="#status.index > 0">
				&nbsp;&#187;&nbsp;
			</s:if>
			<s:url id="uri" action="%{action}" namespace="%{nameSpace}"/>
			<nobr>
			<s:if test="#status.index < #size">
				<a href="<s:property value="uri"/>">
			</s:if>
			<span id="actionLink<s:property value="#status.index"/>"><s:property value="wildPortionOfName"/></span>
			<s:if test="#status.index < #size">
				</a>
			</s:if>
			</nobr>		
		</s:iterator>&nbsp;
	</div>
	
	<s:if test="authUser.multiDl eq true && authUser.dlInforiv eq true && (actionName != null 
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
		<div style="float:left; width:15%; text-align:center; margin-left:auto; margin-right:auto;">
			<s:if test="actionName != null && actionName.equals('sonoInoltreUscite_showBollaSonoInoltreUscite.action')">
				<s:form id="dlAction" action="bollaRivendita_showFilterSonoInoltreUscite.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:if>
			<s:elseif test="actionName != null && actionName.equals('bollaResa_showBollaResa.action')">
				<s:form id="dlAction" action="bollaResa_showFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && actionName.equals('estrattoConto_showEstratto.action')">
				<s:form id="dlAction" action="estrattoConto_showFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && actionName.equals('infoPubblicazioni_reportContoDeposito.action')">
				<s:form id="dlAction" action="infoPubblicazioni_reportContoDepositoFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && actionName.equals('infoPubblicazioni_showPubblicazioni.action')">
				<s:form id="dlAction" action="infoPubblicazioni_showFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && actionName.equals('statisticaPubblicazioni_showPubblicazioniStatistica.action')">
				<s:form id="dlAction" action="statisticaPubblicazioni_showFilterStatistica.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && actionName.equals('bollaResa_showQuadraturaResa.action')">
				<s:form id="dlAction" action="bollaResa_showQuadraturaResaFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && actionName.equals('mancanzeBolla_showMancanzeBolla.action')">
				<s:form id="dlAction" action="mancanzeBolla_showMancanzeBollaFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && actionName.equals('emailInviati_showEmailInviati.action')">
				<s:form id="dlAction" action="emailInviati_showEmailInviatiFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:else>
				<s:form id="dlAction" action="%{actionName}" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>
			</s:else>
		</div>
	</s:if>
	<s:elseif test="authUser.multiDl eq true && (actionName != null 
		&& !actionName.contains('vendite_') 
		&& !actionName.contains('reportVendite_')
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
	<div style="float:left; width:15%; text-align:center; margin-left:auto; margin-right:auto;">
			<s:if test="actionName != null && actionName.equals('sonoInoltreUscite_showBollaSonoInoltreUscite.action')">
				<s:form id="dlAction" action="bollaRivendita_showFilterSonoInoltreUscite.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:if>
			<s:elseif test="actionName != null && actionName.equals('bollaResa_showBollaResa.action')">
				<s:form id="dlAction" action="bollaResa_showFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && actionName.equals('estrattoConto_showEstratto.action')">
				<s:form id="dlAction" action="estrattoConto_showFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && actionName.equals('infoPubblicazioni_reportContoDeposito.action')">
				<s:form id="dlAction" action="infoPubblicazioni_reportContoDepositoFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && actionName.equals('infoPubblicazioni_showPubblicazioni.action')">
				<s:form id="dlAction" action="infoPubblicazioni_showFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && actionName.equals('statisticaPubblicazioni_showPubblicazioniStatistica.action')">
				<s:form id="dlAction" action="statisticaPubblicazioni_showFilterStatistica.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && actionName.equals('bollaResa_showQuadraturaResa.action')">
				<s:form id="dlAction" action="bollaResa_showQuadraturaResaFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && actionName.equals('mancanzeBolla_showMancanzeBolla.action')">
				<s:form id="dlAction" action="mancanzeBolla_showMancanzeBollaFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && actionName.equals('emailInviati_showEmailInviati.action')">
				<s:form id="dlAction" action="emailInviati_showEmailInviatiFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && (actionName.equals('rifornimenti_showPubblicazioniRifornimenti.action') || actionName.equals('rifornimenti_showFilterRifornimenti.action'))">
				<s:form id="dlAction" action="rifornimenti_showFilterRifornimenti.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && (actionName.equals('variazioni_showPubblicazioniVariazioni.action') || actionName.equals('variazioni_showFilterVariazioni.action'))">
				<s:form id="dlAction" action="variazioni_showFilterVariazioni.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:elseif test="actionName != null && (actionName.equals('gestioneClienti_showClienti.action') || actionName.equals('gestioneClienti_showFilter.action'))">
				<s:form id="dlAction" action="gestioneClienti_showFilter.action" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>				
			</s:elseif>
			<s:else>
				<s:form id="dlAction" action="%{actionName}" method="POST">
					<s:select name="authUser.codFiegDl"
					    id="dlSelect" 
					    listKey="codFiegDl" 
					    listValue="ragioneSocialeDlPrimaRiga"
					    list="authUser.listDl"
					    cssStyle="width:150px; font-size:80%; height:20px"
					    />
					  <s:hidden name="coddlSelect" id="coddlSelect"/>
				</s:form>
			</s:else>
		</div>
	</s:elseif>
</div>
