<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	var msgUploadFileError = '<s:text name="igeriv.upload.file.error"/>';
	
	$(document).ready(function() {
		
		<s:if test="hasFieldErrors()">
			var msg = '';
			<s:iterator value="fieldErrors">
				msg += '<s:property escape="false" />';
			</s:iterator>
			$.alerts.dialogClass = "style_1";
			jAlert(msg, attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
			});
		</s:if>
	
		$("input[name=destinatario]").change(function() {switchDestinaratariDiv()});
		
		$('#delete1,#delete2,#delete3').click(function() {			
			var index = $(this).attr("id").substring($(this).attr("id").length - 1);
			$('#attachmentName' + index).val('');
			$(this).parent().hide();			
		});
		
		$('#template').change(doChangeTemplate);
	
		$('#template').contextMenu({menu : 'templateMenu', yTop : 150, xLeft : 30, highlight : true}, function(action, el, pos) {
			var codTemplate = $("#" + el.attr("id")).val();
			if (action == "delete" && !isNaN(codTemplate) && Number(codTemplate) > 0) {
				jConfirm('<s:text name="plg.confirm.cancellare.template"/>', attenzioneMsg.toUpperCase(), function(r) {
					if (r) {
						dojo.xhrGet({
							url: "messagesClientiJ_deleteEmailTemplate.action?codTemplate=" + codTemplate,	
							preventCache: true,
							handleAs: "json",				
							headers: { "Content-Type": "application/json; charset=utf-8"}, 	
							handle: function(data,args) {
								if (args.xhr.status == 200) {
									if (typeof(data) !== 'undefined' && typeof(data.success) !== 'undefined' && data.success.toString() === 'true') {
										jAlert('<s:text name="plg.template.cancellato"/>', informazioneMsg.toUpperCase(), function() {
											$('#rte1').contents().find('html').children(":visible").html('');
											reloadTemplates();
										});
									} else if (typeof(data) !== 'undefined' && typeof(data.error) !== 'undefined') {
										$.alerts.dialogClass = "style_1";
										jAlert(data.error, attenzioneMsg, function() {
											$.alerts.dialogClass = null;
										});
									}
								} else {
									$.alerts.dialogClass = "style_1";
									jAlert('<s:text name="gp.errore.imprevisto"/>', attenzioneMsg.toUpperCase(), function() {
										$.alerts.dialogClass = null;
									});
								}
							}
						});
					}
				}, true, false);
			}
		});
		
		//Usage: writeRichText(fieldname, html, width, height, buttons, readOnly) 
		writeRichText('rte1', '', 600, 300, true, false);	
		setContentDivHeight(30);
	});	
	
	function checkFields() {
		if ($("#MessageForm input[name='destinatari']:checked").length == 0) {
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="igeriv.nessun.email.selezionato"/>', attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
			});
			unBlockUI();
			return false;
		}
		if ($("#oggetto").val().trim().length == 0) {
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="igeriv.nessun.oggetto.email"/>', attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
				$("#oggetto").focus();
			});
			unBlockUI();
			return false;
		}
		if ($('#rte1').contents().find('html').children(":visible").text().trim().length == 0) {
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="igeriv.email.vuoto"/>', attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
				$('#rte1').focus();
			});
			unBlockUI();
			return false;
		}
		return true;	
	}
	
	function switchDestinaratariDiv() {
		var $chkDest = $("#MessageForm input[type='checkbox'][name='destinatari']");
		$chkDest.attr("checked", false);
		switch(Number($("input[@name=destinatario]:checked").val())) {
			case 0:
				$chkDest.attr("checked", true);
				break;
			case 1:
				$chkDest.filter("[hasEC='true']").attr("checked", true);
				break;
			case 2:
				$chkDest.filter("[hasEC='false']").attr("checked", true);
				break;
		}
	}
	
	function synchEditor() {
		updateRTE('rte1');
		$("#message").val($('#rte1').contents().find('html').html());
		return true;
	}
	
	function resetForm() {
		$("#MessageForm input[name='destinatario']").attr("checked", false);
		$("#MessageForm input[type='checkbox'][name='destinatari']").attr("checked", false);
		$("#oggetto").val("");
		$("#MessageForm input[name^='attachment']").val("");
		$('#rte1').contents().find('html').html('');
		$('#rte1').contents().find('body').css('background-color', '#fff');
		$('#template').val('');
	}
	
	function showAlertMsgSent() {
		jAlert('<s:text name="igeriv.msg.inviati"/>', informazioneMsg, function() {
			$.alerts.dialogClass = null;
		});
	}
	
	function doUploadImage(img) {
		$("#upadloadImgForm").attr("action", "uploadMessagesClienti_uploadImage.action");
		dojo.io.iframe.send({
			form: "upadloadImgForm",			
			handleAs: "text",
			load: function(response, ioArgs) {
				if (typeof(response !== 'undefined' && response.trim().length > 0)) {
					rteCommand('rte1', 'InsertImage', response.trim()); 
				}
			},
			error: function(response, ioArgs) {
				jAlert(msgUploadFileError, attenzioneMsg.toUpperCase(), null);
		    }
		});
	}
	
	function saveTemplate() {
		var $textArea = $('#rte1').contents().find('html').children(":visible");
		if ($textArea.text().trim().length == 0) {
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="igeriv.email.vuoto"/>', attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
			});
			setTimeout(function() {
				unBlockUI();
			}, 100);
			return false;
		}
		jPrompt('<s:text name="plg.inserisci.nome.template"/>', 'Email Template', '<s:text name="plg.template.messaggio.email"/>', function(res) {
			if (res != null && res.length > 0) {
				$("#nome").val(res);
				$("#messageTemplate").val($textArea.html());
				dojo.xhrPost({
					form: "EmailTemplateForm",			
					handleAs: "json",		
					handle: function(data,args) {
						unBlockUI();
						if (typeof(data) !== 'undefined' && typeof(data.success) !== 'undefined' && data.success.toString() === 'true') {
							jAlert('<s:text name="plg.template.memorizzato"/>', informazioneMsg.toUpperCase(), function() {
								$textArea.html('');
								reloadTemplates();
							});
						} else if (typeof(data) !== 'undefined' && typeof(data.error) !== 'undefined') {
							$.alerts.dialogClass = "style_1";
							jAlert(data.error, attenzioneMsg, function() {
								$.alerts.dialogClass = null;
							});
						} else {
							$.alerts.dialogClass = "style_1";
							jAlert('<s:text name="gp.errore.imprevisto"/>', attenzioneMsg.toUpperCase(), function() {
								$.alerts.dialogClass = null;
							});
						}
					}		
				});
			} else {
				setTimeout(function() {
					unBlockUI();
				}, 100);
				return false;
			}
		});
	}
	
	function reloadTemplates() {
		dojo.xhrGet({
			url: "messagesClientiJ_getEmailTemplates.action",	
			preventCache: true,
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=utf-8"}, 	
			handle: function(data,args) {
				if (args.xhr.status == 200) {
					if (typeof(data) === 'undefined') {
						$.alerts.dialogClass = "style_1";
						jAlert(data.error, attenzioneMsg.toUpperCase(), function() {
							$.alerts.dialogClass = null;
						});
					} else {
						var templateSel = '<s:property value="codTemplate"/>';
						$('#template').empty();
						$('#template').append($("<option/>", {
					        value: '',
					        text: ''
					    }));
						$.each(data, function(key, value) {
							var sel = templateSel == key ? "selected" : ""; 
							$('#template').append($("<option/>", {
						        value: key,
						        text: value,
						        selected : sel
						    }));
						});
					}
				} else {
					$.alerts.dialogClass = "style_1";
					jAlert('<s:text name="gp.errore.imprevisto"/>', attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
					});
				}
			}
		});
	}
	
	function doChangeTemplate() {
		var codTemplate = $(this).val();
		if (!isNaN(codTemplate) && Number(codTemplate) > 0) {
			dojo.xhrGet({
				url: "messagesClientiJ_getEmailTemplate.action?codTemplate=" + $(this).val(),	
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=utf-8"}, 	
				handle: function(data,args) {
					if (args.xhr.status == 200) {
						if (typeof(data) !== 'undefined' && typeof(data.error) !== 'undefined') {
							$.alerts.dialogClass = "style_1";
							jAlert(data.error, attenzioneMsg.toUpperCase(), function() {
								$.alerts.dialogClass = null;
							});
						} else if (typeof(data) !== 'undefined' && typeof(data.template) !== 'undefined') {
							$('#rte1').contents().find('html').children(":visible").html(data.template);
						}
					} else {
						$.alerts.dialogClass = "style_1";
						jAlert('<s:text name="gp.errore.imprevisto"/>', attenzioneMsg.toUpperCase(), function() {
							$.alerts.dialogClass = null;
						});
					}
				}
			});
		} else {
			$('#rte1').contents().find('html').children(":visible").html('');
		}
	}
	
	function uploadErrCallback($doc) {
		var error = '';
		if (typeof($doc) !== 'undefined') {
			if ($('#err_attachment1', $doc).text().length > 0) {
				error = '<s:text name="igeriv.allegato1" />: ' + $('#err_attachment1', $doc).html();
			} else if ($('#err_attachment2', $doc).text().length > 0) {
				error = '<s:text name="igeriv.allegato2" />: ' + $('#err_attachment2', $doc).html();
			} else if ($('#err_attachment3', $doc).text().length > 0) {
				error = '<s:text name="igeriv.allegato3" />: ' + $('#err_attachment3', $doc).html();
			}
		}
		if (error != '') {
			$.alerts.dialogClass = "style_1";
			jAlert(error, attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
			});
			return false;
		}
		return true;
	}
	
	initRTE("/app_img/", "${pageContext.request.contextPath}/html/", "", doUploadImage);	
	
</script>