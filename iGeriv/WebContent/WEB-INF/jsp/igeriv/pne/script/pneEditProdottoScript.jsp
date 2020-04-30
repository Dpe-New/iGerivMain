<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	var campoObbligatorio = '<s:text name="error.campo.x.obbligatorio.2"/>';
	var attenzioneMsg = "<s:text name='msg.alert.attenzione'/>";
	var attenzioneMsg = "<s:text name='msg.alert.attenzione'/>";
	var contextProdotto = window.parent.document;
	$(document).ready(function() {
		if ($("#prodottoDl", contextProdotto).val() == 'true') {
			$("#prodottoEdicola\\.descrizioneProdottoA",  window.parent.document).attr("disabled", true);
			$("#prodottoEdicola\\.descrizioneProdottoB",  window.parent.document).attr("disabled", true);
			$("#codCategoria",  window.parent.document).attr("disabled", true);
			$("#sottoCategoria",  window.parent.document).attr("disabled", true);
			$("#aliquotaIva",  window.parent.document).attr("disabled", true);
			$("#causaleIva",  window.parent.document).attr("disabled", true);
			$("#attachment1",  window.parent.document).attr("disabled", true);
		}
		categoriaChange();
		aliquotaChange();
		$("#codCategoria", contextProdotto).change(categoriaChange);
		$("#sottoCategoria", contextProdotto).change(sottoCategoriaChange);
		$("#aliquotaIva", contextProdotto).change(aliquotaChange);
		addFadeLayerEvents(contextProdotto);	
		var $tableTr = $("#PrezziTab_table tbody tr", contextProdotto);
		var $tableThead = $("#PrezziTab_table tr.tableHeader", contextProdotto);
		if ($tableTr.length == 0) {
			addFirstRow();
			$tableTr.find("td:nth-child(3)").remove();
			$tableThead.find("td:nth-child(3)").remove();
		} else {
			$tableTr.find("td:nth-child(3)").html('<td id="eliminaDiv" class="extremeTableFields" style="text-align: center;" width="8%"><img name="elimina" style="cursor:pointer" src="/app_img/remove.jpg" alt="<s:text name="dpe.contact.form.elimina"/>" border="0" title="<s:text name="dpe.contact.form.elimina"/>"/></td>');
			$("#eliminaDiv", contextProdotto).append('&nbsp;&nbsp;<img id="aggiungi" name="aggiungi"  src="/app_img/insert.gif" alt="<s:text name="igeriv.aggiungi"/>" border="0" title="<s:text name="igeriv.aggiungi"/>"/>');
		}
		$("#aggiungi", contextProdotto).css({"cursor":"pointer"});
		$("#generateCode", contextProdotto).css({"cursor":"pointer"});
		$("#generateCode", contextProdotto).click(function() {
			if ($("#prodottoEdicola\\.descrizioneProdottoA", contextProdotto).val().trim() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}','<s:text name="igeriv.descrizione" />'), attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
				return false; 
			}
		});
		$("img[name='elimina']", contextProdotto).click(function() {
			$(this).closest("tr").remove();
			if ($tableTr.length == 1) {
				addFirstRow();
			}
		});
		$("#aggiungi", contextProdotto).click(function() {
			addFirstRow();
		});
		
		$("#imgProd", contextProdotto).load(function() {
			$(this).css({"border-style":"solid","border-width":"1px","border-color":"black"});
	        $(this).parent().after('<div style="float:left; margin-left:10px;  margin-top:20px;"><img id="eliminaImgProd" style="cursor:pointer" src="/app_img/remove.jpg" alt="<s:text name="dpe.contact.form.elimina"/>" border="0" title="<s:text name="dpe.contact.form.elimina"/>"/></div>');
	        $("#eliminaImgProd", contextProdotto).click(function() {
				$(this).remove();
				$("#imgProd", contextProdotto).remove();
				$("#nomeImmagine", contextProdotto).val('');
			});
	    });
		
		$("#prodottoEdicola\\.codProdottoEsterno, #prodottoEdicola\\.descrizioneProdottoA, #prodottoEdicola\\.descrizioneProdottoA, #prodottoEdicola\\.descrizioneProdottoB, #codCategoria, #sottoCategoria, #prodottoEdicola\\.barcode, #attachment1",  window.parent.document).keypress(function(event) {
			if ($(this).attr("id") == 'attachment1') {
				$("#memEditProdotto").focus();
			} else {
				focusNextFormField(event);
			}
		});
		
		$("#PrezziTab_table", window.parent.document).find("input:text", window.parent.document).keypress(function(event) {
			focusNextFormField(event);
		});
		
		setLastFocusedElement(window.parent.document);
		
		var desc = $("#prodottoEdicola\\.descrizioneProdottoA",  window.parent.document).val();
		var descB = $("#prodottoEdicola\\.descrizioneProdottoB",  window.parent.document).val();
		$("#prodottoEdicola\\.descrizioneProdottoA",  window.parent.document).val(desc.replace('&#8364;', '\u20AC'));
		$("#prodottoEdicola\\.descrizioneProdottoB",  window.parent.document).val(descB.replace('&#8364;', '\u20AC'));
		setFocusedFieldStyle(window.parent.document);
		$("#prodottoEdicola\\.descrizioneProdottoA",  window.parent.document).focus();
		
		//GIFT CARD EPIPOLI
		var isProDig = $("#prodottoDigitale",  window.parent.document).val();
		
		if (isProDig == "S") {
			$("img[name='elimina']",  	window.parent.document).remove();
			$("img[name='aggiungi']",  	window.parent.document).remove();
			$("#memEditProdotto",  		window.parent.document).remove();
		}
		
		
	});	
	
	function focusNextFormField(event) {
		var keycode = (event.keyCode ? event.keyCode : event.charCode);  
		if (keycode == 13) {
			event.preventDefault();
			var fieldId = lastFocusedFieldId.replace(/([.])/g,'\\\$1');
			var $lastField = $("#" + fieldId, window.parent.document);
			var $parentElement = $lastField.closest("div.rowDiv");
			if (fieldId.indexOf("prezzi") != -1) {
				$parentElement = $lastField.closest("td");
			}
			var $nextEl = $parentElement.next();
			var $nextValidElements = $nextEl.find("input:text:enabled, input:file:enabled, button:enabled, select:enabled", window.parent.document);
			if ($nextValidElements.length == 0) {
				while (true) {
					$nextEl = $nextEl.next();
					$nextValidElements = $nextEl.find("input:text:enabled, input:file:enabled, button:enabled, select:enabled", window.parent.document);
					if ($nextValidElements.length > 0) {
						break;
					}
				}
			} 
			$nextValidElements.first().focus().select();
			$lastField.css('background','#fff');
			/*if (fieldId == 'prodottoEdicola\\.descrizioneProdottoA' && $lastField.val().trim().length > 0 && $("#prodottoEdicola\\.codProdottoEsterno", window.parent.document).val().length == 0) {
				generateProductCode();
			}*/
		}  
	}
	
	function addFirstRow() {
		if ($("#PrezziTab_table tbody tr:first", contextProdotto).find("input:text").length == 0) {
			var trStr = '<tr class="odd" style="height: 20px;" divparam="" iv="" pv="" an="" an1="" an2="">';
			trStr += '<td class="extremeTableFields" style="text-align: right;" width="8%"><input id="prezzi" value="" class="extremeTableFields" style="text-align: right;" size="10" maxlength="10" validateisnumeric="false" type="text"></td>';
			trStr += '<td class="extremeTableFields" style="text-align: center;" width="8%"><input id="validita" value="" class="extremeTableFields" style="text-align: center;" size="10" maxlength="10" validateisnumeric="false" type="text"></td>';
			trStr += '</tr>';
			$("#PrezziTab_table tbody", contextProdotto).prepend(trStr);
			$("#validita", contextProdotto).mask("99/99/9999");
		}
	}
	
	function categoriaChange() {
		dojo.xhrGet({
			url: '${pageContext.request.contextPath}/pubblicazioniRpc_getSottocategoriePne.action?categoria=' + $("#codCategoria", contextProdotto).val(),			
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=utf-8"}, 	
			preventCache: true,
			handle: function(data,args) {	
				var list = '';		
	            for (i = 0; i < data.length; i++) {
	            	var strChecked = '';
	            	if (data[i].codSottoCategoria == $("#codSottoCategoria", contextProdotto).val()) {
	            		strChecked = ' selected ';
	            	}
	            	list += '<option value="' + data[i].codSottoCategoria + '" ' + strChecked + '>' +  data[i].descrizioneNoHtml + '</option>';
	            }
	            $("#sottoCategoria", contextProdotto).empty();
	            $("#sottoCategoria", contextProdotto).html(list);
	            $("#codSottoCategoria", contextProdotto).val($("#sottoCategoria", contextProdotto).val());
			}
	    });
	}
	
	function aliquotaChange() {
		dojo.xhrGet({
			url: '${pageContext.request.contextPath}/pubblicazioniRpc_getCausaliIva.action?aliquota=' + $("#aliquotaIva", contextProdotto).val(),			
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=utf-8"}, 	
			preventCache: true,
			handle: function(data,args) {	
				var list = '';		
	            for (i = 0; i < data.length; i++) {
	            	var strChecked = '';
	            	if (data[i].pk == $("#causaleIvaHidden", contextProdotto).val()) {
	            		strChecked = ' selected ';
	            	}
	            	list += '<option value="' + data[i].pk + '" ' + strChecked + '>' +  data[i].descrizione + '</option>';
	            }
	            $("#causaleIva", contextProdotto).empty();
	            $("#causaleIva", contextProdotto).html(list);
			}
	    });
	}
	
	function sottoCategoriaChange() {
		$("#codSottoCategoria", contextProdotto).val($("#sottoCategoria", contextProdotto).val());
	} 
	
	function sendData() {
		dojo.io.iframe.send({
			form : "PNEEditProdottoForm",
			handleAs : "text",
			load : function(response, ioArgs) {
				return response;
			}
		});
	}
	
</script>
