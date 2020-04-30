<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script>
	$(document).ready(function() {	
		if ($("input[name=destinatario]:checked").length == 0) {
			$("#destinatario1").attr("checked","true");
		}
		switchDestinaratariDiv();
		$("input[name=destinatario]").change(function() {switchDestinaratariDiv()});
		<s:if test="destinatario != null && ((giroTipoSelected != null && giroSelected != null) || (zonaTipoSelected != null && zonaSelected != null)) ">	
			<s:if test="destinatario == 2">
				getGiri(<s:text name="giroTipoSelected"/>, <s:text name="giroSelected"/>);
			</s:if>	
			<s:elseif test="destinatario == 3">
				getZone(<s:text name="zonaTipoSelected"/>, <s:text name="zonaSelected"/>);
			</s:elseif>	
		</s:if>	
		$("#giro_3").attr("checked","true");
		<s:if test="%{disableForm eq true}">
			disableAllFormFields('confermeLettura');
			$("#confermeLettura").click(function() {
					var popID = 'popup_name';   	     		    	  
				    var popWidth = 600;
				    var popHeight = 450;	 	
				    var pk = "<s:text name="messaggio.pk"/>";
				 	var url = "${pageContext.request.contextPath}${ap}/messages_showConfermeLettura.action?messagePk=" + pk;
					openDiv(popID, popWidth, popHeight, url);
				}
			);
		</s:if>	
		<s:else>
			$('#delete1,#delete2,#delete3').click(function() {			
				var index = $(this).attr("id").substring($(this).attr("id").length - 1);
				$('#attachmentName' + index).val('');
				$(this).parent().hide();			
			});
		</s:else>
		
		$("#destinatarioSelected").val('<s:text name="destinatario"/>');
		
		var msg = '';
		if (document.getElementById('messaggio').value != '') {		
			msg = document.getElementById('messaggio').value;
		} 
		//Usage: writeRichText(fieldname, html, width, height, buttons, readOnly) 
		writeRichText('rte1', msg, 600, 300, true, <s:text name="disableForm"/>);	
		setContentDivHeight(30); 
	});		 
	
	function checkFields(noAlerts) {
		if ($("#message").val() == '') {
			if (!noAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="igeriv.email.vuoto"/>', attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
			}
			unBlockUI();
			return false;
		}
		switch(Number($("input[name='destinatario']:checked").val())) {
				case 1:
					if ($("#edicolaLabel").val() == '') {
						if (!noAlerts) {
							jAlert('<s:text name="igeriv.specificare.rivendita"/>');
						}
						unBlockUI();
						return false;
					}
					break;
				case 2:
					if ($("#giroTipo").val() == '' || $("input[name='giro']:checked").length == 0) {
						if (!noAlerts) {
							$.alerts.dialogClass = "style_1";
							jAlert('<s:text name="igeriv.selezionare.giri"/>', attenzioneMsg.toUpperCase(), function() {
								$.alerts.dialogClass = null;
							});
						}
						unBlockUI();
						return false;
					}
					break;
				case 3:
					if ($("#zonaTipo").val() == '' || $("input[name='zona']:checked").length == 0) {
						if (!noAlerts) {
							$.alerts.dialogClass = "style_1";
							jAlert('<s:text name="igeriv.selezionare.zone"/>', attenzioneMsg.toUpperCase(), function() {
								$.alerts.dialogClass = null;
							});
						}
						unBlockUI();
						return false;
					}
					break;
		}
		return true;	
	}
	
	function switchDestinaratariDiv() {
		switch(Number($("input[@name=destinatario]:checked").val())) {
			case 0:
				$("#rivenditeDiv").show();
				$("#giriDiv").hide();
				$("#zoneDiv").hide();
				$("#edicolaLabel").attr("disabled","true");
				break;
			case 1:
				$("#rivenditeDiv").show();
				$("#giriDiv").hide();
				$("#zoneDiv").hide();
				$("#edicolaLabel").removeAttr("disabled");
				$("#edicolaLabel").focus();
				break;
			case 2:
				$("#rivenditeDiv").hide();
				$("#giriDiv").show();
				$("#zoneDiv").hide();
				$("#giroTipo").focus();
				setContentDivHeight(50);
				break;
			case 3:
				$("#rivenditeDiv").hide();
				$("#giriDiv").hide();
				$("#zoneDiv").show();
				$("#zonaTipo").focus();
				setContentDivHeight(50);
				break;
		}
	}
	
	function submitForm(actionName) {
		document.getElementById("MessageForm").action = actionName;
		document.getElementById("MessageForm").submit();
	}
	
	function afterSuccessSave() {						
		// $("#ricerca").trigger('click');								
	}		
 
	function onLoadFunction() {
		// document.getElementById('utente.nomeUtente').focus();					
	}
	
	function synchEditor() {
		updateRTE('rte1');
		document.getElementById("message").value = document.forms[0].rte1.value;
		return true;
	}
	
	$("input#edicolaLabel").autocomplete({	
		minLength: 3,			
		source: function(request, response) {		
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}${ap}/automcomplete_edicole.action",	
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
		                                    value: item.label		                                   
		                                }
		                            }))
				}
			});
		},
		select: function (event, ui) {		
			document.getElementById('strCodEdicolaDl').value = ui.item.id;		
		}
	});	
	
	
	$("input#giroTipo").autocomplete({	
		minLength: 1,			
		source: function(request, response) {		
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}${ap}/automcomplete_giriTipo.action",	
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
		                                    value: item.label		                                   
		                                }
		                            }))
				}
			});
		},
		select: function (event, ui) {						
			getGiri(ui.item.id);
		}
	});	
	
	$("input#zonaTipo").autocomplete({	
		minLength: 1,			
		source: function(request, response) {		
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}${ap}/automcomplete_zoneTipo.action",	
				preventCache: true,
				handleAs: "json",				
				content: { 
			    	term: request.term
			    }, 
				headers: { "Content-Type": "application/json; charset=utf-8"}, 	
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
			getZone(ui.item.id);
		}
	});	
	
	
	function getGiri(giroTipo, giro) {
		dojo.xhrGet({
			url: "${pageContext.request.contextPath}${ap}/pubblicazioniRpc_giri.action?giroTipo=" + giroTipo,	
			preventCache: true,
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=utf-8"}, 	
			handle: function(data,args) {
	            var list = '';		
	            for (i = 0; i < data.length; i++) {
	            	var strChecked = '';
	            	if (data[i].id == giro) {
	            		strChecked = ' checked ';
	            	}
	            	list += '<div style="float:left; width:60px;" class="required"><input type="checkbox" name="giro" id="giro_' + data[i].id + '" value="' + data[i].id  + '" ' + strChecked + ' <s:if test="%{disableForm eq true}"> disabled </s:if>/>&nbsp;&nbsp;<s:text name="igeriv.giro"/>&nbsp;' + data[i].label + '</div>';
	            }
	            $("#giriList").empty();
	            $("#giriList").html(list);
			}
		});		
	}
	 
	function getZone(zonaTipo, zona) {
		dojo.xhrGet({
			url: "${pageContext.request.contextPath}${ap}/pubblicazioniRpc_zone.action?giroTipo=" + zonaTipo,	
			preventCache: true,
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=utf-8"}, 	
			handle: function(data,args) {
				var list = '';		
	            for (i = 0; i < data.length; i++) {
	            	var strChecked = '';
	            	if (data[i].id == zona) {
	            		strChecked = ' checked ';
	            	}
	            	list += '<div style="float:left; width:60px;" class="required"><input type="checkbox" name="zona" id="zona_' + data[i].id + '" value="' + data[i].id  + '" ' + strChecked + ' <s:if test="%{disableForm eq true}"> disabled </s:if>/>&nbsp;&nbsp;<s:text name="igeriv.zona"/>&nbsp;' + data[i].label + '</div>';
	            }
	            $("#zoneList").empty();
	            $("#zoneList").html(list);
			}
		});		
	}
	
	function doDelete() {
		PlaySound('beep3');
		jConfirm("<s:text name='gp.cancellare.dati'/>", attenzioneMsg, 
			function(r) {
				if (r) {
					submitForm('messages_deleteMessage.action');
				} else {
					setTimeout('unBlockUI();', 100);
				}
			}
		);
	}
	
	initRTE("/app_img/", "${pageContext.request.contextPath}/html/", "");	
						
</script>