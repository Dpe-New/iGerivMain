<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	dojo.require("dojo.rpc.RpcService");
	dojo.require("dojo.rpc.JsonService");	
	var service = new dojo.rpc.JsonService("${smdUrl}");
	
	$(document).ready(function() {	
		
	});
	
	function doAlert(s) {
		$.alerts.dialogClass = "style_1";
		jAlert(s, '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
			$.alerts.dialogClass = null;
		});
	}
	
	function esportaRifornimenti(){		
		var applet = document.getElementById('dlApplet');
		try {
			applet.testConnection();
			var deferred = service.esportaRifornimenti();		
			deferred.addCallback(function(map) {
				if (map.errMsg) {
					$.alerts.dialogClass = "style_1";
					jAlert(map.errMsg, '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
						$.alerts.dialogClass = null;
					});
					var fileName = map.file;
					if (fileName != null && fileName != '') {
						esportaFile(fileName, '10');
					}
				} else if (map.file) { 				
					var fileName = map.file;
					esportaFile(fileName, '10');
				}
			});	
		} catch (e) {
			if (e.toString().indexOf("java.security.AccessControlException") != -1) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="igeriv.errore.connessione.server.gestionale"/>', '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
			}
		}
		unBlockUI();			
	}
	
	function esportaAltriDati(){			
		var applet = document.getElementById('dlApplet');
		try {
			applet.testConnection();
			var deferred = service.esportaAltriDati();		
			deferred.addCallback(function(map) {
				if (map.errMsg) {		
					$.alerts.dialogClass = "style_1";
					jAlert(map.errMsg, '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
						$.alerts.dialogClass = null;
					});
					var fileName = map.file;
					if (fileName != null && fileName != '') {					
						esportaFile(fileName, '20');
					}
				} else if (map.file) {				
					var fileName = map.file;
					esportaFile(fileName, '20');
				}
			});	
		} catch (e) {
			if (e.toString().indexOf("java.security.AccessControlException") != -1) {
				$.alerts.dialogClass = "style_1";
				jAlert("<s:text name="igeriv.errore.connessione.server.gestionale"/>", '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
			}
		}
		unBlockUI();
	}
	
	function esportaTutto(){	
		var deferred = service.esportaTutto();		
		deferred.addCallback(function(map) {
			var applet = document.getElementById('dlApplet');
			if (map.errMsg) {
				$.alerts.dialogClass = "style_1";
				jAlert(map.errMsg, '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
				var fileName = map.file;
				if (fileName != null && fileName != '') {
					esportaFile(fileName, '20');
				}
			} else if (map.file) {
				var fileName = map.file;
				var fileName1 = map.file1;
				esportaFile(fileName, '20');
				esportaFile(fileName1, '20');
			}
			unBlockUI();
		});		
	}
	
	function esportaFile(fileName, tipo) {
		var applet = document.getElementById('dlApplet');		
		try {
			applet.esporta(fileName, tipo);
		} catch(e) {
			if (e.toString().indexOf("java.security.AccessControlException") != -1) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="igeriv.errore.connessione.server.gestionale"/>', '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
			} else if (e.toString().indexOf("it.dpe.igeriv.web.applet.scaricoDatiDl.exception.UnreadableFileOnServerException") != -1) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="igeriv.errore.lettura.file.http.server"/>'.replace('{0}', fileName), '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
			} else {
				$.alerts.dialogClass = "style_1";
				jAlert("<s:text name='igeriv.errore.esecuzione.procedura'/>", '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
			}
			unBlockUI();
		}
	}
	
	function esportazioneFileCompletata(fileName, tipo) {
		var deferred = service.updateDataDownload(fileName, tipo);
		deferred.addCallback(function(ret) {
			if (ret == 'success') {
				jAlert('<s:text name="igeriv.procedura.eseguito.con.suceesso"/>', '<s:text name="msg.avviso"/>', function() {});
			} else {
				$.alerts.dialogClass = "style_1";
				jAlert("<s:text name='igeriv.errore.fine.esecuzione.procedura'/>".replace('{0}', fileName), '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
			}
			unBlockUI();
		});		
	}
</script>