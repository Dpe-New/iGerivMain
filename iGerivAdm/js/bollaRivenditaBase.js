dojo.require("dojox.grid.DataGrid");
dojo.require("dojo.data.ItemFileWriteStore");
dojo.require("dojo.rpc.RpcService");
dojo.require("dojo.rpc.JsonService");	
dojo.require("dijit.Tree");
dojo.require("dijit.tree.ForestStoreModel");

var service = null;
var $fields = null;

setDataTipoBolla(dataTipoBolla, "dataTipoBolla");
if (conditionItensBolla) {
	$(document).ready(function() {
		
		$("#BollaControlloTab_table").thfloat();
		if (hasMessaggioDocumentoDisponibile) {
			var $op = $("#dataTipoBolla").find("option:selected");
			var drd = $op.attr("drd");
			var ord = $op.attr("ord");
			if (drd.length > 0 && ord.length > 0) {
				$("#docDisponibile").html($.validator.format(msgDocumentoDisponibile, [drd, ord]));
			}
		}
		var $msgDiv = $("#msgDiv");
		if ($msgDiv.length > 0) {
			var $toggleMsgs = $("#toggleMsgs");
			if (conditionMessaggiBolla) {
				$toggleMsgs.click(function() {
					$msgDiv.slideToggle("slow", function() {
						$(this).is(":visible") ? $toggleMsgs.attr("value", msgNascondiMessaggiBolla) : $toggleMsgs.attr("value", msgMostraMessaggiBolla);
					});
				});
			}
			if ($msgDiv.is(":visible") && !msgLetti) {
				$toggleMsgs.attr("value", msgNascondiMessaggiBolla);
			} else {
				$msgDiv.slideUp();
				$toggleMsgs.attr("value", msgMostraMessaggiBolla);
			}
		}
		$("#footer").hide();
		
		var container = $("#messageDiv");
		/*
		if (conditionDisableForm) {
			disableBollaConsegna();
		} else {
			container.css({"border":"none"});
			container.text("");
		}
		*/
		$('#totalHeader_1').text($('#BollaControlloTab_table tr.calcRow td:nth-child(10)').text());
		$('#totalHeader_2').text($('#importoLordo').val());
		$('#totalHeader_3').text(totaleBollaFormat);
		$('#totalHeader_4').text(totaleFondoBollaFormat);
		$('#totalHeader_5').text(totaleBollaLordoFormat);
		$('#totalHeader_6').text(totaleFondoBollaLordoFormat);
		
		$('#imgClients, #imgClientsDetail, #memorizza, #memorizzaInvia, .variazioneClass').tooltip({
            delay: 0,
            showURL: false
        });
		
		var table = document.getElementById("BollaControlloTab_table");
		for (var i = 0, row; row = table.rows[i]; i ++) {
			
			if (i > 0) {
				var titolo = null; 
				var cpu = null;
				var $row = $(row);
				var rigaAggiuntaFuoriCompetenza = (typeof($row.attr('agfc')) != 'undefined') && $row.attr('agfc') === 'true';
		        for (var j = 0, cell; cell = row.cells[j]; j ++) {
		            var index = cell.cellIndex;
		            var $cell = $(cell);
		            switch (index) {
		            	  case 1:
		            		  cpu = $cell.text();
		            		  break;
		            	  case 2:
		            		  //30-05-2014
		            		  titolo = $cell.contents(':not(span)').text()+' '+$cell.contents('span').text();
		            		  $cell.click(function() {openLayer($(this));});
		            		  break;
		            	  case 8:
		            		  var link = $cell.find("a");
		            		  if (link && link.length > 0) {
			            		  link.click(function() {
			                          if ($(this).length > 0 && $(this).attr("href").length > 0) {
			                        	  var $cpu = $(this).closest("tr").find("td:nth-child(2)").first().text();
			                              openStatistica($(this).attr("href"), $cpu);
			                          }
			                      });
			            		  link.tooltip({
			                          delay: 0,
			                          showURL: false,
			                          bodyHandler: function() { 
			              		    	return "<b>" + msgVisualizzaStatistiche + "</b>";
			              		      }
			                      });
		            		  }
		            		  break;
		                  case 10:
		                	  break;
		                  case 11:
		                	  var $pnu = $row.attr('pnu');
		                	  if ($pnu && $pnu === 'true') {
	              			  	$cell.append($("<div />")
		                		  	.css({
		              			        position: "relative"
		              			      , top: -32
		              			      , left: -3
		              			      , width: $cell.outerWidth()
		              			      , height: 30
		              			      , zIndex: 9999999
		              			      , backgroundColor: "#fff"
		              			      , opacity: 0
		              			    })
		              			    .click(function() {
		              			    	$.alerts.dialogClass = "style_1";
		              					jAlert("<b>" + msgPubbNonDisponibile + "</b>", attenzioneMsg.toUpperCase(), function() {
		              						$.alerts.dialogClass = null;
		              					}); 
	              			    	}));
	              			  	$cell.css({"height":"35px", "display": "block"});
		                	  }
		                	  break;	
		                  case 12: {
		                	  var $iv = $row.attr('iv');
		                	  var $pv = $row.attr('pv');
		                	  var $idtn = $row.attr('idtn');
		                	  var $resp = $row.attr('resp');
		                	  var $fb = $row.attr('fb');
		                	  var $barf = $row.attr('barf');
		                	  var $qfdv = $row.attr('qfdv');
		                	  var hasQfdv = (typeof($qfdv) != 'undefined') && $qfdv != '';
		                	  var $cddif = $row.attr('cddif');
		                	  var hasCddif = (typeof($cddif) != 'undefined') && $cddif != '';
		                	  var $scdif = $row.attr('scdif');
		                	  var hasScddif = (typeof($scdif) != 'undefined') && $scdif != '';
		                	  var $idMessaggioIdtn = $row.attr('idMessaggioIdtn');
		                	  var hasIdMessaggioIdtn = (typeof($idMessaggioIdtn) != 'undefined') && $idMessaggioIdtn != '';
		                	  
		                	  
		                	  if (($idtn && $idtn != 'undefined') || ($iv && $iv > 0) || ($pv && $pv != ' ') || ($resp && $resp != 'undefined')) {
			                	  var str = '<span style="float:left; white-space:nowrap; overflow:hidden">';
			                	  if (((typeof($fb) == 'undefined') || $fb != 'true') && $idtn && $idtn != 'undefined') {
			                		  var noteRivenditaCpuVal = $("#noteRivenditaCpu" + cpu).val();
			                		  var hasNoteCpu = typeof(noteRivenditaCpuVal) != 'undefined' ? noteRivenditaCpuVal.trim().length > 0 : false;
			                		  var noteRivenditaVal = $("#noteRivendita" + $idtn).val();
			                		  var imgName = "note_rivendita" + ((noteRivenditaVal.trim().length > 0 || hasNoteCpu) ? "_red" : "") + ".gif";
			                		  var titleMsg = getNoteRivendita(noteRivenditaVal, noteRivenditaCpuVal);
			                		  //30-05-2014 
			                		  var titoloSottotitolo = titolo.replace(new RegExp('\"', 'g'),'');
			                		  titoloSottotitolo = titoloSottotitolo.replace(new RegExp('\'', 'g'),'\\\'');
			                		  
			                		  str += '&nbsp;<img name="noteRiv" id="noteRiv' + $idtn + '" src="/app_img/' + imgName + '" border="0px" style="border-style: none" onclick="javascript: addNote(\'' + titoloSottotitolo + '\',\'' + $idtn + '\',\'' + cpu + '\')" title="' + titleMsg + '" alt="' + msgNoteRivendita + '" />';
			                	  }
			                	  if (($iv && $iv > 0) || ($pv && $pv != ' ') || ($resp == 'true')) {
				                      if ($iv && $iv == 1) {
				                          str += '<img src="/app_img/non_valorizzare.jpg" border="0px" style="border-style: none" title="' + msgNonValorizzare + '" alt="' + msgNonValorizzare + '" />';
				                      } else if ($iv && $iv == 2) {
				                    	  var $dpa = (typeof($row.attr('dpa')) != 'undefined' && $row.attr('dpa').length > 0) ? "&nbsp;<span style='font-size:80%'>(" + msgDataPresuntoAddebito + ": " + $row.attr('dpa') + ")</span>": "";
				                          str += '&nbsp;<img src="/app_img/conto_deposito.gif" border="0px" style="border-style: none" title="' + msgContoDeposito + $dpa + '" alt="' + msgContoDeposito + $dpa + '" />';
				                      }
				                      if ($pv && $pv != ' ') {
				                          str += '&nbsp;<img src="/app_img/cambio_prezzo.jpg" border="0px" style="border-style: none" title="' + msgPrezzoVariato + '" alt="' + msgPrezzoVariato + '" />';
				                      }
				                      if ($resp == 'true') {
				                          str += '&nbsp;<img src="/app_img/respingere.png" border="0px" style="border-style: none" title="' + msgDaRespingereInResa + '" alt="' + msgDaRespingereInResa + '" />';
				                      }
			                	  }
			                	  if (hasIdMessaggioIdtn) {
			                		  str += '&nbsp;<img src="/app_img/avviso_copertina.jpg" name="avviso_copertina" border="0px" style="border-style: none" title="' + msgCopertina + '" alt="' + msgCopertina + '" onclick="javascript: showMessaggioIdtn(' + $idMessaggioIdtn + ');" />';
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
			                		  str += $cell.text() + '&nbsp;<img src="/app_img/important_small.png" name="attImg" width="17px" height="17px" border="0px" style="border-style: none" title="' + title + '" alt="' + title + '" />';
								  } else {
									  str += $cell.text();
								  }
			                	  if ($barf && $barf === 'true') {
			                		  str += '&nbsp;<img src="/app_img/avviso_barcode_fittizio.png" name="avviso_barcode_fittizio" border="0px" style="border-style: none" title="' + msgBarcodeFittizio + '" alt="' + msgBarcodeFittizio + '"/>';
			                	  }
			                	  str += '</span>';
			                	  $cell.html(str);
		                	  }
		                      $cell.find("img").tooltip({
		                          delay: 0,
		                          showURL: false
		                      });
		                      break;
		                  }
		                  case 13: {
		                      $cell.click(function() {
		                          var link = $(this).find("a");
		                          if (link.length > 0 && link.attr("href").length > 0) {
		                        	  var query= link.attr("href").split('?');
		                        	  var dim= query[1].split('&');	   	   	    	  		  
		                        	  var idtn = dim[0].split('=')[1];
		                              openOrdini(idtn);
		                          }
		                      });
		                      break;
		                  }
		                  case 14:
		                	  	break;
		                  default: {
		                      $cell.click(function() {openLayer($(this));});
		                  }
		            }
		        }
		        if (rigaAggiuntaFuoriCompetenza) {
					$row.contents('td').css({'border-top': '1px solid red','border-bottom': '1px solid red','border-left': 'none', 'border-right': 'none'});
					$row.contents('td:first').css('border-left', '1px solid red');
					$row.contents('td:last').css('border-right', '1px solid red');
				}
			}
		}
		addFadeLayerEvents();
		setLastFocusedElement();
		setBollaRivKeyPress();
		setBarcodeKeyDownEvent();
		var imgsQfdv = $("#BollaControlloTab_table img[name='attImg'], #BollaControlloTab_table img[name='avviso_copertina'], #BollaControlloTab_table img[name='avviso_barcode_fittizio']");
		if (imgsQfdv.length > 0) {
			function pulsateImgs() {
				imgsQfdv.animate({opacity: 0.2}, 500, 'linear').animate({opacity: 1}, 500, 'linear', pulsateImgs); 
			} 
			pulsateImgs();
		}
		if (hasPrenotazioniFisseNonEvase) {
			$("#prenotazioniFisseNonEvaseDiv").html('<img id="pfne" src="/app_img/ordini_non_evasi.png" border="0" title="' + prenotazioniFisseNonEvase + '" onclick="javascript: viewPrenotazioniNonEvsase()" style="cursor:pointer"/>');
			function pulsatePrenotazioniFisseNonEvaseImgs() {
				$("#pfne").animate({opacity: 0.2}, 500, 'linear').animate({opacity: 1}, 500, 'linear', pulsatePrenotazioniFisseNonEvaseImgs); 
			} 
			pulsatePrenotazioniFisseNonEvaseImgs();
			$("#pfne").tooltip({
                delay: 0,
                showURL: false
            });
		}
		$("#aggiornaBarcode").change(function() {
			if ($(this).attr("checked") == true) {
				$("#barcode").focus(function() {
					$(this).css('background','#CCEAFF');
		    	});
				$("#barcode").focus();
			} else {
				$("#barcode").focus(function() {
					$(this).css('background','#ffff99');
		    	});
				$("#barcode").focus();
			}
		});
		
		setFieldEvents();
		
		if ($("#msgBollaLetti").length > 0) {
			$("#msgBollaLetti").change(function() {
				if ($(this).attr("checked") == true) {
					dojo.xhrGet({
						url: appContext + '/bollaRivendita_saveMessaggiBollaLetti.action?dataTipoBolla=' + escape($("#dataTipoBolla").val()),			
						handleAs: "text",				
						preventCache: true,
						handle: function(data,args) {
							if (args.xhr.status == 200) {
								$("#msgBollaLettiSpan").remove();
								$("#toggleMsgs").trigger("click");
							}
						}
				    });
				}
			});
		}
		
		$('#totalHeader_7').text(calculateMancanze());
		$('#totalHeader_8').text(calculateEccedenze());		
		
		if (conditionDisableForm) {
			disableBollaConsegna();
		} else {
			container.css({"border":"none"});
			container.text("");
		}
					
		$('#BollaControlloForm input:checkbox[name^=spunte]').change(function() {checkboxChanged($(this));});
		
		service = new dojo.rpc.JsonService(smdUrl);
		
		$("#aggiornaBarcode").attr("checked", false);
		
		$("#barcode").numeric({"decimal":false, "negative":false});
		
		$('#barcode').focus();
	});
	
	function checkSpuntaBolla() {
		if (hasSpuntaObbligatoriaBollaConsegna) {
			var unchked = $('#BollaControlloForm input:checkbox[name^=spunte]:not(:checked)').length;
			if (unchked > 0) {
				$.alerts.dialogClass = "style_1";
				jAlert(msgRigheNonSpuntate, attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				}); 
				setTimeout(function() {
					unBlockUI();
				}, 100);
				return false;
			}
		}
		if (isMenta && $("#msgBollaLetti").length > 0 && !$("#msgBollaLetti").is(":checked")) {
			$.alerts.dialogClass = "style_1";
			jAlert(msgBollaNonLetti, attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
			}); 
			setTimeout(function() {
				unBlockUI();
			}, 100);
			return false;
		}
		return true;
	}
	
	function saveBolla() {
		setTimeout(
				function() {
					return (validateFields('BollaControlloForm') 
							&& setFieldsToSave(true) 
							&& setFormAction('BollaControlloForm','bollaRivendita_save.action', '', 'messageDiv', false, 
									'', function() { setFieldsToSave(false); }));
				}
		, 10);
	}
	
	function saveBollaAuto() {
		setTimeout(
				function() {
					return (validateFields('BollaControlloForm') 
							&& setFieldsToSave(true) 
							&& setFormAction('BollaControlloForm','bollaRivendita_save.action', '', 
												'messageDiv', false, '', function() { setFieldsToSave(false); }, 
												'',false, '', true));
				}
		, 10);
	}
	
	function saveAndSendBolla() {
		if (!checkSpuntaBolla()) {
			return false;
		}
		confirmMemorizzaInvia = function() {
			setTimeout(
				jConfirm(memorizzaInviaConfirm, attenzioneMsg, function(r) {
					if (r) {
						return (validateFields('BollaControlloForm') 
								&& setFieldsToSave(true) 
								&& setFormAction('BollaControlloForm','bollaRivendita_send.action', '', 'messageDiv', false, '', function() {setFieldsToSave(false) && reloadDataTipoBolle();})
						);
					} 
					unBlockUI();
				}, true, false)
			,10);
		};
		
		if (hasPopupConfermaSuMemorizzaInviaBolle) {
			var spunte = $('#BollaControlloForm input:checkbox[id^=dpeChkbx_][fb=false]:not(:checked)').length;
			var msgSpunte = spunte > 0 ? $.validator.format(msgConfermaSpunte, [spunte]) + '<br/>' : '';
			var msg = $.validator.format(msgConfermaMancanzeEccedenze, [msgSpunte, calculateMancanze(), calculateEccedenze()]);
			setTimeout(
				jConfirm(msg, attenzioneMsg, function(r) {
					if (r) {
						confirmMemorizzaInvia();
					} 
					unBlockUI();
				}, true, false)
			,10);
		} else {
			confirmMemorizzaInvia();
		}
	}
	
	dojo.addOnLoad(function() {
		var tabBarcode = dijit.byId('pubblicazioniTableBarcode');
		dojo.connect(tabBarcode, "onRowClick", function () { onSelectPubblicazioniBarcodeTable("barcode"); });
		dojo.connect(tabBarcode, "onKeyDown", tabBarcode, function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.charCode);  
			if (keycode == 13) {
				var row = this.getRowNode(this.focus.rowIndex);
				row.trigger('click');
			} else if (keycode == 27) {
				$("#aggiornaBarcode").attr("checked", false);
				$("#aggiornaBarcode").trigger("change");
				$("#barcode").val('');
				$("#barcode").focus();
				$('#close').trigger('click');
			}
		});
		
		var tabRBarcode = dijit.byId('pubblicazioniTableRichiediBarcode');
		dojo.connect(tabRBarcode, "onRowClick", function () { onSelectPubblicazioniRichiediBarcodeTable("barcode"); });
		dojo.connect(tabRBarcode, "onKeyDown", tabRBarcode, function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.charCode);  
			if (keycode == 13) {
				var row = this.getRowNode(this.focus.rowIndex);
				row.trigger('click');
			} else if (keycode == 27) {
				$("#aggiornaBarcode").attr("checked", false);
				$("#aggiornaBarcode").trigger("change");
				$("#barcode").val('');
				$("#barcode").focus();
				$('#close').trigger('click');
			}
		});
	});
	
	onCloseLayer = function() {
		$("#aggiornaBarcode").attr("checked", false);
		$("#aggiornaBarcode").trigger("change");
	};
	
	function noBarcodeFoundAction(fieldVal) {
		if (hasEdicoleAutorizzateAggiornaBarcode) {
			callBarcodeVenditeService();
		} else {
			$.alerts.dialogClass = "style_1";
			jAlert(nessunaPubblicazione, attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
				nessunaPubblicazioneAction();
			});
		}
		return false;
	}

	function callBarcodeVenditeService() {
		ray.ajax();
		var dtBolla = $("#dataTipoBolla").val().split("|")[0];
		var tpBolla = $("#dataTipoBolla").val().split("|")[1].replace("Tipo","").trim();
		service.getRows({inputText:$('#barcode').val(), progressivo : "0"}).addCallback(function(bean) {
			if (bean.type == 'VENDITE_BARCODE') {
				var msgPre = !$("#aggiornaBarcode").is(":checked") ? nessunaPubblicazione + "<br/>" : "";
				bean.exceptionMessage = (abilitataCorrezioneBarcode == 'true') ? msgPre + msgConfirmAggiornareAssociazioneBarcode : msgPre + msgConfirmRichiestaAggiornareAssociazioneBarcode;
				bean.type = (abilitataCorrezioneBarcode == 'true') ? 'CONFIRM_ASSOCIA_BARCODE' : 'CONFIRM_INVIA_MESSAGGIO_ASSOCIA_BARCODE';
				doAssociaBarcode(bean, null, function() {$('#barcode').val(''); $('#aggiornaBarcode').attr("checked", false); $("#aggiornaBarcode").trigger("change"); $('#barcode').val(''); $('#barcode').focus();}, dtBolla, tpBolla);
			} else if (bean.type == 'CONFIRM_ASSOCIA_BARCODE' || bean.type == 'CONFIRM_INVIA_MESSAGGIO_ASSOCIA_BARCODE') {
				doAssociaBarcode(bean, null, function() {$('#barcode').val(''); $('#aggiornaBarcode').attr("checked", false); $("#aggiornaBarcode").trigger("change"); $('#barcode').val(''); $('#barcode').focus();}, dtBolla, tpBolla);
			}
			unBlockUI();
		});
	}

	function showMessaggioIdtn(idMessaggioIdtn) {
		var url = appContext + "/messages_showMessaggioIdtn.action?idMessaggioIdtn=" + idMessaggioIdtn;
		openDiv('popup_name', 750, 450, url);
		return false;
	}
	
	function disableBollaConsegna() {
		disableAllFormFields('dlSelect','dataTipoBolla','soloRigheSpuntare','submitFilter','barcode','popup_ok');			
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
	
	window.onbeforeunload = function () {
		if (attivaMemorizzazioneAutomatica && $("#dtb").val().split("|")[2] == "true" && $("#BollaControlloForm input:hidden[name^=modificato][value=true]").length > 0) {
			validateFields('BollaControlloForm'); 
			validateFields(false) && setFieldsToSave(true) && setFormAction('BollaControlloForm','bollaRivendita_save.action', '', 'messageDiv', true, '', function() {setFieldsToSave(false);}, '', false);
			return msgConfirmSaveBolla;
		}
	};
	
	function openLayer(obj) {
        var popID = 'popup_name_rifornimenti';
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
        
        var pk = tr.find("td:nth-child(12)").find("input:text[name^='field']:first").attr("id").replace('differenze','');
        var url = appContext + "/sonoInoltreUscite_showRichiesteRifornimenti.action?idtn=" + idtn + "&pkSel=" + pk + "&coddl=" + coddl;
        if (periodicita != '') {
            url += "&periodicita=" + periodicita;
        }
        if (lastFocusedRow != '' && lastFocusColor != '') {
        	lastFocusedRow.css({"backgroundColor":lastFocusColor});
        }
        //ticket 0000374
        openDivRifornimenti(popID, popWidth, popHeight, url);
        //openDiv(popID, popWidth, popHeight, url);
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
	    var url = appContext + "/statisticaPubblicazioni_showNumeriPubblicazioniStatistica.action?codPubblicazione=" + cpu + "&periodicita=" + periodicita;
		openDiv(popID, popWidth, popHeight, url);
		return false;
	}
	
	function calculateMancanze() {
		var sum = 0;
		$fields.each(function() {
			var $val = $(this).val();
			if ($val.length > 0 && !isNaN($val) && Number($val) < 0) {
				sum += Number($val);
			};
		});
		return sum;
	};
	
	function calculateEccedenze() {
		var sum = 0;
		$fields.each(function() {
			var $val = $(this).val();
			if ($val.length > 0 && !isNaN($val) && Number($val) > 0) {
				sum += Number($val);
			};
		});
		return sum;
	};
	
	function checkboxChanged($checkField) {
		var isChecked = $checkField.attr("checked");
		var $nextTd = $checkField.parent("td").next("td");
		var $diffTextField = $nextTd.find("input:text").first();
		var $fSpunta = $nextTd.find("input:hidden[name^=spunta]").first();
		var $fModificato = $fSpunta.next("input:hidden[name^=modificato]");
		$fSpunta.val(isChecked ? 1 : 0);
		$fModificato.val("true");
		$diffTextField.select();
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
	
	function nessunaPubblicazioneAction() {
		$("#barcode").val('');
		return false;
	}
	
	function viewPrenotazioniNonEvsase() {
	 	var url = appContext + "/bollaRivendita_showPrenotazioniNonEvase.action?dataTipoBolla=" + escape($("#dataTipoBolla").val());
		openDiv('popup_name', 650, 300, url);
		return false;
	}
	
	function esportaOrdini(tipoReportOrdiniClienti) {
		var dataBolla = $("#dataTipoBolla").val().split("|")[0];
    	if (fillFormEsportaOrdini() <= 0) {
    		$.alerts.dialogClass = "style_1";
			jAlert(msgNessunOrdineClienteInBolla, attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
			});
			return false;
    	} else {
	    	fillFormEsportaOrdini();
	    	$("#ReportOrdiniClientiForm").append('<input type="hidden" name="esportaClientiDiTutteLeBolle" value="true"/>');
	    }
	    $("#ReportOrdiniClientiForm").append('<input type="hidden" name="tipoReportOrdiniClienti" value="' + tipoReportOrdiniClienti + '"/>');
	    $("#ReportOrdiniClientiForm").append('<input type="hidden" name="dataTipoBolla" value="' + $("#dataTipoBolla").val() + '"/>');
	    $("#ReportOrdiniClientiForm").submit();
		unBlockUI();
		
		/*jConfirm(confirmEsportareClientiDiTutteLeBolle.replace('{0}', dataBolla), attenzioneMsg.toUpperCase(), function(r) {
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
		}, true, true);*/
	}
	
	function fillFormEsportaOrdini() {
		var j = 0;
		var arrIdtn = new Array();
		$('#totalHeader_1').text($('#BollaControlloTab_table tbody tr td:nth-child(14)').each(function(i) {
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
	
	function setBorderToNonUscita() {
		$("#BollaResaForm input:text[name^='resoFuoriVoce']").closest("tr").each(function() {
			var $tr = $(this);
			$tr.contents('td').css({'border-top': '1px solid red','border-bottom': '1px solid red','border-left': 'none', 'border-right': 'none'});
			$tr.contents('td:first').css('border-left', '1px solid red');
			$tr.contents('td:last').css('border-right', '1px solid red');
		});
		return false;
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