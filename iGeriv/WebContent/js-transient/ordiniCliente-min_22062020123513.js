function spuntaSave() {
	if (spunta()) {
		//setFormAction('LibriScolasticiVenditaDettForm','libriScolasticiVendita_showConferma.action', '', 'messageDiv');
		setFormAction('LibriScolasticiVenditaDettForm','libriScolasticiVendita_showConferma.action', '', 'messageDiv', true, null, function() {
						$('#LibriScolasticiVenditaForm_ordineFornitore').val("")
						$("#ricerca").trigger('click');
					}, null, true);
		return true;
	}
	alert ("Ordini non selezionati");
}
function spuntaReport() {
	var selectOrd = ordiniSel();
	if (selectOrd==null) {
		alert ("Ordini non selezionati");
		return false;
	}
	var form = $("#ReportVenditaForm");
	form.empty();
	form.append('<input type="hidden" name="ordiniSelezionati" value="' + selectOrd + '"/>');
	form.attr("target","_blank");
	form.submit();
	
	return true;
}

function ordiniSel() {
	var result=false;
	var arrSpunte = new Array();
	var checks = $("input[name='spunte']");
	if (checks==null || checks.length==0) {
		return null;
	}
	
	for (var i = 0; i<checks.length; i++) {
		var check = checks[i];
		if (check.checked) {
			arrSpunte.push(check.value);
			result=true;
		}
	}
	return result ? arrSpunte : null;
}

function spunta() {
	var select = ordiniSel();
	if (select==null) {
		return false;
	}
	$("#ordiniSelezionati").val(select);
	return true;
}
function updateTotale(check) {
	var table = document.getElementById("LibriScolasticiVenditaTab_table");
	var divTotale = document.getElementById("divTotale");
	var checks = $("input[name='spunte']");
	
	
	if (table == null || $(table).length == 0) {
		return false;
	}
	var tot=0.0;
	
	for (var i = 1; i< table.rows.length; i++) {
		var check=checks[i-1];
		if (!check.checked) {
			continue;
		}
		var row = table.rows[i];
		//prezzo copertinatura
		var cell = row.cells[5];
		//prezzo libro
		var cell_libro = row.cells[6];
		
		var imp = $(cell).text();
		var importo = imp.replace(/[\.,]/g, "");
		tot = tot + (importo/100);
		
		var imp_libro = $(cell_libro).text();
		var importo_libro = imp_libro.replace(/[\.,]/g, "");
		tot = tot + (importo_libro/100);
		
	}
	
	
	divTotale.innerHTML ="[   Totale selezionato:        <b>" + tot.toFixed(2) + "</b> Euro ]";
	return true;
}

/////////////////
function ricercaCliente(button) {
	setFormAction('LibriScolasticiVenditaForm','libriScolasticiVendita_showClienti.action', '', 'messageDiv');
	return false;
}