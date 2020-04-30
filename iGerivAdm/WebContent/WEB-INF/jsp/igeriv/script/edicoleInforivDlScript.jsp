<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<script>
	$(document).ready(function() {
		setContentDivHeight(70);
		
		$("input#edicolaLabel").autocomplete({	
			minLength: 3,			
			source: function(request, response) {	
				var strUrl = "automcomplete_edicoleInforivDl.action";
				dojo.xhrGet({
					url: strUrl,	
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
				$('#codEdicolaWebStr').val(ui.item.id);	
			}
		});	
		
		var $rows = $("#EdicoleTab_table tbody tr");
		$rows.css("cursor","pointer").click(function() {
		    var popURL = $(this).attr('divParam'); 
		 	var query= popURL.split('?');
		    var dim= query[1].split('&');	   	   	    	  		  
		    var codEdicolaWebStr = dim[0].split('=')[1];
		 	var url = "edicoleInforivDl_showEdicolaInforivDl.action?codEdicolaWeb=" + codEdicolaWebStr;	 	
			openDiv('popup_name', 800, 500, url);
			return false;
		});
		
		function showCalendar() { 
        	show_cal(document.getElementById($(this).attr('id')));              			            		    		
		}
		
	});
	
	function insertNew() {
		var url = "edicoleInforivDl_showEdicolaInforivDl.action";	 	
		openDiv('popup_name', 800, 500, url);
	}
	
	function doSubmit() { 
		if ($("#edicolaLabel").val() == '') {
			document.getElementById('codEdicolaWebStr').value = '';
		} else if (!isNaN($("#edicolaLabel").val())) {
			document.getElementById('codEdicolaWebStr').value = $("#edicolaLabel").val();
		}
		return true;
	};
	
</script>