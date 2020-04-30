<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 20px;
	height: 80px
}

div#content1 {
	height: 470px;
}

ul.actionMessage {
	list-style: none;
}
</style>
<div style="width: 100%">
	<div
		style="margin-top: 5px; width: 100%; height: 30px; text-align: center;"
		class="textTitle">
		<s:text name="plg.prodotti.non.editoriali.edicola" />
	</div>
	<div style="width: 100%; margin-top: 30px">
		<div
			style="float: left; width: 60%; height: 30px; text-align: center; cursor: pointer"
			class="textTitle" id="myProducts">
			<s:text name="plg.miei.prodotti" />
		</div>
		<div
			style="float: right; width: 40%; height: 30px; text-align: left; cursor: pointer"
			class="textTitle" id="availableProducts">
			<s:text name="plg.prodotti.disponibili" />
		</div>
	</div>
</div>