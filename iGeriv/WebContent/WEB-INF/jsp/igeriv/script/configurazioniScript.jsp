<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	var escludiImgMsg = '<s:text name="igeriv.escludi.immagine"/>';
	var spostaImgMsg = '<s:text name="igeriv.sposta.immagine.barra.scelta.rapida"/>';
	var liRightClickedId;
	
	$(document).ready(docReady);
	
	function docReady() {
		$(document)[0].oncontextmenu = function() {return false;}; 
		$gallery = $( "#gallery" ),
		$trash = $("#mostSoldBarL, #mostSoldBarR");
		$gallerySort = $("#iternalUlL li, #iternalUlR li");
		
		$("li", $gallery ).draggable({
			cancel: "a.ui-icon",
			revert: "invalid",
			containment: $( "#demo-frame" ).length ? "#demo-frame" : "document",
			helper: "clone",
			cursor: "move"
		});
		
		$("li", $trash ).draggable({
			cancel: "a.ui-icon",
			revert: "invalid",
			containment: $( "#demo-frame" ).length ? "#demo-frame" : "document",
			helper: "clone",
			cursor: "move"
		});
		
		$trash.droppable({
			accept: function(d) {
		        if (d.attr("tagName") == "LI") { 
		            return true;
		        } else {
		        	return false;
		        }
		    },
			activeClass: "ui-state-highlight",
			drop: function( event, ui ) {
				var curEl = ui.helper;
				var ulId = $(this).attr("id");
				var curElUlId = curEl.parent("ul").attr("id");
				if (curEl.attr("tagName") == "LI") {
					if ((ulId == 'mostSoldBarR' && curElUlId == 'iternalUlR') || (ulId == 'mostSoldBarL' && curElUlId == 'iternalUlL')) {
						var curElId = curEl.attr("id");
						var currentPosition = curEl.position();
						var curDroppedOnEl = findCurrDroppedOnElement(currentPosition, curElId, ulId);
						if (curDroppedOnEl != null) {
							$("#" + curElId).insertAfter(curDroppedOnEl);
						}
					} else {
						deleteImage( ui.draggable, ulId );
						$(ui.draggable).contextMenu({menu : 'updateImgEdicola', yTop : 320, xLeft : 30, highlight : true}, function(action, el, pos) {
							rightContextMenuWorkProduct(action, el, pos);
						});
					}
				}
			}
		});
		
		$gallery.droppable({
			accept: "#mostSoldBarL li, #mostSoldBarR li",
			activeClass: "custom-state-active",
			drop: function( event, ui ) {
				recycleImage( ui.draggable );
			}
		});
		
		$( "ul.gallery > li" ).click(function( event ) {
			var $item = $( this ),
				$target = $( event.target );

			if ( $target.is( "a.ui-icon-trash" ) ) {
				deleteImage( $item, "gallery" );
				$item.contextMenu({menu : 'updateImgEdicola', yTop : 320, xLeft : 30, highlight : true}, function(action, el, pos) {
					rightContextMenuWorkProduct(action, el, pos);
				});
			} else if ( $target.is( "a.ui-icon-zoomin" ) ) {
				viewLargerImage( $target );
			} else if ( $target.is( "a.ui-icon-refresh" ) ) {
				recycleImage( $item );
			}
			return false;
		});
		
		$(".ui-icon-trash").css({"background": "url(/app_img/move_down.gif) center center repeat-x"});
		
		$(".ui-icon-refresh").css({"background": "url(/app_img/move_up.gif) center center repeat-x"});
		
		$(".imgMiniatureClass, .ui-icon, .ui-icon-trash, .ui-icon-refresh").tooltip({ 
			delay: 0,  
		    showURL: false
		});
		
		$("#butScegliAltraPubblicazione").click(function() {
			var popID = 'popup_name';   	     		    	  
		    var popWidth = 900;
		    var popHeight = 500;
		 	var url = "${pageContext.request.contextPath}/configPubblicazioni_showFilter.action";
			openDiv(popID, popWidth, popHeight, url);
		});
		
		$("li", $trash).contextMenu({menu : 'updateImgEdicola', yTop : 320, xLeft : 30, highlight : true}, function(action, el, pos) {
			rightContextMenuWorkProduct(action, el, pos);
		});
	}
	
	function findCurrDroppedOnElement(pos, excludeId, ulId) {
		var $el = null;
		$("#" + ulId).find("li").each(function() {
			var $li = $(this);
			var $img = $li.find("img").first();
			var liLeftPos = $li.position().left;
			var liTopPos = $li.position().top;
			var liRightPos = $li.position().left + $img.width();
			if ($li.attr("id") != excludeId 
					&& (Math.floor(liTopPos) >= Math.floor(pos.top - 10) && Math.floor(liTopPos) <= Math.floor(pos.top + 10)) 
					&& (liLeftPos <= (pos.left + 10) && liRightPos >= (pos.left - 10))) {
				$el = $li;
				return false;
			}
		});
		return $el;
	}
	
	function rightContextMenuWorkProduct(action, el, pos) {
		var ulId = el.closest("ul.configBarraVenditeScrollDiv").attr("id");
		var ac = 'configurazioni_editImgMiniaturaEdicolaSinistra.action';
		if (ulId == 'mostSoldBarR') {
			ac = 'configurazioni_editImgMiniaturaEdicolaDestra.action';
		}
		liRightClickedId = el.attr("id");
		var $img = $(el).find("img").first();
		var src = $img.attr("src");
		var type = $img.attr("type");
		var nomeImmagine = src.substring(src.lastIndexOf("/") + 1);
		var tipoImmagine = Number(type);
		if (action == "edit") {
			var url = appContext + "/" + ac + "?cpu=" + liRightClickedId + "&nomeImmagine=" + escape(nomeImmagine) + "&tipoImmagine=" + tipoImmagine;
			openDiv("popup_name", 520, 200, url);
		}
	}
	
	function editImmagineSuccessCallback(imgName) {
		var $img = $("#" + liRightClickedId).find("img").first();
		$img.attr('src', '/immagini_miniature_edicola/' + imgName);
		$img.attr('type', '<s:text name="constants.COD_TIPO_IMMAGINE_MINIATURA_EDICOLA"/>');
		unBlockUI();
		$("#close").trigger('click');
	}

	function deleteImage( $item, ulId ) { 
		$item.fadeOut(function() {
			var $list = $( "ul", $( "#" + ulId ) ).length ?
				$( "ul", $( "#" + ulId ) ) :
				$( "<ul class='gallery ui-helper-reset'/>" ).appendTo( $( "#" + ulId ) );
				
			$item.appendTo( $list ).fadeIn(function() {
				$item
					.animate({ width: "<s:text name="venditeIconWidth"/>px" })
					.find( "img" )
						.animate({ height: "60px" });
			});
			
			$(".ui-icon-refresh").css({"background": "url(/app_img/move_up.gif) center center repeat-x"});
			
			$(".imgMiniatureClass, .ui-icon, .ui-icon-trash, .ui-icon-refresh").tooltip({ 
				delay: 0,  
			    showURL: false
			});
		});
	}

	function recycleImage( $item ) { 
		$item.fadeOut(function() {
			$item
				.css( "width", "96px")
				.find( "img" ).css( "height", "<s:text name="venditeIconHeight"/>px" )
				.end()
				.appendTo( $("#gallery") )
				.fadeIn();
			
			$(".ui-icon-trash").css({"background": "url(/app_img/move_down.gif) center center repeat-x"});
			
			$(".imgMiniatureClass, .ui-icon, .ui-icon-trash, .ui-icon-refresh").tooltip({ 
				delay: 0,  
			    showURL: false
			});
		});
	}

	function viewLargerImage( $link ) {
		var src = $link.attr( "href" ),
			title = $link.siblings( "img" ).attr( "alt" ),
			$modal = $( "img[src$='" + src + "']" );
		if ( $modal.length ) {
			$modal.dialog( "open" );
		} else {
			var img = $( "<img alt='" + title + "' width='384' height='288' style='display: none; padding: 8px;' />" )
				.attr( "src", src ).appendTo( "body" );
			setTimeout(function() {
				img.dialog({
					title: title,
					width: 400,
					modal: true
				});
			}, 1 );
		}
	}

	function setLeftFields() {
		$("#codiciPubblicazioniL").val('');
		$("#coddlL").val('');
		$("#nomeImmaginiL").val('');
		$("#tipoImmaginiL").val('');
		$("#mostSoldBarL").find("li").each(function() {
			var $li = $(this);
			var $img = $li.find("img").first();
			var $src = $img.attr("src");
			var $fileType = $img.attr("type");
			var $fileName = $src.substring($src.lastIndexOf("/") + 1);
			if ($("#codiciPubblicazioniL").val().length > 0) {
				$("#codiciPubblicazioniL").val($("#codiciPubblicazioniL").val() + ",");
				$("#coddlL").val($("#coddlL").val() + ",");
				$("#nomeImmaginiL").val($("#nomeImmaginiL").val() + ",");
				$("#tipoImmaginiL").val($("#tipoImmaginiL").val() + ",");
			}
			$("#codiciPubblicazioniL").val($("#codiciPubblicazioniL").val() + $li.attr("id").trim());
			$("#coddlL").val($("#coddlL").val() + $li.attr("coddl").trim());
			$("#nomeImmaginiL").val($("#nomeImmaginiL").val() + $fileName.trim());
			$("#tipoImmaginiL").val($("#tipoImmaginiL").val() + $fileType.trim());
		});
	}
	
	function setRightFields() {
		$("#codiciPubblicazioniR").val('');
		$("#coddlR").val('');
		$("#nomeImmaginiR").val('');
		$("#tipoImmaginiR").val('');
		$("#mostSoldBarR").find("li").each(function() {
			var $li = $(this);
			var $img = $li.find("img").first();
			var $src = $img.attr("src");
			var $fileType = $img.attr("type");
			var $fileName = $src.substring($src.lastIndexOf("/") + 1);
			if ($("#codiciPubblicazioniR").val().length > 0) {
				$("#codiciPubblicazioniR").val($("#codiciPubblicazioniR").val() + ",");
				$("#coddlR").val($("#coddlR").val() + ",");
				$("#nomeImmaginiR").val($("#nomeImmaginiR").val() + ",");
				$("#tipoImmaginiR").val($("#tipoImmaginiR").val() + ",");
			}
			$("#codiciPubblicazioniR").val($("#codiciPubblicazioniR").val() + $li.attr("id").trim());
			$("#coddlR").val($("#coddlR").val() + $li.attr("coddl").trim());
			$("#nomeImmaginiR").val($("#nomeImmaginiR").val() + $fileName.trim());
			$("#tipoImmaginiR").val($("#tipoImmaginiR").val() + $fileType.trim());
		});
	}
	
	function setFieldsToSave() { 
		setLeftFields();
		setRightFields();
		return true;
	}
</script>