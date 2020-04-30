<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 0px;
}

div#content1 {
	height: 800px;
}
</style>
<div style="width: 100%; height: 80%;">
	<div
		style="width: 900px; height: 100%; text-align: center; margin-left: auto; margin-right: auto; background-image: url(/app_img/rete_igeriv.png); background-repeat: no-repeat;">
		<div style="height: 20px">&nbsp;</div>
		<div>
			<h1>
				Iniziativa <i><b>Porta un collega in iGeriv e guadagna un
						mese gratis</b></i>
			</h1>
			<br>
			<div>
				Suggerisci ai tuoi colleghi edicolanti, che non hanno ancora iGeriv,
				di provare il sistema. <br> Per <b>ogni</b> nuovo collega da te
				indicato che confermerà l'attivazione di iGeriv entro il periodo di
				prova di 30 giorni, <br> guadagnerai<b> una
					mensalit&agrave; gratuita sul tuo canone mensile</b><br> (il bonus
				sarà accreditato sul trimestre successivo a quello corrente). <br>
				In questo modo, pi&ugrave; colleghi porti in iGeriv, minore
				sar&agrave; per te il costo del servizio. <br> Inoltre,
				aumentando il numero degli iscritti al sistema, si avr&agrave; un
				servizio sempre migliore.
			</div>
			<br>
			<h2>
				<i>Più siamo, migliore è il servizio, minore è il costo !</i>
			</h2>
			<div style="height: 100px">
				Se un tuo collega &egrave; daccordo con l&\#39;iniziativa, e decide
				di iscriversi a iGeriv, <br> fatti dare il suo codice edicola
				(quello che viene stampato sulle bolle) e la sua email<br> e
				inseriscile nel form qui di seguito.<br> Se i dati sono
				corretti, riceverai subito una mail che conferma l&\#39;avvenuta
				richiesta di iscrizione.<br> Una volta attivata la nuova
				edicola presso il DL, riceverai una mail di conferma <br>
				dell&\#39;accredito del bonus.
			</div>
		</div>
		<div style="height: 10px">&nbsp;</div>
		<s:form id="SendRequestForm" action="cic_sendRequest.action"
			namespace="/" method="POST" theme="igeriv" validate="false"
			onsubmit="return (ray.ajax())">
			<div
				style="width: 50%; text-align: center; margin-left: auto; margin-right: auto;">
				<div style="float: left; width: 45%">
					<s:text name="igeriv.dl.collega" />
				</div>
				<div style="float: left; width: 50%">
					<s:select label="" name="coddl" id="coddl" listKey="coddl"
						listValue="nome" list="listDl" emptyOption="false"
						cssStyle="width: 300px" cssClass="tableFields" />
				</div>
			</div>
			<div
				style="width: 50%; height: 30px; text-align: center; margin-left: auto; margin-right: auto; margin-top: 10px;">
				<div style="float: left; width: 45%">
					<s:text name="dpe.email.collega" />
				</div>
				<div style="float: left; width: 50%">
					<s:textfield label="" name="email" id="emailRequest"
						cssStyle="width:300px" cssClass="tableFields" maxlength="100" />
				</div>
			</div>
			<div
				style="width: 50%; height: 30px; text-align: center; margin-left: auto; margin-right: auto; margin-top: 10px;">
				<div style="float: left; width: 45%;">
					<s:text
						name="igeriv.statistiche.utilizzo.codice.rivendita.dl.solo.numeri" />
					<br>
				</div>
				<div style="float: left; width: 50%; white-space: nowrap">
					<s:textfield label="" name="codEdicolaDl" id="codEdicolaDl"
						cssStyle="width:40px" cssClass="tableFields" maxlength="5" />
					&nbsp;
					<s:textfield label="" name="ragSoc" id="ragSoc"
						cssStyle="width:250px" cssClass="tableFields" maxlength="100" />
				</div>
			</div>
			<div style="width: 100%; height: 30px; text-align: center;">
				<div
					style="width: 100px; margin-left: auto; margin-right: auto; margin-top: 10px;">
					<input type="button" name="invia" id="invia"
						value="<s:text name='dpe.invia.richiesta'/>" align="center"
						class="tableFields" style="text-align: center; width: 150px"
						onclick="sendRequest()" />
				</div>
			</div>
			<s:hidden name="nomeDl" id="nomeDl" />
		</s:form>
	</div>
</div>
