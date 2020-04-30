<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	var oRTE;
	
	$(document).ready(function() {
		addFadeLayerEvents();
		var frameHtml = '<html><body><div id="datacontainer" style="position:absolute;left:1px;top:10px;width:100%">';
		<s:iterator value="%{#request.messaggi}" status="status">
			<s:if test="url != null">
				frameHtml += '<a href=\'javascript: parent.window.parent.window.location="<s:property value="url"/>"\' target="_new" style="text-decoration:none; cursor:pointer" >';
			</s:if>
			frameHtml += '<center><s:property value="titolo" escape="false"/></center><br><br><s:property value="testo" escape="false"/>';
			<s:if test="url != null">
				frameHtml += '</a>';
			</s:if>
			frameHtml += '<hr style="border: 1px solid #000;" width="100%">';
		</s:iterator>
		frameHtml += '</div></body></html>';
		oRTE = document.getElementById('datamain').contentWindow.document;
		oRTE.open();
		oRTE.write(frameHtml);
		oRTE.close();
		initializeScroller();
		$("#datamain").mouseover(function() {
			scrollspeed=0;
		});
		$("#datamain").mouseout(function() {
			scrollspeed=cache;
		});
	});

	/***********************************************
	 * IFRAME Scroller script- © Dynamic Drive DHTML code library (www.dynamicdrive.com)
	 * This notice MUST stay intact for legal use
	 * Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
	 ***********************************************/

	//Specify speed of scroll. Larger=faster (ie: 5)
	var scrollspeed=cache=1;
	 
	//Specify intial delay before scroller starts scrolling (in miliseconds):
	var initialdelay = 5000;
	var dataobj;
	
	function initializeScroller() {
		dataobj = document.all ? document.getElementById('datamain').contentWindow.document.all.datacontainer : document.getElementById('datamain').contentWindow.document.getElementById("datacontainer");
		dataobj.style.top = "5px";
		setTimeout("getdataheight()", initialdelay);
	}

	function getdataheight() {
		thelength = dataobj.offsetHeight;
		if (thelength == 0)
			setTimeout("getdataheight()", 10);
		else
			scrollDiv();
	}

	function scrollDiv() {
		dataobj.style.top = parseInt(dataobj.style.top) - scrollspeed + "px";
		if (parseInt(dataobj.style.top) < thelength * (-1))
			dataobj.style.top = "5px";
		setTimeout("scrollDiv()", 40);
	}
	
	function onCloseLayer() {		
		dataobj = null;
	}
</script>
