<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script type="text/javascript">		
	var ids;
	var parentIds;
	var depths;

	$(document).ready(function() {
		$(document)[0].oncontextmenu = function() {return false;}  
		setContentDivHeight(1500);
		$('ol.sortable').nestedSortable({
			disableNesting: 'no-nest',
			forcePlaceholderSize: false,
			handle: 'div',
			helper:	'clone',
			items: 'li',
			maxLevels: 3,
			opacity: .6,
			placeholder: 'placeholder',
			revert: 250,
			tabSize: 50,
			tolerance: 'pointer',
			toleranceElement: '> div'
		});
		
		$("#idProfilo").contextMenu({menu : 'myMenu', yTop : 290}, function(action, el, pos) {
			rightContextMenuWork(action, el, pos, 'Profilo');
		});
		
		$("div.marker").contextMenu({menu : 'myMenu', yTop : 290}, function(action, el, pos) {
			rightContextMenuWork(action, el, pos, 'Menu');
		});
		
		function rightContextMenuWork(action, el, pos, tipo) {
			var id = el.attr("id");
			var level = el.attr("livello");
			var idMenuPadre = el.attr("idMenuPadre");
			if (isNaN(id)) {
				id = el.val();
			}
			if (action == "delete") {
				PlaySound('beep3');
				if (confirm('<s:text name="gp.cancellare.menu"/>')) {
					deleteItem(id, tipo);
				}
			} else if (action == "edit") {
				editItem(id, tipo);
			} else if (action == "insert") {
				newItem(id, tipo, level, idMenuPadre);
			}
		}
		
	});
	
	function editModulo(id) {
	 	var url = "${pageContext.request.contextPath}${ap}/menu_editMenu.action?idMenu=" + id;
		openDiv("popup_name", 800, 500, url);
	}
	
	function deleteItem(id, tipo) {
		dojo.xhrGet({
			url: "${pageContext.request.contextPath}${ap}/menu_deleteMenu.action?idMenu=" + id,	
			preventCache: true,
			handleAs: "text",				
			handle: function(data,args) {
				if (args.xhr.status == 200) {
					jAlert('<s:text name="gp.dati.memorizzati.delete"/>');
					$("#list_" + id).remove();
				} else {
					$.alerts.dialogClass = "style_1";
					jAlert("<s:text name="msg.errore.invio.richiesta"/>", attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
					});
				}
			}
		});		
	}
	
	function editItem(id, tipo) {
	 	var url = "${pageContext.request.contextPath}${ap}/menu_edit" + tipo + ".action";
	 	if (tipo == 'Profilo') {
	 		url += "?id" + tipo + "=" + id;
	 	} else if (tipo == 'Menu') {
	 		url += "?id" + tipo + "=" + id + '&idProfilo=' + $("#idProfilo").val();
	 	}
		openDiv("popup_name", 800, 500, url);
	}
	
	function newItem(id, tipo, level, idMenuPadre) {
	 	var url = "${pageContext.request.contextPath}${ap}/menu_edit" + tipo + ".action";
	 	if (tipo == 'Menu') {
	 		url = "${pageContext.request.contextPath}${ap}/menu_insert" + tipo + ".action";
	 		url += "?idProfilo=" + $("#idProfilo").val() + "&livello=" + level;
	 		if (idMenuPadre.length > 0) {
	 			url += "&idMenuPadre=" + idMenuPadre;	
	 		}
	 	} 
		openDiv("popup_name", 800, 500, url);
	}
	
	function deleteProfilo(id) {
		dojo.xhrGet({
			url: "${pageContext.request.contextPath}${ap}/menu_deleteProfilo.action?idProfilo=" + id,	
			preventCache: true,
			handleAs: "text",				
			handle: function(data,args) {
				if (ioargs.xhr.status == 200) {
					//$("#list_" + id).remove();
					jAlert("Profilo Cancellato");
				} else {
					jAlert("Errore");
				}
			}
		});		
	}
	
	
	function prepareMenuResult() {
		ids = new Array();
		parentIds = new Array();
		depths = new Array();
		var arraied = $('ol.sortable').nestedSortable('toArray', {
			startDepthCount : 0
		});
		arraied = dump(arraied);
		$("#ids").val(ids);
		$("#parentIds").val(parentIds);
		$("#livello").val(depths);
		return true;
	}

	function dump(arr, level) {
		var dumped_text = "";
		if (!level)
			level = 0;

		var level_padding = "";
		for ( var j = 0; j < level + 1; j++)
			level_padding += "    ";

		if (typeof (arr) == 'object') { //Array/Hashes/Objects
			for ( var item in arr) {
				var value = arr[item];
				if (typeof (value) == 'object') { //If it is an array,
					dumped_text += level_padding + "'" + item + "' ...\n";
					dumped_text += dump(value, level + 1);
				} else if (item == 'item_id') {
					ids.push(value);
				} else if (item == 'parent_id') {
					parentIds.push(value);
				} else if (item == 'depth') {
					depths.push(value);
				}
			}
		} else { //Strings/Chars/Numbers etc.
			dumped_text = "===>" + arr + "<===(" + typeof (arr) + ")";
		}
		return dumped_text;
	}
</script>		