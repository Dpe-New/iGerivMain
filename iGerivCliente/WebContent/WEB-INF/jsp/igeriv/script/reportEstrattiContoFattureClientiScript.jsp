<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		$('#dataDaStr').click(function() { 
	    	show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});	
		
		$('#dataAStr').click(function() { 
		    show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});
		
		$("img[name='imgPdf']").tooltip({ 
			delay: 0,  
		    showURL: false
		});
		
		$("#ECFattureClientiTab_table tbody tr td:nth-child(6)").click(function() { 
			var $td = $(this);
			var $tr = $td.closest("tr");
		    var popURL = $tr.attr('divParam'); 
		    var query= popURL.split('&'); 
		    var tipo = query[0].split('=')[1];	 
		    var codCliente = query[1].split('=')[1];
		    var fileName = query[2].split('=')[1];
		    if (tipo == '<s:text name="constants.FATTURA"/>') {
		    	window.open('edImg.action?fileName=' + fileName + '&type=7', '_blank');
		    } else if (tipo == '<s:text name="constants.ESTRATTO_CONTO"/>') {
		    	var $dtCompTd = $td.prev("td").prev("td").prev("td");
		    	var dataEstrattoConto = $dtCompTd.text();
		    	$("#dataEstrattoConto").val(dataEstrattoConto);
		    	$("#codCliente").val(codCliente);
				$("#EstrattoContoClientiForm").submit();
		    }
			return false;
		});
		
		doRicerca = function() {
			var dataDa = $("#dataDaStr").val();
			var dataA = $("#dataAStr").val();
			if (dataDa.trim() != '' && !checkDate(dataDa)) {
				$.alerts.dialogClass = "style_1";
				jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$('#dataDaStr').focus();
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false; 
			} 
			if (dataA.trim() != '' && !checkDate(dataA)) {
				$.alerts.dialogClass = "style_1";
				jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$('#dataAStr').focus();
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false; 
			} 
			$("#filterForm").submit();
		}
	});
</script>