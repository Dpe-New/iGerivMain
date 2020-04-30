<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		addFadeLayerEvents();
		setContentDivHeight(10);
		$("#imgPrintEC").tooltip({
			delay: 0,  
		    showURL: false
		}); 
		//$("#strDataA").focus();
	});
	
	function changeDataEstrattoConto(obj) {
		ray.ajax();
		var strData = $(obj).val();
		dojo.xhrGet({
			url: "${pageContext.request.contextPath}/pubblicazioniRpc_getSingleEstrattoContoCliente.action?dataAStr=" + strData + "&codCliente=" + $("input:hidden[name='codCliente']").val(),	
			preventCache: true,
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=utf-8"}, 	
			handle: function(data,args) {
				var html = '';
				var count = 0;
				for(var i = 0; i < data.length; i++) {
			        var obj = data[i];
			        trClass = (count % 2 == 0) ? "even" : "odd";
			        html += '<tr class="' +  trClass + '" style="height: 25px;" onmouseover="this.className=\'highlight\'" onmouseout="this.className=\'' + trClass + '\'">';
			        html += '<td style="text-align: center;" width="30%">' + obj.tipoMovimento + '</td>';
			        html += '<td style="text-align: center;" width="30%">' + obj.dataMovimento + '</td>';
			        html += '<td style="text-align: right;" width="20%">' + obj.importoDare + '</td>';
			        html += '<td style="text-align: right;" width="20%">' + obj.importoAvere + '</td>';
			        html += '</tr>';
			        count++;
			    }
				$("#EstrattoContoClientiTab_table tbody").html(html);
				$("#EstrattoContoClientiTab_table").tablesorter();
				setTimeout(function() {
					$("#impDebito").html(getDebitoFormatted());
					unBlockUI();
				}, 100);
			}
		});
	}
	
	function getDebito() {
		var dare = 0;
		var avere = 0;
		$("#EstrattoContoClientiTab_table tbody tr td:nth-child(3)").each(function() {
			dare += Number(parseLocalNum($(this).text().trim()));
		});
		$("#EstrattoContoClientiTab_table tbody tr td:nth-child(4)").each(function() {
			avere += Number(parseLocalNum($(this).text().trim()));
		});
		var num = Number(dare - avere).toFixed(2);
		return num;
	}
	
	function getDebitoFormatted() {
		return '&#8364;&nbsp;' + displayNum(getDebito());
	}
	
	function printEC() {
		if ($("#strDataA").val() != '') {
			document.forms.EstrattoContoClientiForm.ec_eti.value='';
			$("#EstrattoContoClientiForm").attr("action", "report_showEstrattoContoClienti.action"); 	
			$("#EstrattoContoClientiForm").attr("target", "_blank"); 
			$("#EstrattoContoClientiForm").submit();
		}
	}
</script>