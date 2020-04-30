<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	var escludiImgMsg = '<s:text name="igeriv.escludi.immagine"/>';
	var spostaImgMsg = '<s:text name="igeriv.sposta.immagine.barra.scelta.rapida"/>';
	var liRightClickedId;
	
	$(document).ready(docReady);

	function docReady() {
		$gallery = $( "#gallery" ),
		$trash = $("#mostSoldBar");
		$gallerySort = $("#iternalUl li");
		
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
				if (curEl.attr("tagName") == "LI" && curEl.parent("ul").attr("id") == "gallery" ) {
					deleteImage( ui.draggable, $(this) );
					$(ui.draggable).contextMenu({menu : 'updateImgEdicola', yTop : 320, xLeft : 30, highlight : true}, function(action, el, pos) {
						rightContextMenuWorkProduct(action, el, pos);
					});
				} else if (curEl.attr("tagName") == "LI") {
					var curElId = curEl.attr("id");
					var currentPosition = curEl.position();
					var curDroppedOnEl = findCurrDroppedOnElement(currentPosition, curElId);
					if (curDroppedOnEl != null) {
						$("#" + curElId).insertAfter(curDroppedOnEl);
					}
				}
			}
		});
		
		$gallery.droppable({
			accept: "#mostSoldBar li",
			activeClass: "custom-state-active",
			drop: function( event, ui ) {
				recycleImage( ui.draggable );
			}
		});
		
		$( "ul.gallery > li" ).click(function( event ) {
			var $item = $( this ),
				$target = $( event.target );

			if ( $target.is( "a.ui-icon-trash" ) ) {
				deleteImage( $item );
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
	
	function findCurrDroppedOnElement(pos, excludeId) {
		var $el = null;
		$("#mostSoldBar").find("li").each(function() {
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
		liRightClickedId = el.attr("id");
		var $img = $(el).find("img").first();
		var src = $img.attr("src");
		var type = $img.attr("type");
		var nomeImmagine = src.substring(src.lastIndexOf("/") + 1);
		var tipoImmagine = Number(type);
		if (action == "edit") {
			var url = appContext + "/configurazioni_editImgMiniaturaEdicola.action?cpu=" + liRightClickedId + "&nomeImmagine=" + escape(nomeImmagine) + "&tipoImmagine=" + tipoImmagine;
			openDiv("popup_name", 520, 200, url);
		}
	}
	
	function editImmagineSuccessCallback(pref) {
		var separator = $("#attachment1").val().indexOf("/") != -1 ? "/" : "\\";
		var imgName = $("#attachment1").val().substring( $("#attachment1").val().lastIndexOf(separator) + 1).trim();
		var $img = $("#" + liRightClickedId).find("img").first();
		$img.attr('src', '/immagini_miniature_edicola/' + pref + imgName);
		$img.attr('type', '<s:text name="constants.COD_TIPO_IMMAGINE_MINIATURA_EDICOLA"/>');
		unBlockUI();
		$("#close").trigger('click');
	}

	function deleteImage( $item, droppable ) { 
		var recycle_icon = "<a href='link/to/recycle/script/when/we/have/js/off' title='" + escludiImgMsg + "' class='ui-icon ui-icon-refresh'>" + escludiImgMsg + "</a>";
		$item.fadeOut(function() {
			var $list = $( "ul", $( "#mostSoldBar" ) ).length ?
				$( "ul", $( "#mostSoldBar" ) ) :
				$( "<ul class='gallery ui-helper-reset'/>" ).appendTo( $( "#mostSoldBar" ) );
			
			$item.find( "a.ui-icon-trash" ).remove();
			$item.append( recycle_icon ).appendTo( $list ).fadeIn(function() {
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
		var trash_icon = "<a href='link/to/trash/script/when/we/have/js/off' title='' class='ui-icon ui-icon-trash'>" + spostaImgMsg + "</a>";
		$item.fadeOut(function() {
			$item
				.find( "a.ui-icon-refresh" )
					.remove()
				.end()
				.css( "width", "96px")
				.append( trash_icon )
				.find( "img" )
					.css( "height", "<s:text name="venditeIconHeight"/>px" )
				.end()
				.appendTo( $( "#gallery" ) )
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

	function setFieldsToSave() {
		$("#codiciPubblicazioni").val('');
		$("#coddl").val('');
		$("#nomeImmagini").val('');
		$("#tipoImmagini").val('');
		$("#mostSoldBar").find("li").each(function() {
			var $li = $(this);
			var $img = $li.find("img").first();
			var $src = $img.attr("src");
			var $fileType = $img.attr("type");
			var $fileName = $src.substring($src.lastIndexOf("/") + 1);
			if ($("#codiciPubblicazioni").val().length > 0) {
				$("#codiciPubblicazioni").val($("#codiciPubblicazioni").val() + ",");
				$("#coddl").val($("#coddl").val() + ",");
				$("#nomeImmagini").val($("#nomeImmagini").val() + ",");
				$("#tipoImmagini").val($("#tipoImmagini").val() + ",");
			}
			$("#codiciPubblicazioni").val($("#codiciPubblicazioni").val() + $li.attr("id").trim());
			$("#coddl").val($("#coddl").val() + $li.attr("coddl").trim());
			$("#nomeImmagini").val($("#nomeImmagini").val() + $fileName.trim());
			$("#tipoImmagini").val($("#tipoImmagini").val() + $fileType.trim());
		});
		return true;
	}
</script>