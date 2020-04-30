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
<%--     <script type="text/javascript" src='https://maps.googleapis.com/maps/api/js?key='+key+'' ></script>  --%>

<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css">

<%--<script src='http://maps.googleapis.com/maps/api/js?v=3&sensor=false&amp;libraries=places&key=xxx'></script> --%>
<%--<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?libraries=places&sensor=false"></script> --%>


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
   
   
   
   
   $(document).ready(function() { 
// 	   $('#button_ricerca').click(function() { 
// 		   //unBlockUI();
// 		   initialize();
// 		   //unBlockUI();   
// 	   });
	   
	   $('#ragSocRivendita').text(ragsocDB);
	   
	   $('#button_conferma').click(function() { 
		   askForConfirmGruppoEdicole();
	   });
	   
	   $( "#slider-range-max" ).slider({
			range: "max",
			min: 1,
			max: 20,
			value: 5,
			slide: function( event, ui ) {
				$( "#amount" ).html( ui.value );
				}
			});
			
	   		$( "#amount" ).html( $( "#slider-range-max" ).slider( "value" ) ); 
	   		$( "#km" ).val( $( "#slider-range-max" ).slider( "value" ) ); 	
   	});
   
   
   
   
   
   var imageIcon ="https://maps.google.com/mapfiles/kml/shapes/schools_maps.png";
   var imageIconEdicoleVicine = "https://maps.gstatic.com/mapfiles/ms2/micons/red-dot.png";
   var imageIconEdicoleDelGruppo ="https://maps.gstatic.com/mapfiles/ms2/micons/green-dot.png";
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
        	insertEdicoleVicine();
        	circleKm();
        }
        unBlockUI(); 
    }
   
   var cityCircle;
   var myLatLng;
    
   function initializePointDB() {
	   
	   myLatLng = new google.maps.LatLng(latDB, lngDB);
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
       
       var infowindow = new google.maps.InfoWindow({ 
    	   		content: contentString 
    	   });
       
       google.maps.event.addListener(marker, 'click', function() {  
    	   		infowindow.open(map,marker);
    	   });
	 }

   
   
   
   function circleKm(){
	   var kmInt = $("#km").val();
	   var myCircle = new google.maps.Circle({
    	   center:myLatLng,
    	   radius:kmInt*1000,
    	   strokeColor:"#0000FF",
    	   strokeOpacity:0.2,
    	   strokeWeight:1,
    	   fillColor:"#0000FF",
    	   fillOpacity:0.1
    	   });
       myCircle.setMap(map);
	   
   }
   
   
   
   var markers = [];
   function insertEdicoleVicine() {
	  	var distanzakm = $("#amount").text();
	  	$("#km" ).val(distanzakm); 
	  	
		dojo.xhrGet({
			url: appContext + '/pubblicazioniRpc_getEdicoleVicine.action?distanzakm=' + distanzakm,		
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
			preventCache: true,
			handle: function(data,args) {	
				data = JSON.parse(data);
				var list = '';		
				var mks = [];
				$("#resultSearch").html("Edicole georeferenziate : "+data.length);
				var arrayCrivw = new Array();
						  	
				for (var i = 0; i < data.length; i++) {
	            	
					var iconUse="";
					if(data[i].isInMyGroup== true){
						iconUse =imageIconEdicoleDelGruppo; 
					}else{
						iconUse = imageIconEdicoleVicine;
					}
					
					
	            	var myLatLng = new google.maps.LatLng(data[i].latitudine, data[i].longitudine);
					var markerEdicole = new google.maps.Marker({
				           map: map,
				           title: data[i].ragioneSociale1,
				           position: myLatLng,
				           icon :iconUse
				       });

						markerEdicole.id = data[i].codEdicolaWeb;
				       	
					
						var contentString = '<div id="content">'+
										       '<div id="siteNotice">'+
										       '</div>'+
										       '<h3 id="firstHeading" class="firstHeading">Punto Vendita</h3>'+
										       '<div id="bodyContent">'+
										       '<p><b>'+data[i].ragioneSociale1+'</b></p>'+
										       '<p><input type = "button" value = "Delete" onclick = "DeleteMarker('+ markerEdicole.id + ');" value = "Delete" /></p>'+
										       '</div>'+
										       '</div>';
				    
				      	var infowindow = new google.maps.InfoWindow({ content: contentString });		       
				       	makeInfoWindowEvent(map, infowindow, contentString, markerEdicole); 
				       	mks.push(markerEdicole);
				       	/*markers.push(markerEdicole);*/
				       	/*markers = mks;*/
				       	arrayCrivw[i] = data[i].codEdicolaWeb;
				  
	            }
				markers = mks;
				$("#arrayCrivwGeoref").val(arrayCrivw);
				
			}
	    });
		circleKm();
	}
   
   
   function DeleteMarker(id) {
	   var indiceArray = 0;
	   var arrayCrivw = new Array();
       //Find and remove the marker from the Array
       for (var i = 0; i < markers.length; i++) {
           if (markers[i].id == id) {
               //Remove the marker from Map                  
               markers[i].setMap(null);

               //Remove the marker from array.
               markers.splice(i, 1);
               //return;
           }else{
        	   arrayCrivw[indiceArray] = markers[i].id;
        	   indiceArray++;
           }
       }
       $("#arrayCrivwGeoref").val(arrayCrivw);
   };
   
   
   
   
   function makeInfoWindowEvent(map, infowindow, contentString, marker) {
	   google.maps.event.addListener(marker, 'click', function() {
	     infowindow.setContent(contentString);
	     infowindow.open(map, marker);
	   });
	 }
   
     
    function askForConfirmGruppoEdicole() {
		PlaySound('beep3');
		jConfirm('Confermi che le edicole appartenenti al tuo gruppo edicole sono quelle visualizzate sulla mappa?', '<s:text name="msg.alert.attenzione"/>',
			function(r) {
				if (r) {
					submitForm('georef_saveGruppoEdicoleGeoref.action');
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
