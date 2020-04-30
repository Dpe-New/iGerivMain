<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<link rel="stylesheet" type="text/css"
	href="/app_js/dojo-release-1.7.2/dijit/themes/claro/claro.css" />
<link rel="stylesheet" type="text/css"
	href="/app_js/dojo-release-1.7.2/dojox/grid/resources/Grid.css" />
<link rel="stylesheet" type="text/css"
	href="/app_js/dojo-release-1.7.2/dojox/grid/resources/claroGrid.css" />
<script>
	var smdUrlVendite = "${smdUrlVendite}";
	var smdUrlInventario = "${smdUrlInventario}";
	var serviceVendite = null;
	var maxValueMultiplier = 100;
	var msgCpu = "<s:text name='igeriv.cpu'/>";
	var msgSottotitolo = "<s:text name='igeriv.sottotitolo'/>";
	var msgTitolo = "<s:text name='igeriv.titolo'/>";
	var msgNumero = "<s:text name='igeriv.numero'/>";
	var msgPrezzoAcquisto = "<s:text name='igeriv.prezzo.netto'/>";
	var msgPrezzoLordo = "<s:text name='igeriv.prezzo.lordo'/>";
	var msgDataUscita = "<s:text name='igeriv.data.uscita'/>";
	var msgQta = "<s:text name='igeriv.quantita'/>";
	var msgGiacenza = "<s:text name='igeriv.giacienza.ext'/>";
	var msgNessunaPubb = "<s:text name='label.pubblication_input_module_listener.Pubblication_Not_Found'/>";
	var moltiplicatoreMoltoGrandeMsg = '<s:text name="igeriv.moltiplicatore.molto.alto"/>';
	var ricercaMinCaratteriMsg = '<s:text name="error.ricerca.per.titolo.almeno.due.caratteri"/>';
	var moltiplicatoreInvalidoMsg = '<s:text name="igeriv.moltiplicatore.invalido"/>';
	var msgConfirmDeleteInventario = '<s:text name="gp.cancellare.inventario.data"/>';
	var msgEsisteGiaInventario = '<s:text name="igeriv.error.msg.inventario.gia.esistente"/>';
	var nessunaDataInventarioSelezionataMsg = '<s:text name="igeriv.error.msg.nessuna.data.inventario.selezionata"/>';
	var moltiplicatoreZeroMsg = '<s:text name="igeriv.moltiplicatore.uguale.a.zero.non.ammesso"/>';
	var msgContoDeposito = '<s:text name="igeriv.conto.deposito"/>';
	var msgScaduta = '<s:text name="igeriv.scaduta"/>';
	var modalita = 0;
	var MODALITA_DEFAULT = 0;
	var MODALITA_RICERCA = 1;
	dojo.require("dojox.grid.DataGrid");
	dojo.require("dojo.data.ItemFileWriteStore");
	dojo.require("dojo.rpc.RpcService");
	dojo.require("dojo.rpc.JsonService");	
	
	$(document).ready(function() {
		$("#add, #delete, #pdf, #findPub, #xls").tooltip({
			delay: 0, 
		    showURL: false
		});	 
		$("#inputText").keydown(function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.charCode); 
			if (keycode == 13) {
				event.preventDefault();
				doInputTextEnterKeyPress();				
			}    
			if (keycode == 37) {
				if ($("#dialogContent").is(":hidden")) {
					event.preventDefault();
				}
				$("#qta").select() 
				$("#qta").focus();
			}
			if (keycode == 38 || keycode == 40) {
				if ($("#dialogContent").is(":visible")) {
					tab.focus.setFocusIndex(0, 0);
				}
			}
		});
		$("#qta").keydown(function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.charCode); 
			if (keycode == 39) {
				if ($("#dialogContent").is(":hidden")) {
					event.preventDefault();
				}
				$("#inputText").select() 
				$("#inputText").focus();
				$("#qta").css('background','#fff');
			}
			if (keycode == 38 || keycode == 40) {
				if ($("#dialogContent").is(":visible")) {
					tab.focus.setFocusIndex(0, 0);
				}
			}
		});
		$.alt('X', function() {
			setModalita(MODALITA_RICERCA);
		});
		$.altPlus(function() {
			var val = isNaN($("#qta").val()) ? 0 : Number($("#qta").val());
			$("#qta").val(++val);
		}, null);
		
		$.altMinus(function() {
			var val = isNaN($("#qta").val()) ? 0 : Number($("#qta").val());
			$("#qta").val(--val);
		}, null);
		
		$(this).bind('keydown', function(event) { 
			var keycode = (event.keyCode ? event.keyCode : event.charCode); 
			switch (keycode) {
				case 27:		// ESC
					event.preventDefault();
					if ($("#dialogContent").is(":visible")) {
						$('#close').trigger('click');
					}
					setModalita(MODALITA_DEFAULT);
					break;
				case 38:		// arrow up
				case 40: {		// arrow down
					tab.focus.setFocusIndex(0, 0);
					break;
				}
			}
		});
		
		refreshDataVendite = function() {
			ray.ajax();
			var inputVal = $('#inputText').val();
			var params = {inputText: inputVal, progressivo: '1', idConto: null, operation: 'CONSEGNA', quantita:Number($("#qta").val().trim()), mostraTutteUscite : 'true', ricercaProdottiVari : 'false', findPrezzoEdicola : 'true', findCopieInContoDeposito : 'true', findGiacenza : 'true'};
			var deferred = serviceVendite.getRows(params);
			deferred.addCallback(rowsCallback);
		};
		
		rowsCallback = function(bean) {
			if (bean != null) {
				if (bean.type == 'EXCEPTION') {							
					$.alerts.dialogClass = "style_1";
					setModalita(MODALITA_DEFAULT);
					jAlert(bean.exceptionMessage, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						$('#inputText').val('');
						$('#inputText').focus();
					});
				} else if (bean.type == 'VENDITE_TITOLO') {
					unBlockUI();
					showInvDialog('dialogContent');
					populateTablePubblicazioni(bean.result, 'pubInventarioTable');
					$('#inputText').val('');
					
				} else if (bean.type == 'VENDITE_BARCODE') {
					addDettaglioToInventario(bean.result);
					setModalita(MODALITA_DEFAULT);
					unBlockUI();
				} else {
					jAlert(msgNessunaPubb, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						$('#inputText').val('');
						$('#inputText').focus();
					});
				}
			} else {
				jAlert(msgNessunaPubb, attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					$('#inputText').val('');
					$('#inputText').focus();
				});
			}
			setTimeout(function() {
				unBlockUI();				
			}, 100)
		};
		
		showInvDialog = function(divId) {																
			var div = $("#" + divId);	
			var str = '<div id="close" style="z-index:999999; position:absolute; top:30px; left:720px"><a href="#"><img id="imgClose" src="/app_img/close.gif" style="border-style: none" border="0px" class="btn_close" title="' + chiudiMsg + '" alt="' + chiudiMsg + '"/></a></div>';		
			div.fadeIn('slow', function() {
				div.prepend(str);
			});										
			div.css({'visibility':'visible','top':'350px','width':'730px','height':'520px','border-color':'#66cccc'});											
			var popMargTop = (div.height() + 80) / 2;
		    var popMargLeft = (div.width() + 80) / 2;
		    div.css({
		        'margin-top' : -popMargTop,
		        'margin-left' : -popMargLeft
		    });
		    addFadeLayerEvents();	
		}
		
		onCloseLayer = function() {
			setModalita(MODALITA_DEFAULT);
			unBlockUI();
		};
		
		
		
// 		populateTablePubblicazioni = function(bean) {
// 			var data = {identifier: 'idtn', items: bean};
// 			var dataStore = new dojo.data.ItemFileWriteStore({ data: data });
// 			tab = dijit.byId('pubInventarioTable');
// 			tab.setStore(dataStore);
// 			tab.store = dataStore;
// 			$("#pubInventarioTable tr").each(function() {
// 				$tr = $(this);
// 				$tr.css({"cursor":"pointer"});
// 				$tr.mouseover(function() {
// 					$(this).addClass("highlightVendite");
// 				});  
// 				$tr.mouseout(function() {
// 					$(this).removeClass("highlightVendite");
// 				});  					
// 			});				    	    
// 		}
		
		doInputTextEnterKeyPress = function() {
			var val = $("#inputText").val();
			if (val.trim().length > 0) {
				var inputVal = parseLocalNum(val);
				var multiplier = $("#qta").val().trim();
				if (isNaN(multiplier)) {
					$.alerts.dialogClass = "style_1";
					jAlert(moltiplicatoreInvalidoMsg, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						$("#qta").select();
						$("#qta").focus();
					});
					return false;
				} 
				if (multiplier == 0) {
					$.alerts.dialogClass = "style_1";
					jAlert(moltiplicatoreZeroMsg, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						$("#qta").select();
						$("#qta").focus();
					});
					return false;
				} 
				if (isNaN(inputVal) && inputVal.length < 2) {
					jAlert(ricercaMinCaratteriMsg, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						$("#inputText").focus();
					});
					return false;
				} 
				if (isNaN(inputVal) && inputVal.length >= 2) {
					var re = /%/g;
					var result = inputVal.replace(re, "");
					if(result.trim().length<2){
						jAlert(ricercaMinCaratteriMsg, attenzioneMsg.toUpperCase(), function() {
							$.alerts.dialogClass = null;
							$("#inputText").focus();
						});
						return false;
					}
					
				} 
				
				
				
				if ($("#strDataInventario").val() == null || $("#strDataInventario").val() == '') {
					setTimeout(function() {
						jAlert(nessunaDataInventarioSelezionataMsg, attenzioneMsg.toUpperCase(), function() {
							$.alerts.dialogClass = null;
							setTimeout(function() {
								$("#inputText").focus();
							}, 100);
							return false;
						});
					}, 100);
					return false;
				} 
				if (Number(multiplier) > maxValueMultiplier) {
					PlaySound('beep3');
					jConfirm(moltiplicatoreMoltoGrandeMsg.replace('{0}',multiplier), attenzioneMsg, function(r) {
					    if (r) { 
					    	$("#inputText").val(parseLocalNum(val));
					    	refreshDataVendite();	
					    } else {
					    	$("#qta").val('1');
					    	$("#inputText").focus();
					    }
					});
				} else {
					var val1 = /[a-zA-Z]/.test(val) ? val : parseLocalNum(val);
					$("#inputText").val(val1);
					refreshDataVendite();	
				}
			}
		}
		
		addDettaglioToInventario = function(bean) {
			var idInv = $("#strDataInventario").val().split("|")[1];
			var quantitaCopieContoDeposito = typeof(bean.quantitaCopieContoDeposito) !== 'undefined' && bean.quantitaCopieContoDeposito != '' ? Number(bean.quantitaCopieContoDeposito) : null;
			var isScaduta = typeof(bean.codMotivoRespinto) !== 'undefined' && Number(bean.codMotivoRespinto) > 0 && Number(bean.codMotivoRespinto) != 2 ? true : false;
			var qta = Number($("#qta").val().trim());
			var pub = {'idInventario' : idInv, 'idtn' : Number(bean.idtn), 'titolo' : bean.titolo.toString(), 'sottoTitolo' : bean.sottoTitolo.toString(), 'numeroCopertina' : bean.numeroCopertina.toString(), 'prezzoCopertina' : Number(bean.prezzoCopertina), 'prezzoEdicola' : Number(bean.prezzoEdicola), 'quantitaCopieContoDeposito' : quantitaCopieContoDeposito, 'dataUscitaFormat' : bean.dataUscitaFormat.toString(), 'quantita' : qta, 'modalita' : modalita, 'giacenzaSP' : Number(bean.giacenzaSP), 'prezzoCopertina' : Number(bean.prezzoCopertina), 'isScaduta' : isScaduta};
			var deferred = serviceInventario.addDettaglioToInventario(pub);
			deferred.addCallback(function(bean) {
				if (bean.type == 'EXCEPTION') {					
					$.alerts.dialogClass = "style_1";
					jAlert(bean.exceptionMessage, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						$("#qta").val('1');
						$('#inputText').val('');
						$('#inputText').focus();
						setTimeout(function() {
							unBlockUI();					
						}, 100);
					});
				} else if (bean.type == 'OK') {
					var result = bean.result;
					writeOutputLine(result);
					var importo = Number(result.importoTotale).toFixed(2);
					$("#totaleInventario").text(displayNum(importo));
					$("#qta").val('1');
					$('#inputText').val('');
				}
				unBlockUI();					
			});
		}
		
		writeOutputLine = function(bean) {
			var html = '<table cellspacing="0" cellpadding="0" border="0" width="100%" style="table-layout: fixed;" class="extremeTableFieldsVeryLarge" id="InventarioTab_table"><thead><tr>'
				+ '<td width="18%" class="tableHeader" style="text-align:left">' + msgTitolo + '</td>'
				+ '<td width="10%" class="tableHeader" style="text-align:left">' + msgSottotitolo + '</td>'
				+ '<td width="8%" class="tableHeader" style="text-align:center">' + msgNumero + '</td>'
				+ '<td width="9%" class="tableHeader" style="text-align:right">' + msgPrezzoAcquisto + '</td>'
				+ '<td width="9%" class="tableHeader" style="text-align:right">' + msgPrezzoLordo + '</td>'
				+ '<td width="12%" class="tableHeader" style="text-align:center">' + msgDataUscita + '</td>'
				+ '<td width="5%" class="tableHeader" style="text-align:center">' + msgQta + '</td>'
				+ '<td width="10%" class="tableHeader" style="text-align:center">' + msgGiacenza + '</td>'
				+ '<td width="10%" class="tableHeader" style="text-align:center">' + msgContoDeposito + '</td>'
				+ '<td width="8%" class="tableHeader" style="text-align:center">' + msgScaduta + '</td>'
				+ '</tr></thead>';
			html += '<tbody><tr><td width="20%">' + bean.titolo + '</td>'
				+ '<td width="12%">' + bean.sottoTitolo + '</td>'
				+ '<td width="8%" style="text-align: center;">' + bean.numeroCopertina + '</td>'
				+ '<td width="10%" style="text-align: right;">' + bean.prezzoEdicolaFormat + '</td>'
				+ '<td width="10%" style="text-align: right;">' + bean.prezzoCopertinaFormat + '</td>'
				+ '<td width="12%" style="text-align:center">' + bean.dataUscitaFormat + '</td>'
				+ '<td width="10%" style="text-align:center">' + bean.quantita + '</td>'
				+ '<td width="10%" style="text-align:center">' + bean.giacenza + '</td>'
				+ '<td width="10%" style="text-align:center">' + bean.isContoDeposito + '</td>'
				+ '<td width="8%" style="text-align:center">' + bean.isScaduta + '</td>'
				+ '</tr></tbody></table>';
			$("#rowOutput").html(html);
		}
		
		
		addInventario = function() {
			var url = "inventario_showPopupInventario.action";
			openDiv('popup_name', 450, 250, url);
			return false;
		}
		
		deleteInventario = function() {
			if ($("#strDataInventario").val() != '' && $("#strDataInventario").val() != null) {
				var dataInventario = $("#strDataInventario").val().split("|")[0];
				var idInv = $("#strDataInventario").val().split("|")[1];
				jConfirm(msgConfirmDeleteInventario.replace('{0}', dataInventario), attenzioneMsg, function(r) {
					if (r) {
						dojo.xhrGet({
							url: "${pageContext.request.contextPath}${ap}/inventario_deleteInventario.action?idInventario=" + idInv,	
							preventCache: true,
							handleAs: "text",				
							handle: function(data,args) {
								var $doc = $(data);
								var hasError = $("igerivException", $doc).length > 0;
								if (args.xhr.status != 200 || hasError) {
									$.alerts.dialogClass = "style_1";
									jAlert($("igerivException", $doc).text(), attenzioneMsg.toUpperCase(), function() {
										$.alerts.dialogClass = null;
									});
								} else {
									reloadDateIdInventario(null);
								}
							}
						});
					}
				}, true, false);
			}
		}
		
		reportInventario = function(tipo) {
			if ($("#strDataInventario").val() != '') {
				var idInv = $("#strDataInventario").val().split("|")[1];
				$("#InventarioForm").attr("action", "report_reportInventario.action"); 	
				$("#InventarioForm").attr("target", "_blank"); 
				$("#idInventario").val(idInv);
				$("#tipo").val(tipo);
				$("#InventarioForm").submit();
			}
		}
		
		reloadDateIdInventario = function(dataInventarioSelected) {
			dojo.xhrGet({
				url: appContext + '/pubblicazioniRpc_getDateIdInventario.action',			
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=utf-8"}, 	
				preventCache: true,
				handle: function(data,args) {
					var list = '';		
		            for (i = 0; i < data.length; i++) {
		            	var strSelected = '';
		            	var valueStringForSelectBox = data[i].valueStringForSelectBox;
		            	var keyStringForSelectBox = data[i].keyStringForSelectBox;
	            		if (dataInventarioSelected != null && valueStringForSelectBox == dataInventarioSelected) {
	            			strSelected = "selected";
	            		}
						list += '<option ' + strSelected + ' value="' + keyStringForSelectBox + '">' + valueStringForSelectBox + '</option>';
		            }
		            $("#strDataInventario").empty();
		            $("#strDataInventario").html(list);
		            setModalita(MODALITA_DEFAULT);
					var idInv = typeof(dataInventarioSelected) !== 'undefined' && dataInventarioSelected != '' ? dataInventarioSelected : $("#strDataInventario").val().split("|")[1];
					setTotaleInventario(idInv);
					$("#rowOutput").html('');
				}
		    });
		}
		
		setModalita = function(mod) {
			modalita = mod;
			switch(mod) {
				case MODALITA_RICERCA:
					$("#qta").attr("disabled", true);
					$("#inputText").css({"border-style":"solid","border-width":"3px","border-color":"#25C23F"});
					if ($("#rowOutput").is(":visible")) { 
						$("#rowOutput").css({"margin-top":"14px"});
					}
					break;
				default:
					$("#qta").val(1);
					$("#qta").attr("disabled", false);
					$("#inputText").css({"border-style":"none"});
					if ($("#rowOutput").is(":visible")) { 
						$("#rowOutput").css({"margin-top":"18px"});
					}
					break;
			}
			$("#inputText").focus();
		}
		
		setTotaleInventario = function(idInventario) {
			if (serviceInventario) {
				var deferred = serviceInventario.getTotaleInventario(idInventario);
				deferred.addCallback(function(totale) {
					var importo = Number(totale).toFixed(2);
					$("#totaleInventario").text(displayNum(importo));	
					$('#inputText').focus(); 
				});
			}
		}
		
		$("#strDataInventario").change(function() {
			var idInv = $(this).val().split("|")[1];
			setTotaleInventario(idInv);
			$("#rowOutput").html('');
		});
		
	});
	
	dojo.addOnLoad(function() {
		serviceVendite = new dojo.rpc.JsonService(smdUrlVendite);
		serviceInventario = new dojo.rpc.JsonService(smdUrlInventario);
		var store = new dojo.data.ItemFileWriteStore({
			data: []
		});
		tab = dijit.byId('pubInventarioTable');
		dojo.connect(tab, "onRowClick", function () { onSelectPubblicazioniTable(); });
		onSelectPubblicazioniTable = function(event) {	
			var selectedBean = tab.getItem(tab.focus.rowIndex);	
			addDettaglioToInventario(selectedBean);
			setModalita(MODALITA_DEFAULT);
			$('#close').trigger('click');
		};
		if ($("#strDataInventario").val() != '') {
			var idInv = $("#strDataInventario").val().split("|")[1];
			setTotaleInventario(idInv);
		}
		$("#qta").val('1');
		$('#inputText').focus();   						           
	});
	

	function populateTablePubblicazioni(bean, tableId) {
		var tab = dijit.byId(tableId);
		var data = {identifier: "idtn", items: bean};
		var dataStore = new dojo.data.ItemFileWriteStore({ data: data });
		tab.setStore(dataStore);
		tab.store = dataStore;
		$("#" + tableId + " tr").each(function() {
			$tr = $(this);
			$tr.css({"cursor":"pointer"});
			$tr.mouseover(function() {
				$(this).addClass("highlightVendite");
			});  
			$tr.mouseout(function() {
				$(this).removeClass("highlightVendite");
			});  					
		});				    	    
	}
	
	
</script>
