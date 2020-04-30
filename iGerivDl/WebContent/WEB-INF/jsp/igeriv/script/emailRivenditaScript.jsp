<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script>
	var arrEmails = new Array();
	var emails = <s:text name="#session['emailDlJson']"/>;
	var msgIndirizziMail = '<s:text name="igeriv.scelta.indirizzi.email.dl"/>';
	var msgRiv = "<s:text name="#session['messaggioDaRivendita']"/>";

	$(document).ready(function() {
		writeRichText('rte1', '', 600, 300, true, false);	
		setContentDivHeight(30);  
		$("#destinatariImg").tooltip({
			delay: 0,  
		    showURL: false
		}); 
		$("#destinatariImg").click(function() {
			openEmailPopup('destinatari');
		});		
		<s:if test="%{msgSent == true}">
			jAlert('<s:text name="igeriv.msg.inviato"/>');
		</s:if>
		<s:if test="%{#request.emailValido == false}">
			jConfirm('<s:text name="igeriv.msg.email.non.registrata.invio.messaggio.al.dl"/>', '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function(r) {
				if (r) {
					setTimeout(function() {
						promptForEmail('${pageContext.request.contextPath}${ap}/<s:text name="actionName"/>');
					}, 100);
				}
			}, true, true);
		</s:if>
	});		 
		
	function openEmailPopup(field) {
		var helpList = "<div class='helpmenu' style='white-space:nowrap; width:400px'>";
		helpList += "<ul id='emailMenuId'>";
		for (i = 0; i < emails.length; i++) {
			var codEmail = emails[i].codEmailVo;
			var nome = emails[i].nome;
			var reparto = emails[i].reparto;
			var checked = $.inArray(codEmail, arrEmails) == -1 ? "" : "checked";
			helpList += '<li><input type="checkbox" onclick="javascript:setCheckbox($(this), ' + codEmail + ');" id="selectedEmail' + i + '" ' + checked + '/>&nbsp;&nbsp;' + nome + ' (' + reparto + ')</li>';
		}
		helpList += "</ul>";
		helpList += "</div>";
		$.alerts.dialogClass = "style_email_popup";
		jAlert(helpList, msgIndirizziMail, function() {
			$.alerts.dialogClass = null;
			var emailNames = '';
			for (i = 0; i < arrEmails.length; i++) {
				for (y = 0; y < emails.length; y++) {
					var codEmail = emails[y].codEmailVo;
					var nome = emails[y].nome;
					if (codEmail == arrEmails[i]) {
						emailNames += (i > 0 && i < arrEmails.length) ? ";" + nome : nome;
						break;
					}
				}
			}
			$("#" + field).val(emailNames);
		});
	}

	function setCheckbox(obj, codEmail) {
		if (obj.is(':checked')) {
			arrEmails.push(codEmail);			
		} else {
			for (y = 0; y < arrEmails.length; y++) {
				if (arrEmails[y] == codEmail) {
					arrEmails.splice(y, 1);
					break;
				}
			}
		}
	}
	
	function submitForm(actionName) {
		<s:if test='destinatari == null'>
			if (arrEmails.length == 0) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="igeriv.nessun.email.selezionato"/>','<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
				});
				return false;
			}
		</s:if>
		if ($("#message").val().length == 0) {
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="igeriv.email.vuoto"/>','<s:text name="msg.alert.attenzione"/>', function() {
				$.alerts.dialogClass = null;
			});
			return false;
		}
		$("#emailsToSend").val(arrEmails);
		$("#MessageForm").attr('action', actionName);
		$("#MessageForm").submit();
	}
	
	function synchEditor() {
		updateRTE('rte1');
		$("#message").val(document.forms[0].rte1.value);
		return true;
	}
	
	initRTE("/app_img/", "${pageContext.request.contextPath}/html/", "");	
	
</script>