var context;
var categoryOrderChanged = false;
var subCategoryOrderChanged = false;
var productOrderChanged = false;

window.onbeforeunload = function() { 
	if (categoryOrderChanged) {
		var sortedCategoryIds = new Array();
		$("#mieiProdottiTree span.level1").each(function() {
			sortedCategoryIds.push($(this).attr("codcategoria"));
		});
		dojo.xhrGet({
			url: appContext + '/pne_updateCategoryPositions.action?sortedCategoryIds=' + sortedCategoryIds,			
			handleAs: "text",				
			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
			preventCache: true,
			sync: true
	    });
	}
	if (subCategoryOrderChanged) {
		var sortedSubcategoryIds = new Array();
		$("#mieiProdottiTree span.subFolder").each(function() {
			if ($(this).attr("changed") == 'true') {
				sortedSubcategoryIds.push($(this).attr("codcategoria") + "_" + $(this).attr("codsubcategoria"));
			}
		});
		dojo.xhrGet({
			url: appContext + '/pne_updateSubCategoryPositions.action?sortedSubcategoryIds=' + sortedSubcategoryIds,			
			handleAs: "text",				
			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
			preventCache: true,
			sync: true
	    });
	}
	if (productOrderChanged) {
		var sortedProdottiIds = new Array();
		$("#mieiProdottiTree span.fileEdicola").each(function() {
			if ($(this).attr("changed") == 'true') {
				sortedProdottiIds.push($(this).attr("id").replace('folder_2_',''));
			}
		});
		dojo.xhrGet({
			url: appContext + '/pne_updateProductPositions.action?sortedProdottiIds=' + sortedProdottiIds,			
			handleAs: "text",				
			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
			preventCache: true,
			sync: true
		});
	}
};

$(document).ready(function() {
	if (conditionHasActionMessages) {
		jConfirm(prodErrMsg, attenzioneMsg, function(r) {
		    if (r) { 
		    	var prodId = $("#codProdotto").val().trim();
		    	window.parent.document.getElementById('prodottiEdicolaFrame').contentDocument.location = appContext + '/pne_deleteProdottoEdicola.action?codProdotto=' + prodId + "&deleteDependenciesProdotto=true";
	    }
	});
}
if (conditionHasActionErrors) {
	jAlert(actionErrors, attenzioneMsg, function() {
		$.alerts.dialogClass = null;
	});
}
$("#prodottiTree").treeview({ speed: "slow", collapsed: true });
	docReady();
});			

function docReady() {
	prepareProdottiEdicola();
	prepareProdottiGenerici();
	$("#memorizza").click(function() {
	ray.ajax();
}); 

if (lastProdotto.trim() != '' && lastProdotto != null) {
	showProduct(lastProdotto);
	if (prevProdotto.trim() != '' && prevProdotto != null) {
		showProduct(prevProdotto);
	}
} else if (lastSubcategory.trim() != '' && lastSubcategory != null) {
	var $product = $("#" + lastSubcategory);
	var $prodParentLi = $product.parents('li.expandable');
	$prodParentLi.children("ul").css({'display':'block'});
	$prodParentLi.removeClass('expandable').removeClass('lastExpandable').addClass('collapsable').addClass('lastCollapsable');
	$prodParentLi.children("div").first().removeClass('expandable-hitarea').addClass('collapsable-hitarea').removeClass('lastExpandable-hitarea').addClass('lastCollapsable-hitarea');
	$prodParentLi.find("ul li.expandable").find("ul").css({'display':'none'});
	$("#mieiProdottiTree").removeAttr("style");
	}
}

function showProduct(lastProdotto) {
	var $product = $("#" + lastProdotto);
	var $prodParentLi = $product.parents('li.expandable');
	$prodParentLi.removeClass('expandable').removeClass('lastExpandable').addClass('collapsable').addClass('lastCollapsable');
	$prodParentLi.children("div").first().removeClass('expandable-hitarea').addClass('collapsable-hitarea').removeClass('lastExpandable-hitarea').addClass('lastCollapsable-hitarea');
	$product.closest("li.collapsable").find('ul').css({'display':'block'});
	$product.parents("ul").css({'display':'block'});
	$product.parents("li.collapsable").last().children('ul').css({'display':'block'});
	$("#mieiProdottiTree").removeAttr("style");
}

function prepareProdottiGenerici() {
	var $prodotti = $("#prodottiEdicola span[id*='folderTemplate_2_'][isprodottodl='false']" );
	var $mieiProdotti = $("#prodottiEdicola");
	var $mieiProdottiLi = $("#prodottiEdicola li:not(:has(span[isprodottodl=true]))");
	var $mieiProdottiItens = $("#prodottiEdicola :not(:has(span[isprodottodl=true])) span.subFolder" );
	
	$prodotti.draggable({
		cancel: "a.ui-icon",
		revert: "invalid",
		helper: "clone",
		cursor: "move",
		zIndex: '5000'
	});
	
	$mieiProdotti.droppable({
		accept: "span.file",
		activeClass: "ui-state-highlight",
		drop: function( event, ui ) {		
			var prodId = Number($(ui.draggable).attr("id").replace('folderTemplate_2_','').trim());
			window.parent.document.getElementById('prodottiEdicolaFrame').contentDocument.location.href = appContext + '/pne_showProdotti.action?idProdottiEdicola=' + prodId;
		}
	});
	
	$mieiProdottiItens.droppable({
		accept: function(d) {
			var isSpan = d.attr("tagName") == "LI"; 
			var hasClassEdicola = d.find("span:first").hasClass("fileEdicola");
			return (isSpan && hasClassEdicola) ? true : false;
	    },
		activeClass: "ui-state-highlight",
		hoverClass: 'ui-state-active',
		drop: function( event, ui ) {		
			categoryOrderChanged = true;
			var prodId = Number($(ui.draggable).find("span:first").attr("id").replace('folder_2_','').trim());
			var codCat = $(this).attr("codcategoria").trim();
			var codScat = $(this).attr("codsubcategoria").trim();
			window.parent.document.getElementById('prodottiEdicolaFrame').contentDocument.location.href = appContext + '/pne_updateProductCategory.action?codProdotto=' + prodId + '&codCategoria=' + codCat + '&codSottoCategoria=' + codScat;
		}
	});
	
	$("#gallery").droppable({
		accept: "span.fileEdicola",
		activeClass: "ui-state-highlight",
		drop: function( event, ui ) {
			$(ui.draggable).remove();
			var prodId = Number($(ui.draggable).attr("id").replace('folder_2_','').trim());
			window.parent.document.getElementById('prodottiEdicolaFrame').contentDocument.location.href = appContext + '/pne_deleteProdottoEdicola.action?codProdotto=' + prodId;
		}
	});
	
	$mieiProdotti.sortable({
		delay: 300,
		cursor: 'pointer',
		placeholder: 'ui-state-highlight',
		items:  '> ul > li',
		axis: 'y',
		revert: true,
		update: function (e, ui) {
			categoryOrderChanged = true;
		}
	});
	
	$mieiProdottiLi.sortable({
		delay: 300,
		cursor: 'pointer',
		placeholder: 'ui-state-highlight',
		items:  '> ul > li',
		axis: 'y',
		update: function (e, ui) { 
			var itemId = $(ui.item.context).find("span:first").attr("id"); 
			if (itemId.indexOf("sucategory") != -1) {
				subCategoryOrderChanged = true;
				$(this).closest("li").find("span").each(function() {
					$(this).attr("changed", true);
				});
			} else {
				productOrderChanged = true;
				$(this).closest("li").find("span").each(function() {
					$(this).attr("changed", true);
				});
			}
		}
	});
	
	$("#myProducts").tooltip({
		delay: 0,  
	    showURL: false,		
	    bodyHandler: function() {
	    	var str = '<b>' + msgMieiProdotti + '</b>';
	    	return str;
	    }
	});
	
	$(".linethrough").tooltip({
		delay: 0,  
	    showURL: false,
	    bodyHandler: function() { 
	    	return "<b>" + msgProdottoEsclusoVendite + "</b>";
	    }
	}); 
	
	$("#availableProducts, #selectedProdcuts").tooltip({
		delay: 0,  
	    showURL: false,		
	    bodyHandler: function() {
	    	var str = '<b>' + msgProdottiDisponibili + '</b>';
		    	return str;
		    }
		});
	}
	
	function prepareProdottiEdicola() {
		$("#mieiProdottiTree").treeview({ speed: "slow", collapsed: true });
		
		$("span[id*='folderTemplate_']").click(function() {
			$(this).effect("highlight", {}, 1000);
		});

		// prodotti non dl 
		$("#prodottiEdicola span.fileEdicola[isprodottodl=false]").contextMenu({menu : 'myMenu', yTop : -10, xLeft : 0, highlight : true}, function(action, el, pos) {
			rightContextMenuWorkProduct(action, el, pos);
		});
		
		// prodotti dl 
		$("#prodottiEdicola span.fileEdicola[isprodottodl=true]").contextMenu({menu : 'menuProdottiDl', yTop : -10, xLeft : 0, highlight : true}, function(action, el, pos) {
			rightContextMenuWorkProduct(action, el, pos);
		});
		
		// categorie senza prodotti dl
		var categorieSenzaProdottiDl = $("#prodottiEdicola li.level1:not(:has(span[isprodottodl=true])) span.level1");
		categorieSenzaProdottiDl.contextMenu({menu : 'categoryEditDelete', yTop : -10, xLeft : 0, highlight : true}, function(action, el, pos) {
			editCat(action, el, pos);
		});
		
		// sottocategorie senza prodotti dl
		$("#prodottiEdicola li.level2:not(:has(span[isprodottodl=true])) span.subFolder").filter(":not(:has(span[isprodottodl='true']))").contextMenu({menu : 'subcategoryEditDelete', yTop : -10, xLeft : 0, highlight : true}, function(action, el, pos) {
			if (action == "insertProd") {
				editProdotto(action, el, pos);
			} else {
				editScat(action, el, pos);
			} 
		});
		
		if (isProdottiEdicolaEmpty) {
			$("#prodottiEdicola").contextMenu({menu : 'categoryInsert', yTop : -10, xLeft : 0, highlight : true}, function(action, el, pos) {
				var url = appContext + "/pne_editCategoria.action";
				openDiv("popup_name", 600, 350, url, '', '', window.parent.document);
			});
		} else if (categorieSenzaProdottiDl.length == 0) {
			var categorieConProdottiDl = $("#prodottiEdicola li.level1:has(span[isprodottodl=true]) span.level1");
			categorieConProdottiDl.contextMenu({menu : 'categoryInsert', yTop : -10, xLeft : 0, highlight : true}, function(action, el, pos) {
				var url = appContext + "/pne_editCategoria.action";
				openDiv("popup_name", 600, 350, url, '', '', window.parent.document);
			});
		}
		
		function rightContextMenuWorkProduct(action, el, pos) {
			var id = el.attr("id").replace('folder_2_','');
			if (action == "delete") {
				jConfirm(msgCancellareProdotto, attenzioneMsg, function(r) {
				    if (r) { 
				    	ray.ajax();
						window.parent.document.getElementById('prodottiEdicolaFrame').contentDocument.location.href = appContext + '/pne_deleteProdottoEdicola.action?codProdotto=' + id;
						//document.getElementById('prodottiEdicolaFrame').contentDocument.location.href = appContext + '/pne_showProdotti.action';
				    }
				});
			} else if (action == "edit") {
				var url = appContext + "/pne_editProdottoEdicola.action?codProdotto=" + id;
				openDiv("popup_name", 800, 570, url, '', '', window.parent.document);
			} 
		}
		
		function editScat(action, el, pos) {
			var codCat = el.attr("codcategoria").trim();
			var codScat = el.attr("codsubcategoria").trim();
			var url = '';
			if (action == "insertScat") {
				url = appContext + "/pne_editSottoCategoria.action?codCategoria=" + codCat;
			} else if (action == "editScat") {
				url = appContext + "/pne_editSottoCategoria.action?codCategoria=" + codCat + "&codSottoCategoria=" + codScat;
			} else if (action == "deleteScat") {
				jConfirm(msgCancellareSottocategoria.replace("{0}", el.text()), attenzioneMsg, function(r) {
				    if (r) { 
				    	ray.ajax();
				    	window.parent.document.getElementById('prodottiEdicolaFrame').contentDocument.location.href = appContext + '/pne_deleteSottoCategoria.action?codCategoria=' + codCat + '&codSottoCategoria=' + codScat;
						//document.getElementById('prodottiEdicolaFrame').contentDocument.location.href = appContext + '/pne_showProdotti.action';
				    }
				});
			}
			if (url != '') {
				openDiv("popup_name", 600, 350, url, '', '', window.parent.document);
			}
		}
		
		function editCat(action, el, pos) {
			var codCat = el.attr("codcategoria").trim();
			var url = '';
			if (action == "insertCat") {
				url = appContext + "/pne_editCategoria.action";
			} else if (action == "insertScat") {
				url = appContext + "/pne_editSottoCategoria.action?codCategoria=" + codCat;
			} else if (action == "editCat") {
				url = appContext + "/pne_editCategoria.action?codCategoria=" + codCat;
			} else if (action == "deleteCat") {
				jConfirm(msgCancellareCategoria.replace("{0}", el.text()), attenzioneMsg, function(r) {
				    if (r) { 
						ray.ajax();
						window.parent.document.getElementById('prodottiEdicolaFrame').contentDocument.location.href = appContext + '/pne_deleteCategoria.action?codCategoria=' + codCat;
						//document.getElementById('prodottiEdicolaFrame').contentDocument.location.href = appContext + '/pne_showProdotti.action';
				    }
				});
			}
			if (url != '') {
				openDiv("popup_name", 600, 350, url, '', '', window.parent.document);
			}
		}
		
		function editProdotto(action, el, pos) {
			var catTitle = el.parent().parent().parent().find("span").first().text().trim();
			var subCatTitle = el.text().trim();
			var url = appContext + "/pne_editProdottoEdicola.action?catTitle=" + escape(catTitle) + "&subCatTitle=" + escape(subCatTitle);
			openDiv("popup_name", 800, 570, url, '', '', window.parent.document);
		}
	}