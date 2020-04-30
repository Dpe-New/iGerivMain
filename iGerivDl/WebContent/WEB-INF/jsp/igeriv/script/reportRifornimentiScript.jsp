<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<script> 
	function onLoadFunction() {
		document.getElementById('strDataDa').focus();
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#strDataDa').datepicker();
		$('#strDataA').datepicker();
		
		
// 		$('#strDataDa').click(function() {
// 	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});
// 		$('#strDataA').click(function() {
// 	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});

		if ('<s:property value="codPubblicazione"/>' != '') {
			findNumeriPubblicazione('<s:property value="codPubblicazione"/>');
		}
	}
	
	$("input#autocompleteRiv").keydown(setCodEdicola);
	$("input#autocompletePub").keydown(setCodPubblicazione);
	$("input#autocompletePub").blur(function() {
		if ($("input#autocompletePub").val() == '') {
			$('#numCopertina').empty();
		}
	});
	
	function setCodEdicola(event) { 
		var keycode = (event.keyCode ? event.keyCode : event.charCode);
		if (keycode == '13' && $("input#autocompleteRiv").val().length > 0 && !isNaN($("#autocompleteRiv").val())) {
			$("#codEdicola").val($("input#autocompleteRiv").val());
			return true;
		} 
	}
	
	function setCodPubblicazione(event) { 
		var keycode = (event.keyCode ? event.keyCode : event.charCode);
		if (keycode == '13' && $("input#autocompletePub").val().length > 0 && !isNaN($("#autocompletePub").val())) {
			$("#codPubblicazione").val($("input#autocompletePub").val());
			return true;
		} 
	}
	
	function doSubmit() { 
		if ($("#autocompleteRiv").val() == '') {
			document.getElementById('codEdicola').value = '';
		} 
		else if (!isNaN($("#autocompleteRiv").val())) {
			document.getElementById('codEdicola').value = $("#autocompleteRiv").val();
		}
		if ($("#autocompletePub").val() == '') {
			document.getElementById('codPubblicazione').value = '';
		} 
		else if (!isNaN($("#autocompletePub").val())) {
			document.getElementById('codPubblicazione').value = $("#autocompletePub").val();
		}
		return true;
	};
	
	$("input#autocompleteRiv").autocomplete({	
		minLength: 1,			
		source: function(request, response) {		
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}${ap}/automcomplete_edicoleByCrivDl.action",	
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
				content: { 
			    	term: request.term
			    }, 					
				handle: function(data,args) {
					response($.map(data, function(item) {											
		                                return {
		                                    id: item.id,	
		                                    label: item.label,
		                                    value: item.label		                                   
		                                }
		                            }))
				}
			});
		},
		select: function (event, ui) {	
			document.getElementById('codEdicola').value = ui.item.id;
		}
	});
	
	$("input#autocompletePub").autocomplete({	
		minLength: 3,			
		source: function(request, response) {		
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}${ap}/automcomplete_getProdottoByCodOrTitolo.action",	
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
				content: { 
			    	term: request.term
			    }, 					
				handle: function(data,args) {
					response($.map(data, function(item) {											
		                                return {
		                                    id: item.id,	
		                                    label: item.label,
		                                    value: item.label		                                   
		                                }
		                            }))
				}
			});
		},
		select: function (event, ui) {	
			document.getElementById('codPubblicazione').value = ui.item.id;
			findNumeriPubblicazione(ui.item.id);
		}
	});
	
	findNumeriPubblicazione = function(codPubblicazione) {
		dojo.xhrGet({
			url : "${pageContext.request.contextPath}${ap}/automcomplete_getNumeriCopertina.action?term=" + Number(codPubblicazione),
			preventCache : true,
			handleAs : "json",
			headers : {
				"Content-Type" : "application/json; charset=uft-8"
			},
			handle : function(data, args) {
				var copertina = '<s:property value="numCopertina"/>';
				var $copertineSelect = $('#numCopertina');
				$copertineSelect.empty();
				$copertineSelect.append($("<option/>", {
			        value: "",
			        text: ""
			    }));
				$.map(data, function(item) {
					var sel = copertina == item.id ? "selected" : "";
					$copertineSelect.append($("<option/>", {
				        value: item.id,
				        text: item.label,
				        selected : sel
				    }));
				});
			}
		});
	}
	
	function prepareFieldValues() {
		if ($('#strDataDa').val().trim().length == 0 || $('#strDataA').val().trim().length == 0) {
			$.alerts.dialogClass = "style_1";
			jAlert("<s:text name='error.specificare.intervallo.date'/>", attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$("#strDataDa").focus();
			});
			return false;
		}
		
		return true;
	}
</script>