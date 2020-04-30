<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
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
				    var popWidth = 700;
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
				$("#attachment"+ index+"_inputfile").show();	
				$("#attachment"+ index+"_viewfile").hide();	
				//$(this).parent().hide();			
			});
		</s:else>
		
		$("#destinatarioSelected").val('<s:text name="destinatario"/>');
		
		var msg = '';
		if (document.getElementById('messaggio').value != '') {		
			msg = document.getElementById('messaggio').value;
		} 
		
		$("#rivenditeSel").click(function(e) {
			var val = $(this).val().length > 1 ? $(this).val()[0] : $(this).val();
			$("#rivenditeSel option:selected").removeAttr("selected");
			$("#rivenditeSel").val(val);				
		});
		
		$("#rivenditeSel").contextMenu({
				menu : 'edicoleMenu', yTop : 150, xLeft : 30, highlight : true
			}, function(action, el, pos) {
				val = $(el).find("option:selected").val();
				$("#rivenditeSel").find("option[value='" + val + "']").remove();
			}
		);
		
		//Usage: writeRichText(fieldname, html, width, height, buttons, readOnly) 
		writeRichText('rte1', msg, 730, 300, true, <s:text name="disableForm"/>);	
		setContentDivHeight(30); 
		$(document)[0].oncontextmenu = function() {return false;}; 
		
		<s:if test="messaggio.attachmentName1 == null || messaggio.attachmentName1 == ''">
			$("#attachment1_inputfile").show();	
			$("#attachment1_viewfile").hide();	
		</s:if>	
		<s:else>
			$("#attachment1_inputfile").hide();	
			$("#attachment1_viewfile").show();	
		</s:else>
		<s:if test="messaggio.attachmentName2 == null || messaggio.attachmentName2 == ''">
			$("#attachment2_inputfile").show();	
			$("#attachment2_viewfile").hide();	
		</s:if>	
		<s:else>
			$("#attachment2_inputfile").hide();	
			$("#attachment2_viewfile").show();	
		</s:else>
		<s:if test="messaggio.attachmentName3 == null || messaggio.attachmentName3 == ''">
			$("#attachment3_inputfile").show();	
			$("#attachment3_viewfile").hide();	
		</s:if>	
		<s:else>
			$("#attachment3_inputfile").hide();	
			$("#attachment3_viewfile").show();	
		</s:else>
		
	});		 
	
	$.datepicker.setDefaults($.datepicker.regional['it']);
	$('#strDataMessaggio').datepicker();
	
// 	$('#strDataMessaggio').click(function() { 
//     	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 	});
	
	$('#sysdate').click(function() {
		var checked = $('#sysdate').attr('checked');
		$('#strDataMessaggio').attr('disabled', checked);
		$('#oraMessaggio').attr('disabled', checked);
		$('#minutoMessaggio').attr('disabled', checked);
		$('#secondoMessaggio').attr('disabled', checked);
	});
	
	function checkFields(noAlerts) {
		if ($("#dtMessaggio").val().length == 0 && !$('#sysdate').attr('checked') && $('#strDataMessaggio').val() == '') {
			if (!noAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="error.specificare.data"/>', attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
			}
			unBlockUI();
			return false;
		}
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
					//07-09-2017 solo se viene inserito un valore numerico 
					if( $("#crivwSelect").val() == ''){
						if (!noAlerts) {
							jAlert('<s:text name="igeriv.specificare.rivendita.auto"/>');
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
		$("#rivenditeSel option").attr("selected", "selected");
		return true;	
	}
	
	function switchDestinaratariDiv() {
		switch(Number($("input[name=destinatario]:checked").val())) {
			case 0:
				$("#rivenditeMultipleDiv").hide();
				$("#rivenditeDiv").show();
				$("#giriDiv").hide();
				$("#zoneDiv").hide();
				$("#edicolaLabel").attr("disabled","true");
				break;
			case 1:
				$("#rivenditeMultipleDiv").hide();
				$("#rivenditeDiv").show();
				$("#giriDiv").hide();
				$("#zoneDiv").hide();
				$("#edicolaLabel").removeAttr("disabled");
				$("#edicolaLabel").focus();
				break;
			case 2:
				$("#rivenditeMultipleDiv").hide();
				$("#rivenditeDiv").hide();
				$("#giriDiv").show();
				$("#zoneDiv").hide();
				$("#giroTipo").focus();
				setContentDivHeight(50);
				break;
			case 3:
				$("#rivenditeMultipleDiv").hide();
				$("#rivenditeDiv").hide();
				$("#giriDiv").hide();
				$("#zoneDiv").show();
				$("#zonaTipo").focus();
				setContentDivHeight(50);
				break;
			case 4:
				$("#rivenditeMultipleDiv").show();
				$("#rivenditeDiv").hide();
				$("#giriDiv").hide();
				$("#zoneDiv").hide();
				$("#riv").focus();
				setContentDivHeight(50);
				break;
		}
	}
	
	function submitForm(actionName) {
		document.getElementById("MessageForm").action = actionName;
		document.getElementById("MessageForm").submit();
	}
	
	function synchEditor() {
		updateRTE('rte1');
		document.getElementById("message").value = document.forms[0].rte1.value.replace(/\u20ac/g,'&#8364;');
		return true;
	}
	
	$("input#edicolaLabel, input#riv").keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode); 
		var val = $(this).val();
		if (keycode == 13 && !isNaN(val)) {
			event.preventDefault();
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}${ap}/automcomplete_edicoleWebByCrivDl.action?soloUtentiAmministratori=true&term=" + val,	
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
				handle: function(data,args) {
					if (typeof(data) !== 'undefined' && data.length > 0) {
						var op = Number($("input[name=destinatario]:checked").val());
						var item = data[0];
						if (op == 1) {
							$("#edicolaLabel").val(item.label);
							$('#strCodEdicolaDl').val(item.id);
							//aggiunto per la validazione della scelta in autocompletamento
							$('#crivwSelect').val(item.id);
							
						} else if ($("#rivenditeSel option[value='" + item.id + "']").length === 0) {
							$("#riv").val(item.label);
							//aggiunto per la validazione della scelta in autocompletamento
							$('#crivwSelect').val(item.id);
							$('#rivenditeSel').append($("<option/>", {
								value: item.id,
								text: item.label
							}));
							setTimeout(function() {
								$("#riv").val("");
							}, 100);
						}
					}
				}
			});
		}   
	});
	
	$("input#edicolaLabel, input#riv").autocomplete({	
		minLength: 1,			
		source: function(request, response) {		
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}${ap}/automcomplete_edicoleWebByCrivDl.action?soloUtentiAmministratori=true",	
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
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
			var op = Number($("input[name=destinatario]:checked").val());
			if (op == 1) {
				$('#strCodEdicolaDl').val(ui.item.id);
				$('#crivwSelect').val(ui.item.id);
			} else if ($("#rivenditeSel option[value='" + ui.item.id + "']").length === 0) {
				$('#rivenditeSel').append($("<option/>", {
					value: ui.item.id,
					text: ui.item.label
				}));
				setTimeout(function() {
					$("#riv").val("");
				}, 100);
			}
		}
	});	
	
	
	$("input#giroTipo").autocomplete({	
		minLength: 1,			
		source: function(request, response) {		
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}${ap}/automcomplete_giriTipo.action",	
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
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
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
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
			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
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
			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
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