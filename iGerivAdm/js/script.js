$(window).ready(function() {
	setMenuListWidths();
	
	if (userType == 1) {
		if (allowSpuntaBolla == 'true') {
			var liList = $('#listMenu li').map(function(i,n) {
			    return $(n);
			});
			liList[1].find("ul li").first().before('<li><a childs="bollaRivenditaSpunta_showBolla.action" id="bollaRivenditaSpunta_showBolla.action" href="' + appContext + '/bollaRivenditaSpunta_showBolla.action">' + spuntaBollaConsegna + '</a></li>');
		}
		if (actionName.indexOf('bollaRivendita') != -1 || actionName.indexOf('bollaResa') != -1) {
			if (allowNetworkDetection == 'true') {
				new networkDetection(appContext + namespace + '/test.action', networkDetectionInterval).StartPolling();
			}
			if (attivaMemorizzazioneAutomatica) {
				new autoSaveBolle(autoSaveBolleInterval).StartPolling();
			}
		}
		dojo.require("dojo.rpc.JsonService");
		generalRpcService = new dojo.rpc.JsonService(generalRpcActionUrl);
		new highPriorityMessagesCheck(highPriorityMessagesCheckInterval).StartPolling();
	}
	
	ray = {
		ajax : function(st) {	
			if ($("#load").length > 0) {
				$("#load").remove();						
			}
			$("body").prepend('<div id="load" style="position:absolute; width:100%; height:150%; margin-left:auto; margin-right:auto; background-repeat: no-repeat; background-position: center; z-index:999999; left:0; background-image: url(/app_img/loading.gif);"></div>');
		},
		show : function(el) {
			this.getID(el).style.display = '';
		},
		getID : function(el) {
			return document.getElementById(el);
		}
	};	
	var index = 0;	
	while (true) {
		var linkTag;
		if (!document.getElementById("actionLink" + index)) {					
			break;
		}
		linkTag = $("#actionLink" + index);
		var a = linkTag.closest('a');
		if (a.length > 0) {
			var act = a.attr('href');			
			act = act.substring(act.lastIndexOf("/") + 1);			
			if (Trim(linkTag.text()).toUpperCase() != 'HOME') {			
				var menuLink = $("#mydroplinemenu a[id='" + act + "']");
				if (menuLink.length <= 0) {
					var parentMenu = $("a[href$='#']:contains('" + a.text().trim() + "')").first();
					parentMenu.css({"color":"#FA7204","font-size":"12px"});
					a.parent().text(a.text());
				}				
			} else {
				a.text('Home');
			}
		} else {
			var title = Trim(linkTag.text());
			var menuLink = $("#mydroplinemenu a[id='" + title + "']");
			var menuLink1 = $("#mydroplinemenu a[childs~='" + title + "']");						
			if (menuLink.length > 0) {
				title = Trim(menuLink.text());	
				linkTag.text(title);
			} else if (menuLink1.length > 0) {		
				title = Trim(menuLink1.text());					
				linkTag.text(title);	
			}	
		}
		index++;
	}

	droplinemenu.buildmenu("mydroplinemenu");
	
	if ('home.action' == actionName) {
		$("#menuHomeLink a").css({"color":"#FA7204","font-size":"12px"});
	}
	
	$("#menuHomeLink a").hover(function() {
		  $(this).css({"color":"#ffcc66"});
		}, function() {
			$(this).css({"color":"white"});
		}
	);
	
	// MESSAGGI DALLA DPE - NOVITA'
	if (hasMessaggiDpe) {
		var div = '<a href="javascript: openDiv(\'popup_name\', 800, 500, \'' + appContext + namespace + '/msgDpe_viewActiveMessages.action?abilitati=true\');" style="text-decoration:none; text-align:top; margin-top:0; color:#336699; font-family:century; font-style: italic; font-weight:bold; font-size:120%;">' + msgNovita + '</a>';
		$('#dpeMessageDiv').html(div);	
		$('#dpeMessageDiv').tooltip({
			delay: 0,  
		    showURL: false,
		    bodyHandler: function() { 
		    	return "<b>" + msgNovitaSistema + "</b>";
		    }
		}); 
	}
	
	// MESSAGGI DL PRIORITA' MEDIA
	if (hasMessaggiPrioritaMedia) {
		var div = '<a href="' + appContext + namespace + '/messages_showMessagesEdicole.action" style="text-decoration:none; text-align:center; color:red; font-family:century; font-weight:bold; font-size:110%;">' + msgNuoviMessaggi + '</a>';
		$('#alertMessageDiv').html(div);		
		$("#alertMessageDiv").tooltip({
			delay: 0,  
		    showURL: false,
		    bodyHandler: function() { 
		    	return "<b>" + msgVisualizzaMsgImportantiDl + "</b>";
		    }
		}); 
		function pulsate() { 
		    $("#alertMessageDiv"). 
		      animate({opacity: 0.2}, 1000, 'linear'). 
		      animate({opacity: 1}, 1000, 'linear', pulsate); 
		} 
		pulsate(); 			 
	}
	
	// MESSAGGI DL PRIORITA' ALTA
	if (hasMessaggiPrioritaAlta) {
		showAlertMessaggioAltaPriorita(messaggioNonLetto);
	}
	
	// ARGOMENTI DI AIUTO
	if (hasArgomentiAiuto) {		
		var helpTitle = "<div class='textTitle' style='margin-top:10px; margin-left:10px'>" + msgHelp + "</div>";
		var helpText = helpTitle + helpList;
		if (actionName.indexOf('vendite_') != -1) {
			$("#help").remove();
			$("#content1").prepend('<div id="help" style="position:absolute; cursor: pointer; margin-top:-48px; margin-left:20px;"></div>');
		}
		$("#help").html('<img id="questionHelp" title="' + helpArgumentsTitle + '" src="/app_img/question_48x48.png"/>');
		$("#help").append('<div id="helpText" style="border:2px solid; margin-top:10px; width:250px; background:#dfe4e8; position:absolute; border-color:#3399cc; z-index:99">' + helpText + '</div>');
		var h = $("#helpMenuId").height() + 50;
		$("#helpText").hide();
		$("#helpText").corner("round 8px").parent().css('padding', '4px').corner("round 10px");
		$("#helpText").height(h);
		$("#help").click(function() {
			if ($("#helpText").is(":hidden")) {
				$("#helpText").slideDown("slow");
			} else {
				$("#helpText").slideUp('slow');
			}
		});
	}
	
	// NOTIFICA PRESENZA NUOVI ORDINI CLIENTI
	if (hasNotificheOrdiniClienti && hasProfiloStarter != 'true') {
		var div1 = $("<div id='notificheClientiDiv'>");
		var left = $("#dpeMessageDiv").is(":visible") ? Math.abs($("#icons").offset().left + $("#icons").width()) + $("#dpeMessageDiv").width() : Math.abs($("#icons").offset().left + $("#icons").width());
		div1.css({'float':'left','width':'50px','top':'0px','margin-top':'0px','height':'45px','left': left + 'px','background':'transparent','position':'absolute'});				
		div1.html('<div style="top:20%; position:absolute;"><a href="' + appContext + namespace + '/report_esportaOrdiniClientiNotifichePdf.action" target="_blank"><img id="ordiniClienti" src="/app_img/ordini_clienti.png" alt="' + msgNuoviOrdiniClienti + '" border="0" title="' + msgNuoviOrdiniClienti + '"/></a></div>');
		$('#icons').append(div1);	
		function pulsate1() {
		    $("#notificheClientiDiv").animate({opacity: 0.2}, 1000, 'linear').animate({opacity: 1}, 1000, 'linear', pulsate1); 
		} 
		pulsate1();
		$(div1).tooltip({
			delay: 0,  
		    showURL: false,
		    bodyHandler: function() { 
		    	return "<b>" + msgNotificheOrdiniClienti + "</b>";
		    }
		}); 
	}
	
	// AGGIUNGE L'IMMAGINE BOTTONE PER L'ATTIVAZIONE DEL PERIODO DI PROVA ALL'IGERIV STARTER
	if (hasProfiloStarter == 'true') {
		var msgProva = msgAttivaPeriodoProva.replace('{0}', giorniProvaPerStarter);
		var div1 = $("<div id='attivaPeriodoProvaDiv'>");
		var left = $("#dpeMessageDiv").is(":visible") ? Math.abs($("#icons").offset().left + $("#icons").width()) + $("#dpeMessageDiv").width() : Math.abs($("#icons").offset().left + $("#icons").width());
		div1.css({'float':'left','width':'50px','top':'0px','margin-top':'0px','height':'45px','left': left + 'px','background':'transparent','position':'absolute'});				
		div1.html('<div style="top:20%; position:absolute;"><img id="attivaPeriodoProva" src="/app_img/attiva_prova.png" border="0" onclick="javascript:attivaPeriodoProvaStarter();" style="cursor:pointer"/></div>');
		$('#icons').append(div1);	
		function pulsatePeriodoProva() {
		    $("#attivaPeriodoProvaDiv").animate({opacity: 0.2}, 1000, 'linear').animate({opacity: 1}, 1000, 'linear', pulsatePeriodoProva); 
		} 
		pulsatePeriodoProva();
		$(div1).tooltip({
			delay: 0,  
		    showURL: false,
		    bodyHandler: function() { 
		    	return "<b>" + msgProva + "</b>";
		    }
		}); 
	}
	
	$(document).click(function(evt) {
		var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
	    if (!($(node).closest("div").hasClass("droplinebar"))) {
	    	 $("#mydroplinemenu>ul>li>ul").slideUp();
	    	 $("#mydroplinemenu>ul>li>ul").removeAttr('down');
	    }
	});
	
	$("table[id$='_table']").each(function() {						
			addTableLastRow($(this).attr("id"));				
	});	
	
	$("img[src*='pdf.gif'],img[src*='xls.gif']").closest("form").first().each(function() {
		if ($("#downloadToken").length == 0 && $('input[name="downloadToken"]').length == 0) {
			addHiddenInput($(this).attr("id"), "downloadToken");
		}
	});	
	
	$('[id*=_table]').each(function() {
		var tableId = $(this).attr("id");
		if ($(this).closest("div[id*=Scroll]").first().hasScrollBar()) {
			setScrollingTableBody(tableId, $(this).closest("div[id*=Scroll]").first().attr("id"));
		} else {
			addWaitingLayerEventOnTableHeaderThead(tableId);
		}
	});
	
	$("#dlSelect").click(function() {
		var old = $(this).val();
		$(this).data('oldData', old);
	}).change(function() {
		var coddlSelect = $(this).val();
		var oldData = $(this).data('oldData');
		$.alerts.dialogClass = "style_1";
		PlaySound('beep3');
		jConfirm(msgCambioDl, attenzioneMsg, function(r) {
			$.alerts.dialogClass = null;
		    if (r) { 
		    	$("#coddlSelect").val(coddlSelect);
		    	$("#dlAction").submit();
		    } else {
		    	$("#dlSelect").val(oldData);
		    }
		});
	});
	
	$("#rtaeImg, #logoutImg, #igerivImg, #dlDivImg, #questionHelp, #forum, #blog, #fb, #finalizza").tooltip({
		delay: 0,  
	    showURL: false
	}); 
	
	if ($("#promo").length > 0) {
		$("#promo").tooltip({
			delay: 0,  
		    showURL: false,
		    bodyHandler: function() { 
		    	return "<center><b>" + msgPromoInvitaUnCollega + "</b><br>(" + msgCliccaPerSaperneDiPiu + ")</center>";
		    }
		}); 
		function pulsatePromo() { 
		    $("#promo"). 
		      animate({opacity: 0.2}, 1000, 'linear'). 
		      animate({opacity: 1}, 1000, 'linear', pulsatePromo); 
		}
		pulsatePromo();
	}
	
	$(".tableHeader, .tableHeaderSort").tooltip({
		delay: 0,  
	    showURL: false
	}); 
	
	$("img[src*='pdf.gif'],img[src*='xls.gif']").css({"cursor":"pointer"}).tooltip({
		delay: 0,  
	    showURL: false
	}); 
	
	setFocusedFieldStyle();	
	
	$(function () {
		$("#thumbBox").draggable();
		enableDraggingOnPopup($("#popup_name"));
		enableDraggingOnPopup($("#popup_name_det"));
		enableDraggingOnPopup($("#popup_name_dettaglio_profilo"));
	});	
	
	if (checkedEmail) { 
		if (userType == 1 && !hasProfiloStarter) {
			jAlert(msgEmailValidato, msgAvviso);
		} else if (userType == 2 || (userType == 1 && hasProfiloStarter)) {
			jAlert(msgEmailValidatoCliente, msgAvviso);
		}
	}
	
	$(".numericOnly").keypress(function (event) {
		var code = (event.keyCode ? event.keyCode : event.charCode);
	    var match = String.fromCharCode(code).match(/[^0-9]/g);
		var backspaceTabEnterDel = (code == 8 || code == 9 || code == 13 || code == 46);
	    if (match && !backspaceTabEnterDel) {
	    	return false;
	    }
	});
});

function enableDraggingOnPopup($obj) {
	$obj.draggable({cursor: 'move', cancel: "div[class*=Scroll], #mainDiv, #contentDiv, #pubMainDiv, #contentScrollableDiv"});
}

function onLoadFunction() {}

function setFocus(fieldId) {
	if (document.getElementById(fieldId)) {
		document.getElementById(fieldId).focus();
	}
}

function appendStringToTableTile(str) {
	var e = document.getElementById("tableTitleSpan");
	if (e && e.innerHTML.indexOf(str) == -1) {
		e.innerHTML = e.innerHTML + "&nbsp;&nbsp;" + str;
	}
}

function setContentDivHeight(additionalHeight) {
	var top = 0;  
	$("#content1").find("form").each(function() {
		top += $(this).height();
	}); 
	if (typeof( additionalHeight ) != "undefined") {
		top = (top == 0) ? (570 - $("#filter").height()) : (top + additionalHeight);
	} else {
		top = (top == 0) ? (570 - $("#filter").height()) : top;
	}
	$("#content1").css({"height": top + "px"});	
}