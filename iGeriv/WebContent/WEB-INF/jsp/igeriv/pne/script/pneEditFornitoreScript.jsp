<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {	
		$("input#autocomplete").autocomplete({	
			minLength: 3,			
			source: function(request, response) {		
				dojo.xhrGet({
					url: "${pageContext.request.contextPath}/automcomplete_localita.action",	
					preventCache: true,
					handleAs: "json",				
					headers: { "Content-Type": "application/json; charset=utf-8"}, 	
					content: { 
				    	term: request.term
				    }, 					
					handle: function(data,args) {
						response($.map(data, function(item) {		                            		                         	
			                                return {
			                                    id: item.id,	
			                                    label: item.label,
			                                    value: item.value,
			                                    idProvincia: item.idProvincia
			                                }
			                            }))
					}
				});
			},
			select: function (event, ui) {			
				document.getElementById('fornitore.provincia').value = ui.item.idProvincia;
				document.getElementById('codLocalita').value = ui.item.id;			
			}
		});	
		
		$("#fornitore\\.nome, #fornitore\\.cognome, #tipoLocalita, #fornitore\\.indirizzo, #fornitore\\.numeroCivico, #fornitore\\.estensione, #autocomplete, #fornitore\\.provincia, #descPaese, #fornitore\\.cap, #fornitore\\.codiceFiscale, #fornitore\\.piva, #fornitore\\.telefono, #fornitore\\.fax, #fornitore\\.email, #fornitore\\.url").keypress(function(event) {
			focusNextFormField(event);
		});
		
		setLastFocusedElement();
		
		$("#fornitore\\.nome").focus();
	});	
	
	function focusNextFormField(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);  
		if (keycode == 13) {
			event.preventDefault();
			var fieldId = lastFocusedFieldId.replace(/([.])/g,'\\\$1');
			if (fieldId == 'fornitore\\.url') {
				$("#memorizza").focus();
				return false;
			}
			var $lastField = $("#" + fieldId);
			var $nextTd = $lastField.closest("td");
			var $nextCell = null;
			var $lastValidInput = $lastField;
			while (true) {
				var $nextValidInput = $lastValidInput.next();
				if ($nextValidInput.length > 0) {
					$nextCell = $nextValidInput;
					break;
				}
				var $nextTdTemp = $nextTd.next();
				$nextTd = $nextTdTemp.length > 0 ? $nextTdTemp : $nextTd;
				if ($nextTdTemp.length == 0) {
					$nextTd = $nextTd.closest("tr").next().find("td").first();
					$validInputs = $nextTd.find("input:text:enabled, input:file:enabled, button:enabled, select:enabled");
					if ($validInputs.length > 0) {
						$nextCell = $validInputs.first();
						break;
					}
				} else {
					$validInputs = $nextTd.find("input:text:enabled, input:file:enabled, button:enabled, select:enabled");
					if ($validInputs.length > 0) {
						$lastValidInput = $validInputs.first();
						$nextCell = $lastValidInput;
						break;
					}
				}
			}
			$nextCell.focus().select();
			$lastField.css('background','#fff');
		}  
	}
	
	function doDelete() {
		jConfirm("<s:text name='gp.vuoi.cancellare.fornitore'/>", attenzioneMsg, 
			function(r) {
				if (r) {
					setFormAction('FornitoriEditForm','pneFornitori_deleteFornitore.action', '', 'messageDiv', false, '', function() {$('#ricerca').trigger('click');});
					deleted = true;
				} else {
					setTimeout('unBlockUI();', 100);
				}
			}
		);
	}
	
	function afterSuccessSave() {}
	
	function validateFieldsFornitore(showAlerts) {
		if ($("#fornitore\\.nome").val() == '') {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="error.campo.x.obbligatorio"/>'.replace('{0}','<s:text name="dpe.contact.form.name"/>'), '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
					$("#fornitore\\.nome").focus();
				});
			}
			setTimeout('unBlockUI();', 100);
			stopWaitingDiv = true;
			return false;
		}
		if ($("#fornitore\\.email").val() != '' && !checkEmail($("#fornitore\\.email").val())) {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="dpe.validation.msg.invalid.email"/>', '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
					$("#fornitore\\.email").focus()
				});
			}
			setTimeout('unBlockUI();', 100);
			stopWaitingDiv = true;
			return false;
		}
		return true;
	}
	
	function closeLayerConfirm() {
		jConfirm(msgConfirmCloseDialog, attenzioneMsg, function(r) {
			if (r) {
				$("#close").trigger('click');
			}
		}, true, false);
	}
</script>
