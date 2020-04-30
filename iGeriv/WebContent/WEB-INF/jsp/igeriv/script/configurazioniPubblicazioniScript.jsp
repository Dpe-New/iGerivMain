<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script>
	var msgTitle = "<s:text name='msg.immagine.non.disponibile'/>";
	var msgCopieVendute = "<s:text name='label.print.Table.Copies.Sold'/>";
	var msgImgUltCop = "<s:text name='igeriv.immagine.ultima.copertina'/>";
	var msgSposta = "<s:text name='igeriv.sposta.immagine.barra.scelta.rapida'/>";
	var richiestaInviato = "<s:text name='msg.richiesta.inviata'/>";
	var erroreInvioRichiesta = "<s:text name='msg.errore.invio.richiesta'/>";
	
	$(document).ready(function() {
		if ($("#titolo").length > 0) {
			$("#titolo").focus();
		} 
		addFadeLayerEvents();
		thumbnailviewer.init();
			
		$('#pubMainDiv').keydown(function(evt){ 
		    var evt = (evt) ? evt : ((event) ? event : null); 
		    	if (evt.keyCode == 13)  {		    		
		    		$('#ricerca').trigger('click');	    	
		    		return false;
		    	} 
		});
	});
	
	function doTableClick() {
	    var popURL = $(this).parent().attr('divParam'); 
	    var query = popURL.split('?'); 
	    var dim = query[1].split('&');	   	    	    	  		  
	    var cpu = dim[0].split('=')[1];
	    dojo.xhrGet({
			url: '${pageContext.request.contextPath}/pubblicazioniRpc_getPubblicazionePiuVendutaByCpu.action?cpu=' + Number(cpu),			
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=utf-8"}, 	
			preventCache: true,
			handle: function(data,args) {				
				unBlockUI();
				var codicePubblicazione = data.codicePubblicazione;
				var coddl = data.codFiegDl;
				if (codicePubblicazione > 0) {
					var quantita = (data.quantita == null) ? "" : data.quantita;
					var nomeImmagine = data.nomeImmagine;
					var nomeImmagineShort = data.nomeImmagineShort;
					var immagineUltimaCopertina = data.immagineUltimaCopertina;
					var exists = false;
					$("#gallery").find("li").each(function() {
						if (Number($(this).attr("id")) == Number(codicePubblicazione)) {
							exists = true;
						}
					});
					if (!exists) {
						var li = '<li id="' + codicePubblicazione + '" coddl="' + coddl + '" class="ui-widget-content ui-corner-tr"><h5 id="titleSpan" class="ui-widget-header" style="white-space:nowrap;">' + nomeImmagineShort + '</h5><img class="imgMiniatureClass" height="80px" src="/immagini_miniature/' + nomeImmagine + '" type="2" title="' + nomeImmagineShort + '" &nbsp;(' + quantita + '&nbsp;' + msgCopieVendute + '")" border="0"/><a href="/immagini/' + immagineUltimaCopertina + '" rel="thumbnail" title="' + msgImgUltCop + '" class="ui-icon ui-icon-zoomin">' + msgImgUltCop + '</a></li>';
						$("#gallery").append(li);
					}
				}
				$( "ul.gallery > li" ).unbind('click');
				docReady();
				thumbnailviewer.init();
				$("#close").trigger("click");
			}
	    });
	}
	
	function submitForm(formid) {
		dojo.xhrPost({
			form: formid,			
			handleAs: "text",		
			handle: function(data,args) {
				var $doc = null;
				if (getBrowser().indexOf("MSIE") != -1) {
					$doc = document.createElement('div');
					$doc.innerHTML = data;
				} else {
					$doc = $(data);
				}
				var formData = $("#contentDiv", $doc).html();																	
				dojo.byId('contentDiv').innerHTML = formData;	
				unBlockUI();
				thumbnailviewer.init();
				addTableLastRow("PubblicazioniTab_table");
				$("#PubblicazioniScrollDiv").removeClass("bollaScrollDiv");
				$("#PubblicazioniScrollDiv").addClass("bollaScrollDivContent");
				if ($("#titolo").length > 0) {
					$("#titolo").focus();
				}
				setFocusedFieldStyle();
				$("#PubblicazioniTab_table tbody").find("td:not(:last-child)").click(doTableClick);
			}		
		});
	}
</script>