<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:if test="actionName != null && !actionName.contains('rifornimenti')">
	<script>	
		$('#pubMainDiv').keydown(function(evt){ 
		    var evt = (evt) ? evt : ((event) ? event : null); 
	    	if (evt.keyCode == 13)  {		    		
	    		$('#ricerca').trigger('click');	    	
	    		return false;
	    	} 
		}); 							
	</script>
</s:if>
<script>
	var stopWaitingDiv;
	$(document).ready(function() {
		loadFunctions();				
		thumbnailviewer.init();
		if ($("#titolo").length > 0) { 
			$("#titolo").focus();
		}	
		$("#titolo").focus();
	});
	
	function addWaitingDivToButton() {
		$('#ricerca').click(        	       	
	    		function() {
	    		if (!stopWaitingDiv) {				       		 
	    			return (ray1.ajax()); 	
	    		}	    		       		
	    	});
	}
	
	function loadFunctions() {	
		addFadeLayerEvents();
		addWaitingDivToButton();
	}
	
	function submitForm(formid){	
		dojo.xhrPost({
			form: formid,			
			handleAs: "text",		
			handle: function(data,args) {	
				var errMsg = "";		
				var $doc = $(data);
				var formData = $("#PubblicazioniForm", $doc).html();	
				var pos1 = formData.indexOf('<span><span>');
				var pos2 = formData.indexOf('</span></span>');
				if (pos1 != -1 && pos2 != -1) {					
					errMsg = formData.substring(pos1 + 12, pos2).trim();
				}
				if (errMsg != '') {
					dojo.byId('errorDiv').innerHTML = "<font color='red'><span class='errorMessage'>" + errMsg + "</span></font>";
				} else {
					dojo.byId('errorDiv').innerHTML = "<font color='red'><span class='errorMessage'></span></font>"
					var data1 = $("#contentDiv", $doc).html();	
					dojo.byId('contentDiv').innerHTML = data1;	
					$("#PubblicazioniTab_table thead [class=tableHeader]").removeAttr('onclick');
					$("#PubblicazioniTab_table tbody").find("td:not(:last-child)").click(function() {	
					    var popURL = $(this).parent().attr('divParam'); 
					    var query= popURL.split('?');
					    var dim= query[1].split('&');	   	   	    	  		  
					    var cpu = dim[0].split('=')[1];
					    var coddl = dim[2].split('=')[1];
					    var crivw = dim[3].split('=')[1];
					    if (!stopWaitingDiv) {		
					    	$('#errorDiv').text('');
							submitUrl("bollaResa_showBollaResaDimenticata.action?cpu=" + cpu + "&coddl=" + coddl + "&crivw=" + crivw + "&dataTipoBolla=" + escape(document.getElementById('dataTipoBolla').value));
							return false; 	
					    }	    		       		
					    return false;
					});
					addTableLastRow("PubblicazioniTab_table");
				}
				unBlockUI();
				thumbnailviewer.init();
				if (isEdicolaPromo) {
					disableImagesForPromo($('#popup_name'));
				}
				$("#PubblicazioniScrollDiv").removeClass("bollaScrollDiv");
				$("#PubblicazioniScrollDiv").addClass("<s:property value='tableStyle'/>");
				if ($("#titolo").length > 0) {
					$("#titolo").focus();
				}
				setFocusedFieldStyle();
			}		
		});
	}
	
	
	function submitUrl(url){	
		ray.ajax();
		dojo.xhrGet({
			url: url,			
			preventCache: true,
			handleAs: "text",		
			handle: function(data,args) {	
				var $doc = $(data);
				var content = $('igerivException', $doc).length > 0 ? $('igerivException', $doc).html() : '';
				if (content != '') {
					setTimeout(function() {
						unBlockUI();
						$.alerts.dialogClass = "style_1";
						jAlert(content, '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
							$.alerts.dialogClass = null;
							$("#popup_ok").focus();
						});
					}, 200);
				} else {
					var form = document.createElement("FORM");
					form.setAttribute("id","BollaResaFuoriVoceForm");
					form.setAttribute("name","bollaResa_saveBollaResaFuoriVoce");
					form.setAttribute("action","${pageContext.request.contextPath}/bollaResa_saveBollaResaFuoriVoce.action");
					form.setAttribute("method","POST");
					$("#hasBolleQuotidianiPeriodiciDivise1", $doc).val($("#hasBolleQuotidianiPeriodiciDivise").val());
					form.innerHTML = $("#BollaResaFuoriVoceForm", $doc).html();
					dojo.byId('contentDiv').innerHTML = '';
					dojo.byId('contentDiv').appendChild(form);															
					var newScript = document.createElement('script');
					newScript.src = '${pageContext.request.contextPath}/js-transient/bollaResaFuoriVoce-min_<s:text name="igeriv.version.timestamp"/>.js';
					dojo.byId('contentDiv').appendChild(newScript);	
					if ($("#titolo").length > 0) {
						$("#titolo").focus();
					}	
				}
				if (document.getElementById('load')) {
					document.getElementById('load').style.visibility = "hidden";
				}
				if (document.getElementById('load1')) {
					document.getElementById('load1').style.visibility = "hidden";
				}
				thumbnailviewer.init();
				setFocusedFieldStyle();
			}		
		});
	}
	
	function addPubblicazioniCloseButtonToLayer(layerId, imgSrc, closeLabel) {
		var str = '<div id="close" style="z-index:9999"><a href="#" class="btn_close"><img id="imgClose" src="' + imgSrc + '" style="border-style: none" border="0px" class="btn_close" title="' + closeLabel + '" alt="' + closeLabel + '"/></a></div>';													
		$('#' + layerId).prepend(str);			
	}
	
</script>