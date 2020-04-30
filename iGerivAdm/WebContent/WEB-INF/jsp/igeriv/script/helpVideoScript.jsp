<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/flowplayer-3.2.8.min.js"></script>
<script>
	$(document).ready(function() {		
		addFadeLayerEvents();
		// http://${pageContext.request.serverName}:${pageContext.request.serverPort}/video/flowplayer-3.2.8.swf
		$f("player", "http://releases.flowplayer.org/swf/flowplayer-3.2.8.swf",  {
 
		    clip: {
		        url: 'http://${pageContext.request.serverName}:${pageContext.request.serverPort}/video/<s:text name="param"/>',
		        autoPlay: true,
		        autoBuffering: true
		    },
		 
		    plugins:  {
		 
		        // default controls with the same background color as the page background
		        controls:  {
		            backgroundColor: '#336699',
		            backgroundGradient: 'none',
		            all:false,
		            scrubber:true,
		            mute:false,
		            volume: false,
		            play: true,
		            height:30,
		            progressColor: '#ffffff',
		            bufferColor: '#333333',
		            autoHide: false,
		            fullscreen: true,
		            tooltips: {
				    	buttons: true,
				    	pause: "<s:text name='igeriv.video.pausa'/>",
				    	play: "<s:text name='igeriv.video.continua'/>",
				        fullscreen: "<s:text name='igeriv.video.modalita.schermo.intero'/>"
				    }			            
		        },
		 
		        // time display positioned into upper right corner
		        time: {
		            url: "http://releases.flowplayer.org/swf/flowplayer.controls-3.2.8.swf",
		            //url: "http://${pageContext.request.serverName}:${pageContext.request.serverPort}/video/flowplayer.controls-3.2.8.swf",
		            top:0,
		            backgroundGradient: 'none',
		            backgroundColor: 'transparent',
		            buttonColor: '#254558',
		            all: false,
		            time: true,
		            height:40,
		            right:30,
		            width:100,
		            autoHide: false
		        }
		    },
		 
		    // canvas coloring and custom gradient setting
		    canvas: {
		        backgroundColor:'#254558',
		        backgroundGradient: [0.1, 0]
		    }
		 
		});						
	});
</script>