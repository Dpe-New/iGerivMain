$(document).ready(function() {
	addFadeLayerEvents();
	$("#BollaResaFuoriVoceForm input:text[name^='resoFuoriVoce']").each(function() {
		var $resoField = $(this);
		$resoField.numeric(false);
		var val = $resoField.val().trim();			
		$resoField.data('oldVal', val);
	});
	thumbnailviewer.init();
	if (isEdicolaPromo) {
		disableImagesForPromo($('#popup_name'));
	}
	$("#BollaResaFuoriVoceForm input[name='resoFuoriVoce']:first").focus();
});	

function afterSaveResaFuoriVoce() {
	var hiddenFields = '';
	var arrRowIds = new Array();
	var arrRowCount = 0;
	$($("#BollaResaFuoriVoceForm input:text[name^='resoFuoriVoce']").get().reverse()).each(function() {
		var formCpuValue = $("input:hidden[name='cpu']").val();	
		var mainTextField = $(this);		
		var textResoFuoriVocePk = mainTextField.attr("id");
		var resoVal = mainTextField.val();
		var resoName = mainTextField.attr("id");
		var resoId = mainTextField.attr("id");
		var mainRow = mainTextField.parent().parent();
		var isResaAnticipata = mainRow.attr('divParam').split('?')[1].split('=')[1];
		var rowIndex = resoName.split("|")[4];													
		var arrCpuRows = new Array();
		var arrCpuTextFields = new Array();
		var arrCpuTextNames = new Array();
		var count = 0;
		var found = false;
		var insertBefore = false;
		
		$("#BollaResaTab_table tbody tr[divParam]").each(function() {
				var popURL = $(this).attr('divParam');
				var query= popURL.split('?');
			    var dim= query[1].split('&');	   	   	    	  		  
			    var currCpuValue = dim[0].split('=')[1];
			    if (Number(currCpuValue) == Number(formCpuValue)) {
			    	var cpuRow = $(this);
			    	arrCpuRows[count] = cpuRow;				    				    	
			    	var textReso = cpuRow.find("input:text[name^='reso']");
			    	arrCpuTextFields[count] = textReso;
			    	arrCpuTextNames[count] = textReso.attr('id');				    	
			    	var textResoPk = textReso.attr("id");		
			    	if (Number(textResoPk.split("|")[4]) == Number(textResoFuoriVocePk.split("|")[4])) {
			    		// text field exists => update value				
			    		var giacenza = cpuRow.find("td:nth-child(7)").html().trim();  
			    		var pk1 = textResoPk.replace("reso", "").replace("resoFuoriVoce", "");  				    				    
		    			updateHiddenFields(pk1, textReso.val(), giacenza);						    					    		
			    		textReso.val(resoVal);	
			    		textReso.attr("isnewfield", "false");
			    		arrRowIds[arrRowCount] = pk1;				    						    		
			    		arrRowCount++;		    						    						    			    				
				    	found = true;
				    	return false;
			    	}
			    	count++;
			    }
		});								
		
		if (!found && arrCpuRows.length > 0) {
			// append row to the nearest cpu row in table					
			var maxRowIndex = 0;
			var first = 0;
			for (var i = 0; i < arrCpuRows.length; i++) {
				var currRowIndex = arrCpuTextNames[i].split("|")[4];
				if (i == 0) {
					first = currRowIndex;
				}
				if ((Number(currRowIndex) < Number(rowIndex)) && (Number(currRowIndex) > Number(maxRowIndex))) {
					maxRowIndex = currRowIndex;
				}
			}
			if (maxRowIndex == 0 && first > 0) {
				maxRowIndex = first;
				insertBefore = true;
			} 
			for (i = 0; i < arrCpuRows.length; i++) {
				var row = mainRow.clone();
				var currRowIndex = arrCpuTextNames[i].split("|")[4];
				if (currRowIndex == maxRowIndex) {
					var cpuAttr = arrCpuRows[i].attr("divparam");
					var dim = cpuAttr.split('&');	   	   	    	  		  
			    	var cpu = dim[0].split('=')[1];
			    	var tipoRichiamo = arrCpuRows[i].find("td:nth-child(13)").find(".richiamoResaCls").html();					
			    	var giacenza = row.find("td:nth-child(6)").html().trim();
					
					//15/12/2016 CDL - Gestione Ceste
			    	//var img = arrCpuRows[i].find("td:nth-child(14)").html();
			    	var trTag = null;
					if(isCDL){
						var img = arrCpuRows[i].find("td:nth-child(15)").html();
						var cesta = arrCpuRows[i].find("td:nth-child(14)").html();
						trTag = getRowHtmlCDL(row, cpu, tipoRichiamo, img, resoId, resoVal, false, isResaAnticipata,cesta);
					}else{
						var img = arrCpuRows[i].find("td:nth-child(14)").html();
						trTag = getRowHtml(row, cpu, tipoRichiamo, img, resoId, resoVal, false, isResaAnticipata);
					}
					//var trTag = getRowHtml(row, cpu, tipoRichiamo, img, resoId, resoVal, false, isResaAnticipata);
					
					
					
					if (resoVal != null && resoVal != '') {
						hiddenFields += addExtraHiddenFields(resoId, resoVal, giacenza);
					}
					if (resoVal != '' && Number(resoVal) > 0) {
						if (insertBefore) {
							$(trTag).insertBefore(arrCpuRows[i]);	
						} else {						
							$(trTag).insertAfter(arrCpuRows[i]);	
						}
						$("#content1").css({"height": ($("#BollaResaForm").height() + 30) + "px"});
						arrRowIds[arrRowCount] = resoId.replace("reso", "").replace("resoFuoriVoce", "");
						arrRowCount++;
						thumbnailviewer.init();
					}
					break;
				}					
			}						
		} else if (!found) {
			// append row to end of table
			var row = mainRow.clone();
			
			//15/12/2016 CDL - Gestione Ceste
			var trTag = null;
			if(isCDL){
				var img = $(row).find("td:nth-child(11)").html();
				trTag = getRowHtmlCDL(row, formCpuValue, "", img, resoId, resoVal, false, isResaAnticipata);		
			}else{
				var img = $(row).find("td:nth-child(10)").html();
				trTag = getRowHtml(row, formCpuValue, "", img, resoId, resoVal, false, isResaAnticipata);		
			}
			//var trTag = getRowHtml(row, formCpuValue, "", img, resoId, resoVal, false, isResaAnticipata);			
			
			var giacenza = row.find("td:nth-child(6)").html().trim();			
			if (resoVal != null && resoVal != '') {
				hiddenFields += addExtraHiddenFields(resoId, resoVal, giacenza);
			}
			if (resoVal != '' && Number(resoVal) > 0) {		
				$(trTag).insertBefore($('#BollaResaTab_table tbody tr:last'));
				$("#content1").css({"height": ($("#BollaResaForm").height() + 30) + "px"});
				arrRowIds[arrRowCount] = resoId.replace("reso", "").replace("resoFuoriVoce", "");
				arrRowCount++;
				thumbnailviewer.init();
			}
		}
	});
	// Iterate all rows and set style
	var count = 0;
	$("#BollaResaTab_table tbody tr[divParam]").each(function() {
		var clazz;
		if (count % 2 == 0) {
			clazz = "even";
		} else {
			clazz = "odd";
		}		
		$(this).removeClass('odd').removeClass('even').addClass(clazz);
		$(this).mouseout(function() {
			$(this).removeClass(clazz).addClass(clazz == 'even' ? 'odd' : 'even');
		});
		count++;
	});
	if (hiddenFields != '') {
		$('#extraHidden').append(hiddenFields);	
	}	
	for (var i = 0; i < arrRowIds.length; i++) {		
		var el = document.getElementById('reso' + arrRowIds[i]);	
		$(el).keydown( focusBarcodeField );
		if (i == 0) {
			lastFieldFocusId = 'reso' + arrRowIds[i];
		}
		setTotals(el, true);
	}
	setOnChangeEventOnTextFields();
	setBorderToFuoriVoce();
	deleteEmptyRowsFuoriVoce();
	setFocusedFieldStyle();
	var $tds = $("#BollaResaTab_table tbody").find("td:nth-child(3)");
	$tds.unbind("click", tableRowClick);
	$tds.css("cursor","pointer").click(tableRowClick);
	doCloseLayer(function() {
		if (lastFieldFocusId != '') {
			var fId = lastFieldFocusId.replace(/([|@])/g,'\\\$1');
			var $lff = $("#" + fId);
			if (!$lff.is(':disabled')) {
				setLastFocusedElement();
				$lff.focus();
				return false;
			}
		}
	});	
	
}

function getRowHtml(row, cpu, tipRichiamo, img, resoId, resoVal, isContoDeposito, isResaAnticipata) {
	resoVal = isNaN(resoVal) ? '' : Number(resoVal);
	var prezzoLordo = isNaN(parseLocalNum(row.find("td:nth-child(8)").text().trim())) ? '' : Number(parseLocalNum(row.find("td:nth-child(8)").text().trim())).toFixed(2);
	var prezzoNetto = isNaN(parseLocalNum(row.find("td:nth-child(9)").text().trim())) ? '' : Number(parseLocalNum(row.find("td:nth-child(9)").text().trim())).toFixed(4);
	var importoLordo = (!isContoDeposito) ? ((resoVal == '' || prezzoLordo == '') ? '' : (Number(resoVal) * Number(prezzoLordo)).toFixed(2)) : 0;
	var importoNetto = (!isContoDeposito) ? ((resoVal == '' || prezzoNetto == '') ? '' : (Number(resoVal) * Number(prezzoNetto)).toFixed(4)) : 0;
	var pkVal = resoId.replace("resoFuoriVoce", "").replace("reso", "");
	var id = resoId.replace("resoFuoriVoce", "reso");
	var trTag = "<tr class='odd' style='height: 30px;' onmouseover='this.className=\"highlight\"' onmouseout='this.className=\"odd\"' divparam='#?cpu=" + cpu + "' iv='' pv=''>";									
	var sottotitolo = row.find("td:nth-child(2)").text().trim().length > 0 ? "<br/><span class='sottotitoloBolla'>" + row.find("td:nth-child(2)").text() + "</span>" : "";
	var tipRich = (isResaAnticipata.toString() === 'true') ? msgResaAnticipata + " "  + tipRichiamo : tipRichiamo;
	trTag += "<td width='2%'>&nbsp;</td>";
	trTag += "<td style='text-align: center;' width='4%'>" + cpu + "</td>";
	trTag += "<td style='cursor: pointer;' width='23%'>" + row.find("td:nth-child(1)").html() + sottotitolo + "</td>";
	trTag += "<td style='text-align: center;' width='4%' style='font-size:12px;'>" + row.find("td:nth-child(3)").html() + "</td>";
	trTag += "<td style='text-align: center;' width='5%'>" + row.find("td:nth-child(4)").html() + "</td>";
	trTag += "<td style='font-size: 120%; font-weight: bold; text-align: right; border-width: 1px medium; border-style: solid none; border-color: red -moz-use-text-color;' width='5%'><input name='resoFuoriVoce[&#39;" + pkVal + "&#39;]' id='" + id + "' value='" + resoVal + "' isContoDeposito='" + isContoDeposito + "' style='font-size:120%; font-weight:bold; text-align:right; border-color: rgb(153, 153, 153);' size='2' maxlength='3' validateisnumeric='true' isnewfield='true' type='text'><input type='hidden' name='modificatoFV[&#39;" + pkVal + "&#39;]' id='modificatoFV" + pkVal + "' value='false'/></td>";
	trTag += "<td style='text-align: center;' width='5%'>" + row.find("td:nth-child(6)").html() + "</td>";				
	trTag += "<td style='text-align: center;' style='text-align:center;' width='8%'>" + row.find("td:nth-child(7)").html() + "</td>";
	trTag += "<td style='text-align: right;' width='6%'>" + displayNum(prezzoLordo) + "</td>";
	trTag += "<td style='text-align: right;' width='6%'>" + displayNum(prezzoNetto) + "</td>";	
	trTag += "<td style='text-align: right;' width='7%'>" + importoLordo + "</td>";
	trTag += "<td style='text-align: right;' width='7%'>" + importoNetto + "</td>";					
	trTag += "<td style='text-align: center;' width='12%'>" + tipRich + "</td>";
	trTag += "<td style='text-align: center;' width='4%'>" + img + "</td>";
	trTag += "</tr>";	
	return trTag;
}

//15/12/2016 CDL - Gestione Ceste
function getRowHtmlCDL(row, cpu, tipRichiamo, img, resoId, resoVal, isContoDeposito, isResaAnticipata,cesta) {
	resoVal = isNaN(resoVal) ? '' : Number(resoVal);
	var prezzoLordo = isNaN(parseLocalNum(row.find("td:nth-child(8)").text().trim())) ? '' : Number(parseLocalNum(row.find("td:nth-child(8)").text().trim())).toFixed(2);
	var prezzoNetto = isNaN(parseLocalNum(row.find("td:nth-child(9)").text().trim())) ? '' : Number(parseLocalNum(row.find("td:nth-child(9)").text().trim())).toFixed(4);
	var importoLordo = (!isContoDeposito) ? ((resoVal == '' || prezzoLordo == '') ? '' : (Number(resoVal) * Number(prezzoLordo)).toFixed(2)) : 0;
	var importoNetto = (!isContoDeposito) ? ((resoVal == '' || prezzoNetto == '') ? '' : (Number(resoVal) * Number(prezzoNetto)).toFixed(4)) : 0;
	var pkVal = resoId.replace("resoFuoriVoce", "").replace("reso", "");
	var id = resoId.replace("resoFuoriVoce", "reso");
	var trTag = "<tr class='odd' style='height: 30px;' onmouseover='this.className=\"highlight\"' onmouseout='this.className=\"odd\"' divparam='#?cpu=" + cpu + "' iv='' pv=''>";									
	var sottotitolo = row.find("td:nth-child(2)").text().trim().length > 0 ? "<br/><span class='sottotitoloBolla'>" + row.find("td:nth-child(2)").text() + "</span>" : "";
	var tipRich = (isResaAnticipata.toString() === 'true') ? msgResaAnticipata + " "  + tipRichiamo : tipRichiamo;
	
	var cdl_cesta = (typeof cesta === 'undefined')?'':cesta;
	
	trTag += "<td width='2%'>&nbsp;</td>";
	trTag += "<td style='text-align: center;' width='4%'>" + cpu + "</td>";
	trTag += "<td style='cursor: pointer;' width='23%'>" + row.find("td:nth-child(1)").html() + sottotitolo + "</td>";
	trTag += "<td style='text-align: center;' width='4%' style='font-size:12px;'>" + row.find("td:nth-child(3)").html() + "</td>";
	trTag += "<td style='text-align: center;' width='5%'>" + row.find("td:nth-child(4)").html() + "</td>";
	trTag += "<td style='font-size: 120%; font-weight: bold; text-align: right; border-width: 1px medium; border-style: solid none; border-color: red -moz-use-text-color;' width='5%'><input name='resoFuoriVoce[&#39;" + pkVal + "&#39;]' id='" + id + "' value='" + resoVal + "' isContoDeposito='" + isContoDeposito + "' style='font-size:120%; font-weight:bold; text-align:right; border-color: rgb(153, 153, 153);' size='2' maxlength='3' validateisnumeric='true' isnewfield='true' type='text'><input type='hidden' name='modificatoFV[&#39;" + pkVal + "&#39;]' id='modificatoFV" + pkVal + "' value='false'/></td>";
	trTag += "<td style='text-align: center;' width='5%'>" + row.find("td:nth-child(6)").html() + "</td>";				
	trTag += "<td style='text-align: center;' style='text-align:center;' width='8%'>" + row.find("td:nth-child(7)").html() + "</td>";
	trTag += "<td style='text-align: right;' width='6%'>" + displayNum(prezzoLordo) + "</td>";
	trTag += "<td style='text-align: right;' width='6%'>" + displayNum(prezzoNetto) + "</td>";	
	trTag += "<td style='text-align: right;' width='7%'>" + importoLordo + "</td>";
	trTag += "<td style='text-align: right;' width='7%'>" + importoNetto + "</td>";					
	trTag += "<td style='text-align: center;' width='12%'>" + tipRich + "</td>";
	trTag += "<td style='text-align: center;' width='5%'>" + cdl_cesta + "</td>"; 
	trTag += "<td style='text-align: center;' width='4%'>" + img + "</td>";
	trTag += "</tr>";	
	return trTag;
}


function addExtraHiddenFields(pk, reso, giacenza) {
	pk = pk.replace("reso", "").replace("resoFuoriVoce", "");
	var hiddenResoOld = "<input type='hidden' id='reso_old_" + pk + "' value='" + reso + "'/>";
	var hiddenGiacenzaOld = "<input type='hidden' id='giacenza_old_" + pk + "' value='" + giacenza + "'/>";
	return hiddenResoOld + hiddenGiacenzaOld;
}

function updateHiddenFields(pk, reso, giacenza) {		
	document.getElementById('reso_old_' + pk).value = reso;
	document.getElementById('giacenza_old_' + pk).value = giacenza;	
}