<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {	
		addFadeLayerEvents();
		//ADD prezzo netto
		//var $tdFornito = $("#InfoPubblicazioniTab_table tbody tr td:nth-child(5)");
		//var $tdResoDichiarato = $("#InfoPubblicazioniTab_table tbody tr td:nth-child(6)");
		//var $tdResoRiscontrato = $("#InfoPubblicazioniTab_table tbody tr td:nth-child(7)");
		//var $tdVendite = <s:if test="%{authUser.visualizzaResoRiscontratoStatistica}">$("#InfoPubblicazioniTab_table tbody tr td:nth-child(11)");</s:if><s:else>$("#InfoPubblicazioniTab_table tbody tr td:nth-child(10)");</s:else>
		
		var $tdFornito = $("#InfoPubblicazioniTab_table tbody tr td:nth-child(6)");
		var $tdResoDichiarato = $("#InfoPubblicazioniTab_table tbody tr td:nth-child(7)");
		var $tdResoRiscontrato = $("#InfoPubblicazioniTab_table tbody tr td:nth-child(8)");
		var $tdVendite = <s:if test="%{authUser.visualizzaResoRiscontratoStatistica}">$("#InfoPubblicazioniTab_table tbody tr td:nth-child(12)");</s:if><s:else>$("#InfoPubblicazioniTab_table tbody tr td:nth-child(11)");</s:else>
		
		
		
		
		<s:if test="%{authUser.visualizzaResoRiscontratoStatistica}">
			$tdResoRiscontrato.click(function() {
				if ($(this).text().trim().length > 0) {
					var href = $(this).parent().attr("divParam");
					var numero = $(this).parent().find("td").first().text();
		            openDettaglio(href, "ResoRiscontrato", numero);
				}
	        });
		
			$tdResoRiscontrato.tooltip({
				delay: 0,  
			    showURL: false,
			    bodyHandler: function() {
			    	if ($(this).text().trim().length > 0) {
			    		return "<b><s:text name='plg.visualizza'/>&nbsp;<s:text name='igeriv.dettaglio.reso.riscontrato'/></b>";
			    	}
			    }
			}); 
		</s:if>
		$tdFornito.click(function() {
			if ($(this).text().trim().length > 0) {
				var href = $(this).parent().attr('divParam');
				var numero = $(this).parent().find("td").first().text();
	            openDettaglio(href, "Fornito", numero);
			}
        });
		$tdFornito.tooltip({
			delay: 0,  
		    showURL: false,
		    bodyHandler: function() {
		    	if ($(this).text().trim().length > 0) {
		    		return "<b><s:text name='plg.visualizza'/>&nbsp;<s:text name='igeriv.dettaglio.fornito'/></b>";
		    	}
		    }
		});
		$tdResoDichiarato.click(function() {
			if ($(this).text().trim().length > 0) {
				var href = $(this).parent().attr("divParam");
				var numero = $(this).parent().find("td").first().text();
	            openDettaglio(href, "Reso", numero);
			}
        });
		$tdResoDichiarato.tooltip({
			delay: 0,  
		    showURL: false,
		    bodyHandler: function() {
		    	if ($(this).text().trim().length > 0) {
		    		return "<b><s:text name='plg.visualizza'/>&nbsp;<s:text name='igeriv.dettaglio.reso'/></b>";
		    	}
		    }
		}); 
		$tdVendite.click(function() {
			if ($(this).text().trim().length > 0) {
				var href = $(this).parent().attr("divParam");
				var numero = $(this).parent().find("td").first().text();
	            openDettaglio(href, "Venduto", numero);
			}
        });
		$tdVendite.tooltip({
			delay: 0,  
		    showURL: false,
		    bodyHandler: function() {
		    	if ($(this).text().trim().length > 0) {
		    		return "<b><s:text name='plg.visualizza'/>&nbsp;<s:text name='igeriv.dettaglio.venduto'/></b>";
		    	}
		    }
		}); 
		thumbnailviewer.init();
		$("#InfoPubblicazioniTab_table").tablesorter();
	});
	
	function openDettaglio(href, tipo, numero) {
		var popID = 'popup_name_det'; 
	 	var popWidth = 450;
	 	var popHeight = 400;
	 	var query= href.split('?');
	    var dim= query[1].split('&');	   	   	    	  		  
	    var idtn = dim[0].split('=')[1];
	    var url = "${pageContext.request.contextPath}/statisticaPubblicazioni_showStatisticaDettaglio" + tipo + ".action?idtn=" + escape(idtn) + "&titolo=" + escape($("#titoloDet").val().toUpperCase()) + "&numero=" + escape(numero);
		openDiv(popID, popWidth, popHeight, url, "", "det", "");
		return false;
	}
</script>