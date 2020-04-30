<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<script>
	$(document).ready(function() {
		setContentDivHeight(70);
		
		
		$("select#codDl").change(function(){
			
			var select_coddl = $(this).val();  
			ray.ajax();
    		dojo.xhrGet({
    			url: "${pageContext.request.contextPath}/pubblicazioniRpc_getProfiliDL.action?select_coddl="+select_coddl+"",			
    			handleAs: "json",				
    			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
    			preventCache: true,
    			handle: function(data,args) {
    				unBlockUI();
					if (args.xhr.status == 200) {
						$("select#profiliDL").find('option').remove();
						for (i = 0; i < data.length; i++) {
							$("select#profiliDL").append('<option value="'+data[i].key+'">' +data[i].value + '</option>');
						}
						
						if(data.length>0){
							$("select#profiliDL").removeAttr("disabled");
						}else{
							$("select#profiliDL").attr("disabled","true")
						}
						
					} else {
						alert('not ok');
					} 
    			}
    			
    			
    	    });
			
			
		});
		
		
		
		
		
		$("input#edicolaLabel").autocomplete({	
			minLength: 3,			
			source: function(request, response) {	
				var strUrl = "${pageContext.request.contextPath}${ap}/automcomplete_edicoleWeb.action" + (($("#codDl").val().length > 0) ? "?coddl=" + $("#codDl").val() : "");
				dojo.xhrGet({
					url: strUrl,	
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
				$('#codEdicolaWebStr').val(ui.item.id);	
			}
		});	
		
		var $dateFields = $("input:text[id*=dataInserimento], input:text[id*=dataSospensione], input:text[id*=edicolaPlusdtIni], input:text[id*=edicolaPlusdtFin], input:text[id*=edicolaPromodtIni], input:text[id*=edicolaPromodtFin]");
		
		$dateFields.click(showCalendar);
		
		$(":input[id*=profilo]").change(function() {
			doChange($(this));
		});
		
		$(":checkbox[id*=edicolaTest]").click(function() {
			doChange($(this));
		});
		
		$(":checkbox[id*=edicolaPromo]").click(function() {
			doChange($(this));
		});
		
		$(":checkbox[id*=edicolaPlus]").click(function() {
			doChange($(this));
		});
		
		$dateFields.change(function() {
			doChange($(this));
		});
		
		function showCalendar() { 
        	show_cal(document.getElementById($(this).attr('id')));              			            		    		
		}
		
	});
	
	function doSubmit() { 
		if ($("#edicolaLabel").val() == '') {
			document.getElementById('codEdicolaWebStr').value = '';
		} else if (!isNaN($("#edicolaLabel").val())) {
			document.getElementById('codEdicolaWebStr').value = $("#edicolaLabel").val();
		}
		return true;
	};
	
	function cal2_on_change(cal, object_code) {
		if (object_code == 'day') {
			formElement.value = cal.get_formatted_date(format);
			cal.hide();
			cal_obj2 = null;
			doChange(formElement);
		}
	}
	
	function doChange(formElement) {
		$(formElement).attr('isChanged', 'true');	
		$(formElement).closest('tr').find('input:hidden[name^=pk]').attr('isChanged', 'true');	
	}
	
	function setFieldsToSave(isSubmit) {
		var arrDtIniPlus = new Array();
		var arrDtFinPlus = new Array();
		var arrDtIniPromo = new Array();
		var arrDtFinPromo = new Array();
		$("input:text[id*=dataInserimento],input:text[id*=dataSospensione],input:text[id*=edicolaPlusdtIni],input:text[id*=edicolaPlusdtFin],input:text[id*=edicolaPromodtIni],input:text[id*=edicolaPromodtFin],input:hidden[name^=pk],:input[id*=profilo]").each(function() {	
			var $field = $(this);
			if (isSubmit) {
				var $id = $field.attr("id");
				var $val = $field.val();
				if ($field.attr('isChanged') != 'true') {		
					$field.attr('disabled', true);
				} else if ($id.indexOf("profilo") != -1) {
					var idEdicola = $field.closest("tr").find("input:hidden[name='pk']").first().val();
					$field.find("option:selected").val(idEdicola + "|" + $val);
				} else if ($id.indexOf("edicolaPlusdtIni") != -1) {
					arrDtIniPlus.push($id + "|" + $val);
				} else if ($id.indexOf("edicolaPlusdtFin") != -1) {
					arrDtFinPlus.push($id + "|" + $val);
				} else if ($id.indexOf("edicolaPromodtIni") != -1) {
					arrDtIniPromo.push($id + "|" + $val);
				} else if ($id.indexOf("edicolaPromodtFin") != -1) {
					arrDtFinPromo.push($id + "|" + $val);
				}
			} else {
				$field.attr('disabled', false);
				$field.removeAttr('isChanged');
			}	
		});
		$("#edicolaPlusdtIniHidden").val(arrDtIniPlus);
		$("#edicolaPlusdtFinHidden").val(arrDtFinPlus);
		$("#edicolaPromodtIniHidden").val(arrDtIniPromo);
		$("#edicolaPromodtFinHidden").val(arrDtFinPromo);
		$(":checkbox[id*=edicolaTest]").each(function() {		
			if (isSubmit) {
				var $chk = $(this);
				if ($chk.attr('isChanged') == 'true') {	
					var val = $chk.val().split("|")[0];
					$chk.val(val + '|' + $chk.is(':checked'));
					$chk.attr('checked', true);
				}
			} else {
				var $chk = $(this);
				if ($chk.attr('isChanged') == 'true') {
					if ($chk.val().indexOf('true') != -1) {
						$chk.attr('checked', true);
					} else {
						$chk.attr('checked', false);
					}
				}
				$(this).removeAttr('isChanged');
			}
		});
		$(":checkbox[id*=edicolaPromo]").each(function() {		
			if (isSubmit) {
				var $chk = $(this);
				if ($chk.attr('isChanged') == 'true') {	
					var val = $chk.val().split("|")[0];
					$chk.val(val + '|' + $chk.is(':checked'));
					$chk.attr('checked', true);
				}
			} else {
				var $chk = $(this);
				if ($chk.attr('isChanged') == 'true') {
					if ($chk.val().indexOf('true') != -1) {
						$chk.attr('checked', true);
					} else {
						$chk.attr('checked', false);
					}
				}
				$(this).removeAttr('isChanged');
			}
		});
		$(":checkbox[id*=edicolaPlus]").each(function() {		
			if (isSubmit) {
				var $chk = $(this);
				if ($chk.attr('isChanged') == 'true') {	
					var val = $chk.val().split("|")[0];
					$chk.val(val + '|' + $chk.is(':checked'));
					$chk.attr('checked', true);
				}
			} else {
				var $chk = $(this);
				if ($chk.attr('isChanged') == 'true') {
					if ($chk.val().indexOf('true') != -1) {
						$chk.attr('checked', true);
					} else {
						$chk.attr('checked', false);
					}
				}
				$(this).removeAttr('isChanged');
			}
		});
		return true;
	}
	
	openPopUpDettaglioProfilo = function(codDl,pkProfilo) {
		
		var url = appContext + "/profilazione_showDettaglioProfilo.action?codEdicolaWeb=" + pkProfilo+"&codDl="+codDl ;
		openDiv('popup_name_dettaglio_profilo', 1200, 700, url);
		
	};
	
	
	
</script>