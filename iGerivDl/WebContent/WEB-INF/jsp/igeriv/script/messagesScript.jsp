<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script> 
	function onLoadFunction() {
						
	}
	
	function doSubmit() { 
		if ($("#edicolaLabel").val() == '') {
			document.getElementById('strCodEdicolaDl').value = '';
		} else if (!isNaN($("#edicolaLabel").val())) {
			document.getElementById('strCodEdicolaDl').value = $("#edicolaLabel").val();
		}
		return true;
	};
	
	$("#butNuovo").click(function() {
			var popID = 'popup_name'; //Get Popup Name	   	     		    	  
		    var popWidth = 650;
		    var popHeight = 480;  
		 	var url = "${pageContext.request.contextPath}${ap}/messages_newMessage.action";
			openDiv(popID, popWidth, popHeight, url);
		}
	);
	
	$.datepicker.setDefaults($.datepicker.regional['it']);
	$('#strDataMessaggioDa').datepicker();
	$('#strDataMessaggioA').datepicker();
	
	
// 	$('#strDataMessaggioDa').click(function() { 
//         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 	});	
// 	$('#strDataMessaggioA').click(function() { 
//         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 	});	
	
	$("input#edicolaLabel").autocomplete({	
		minLength: 3,			
		source: function(request, response) {		
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}${ap}/automcomplete_edicole.action",
				preventCache: true,
				handleAs: "json",				
				headers: { "Content-Type": "application/json; charset=uft-8"}, 	
				content: { 
			    	term: request.term
			    }, 					
				handle: function(data,args) {
					response($.map(data, function(item) {											
		                                return {
		                                    id: item.id,	
		                                    label: item.label,
		                                    value: item.label		                                   
		                                }
		                            }))
				}
			});
		},
		select: function (event, ui) {						
			document.getElementById('strCodEdicolaDl').value = ui.item.id;		
		}
	});	
</script>
<s:if test="%{#request.messaggi != null && !#request.messaggi.isEmpty()}">
<script>
	$(document).ready(function() {		 		
		addFadeLayerEvents(); 
		setContentDivHeight(10);
	});
	
	$("#MessaggiTab_table tbody td:not(:nth-child(6)):not(:nth-child(7)):not(:nth-child(8))").click(function() { 
	    var popURL = $(this).parent().attr('divparam'); 
	    var query= popURL.split('?');
	    var dim= query[1].split('&');	   	   	    	  		  
	    var pk = dim[0].split('=')[1];
		$('#messagePk').val(pk);
		$("#MessaggiForm").append('<input type="hidden" name="messagePk" id="messagePk" value="' + pk + '"/>');
		$('#MessaggiForm').attr('action', '${pageContext.request.contextPath}${ap}/messages_showMessage.action');
	  	$('#MessaggiForm').submit();
	  	return (ray.ajax()); 
	});
	
	$('#MessaggiTab_table tbody tr').each(function() {				
		var child1 = $(this).find('td:nth-child(6)');	
		var str = '<span style="width:20px;">';								
		if (typeof($(this).attr('an')) !== 'undefined' && $(this).attr('an') != '') {				
			str += '<a href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=' + encodeURIComponent($(this).attr('an')) + '" target="new"><img src="/app_img/attachment_small.png" border="0px" width="30px" height="30px" style="border-style: none" title="<s:text name="igeriv.allegato"/>: '+$(this).attr('an')+'" alt="'+$(this).attr('an')+'" /></a>';	
		}   
		str += '</span>';			
		child1.html(str);	
		
		var child2 = $(this).find('td:nth-child(7)');	
		var str = '<span style="width:20px;">';								
		if (typeof($(this).attr('an1')) !== 'undefined' && $(this).attr('an1') != '') {				
			str += '<a href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=' + encodeURIComponent($(this).attr('an1')) + '" target="new"><img src="/app_img/attachment_small.png" border="0px" width="30px" height="30px" style="border-style: none" title="<s:text name="igeriv.allegato"/>: '+$(this).attr('an1')+'" alt="'+$(this).attr('an1')+'" /></a>';	
		}   
		str += '</span>';			
		child2.html(str);
		
		var child3 = $(this).find('td:nth-child(8)');	
		var str = '<span style="width:20px;">';								
		if (typeof($(this).attr('an2')) !== 'undefined' && $(this).attr('an2') != '') {				
			str += '<a href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=' + encodeURIComponent($(this).attr('an2')) + '" target="new"><img src="/app_img/attachment_small.png" border="0px" width="30px" height="30px" style="border-style: none" title="<s:text name="igeriv.allegato"/>: '+$(this).attr('an2')+'" alt="'+$(this).attr('an2')+'" /></a>';	
		}   
		str += '</span>';			
		child3.html(str);				
	});	
	
</script>
</s:if>


