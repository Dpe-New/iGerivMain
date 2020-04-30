<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		
		<s:if test="hasFieldErrors()">
			var msg = '';
			<s:iterator value="fieldErrors">
				msg += '<s:property value="%{value}" escape="false" />'.replace("[","").replace("]","");
			</s:iterator>
			$.alerts.dialogClass = "style_1";
			jAlert(msg, attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
			});
		</s:if>
		
		$("#attachment1_show, #attachment2_show, #attachment3_show").hide();
		
		$('#delete1,#delete2,#delete3').click(function() {			
			var index = $(this).attr("id").substring($(this).attr("id").length - 1);
			$('#attachmentName' + index).val('');
			$("#attachment" + index + "_input").show();
			$("#attachment" + index + "_show").hide();
			$("#attachment" + index + "_href").attr("href", "");
			$("#attachment" + index + "_href").contents().replaceWith("_");
		});

		initRTE("/app_img/", "${pageContext.request.contextPath}/html/", "");
		//Usage: writeRichText(fieldname, html, width, height, buttons, readOnly) 
		writeRichText('rte1', '', 730, 300, true, <s:text name="disableForm"/>);	
		setContentDivHeight(30); 
		$(document)[0].oncontextmenu = function() {return false;}; 
		
		<s:if test="hasFieldErrors()">
			$('#rte1').contents().find('html').html('<s:property value="message" escape="false"/>');
			$('#rte1').contents().find('body').css('background-color', '#fff');
		</s:if>
	});
	
	$("#pubblicazione").blur(function() {
		if ($("#pubblicazione").val() == '') {
			$('#copertina').empty();
			clearMessaggio();
		}
	});
	
	$("#pubblicazione").keydown(function(event) {
		if (event.which == 13) {
			event.preventDefault();
		}
	});
	
	$("#copertina").change(function() {
		if ($("#copertina").val() == '') {
			clearMessaggio();
		}
		else {
			loadMessaggio();
		}
	});
	
	$("#pubblicazione").autocomplete({
		minLength: 3,			
		source: function(request, response) {		
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}${ap}/automcomplete_getProdottoByCodOrTitolo.action",	
				preventCache: true,
				handleAs: "json",				
				headers: {"Content-Type": "application/json; charset=uft-8"}, 	
				content: { 
			    	term: request.term
			    }, 					
				handle: function(data,args) {
					response($.map(data, function(item) {											
		                                return {
		                                    id: item.id,	
		                                    label: item.label,
		                                    value: item.label		                                   
		                                }
		                            }))
				}
			});
		},
		select: function (event, ui) {	
			findNumeriCopertina(ui.item.id);
		}
	});
	
	function findNumeriCopertina(codPubblicazione) {
		$('#copertina').empty();
		clearMessaggio();
		if (codPubblicazione != '') {
			dojo.xhrGet({
				url : "${pageContext.request.contextPath}/automcomplete_getNumeriCopertina.action",
				preventCache : true,
				handleAs : "json",
				headers : {
					"Content-Type" : "application/json; charset=uft-8"
				},
				content: {
					term: codPubblicazione
				},
				handle : function(data, args) {
					var numCopertinaSel = '<s:property value="numCopertina"/>';
					$('#copertina').append($("<option/>", {
				        value: '',
				        text: ''
				    }));
					$.map(data, function(item) {
						var sel = numCopertinaSel == item.id ? "selected" : ""; 
						$('#copertina').append($("<option/>", {
					        value: item.id,
					        text: item.label,
					        selected : sel
					    }));
					});
				}
			});
		}
	};
	
	function clearMessaggio() {
		document.getElementById("rte1").contentWindow.document.execCommand('selectAll', false, null);
		document.getElementById("rte1").contentWindow.document.execCommand('delete', false, null);
		$("#idMessaggioIdtn, #message, #testo, #attachmentName1, #attachmentName2, #attachmentName3").val('');
		$("#attachment1_input, #attachment2_input, #attachment3_input").show();
		$("#attachment1_show, #attachment2_show, #attachment3_show").hide();
		$("#attachment1_href, #attachment2_href, #attachment3_href").attr("href", "");
		$("#attachment1_href, #attachment2_href, #attachment3_href").contents().replaceWith("_");
	}
	
	function loadMessaggio() {
		ray.ajax();
		$("#idtn").val($("#copertina").val());
		
		dojo.xhrGet({
			url: "${pageContext.request.contextPath}${ap}/messagesJson_loadMessageIdtn.action",	
			preventCache: true,
			handleAs: "json",
			content: {
				idtn: $("#copertina").val()
			},
			load: function(data, args) {
				clearMessaggio();
				if (data.id != '') {
					$("#idMessaggioIdtn").val(data.id);
					$("#message, #testo").val(data.testo);
					document.getElementById("rte1").contentWindow.document.execCommand('insertHTML', false, data.testo);
					if (data.allegato1 != null && data.allegato1 != '') {
						$("#attachmentName1").val(data.allegato1);
						$("#attachment1_input").hide();
						$("#attachment1_show").show();
						$("#attachment1_href").attr("href", "${pageContext.request.contextPath}${ap}/attachment.action?fileName=" + data.allegato1);
						$("#attachment1_href").contents().replaceWith(data.allegato1);
					}
					if (data.allegato2 != null && data.allegato2 != '') {
						$("#attachmentName2").val(data.allegato2);
						$("#attachment2_input").hide();
						$("#attachment2_show").show();
						$("#attachment2_href").attr("href", "${pageContext.request.contextPath}${ap}/attachment.action?fileName=" + data.allegato2);
						$("#attachment2_href").contents().replaceWith(data.allegato2);
					}
					if (data.allegato3 != null && data.allegato3 != '') {
						$("#attachmentName3").val(data.allegato3);
						$("#attachment3_input").hide();
						$("#attachment3_show").show();
						$("#attachment3_href").attr("href", "${pageContext.request.contextPath}${ap}/attachment.action?fileName=" + data.allegato3);
						$("#attachment3_href").contents().replaceWith(data.allegato3);
					}
				}
				
				unBlockUI();
			},
			error: function(data, args) {
				unBlockUI();
				$.alerts.dialogClass = "style_1";
				jAlert(msgErroreInvioRichiesta, '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
			}
		});
	}
	
	function checkFields(noAlerts) {
		if ($("#idtn").val() == '') {
			if (!noAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="igeriv.specificare.copertina"/>', attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
			}
			unBlockUI();
			return false;
		}
		else if ($("#message").val() == '') {
			if (!noAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="igeriv.email.vuoto"/>', attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
			}
			unBlockUI();
			return false;
		}
		return true;	
	}
	
	function doDelete() {
		PlaySound('beep3');
		jConfirm("<s:text name='gp.cancellare.dati'/>", attenzioneMsg, 
			function(r) {
				if (r) {
					submitForm('messages_deleteMessageIdtn.action');
				} else {
					setTimeout('unBlockUI();', 100);
				}
			}
		);
	}
	
	function synchEditor() {
		updateRTE('rte1');
		$("#message").val(document.forms[0].rte1.value);
		$("#testo").val(document.forms[0].rte1.value);
		return true;
	}
	
	function submitForm(actionName) {
		document.getElementById("MessageForm").action = actionName;
		document.getElementById("MessageForm").submit();
	}
	
</script>