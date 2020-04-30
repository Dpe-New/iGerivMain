<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {	
		sendRequest = function() {
			var codEdicolaDl = $("#codEdicolaDl").val();
			var email = $("#emailRequest").val();
			if (email == null || email.trim() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="dpe.email"/>'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#emailRequest").focus();
				});
				return false; 
			}
			if (!checkEmail(email)) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="igeriv.email.non.valido"/>', attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#emailRequest").focus();
				});
				return false;
			}
			if (codEdicolaDl == null || codEdicolaDl.trim() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="igeriv.statistiche.utilizzo.codice.rivendita.dl"/>'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#codEdicolaDl").focus();
				});
				return false; 
			}
			if (codEdicolaDl != '' && isNaN(parseLocalNum(codEdicolaDl))) {
				$.alerts.dialogClass = "style_1";
				jAlert(numCivicoMsg.replace('{0}', '<s:text name="igeriv.statistiche.utilizzo.codice.rivendita.dl"/>'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$('#codEdicolaDl').focus();
				});
				return false; 
			}
			$("#nomeDl").val($("#coddl option:selected").text());
			$('#ragSoc').attr("disabled", false);
			setTimeout(function() {
				setFormAction('SendRequestForm','cic_sendRequest.action', '', '', false, '', function() { requestSent(); }, '', false, '');
			}, 10);
		};
		
		requestSent = function() {
			jAlert('<s:text name="igeriv.msg.richiesta.attivazione.inviata"/>'.replace('{0}', $("#codEdicolaDl").val()), informazioneMsg.toUpperCase());
			return false;
		};
		
		<s:if test="%{#request.emailValido == false}">
			jConfirm('<s:text name="igeriv.msg.email.non.registrata.iniziativa.invita.collega"/>', '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function(r) {
				if (r) {
					setTimeout(function() {
						promptForEmail('${pageContext.request.contextPath}${ap}/<s:text name="actionName"/>');
					}, 100);
				}
			}, true, true);
		</s:if>
		
		clearFields = function() {
			$('#ragSoc').attr("disabled", false);
			$('#codEdicolaDl').val("");
			$('#emailRequest').val("");
			$('#ragSoc').val("");
		} 
		
		getEdicola = function() {	
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}/automcomplete_edicoleGeneraleByCrivDl.action",	
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=utf-8"}, 	
				content: { 
			    	term: $("#codEdicolaDl").val(),
			    	coddl: Number($("#coddl").val())
			    }, 					
				handle: function(data,args) {
					if (typeof(data[0]) !== 'undefined') {
						$("#codEdicolaDl").val(data[0].id);
	 					$('#ragSoc').val(data[0].value);
	 					$('#ragSoc').attr("disabled", true);
					} else {
						$("#ragSoc").val("");
	 					$('#ragSoc').attr("disabled", false);
					}
				}
			});
		}
		
		$("#codEdicolaDl").keydown(function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.charCode);  
			if (keycode == 13) {
				getEdicola();
			} else if (keycode == 27) {
				clearFields();
			}	
		});
		$("#codEdicolaDl").blur(function() {getEdicola();});
		$("#coddl").change(function() {clearFields();});
		
		clearFields();
	});
</script>
