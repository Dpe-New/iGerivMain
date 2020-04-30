<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		
		var tr1_s1Dal = '<s:property value="campagnaTurno1.S1_Op1DataDal"/>'; 
		var tr1_s1Al = '<s:property value="campagnaTurno1.S1_Op1DataAl"/>'; 
		var tr1_s2Dal = '<s:property value="campagnaTurno1.S2_Op1DataDal"/>'; 
		var tr1_s2Al = '<s:property value="campagnaTurno1.S2_Op1DataAl"/>'; 
		var tr2_s1Dal = '<s:property value="campagnaTurno2.S1_Op1DataDal"/>'; 
		var tr2_s1Al = '<s:property value="campagnaTurno2.S1_Op1DataAl"/>'; 
		var tr2_s2Dal = '<s:property value="campagnaTurno2.S2_Op1DataDal"/>'; 
		var tr2_s2Al = '<s:property value="campagnaTurno2.S2_Op1DataAl"/>'; 
		
		$("#anagraficaTable4 *").attr("disabled","disabled");
		$("#anagraficaTable5 *").attr("disabled","disabled");
		$("#anagraficaTable6 *").attr("disabled","disabled");
		
		$('input:radio[name^=campagnaEdicola.flgaperto9227]').change(function() {
			if($(this).val() == 0){
				$("#anagraficaTable4 *").removeAttr("disabled");
				$("#anagraficaTable5 *").removeAttr("disabled");
				//$("#anagraficaTable6 *").removeAttr("disabled");
			}else{
				$("input:radio[name^=campagnaEdicola.turno9227]").removeAttr("checked");
				$("#anagraficaTable4 *").attr("disabled","disabled");
				$("#anagraficaTable5 *").attr("disabled","disabled");
				$("#anagraficaTable6 *").attr("disabled","disabled");
				
				$("#S1_CheckBox").attr('checked', false);
				$("#S2_CheckBox").attr('checked', false);
				$("#S3_CheckBox").attr('checked', false);
				
				$("#S1_Op1DataDal").val("");
				$("#S1_Op1DataAl").val("");
				$("#S2_Op1DataDal").val("");
				$("#S2_Op1DataAl").val("");
				$("#S1_TotGiorni").val("0");
				$("#S2_TotGiorni").val("0");
				$("#S1_TotGiorni").removeAttr('style');
				$("#S2_TotGiorni").removeAttr('style');
				
				$("#S3_Op1DataDal").val("");
				$("#S3_Op1DataAl").val("");
				$("#S3_TotGiorni").val("0");
				$("#S3_TotGiorni").removeAttr('style');
			}
		});
		
		$('input:radio[name^=campagnaEdicola.turno9227]').change(function() {
			if($(this).val() == 1){
				$("#S1_TotGiorni").removeAttr('style');
				$("#S2_TotGiorni").removeAttr('style');
				
				$("#S1_Op1DataDal").val(tr1_s1Dal);
				$("#S1_Op1DataAl").val(tr1_s1Al);
				$("#S2_Op1DataDal").val(tr1_s2Dal);
				$("#S2_Op1DataAl").val(tr1_s2Al);
				
				var DT_tr1_s1Dal = parseDate(tr1_s1Dal);
				var DT_tr1_s1Al = parseDate(tr1_s1Al);
				var totGiorniSC1  = days_between(DT_tr1_s1Dal,DT_tr1_s1Al);
				$("#S1_TotGiorni").val(totGiorniSC1);
				if(totGiorniSC1>21){
					$("#S1_TotGiorni").css("background","red");
				}
				
				var DT_tr1_s2Dal = parseDate(tr1_s2Dal);
				var DT_tr1_s2Al = parseDate(tr1_s2Al);
				var totGiorniSC2 = days_between(DT_tr1_s2Dal,DT_tr1_s2Al);
				$("#S2_TotGiorni").val(totGiorniSC2);
				if(totGiorniSC2>14){
					$("#S2_TotGiorni").css("background","red");
				}
				
			}else{
				$("#S1_TotGiorni").removeAttr('style');
				$("#S2_TotGiorni").removeAttr('style');
				
				$("#S1_Op1DataDal").val(tr2_s1Dal);
				$("#S1_Op1DataAl").val(tr2_s1Al);
				$("#S2_Op1DataDal").val(tr2_s2Dal);
				$("#S2_Op1DataAl").val(tr2_s2Al);
				
				var DT_tr2_s1Dal = parseDate(tr2_s1Dal);
				var DT_tr2_s1Al = parseDate(tr2_s1Al);
				var totGiorniSC1  = days_between(DT_tr2_s1Dal,DT_tr2_s1Al);
				$("#S1_TotGiorni").val(totGiorniSC1);
				if(totGiorniSC1>21){
					$("#S1_TotGiorni").css("background","red");
				}
								
				var DT_tr2_s2Dal = parseDate(tr2_s2Dal);
				var DT_tr2_s2Al = parseDate(tr2_s2Al);
				var totGiorniSC2 = days_between(DT_tr2_s2Dal,DT_tr2_s2Al);
				$("#S2_TotGiorni").val(totGiorniSC2);
				if(totGiorniSC2>14){
					$("#S2_TotGiorni").css("background","red");
				}
				
			}
		});
		
		$('#S1_CheckBox').click(function() { 
			if($('#S1_CheckBox').is(':checked')) {
				$("#anagraficaTable6 *").attr("disabled","disabled");
				$("#S2_CheckBox").attr('checked', false);
				$("#S3_CheckBox").attr('checked', false);
				$("#S2_TotGiorni").removeAttr('style');
				$("#S3_TotGiorni").removeAttr('style');
				
				var txtVal_s1Dal = $('#S1_Op1DataDal').val();
		        if(!isDate(txtVal_s1Dal)){
		        	jAlert('Campo data non conforme', '<s:text name="msg.alert.attenzione"/>', function() {
						$.alerts.dialogClass = null;
						//$("#S1_Op1DataDal").focus();
					});
					return false;
		        }
		        var txtVal_s1Al = $('#S1_Op1DataAl').val();
		        if(!isDate(txtVal_s1Al)){
		        	jAlert('Campo data non conforme', '<s:text name="msg.alert.attenzione"/>', function() {
						$.alerts.dialogClass = null;
						//$("#S1_Op1DataAl").focus();
					});
					return false;
		        }
		        var DT_txtVal_s1Dal = parseDate(txtVal_s1Dal);
				var DT_txtVal_s1Al = parseDate(txtVal_s1Al);
				
				if(!validateDateToDateFrom(DT_txtVal_s1Dal, DT_txtVal_s1Al)){
					jAlert('Campi data non conformi', '<s:text name="msg.alert.attenzione"/>', function() {
						$.alerts.dialogClass = null;
						//$("#S1_Op1DataAl").focus();
					});
					return false;
				}
				
				
				var totGiorniSC1  = days_between(DT_txtVal_s1Dal,DT_txtVal_s1Al);
				$("#S1_TotGiorni").val(totGiorniSC1);
				if(totGiorniSC1>21){
					$("#S1_TotGiorni").css("background","red");
				}
			} else {
				$("#anagraficaTable6 *").removeAttr("disabled");
			}
		});	
		
		$('#S2_CheckBox').click(function() { 
			if($('#S2_CheckBox').is(':checked')) {
				$("#anagraficaTable6 *").removeAttr("disabled");
				$("#S1_CheckBox").attr('checked', false);
				$("#S1_TotGiorni").removeAttr('style');
				
				var txtVal_s2Dal = $('#S2_Op1DataDal').val();
		        if(!isDate(txtVal_s2Dal)){
		        	jAlert('Campo data non conforme', '<s:text name="msg.alert.attenzione"/>', function() {
						$.alerts.dialogClass = null;
						//$("#S2_Op1DataDal").focus();
					});
					return false;
		        }
		        var txtVal_s2Al = $('#S2_Op1DataAl').val();
		        if(!isDate(txtVal_s2Al)){
		        	jAlert('Campo data non conforme', '<s:text name="msg.alert.attenzione"/>', function() {
						$.alerts.dialogClass = null;
						//$("#S2_Op1DataAl").focus();
					});
					return false;
		        }
		        var DT_txtVal_s2Dal = parseDate(txtVal_s2Dal);
				var DT_txtVal_s2Al = parseDate(txtVal_s2Al);
				
				if(!validateDateToDateFrom(DT_txtVal_s2Dal, DT_txtVal_s2Al)){
					jAlert('Campi data non conformi', '<s:text name="msg.alert.attenzione"/>', function() {
						$.alerts.dialogClass = null;
						//$("#S2_Op1DataAl").focus();
					});
					return false;
				}
				
				
				var totGiorniSC2  = days_between(DT_txtVal_s2Dal,DT_txtVal_s2Al);
				$("#S2_TotGiorni").val(totGiorniSC2);
				if(totGiorniSC2>14){
					$("#S2_TotGiorni").css("background","red");
				}
				
			} else {
				$("#S3_CheckBox").attr('checked', false);
				$("#anagraficaTable6 *").attr("disabled","disabled");
			}	
		});
		
		$('#S3_CheckBox').click(function() { 
			if($('#S3_CheckBox').is(':checked')) {
				if($('#S2_CheckBox').is(':checked')) {
					
					var txtVal_s3Dal = $('#S3_Op1DataDal').val();
			        if(!isDate(txtVal_s3Dal)){
			        	jAlert('Campo data non conforme', '<s:text name="msg.alert.attenzione"/>', function() {
							$.alerts.dialogClass = null;
							//$("#S3_Op1DataDal").focus();
						});
						return false;
			        }
					
			        var txtVal_s3Al = $('#S3_Op1DataAl').val();
			        if(!isDate(txtVal_s3Al)){
			        	jAlert('Campo data non conforme', '<s:text name="msg.alert.attenzione"/>', function() {
							$.alerts.dialogClass = null;
							//$("#S3_Op1DataAl").focus();
						});
						return false;
			        }
			        
			        var DT_txtVal_s3Dal = parseDate(txtVal_s3Dal);
					var DT_txtVal_s3Al = parseDate(txtVal_s3Al);
					
					if(!validateDateToDateFrom(DT_txtVal_s3Dal, DT_txtVal_s3Al)){
						jAlert('Campi data non conformi', '<s:text name="msg.alert.attenzione"/>', function() {
							$.alerts.dialogClass = null;
							//$("#S3_Op1DataAl").focus();
						});
						return false;
					}
					
					var totGiorniSC3  = days_between(DT_txtVal_s3Dal,DT_txtVal_s3Al);
					$("#S3_TotGiorni").val(totGiorniSC3);
					if(totGiorniSC3>7){
						$("#S3_TotGiorni").css("background","red");
					}
				}else{
					jAlert('Attenzione - Selezionare prima una delle opzioni dei turni', '<s:text name="msg.alert.attenzione"/>', function() {
						$.alerts.dialogClass = null;
						//$("#S3_CheckBox").focus();
					});
					return false;
				}
			} else {
				
			}				
		});
		
		
		$('#S1_Op1DataDal').click(function() { 
			$("#S1_CheckBox").attr('checked', false);
			$("#S1_TotGiorni").removeAttr('style');
        	//show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});	
		
		$('#S1_Op1DataAl').click(function() { 
			$("#S1_CheckBox").attr('checked', false);
			$("#S1_TotGiorni").removeAttr('style');
        	//show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});	
		
		$('#S2_Op1DataDal').click(function() { 
			$("#S2_CheckBox").attr('checked', false);
			$("#S2_TotGiorni").removeAttr('style');
        	//show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});	
		
		$('#S2_Op1DataAl').click(function() { 
			$("#S2_CheckBox").attr('checked', false);
			$("#S2_TotGiorni").removeAttr('style');
        	//show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});	
		
		$('#S3_Op1DataDal').click(function() { 
			$("#S3_CheckBox").attr('checked', false);
			$("#S3_TotGiorni").removeAttr('style');
        	//show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});	
		
		$('#S3_Op1DataAl').click(function() { 
			$("#S3_CheckBox").attr('checked', false);
			$("#S3_TotGiorni").removeAttr('style');
        	//show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});	
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		
		$('#S1_Op1DataDal').datepicker({ changeYear: false, changeMonth: true});
		$('#S1_Op1DataAl').datepicker({ changeYear: false, changeMonth: true});
		$('#S2_Op1DataDal').datepicker({ changeYear: false, changeMonth: true});
		$('#S2_Op1DataAl').datepicker({ changeYear: false, changeMonth: true});
		$('#S3_Op1DataDal').datepicker({ changeYear: false, changeMonth: true});
		$('#S3_Op1DataAl').datepicker({ changeYear: false, changeMonth: true});
		
// 		$('#S1_Op1DataDal').datepicker();
// 		$('#S1_Op1DataAl').datepicker();
// 		$('#S2_Op1DataDal').datepicker();
// 		$('#S2_Op1DataAl').datepicker();
// 		$('#S3_Op1DataDal').datepicker();
// 		$('#S3_Op1DataAl').datepicker();
		
		
		
	});
	
	function parseDate(input) {
		  var parts = input.split('/');
		  return new Date(parts[2], parts[1]-1, parts[0]); // Note: months are 0-based
	}
	
	
	function validateDateToDateFrom(dateTo, dateFrom) {
		date1 = new Date(dateTo);
        date2 = new Date(dateFrom);
		// Convert both dates to milliseconds
        var date1_ms = date1.getTime();
        var date2_ms = date2.getTime();
		
        if(date1_ms <=date2_ms)
        	return true;
        else
        	return false;
	}
	
	//@TODO da modificare (non utilizzata)
	function validatePeriodoFerieNonInclusoInAltri(dateTo1, dateFrom1,dateTo2, dateFrom2) {
		date1 = new Date(dateTo1);
        date2 = new Date(dateFrom1);
		// Convert both dates to milliseconds
        var date1_ms = date1.getTime();
        var date2_ms = date2.getTime();
        
        dateConf1 = new Date(dateTo2);
        dateConf2 = new Date(dateFrom2);
		// Convert both dates to milliseconds
        var dateConf1_ms = dateConf1.getTime();
        var dateConf2_ms = dateConf2.getTime();
        
        if(dateConf1_ms>=date1_m && date1_m<=dateConf2_ms){
        	return false;
        }
        if(dateConf1_ms>=date2_ms && date2_ms<=dateConf2_ms){
        	return false;
        }
		return true;
        
	}
	
	
	
	function days_between(dateTo, dateFrom) {

        // The number of milliseconds in one day
        var ONE_DAY = 1000 * 60 * 60 * 24;

        date1 = new Date(dateTo);
        date2 = new Date(dateFrom);

        // Convert both dates to milliseconds
        var date1_ms = date1.getTime();
        var date2_ms = date2.getTime();
        
        // Calculate the difference in milliseconds
        var difference_ms = Math.abs(date1_ms - date2_ms);

        // Convert back to days and return
        return Math.round(difference_ms / ONE_DAY)+1;

    }
	
	function isDate(txtDate){
		var today = new Date();
		var year = today.getFullYear();
		
	    var currVal = txtDate;
	    if(currVal == '')
	        return false;
	    
	    var rxDatePattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/; //Declare Regex
	    var dtArray = currVal.match(rxDatePattern); // is format OK?
	    
	    if (dtArray == null) 
	        return false;
	    
	    //Checks for dd/mm/yyyy format.
	    dtDay= dtArray[1];
	    dtMonth = dtArray[3];
	    dtYear = dtArray[5];        
	    
	    if (dtMonth < 1 || dtMonth > 12) 
	        return false;
	    else if (dtDay < 1 || dtDay> 31) 
	        return false;
	    else if ((dtMonth==4 || dtMonth==6 || dtMonth==9 || dtMonth==11) && dtDay ==31) 
	        return false;
	    else if (dtMonth == 2) 
	    {
	        var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
	        if (dtDay> 29 || (dtDay ==29 && !isleap)) 
	                return false;
	    }else if(dtYear !=year){
	    	 return false;
	    }
	    
	    return true;
	}

	function validateFormConfermaFerie() {
		var radios = document.getElementsByName("campagnaEdicola.flgaperto9227");
		var value = null;
		for (var i = 0; i < radios.length; i++) {
		    if (radios[i].type === 'radio' && radios[i].checked) {
		        value = radios[i].value;       
		    }
		}
		
		if(value!=null && value == 1){
			askForConfirmNoFerie();	
		}else{
			var radioTurno = document.getElementsByName("campagnaEdicola.turno9227");
			var valueTurno = null;
			for (var i = 0; i < radioTurno.length; i++) {
			    if (radioTurno[i].type === 'radio' && radioTurno[i].checked) {
			    	valueTurno = radioTurno[i].value;       
			    }
			}
			if(valueTurno==null){
				jAlert('Attenzione - Compilare tutti i campi obbligatori', '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
				});
				return false;
			}
			
			check1 = document.getElementById("S1_CheckBox").checked;
			check2 = document.getElementById("S2_CheckBox").checked;
			if(check1==false && check2==false ){
				jAlert('Attenzione - Compilare tutti i campi obbligatori', '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
				});
				return false;
			}
			
			var textConfirm ="";
			if(check1==true){
				var S1_Op1DataDal = document.getElementById("S1_Op1DataDal").value;
				var S1_Op1DataAl = document.getElementById("S1_Op1DataAl").value;
				var totGiorniS1 = document.getElementById("S1_TotGiorni").value;
				textConfirm += "<br/> - Dal "+S1_Op1DataDal+" al "+ S1_Op1DataAl+" per un totale di "+totGiorniS1+" giorni";
				
				if(totGiorniS1>21){
					jAlert('Attenzione - Valori inseriti non corretti', '<s:text name="msg.alert.attenzione"/>', function() {
						$.alerts.dialogClass = null;
					});
					return false;
				}
				
			}
			if(check2==true){
				var S2_Op1DataDal = document.getElementById("S2_Op1DataDal").value;
				var S2_Op1DataAl = document.getElementById("S2_Op1DataAl").value;
				var totGiorniS2 = document.getElementById("S2_TotGiorni").value;
				textConfirm += "<br/> - Dal "+S2_Op1DataDal+" al "+ S2_Op1DataAl+" per un totale di "+totGiorniS2+" giorni";
				
				if(totGiorniS2>14){
					jAlert('Attenzione - Valori inseriti non corretti', '<s:text name="msg.alert.attenzione"/>', function() {
						$.alerts.dialogClass = null;
					});
					return false;
				}
			}
			check3 = document.getElementById("S3_CheckBox").checked;
			if(check3==true){
				var S3_Op1DataDal = document.getElementById("S3_Op1DataDal").value;
				var S3_Op1DataAl = document.getElementById("S3_Op1DataAl").value;
				var totGiorniS3 = document.getElementById("S3_TotGiorni").value;
				textConfirm += "<br/> - Dal "+S3_Op1DataDal+" al "+ S3_Op1DataAl+" per un totale di "+totGiorniS3+" giorni";
				
				if(totGiorniS3>7){
					jAlert('Attenzione - Valori inseriti non corretti', '<s:text name="msg.alert.attenzione"/>', function() {
						$.alerts.dialogClass = null;
					});
					return false;
				}
			}
			askForConfirmFerie(textConfirm);
		}
		
		//return true;
	}
	
	function askForConfirmNoFerie() {
		PlaySound('beep3');
		jConfirm('Confermi che il punto vendita non effettuer&agrave; nessun giorno di chiusura?', '<s:text name="msg.alert.attenzione"/>',
			function(r) {
				if (r) {
					submitForm('campagna_saveComunicazioni.action');
				} else {
					unBlockUI();
					return false;
				}
			}
		);
	}
	
	function askForConfirmFerie(textConfirm) {
		PlaySound('beep3');
		jConfirm('Confermi che il punto vendita rester&agrave; chiuso nei giorni indicati?'+textConfirm, '<s:text name="msg.alert.attenzione"/>',
			function(r) {
				if (r) {
					submitForm('campagna_saveComunicazioni.action');
				} else {
					unBlockUI();
					return false;
				}
			}
		);
	}
	
	function submitForm(actionName) {
		ray.ajax();
		document.getElementById("comunicazioniForm").action = actionName;
		document.getElementById("comunicazioniForm").submit();
	}
	
</script>
