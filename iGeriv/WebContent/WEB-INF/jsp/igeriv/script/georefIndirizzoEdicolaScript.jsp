<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style type="text/css">
#map_canvas {
	-moz-border-radius: 15px;
	border-radius: 15px;
	border: 2px solid #9ad0ef;
	padding: 10px;
	width: 100%;
	height: 300px;
}

.center {
	margin: auto;
	width: 70%;
	/*background-color: #b0e0e6;*/
}
</style>

<script type="text/javascript">
    	var key = "<s:text name='#session["keyGoogleMaps"]'/>";
        var pathKey = "https://maps.googleapis.com/maps/api/js?key="+key;
    	document.write('<script type="text/javascript" src="' + pathKey + '"><\/script>');
    </script>



<%--     <script src='http://maps.googleapis.com/maps/api/js?v=3&sensor=false&amp;libraries=places&key=xxx'></script> --%>
<%-- 	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?libraries=places&sensor=false"></script>      --%>


<script type="text/javascript">
      
   
   var ragsocDB 	= '<s:property value="anagraficaEdicola.ragioneSocialeEdicola"/>'; 
   var indirizzoDB 	= '<s:property value="anagraficaEdicola.indirizzoEdicola"/>'; 
   var comuneDB		= '<s:property value="anagraficaEdicola.localitaEdicola"/>'; 
   var indirizzoGeo = indirizzoDB +" , "+comuneDB+", Italy";
   //$("#label_rivendita_ragioneSociale").val(ragsocDB);
   $("#address").val(indirizzoGeo);
   var flagReadOnly		= '<s:property value="anagraficaEdicola.flagReadOnly"/>'; 
   var latDB			= '<s:property value="anagraficaEdicola.latDB"/>';
   var lngDB			= '<s:property value="anagraficaEdicola.lngDB"/>';
   
   
   if(flagReadOnly == "1"){
	   	$("#address").attr("disabled", true);
	   	$("#button_ricerca").attr("disabled", true);
	   	$("#button_conferma").attr("disabled", true);
	   	jAlert('L\'Indirizzo del punto vendita risulta confermato', '<s:text name="msg.alert.attenzione"/>', function() {
			$.alerts.dialogClass = null;
		});
   }
   
   $(document).ready(function() { 
// 	   $('#button_ricerca').click(function() { 
// 		   initialize();
// 		   unBlockUI();   
// 	   });
	   
	   $('#button_conferma').click(function() { 
		   getPosition();
	   });
	   
	   
	   $('#ragSocRivendita').text(ragsocDB);
	   
   });
   
   
   
   
   
   var imageIcon ="https://maps.google.com/mapfiles/kml/shapes/schools_maps.png";
   var imageIconEdicoleVicine ="https://maps.gstatic.com/mapfiles/ms2/micons/green-dot.png";
   var geocoder;
   var map;
   var marker;
   
   google.maps.event.addDomListener(window, 'load', initialize);
   
   function initialize() {
        geocoder = new google.maps.Geocoder();
        var latlng = new google.maps.LatLng(-34.397, 150.644);
        var mapOptions = {
          zoom: 15,
          center: latlng,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        }
        map = new google.maps.Map(document.getElementById('map_canvas'), mapOptions);
        
        if(flagReadOnly != "1"){
        	codeAddress();
        }else{
        	initializePointDB();
        }
        unBlockUI();
   }

   function initializePointDB() {
	   
	   var myLatLng = new google.maps.LatLng(latDB, lngDB);
	   map.setCenter(myLatLng);
	   marker = new google.maps.Marker({
           map: map,
           title: ragsocDB,
           position: myLatLng,
           icon :imageIcon
       });
	   	   
	   var contentString = '<div id="content">'+
       '<div id="siteNotice">'+
       '</div>'+
       '<h3 id="firstHeading" class="firstHeading">Punto Vendita</h3>'+
       '<div id="bodyContent">'+
       '<p><b>'+ragsocDB+'</b></p>'+
       '</div>'+
       '</div>';
       
       var infowindow = new google.maps.InfoWindow({ content: contentString });
       google.maps.event.addListener(marker, 'click', function() {  infowindow.open(map,marker);});
	 }

   
   
   
   function codeAddress() {
        var address = document.getElementById('address').value;
        geocoder.geocode( { 'address': address}, function(results, status) {
          if (status == google.maps.GeocoderStatus.OK) {
	            map.setCenter(results[0].geometry.location);
	            marker = new google.maps.Marker({
	                map: map,
	                title: ragsocDB,
	                position: results[0].geometry.location,
	                icon :imageIcon
	            });
	            var contentString = '<div id="content">'+
	            '<div id="siteNotice">'+
	            '</div>'+
	            '<h3 id="firstHeading" class="firstHeading">Punto Vendita</h3>'+
	            '<div id="bodyContent">'+
	            '<p><b>'+ragsocDB+'</b></p>'+
	            '</div>'+
	            '</div>';
	            
	            var infowindow = new google.maps.InfoWindow({ content: contentString });
	            google.maps.event.addListener(marker, 'click', function() {  infowindow.open(map,marker);});
	            
          } else {
	            jAlert('Geocode non riuscita per il seguente motivo: ' + status, '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
					$("#address").focus();
				});
          }
          
        });
        $("#address").focus();
        
        
      }
   
    function getPosition(){
    	 var address = document.getElementById('address').value;
         geocoder.geocode( { 'address': address}, function(results, status) {
           if (status == google.maps.GeocoderStatus.OK) {
        	   $("#lat").val(results[0].geometry.location.lat());
        	   $("#lng").val(results[0].geometry.location.lng());
        	   $("#indirizzo").val($('#address').val());
        	   askForConfirmPosition();
           } else {
             jAlert('Geocode non riuscita per il seguente motivo: ' + status, '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
					$("#address").focus();
				});
				return false;
           }
         });
    }
    
    
     
    function askForConfirmPosition() {
		PlaySound('beep3');
		jConfirm('Confermi che il punto vendita &egrave; geolocalizzato nel punto indicato nella mappa ?', '<s:text name="msg.alert.attenzione"/>',
			function(r) {
				if (r) {
					$('#address').blur();
					submitForm('georef_saveGeocodingAddress.action');
				} else {
					unBlockUI();
					return false;
				}
			}
		);
	}
    
    function submitForm(actionName) {
		ray.ajax();
		document.getElementById("comunicazioniConfermaIndirizzoGeoref").action = actionName;
		document.getElementById("comunicazioniConfermaIndirizzoGeoref").submit();
	}
   
</script>
