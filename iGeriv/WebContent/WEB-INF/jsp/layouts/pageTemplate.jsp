<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<!--[if IE ]>
	<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
	<META HTTP-EQUIV="Expires" CONTENT="-1">
	<![endif]-->
<META charset="ISO-8859-1">
	<script>
		var ray; 
		document.write('<div id="load" style="position:absolute;width:100%;height:150%;margin-left:auto;margin-right:auto;background-repeat: no-repeat;background-position: center;z-index:999999;left:0;background-image: url(/app_img/loading.gif);"></div>');
	</script>
	<title><tiles:insertAttribute name="title" /></title> <tiles:insertAttribute
		name="header" />
	<tiles:insertAttribute name="style" />
</head>
<body onload="javascript: onLoadFunction();">
	<div id="page">
		<div id="header">
			<div id="logo">
				<tiles:insertAttribute name="logo" />
			</div>
			<div id="dl">
				<tiles:insertAttribute name="dl" />
			</div>
			<div id="icons">
				<tiles:insertAttribute name="icons" />
			</div>
			<div id="rightBox">
				<div id="user">
					<tiles:insertAttribute name="user" />
				</div>
				<div id="logout">
					<tiles:insertAttribute name="logout" />
				</div>
			</div>
		</div>
		<div id="menu">
			<div>
				<tiles:insertAttribute name="menu" />
			</div>
		</div>
		<div id="breadCrumb">
			<tiles:insertAttribute name="breadCrumb" />
		</div>
		<div id="filter">
			<div id="help"
				style="position: absolute; cursor: pointer; margin-left: 70px; margin-top: 30px;"></div>
			<tiles:insertAttribute name="filter" />
		</div>
		<div id="content1">
			<tiles:insertAttribute name="content1" />
		</div>
		<div id="content2">
			<tiles:insertAttribute name="content2" />
		</div>
		<div id="popup_name" class="popup_block ui-draggable"></div>
		<div id="popup_name_rifornimenti" class="popup_block ui-draggable"></div>
		<div id="popup_name_ordini" class="popup_block ui-draggable"></div>
		
		<div id="footer">
			<tiles:insertAttribute name="footer" />
		</div>
		<div id="popup_name_det" class="popup_block ui-draggable"></div>
		<script>
			if (typeof afterSuccessSave != 'function') { 
				function afterSuccessSave() {}
			}
			if (typeof onCloseLayer != 'function') {
				function onCloseLayer() {}
			}
		</script>
	</div>
<%-- 	<tiles:insertAttribute name="sounds" /> --%>
	<tiles:insertAttribute name="printMenu" />
<%-- 	<tiles:insertAttribute name="sounds" /> --%>
	<div class="overlay" id="overlay" style="display: none;"></div>
	<!-- Chat -->
	<!--
	    <div id="content"></div>
	-->
	</div>
	<tiles:insertAttribute name="pre-script" />
	<tiles:insertAttribute name="script" />
	<tiles:insertAttribute name="customScript" />
	
	<div>
		<tiles:insertAttribute name="unblockUserScript" />
	</div>
	<tiles:insertAttribute name="sounds" />
</body>
</html>