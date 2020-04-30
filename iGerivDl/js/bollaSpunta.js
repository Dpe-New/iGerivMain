$(document).ready(function() {
	if (conditionItensBolla) {
		setContentDivHeight(135);
		var container = $("#messageDiv");
		if (conditionDisableForm) {
			disableAllFormFields('dataTipoBolla');			
			var newdiv = document.createElement("div");
			newdiv.style.color = "red";
			newdiv.style.width = "180px";
			newdiv.style.borderStyle = "solid";
			newdiv.style.border = "1px solid red";
			newdiv.style.textAlign = "center";
			newdiv.innerHTML = "<b>" + msgBollaGiaInviata;				
			container.css({"width":"180px"});
			if (container.find("div")[0]) {
				y = container.find("div")[0];
				container.replace(newdiv, y);
			} else {
				container.append(newdiv);
			}
		} else {
			container.css({"border":"none"});
			container.text("");
		}
		
		$.alt('X', function() {
			setModalita(MODALITA_RICERCA);
		});
		
		if ($('#barcode').length > 0) {
			$('#barcode').keydown(manageBarcodeSpuntaKeyDown);
		}
		
		$('#imgClients, #findPub').tooltip({
            delay: 0,
            showURL: false
        });
		
		var table = document.getElementById("BollaControlloTab_table");
		for (var i = 0, row; row = table.rows[i]; i ++) {
			var titolo = null; 
			var cpu = null;
	        for (var j = 0, cell; cell = row.cells[j]; j ++) {
	            var index = cell.cellIndex;
	            switch (index) {
			          case 1:
		          		  cpu = $(cell).text();
		          		  break;
	            	  case 2:
	            		  titolo = $(cell).text();
	            		  $(cell).css({"cursor":"pointer"});
	                	  $(cell).click(function() {
	                		  var $rw = $(this).parent();
	                		  var idtn = $rw.attr('idtn');
	              			  var pk = $rw.find("td:nth-child(10)").find("input:hidden[name='pk']").first().val();
	                		  addResultRow(idtn, pk, true);
	                		  $("#qta").val(1);
	                	  });
	            		  break;
	            	  case 6:
	            		  $(cell).click(function() {
	                          var link = $(this).find("a");
	                          if (link.length > 0 && link.attr("href").length > 0) {
	                        	  var cpu = $(this).closest("tr").find("td:nth-child(2)").first().text();
	                              openStatistica(link.attr("href"), cpu);
	                          }
	                      });
	            		  $(cell).tooltip({
	                          delay: 0,
	                          showURL: false,
	                          bodyHandler: function() { 
	              		    	return "<b>" + msgVisualizzaStatistiche + "</b>";
	              		      }
	                      });
	            		  break;
	            	  case 9: 
	            		  $(cell).find("input:text").first().change(function() {
	            			  setDifferenze($(this));
	            		  });
	            		  break;
	                  case 10: {
	                	  var $iv = $(row).attr('iv');
	                	  var $pv = $(row).attr('pv');
	                	  var $idtn = $(row).attr('idtn');
	                	  var $resp = $(row).attr('resp');
	                	  var $fb = $(row).attr('fb');
	                	  if (($idtn && $idtn != 'undefined') || ($iv && $iv > 0) || ($pv && $pv != ' ') || ($resp && $resp != 'undefined')) {
		                	  var str = '<span style="float:left; white-space:nowrap; overflow:hidden">';
		                	  if (((typeof($fb) == 'undefined') || $fb != 'true') && $idtn && $idtn != 'undefined') {
		                		  var hasNoteCpu = $("#noteRivenditaCpu" + cpu).length > 0 && $("#noteRivenditaCpu" + cpu).val().trim().length > 0;
		                		  var imgName = "note_rivendita" + (($("#noteRivendita" + $idtn).val().trim().length > 0 || hasNoteCpu) ? "_red" : "") + ".gif";
		                		  var titleMsg = getNoteRivendita($("#noteRivendita" + $idtn).val(), $("#noteRivenditaCpu" + cpu).val());
		                		  str += '<img name="noteRiv" id="noteRiv' + $idtn + '" src="/app_img/' + imgName + '" border="0px" style="cursor:pointer; border-style: none" onclick="javascript: addNote(\'' + titolo.replace(new RegExp('\'', 'g'),'\\\'') + '\',\'' + $idtn + '\',\'' + cpu + '\')" title="' + titleMsg + '" alt="' + msgNoteRivendita + '" />&nbsp;';
		                	  }
		                	  if (($iv && $iv > 0) || ($pv && $pv != ' ') || ($resp == 'true')) {
			                      if ($iv && $iv == 1) {
			                          str += '<img src="/app_img/non_valorizzare.jpg" border="0px" style="border-style: none" title="' + msgNonValorizzare + '" alt="' + msgNonValorizzare + '" />&nbsp;';
			                      } else if ($iv && $iv == 2) {
			                    	  var $dpa = (typeof($(row).attr('dpa')) != 'undefined' && $(row).attr('dpa').length > 0) ? "&nbsp;<span style='font-size:80%'>(" + msgDataPresuntoAddebito + ": " + $(row).attr('dpa') + ")</span>": "";
			                          str += '<img src="/app_img/conto_deposito.gif" border="0px" style="border-style: none" title="' + msgContoDeposito + $dpa + '" alt="' + msgContoDeposito + $dpa + '" />&nbsp;';
			                      }
			                      if ($pv && $pv != ' ') {
			                          str += '<img src="/app_img/cambio_prezzo.jpg" border="0px" style="border-style: none" title="' + msgPrezzoVariato + '" alt="' + msgPrezzoVariato + '" />&nbsp;';
			                      }
			                      if ($resp == 'true') {
			                          str += '<img src="/app_img/respingere.png" border="0px" style="border-style: none" title="' + msgDaRespingereInResa + '" alt="' + msgDaRespingereInResa + '" />';
			                      }
		                	  }
		                	  if ((typeof($fb) != 'undefined') && $fb == 'true') {
		                		  str += $(cell).text();
							  }
		                	  str += '</span>';
		                	  $(cell).html(str);
	                	  }
	                      $(cell).find("img").tooltip({
	                          delay: 0,
	                          showURL: false
	                      });
	                      break;
	                  }
	                  case 11: {
	                      $(cell).click(function() {
	                          var link = $(this).find("a");
	                          if (link.length > 0 && link.attr("href").length > 0) {
	                              openOrdini(link.attr("href"));
	                          }
	                      });
	                      break;
	                  }
	                  case 12: {
	                	  break;
	                  }
	                  default: {
	                	  $(cell).css({"cursor":"pointer"});
	                	  $(cell).click(function() {
	                		  var $rw = $(this).parent();
	                		  var idtn = $rw.attr('idtn');
	              			  var pk = $rw.find("td:nth-child(10)").find("input:hidden[name='pk']").first().val();
	                		  addResultRow(idtn, pk, true);
	                		  $("#qta").val(1);
	                	  });
	                	  break;
	                  }
	            }
	        }
		}
		addFadeLayerEvents();
		setLastFocusedElement();
		$(this).bind('keydown', bollaRivKeyPress);
	}
	setDataTipoBolla(dataTipoBolla, "dataTipoBolla");
	$("#dataTipoBolla").click(function() {
		var old = $(this).val();
		$(this).data('oldData', old);
	}).change(function() {
		var oldData = $(this).data('oldData');
		var val = $(this).val();
		var bollaNonModificabile = val.split("|")[3];
		if (typeof(bollaNonModificabile) !== 'undefined' && bollaNonModificabile != '' && bollaNonModificabile == 'true') {
			$.alerts.dialogClass = "style_1";
			jAlert(msgBollaNonModificabile, attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
				$("#filterForm").submit();
			});
		} else {
			if (typeof(oldData) !== 'undefined' && oldData != '') {
				$.alerts.dialogClass = "style_1";
				PlaySound('beep3');
				jConfirm(msgCambioBolla, attenzioneMsg, function(r) {
					$.alerts.dialogClass = null;
				    if (r) { 
				    	$("#filterForm").submit();
				    } else {
				    	$("#dataTipoBolla").val(oldData);
				    }
				});
			} else {
				$("#filterForm").submit();
			}
		}
	});
	$('#barcode').focus();
});

function setDifferenze($field) {
	 var $td = $field.parent();
	 var confermaDiff = $field.val() == '' ? 0 : $field.val();
	 var $tdCopieLette = $td.prev("td").prev("td");
	 var $tdDiff = $td.prev("td");
	 var copieBolla = $tdDiff.prev("td").prev("td").text().trim() == '' ? 0 : Number($tdDiff.prev("td").prev("td").text().trim());
	 $tdDiff.text(confermaDiff);
	 var diff = $tdDiff.text().trim() == '' ? 0 : Number($tdDiff.text().trim());
	 $tdCopieLette.text(Number(copieBolla) + Number(diff));
	 return true;
}

function openStatistica(href, cpu) {
	var popID = 'popup_name'; 
    var popURL = href;
 	var popWidth = 900;
 	var popHeight = 380;
 	var query= popURL.split('?');
    var dim= query[1].split('&');	   	   	    	  		  
    var idtn = dim[0].split('=')[1];
    var periodicita = dim[1].split('=')[1];
    var url = appContext + "/statisticaPubblicazioni_showNumeriPubblicazioniStatistica.action?codPubblicazione=" + cpu + "&idtn=" + idtn + "&periodicita=" + periodicita;
	openDiv(popID, popWidth, popHeight, url);
	return false;
}

function bollaRivKeyPress(event) {
	var keycode = (event.keyCode ? event.keyCode : event.charCode);
	if ((!event.shiftKey && keycode == '9') || keycode == '40') {	// tab	|| arrow down	
		setTimeout(function() {var id = lastFocusedFieldId.replace(/([|])/g,'\\\$1'); var field1 = $("#" + id); setDifferenze(field1); var field = field1.closest("tr").next().find("input:text[name='differenze']").first(); $("#" + id).css('background','#fff'); field.focus(); field.select();}, 100);
	} else if ((event.shiftKey && keycode == '9') || keycode == '38') {	// shift + tab	|| arrow up
		setTimeout(function() {var id = lastFocusedFieldId.replace(/([|])/g,'\\\$1'); var field1 = $("#" + id); setDifferenze(field1); var field = field1.closest("tr").prev().find("input:text[name='differenze']").first(); $("#" + id).css('background','#fff'); field.focus(); field.select();}, 100);
	} else if (keycode == 37) { // left arrow
		event.preventDefault();
		$("#qta").select();
		$("#qta").focus();
		$("#barcode").css('background','#fff');
	} else if (keycode == 39) { // right arrow
		event.preventDefault();
		$("#barcode").select();
		$("#barcode").focus();
		$("#qta").css('background','#fff');
	} else if (keycode == 107 || keycode == 171 || keycode == 187) { // +
		event.preventDefault();
		var val = isNaN($("#qta").val()) ? 0 : Number($("#qta").val());
		$("#qta").val(++val);
	} else if ((keycode == 109 || keycode == 173 || keycode == 189) && (lastFocusedFieldId.indexOf("differenze") == -1)) { // -
		event.preventDefault();
		var val = isNaN($("#qta").val()) ? 0 : Number($("#qta").val());
		$("#qta").val(--val);
	}
}

function openOrdini(href) {
	var popID = 'popup_name'; 
    var popURL = href;
 	var popWidth = 900;
 	var popHeight = 380;
 	var query= popURL.split('?');
    var dim= query[1].split('&');	   	   	    	  		  
    var idtn = dim[0].split('=')[1];
 	var url = appContext + "/bollaRivenditaSpunta_showOrdini.action?idtn=" + idtn;
	openDiv(popID, popWidth, popHeight, url);
	return false;
}

function nessunaPubblicazioneAction() {
	$("#barcode").val('');
	return false;
}

function noBarcodeFoundAction(fieldVal) {
	$.alerts.dialogClass = "style_1";
	jAlert(nessunaPubblicazione, attenzioneMsg.toUpperCase(), function() {
		$.alerts.dialogClass = null;
		nessunaPubblicazioneAction();
	});
	return false;
}

function esportaOrdini() {
	var j = 0;
	var arrIdtn = new Array();
	$('#totalHeader_1').text($('#BollaControlloTab_table tbody tr td:nth-child(16)').each(function(i) {
		var $td = $(this);
		var $tdDiff = $(this).prev().prev().find("input:text[name^='differenzeFondoBolla'");
		var ordini = $td.text().trim();
		if (ordini && ordini.length > 0 && $tdDiff.length == 0) {
			var link = $td.find("a");
			if (link.length > 0 && link.attr("href").length > 0) {
				var popURL = link.attr("href");
				var query= popURL.split('?');
			    var dim= query[1].split('&');	   	   	    	  		  
			    var idtn = dim[0].split('=')[1];
			    arrIdtn[j++] = idtn;
            }
		}
	}));
	if (arrIdtn.length > 0) {
		$("#ReportOrdiniClientiForm").empty();
		$("#ReportOrdiniClientiForm").append('<input type="hidden" name="dataTipoBolla" value="' + $("#dataTipoBolla").val() + '"/>');
		$("#ReportOrdiniClientiForm").append('<input type="hidden" name="idtnOrdini" value="' + arrIdtn + '"/>');
		$("#ReportOrdiniClientiForm").attr("target","_blank"); 
		$("#ReportOrdiniClientiForm").submit();
		unBlockUI();
	}
}

function reloadDataTipoBolle() {
	dojo.xhrGet({
		url: '${pageContext.request.contextPath}/pubblicazioniRpc_getDateTipoBolla.action',			
		handleAs: "json",				
		headers: { "Content-Type": "application/json; charset=uft-8"}, 	
		preventCache: true,
		handle: function(data,args) {	
			data = JSON.parse(data);
			var list = '';		
            for (var i = 0; i < data.length; i++) {
            	var strChecked = '';
           		var dataTipoBollaStatus = data[i].dataBollaFormat + '|' + data[i].tipoBolla + '|' + data[i].readonly + '|';
           		if (dataTipoBolla == dataTipoBollaStatus) {
           			strChecked = "selected";
           		}
				if (Number(data[i].bollaTrasmessaDl) >= 2) {
					list += '<option ' + strChecked + ' style="color:red" value="' + dataTipoBollaStatus + '">' + data[i].dataBollaFormat + '&nbsp;&nbsp;' + data[i].tipoBolla + '&nbsp;(' + msgBollaTrasmessa + ')</option>';
				} else if (Number(data[i].bollaTrasmessaDl) == 1) {
					list += '<option ' + strChecked + ' style="color:blue" value="' + dataTipoBollaStatus + '">' + data[i].dataBollaFormat + '&nbsp;&nbsp;' + data[i].tipoBolla + '&nbsp;(' + msgBollaInTrasmissione + ')</option>';
				} else if (Number(data[i].bollaTrasmessaDl) == 0) {
					list += '<option ' + strChecked + ' value="' + dataTipoBollaStatus + '">' + data[i].dataBollaFormat + '&nbsp;&nbsp;' + data[i].tipoBolla + '&nbsp;(' + msgBollaNonTrasmessa + ')</option>';
           		} else {
           			list += '<option ' + strChecked + ' value="' + dataTipoBollaStatus + '">' + data[i].dataBollaFormat + '&nbsp;&nbsp;' + data[i].tipoBolla + '</option>';
           		}
            }
            $("#dataTipoBolla").empty();
            $("#dataTipoBolla").html(list);
		}
    });
}

function manageBarcodeSpuntaKeyDown(evt) {
	var keycode = (evt.keyCode ? evt.keyCode : evt.charCode);
	if (keycode == '13') {				
		var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
		evt.preventDefault();		
		var fieldVal = node.value;	
		var bcodeRegexp = /(?:^\d{5,9}$)|(?:^\d{11,18}$)/;
		var priceRegexp = /(?:^[\d]{1,4}){1}(?:[\\.][\d]{1,2})?$/;
		var titleRegexp = /^[%a-zA-Z]{1}.*$/;
		fieldVal = fieldVal.replace(/([,])/g,'.');
		if (bcodeRegexp.test(fieldVal)) {
			manageBarcodeSearch(fieldVal);
		} else if (priceRegexp.test(fieldVal)) {
			managePriceSearch(fieldVal);
		} else if (titleRegexp.test(fieldVal)) {
			manageTitleSearch(fieldVal);
		}
	} else if (keycode == '27') {
		setModalita(MODALITA_DEFAULT);
	}
}

function manageBarcodeSearch(fieldVal) {
	if ($("#" + fieldVal).length == 0) {
		setTimeout(function() {
			$.alerts.dialogClass = "style_1";
			jAlert(pubNonInBollaMsg, attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
				$("#barcode").val("");
				$("#barcode").focus();
			});
		}, 100);
	} else {
		var pk = $("#" + fieldVal).val();	
		if (pk.length > 0) {
			var idtn = $("#idtn_" + $("#barcode").val()).val();
			addSpunta(idtn, pk);
		}
	}
}

function managePriceSearch(fieldVal) {
	var num = parseLocalNum(fieldVal);
	var str = '[';
	$("#BollaControlloTab_table tbody tr td:nth-child(6)").each(function() {
		var price = parseLocalNum($(this).text().trim());
		if (Number(price) == Number(num)) {
			var row = $(this).parent();
			var idtn = row.attr("idtn");
			var tit = row.find("td:nth-child(3)").text();
			var pri = row.find("td:nth-child(6)").text();
			var pk = row.find("td:nth-child(10)").find("input[name='pk']").first().val();
			str += '{"label":"' + tit + ' (' + pri + ')","value":"' + tit + '","idtn":"' + idtn + '","pk":"' + pk + '"},';
		}
	});
	if (str.endsWith(",")) {
		str = str.substring(0, str.length - 1);
	}
	str += "]";
	$("input#barcode").autocomplete({
		source: jQuery.parseJSON(str),
		preventCache: true,
		select: function (event, ui) {			
			var idtn = ui.item.idtn;
			var pk = ui.item.pk;
			addSpunta(idtn, pk);
		}
	});
	e = jQuery.Event("keydown");
	e.which = 13;
	$("#barcode").trigger(e);
}

function manageTitleSearch(fieldVal) {
	var str = '[';
	$("#BollaControlloTab_table tbody tr td:nth-child(3)").each(function() {
		var tit = $(this).text();
		if (tit.toUpperCase().startsWith(fieldVal.toUpperCase())) {
			var row = $(this).parent();
			var idtn = row.attr("idtn");
			var pk = row.find("td:nth-child(10)").find("input[name='pk']").first().val();
			str += '{"label":"' + tit + '","value":"' + tit + '","idtn":"' + idtn + '","pk":"' + pk + '"},';
		}
	});
	if (str.endsWith(",")) {
		str = str.substring(0, str.length - 1);
	}
	str += "]";
	$("input#barcode").autocomplete({	
		source: jQuery.parseJSON(str),
		select: function (event, ui) {			
			var idtn = ui.item.idtn;
			var pk = ui.item.pk;
			addSpunta(idtn, pk);
		} 
	});
	e = jQuery.Event("keydown");
	e.which = 13;
	$("#barcode").trigger(e);
}

function addSpunta(idtn, pk) {
	addResultRow(idtn, pk, false);
	setTimeout(function() {
		$("#qta").val(1);
		$("#barcode").val("");
		setModalita(MODALITA_DEFAULT);
	}, 100);
}

function addResultRow(idtn, pk, skipCalc) {
	var skip = modalita == MODALITA_RICERCA ? true : skipCalc;
	var row = $("#BollaControlloTab_table tbody tr[idtn='" + idtn + "']").first();
	var cpu = row.find("td:nth-child(2)").text();
	var titolo = row.find("td:nth-child(3)").text();
	var sottotitolo = row.find("td:nth-child(4)").text();
	var numero = row.find("td:nth-child(5)").text();
	var prezzo = row.find("td:nth-child(6)").text();
	var copieBolla = isNaN(row.find("td:nth-child(7)").text()) ? 0 : row.find("td:nth-child(7)").text();
	var copieLettePrec = isNaN(row.find("td:nth-child(8)").text()) ? 0 : row.find("td:nth-child(8)").text();
	var qta = $("#qta").val();
	var copieLette = Number(copieLettePrec) + Number(qta);
	var differenze = Number(copieLette) - Number(copieBolla);
	var html = '<table cellspacing="0" cellpadding="0" border="0" width="100%" style="table-layout: fixed;" class="extremeTableFieldsVeryLarge" id="BollaControlloTab_table_head"><thead><tr><td width="8%" class="tableHeader" style="text-align:center">' + msgCpu + '</td><td width="30%" class="tableHeader" style="cursor: default;">' + msgTitolo + '</td><td width="22%" class="tableHeader">' + msgSottotitolo + '</td><td width="8%" class="tableHeader" style="text-align:center">' + msgNumero + '</td><td width="8%" class="tableHeader" style="text-align:right">' + msgPrezzoLordo + '</td><td width="8%" class="tableHeader" style="text-align:center">' + msgCopieBolla + '</td><td width="8%" class="tableHeader" style="text-align:center">' + msgCopieLette + '</td><td width="8%" class="tableHeader" style="text-align:center">' + msgDifferenze + '</td></tr></thead>';
	html += '<tbody><tr><td width="8%" style="text-align: center;">' + cpu + '</td><td width="30%">' + titolo + '</td><td width="22%">' + sottotitolo + '</td><td width="8%" style="text-align: center;">' + numero + '</td><td width="8%" style="text-align: right;">' + prezzo + '</td><td width="8%" style="text-align:center">' + copieBolla + '</td><td width="8%" style="text-align:center">' + copieLette + '</td><td width="8%" style="text-align:center">' + differenze + '</td></tr></tbody></table>';
	$("#spuntaOutput").html(html);
	var textId = pk.replace(/([|])/g,'\\\$1');
	var txtCopie = $("#differenze" + textId);
	var $tdDifferenze = txtCopie.parent().prev("td");
	var $tdCopieLette = $tdDifferenze.prev("td");
	if (!skip) {
		$tdCopieLette.text(copieLette);
		$tdDifferenze.text(copieLette - copieBolla);
	}
	if (typeof($lastTr) != 'undefined') {
		$lastTr.contents('td').css({'border-top': 'none','border-bottom': 'none','border-left': 'none', 'border-right': 'none'});
		$lastTr.contents('td:first').css('border-left', 'none');
		$lastTr.contents('td:last').css('border-right', 'none');
	}
	var $tr = $tdCopieLette.parent();
	$tr.contents('td').css({'border-top': '1px solid red','border-bottom': '1px solid red','border-left': 'none', 'border-right': 'none'});
	$tr.contents('td:first').css('border-left', '1px solid red');
	$tr.contents('td:last').css('border-right', '1px solid red');
	var element = document.getElementById("differenze" + pk);
	element.scrollIntoView(false);
	$lastTr = $tr;
}

function validateFields() {
	var table = document.getElementById("BollaControlloTab_table");
	var arrSpunte = new Array();
	for (var i = 0, row; row = table.rows[i]; i ++) {
		if (i > 0) {
	        for (var j = 0, cell; cell = row.cells[j]; j ++) {
	            var index = cell.cellIndex;
	            if (index == 7) {
	            	arrSpunte.push($(cell).text());
	            } else if (index == 8) {
	            	var $tdDiff = $(cell);
	            	var diff = $tdDiff.text().trim();
	            	var $inputDiff = $tdDiff.next("td").find("input:text").first();
	    			var diffConf = $inputDiff.val();
	    			if (diff != '' && diffConf == '') {
	    				$.alerts.dialogClass = "style_1";
	    				jAlert(msgDifferenzeNonConfermate, attenzioneMsg.toUpperCase(), function() {
	    					$.alerts.dialogClass = null;
	    					$inputDiff.focus();
	    				});
	    				unBlockUI();
	    				return false;
	    			} else if (diffConf != '' && isNaN(diffConf)) {
	    				$.alerts.dialogClass = "style_1";
	    				jAlert(msgCampoNumerico.replace('{0}', msgConfermaDifferenze), attenzioneMsg.toUpperCase(), function() {
	    					$.alerts.dialogClass = null;
	    					$inputDiff.focus();
	    				});
	    				unBlockUI();
	    				return false;
	    			}
	            }
			}
		}
	}
	$("#spunte").val(arrSpunte);
	return true;
}

function copiaDifferenze(but) {
	jConfirm(msgConfirmCopiaDifferenze, attenzioneMsg, function(r) {
		$.alerts.dialogClass = null;
	    if (r) { 
	    	$("#BollaControlloTab_table tbody tr td:nth-child(9)").each(function() {
	    		var $td = $(this);
	    		var $tdCopieLette = $td.prev("td");
	    		var copieBolla = $tdCopieLette.prev("td").text().trim() == '' ? 0 : $tdCopieLette.prev("td").text().trim();
	    		var diff = $td.text().trim();
	    		if (diff != '') {
	    			$td.next("td").find("input:text").first().val(diff);
	    			$tdCopieLette.text(Number(copieBolla) + Number(diff));
	    		}
	    	});
	    }
	});
}

function setModalita(mod) {
	modalita = mod;
	switch(mod) {
		case MODALITA_RICERCA:
			$("#qta").attr("disabled", true);
			$("#barcode").css({"border-style":"solid","border-width":"3px","border-color":"#25C23F"});
			if ($("#spuntaOutput").is(":visible")) { 
				$("#spuntaOutput").css({"margin-top":"14px"});
			}
			break;
		default:
			$("#qta").val(1);
			$("#qta").attr("disabled", false);
			$("#barcode").css({"border-style":"none"});
			if ($("#spuntaOutput").is(":visible")) { 
				$("#spuntaOutput").css({"margin-top":"18px"});
			}
			break;
	}
	$("#barcode").focus();
}