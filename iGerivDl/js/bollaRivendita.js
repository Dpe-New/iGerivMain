setDataTipoBolla(dataTipoBolla, "dataTipoBolla");
if (conditionItensBolla) {
	$(document).ready(function() {
		$("#footer").hide();
		var container = $("#messageDiv");
		if (conditionDisableForm) {
			disableBollaConsegna();
		} else {
			container.css({"border":"none"});
			container.text("");
		}
		$('#totalHeader_1').text($('#BollaControlloTab_table tr.calcRow td:nth-child(11)').text());
		$('#totalHeader_2').text($('#importoLordo').val());
		$('#totalHeader_3').text(totaleBollaFormat);
		$('#totalHeader_4').text(totaleFondoBollaFormat);
		$('#totalHeader_5').text(totaleBollaLordoFormat);
		$('#totalHeader_6').text(totaleFondoBollaLordoFormat);
		
		$('#imgClients, #imgClientsDetail').tooltip({
            delay: 0,
            showURL: false
        });
		var table = document.getElementById("BollaControlloTab_table");
		for (var i = 0, row; row = table.rows[i]; i ++) {
			if (i > 0) {
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
		            		  $(cell).click(function() {openLayer($(this));});
		            		  break;
		            	  case 9:
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
		                  case 11:
		                  case 12:
		                	  break;	
		                  case 13: {
		                	  var $iv = $(row).attr('iv');
		                	  var $pv = $(row).attr('pv');
		                	  var $idtn = $(row).attr('idtn');
		                	  var $resp = $(row).attr('resp');
		                	  var $fb = $(row).attr('fb');
		                	  var $qfdv = $(row).attr('qfdv');
		                	  var hasQfdv = (typeof($qfdv) != 'undefined') && $qfdv != '';
		                	  var $cddif = $(row).attr('cddif');
		                	  var hasCddif = (typeof($cddif) != 'undefined') && $cddif != '';
		                	  var $scdif = $(row).attr('scdif');
		                	  var hasScddif = (typeof($scdif) != 'undefined') && $scdif != '';
		                	  if (($idtn && $idtn != 'undefined') || ($iv && $iv > 0) || ($pv && $pv != ' ') || ($resp && $resp != 'undefined')) {
			                	  var str = '<span style="float:left; white-space:nowrap; overflow:hidden">';
			                	  if (((typeof($fb) == 'undefined') || $fb != 'true') && $idtn && $idtn != 'undefined') {
			                		  var noteRivenditaCpuVal = $("#noteRivenditaCpu" + cpu).val();
			                		  var hasNoteCpu = typeof(noteRivenditaCpuVal) != 'undefined' ? noteRivenditaCpuVal.trim().length > 0 : false;
			                		  var noteRivenditaVal = $("#noteRivendita" + $idtn).val();
			                		  var imgName = "note_rivendita" + ((noteRivenditaVal.trim().length > 0 || hasNoteCpu) ? "_red" : "") + ".gif";
			                		  var titleMsg = getNoteRivendita(noteRivenditaVal, noteRivenditaCpuVal);
			                		  str += '&nbsp;<img name="noteRiv" id="noteRiv' + $idtn + '" src="/app_img/' + imgName + '" border="0px" style="border-style: none" onclick="javascript: addNote(\'' + titolo.replace(new RegExp('\'', 'g'),'\\\'') + '\',\'' + $idtn + '\',\'' + cpu + '\')" title="' + titleMsg + '" alt="' + msgNoteRivendita + '" />';
			                	  }
			                	  if (($iv && $iv > 0) || ($pv && $pv != ' ') || ($resp == 'true')) {
				                      if ($iv && $iv == 1) {
				                          str += '<img src="/app_img/non_valorizzare.jpg" border="0px" style="border-style: none" title="' + msgNonValorizzare + '" alt="' + msgNonValorizzare + '" />';
				                      } else if ($iv && $iv == 2) {
				                    	  var $dpa = (typeof($(row).attr('dpa')) != 'undefined' && $(row).attr('dpa').length > 0) ? "&nbsp;<span style='font-size:80%'>(" + msgDataPresuntoAddebito + ": " + $(row).attr('dpa') + ")</span>": "";
				                          str += '&nbsp;<img src="/app_img/conto_deposito.gif" border="0px" style="border-style: none" title="' + msgContoDeposito + $dpa + '" alt="' + msgContoDeposito + $dpa + '" />';
				                      }
				                      if ($pv && $pv != ' ') {
				                          str += '&nbsp;<img src="/app_img/cambio_prezzo.jpg" border="0px" style="border-style: none" title="' + msgPrezzoVariato + '" alt="' + msgPrezzoVariato + '" />';
				                      }
				                      if ($resp == 'true') {
				                          str += '&nbsp;<img src="/app_img/respingere.png" border="0px" style="border-style: none" title="' + msgDaRespingereInResa + '" alt="' + msgDaRespingereInResa + '" />';
				                      }
			                	  }
			                	  if (hasQfdv || hasCddif || hasScddif) {
			                		  var title = "";
			                		  if (hasCddif) {
			                			  var arrCddif = $cddif.split("|");
			                			  if (arrCddif[0].toString() == 'true') {
			                				  title = msgPubblicazioneCDInBollaNonInforete.replace('{0}', titolo);
			                			  } else {
			                				  title = msgPubblicazioneCDInforeteNonInBolla.replace('{0}', titolo);
			                			  }
			                		  } else if (hasScddif) {
			                			  var arrScddif = $scdif.split("|");
			                			  title = msgScontoDiversoInforete.replace('{0}', titolo).replace('{1}',arrScddif[0]).replace('{2}',arrScddif[1]);
			                		  } else if (hasQfdv) {
			                			  title = msgFatturatoInContoDepositoDiversoDaVenduto;
			                		  }
			                		  str += $(cell).text() + '&nbsp;<img src="/app_img/important_small.png" name="attImg" width="17px" height="17px" border="0px" style="border-style: none" title="' + title + '" alt="' + title + '" />';
								  } else {
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
		                  case 14: {
		                      $(cell).click(function() {
		                          var link = $(this).find("a");
		                          if (link.length > 0 && link.attr("href").length > 0) {
		                              openOrdini(link.attr("href"));
		                          }
		                      });
		                      break;
		                  }
		                  case 15:
		                	  	break;
		                  default: {
		                      $(cell).click(function() {openLayer($(this));});
		                  }
		            }
		        }
			}
		}
		addFadeLayerEvents();
		setLastFocusedElement();
		$(this).bind('keydown', bollaRivKeyPress);
		if ($('#barcode').length > 0) { 
			$('#barcode').keydown(manageBarcodeKeyDown);
		}
		var imgsQfdv = $("#BollaControlloTab_table img[name='attImg']");
		if (imgsQfdv.length > 0) {
			function pulsateImgs() {
				imgsQfdv.animate({opacity: 0.2}, 500, 'linear').animate({opacity: 1}, 500, 'linear', pulsateImgs); 
			} 
			pulsateImgs();
		}
		$('#barcode').focus();
	});
	
	function disableBollaConsegna() {
		disableAllFormFields('dataTipoBolla','soloRigheSpuntare','submitFilter','barcode','popup_ok');			
		var newdiv = document.createElement("div");
		newdiv.style.color = "red";
		newdiv.style.width = "180px";
		newdiv.style.borderStyle = "solid";
		newdiv.style.border = "1px solid red";
		newdiv.style.textAlign = "center";
		newdiv.innerHTML = "<b>" + msgBollaGiaInviata;	
		var container = $("#messageDiv");
		container.css({"width":"180px"});
		if (container.find("div")[0]) {
			y = container.find("div")[0];
			container.replace(newdiv, y);
		} else {
			container.append(newdiv);
		}
	}
	
	function bollaRivKeyPress(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);
		if ((!event.shiftKey && keycode == '9') || keycode == '40') {	// tab	|| arrow down	
			setTimeout(function() {var id = lastFocusedFieldId.replace(/([|])/g,'\\\$1'); var field = $("#" + id).closest("tr").next().find("input:text[name^='fieldMap']").first(); $("#" + id).css('background','#fff'); field.focus(); $("#" + id).trigger("blur");}, 100);
		} else if ((event.shiftKey && keycode == '9') || keycode == '38') {	// shift + tab	|| arrow up
			setTimeout(function() {var id = lastFocusedFieldId.replace(/([|])/g,'\\\$1'); var field = $("#" + id).closest("tr").prev().find("input:text[name^='fieldMap']").first(); $("#" + id).css('background','#fff'); field.focus(); $("#" + id).trigger("blur");}, 100);
		}
	}
	
	window.onbeforeunload = function () {
		if (attivaMemorizzazioneAutomatica && $("#BollaControlloForm input:hidden[name^=modificato][value=true]").length > 0) {
			validateFields('BollaControlloForm'); 
			validateFields(false) && setFieldsToSave(true) && setFormAction('BollaControlloForm','bollaRivendita_save.action', '', 'messageDiv', true, '', function() {setFieldsToSave(false);}, '', false);
			return msgConfirmSaveBolla;
		}
	};
	
	function openLayer(obj) {
        var popID = 'popup_name';
        var tr = obj.parent();
        var popURL = tr.attr('divParam');
        var popWidth = 900;
        var popHeight = 500;
        var query= popURL.split('?');
        var dim= query[1].split('&');
        var idtn = dim[0].split('=')[1];
        var periodicita = '';
        var coddl = '';
        if (dim.length > 1) {
            periodicita = dim[1].split('=')[1];
            coddl = dim[2].split('=')[1];
        }
        var pk = tr.find("td:nth-child(13)").find("input:text[name^='field']:first").attr("id").replace('differenze','');
        var url = appContext + "/sonoInoltreUscite_showRichiesteRifornimenti.action?idtn=" + idtn + "&pkSel=" + pk + "&coddl=" + coddl;
        if (periodicita != '') {
            url += "&periodicita=" + periodicita;
        }
        if (lastFocusedRow != '' && lastFocusColor != '') {
        	lastFocusedRow.css({"backgroundColor":lastFocusColor});
        }
        openDiv(popID, popWidth, popHeight, url);
        return false;
    }
	
	function openOrdini(href) {
		var popID = 'popup_name'; 
	    var popURL = href;
	 	var popWidth = 900;
	 	var popHeight = 380;
	 	var query= popURL.split('?');
	    var dim= query[1].split('&');	   	   	    	  		  
	    var idtn = dim[0].split('=')[1];
	 	var url = appContext + "/bollaRivendita_showOrdini.action?idtn=" + idtn;
		openDiv(popID, popWidth, popHeight, url);
		return false;
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
	
	var $fields = $('#BollaControlloForm input:text[name^=field]');
	
	$fields.numeric(false, null, function() {
		var $field = $(this);
		var $fSpunta = $field.next("input:hidden[name^=spunta]");
		var $fModificato = $fSpunta.next("input:hidden[name^=modificato]");
		if (Math.abs($field.val()) > 101) {
			PlaySound('beep3');
			jConfirm(differenzaMoltoGrandeConfirm.replace('{0}', $field.val()), attenzioneMsg.toUpperCase(), function(r) {
			    if (r) {
			    	$fModificato.val("true");
			    } else {
			    	setTimeout(function () { $field.focus(); }, 50);	
					$field.val('');
					return false;
			    }
			}, true, false);
			return false;
		}
		$fModificato.val("true");
	});
	
	$('#BollaControlloForm input:checkbox[name^=spunte]').change(function() {checkboxChanged($(this));});
	
	function checkboxChanged($checkField) {
		var isChecked = $checkField.attr("checked");
		var $fSpunta = $checkField.parent("td").next("td").find("input:hidden[name^=spunta]").first();
		var $fModificato = $fSpunta.next("input:hidden[name^=modificato]");
		$fSpunta.val(isChecked ? 1 : 0);
		$fModificato.val("true");
	}
	
	function setAllCheckboxChanged() {
		$('#BollaControlloForm input:checkbox[id^=dpeChkbx_]').each(function() {
			var $checkField = $(this);
			var $fSpunta = $checkField.parent("td").next("td").find("input:hidden[name^=spunta]").first();
			var $fModificato = $fSpunta.next("input:hidden[name^=modificato]");
			$fSpunta.val(1);
			$fModificato.val("true");
		});
		return false;
	}
	
	function setFieldsToSave(isSubmit) {
		$('#BollaControlloForm input:text[name^=field]').each(function() {
			var $field = $(this);
			var $fSpunta = $field.next("input:hidden[name^=spunta]");
			if (isSubmit) {
				$field.data('oldVal', $field.val());
				$fSpunta.data('oldVal', $fSpunta.val());
			} else {
				var $fModificato = $fSpunta.next("input:hidden[name^=modificato]");
				if ($field.data('oldVal') != $field.val() || $fSpunta.data('oldVal') != $fSpunta.val()) {
					$fModificato.val("true");
				} else {
					$fModificato.val("false");
				}
			}
		});
		return true;
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
	
	function esportaOrdini(tipoReportOrdiniClienti) {
		var dataBolla = $("#dataTipoBolla").val().split("|")[0];
		jConfirm(confirmEsportareClientiDiTutteLeBolle.replace('{0}', dataBolla), attenzioneMsg.toUpperCase(), function(r) {
			 try {
				if (!r) {
			    	if (fillFormEsportaOrdini() <= 0) {
			    		throw "NoOrdersException";
			    	}																
			    } else if (tipoReportOrdiniClienti == 2) {
			    	$("#ReportOrdiniClientiForm").append('<input type="hidden" name="esportaClientiDiTutteLeBolle" value="true"/>');
			    } else {
			    	fillFormEsportaOrdini();
			    	$("#ReportOrdiniClientiForm").append('<input type="hidden" name="esportaClientiDiTutteLeBolle" value="true"/>');
			    }
			    $("#ReportOrdiniClientiForm").append('<input type="hidden" name="tipoReportOrdiniClienti" value="' + tipoReportOrdiniClienti + '"/>');
			    $("#ReportOrdiniClientiForm").append('<input type="hidden" name="dataTipoBolla" value="' + $("#dataTipoBolla").val() + '"/>');
			    $("#ReportOrdiniClientiForm").submit();
			 } catch (e) {
				 if (e == "NoOrdersException") {
					 $.alerts.dialogClass = "style_1";
					jAlert(msgNessunOrdineClienteInBolla, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
					});
					return false;
				 }
			 }
			unBlockUI();
		}, true, true);
	}
	
	function fillFormEsportaOrdini() {
		var j = 0;
		var arrIdtn = new Array();
		$('#totalHeader_1').text($('#BollaControlloTab_table tbody tr td:nth-child(15)').each(function(i) {
			var $td = $(this);
			var $tdDiff = $(this).prev().prev().find("input:text[name^='fieldFBMap'");
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
			$("#ReportOrdiniClientiForm").append('<input type="hidden" name="idtnOrdini" value="' + arrIdtn + '"/>');
			$("#ReportOrdiniClientiForm").attr("target","_blank"); 
		} 
		return arrIdtn.length;
	}
	
	function reloadDataTipoBolle() {
		dojo.xhrGet({
			url: appContext + '/pubblicazioniRpc_getDateTipoBolla.action',			
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
            			if (data[i].readonly == true) {
            				disableBollaConsegna();
            			}
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
}