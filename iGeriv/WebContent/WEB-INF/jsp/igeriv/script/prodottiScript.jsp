<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		if ($("#categoria").val() != '') {
			getSottocategorie($("#categoria").val());
		}
		$("#categoria").change(function() {
			getSottocategorie($(this).val());
		});
		setGiacenza();
		setContentDivHeight(20);
		$("#codice").focus();
	});
	
	function setGiacenza() {
		var count = 0;
		$("#ProdottiTab_table tbody tr td:nth-child(8)").each(function () {
			$td = $(this);
			$val = $td.text();
			var divCircle = '';
			if (!isNaN($val.trim()) && Number($val.trim()) > 0) {
				divCircle = '<div id="disponibilita_' + count + '" title="Disponibile" style="margin-top:0px; margin-left:auto; margin-right:auto; text-align:center; width: 1.0em; height: 1.3em; -webkit-border-radius: 1em; -moz-border-radius: 1em; background:#009933"/>';
			} else {
				divCircle = '<div id="disponibilita_' + count + '" title="Non Disponibile" style="margin-top:0px; margin-left:auto; margin-right:auto; text-align:center; width: 1.0em; height: 1.3em; -webkit-border-radius: 1em; -moz-border-radius: 1em; background:#ff0000"/>';
			}
			$td.html(divCircle);
			$("#disponibilita_" + count).tooltip({
					delay: 0,  
				    showURL: false,			    
				    bodyHandler: function() {
				    	var str = '<div><s:text name="igeriv.leggenda"/>&nbsp;:</div>';
				    	str += '<div style="margin-top:0px; margin-left:auto; margin-right:auto; text-align:center; width: 1.0em; height: 1.3em; -webkit-border-radius: 1em; -moz-border-radius: 1em; background:#009933"/><b><s:text name="igeriv.prodotto.disponibile"/></b>';
				    	str += '<br><div style="margin-top:0px; margin-left:auto; margin-right:auto; text-align:center; width: 1.0em; height: 1.3em; -webkit-border-radius: 1em; -moz-border-radius: 1em; background:#ff0000"/><b><s:text name="igeriv.prodotto.non.disponibile"/></b>';
				    	return str;
				    }
			});
			count++;
		});
	}
	
	function getSottocategorie(categoria) {
		dojo.xhrGet({
			url: "${pageContext.request.contextPath}/pubblicazioniRpc_sottocategorieProdottiDl.action?categoria=" + categoria,	
			preventCache: true,
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=utf-8"}, 	
			handle: function(data,args) {
				var list = '<option value=""></option>';	
	            for (i = 0; i < data.length; i++) {
	            	var strChecked = '';
	            	<s:if test="sottocategoria != null && sottocategoria != ''">
	            	if (data[i].id == <s:text name="sottocategoria"/>) {
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
	
	function submitForm() {
		$("#ProdottiForm").submit();
	}
</script>