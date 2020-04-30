<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	var confirmInserimentoNuovaRichiestaStessoCpu = '<s:text name="msg.confirm.inserimento.nuovo.ordine.stesso.cpu"/>';
	$(document).ready(function() {	
		addFadeLayerEvents();
		setFirstRowColor('<s:text name="%{#request.giacenzaPressoDl}"/>');
		$("#disponibilita").tooltip({
				delay: 0,  
			    showURL: false,			    
			    bodyHandler: function() {
			    	var str = '<div><s:text name="igeriv.leggenda"/>&nbsp;:</div>';
			    	str += '<div style="margin-top:0px; margin-left:auto; margin-right:auto; text-align:center; width: 1.0em; height: 1.3em; -webkit-border-radius: 1em; -moz-border-radius: 1em; background:#009933"/><b><s:text name="igeriv.pubblicazione.disponibile"/></b>';
			    	str += '<br><div style="margin-top:0px; margin-left:auto; margin-right:auto; text-align:center; width: 1.0em; height: 1.3em; -webkit-border-radius: 1em; -moz-border-radius: 1em; background:#ff0000"/><b><s:text name="igeriv.pubblicazione.non.disponibile"/></b>';
			    	return str;
			    }
		});
		$('input:text[name=quantitaRifornimento]:disabled').each(function (){
		    var $self = $(this), $parent = $self.closest("td"), $overlay = $("<div />");
		    var $row = $self.closest("tr");
		    $overlay.css({
		        position: "absolute"
		      , top: $parent.position().top
		      , left: $parent.position().left
		      , width: $parent.outerWidth()
		      , height: $parent.outerHeight()
		      , zIndex: 10000
		      , backgroundColor: "#fff"
		      , opacity: 0
		    }).click(function (){
		    	var $nextRow = $row.next();
		    	var isPresent = false;
		    	if ($nextRow != null && $nextRow.length > 0) {
		    		var curIdtn = $row.find("input:hidden[name=pk]").first().val().split("|")[3];
		    		var nextIdtn = $nextRow.find("input:hidden[name=pk]").first().val().split("|")[3];
		    		isPresent = (nextIdtn == curIdtn);
		    	}
		    	if (!isPresent) {
		    		PlaySound('beep3');
			    	jConfirm(confirmInserimentoNuovaRichiestaStessoCpu, attenzioneMsg, function(r) {
			    	    if (r) { 
			    	    	var $newRow = $row.clone();
			    	    	$newPk = $newRow.find("input:hidden[name=pk]").first();
			    	    	$newQta = $newRow.find("input:text[name=quantitaRifornimento]").first();
			    	    	$newDtaScadenza = $newRow.find("input:text[name=dataScadenzaRichiesta]").first();
			    	    	$newNote = $newRow.find("input:text[name=noteVendita]").first();
			    	    	$newQta.removeAttr('disabled');
			    	    	$newNote.removeAttr('disabled');
			    	    	$newPk.removeAttr('disabled');
			    	    	var newId = $newQta.attr("id");
			    	    	var newIdDate = newId.split("|")[2];
			    	    	var d = new Date();
			    	    	var year = d.getFullYear();
			    			var month = pad2(d.getMonth() + 1);
			    			var day = pad2(d.getDate());
			    	    	var hours = pad2(d.getHours());
			    			var minutes = pad2(d.getMinutes());
			    			var seconds = pad2(d.getSeconds());
			    			var newDate = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds + ".0";
			    			$newPk.attr("name", "pkAggiunte");
			    			$newQta.attr("name", "quantitaRifornimentoAggiunte");
			    			$newDtaScadenza.attr("name", "dataScadenzaRichiestaAggiunte");
			    			$newNote.attr("name", "noteVenditaAggiunte");
			    			$newQta.attr("id", newId.replace(newIdDate, newDate));
			    			$newPk.val($newPk.val().replace(newIdDate, newDate));
			    	    	$newQta.val("");
			    	    	$newNote.val("");
			    	    	$newRow.insertAfter($row);
			    	    	setFocusedFieldStyle();
			    	    	$newQta.focus();
			    	    	return false;
			    	    }
		    		});
		    	}
		    });
		    $parent.append($overlay);
		});
		$('input:text[name=quantitaRifornimento]').each(function (){
			var $self = $(this);
			$self.numeric(false);
			if ($self.attr("disabled") == true) {
				$self.closest("tr").find("input:hidden[name=pk]").first().attr("disabled", true);				
			}
		});
		$('input:text[name=quantitaRifornimento]').not(":disabled").first().focus();
	});
	
	function pad2(number) { 
	    return (number < 10 ? '0' : '') + number
	}
	
	$('tr td:nth-child(5)').click(function() { 
	    	return false;		
	});		
	
	function afterSuccessSave() {
		selectRowByBarcode($("#pkSel").val(), 'differenze');
		$("#close").trigger('click');
	}
	 
	function setFirstRowColor(giacenzaDl) {
		var row = $("#RichiestaRifornimentoTab_table tbody tr td:nth-child(10)").first();
		var divCircle = '';
		if (giacenzaDl > 0) {
			divCircle = '<div id="disponibilita" title="Disponibile" style="margin-top:0px; margin-left:auto; margin-right:auto; text-align:center; width: 1.0em; height: 1.3em; -webkit-border-radius: 1em; -moz-border-radius: 1em; background:#009933"/>';
		} else if (giacenzaDl < 0) {
			divCircle = '<div id="disponibilita" title="Non Disponibile" style="margin-top:0px; margin-left:auto; margin-right:auto; text-align:center; width: 1.0em; height: 1.3em; -webkit-border-radius: 1em; -moz-border-radius: 1em; background:#ff0000"/>';
		}
		row.html(divCircle);
	}
</script>