<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:if test="actionName != null && !actionName.contains('rifornimenti')">
	<script>	
		$('#pubMainDiv').keydown(function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.charCode);
			if (keycode == '13') {			    
				if ($("#submitRicerca").length > 0) {
					$("#PubblicazioniForm").submit();
				} else if ($('#ricerca').length > 0) {
	    			$('#ricerca').trigger('click');
				}
	    	} 
		}); 							
	</script>
</s:if>
<script>
	var msgNoteRivendita = "<s:text name='igeriv.note.rivendita'/>";
	var msgNotePubblicazione = "<s:text name='igeriv.inserisci.note.pubblicazione'/>";
	
	$(document).ready(function() {	
	
		
		<s:if test="actionName != null && actionName.equals('infoPubblicazioni_showPubblicazioni.action')">
		//clearPubblicazioniFilterFields();
		$("table[id*='_table'] tbody").find("td:not(:nth-child(10))").click(function() {
				var popID = 'popup_name';   
			    var popURL = $(this).parent().attr('divParam'); 
			 	var popWidth = 900;
			 	var popHeight = 400;
			    var query= popURL.split('?'); 
			    var dim= query[1].split('&');	   	    	    	  		  
			    var cpu = dim[0].split('=')[1];	  
			    var url='';
			    if($("#idOperazione_ricercaPubblicazione").attr("checked")){
			       url = "${pageContext.request.contextPath}/infoPubblicazioni_showNumeriPubblicazioni.action?flgOperazione=1&codPubblicazione=" + cpu;
			    }else{
			       url = "${pageContext.request.contextPath}/infoPubblicazioni_showNumeriPubblicazioniInserimentoBarcode.action?flgOperazione=0&codPubblicazione=" + cpu;
			    }
			 	
				openDiv(popID, popWidth, popHeight, url);
			}
		); 
		
	    //Modifica del 03/07/2014 Menta
	    //Se la ricerca non dà risultati non effettuare il clear della form
		var rowCount = $("table[id*='_table'] tbody tr").length;
		if(rowCount>0){
			clearPubblicazioniFilterFields();
		}
		
	</s:if>
	
	<s:else>
		$("table[id*='_table'] tbody").find("td:not(:last-child)").click(function() {
				var popID = 'popup_name';   
			    var popURL = $(this).parent().attr('divParam'); 
			 	var popWidth = 900;
			 	var popHeight = 400;
			    var query= popURL.split('?'); 
			    var dim= query[1].split('&');	   	    	    	  		  
			    var cpu = dim[0].split('=')[1];	  
			 	var url = "${pageContext.request.contextPath}/infoPubblicazioni_showNumeriPubblicazioni.action?codPubblicazione=" + cpu;
				openDiv(popID, popWidth, popHeight, url);
			}
		);
	</s:else>
		
		
		
		loadFunctions();
		setContentDivHeight(30); 
		if ($("#titolo").length > 0) {
			$("#titolo").focus();
		}
		
// 		$("input[name=flgOperazione]:radio").change(function () {
// 			clearPubblicazioniFilterFields();
// 			if ($("#idOperazione_ricercaPubblicazione").attr("checked")) {
// 				clearPubblicazioniFilterFields();
// 			}else{
// 				clearPubblicazioniFilterFields();
// 			}
// 		});
		
		
		triggers = $("[automaticallyVisibleIfIdChecked]").map(function(){ return $("#" + $(this).attr("automaticallyVisibleIfIdChecked")).get() })
	    $.unique(triggers);
	    triggers.each( function() {
	        executeAutomaticVisibility(this.name);
	        $(this).change( function(){ executeAutomaticVisibility(this.name); } );
	    });
		
		
	});
	
	function executeAutomaticVisibility(name) {
	    $("[name="+name+"]:checked").each(function() {
	    	$("[automaticallyVisibleIfIdChecked=" + this.id+"]").show();
	    });
	    $("[name="+name+"]:not(:checked)").each(function() {
	    	$("[automaticallyVisibleIfIdChecked=" + this.id+"]").hide();
	    });
	    clearPubblicazioniFilterFields();
	    clearPubblicazioniInsertBarcodeFilterFields();
	    
	}
	
	
	function loadFunctions() {
	}		
			
</script>