dojo.require("dojox.grid.DataGrid");
dojo.require("dojo.data.ItemFileWriteStore");
dojo.require("dojo.rpc.RpcService");
dojo.require("dojo.rpc.JsonService");	
dojo.require("dijit.Tree");
dojo.require("dijit.tree.ForestStoreModel");

var mostSoldBarPos = 0;
var mostSoldBarSizes = 0;
var currentPage = 0; 
var abilitaResto = false;
var includiPubblicazioniScontrino = false;
var ricercaProdottiVari = false;
var barcodeHit = false;
var modalita = 'CONSEGNA';
var selectedBarcode = '';
var width1 = '';
var width2 = '';
var width3 = '';
var objSeparator = null;
var objSeparatorL = null;
var service = null;
var objMain = null;
var venditeTableObject = null;
var contiTreeObject = null;

function itemToJSON(store, item){
	var json = {};
	if(item && store) {
		var attributes = store.getAttributes(item);
	    if (attributes && attributes.length > 0) {
	    	var i;
	    	for (i = 0; i < attributes.length; i++) {
	    		var values = store.getValues(item, attributes[i]);
	    		if (values) {
	    			if (values.length > 1 ) {
	    				var j;
	    				json[attributes[i]] = [];
	    				for (j = 0; j < values.length; j++ ) {
	    					var value = values[j];
	    					if(store.isItem(value)){
	    						json[attributes[i]].push(dojo.fromJson(itemToJSON(store, value)));
	    					} else {
	    						json[attributes[i]].push(value);
	    					}
	    				}
	    			} else {
	    				if (store.isItem(values[0])) {
	    					json[attributes[i]] = dojo.fromJson(itemToJSON(store, values[0]));
	    				} else {
	    					json[attributes[i]] = values[0];
	    				}
	    			}
	    		}
	    	}
	    }
	}
	return dojo.toJson(json);
}

function confirmSendErrorToDpe() {
	unBlockInputText();
	jConfirm(sendErrorToDpeConfirmMsg, attenzioneMsg, function(r) {
	    if (r) { 
	    	regCassaApplet.sendLogFile();
	    }
	    $("#inputText").focus();
	}, true, false);
}

function getJsonStoreString(tab) {
	var json = new Array();
	tab.store.fetch({ query: { },  
        onItem: function(item) {
        	json.push(itemToJSON(tab.store, item));
         }
	});
	return '{list:[' + json + ']}';
}

function getStoreLength(tab) {
	var count = 0;
	tab.store.fetch({ query: { },  
        onItem: function(item) {
        	count++;
         }
	});
	return count;
}

function setTableData(tab, idField, items) {
	var data = {identifier: idField, items: items};
	var dataStore = new dojo.data.ItemFileWriteStore({ data: data });
	tab.setStore(dataStore);
	tab.store = dataStore;
	if (tab == venditeTableObject) {
		dojo.connect(dataStore, "onNew", function(newItem, parentInfo) {
			onVenditeTableAdd(newItem);
		});
		dojo.connect(dataStore, "onDelete", function(item) {
			onVenditeTableDelete();
		});
	}
}

function addTableData(tab, idField, items) {
	if (tab.store) {
		if (items.length) {
			for (var i = 0; i < items.length; i++) {
				tab.store.newItem(items[i]);
			}
		} else {
			tab.store.newItem(items);
		}
	} else {
		setTableData(tab, idField, items);
	}
}

function setTotals() {	
	var total = 0;
	var totalScontrino = 0;
	var totalPubb = 0;
	var totalPezzi = 0;	
	var importo = 0;
	venditeTableObject.store.fetch({ query: { },  
        onItem: function(item) {
        	var isProdottoNonEdit = typeof(item.prodottoNonEditoriale) === 'undefined' ? false : (item.prodottoNonEditoriale == 'true' ? true : false);
        	var qta = Number(item.quantita);
        	totalPezzi += qta;
        	importo = qta * Number(item.prezzoCopertina);
        	total += importo;
        	if (isProdottoNonEdit) {
        		totalScontrino += importo;
        	} else {
        		totalPubb += importo;
        	}
        }
	});
	total = total.toFixed(2); 	
	totalScontrino = totalScontrino.toFixed(2);
	totalPubb = totalPubb.toFixed(2);
	$("#totale").val(displayNum(total));
	if ($("#totaleScontrino").length > 0) {
		$("#totaleScontrino").val(displayNum(totalScontrino));
	}
	if ($("#totalePubb").length > 0) {
		$("#totalePubb").val(displayNum(totalPubb));
	}
	$("#totalePezzi").val(totalPezzi);
}

function onVenditeTableAdd(bean) {
	if (typeof(bean) !== 'undefined') {
		setGiacenzaPub(bean);
	}
	setTotals();
	setTimeout(function() {
		addRightClickMenu(); 
		setScrollBarToBottom();
		$('#inputText').val('');
		$('#inputText').focus();
	}, 100);
}

function setGiacenzaPub(bean) {
	if (bean.prodottoNonEditoriale.toString() == 'false' && !isNaN(bean.giacenzaIniziale)) {
		var curIdtn = bean.idtn.toString();
		var giacenzaIniziale = Number(bean.giacenzaIniziale);
		var giac = 0;
		var count = 0;
		venditeTableObject.store.fetch({ query: {idtn: curIdtn},  
	        onItem: function(item) {
	        	count += Number(item.quantita);
	        	giac = giacenzaIniziale - count;
	        },
	        onComplete: function() {
	        	venditeTableObject.store.setValue(bean, "giacenzaIniziale", giac);
	        }
		});
	}
}

function onVenditeTableDelete() {
	setTotals();
	venditeTableObject.store.save();
	setTimeout(function() {
		addRightClickMenu();
		setScrollBarToBottom();
		$('#inputText').val('');
		$('#inputText').focus();
	}, 100);
}

dojo.addOnLoad(function() {	
	venditeTableObject = dijit.byId("venditeTable");
	if (!showLeftSideBar) {
		hideSidebarL();
	}
	if (!showRightSideBar) {
		hideSidebar();
	}
	setTableData(venditeTableObject, venditeTabIdField, []);

	dojo.extend(dijit.Tree, {
		refreshModel: function () {
			this.dndController.selectNone();
			this.model.store.clearOnClose = true;
    		this.model.store.close();
			this._itemNodesMap = {};
			this.rootNode.state = "UNCHECKED";
			this.model.root.children = null;
			if (this.rootNode) {
				this.rootNode.destroyRecursive();
			}
			this._load();
		}
	});
	
	var store = new dojo.data.ItemFileWriteStore({
		data: []
	});
	
	var treeModel = new dijit.tree.ForestStoreModel({
		store: store,
		query: {"type": "parent"},
		rootId: "root",
		rootLabel: "Groups",
		childrenAttrs: ["children"]
	});
	
	contiTreeObject = new dijit.Tree({
		model: treeModel,
		openOnClick:true,
		showRoot: false,
		collapseAll: true,
		_createTreeNode: function(args){
            var tnode = new dijit._TreeNode(args);
            tnode.labelNode.innerHTML = args.label;
            return tnode;
        },
        onDblClick : function(item) {
        	treeDoubleClick(item);
    	}
	}, "treeDiv");
	dojo.connect(contiTreeObject, "onKeyDown", tab, function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);  
		if (keycode == 27) {
			$("#inputText").focus();
			$('#close').trigger('click');
		}
	});
	
	service = new dojo.rpc.JsonService(smdUrl);
	
	loadLocalStorage();
	
	$("#ricercaProdottiVari").change(function() {
		if ($(this).attr("checked") == true) {
			service.setRicercaProdottiVari("true");
			$("#inputText").focus(function() {
				$(this).css('background','#ffcccc');
	    	});
			ricercaProdottiVari = true;
			$("#inputText").focus();
		} else {
			service.setRicercaProdottiVari("false");
			$("#inputText").focus(function() {
				$(this).css('background','#ffff99');
	    	});
			ricercaProdottiVari = false;
			$("#inputText").focus();
		}
	});
	
	$("#mostraTutteUscite").change(function() {
		$("#inputText").focus();
	});
	
	$("#aggiornaBarcode").change(function() {
		$("#inputText").focus();
	});
	
	if (idContoCliente != null && idContoCliente.trim() != '') {
		var deferred = service.getConto(idContoCliente);
		deferred.addCallback(function(conto) {
			addTableData(venditeTableObject, venditeTabIdField, conto);
			$('#idConto').val(idContoCliente);
			setTimeout(function() {
				setScrollBarToBottom();
				addRightClickMenu();
			}, 100);
			var def = service.deleteConto(Number(idContoCliente));
			def.addCallback(function(bean) {
				var giacenze = bean.result;
				var idtnRows = {};
				var prodRows = {};
				venditeTableObject.store.fetch({ query: { },  
			        onItem: function(item) {
			        	var idtn = item.idtn;
			        	var idProdotto = item.idProdotto;
			        	idtnRows[idtn] = (typeof(idtnRows[idtn]) === 'undefined') ? 1 : Number(idtnRows[idtn]) + 1;
			        	prodRows[idProdotto] = (typeof(prodRows[idProdotto]) === 'undefined') ? 1 : Number(prodRows[idProdotto]) + 1;
			        	var isProdottoNonEditoriale = (typeof(item.prodottoNonEditoriale) !== 'undefined' && item.prodottoNonEditoriale.toString() == 'true') ? true : false;
						if (isProdottoNonEditoriale) {
							if (prodRows[idProdotto] == 1) {
			        			mapGiacenze['prod_' + idProdotto] = giacenze["pne_" + idProdotto];
							}
			        	} 
						var giacenza = isProdottoNonEditoriale ? mapGiacenze['prod_' + idProdotto] : (giacenze["pub_" + idtn] - idtnRows[idtn]);
						if (isProdottoNonEditoriale) {
							mapGiacenze['prod_' + idProdotto] = getLastGiacenza(idProdotto) - 1;
						}
			        	
			        	venditeTableObject.store.setValue(item, "giacenzaIniziale",  giacenza);
			        }
				});
			});
		});	
	}
	
	var tab = dijit.byId('pubblicazioniTable');
	dojo.connect(tab, "onRowClick", function () { onSelectPubblicazioniTable(); });
	
	dojo.connect(tab, "onKeyDown", tab, function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);  
		if (keycode == 13) {
			var row = this.getRowNode(this.focus.rowIndex);
			row.trigger('click');
		} else if (keycode == 27) {
			$("#inputText").focus();
			$('#close').trigger('click');
		}
	});
	
	var tabRicariche = dijit.byId('ricaricheTable');
	dojo.connect(tabRicariche, "onRowClick", function () { onSelectRicaricheTable(); });
	dojo.connect(tabRicariche, "onKeyDown", tab, function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);  
		if (keycode == 13) {
			// 
		} else if (keycode == 27) {
			$("#inputText").focus();
			$('#close').trigger('click');
		}
	});
	
	var tabBarcode = dijit.byId('pubblicazioniTableBarcode');
	dojo.connect(tabBarcode, "onRowClick", function () { onSelectPubblicazioniBarcodeTable(); });
	dojo.connect(tabBarcode, "onKeyDown", tabBarcode, function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);  
		if (keycode == 13) {
			var row = this.getRowNode(this.focus.rowIndex);
			row.trigger('click');
		} else if (keycode == 27) {
			$("#inputText").focus();
			$('#close').trigger('click');
		}
	});
	
	var tabRBarcode = dijit.byId('pubblicazioniTableRichiediBarcode');
	dojo.connect(tabRBarcode, "onRowClick", function () { onSelectPubblicazioniRichiediBarcodeTable(); });
	dojo.connect(tabRBarcode, "onKeyDown", tabRBarcode, function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);  
		if (keycode == 13) {
			var row = this.getRowNode(this.focus.rowIndex);
			row.trigger('click');
		} else if (keycode == 27) {
			$("#inputText").focus();
			$('#close').trigger('click');
		}
	});
	
	dojo.connect(venditeTableObject, "onRowMouseOver", venditeTableObject, function(event) {
		rowIndex = event.rowIndex;
	});
	
	dojo.connect(venditeTableObject, "onKeyDown", venditeTableObject, function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);  
		if (keycode == 46) {	
	    	deleteRow(this.focus.rowIndex);
		} else if (keycode == 27) {
			$("#inputText").focus();
		}
	});
	
	
	$("#imgPrint, #totaleScontrino, #totalePubb, #totale").tooltip({
		delay: 0, 
	    showURL: false
	});	 
	
	$("#ulButtons img").tooltip({
		delay: 0, 
	    showURL: false
	});	 
	
	if ($("#mostSoldBarDiv").length > 0) {
		$("#mostSoldBarDiv").draggable({
			cursor: 'move', 
			cancel: ".mostSoldBarImg",
		    stop: function(event, ui) {
		        mostSoldBarPos = ui.position;
		    }
		});

		$("#mostSoldBarDiv").resizable({
			grid: [85, 85],
			minWidth: 85,
			minHeight: 85, 
			ghost: true,
			stop: function(event, ui) {
				mostSoldBarSizes = ui.size;
		    }
		});
	} 
	$("#mostSoldBarDiv").css({'z-index':99999});
	
	if (positionSizeDtoExists) {
		$("#mostSoldBarDiv").css({"position":"absolute", "top": positionSizeDtoTop, "left": positionSizeDtoLeft });
		$("#mostSoldBarDiv").width(positionSizeDtoWidth);
		$("#mostSoldBarDiv").height(positionSizeDtoHeight);
		$("#mostSoldBarDiv").trigger("resize");
	}
	
	clearFileds();																	
	
	$('#inputText').focus();   						           
});			

$(document).ready(function() {
	regCassaApplet = document.getElementById("regCassaApplet");
	$("#idCliente").css({"width":$("#inputText").width()});
	abilitaResto = $("#abilitaResto").attr("checked");
	includiPubblicazioniScontrino = $("#includiPubblicazioniScontrino").length > 0 ? $("#includiPubblicazioniScontrino").attr("checked") : false;
	ricercaProdottiVari = $("#ricercaProdottiVari").attr("checked");
	width1 = (screen.width > 1440) ? '62%' : '61%';
	width2 = (screen.width > 1440) ? '80%' : '79%';
	width3 = (screen.width > 1440) ? '98%' : '97%';
	$(document)[0].oncontextmenu = function() {return false;}; 
	showCats();
	var butCount = 3;
	if ($(window).width() < 1000) {
		butCount = 2;
	} else if ($(window).width() > 1440) {
		butCount = 4;
	}
	var width = Math.floor(($("#sidebarL").width() - scrollbarWidth) / butCount);
	$("#ulButtons").find("img").each(function() {
		$(this).css({"width":width,"height":width});
	});
	prodIconWidth = Math.floor(($("#sidebar").width() - scrollbarWidth) / butCount);
	prodIconHeight = prodIconWidth;
	$("#pneDiv").find("img").each(function() {
		$(this).css({"width":prodIconWidth,"height":prodIconHeight});
	});
	
	$(this).bind('keydown', handleKeyDown);
	
	$.alt('A', function() {
		doAzzeraConto();
	});

	$.alt('V', function() {
		vendutoGiornaliero();
	});
	
	$.altPlus(function() {
		var val = isNaN($("#qta").val()) ? 0 : Number($("#qta").val());
		$("#qta").val(++val);
	}, null);
	
	$.altMinus(function() {
		var val = isNaN($("#qta").val()) ? 0 : Number($("#qta").val());
		$("#qta").val(--val);
	}, null);
	
	$("#inputText").keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode); 
		if (keycode == 13) {
			event.preventDefault();
			doInputTextEnterKeyPress();				
		}    
		if (keycode == 27) { 
			setModalita('CONSEGNA');
        }
		if (keycode == 37 && !event.shiftKey) { // left arrow
			event.preventDefault();
			$("#qta").focus();
		} 
	});
	
	$("#qta").keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);  
		if (keycode == 39) { // right arrow
			event.preventDefault();
			$("#inputText").focus();
		}
	});
	
	$("#idCliente").val(codCliente != '' ? Number(codCliente) : -1);
	$("#contoCliente").html(codCliente != '' ? contoDelCliente.replace('{0}', contoCliente != "" ? contoCliente : "") : '');
	$("#estrattoConto").attr("disabled", (idContoCliente != '') ? false : true);
	$("#idCliente").change(function() {
		var val = $(this).val();
		var deferred = service.getClienteConEstrattoConto(val);
		deferred.addCallback(function(res) {
			clienteConEstrattoConto = res;
		});	
		var clienteText = (val == '' || val == -1) ? "" : contoDelCliente.replace('{0}', $(this).find("option:selected").text());
		$("#contoCliente").html(clienteText);
		if (val == -1) {
			codCliente = '';
			$("#estrattoConto").attr("disabled", true);
		} else {
			$("#estrattoConto").attr("disabled", false);
			codCliente = val;
		}
		$("#inputText").focus();
	});
	$("#abilitaResto").change(function() {
		if ($(this).attr("checked") == true) {
			abilitaResto = true;
		} else {
			abilitaResto = false;
		}
		$("#inputText").focus();
	});
	if ($("#includiPubblicazioniScontrino").length > 0) {
		$("#includiPubblicazioniScontrino").change(function() {
			if ($(this).attr("checked") == true) {
				includiPubblicazioniScontrino = true;
			} else {
				includiPubblicazioniScontrino = false;
			}
			$("#inputText").focus();
		});
	}
	$("#aggiornaBarcode").attr("checked", false);
	$("#qta").val('1');
	$("#venditeIGerivCard").hide();
	if (ricercaProdottiVari) {
		$("#inputText").focus(function() {
			$(this).css('background','#ffcccc');
    	});
	}
	
	$(function() {
		$("#thumbBox").draggable();
		$("#popup_name").draggable({cursor: 'move', cancel: "div[class*=Scroll]", cancel: "#mainDiv"});
		$("#dialogContent").draggable({cursor: 'move', cancel: "div[class*=Scroll]"}); 
		$("#treeContiContainer").draggable({cursor: 'move', cancel: "#treePlaceHolder"});
	});
	
	addRightClickMenu();
	
	objMain = $('#main');
	objSeparator = $('#separatorR');
	objSeparatorL = $('#separatorL');
	objSeparator.click(function(e){
	    e.preventDefault();
	    if (objMain.hasClass('use-sidebar') ){
	        hideSidebar();
	        $(this).tooltip({
	    		delay: 0,  
	    	    showURL: false,
	    	    bodyHandler: function() { 
	    	    	return "<b>" + msgMostraBarraProdottiVari + "</b>";
	    	    }
	    	});
	    }
	    else {
	        showSidebar();
	        $(this).tooltip({
	    		delay: 0,  
	    	    showURL: false,
	    	    bodyHandler: function() { 
	    	    	return "<b>" + msgNascondiBarraProdottiVari + "</b>";
	    	    } 
	    	});
	    }
	});
	objSeparatorL.click(function(e){
	    e.preventDefault();
	    if (objMain.hasClass('use-sidebar-L') ){
	    	hideSidebarL();
	    	 $(this).tooltip({
	    		delay: 0,  
	    	    showURL: false,
	    	    bodyHandler: function() { 
	    	    	return "<b>" + msgMostraBarraPubblicazioni + "</b>";
	    	    }
	    	});
	    }
	    else {
	    	showSidebarL();
	    	 $(this).tooltip({
	    		delay: 0,  
	    	    showURL: false,
	    	    bodyHandler: function() { 
	    	    	return "<b>" + msgNascondiBarraPubblicazioni + "</b>";
	    	    }
	    	});
	    }
	});
});

function getDateFromLocalStorageItem(item) {
	var itemDate = item.strData.split("-");
	return new Date(Number(itemDate[2]), (Number(itemDate[1]) - 1), Number(itemDate[0]), 0, 0, 0, 0).getTime();
}

function getLocalDataLastKey() {
	for (var i = (localStorage.length - 1); i >= 0; i--) {
		var key = localStorage.key(i);
		if (key.indexOf('igeriv-') != -1) {
			return key;
		}
	}
}

function getLocalDataFirstItem() {
	for (var i = 0; i < localStorage.length; i++) {
		var key = localStorage.key(i);
		if (key.indexOf('igeriv-') != -1) {
			return JSON.parse(localStorage.getItem(key));
		}
	}
	return null;
}

function loadLocalStorage() {
	if (useLocalStorage && ('localStorage' in window) && (window['localStorage'] !== null)) {
		var firstItem = getLocalDataFirstItem();
		var itemDateMills = firstItem != null ? getDateFromLocalStorageItem(firstItem) : null;
		var todayMills = new Date().setHours(0,0,0,0);
		if (itemDateMills == null || itemDateMills != todayMills) {
			block();
			var deferred = service.getLocalDataVendite();
			deferred.addCallback(function(bean) {
				for (var i = 0; i < bean.length; i++) {
					var data = bean[i];	
					try {
						localStorage.setItem('igeriv-' + data.barcode, JSON.stringify(data));
					} catch (e) {
						if (e == QUOTA_EXCEEDED_ERR) {
							jConfirm(quotaExceededConfirmMsg, attenzioneMsg, function(r) {
							    if (r) { 
							    	localStorage.clear();
							    	loadLocalStorage();
							    } else {
							    	localStorage.removeItem(getLocalDataLastKey());
									localStorage.setItem('igeriv-' + data.barcode, JSON.stringify(data));
							    }
							});
						}
					}
				}
				unBlock();
			});	
		} else {
			jConfirm(localStorageAlreadyUpdatedConfirmMsg, attenzioneMsg, function(r) {
			    if (r) { 
			    	localStorage.clear();
			    	loadLocalStorage();
			    }
			    $("#inputText").focus();
			}, true, false);
			setTimeout(function() {
				$("#popup_cancel").focus();
			}, 200);
		}
	}
}

function hasClienteCondificato() {
	return (codCliente != '' && $("#idCliente").val() != -1 && $("#contoCliente").html() != ''); 
}

function doInputTextEnterKeyPress() {
	var val = $("#inputText").val();
	var inputVal = parseLocalNum(val);
	var multiplier = $("#qta").val().trim();
	if (isNaN(multiplier)) {
		$.alerts.dialogClass = "style_1";
		jAlert(moltiplicatoreInvalidoMsg, attenzioneMsg.toUpperCase(), function() {
			$.alerts.dialogClass = null;
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
	if (Number(multiplier) > maxValueMultiplier) {
		PlaySound('beep3');
		jConfirm(moltiplicatoreMoltoGrandeMsg.replace('{0}',multiplier), attenzioneMsg, function(r) {
		    if (r) { 
		    	$("#inputText").val(parseLocalNum(val));
		    	refreshDataVendite(null);	
		    } else {
		    	$("#qta").val('1');
		    	$("#inputText").focus();
		    }
		});
	} else {
		var val1 = /[a-zA-Z]/.test(val) ? val : parseLocalNum(val);
		$("#inputText").val(val1);
		refreshDataVendite(null);	
	}
}

window.onbeforeunload = function() { 
	if (service)  {
		service.setMostraNascodiBarreLaterali($("#sidebarL").is(":visible"), $("#sidebar").is(":visible"));
		var count = getStoreLength(venditeTableObject);
		if (count > 0) {
			return refreshMsg; 
		}
	}
};	

function vendiProdottoDaBarraSceltaRapida(barcode, idtn) {
	var multiplier = $("#qta").val().trim();
	if (isNaN(multiplier)) {
		jAlert(moltiplicatoreInvalidoMsg, attenzioneMsg.toUpperCase(), function() {
			$.alerts.dialogClass = null;
			$("#qta").focus();
		});
		return false;
	}
	if (Number(multiplier) > maxValueMultiplier) {
		PlaySound('beep3');
		jConfirm(moltiplicatoreMoltoGrandeMsg.replace('{0}',multiplier), attenzioneMsg, function(r) {
		    if (r) {
		    	if (barcode.length > 0) {
					$('#inputText').val(barcode);
					refreshDataVendite(null);
				} else if (idtn.length > 0) {
					block();
					var deferred = service.getPubblicazioneByIdtn(idtn, Number(multiplier));		
					deferred.addCallback(rowsCallback);	
					$('#inputText').focus();
				} else {
					$.alerts.dialogClass = "style_1";
					jAlert(pubNonDisponibileMsg, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						$('#inputText').focus();
					});
				}
		    } else {
		    	$("#qta").val('1');
		    	$("#inputText").focus();
		    }
		});
	} else {
		if (barcode.length > 0) {
			$('#inputText').val(barcode);
			refreshDataVendite(null);
		} else if (idtn.length > 0) {
			block();
			var deferred = service.getPubblicazioneByIdtn(idtn, Number(multiplier));		
			deferred.addCallback(rowsCallback);	
			$('#inputText').focus();
		} else {
			$.alerts.dialogClass = "style_1";
			jAlert(pubNonDisponibileMsg, attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
				$('#inputText').focus();
			});
		}
	}
}

function showConti() {						
	showDialog('treeContiContainer');
	refreshDataConti();			
}			

function showEstrattoContoCliente() {
	if (hasClienteCondificato()) {
		openDiv('popup_name', 750, 450, appContext + namespace + '/estrattoContoVenditeClientiEdicola_showFilter.action?codCliente=' + codCliente + "&nomeCliente=" + escape($("#idCliente").find("option:selected").text()));
	}
}

function print() {
	if (getStoreLength(venditeTableObject) > 0) {		
		printReport(hasVenditeProdottiVariConIva());
	}
}

function hasVenditeProdottiVariConIva() {
	var completed = false;
	var hasProdotti = false;
	venditeTableObject.store.fetch({ query: { },  
        onItem: function(item) {
        	var isProdottoNonEdit = typeof(item.prodottoNonEditoriale) === 'undefined' || item.prodottoNonEditoriale == '' ? false : (item.prodottoNonEditoriale == 'true' ? true : false);
        	//var esenteIva = typeof(item.aliquota) === 'undefined' || item.aliquota == '' || item.aliquota == 0 ? true : false;
    		if (isProdottoNonEdit /*&& !esenteIva*/) {
    			hasProdotti = true;
    		}
         },
         onComplete: function() {
        	 completed = true;
         }
	});
	while (true) { if (completed) break; }
	return hasProdotti;
}

function enableFields() {
	$('#totale').removeAttr('disabled'); 
	$('#totaleScontrino').removeAttr('disabled');
	$('#totalePubb').removeAttr('disabled');
	$('#contanti').removeAttr('disabled'); 
	$('#resto').removeAttr('disabled');  
	$('#debito').removeAttr('disabled'); 
}

function clearFileds() {
	$('#totalePezzi').val("");
	$('#totaleScontrino').val("");
	$('#totalePubb').val("");
	$('#totale').val(""); 
	$('#contanti').val(""); 
	$('#resto').val(""); 
	$('#debito').val("");
	$('#idConto').val("");
	$('#closedAccount').val("");
	$('#totale').attr("readonly","true");
	$('#totaleScontrino').attr("readonly","true");
	$('#totalePubb').attr("readonly","true");
	$('#contanti').attr("readonly","true"); 
	$('#resto').attr("readonly","true");  
	$('#debito').attr("readonly","true"); 
	$('#inputText').removeAttr('readonly');
	enableFields();
	$('#closedAccount').val("");
	setTableData(venditeTableObject, venditeTabIdField, []);
	setTableData(dijit.byId('pubblicazioniTable'), pubblicazioniTabIdField, []);
}		

function doAzzeraConto() {
	PlaySound('beep3');
	jConfirm(azzeraContoMsg, attenzioneMsg, function(r) {
		try {
		    if (r) { 
		    	refreshGiacenze();
		    	resetFields();
		    } else {
		    	$("#inputText").focus();
		    }
		} finally {
			unBlock();
		}
	});
}	

function refreshGiacenze() {
	venditeTableObject.store.fetch({ query: { },  
        onItem: function(item) {
        	var isProdottoNonEdit = typeof(item.prodottoNonEditoriale) === 'undefined' ? false : item.prodottoNonEditoriale;
    		var idProd = typeof(item.idProdotto) === 'undefined' ? 0 : item.idProdotto;
    		if (isProdottoNonEdit) {
    			mapGiacenze['prod_' + idProd] = getLastGiacenza(idProd) + 1;
    		}
         }
	});
}

function doCloseAccount() {
	var closedAccount = $('#closedAccount').val();		            	
	if (closedAccount != 'true') {
    	var mainTab = $('#venditeTable tbody tr');
		if (mainTab.length == 0) {
			setTimeout(function() {
				$.alerts.dialogClass = "style_1";
				jAlert(nessunContoApertoMsg, attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					$('#inputText').focus();
				});
			}, 200);
			return false;
		}	
		if (!abilitaResto) {
			chiudiConto(false, 0);
		} else {
    		$('#closedAccount').val("true");
    		$('#contanti').removeAttr('readonly');
    		$('#inputText').attr('readonly','true');
    		enableFields();
    		$('#contanti').focus();
		}
	} else {
		$('#closedAccount').val("false");
		$('#contanti').val("");
		$('#resto').val("");
		$('#debito').val("");
		$('#contanti').attr('readonly','true');
		$('#inputText').removeAttr('readonly');
		enableFields();
		$('#inputText').focus();
	}
}	

function handleKeyDown(event) { 
	var keycode = (event.keyCode ? event.keyCode : event.charCode); 
	switch (keycode) {
		case 32: { 		// space : close account
			if ($("#popup_prompt").length == 0 && $("#inputText").val().trim() == '') {
		    	event.preventDefault(); 
		    	doCloseAccount();  	
			}
			break;
		}
		case 38:		// arrow up
		case 40: {		// arrow down
			event.preventDefault();
			var tab = venditeTableObject;
			if ($("#dialogContentRicariche").is(':visible')) {
				tab = dijit.byId('ricaricheTable');
			} else if ($("#dialogContent").is(':visible')) {
				tab = dijit.byId('pubblicazioniTable');
			} else if ($("#dialogContentBarcode").is(':visible')) {
				tab = dijit.byId('pubblicazioniTableBarcode');
			} else if ($("#dialogContentRichiediBarcode").is(':visible')) {
				tab = dijit.byId('pubblicazioniTableRichiediBarcode');
			}
			tab.focus.setFocusIndex(0, 0);
			break;
		}
	}
}		

function verifyInputContent() {
	var inputVal = parseLocalNum($("#inputText").val());
	if (isNaN(inputVal) && inputVal.length < 4) {			
		return false;
	}
	return true;
}

$('#contanti').keydown(function(event) {
	var keycode = (event.keyCode ? event.keyCode : event.charCode);
	if (keycode == '13') {			
		event.preventDefault();	
		var debito = ($('#debito').val().indexOf(',') != -1) ? parseLocalNum($('#debito').val()) : $('#debito').val();
		if (debito > 0) {
			setTimeout(function() {
				$.alerts.dialogClass = "style_1";
				jAlert(importoMinoreTotaleMsg, attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					$('#contanti').focus();
				});
			}, 200);
			return false;
		}				
		PlaySound('beep3');
		setTimeout(function() {
			jConfirm(chiudiContoMsg, attenzioneMsg, function(r) {
			    if (r) {
			    	chiudiConto(false, 0);
			    } else {
			    	$("#inputText").focus();
			    }
			});
		}, 200);
	} 
});	

$('#contanti').keyup(function(event) {
	var contanti = ($(this).val().indexOf(',') != -1) ? parseLocalNum($(this).val()) : $(this).val();
	if (!isNaN(contanti)) {
		var totale = ($("#totale").val().indexOf(',') != -1) ? parseLocalNum($("#totale").val()) : $("#totale").val();			
		var resto = ((contanti - totale) < 0 ? 0 : (contanti - totale)).toFixed(2); 	
		var residuo = ((totale - contanti) < 0 ? 0 : (totale - contanti)).toFixed(2);
		$('#resto').val(displayNum(resto));	
		$('#debito').val(displayNum(residuo));
	}		
});			

function populateTablePubblicazioni(bean, tableId) {
	var tab = dijit.byId(tableId);
	setTableData(tab, pubblicazioniTabIdField, bean);
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

var contiCallback = function(bean) {
	var jsonStr = "{";
	jsonStr += "\"identifier\": \"id\",";
	jsonStr += "\"label\": \"name\",";
	jsonStr += "\"items\": [";				
	var conti = bean.result;
	if (conti == null) {
		contiTreeObject.model.store = new dojo.data.ItemFileWriteStore({
			data: []
		});	;
		contiTreeObject.refreshModel();
		return false;
	}
	var childCount = 0;
	for (var i = 0; i < conti.length; i++) {
		var conto = conti[i];		
		var codVendita = conto.codVendita;
		var codCliente = conto.codCliente;	
		var contoCliente = conto.contoCliente;
		var trasferitaGestionale = conto.trasferitaGestionale;		
		var dataEstrattoConto = conto.dataEstrattoContoFormat;
		var data = conto.dataVenditaFormat;
		var importoTotale = displayNum(conto.sommaVenduto.toFixed(2));
		var idScontrino = conto.idScontrino;
		var fatturaEmessa = conto.fatturaEmessa;
		var fatturaContoUnico = conto.fatturaContoUnico;
		var numeroFattura = conto.numeroFattura;
		var parentTitle = data + " (" + "&#8364;&nbsp;" + importoTotale + ")";		
		if (i > 0 && i < conti.length) {
			jsonStr += ",";
		}
		jsonStr += "{ \"type\": \"parent\", \"name\": \"" + parentTitle + "\", \"id\": \"parent" + i + "\"";	
		jsonStr += ",\"codVendita\": \"" + codVendita + "\",\"numeroFattura\": \"" + numeroFattura + "\",\"fatturaContoUnico\": \"" + fatturaContoUnico + "\",\"fatturaEmessa\": \"" + fatturaEmessa + "\",\"idScontrino\": \"" + idScontrino + "\", \"codCliente\": \"" + codCliente + "\", \"trasferitaGestionale\": \"" + trasferitaGestionale + "\", \"dataEstrattoConto\": \"" + dataEstrattoConto + "\", \"contoCliente\": \"" + contoCliente + "\"";
		
		if (conto.listVenditaDettaglio.length > 0) {
			jsonStr += ",\"children\":[";
		} else {
			jsonStr += "}";
		}	
		for (var y = 0; y < conto.listVenditaDettaglio.length; y++) {
			var vendita = conto.listVenditaDettaglio[y];				
			var progressivo = (vendita.progressivo != null) ? vendita.progressivo : '';
			var prodottoNonEditoriale = vendita.prodottoNonEditoriale;
			var idProdotto = (vendita.idProdotto != null) ? vendita.idProdotto : '';
			var barcode = (vendita.barcode != null) ? vendita.barcode : '';
			var quantita = (vendita.quantita != null) ? vendita.quantita : '';
			var idtn = (vendita.idtn != null) ? vendita.idtn : '';	
			var codFiegDl = (vendita.codFiegDl != null) ? vendita.codFiegDl : '';	
			var prezzoCopertina = (vendita.prezzoCopertina != null) ? vendita.prezzoCopertina : '';				
			var dataUscita = (vendita.dataUscita != null) ? vendita.dataUscita : '';								
			var titolo = (vendita.titolo != null) ? vendita.titolo : '';							
			var sottoTitolo = (vendita.sottoTitolo != null) ? vendita.sottoTitolo : '';				
			var numeroCopertina = (vendita.numeroCopertina != null) ? vendita.numeroCopertina : '';
			var giacenzaIniziale = (vendita.giacenzaIniziale != null) ? vendita.giacenzaIniziale : '';
			var aliquota = (typeof(vendita.aliquota) !== 'undefined' && vendita.aliquota != null) ? vendita.aliquota : '';
			var price = vendita.prezzoCopertina.toFixed(2);
			var importoDecimals = vendita.importoTotale.toFixed(2);	
			var prezzoCopertinaFormat = displayNum(price); 
			var importoFormat = displayNum(importoDecimals);
			var childTitle = titolo + ((numeroCopertina != '') ? ("&nbsp;(" + numeroCopertina.trim() + ")") : "") + "&nbsp;" 
									+ "&#8364;&nbsp;" 
									+ importoFormat;									
			if (y > 0 && y < conto.listVenditaDettaglio.length) {
				jsonStr += ",";
			}
			jsonStr += "{\"type\":\"Leaf\", \"name\": \"" + childTitle + "\", \"id\":\"child" + childCount++ + "\"";
			jsonStr += ", \"progressivo\": \"" + progressivo + "\"";
			jsonStr += ", \"prodottoNonEditoriale\": \"" + prodottoNonEditoriale + "\"";
			jsonStr += ", \"idProdotto\": \"" + idProdotto + "\"";
			jsonStr += ", \"barcode\": \"" + barcode + "\"";
			jsonStr += ", \"quantita\": \"" + quantita + "\"";
			jsonStr += ", \"titolo\": \"" + titolo + "\"";
			jsonStr += ", \"sottoTitolo\": \"" + sottoTitolo + "\"";
			jsonStr += ", \"numeroCopertina\": \"" + numeroCopertina + "\"";
			jsonStr += ", \"prezzoCopertina\": \"" + prezzoCopertina + "\"";
			jsonStr += ", \"prezzoCopertinaFormat\": \"" + prezzoCopertinaFormat + "\"";
			jsonStr += ", \"importoFormat\": \"" + importoFormat + "\"";
			jsonStr += ", \"dataUscita\": \"" + dataUscita + "\"";
			jsonStr += ", \"codFiegDl\": \"" + codFiegDl + "\"";
			jsonStr += ", \"giacenzaIniziale\": \"" + giacenzaIniziale + "\"";
			jsonStr += ", \"idtn\": \"" + idtn + "\"";
			jsonStr += ", \"aliquota\": \"" + aliquota + "\"";
			jsonStr += "}";
		}
		if (conto.listVenditaDettaglio.length > 0) {
			jsonStr += "]";
		}
		jsonStr += "}";
	}			
	jsonStr += "]}";								
	treeDat = jQuery.parseJSON(jsonStr);
	var newStore = new dojo.data.ItemFileWriteStore({
		data: treeDat
	});						
	contiTreeObject.model.store = newStore;
	contiTreeObject.model.query = {"type": "parent"};
	contiTreeObject.rootId = "root";
	contiTreeObject.rootLabel = "Groups";
	contiTreeObject.childrenAttrs = ["children"];						
	contiTreeObject.refreshModel();
};

function treeDoubleClick(item) {
	var trasferitaGestionale = item.trasferitaGestionale;	
	var chiusaInEC = item.dataEstrattoConto.toString().length > 0 ? true : false;
	var idScontrino = item.idScontrino.toString();
	var fatturaEmessa = item.fatturaEmessa.toString();
	var fatturaContoUnico = item.fatturaContoUnico.toString();
	var numeroFattura = item.numeroFattura.toString();
	if (chiusaInEC) {
		$.alerts.dialogClass = "style_1";
		jAlert(impossibileEditareEstrattoContoChiusoMsg, attenzioneMsg.toUpperCase(), function() {
			$.alerts.dialogClass = null;
		});
		setTimeout(function() {
			$("#popup_ok").focus();
		}, 100);
		return false;
	} else if (isNaN(trasferitaGestionale) || Number(trasferitaGestionale) > 0) {
		$.alerts.dialogClass = "style_1";
		jAlert(impossibileEditareMsg, attenzioneMsg.toUpperCase(), function() {
			$.alerts.dialogClass = null;
		});
		setTimeout(function() {
			$("#popup_ok").focus();
		}, 100);
		return false;
	} else if (fatturaEmessa == 'true' && fatturaContoUnico == 'false') {
		$.alerts.dialogClass = "style_1";
		jAlert(impossibileEditareContoInFatturaEmessaMsg, attenzioneMsg.toUpperCase(), function() {
			$.alerts.dialogClass = null;
		});
		setTimeout(function() {
			$("#popup_ok").focus();
		}, 100);
		return false;
	} else if (idScontrino.length > 0 && idScontrino != 'null') {
		jConfirm(msgConfirmStornoManualeRicevutaFiscale, attenzioneMsg, function(r) {
		    if (r) { 
		    	dblClickDeleteConto(item);
		    } else {
		    	return false;
		    }
		}, true, false);
		return false;
	} else if (fatturaEmessa == 'true' && fatturaContoUnico == 'true') {
		jConfirm(msgConfirmStornoManualeFattura, attenzioneMsg, function(r) {
		    if (r) { 
		    	dblClickDeleteConto(item, function() {doPrintFattura(item.codCliente, false, STORNO_FATTURA, null, numeroFattura);});
		    } else {
		    	return false;
		    }
		}, true, false);
		return false;
	} else {
		dblClickDeleteConto(item);
	}
}

function dblClickDeleteConto(item, callback) {
	var codice = item.codVendita;
	codCliente = item.codCliente;
	$("#contoCliente").html(typeof(item.contoCliente) !== 'undefined' ? htmlEncode(item.contoCliente.toString()) : '');
	$('#idConto').val(codice);
	var def = service.deleteConto(Number(codice));
	def.addCallback(function(bean) {
		if (bean.type == 'EXCEPTION') {						
			$.alerts.dialogClass = "style_1";
			jAlert(bean.exceptionMessage, attenzioneMsg.toUpperCase(), function() {
				unBlockUI();
				$.alerts.dialogClass = null;
			});
		} else {
			var giacenze = bean.result;
			var children = item.children;
			var pubblicazioni = "[";
			var idtnRows = {};
			var prodRows = {};
			for (var i = 0; i < children.length; i++) {
				var child = children[i];
				var progressivo = child.progressivo;	
				var prodottoNonEditoriale = child.prodottoNonEditoriale;
				var idProdotto = child.idProdotto;
				var barcode = child.barcode.toString();
				var titolo1 = child.titolo.toString();
				var quantita = child.quantita;	
				var sottoTitolo1 = child.sottoTitolo.toString();
				var numeroCopertina1 = child.numeroCopertina.toString();	
				var prezzoCopertinaFormat1 = child.prezzoCopertinaFormat.toString();	
				var importoFormat1 = child.importoFormat.toString();
				var prezzoCopertina1 = child.prezzoCopertina;
				var dataUscita1 = child.dataUscita;
				var idtn1 = child.idtn;
				var aliquota1 = child.aliquota;
				idtnRows[idtn1] = (typeof(idtnRows[idtn1]) === 'undefined') ? 1 : Number(idtnRows[idtn1]) + 1;
				prodRows[idProdotto] = (typeof(prodRows[idProdotto]) === 'undefined') ? 1 : Number(prodRows[idProdotto]) + 1;
				var isProdottoNonEditoriale = (typeof(prodottoNonEditoriale) !== 'undefined' && prodottoNonEditoriale.toString() == 'true') ? true : false;
				if (isProdottoNonEditoriale) {
					if (prodRows[idProdotto] == 1) {
	        			mapGiacenze['prod_' + idProdotto] = giacenze["pne_" + idProdotto];
					}
	        	} 
				var giacenzaIniziale1 = isProdottoNonEditoriale ? mapGiacenze['prod_' + idProdotto] : (giacenze["pub_" + idtn1] - idtnRows[idtn1]);
				if (isProdottoNonEditoriale) {
					mapGiacenze['prod_' + idProdotto] = getLastGiacenza(idProdotto) - 1;
				}
				var codFiegDl1 = child.codFiegDl;
				if (i > 0 && i < children.length) {
					pubblicazioni += ",";
				}
				pubblicazioni += "{\"progressivo\":\"" + progressivo 
									+ "\",\"prodottoNonEditoriale\":\"" + prodottoNonEditoriale
									+ "\",\"idProdotto\":\"" + idProdotto
									+ "\",\"barcode\":\"" + barcode
									+ "\",\"quantita\":\"" + quantita 
									+ "\",\"titolo\":\"" + titolo1.replace(/\&#8364;/g, '\u20AC')
									+ "\",\"sottoTitolo\":\"" + sottoTitolo1.replace(/\&#8364;/g, '\u20AC')
									+ "\",\"numeroCopertina\":\"" + numeroCopertina1 
									+ "\",\"prezzoCopertina\":\"" + prezzoCopertina1 
									+ "\",\"prezzoCopertinaFormat\":\"" + prezzoCopertinaFormat1
									+ "\",\"importoFormat\":\"" + importoFormat1
									+ "\",\"dataUscita\":\"" + dataUscita1 
									+ "\",\"coddl\":\"" + codFiegDl1
									+ "\",\"giacenzaIniziale\":\"" + giacenzaIniziale1
									+ "\",\"aliquota\":\"" + aliquota1 
									+ "\",\"idtn\":\"" + idtn1 + "\"}";
			}
			pubblicazioni += "]";	
			pubblicazioniJSON = jQuery.parseJSON(pubblicazioni);
			setTableData(venditeTableObject, venditeTabIdField, pubblicazioniJSON);
			setTotals();
			setTimeout(function() {
				$("#idCliente").val(codCliente != '' ? Number(codCliente) : -1);
				setScrollBarToBottom();
				addRightClickMenu();
				$('#inputText').val('');
				$('#inputText').focus();
				if (typeof(callback) === 'function') {
					callback();
				}
			}, 100);
			$('#close').trigger('click');
		}
	});
}

function block() {
	if (blockVenditaUntillCallback) {
		ray.ajax();
	}
}

function unBlock() {
	unBlockUI();
	unBlockInputText();
}

var rowsCallback = function(bean) {
	try {
		if (bean.type == 'EXCEPTION') {							
			$.alerts.dialogClass = "style_1";
			jAlert(bean.exceptionMessage, attenzioneMsg.toUpperCase(), function() {
				unBlock();
				$.alerts.dialogClass = null;
				$('#inputText').val('');
				$('#inputText').focus();
			});
		} else if (bean.type == 'CONFIRM_ASSOCIA_BARCODE' || bean.type == 'CONFIRM_INVIA_MESSAGGIO_ASSOCIA_BARCODE') {
			doAssociaBarcode(bean);
			unBlock();
		} else if (bean.type == 'CONFIRM_ABILITARE_IGERIV_CARD') {
			doAssociaIGerivCard(bean);
			unBlock();
		} else {				
			if (bean.type == 'VENDITE_TITOLO') {
				showDialog('dialogContent');
				populateTablePubblicazioni(bean.result, 'pubblicazioniTable');
				$('#inputText').blur();
				document.getElementById('inputText').value = '';
				unBlock();
			} else if (bean.type == 'VENDITE_PREZZO') {
				addTableData(venditeTableObject, venditeTabIdField, bean.result);
				document.getElementById('inputText').value = '';
				$("#qta").val(1);
				unBlock();
			} else if (bean.type == 'VENDITE_BARCODE') {
				if ($("#aggiornaBarcode").is(":checked")) {
					bean.exceptionMessage = (abilitataCorrezioneBarcode == 'true') ? msgConfirmAggiornareAssociazioneBarcode : msgConfirmRichiestaAggiornareAssociazioneBarcode;
					bean.type = (abilitataCorrezioneBarcode == 'true') ? 'CONFIRM_ASSOCIA_BARCODE' : 'CONFIRM_INVIA_MESSAGGIO_ASSOCIA_BARCODE';
					doAssociaBarcode(bean);
					unBlock();
				} else if (typeof(bean.result) != 'undefined' && bean.result.prodottoNonEditoriale) {
					var giacenza = getLastGiacenza(bean.result.idProdotto);
					if (giacenza <= 0) {
						PlaySound('beep3');
						jConfirm(msgProdottoSenzaGiacenza.replace('{0}', bean.result.titolo), attenzioneMsg, function(r) {
						    if (r) {
						    	bean.result.giacenzaIniziale = giacenza;
						    	addSearchRowToTable(bean.result);
						    	document.getElementById('inputText').value = '';
						    	mapGiacenze['prod_' + bean.result.idProdotto] = giacenza - 1;
						    } else {
						    	return false;
						    }
						    unBlock();
						});
					} else {
						bean.result.giacenzaIniziale = giacenza;
						addSearchRowToTable(bean.result);
						document.getElementById('inputText').value = '';
						mapGiacenze['prod_' + bean.result.idProdotto] = giacenza - 1;
						unBlock();
					}
				} else {
					bean.result.progressivo = getProgressivo(venditeTableObject);
					addSearchRowToTable(bean.result);
					document.getElementById('inputText').value = '';
					unBlock();
				}
			} else if (bean.type == 'VENDITE_IDTN') {
				if (bean.result == null || bean.result.length == 0) {
					$.alerts.dialogClass = "style_1";
					jAlert(pubNonDisponibileMsg, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
					});
					unBlock();
					$('#inputText').focus();
					return false;
				}
				bean.result.progressivo = getProgressivo(venditeTableObject);
				addTableData(venditeTableObject, venditeTabIdField, bean.result);
				document.getElementById('inputText').value = '';
				$("#qta").val(1);
				unBlock();
			} else if (bean.type == 'VENDITE_ABBONAMENTO') {
				jAlert(bean.exceptionMessage, alertInfoMsg, function() {
					$('#inputText').val('');
					setModalita('CONSEGNA');
				});
				unBlock();
			} else if (bean.type == 'VENDITE_IGERIV_CARD') {
				$("#contoCliente").html(bean.result.contoNome);
				showDivVenditeIGerivCard(bean);
				codCliente = bean.result.codCliente;
				$("#idCliente").val(Number(codCliente));
				$('#inputText').val('');
				unBlock();
				$('#inputText').focus();
			} else if (bean.type == 'RICARICA_MINICARD') {
				jAlert(bean.exceptionMessage, alertInfoMsg, function() {
					$('#inputText').val('');
					unBlock();
					setModalita('CONSEGNA');
				});
			} else if (bean.type == 'STATO_MINICARD') {
				jAlert(bean.exceptionMessage, alertInfoMsg, function() {
					$('#inputText').val('');
					unBlock();
					setModalita('CONSEGNA');
				});
			} else if (bean.type == 'PUBBLICAZIONI_MULTIPLE') {	
				showDialogProdotti(bean.message);
				unBlock();
			} else if (bean.type == 'CONFIRM_ESEGUI_RICARICA') {	
				showConfirmEseguiRicarica(bean);
				unBlock();
			} else if (bean.type == 'GESTIONE_ARRETRATI') {	
				showDialogGestioneArretrati(bean.message);
				unBlock();
			} else if (bean.type == 'VENDUTO_GIORNALIERO') {	
				jAlert(bean.result, informazioneMsg, function() {
					$('#inputText').val('');
					unBlock();
					$('#inputText').focus();
				});
				document.getElementById('inputText').value = '';
			} else if (bean.type == 'VENDUTO_CONTO') {			
				setTableData(venditeTableObject, venditeTabIdField, bean.result);
				$('#idConto').val(bean.codVendita);
				document.getElementById('inputText').value = '';
				unBlock();
			} else if (bean.type == 'CONTO_CHIUSO') {
				resetFields();
				unBlock();
				$("#inputText").focus();
			}
			addRightClickMenu();
		}
	} finally {
		unBlock();
		idScontrino = '';
		dataScontrino = '';
	}
};	

function resetFields() {
	clearFileds();
	$('#totalePezzi').val("");
	$('#inputText').val("");
	codCliente = '';
	$("#idCliente").val(-1);
	$("#estrattoConto").attr("disabled", true);
	$("#contoCliente").html('');
	$('#inputText').focus(); 
}

function getProgressivo(tab) {
	var prog = 0;
	var maxProg = 0;
	tab.store.fetch({ query: { },  
        onItem: function(item) {
        	prog = Number(item.progressivo);
        	if (prog > maxProg) {
        		maxProg = prog;
        	}
        }
	});
	var max = Number(maxProg) + 1;
	return max;
}

function showDivVenditeIGerivCard(bean) {
	$("#venditeIGerivCard").show();
	var htmlPreferiti = '';
	var ultimiAcquisti = bean.result.ultimiAcquisti;
	var evenColor = '#fdecae;';
	var oddColor = '#FDB5AE;';
	if (ultimiAcquisti != null) {
		for (var i = 0; i < ultimiAcquisti.length; i++) {
			var vendita = ultimiAcquisti[i];		
			var titolo = (vendita.titolo != null && vendita.titolo.trim().length > 0) ? vendita.titolo.trim() : '&nbsp;';	
			var sottoTitolo = (vendita.sottoTitolo != null && vendita.sottoTitolo.trim().length > 0) ? vendita.sottoTitolo.trim() : '&nbsp;';	
			var numero = (vendita.numeroCopertina != null && vendita.numeroCopertina.trim().length > 0) ? vendita.numeroCopertina.trim() : '&nbsp;';
			var bgColor = (i%2 == 0) ? evenColor : oddColor;
			htmlPreferiti += '<span style="float:left; width:45%; white-space:nowrap; font-size:90%; background-color: ' + bgColor + '">' + titolo + '</span><span style="float:left; width:30%; white-space:nowrap; font-size:90%; font-size:90%; background-color: ' + bgColor + '">' + sottoTitolo + '</span><span style="float:left; width:8%; white-space:nowrap; font-size:90%; background-color: ' + bgColor + '">' + numMsg  + '</span><span style="float:left; width:17%; white-space:nowrap; font-size:90%; background-color: ' + bgColor + '">' + numero + '</span>';
		}
	}
	var htmlSuggerimenti = '';
	var suggerimentiVendita = bean.result.suggerimentiVendita;
	if (suggerimentiVendita != null) {
		for (var i = 0; i < suggerimentiVendita.length; i++) {
			var vendita = ultimiAcquisti[i];		
			var titolo = (vendita.titolo != null) ? vendita.titolo : '&nbsp;';							
			var sottoTitolo = (vendita.sottoTitolo != null) ? vendita.sottoTitolo : '&nbsp;';	
			var numero = (vendita.numeroCopertina != null) ? vendita.numeroCopertina : '&nbsp;';
			var bgColor = (i%2 == 0) ? evenColor : oddColor;
			htmlSuggerimenti += '<span style="float:left; width:45%; white-space:nowrap; font-size:90%; background-color: ' + bgColor + '">' + titolo + '</span><span style="float:left; width:30%; white-space:nowrap; font-size:90%; font-size:90%; background-color: ' + bgColor + '">' + sottoTitolo + '</span><span style="float:left; width:8%; white-space:nowrap; font-size:90%; background-color: ' + bgColor + '">' + numMsg  + '</span><span style="float:left; width:17%; white-space:nowrap; font-size:90%; background-color: ' + bgColor + '">' + numero + '</span>';
		}
	}
	$("#preferiti").html(htmlPreferiti);
	$("#suggerimenti").html(htmlSuggerimenti);
	$("#venditeIGerivCard").dialog({
		autoOpen: false,
		title: smartCardEdicola,
		closeText: chiudiLabel,
		height: '380'
	});
	$("#venditeIGerivCard").dialog('open');
}

function doAssociaIGerivCard(bean) {
	PlaySound('beep3');
	jConfirm(bean.exceptionMessage, attenzioneMsg, function(r) {
		setTimeout(function() {
			jPromptSmartEdicola(scegliClienteMsg, msgIGerivCard, clientiEdicola, function(codCliente) {
				if (hasClienteCondificato()) {
					var def = service.associaIGerivCard($("#inputText").val(), codCliente, "false");		
					def.addCallback(associateCallback);
			    } else {
			    	$('#inputText').val('');
					$('#inputText').focus();
			    	return false;
			    }
			});
		}, 100);
	});
}

function associateCallback(bean) {
	if (bean.type == 'EXCEPTION') {
		$.alerts.dialogClass = "style_1";
		jAlert(bean.exceptionMessage, attenzioneMsg.toUpperCase(), function() {
			$.alerts.dialogClass = null;
			$('#inputText').val("");
			return false;
		});
	} else if (bean.type == 'CONFIRM_RIASSOCIARE_IGERIV_CARD') { 
		setTimeout(function() {
			jConfirm(bean.exceptionMessage, attenzioneMsg, function(r) {
			    if (r) { 
			    	var def1 = service.associaIGerivCard($("#inputText").val(), codCliente, "true");	
			    	def1.addCallback(associateCallback);
			    } else {
			    	return false;
			    }
			});
		}, 100);
	} else {
		jAlert(bean.result, informazioneMsg, function() {
			$("#contoCliente").html(bean.contoNome);
			codCliente = bean.codCliente;
			$('#inputText').val("");
	    	return false;
		});
	}
}

function doAssociaBarcode(bean) {
	PlaySound('beep3');
	jConfirm(bean.exceptionMessage, attenzioneMsg, function(r) {
	    if (r) { 
	    	setTimeout(function() {
		    	jPrompt(msgInserisciTitolo, null, msgAggiornamentoBarcode, function(res) {
					if (res != null && res.length > 0) {
						var pars = {inputText:res, soloPubblicazioniBarcodeNullo:true, progressivo:getProgressivo(venditeTableObject), mostraTutteUscite : false};
						var def = service.getRows(pars);		
						def.addCallback(function(b) {
							if (b.type == 'EXCEPTION') {							
								$.alerts.dialogClass = "style_1";
								jAlert(b.exceptionMessage, attenzioneMsg.toUpperCase(), function() {
									unBlock();
									$.alerts.dialogClass = null;
									$('#inputText').val('');
									$('#inputText').focus();
								});
							} else {
								showDialog(bean.type == 'CONFIRM_ASSOCIA_BARCODE' ? 'dialogContentBarcode' : 'dialogContentRichiediBarcode');
								var tabId = bean.type == 'CONFIRM_ASSOCIA_BARCODE' ? 'pubblicazioniTableBarcode' : 'pubblicazioniTableRichiediBarcode';
								populateTablePubblicazioni(b.result, tabId);
							}
						});
					}
				});
	    	}, 100);
	    } else {
	    	$('#inputText').val('');
			$('#inputText').focus();
	    	return false;
	    }
	}, true, false);
}

refreshDataVendite = function(idContoVendite) {
	block();
	var inputVal = document.getElementById('inputText').value;
	if (inputVal == barcodeFinalizza) {
		chiudiConto(false, 0);
	} else {
		var localVenditaStr = (useLocalStorage && ('localStorage' in window) && (window['localStorage'] !== null)) ? localStorage.getItem('igeriv-' + inputVal) : null;
		if (localVenditaStr !== null) {
			var quantita = $("#qta").val();
			if (isNaN(quantita)) {
				jAlert(moltiplicatoreInvalidoMsg, attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					$("#qta").focus();
				});
				return false;
			}
			var localVendita = JSON.parse(localVenditaStr);
			localVendita.progressivo = getProgressivo(venditeTableObject);
			localVendita.quantita = Number(quantita);
			localVendita.prodottoNonEditoriale = "false";
			localVendita.importo = localVendita.quantita * Number(localVendita.prezzoCopertina);
			localVendita.importoFormat = displayNum(localVendita.importo.toFixed(2));
			localVendita.giacenzaIniziale = "-";
			addSearchRowToTable(localVendita);
			unBlock();
		} else {
			var params = {inputText:inputVal, progressivo: getProgressivo(venditeTableObject), idConto:idContoVendite, operation:modalita, quantita:Number($("#qta").val().trim()), mostraTutteUscite : $("#mostraTutteUscite").is(':checked'), ricercaProdottiVari : ricercaProdottiVari};
			var deferred = service.getRows(params);
			deferred.addCallback(rowsCallback);
		}
	}
};

refreshDataConti = function() {
	var deferred = service.getConti();	
	deferred.addCallback(contiCallback);			
};

vendutoGiornaliero = function() {		
	var deferred = service.getVendutoGiornaliero();		
	deferred.addCallback(rowsCallback);		
};

printReport = function(hasVenditeProdottiVariConIva) {
	if (hasVenditeProdottiVariConIva && codCliente != '') {
		var radioName = "tipoDocumento";
		var labels = registratoreCassa != '' && registratoreCassa > 0 ? (clienteConEstrattoConto ? [bollettinaConsegna,ricevutaFiscale] : [bollettinaConsegna,ricevutaFiscale,fattura]) : (clienteConEstrattoConto ? [bollettinaConsegna] : [bollettinaConsegna,fattura]);
		var values = registratoreCassa != '' && registratoreCassa > 0 ? (clienteConEstrattoConto ? [BOLLETTINA_CONSEGNA,RICEVUTA_FISCALE] : [BOLLETTINA_CONSEGNA,RICEVUTA_FISCALE,FATTURA]) : (clienteConEstrattoConto ? [BOLLETTINA_CONSEGNA] : [BOLLETTINA_CONSEGNA,FATTURA]);
		jRadio(msgScegliTipoDocumento, msgTipoDocumento, radioName, labels, values, function(res) {
			if (res != '') {
				switch(Number(res)) {
					case RICEVUTA_FISCALE:
						printRicevutaFiscale();
						break;
					case FATTURA:
						printFattura();
						break;
					case BOLLETTINA_CONSEGNA:
						printBollettinaConsegna();
						break;
				}
			}
		});
	} else {
		printBollettinaConsegna();
	}
};

function printRicevutaFiscale() {
	var datiJSON = getJsonStoreString(venditeTableObject);
	regCassaApplet.emettiScontrino(datiJSON, dojo.toJson(arrAliquoteReparti), clienteConEstrattoConto.toString());
}

function printRicevutaFiscaleCallback(dataScont, idScont) {
	unBlockInputText();
	jAlert(msgRicevutaEmessa, informazioneMsg, function() {
		idScontrino = idScont;
		dataScontrino = dataScont;
		jConfirm(msgConfirmStampareBollettinaConsegna, attenzioneMsg, function(r) {
		    if (r) { 
		    	printBollettinaConsegna(function() {chiudiConto(true, 0);});
		    } else {
		    	chiudiConto(true, 0);
		    	$("#inputText").focus();
		    }
		}, true, true);
    	return false;
	});
}

function alertMissingRegCassaActiveProcess() {
	unBlockInputText();
	$.alerts.dialogClass = "style_1";
	jAlert(msgMissingRegCassaActiveProcess, attenzioneMsg.toUpperCase(), function() {
		$.alerts.dialogClass = null;
    	return false;
	});
}

function showAlertRepartiRegCassa(msg) {
	if (!msgRepartiRegistratoreCassaVisto) {
		$.alerts.dialogClass = "style_1"; 
		jConfirmWithCheckboxCallback(msg, attenzioneMsg.toUpperCase(), function(r) {
			$.alerts.dialogClass = null;
			if (r) {
				regCassaApplet.initRegCassa();
			} else {
				regCassaApplet.stop();
				var def = service.deleteParamRegCassa();		
				def.addCallback(function() {
					window.location.href = appContext + "/vendite_showFilter.action";
				});
			}
		}, msgNonVisualizzarePiuMessaggio, function() {
			service.setMessaggioRegCassaVisto(codRegCassa);
		}); 
		setTimeout(function() {
			$("#popup_ok").focus();
		}, 100);
	}
	return false;
}

function alertUnsupportedOS() {
	unBlockInputText();
	$.alerts.dialogClass = "style_1";
	jAlert(unsupportedOS, attenzioneMsg.toUpperCase(), function() {
		$.alerts.dialogClass = null;
    	return false;
	});
}

function promptForRegCassaLocalInstallPath() {
	unBlockInputText();
	jPrompt(msgConfirmRegCassaInstallPath, null, msgRegCassaInstallPath, function(res) {
		if (res != null && res.length > 0) {
			var def = service.setUserRegCassaLocalDir(res);		
			def.addCallback(function(b) {
				if (b.toString() == 'true') {
					regCassaApplet.setUserRegCassaLocalDir(res);
				} else {
					$.alerts.dialogClass = "style_1";
					jAlert(msgErrorUpdateRegCassaInstallPath, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						$('#inputText').val("");
				    	return false;
					});
				}
			});
		}
	});
	setTimeout(function() {
		$("#popup_prompt").focus();
	}, 200);
}

function promptForRegCassaScontriniPath() {
	unBlockInputText();
	jPrompt(msgConfirmRegCassaScontriniPath, null, msgRegCassaScontriniPath, function(res) {
		if (res != null && res.length > 0) {
			var def = service.setUserRegCassaLocalDir(res);		
			def.addCallback(function(b) {
				if (b.toString() == 'true') {
					regCassaApplet.setScontriniRegCassaLocalDir(res);
				} else {
					$.alerts.dialogClass = "style_1";
					jAlert(msgErrorUpdateRegCassaInstallPath, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						$('#inputText').val("");
				    	return false;
					});
				}
			});
		}
	});
	setTimeout(function() {
		$("#popup_prompt").focus();
	}, 200);
}

function printFattura() {
	var deferred = service.validateDatiFiscaliCliente(codCliente);		
	deferred.addCallback(function(res) {	
		if (res != '') {
			jConfirm(res, attenzioneMsg, function(r) {				
			    if (r) { 
				 	var url = "gestioneClienti_showCliente.action?idCliente=" + codCliente;
					openDiv('popup_name', 900, 400, url, '', '', '', function() {
						clienteEdicolaSuccessCallback = function() {
							unBlockUI();
							doCloseLayer();
							doPrintFattura(codCliente, false, FATTURA, function() {afterPrintFatturaCallback();});
				    	};
				    	validateFieldsClienteEdicola = function() {
				    		var showAlerts = true;
				    		if (validateFieldsClienteBase(showAlerts)) {
					    		if ($("#tipoLocalita").val() == '') {
					    			if (showAlerts) {
					    				$.alerts.dialogClass = "style_1";
					    				jAlert(campoObbligatorio.replace('{0}', indirizzo), attenzioneMsg, function() {
					    					$.alerts.dialogClass = null;
					    					$('#tipoLocalita').focus();
					    				});
					    			}
					    			unBlockUI();
					    			return false;
					    		}
					    		if ($("#cliente\\.numeroCivico").val() == '') {
					    			if (showAlerts) {
					    				$.alerts.dialogClass = "style_1";
					    				jAlert(campoObbligatorio.replace('{0}', numCivicoLabel), attenzioneMsg, function() {
					    					$.alerts.dialogClass = null;
					    					$('#cliente\\.numeroCivico').focus();
					    				});
					    			}
					    			unBlockUI();
					    			return false;
					    		}
					    		if ($("#autocomplete").val() == '') {
					    			if (showAlerts) {
					    				$.alerts.dialogClass = "style_1";
					    				jAlert(campoObbligatorio.replace('{0}', localita), attenzioneMsg, function() {
					    					$.alerts.dialogClass = null;
					    					$('#autocomplete').focus();
					    				});
					    			}
					    			unBlockUI();
					    			return false;
					    		}
					    		if ($("#cliente\\.codiceFiscale").val() == '' && $("#cliente\\.piva").val() == '') {
					    			if (showAlerts) {
					    				$.alerts.dialogClass = "style_1";
					    				jAlert(campoObbligatorio.replace('{0}', codFiscale + ' / ' + piva), attenzioneMsg, function() {
					    					$.alerts.dialogClass = null;
					    					$('#cliente\\.codiceFiscale').focus();
					    				});
					    			}
					    			unBlockUI();
					    			return false;
					    		}
					    		return true;
				    		} else {
				    			return false;
				    		}
				    	};
					});
			    	return false;
			    } else {
			    	$('#inputText').val('');
					$('#inputText').focus();
			    	return false;
			    }
			});
		} else {
			doPrintFattura(codCliente, true, FATTURA, function() {afterPrintFatturaCallback();});
		}
	});
}

function doPrintFattura(codCliente, blockUI, tipoDocumento, afterPrintCallback, numeroFattura) {
	blockUIForDownload($(this));
	populateReportVenditeForm();
	var codClienteInput = '<input type="hidden" name="codCliente" value="' + codCliente + '"/>';		
	var tipoDocInput = '<input type="hidden" name="tipoDocumento" value="' + tipoDocumento + '"/>';
	if (tipoDocumento == STORNO_FATTURA && typeof(numeroFattura) !== 'undefined' && numeroFattura != '' && numeroFattura > 0) {
		var numFatturaInput = '<input type="hidden" name="numFattura" value="' + numeroFattura + '"/>';
		$("#ReportVenditeForm").append(numFatturaInput);
	}
	$("#ReportVenditeForm").append(codClienteInput);
	$("#ReportVenditeForm").append(tipoDocInput);
	$("#ReportVenditeForm").attr("target", "_blank"); 		
	$("#ReportVenditeForm").submit();
	if (blockUI == false) {
		unBlockUI();
	}
	if (typeof(afterPrintCallback) === 'function') {
		afterPrintCallback();
	}
}

function afterPrintFatturaCallback() {
	jConfirm(msgConfirmStampareBollettinaConsegna, attenzioneMsg, function(r) {
		var def = service.getLastNumFatturaEmessaDaUtente();
		def.addCallback(function(lastNumFatturaEmessa) {
			if (r) {
		    	printBollettinaConsegna(function() {chiudiConto(true, lastNumFatturaEmessa);});
		    } else {
		    	chiudiConto(true, lastNumFatturaEmessa);
		    	 $("#inputText").focus();
		    }
		});
	}, true, true);
}

function printBollettinaConsegna(callback) {
	var hasCallback = (typeof(callback) === 'function') ? true : false;
	var printBC = function(dati) {
		var hasCliente = hasClienteCondificato(); 
		var hasDati = typeof(dati) !== 'undefined' && dati.length != 'null';
		if (!hasCliente && !hasDati) {
			if (!hasCallback) {
				chiudiConto(true, 0);
			}
			return false;
		}
		blockUIForDownload($(this));
		populateReportVenditeForm();
		if (hasCliente) {
			var codCliInput = '<input type="hidden" name="codCliente" value="' + codCliente + '"/>';
			$("#ReportVenditeForm").append(codCliInput);
		} else if (hasDati) {
			var ragSocInput = '<input type="hidden" name="ragSocCliente" value="' + dati + '"/>';
			$("#ReportVenditeForm").append(ragSocInput);
		}
		var tipoDocInput = '<input type="hidden" name="tipoDocumento" value="' + BOLLETTINA_CONSEGNA + '"/>';	
		$("#ReportVenditeForm").append(tipoDocInput);
		$("#ReportVenditeForm").attr("target", "_blank");
		$("#ReportVenditeForm").submit();
		if (hasCallback) {
			callback();
		}
	};
	if (hasClienteCondificato()) {
		printBC();
	} else {
		jPrompt(ragSocialeClienteIndirizzoDatiFiscali,'',datiCliente, printBC, true, 4, 60);
	}
}

function populateReportVenditeForm() {
	var token = $('#downloadToken').val() == '' ? new Date().getTime() : $('#downloadToken').val();
	$("#ReportVenditeForm").empty();
	var hasProdottoNonEditConIva = false;
	var hasProdottoIvaAssolta = false;
	var i = 0;
	venditeTableObject.store.fetch({ query: { },  
        onItem: function(item) {
        	var prog = item.progressivo;
    		var idtnPub = item.idtn;			
    		var prezzoCopertina = item.prezzoCopertina;	
    		var tit = item.titolo.toString();
    		var sottotit = item.sottoTitolo.toString();
    		var dataUscita = typeof(item.dataUscitaFormat) !== 'undefined' ? item.dataUscitaFormat.toString() : '';
    		var isProdottoNonEdit = typeof(item.prodottoNonEditoriale) === 'undefined' ? false : (item.prodottoNonEditoriale == 'true' ? true : false);
    		var numcop = item.numeroCopertina;
    		var quantita = item.quantita;
    		var aliquota = typeof(item.aliquota) === 'undefined' || item.aliquota == '' ? 0 : Number(item.aliquota);
    		var progElem = '<input type="hidden" name="vendite[' + i + '].progressivo" value="' + prog + '"/>';
    		var idtnElem = '<input type="hidden" name="vendite[' + i + '].idtn" value="' + idtnPub + '"/>';
    		var importoElem = '<input type="hidden" name="vendite[' + i + '].importo" value="' + prezzoCopertina + '"/>';
    		var qtaElem = '<input type="hidden" name="vendite[' + i + '].copie" value="' + quantita + '"/>';
    		var dataUscitaElem = '<input type="hidden" name="vendite[' + i + '].dataUscita" value="' + dataUscita + '"/>';
    		var titoloElem = '<input type="hidden" name="vendite[' + i + '].titolo" id="tit' + i + '" value="' + tit.replace(/\&#8364;/g,'Euro').replace(/\u20ac/g,'Euro') + '"/>';
    		var sottoTitoloElem = '<input type="hidden" name="vendite[' + i + '].sottoTitolo" value="' + sottotit.replace(/\&#8364;/g,'Euro').replace(/\u20ac/g,'Euro') + '"/>';
    		var numeroCopertinaElem = '<input type="hidden" name="vendite[' + i + '].numeroCopertina" value="' + numcop + '"/>';
    		var aliquotaElem = '<input type="hidden" name="vendite[' + i + '].aliquota" value="' + aliquota + '"/>';
    		var pneElem = '<input type="hidden" name="vendite[' + i + '].prodottoNonEditoriale" value="' + isProdottoNonEdit + '"/>';
    		hasProdottoNonEditConIva = (isProdottoNonEdit && (aliquota > 0)) ? true : hasProdottoNonEditConIva;
    		hasProdottoIvaAssolta = (aliquota == 0) ? true : hasProdottoIvaAssolta;
    		$("#ReportVenditeForm").append(progElem);
    		$("#ReportVenditeForm").append(idtnElem);
    		$("#ReportVenditeForm").append(importoElem);
    		$("#ReportVenditeForm").append(qtaElem);
    		$("#ReportVenditeForm").append(titoloElem);
    		$("#ReportVenditeForm").append(sottoTitoloElem);
    		$("#ReportVenditeForm").append(dataUscitaElem);
    		$("#ReportVenditeForm").append(numeroCopertinaElem);
    		$("#ReportVenditeForm").append(aliquotaElem);
    		$("#ReportVenditeForm").append(pneElem);
    		$("#ReportVenditeForm").append('<input type="hidden" name="vendite[' + i + '].codCliente" value="' + codCliente + '"/>');
    		i++;
         }
	});
	$("#ReportVenditeForm").append('<input type="hidden" name="downloadToken" id="downloadToken" value="' + token + '"/>');
	$("#ReportVenditeForm").append('<input type="hidden" name="hasProdottiMisti" value="' + (hasProdottoNonEditConIva && hasProdottoIvaAssolta) + '"/>');	
}

function blockInputText() {
	$("#inputText").attr("disabled", true);
}

function unBlockInputText() {
	$("#inputText").attr("disabled", false);
}

function chiudiConto(byPassEmissioneScontrino, numeroFatturaEmessa) {
	var len = getStoreLength(venditeTableObject);
	if (len > 0) {
		blockInputText();
		ray.ajax();
	}
	var byPass = typeof(byPassEmissioneScontrino) === 'undefined' ? true : byPassEmissioneScontrino;
	if ((byPass == false) && (hasRegistratoreCassa == true) && (hasVenditeProdottiVariConIva() || (includiPubblicazioniScontrino == true))) {
		printRicevutaFiscale();
	} else {
		doChiudiConto(numeroFatturaEmessa);
	}
};

function doChiudiConto(numeroFatturaEmessa) {
	if (hasClienteCondificato()) {
		setTimeout(function() {
			if (clienteConEstrattoConto) {
				executeChiudiConto(false, numeroFatturaEmessa);
			} else {
				executeChiudiConto(true, numeroFatturaEmessa);
			}
			$("#inputText").focus();
		}, 200);
	} else {
		executeChiudiConto(true, numeroFatturaEmessa);
	}
}

function executeChiudiConto(contoPagato, numeroFatturaEmessa) {
	var numFatt = (typeof(numeroFatturaEmessa) !== 'undefined' && numeroFatturaEmessa > 0) ? numeroFatturaEmessa : 0; 
	var params = new Array();
	venditeTableObject.store.fetch({ query: { },  
        onItem: function(item) {
        	var progressivo = item.progressivo.toString();
    		var idtnPub = item.idtn.toString();
    		var prezzoCopertina = item.prezzoCopertina.toString();	
    		var tit = item.titolo.toString();
    		var sottotit = item.sottoTitolo.toString();
    		var numcop = item.numeroCopertina.toString();
    		var qta = Number(item.quantita);
    		var coddlPub = isNaN(item.coddl) ? 0 : Number(item.coddl);
    		var isProdottoNonEdit = typeof(item.prodottoNonEditoriale) === 'undefined' ? false : (item.prodottoNonEditoriale == 'true' ? true : false);
    		var idProd = typeof(item.idProdotto) === 'undefined' ? 0 : Number(item.idProdotto);
    		params.push({prodottoNonEditoriale:isProdottoNonEdit,progressivo:progressivo,idtn:idtnPub,codProdottoNonEditoriale:idProd,importo:prezzoCopertina,titolo:tit,sottoTitolo:sottotit,numeroCopertina:numcop,quantita:qta,codFiegDlPubblicazione:coddlPub});
        }
    });
	setTimeout(function() {
		var len = getStoreLength(venditeTableObject);
		if (len > 0) {
			block();
			var deferred = service.chiudiConto(params, $('#idConto').val(), (typeof(codCliente) === 'undefined' || codCliente == '') ? null : Number(codCliente), parseLocalNum($("#totale").val()), parseLocalNum($("#totaleScontrino").val()), includiPubblicazioniScontrino.toString(), contoPagato.toString(), idScontrino, dataScontrino, numFatt);	
			deferred.addCallback(rowsCallback);
		}
	}, 100);
}

onSelectPubblicazioniTable = function(event) {	
	var tab = dijit.byId('pubblicazioniTable');
	var selectedBean = tab.getItem(tab.focus.rowIndex);	
	var isProdottoNonEdit = typeof(selectedBean.prodottoNonEditoriale) !== 'undefined' && selectedBean.prodottoNonEditoriale.toString() == "true";
	tab.store.setValue(selectedBean, "progressivo", getProgressivo(venditeTableObject));
	if (!isProdottoNonEdit) {
		var deferred = service.getGiacenzaPubblicazione(Number(selectedBean.idtn), Number(selectedBean.coddl));		
		deferred.addCallback(function(giac) {
			tab.store.setValue(selectedBean, "giacenzaIniziale", Number(giac));
			var beanJson = itemToJSON(tab.store, selectedBean);
			var bean = dojo.fromJson(beanJson);
			addSearchRowToTable(bean);	
		});	
	} else {
		var beanJson = itemToJSON(tab.store, selectedBean);
		var bean = dojo.fromJson(beanJson);
		var giacenza = getLastGiacenza(selectedBean.idProdotto);
		if (giacenza <= 0) {
			PlaySound('beep3');
			jConfirm(msgProdottoSenzaGiacenza.replace('{0}', selectedBean.titolo), attenzioneMsg, function(r) {
			    if (r) { 
			    	bean.giacenzaIniziale = giacenza;
			    	addSearchRowToTable(bean);
			    	mapGiacenze['prod_' + selectedBean.idProdotto] = giacenza - 1;
			    } else {
			    	$('#close').trigger('click');
			    	return false;
			    }
			});
		} else {
			bean.giacenzaIniziale = giacenza;
			addSearchRowToTable(bean);
			mapGiacenze['prod_' + selectedBean.idProdotto] = giacenza - 1;
		}
	}
};

onSelectPubblicazioniBarcodeTable = function(event) {		
	var tab = dijit.byId('pubblicazioniTableBarcode');
	var selectedBean = tab.getItem(tab.focus.rowIndex);	
	jConfirm(msgConfirmAssociazioneBarcode.replace('{0}', $('#inputText').val()).replace('{1}', selectedBean.titolo).replace('{2}', selectedBean.numeroCopertina).replace('{3}', selectedBean.dataUscitaFormat), attenzioneMsg, function(r) {
	    if (r) { 
	    	var deferred = service.associaBarcodePubblicazione(Number(selectedBean.idtn), Number(selectedBean.coddl), $('#inputText').val().toString());		
			deferred.addCallback(function(bean) {
				if (bean.type == 'EXCEPTION') {
					$.alerts.dialogClass = "style_1";
					jAlert(bean.result, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						$('#inputText').val("");
				    	$('#close').trigger('click');
				    	return false;
					});
				} else if (bean.type == 'CONFIRM_INVIA_MESSAGGIO_ASSOCIA_BARCODE') {
					doAssociaBarcode(bean);
				} else {
					jAlert(bean.result, informazioneMsg, function() {
						$("#aggiornaBarcode").attr("checked", false);
						doInputTextEnterKeyPress();
						$('#close').trigger('click');
				    	return false;
					});
				}
			});	
	    } else {
	    	$('#inputText').val("");
	    	$('#close').trigger('click');
	    	return false;
	    }
	});
};

onSelectPubblicazioniRichiediBarcodeTable = function(event) {		
	var tab = dijit.byId('pubblicazioniTableRichiediBarcode');
	var selectedBean = tab.getItem(tab.focus.rowIndex);	
	jConfirm(msgConfirmInviaRichiestaAssociazioneBarcode.replace('{0}', $('#inputText').val()).replace('{1}', selectedBean.titolo).replace('{2}', selectedBean.numeroCopertina).replace('{3}', selectedBean.dataUscitaFormat), attenzioneMsg, function(r) {
	    if (r) { 
	    	var deferred = service.sendRequestAggiornaBarcode(Number(selectedBean.idtn), Number(selectedBean.coddl), $('#inputText').val().toString());		
			deferred.addCallback(function(bean) {
				if (bean.type == 'EXCEPTION') {
					$.alerts.dialogClass = "style_1";
					jAlert(bean.result, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
					});
				} else {
					jAlert(bean.result, informazioneMsg, function() {
				    	return false;
					});
				}
				$('#inputText').val("");
				$('#close').trigger('click');
				return false;
			});	
	    } else {
	    	$('#inputText').val("");
	    	$('#close').trigger('click');
	    	return false;
	    }
	});
};

function addSearchRowToTable(bean) {
	addTableData(venditeTableObject, venditeTabIdField, bean);
	$("#qta").val(1);
	if ($('#close').length > 0) {
		$('#close').trigger('click');
	}
	$('#inputText').focus(); 
}

function onCloseLayer() {		
	$('#inputText').focus(); 
}

function deleteRow(pos) {
	var item = venditeTableObject.getItem(pos);
	var titolo = item.titolo;
	var idProd = item.idProdotto;
	var prodottoNonEditoriale = item.prodottoNonEditoriale;
	cancellaVenditaConfirmMsg = cancellaVenditaConfirmMsg.replace('{0}', '"' + titolo + '"');
	PlaySound('beep3');
	jConfirm(cancellaVenditaConfirmMsg, attenzioneMsg, function(r) {
		cancellaVenditaConfirmMsg = cancellaVenditaConfirmMsg.replace('"' + titolo + '"', '{0}');
	    if (r) { 
	    	if (prodottoNonEditoriale) {
	    		mapGiacenze['prod_' + idProd] = getLastGiacenza(idProd) + 1;
	    	}
	      	venditeTableObject.store.deleteItem(item);
	    } else {
	    	$("#inputText").focus();
	    }
	});
}

function addRightClickMenu(event) { 					
    $("#venditeDiv .dojoxGridScrollbox table tbody tr").find("td").contextMenu({ menu: 'myMenu', xLeft : 0}, 
        function(action, el, pos) { rightContextMenuWork(action, el, pos); 
    });
    
    $("#finalizza").contextMenu({ menu: 'barcodeMenu', xLeft : 0}, 
        function(action, el, pos) {  
    		window.open('/pdf/barcode_finalizza.pdf', '_blank'); 
    		return false;
    	}
    );
    $(".openmenu").contextMenu({ menu: 'myMenu', leftButton: true }, 
        function(action, el, pos) { rightContextMenuWork(action, el.parent("tr"), pos); 
    });	
    
 }
 
function rightContextMenuWork(action, el, pos) {
	var index = rowIndex;
    if (action == "delete") {        
    	deleteRow(index); 
    }          
 }

function setScrollBarToBottom() {
	$(".dojoxGridScrollbox").attr({ scrollTop: $(".dojoxGridScrollbox").attr("scrollHeight") });
}


/*
 * ABBONAMENTI
 */
var rechargesCallback = function(bean) {	
	showDialog('dialogContentRicariche');
	populateTableRicariche(bean.ricariche);
	unBlock();
};	

function populateTableRicariche(bean) {		
	var tab = dijit.byId('ricaricheTable');
	addTableData(tab, ricaricheTabIdField, bean);
	$("#ricaricheTable tr").each(function() {
		$(this).mouseover(function() {
			$(this).addClass("highlightVendite");
		});  
		$(this).mouseout(function() {
			$(this).removeClass("highlightVendite");
		});  					
	});				    	    
}

function showDialogProdotti(message) {	
	jPrompt(message, null, insertBarcodeMsg, function(r) {
	    if (r) {
	    	refreshRitiriProdotto(r);
	    } else {
	    	$("#inputText").val('');
	    }
	});
}	

refreshRicariche = function(idEditore, idProdotto) {						
	var params = {inputText:document.getElementById('inputText').value, idEditore:idEditore, idProdotto:idProdotto, operation:'TIPOLOGIE_MINICARD'};
	var deferred = service.getRows(params);		
	deferred.addCallback(rechargesCallback);		
};

refreshRitiriProdotto = function(bcode) {						
	var params = {inputText:document.getElementById('inputText').value, barcode:bcode, operation:modalita, quantita:Number($("#qta").val().trim())};
	var deferred = service.getRows(params);		
	deferred.addCallback(rowsCallback);
};

function showDialogRicariche(message, idEditore, idProdotto) {	
	refreshRicariche(idEditore, idProdotto);
}

function showConfirmEseguiRicarica(bean) {	
	if (bean.exceptionMessage != null && bean.exceptionMessage.length > 0) {
		PlaySound('beep3');
		jConfirm(bean.exceptionMessage, cardRechargeMsg, function(r) {
		    if (r) { 
		    	selectedBarcode = bean.barcode;
		    	showDialogRicariche(availableRechargesMsg, bean.idEditore, bean.idProdotto);
		    } else {
		    	$("#inputText").val('');
				setModalita('CONSEGNA');
		    }
		});
	} else {
		selectedBarcode = bean.barcode;
    	showDialogRicariche(availableRechargesMsg, bean.idEditore, bean.idProdotto);
	}
}

function showDialogGestioneArretrati(message) {	
	PlaySound('beep3');
	jConfirm(message, manageBackMsg, function(r) {
	    if (r) { 
	    	showDialogProdotti(insertBackMsg);
	    }
	});
}

onSelectRicaricheTable = function(event) {	
	var tab = dijit.byId('ricaricheTable');
	var selectedBean = tab.getItem(tab.focus.rowIndex);	
	var codTipologiaMinicard = selectedBean.codTipologiaMinicard;
	var descrizioneTipologia = selectedBean.descrizioneTipologia;
	var importoRicaricaFormat = selectedBean.importoRicaricaFormat.replace(/\&#8364;/g, '\u20AC');
	PlaySound('beep3');
	setTimeout(function() {
		jConfirm($.format(confermaRicaricaMsg, [descrizioneTipologia, importoRicaricaFormat]), attenzioneMsg, function(r) {
		    if (r) {
		    	block();
		    	var params = {inputText:document.getElementById('inputText').value, codTipologiaMinicard:codTipologiaMinicard, barcode:selectedBarcode, operation:'RICARICA'};
				var deferred = service.getRows(params);		
				deferred.addCallback(rowsCallback);	
		    } else {
		    	$('#close').trigger('click');
				setModalita('CONSEGNA');
		    }
		});
	}, 200);
};

function setModalita(mod) {
	switch(mod) {
		case 'MODALITA_RICARICA':
			$("#content1").css({'background-color':'#ff6666'});
			$("#venditeDiv").css({'background-color':'#ff6666'});
			break;
		case 'LETTURA_STATO':
			$("#content1").css({'background-color':'#ffcc66'});
			$("#venditeDiv").css({'background-color':'#ffcc66'});
			break;
		case 'CONSEGNA':
			$("#content1").css({'background-color':'#cccccc'});
			$("#venditeDiv").css({'background-color':'#fff'});
			break;	
		default:
			$("#content1").css({'background-color':'#cccccc'});
			$("#venditeDiv").css({'background-color':'#fff'});
			break;
	}
	modalita = mod;
	$("#butRicarica").css({'background-color':'#ffcc99'});
	$("#butStato").css({'background-color':'#ffffcc'});
	$("#inputText").focus(); 
}

/*
 * Finestre laterali delle pubblicazioni e prodotti vari
 */
if ($("#separatorR").is(":visible")) {
	$("#separatorR").tooltip({
		delay: 0,  
	    showURL: false,
	    bodyHandler: function() { 
	    	return "<b>" + msgNascondiBarraProdottiVari + "</b>";
	    }
	});
} else {
	$("#separatorR").tooltip({
		delay: 0,  
	    showURL: false,
	    bodyHandler: function() { 
	    	return "<b>" + msgMostraBarraProdottiVari + "</b>";
	    }
	});
}

if ($("#separatorL").is(":visible")) {
	$("#separatorL").tooltip({
		delay: 0,  
	    showURL: false,
	    bodyHandler: function() { 
	    	return "<b>"+ msgNascondiBarraPubblicazioni + "</b>";
	    }
	});
} else {
	$("#separatorL").tooltip({
		delay: 0,  
	    showURL: false,
	    bodyHandler: function() { 
	    	return "<b>" + msgMostraBarraPubblicazioni + "</b>";
	    }
	});
}

function showSidebar(){
    objMain.addClass('use-sidebar');
    var width = '';
    $("#arrowUpDiv").show();
	if ($("#sidebarL").is(":visible")) {
		width = width1;
	} else {
		width = width2;
	}
    $("#content").css({"width":width});
    resizeGrid();
}

function hideSidebar(){
    objMain.removeClass('use-sidebar');
    var width = '';
    $("#arrowUpDiv").hide();
	if ($("#sidebarL").is(":visible")) {
		width = width2;
	} else {
		width = width3;
	}
    $("#content").css({"width":width});
    resizeGrid();
}

function showSidebarL(){
	objMain.addClass('use-sidebar-L');
	var width = '';
	if ($("#sidebar").is(":visible")) {
		width = width1;
	} else {
		width = width2;
	}
	$("#content").css({"width":width});
	resizeGrid();
}

function hideSidebarL(){
	objMain.removeClass('use-sidebar-L');
   	var width = '';
	if ($("#sidebar").is(":visible")) {
		width = width2;
	} else {
		width = width3;
	}
   	$("#content").css({"width":width});
   	resizeGrid();
}

function resizeGrid() {
	venditeTableObject.resize();
	venditeTableObject.update();
}

function showCats() {
	var divs = '';
	$.each(arrCat, function(key, value) { 
		if (arrCat.length == 1) {
			return showSubCat(key);
		}
		var fileName = value.substring(0, value.indexOf('|'));
		divs += '<img id="' + key + '" style="display: inline-block;" class="pneImg" title="' + value.substring(value.indexOf('|') + 1) + '" src="/immagini_miniature_edicola_prodotti_vari/' + fileName + '" border="1" width="' + prodIconWidth + 'px" height="' + prodIconHeight + 'px" onclick="javascript: showSubCat(' + key + ')"/>'; 
	});
	$("#pneDiv").html(divs);
	$("#arrowUpDiv").hide();
	$(".pneImg").tooltip({
		delay: 0,  
	    showURL: false
	});
}

function showSubCat(cat) {
	var divs = '';
	var countScat = 0; 
	var currKey = '';
	$.each(arrScat, function(key, value) {
		if (key.indexOf(cat + "_") != -1) {
			currKey = key;
			var fileName = value.substring(0, value.indexOf('|'));
			divs += '<img id="' + key + '" style="display: inline-block;" class="pneImg" title="' + value.substring(value.indexOf('|') + 1) + '" src="/immagini_miniature_edicola_prodotti_vari/' + fileName + '" border="1" width="' + prodIconWidth + 'px" height="' + prodIconHeight + 'px" onclick="javascript: showProd(\'' + key + '\',false)"/>';
			countScat++;
		}
	});
	if (countScat == 1) {
		return showProd(currKey, true);
	}
	attachArrowsCat();
	$("#pneDiv").html(divs); 
	$("#pneDiv img").tooltip({
		delay: 0,  
	    showURL: false
	});
}

function showProd(prodKey, emptySubcats) {
	var divs = '';
	$.each(arrProd, function(key, value) { 
		if (key.startsWith(prodKey)) {
			value = value.replace(/([\'])/g,'\\\$1');
			var keySplit = value.split("|");
			var prezzo = (typeof(keySplit[3]) === 'undefined' || keySplit[3] == '') ? displayNum(Number('0').toFixed(2)) : displayNum(Number(keySplit[3]).toFixed(2));
			var titleProd = keySplit[1] + '&nbsp;(&euro;&nbsp;' + prezzo + ')';
			var fileName = value.substring(0, value.indexOf('|'));
			divs += '<img id="' + key + '" style="display: inline-block;" class="pneImg" title="' + titleProd + '" src="/immagini_miniature_edicola_prodotti_vari/' + fileName + '" border="1" width="' + prodIconWidth + 'px" height="' + prodIconHeight + 'px" onclick="javascript: sellProd(\'' + key + '\',\'' + value + '\')"/>';
		}
	});
	if (emptySubcats) {
		attachArrowsCat();
	} else {
		attachArrowsSCat(prodKey);
	}
	$("#pneDiv").html(divs);
	$("#pneDiv img").tooltip({
		delay: 0,  
	    showURL: false
	});
}

function attachArrowsCat() {
	$("#arrUp").attr("title", msgMostraCategorie);
	$("#arrUp").click(function() {showCats();});
	$("#arrUp").tooltip({
		delay: 0,  
	    showURL: false
	});
	$("#arrowUpDiv").show();
}

function attachArrowsSCat(prodKey) {
	$("#arrUp").attr("title", msgMostraSottocategorie);
	$("#arrUp").click(function() {showSubCat(prodKey.substring(0, prodKey.indexOf("_")));});
	$("#arrUp").tooltip({
		delay: 0,  
	    showURL: false
	});
	$("#arrowUpDiv").show();
}

function sellProd(key, val) {
	var quantita = $("#qta").val();
	if (isNaN(quantita)) {
		jAlert(moltiplicatoreInvalidoMsg, attenzioneMsg.toUpperCase(), function() {
			$.alerts.dialogClass = null;
			$("#qta").focus();
		});
		return false;
	}
	var idProd = key.substring(key.lastIndexOf("_") + 1);
	var keySplit = val.split("|");
	var titleProd = keySplit[1].replace(/\&#8364;/g, '\u20AC');
	var barcode = keySplit[2];
	var prezzo = keySplit[3];
	var titleProd2 = keySplit[4].replace(/\&#8364;/g, '\u20AC');
	var aliq = keySplit[5];
	var giacenza = getLastGiacenza(idProd);
	if (Number(quantita) > maxValueMultiplier) {
		PlaySound('beep3');
		jConfirm(moltiplicatoreMoltoGrandeMsg.replace('{0}',quantita), attenzioneMsg, function(r) {
		    if (r) {
		    	if (giacenza <= 0) {
					jConfirm(msgProdottoSenzaGiacenza.replace('{0}', titleProd), attenzioneMsg, function(r) {
					    if (r) { 
					    	sellProduct(prezzo, quantita, idProd, titleProd, barcode, titleProd2, aliq);
					    } else {
					    	return false;
					    }
					});
				} else {
					sellProduct(prezzo, quantita, idProd, titleProd, barcode, titleProd2, aliq);
				}
		    } else {
		    	$("#qta").val('1');
		    	$("#inputText").focus();
		    }
		});
	} else {
		if (giacenza <= 0) {
			jConfirm(msgProdottoSenzaGiacenza.replace('{0}', titleProd), attenzioneMsg, function(r) {
			    if (r) { 
			    	sellProduct(prezzo, quantita, idProd, titleProd, barcode, titleProd2, aliq);
			    } else {
			    	return false;
			    }
			});
		} else {
			sellProduct(prezzo, quantita, idProd, titleProd, barcode, titleProd2, aliq);
		}
	}
}

function sellProduct(prezzo, quantita, idProd, titleProd, barcode, titleProd2, aliq) {
	if (prezzo == '') {
		jPrompt(msgInserisciPrezzo, null, msgVenditaProdottiVariReparto, function(r) {
			if (r != null && r.length > 0) {
				var pre1 = parseLocalNum(r);
				if (!isNaN(pre1)) {
					addToTableVendite(quantita, idProd, titleProd, pre1, '', titleProd2, aliq);
				} 
			}
		});
		mapGiacenze['prod_' + idProd] = getLastGiacenza(idProd) - quantita;
		$("#qta").val(1);
		setTimeout(function() {
			addRightClickMenu();
		}, 100);
		return false;
	}
	mapGiacenze['prod_' + idProd] = getLastGiacenza(idProd) - quantita;
	addToTableVendite(quantita, idProd, titleProd, prezzo, barcode, titleProd2, aliq);
	setTimeout(function() {
		addRightClickMenu();
	}, 100);
	$("#qta").val(1);
}

function getLastGiacenza(idProd) {
	return isNaN(mapGiacenze['prod_' + idProd]) ? 0 : mapGiacenze['prod_' + idProd];
}

function addToTableVendite(quantita, idProd, titleProd, prezzo, barcode, titleProd2, aliq) {
	var prezzoFormat = displayNum(Number(prezzo).toFixed(2));
	var importoFormat = displayNum((Number(prezzo) * Number(quantita)).toFixed(2));
	var progressivo = getProgressivo(venditeTableObject);
	var prod = {"prodottoNonEditoriale":"true","progressivo":progressivo,"coddl":0,"idtn":0,"numeroCopertina":"","quantita":quantita,"idProdotto":idProd,"titolo":titleProd,"sottoTitolo":titleProd2,"prezzoCopertina":prezzo,"prezzoCopertinaFormat":prezzoFormat,"barcode":barcode,"importoFormat":importoFormat,"giacenzaIniziale":mapGiacenze['prod_' + idProd],"aliquota":aliq};
	addTableData(venditeTableObject, venditeTabIdField, prod);
}

