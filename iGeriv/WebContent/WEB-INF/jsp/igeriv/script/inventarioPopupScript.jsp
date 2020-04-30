<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {	
		addFadeLayerEvents();
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#strNewDataInventario').datepicker();
// 		$('#strNewDataInventario').click(function() { 
// 	    	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});
		
		saveInventario = function() {
			var data = $("#strNewDataInventario").val();
			if ($("#strDataInventario option[value^='" + data + "']").length !== 0) {
				$.alerts.dialogClass = "style_1";
				jAlert(msgEsisteGiaInventario, attenzioneMsg.toUpperCase(), function() {
					unBlockUI();
					$.alerts.dialogClass = null;
					$('#inputText').val('');
					$('#inputText').focus();
				});
				return false;
			}
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}${ap}/inventario_saveInventario.action?strDataInventario=" + $("#strNewDataInventario").val(),	
				preventCache: true,
				handleAs: "text",				
				handle: function(data,args) {
					var $doc = $(data);
					var hasError = $("igerivException", $doc).length > 0;
					if (args.xhr.status != 200 || hasError) {
						$.alerts.dialogClass = "style_1";
						jAlert($("igerivException", $doc).text(), attenzioneMsg.toUpperCase(), function() {
							$.alerts.dialogClass = null;
						});
					} else {
						reloadDateIdInventario(data);
						setTimeout(function() {
							$("#totaleInventario").text(displayNum('0.00'));
							$("#rowOutput").html('');
						}, 100);
						$("#close").trigger("click");
					}
					unBlockUI();
				}
			});
		}
		
		//$('#strNewDataInventario').focus();
	});
</script>