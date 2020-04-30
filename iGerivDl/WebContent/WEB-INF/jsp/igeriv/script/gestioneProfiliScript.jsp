<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>		 		
$(document).ready(function() {
	
	$("#arrowLeft, #arrowRight").tooltip({ 
		delay: 0,  
	    showURL: false
	});
	
	$('#arrowLeft').click(function() {
		$('#moduliSelected option:selected').each(function() {						             
	        var userClone = $(this).clone(true);
	        $('#moduli').append(userClone);
			$(this).remove();
			$("#moduli").sort_select_box();
		});			
	});
	
	$('#arrowRight').click(function() {
		$('#moduli option:selected').each(function() {						             
	        var userClone = $(this).clone(true);
	        $('#moduliSelected').append(userClone);
			$(this).remove();
			$("#moduliSelected").sort_select_box();
		});	
	});
	
	$('#memorizza').click(function() {
		$("#moduliSelected option").attr("selected","selected");   
		doSubmit();
	});
	
	$("#idProfilo").change(function() {
		populateSelects(val);
	});
	
	$("#idProfilo").contextMenu({menu : 'myMenu', yTop : 290, xLeft : 50}, function(action, el, pos) {
		rightContextMenuWork(action, el, pos);
	});
	
	function rightContextMenuWork(action, el, pos) {
		var id = $(el).val();
		if (id == null && (action == "delete" || action == "edit")) {
			jAlert('<s:text name="gp.selezionare.profilo"/>', attenzioneMsg);
			return false;
		}
		if (id != null && action == "delete") {
			PlaySound('beep3');
			jConfirm('<s:text name="gp.cancellare.profilo"/>', attenzioneMsg, function(r) {
				if (r) {
					ray.ajax();
					deleteProfilo(id);
				}
			}, true, true);
		} else if (id != null && action == "edit") {
			editProfilo(id);
		} else {
			newProfilo();
		}
	}
	
	function editProfilo(id) {
		var url = "gestioneProfili_editProfilo.action?idProfilo=" + id;
		openDiv("popup_name", 550, 300, url);
	}
	
	function newProfilo() {
		var url = "gestioneProfili_newProfilo.action";
		openDiv("popup_name", 550, 300, url);
	}
	
	function deleteProfilo(id) {
		dojo.xhrGet({
			url: "gestioneProfiliJson_deleteProfilo.action?idProfilo=" + id,	
			preventCache: true,
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=uft-8"}, 					
			handle: function(data,args) {
				unBlockUI();
				var hasError = (typeof(data.error) !== 'undefined' && data.error.length > 0);
				if (args.xhr.status != 200 || hasError) {
					$.alerts.dialogClass = "style_1";
					var msg = hasError ? data.error : '<s:text name="gp.errore.imprevisto"/>';
					jAlert(msg, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
					});
				} else {
					jAlert('<s:text name="gp.dati.memorizzati.delete"/>', informazioneMsg, function() {
						reloadProfili();
						$('#moduli').empty();
						$('#moduliSelected').empty();
					});
				}
			}
		});		
	}
	
	doSubmit = function() {
		var idProfilo = $("#idProfilo").val();
		if (idProfilo == null || idProfilo.length == 0) {
			jAlert('<s:text name="gp.selezionare.profilo"/>', attenzioneMsg);
			return false;
		}
		$("#idProfiloH").val(idProfilo);
		dojo.xhrPost({
			form: "EditProfiloForm",			
			handleAs: "text",		
			handle: function(data,args) {
				unBlockUI();
				if (args.xhr.status == 200) {
					jAlert('<s:text name="gp.dati.memorizzati"/>', informazioneMsg, function() {
						reloadProfili();
					});
				} else {
					jAlert('<s:text name="gp.errore.imprevisto"/>', attenzioneMsg, function() {});
				}
			}		
	    });
	};
	
	populateSelects = function(val) {
		var idProfilo = $("#idProfilo").val();
		if (idProfilo == null || idProfilo.length == 0) {
			jAlert('<s:text name="gp.selezionare.profilo"/>', attenzioneMsg, function() {
				unBlockUI();
			});
			return false;
		}
		dojo.xhrGet({
			url: "gestioneProfiliJson_getModuliProfilo.action?idProfilo=" + val,	
			preventCache: true,
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=uft-8"}, 				
			handle: function(data,args) {
				unBlockUI();
				if (args.xhr.status == 200) {
					$('#moduli').empty();
					$.each(data[0], function(key, value) { 
						value = $("<div/>").html(value).text();
						$('#moduli').append($('<option>', { value : key }).text(value));
					});
					$("#moduli").sort_select_box();
					$('#moduliSelected').empty();
					if (data[1]) {
						$.each(data[1], function(key, value) { 
							value = $("<div/>").html(value).text();
							$('#moduliSelected').append($('<option>', { value : key }).text(value));
						});
						$("#moduliSelected").sort_select_box();
					}
				} else {
					jAlert('<s:text name="gp.errore.imprevisto"/>', attenzioneMsg, function() {});
				}
			}
		});	
	};
});

function reloadProfili() {
	dojo.xhrGet({
		url: 'pubblicazioniRpc_getProfiliMenu.action',			
		handleAs: "json",				
		headers: { "Content-Type": "application/json; charset=uft-8"}, 	
		preventCache: true,
		handle: function(data,args) {	
			var list = '';		
            for (i = 0; i < data.length; i++) {
            	var strChecked = '';
            	if (data[i].id == $("#idProfilo").val()) {
            		strChecked = ' selected ';
            	}
            	list += '<option value="' + data[i].id + '" ' + strChecked + '>' +  data[i].label + '</option>';
            }
            $("#idProfilo").empty();
            $("#idProfilo").html(list);
		}
    });
	$("#close").trigger("click");
}
</script>