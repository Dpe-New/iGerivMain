<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {	
		if ($("#categoria").val() != '') {
			getSottocategorie($("#categoria").val());
		} else {
			$("#sottocategoria").empty();
		}
		$("#categoria").change(function() {
			var val = $(this).val() == '' ? -1 : $(this).val();
			getSottocategorie(val);
		});
		$("input#prodottoLabel").autocomplete({
			minLength: 4,			
			source: function(request, response) {		
				dojo.xhrGet({
					url: "${pageContext.request.contextPath}${ap}/automcomplete_getProdottoNonEditorialeByCodEdicolaOrDescr.action",
					delay: 100,
					preventCache: true,
					handleAs: "json",				
					headers: { "Content-Type": "application/json; charset=utf-8"}, 	
					content: { 
				    	term: request.term
				    }, 					
					handle: function(data,args) {
						response($.map(data, function(item) {
											return {
			                                	id: item.codProdotto,	
			                                    label: item.descrizione,
			                                    value: item.descrizione		                                   
			                                }
			                            }))
					}
				});
			},
			select: function (event, ui) {
				document.getElementById('codProdotto').value = ui.item.id;		
			}
		});
	});	
	
	function getSottocategorie(categoria) {
		dojo.xhrGet({
			url: "${pageContext.request.contextPath}/pubblicazioniRpc_sottocategorieProdotti.action?categoria=" + categoria,	
			preventCache: true,
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=utf-8"}, 	
			handle: function(data,args) {
				var list = '<option value=""></option>';	
	            for (i = 0; i < data.length; i++) {
	            	var strChecked = '';
	            	<s:if test="codSottoCategoria != null && codSottoCategoria != ''">
	            	if (data[i].id == <s:text name="codSottoCategoria"/>) {
	            		strChecked = ' selected ';
	            	}
	            	</s:if>
	            	list += '<option value="' + data[i].id + '" ' + strChecked + '>' + data[i].label + '</option>';
	            }
	            $("#sottocategoria").empty();
	            $("#sottocategoria").html(list);
			}
		});		
	}
	
	function doSubmit() { 
		if ($("#prodottoLabel").val() == '') {
			document.getElementById('codProdotto').value = '';
		} else if (!isNaN($("#prodottoLabel").val())) {
			document.getElementById('codProdotto').value = $("#prodottoLabel").val();
		}
		return true;
	};
</script>
