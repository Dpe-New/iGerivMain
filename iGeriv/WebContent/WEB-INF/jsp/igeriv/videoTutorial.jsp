<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
div#filter {
	height: 10px;
}

div#footer {
	margin-top: 10px;
}

.box {
	height: 60px; -
	-width: 700px; -
	-background: red;
	font-size: 24px;
	font-style: oblique; -
	-color: #1E90FF;
	text-align: center;
	margin-top: 5px;
	margin-left: 5px;
	display: flex;
	justify-content: center; /* align horizontal */
	align-items: center; /* align vertical */
}
</style>

<s:if
	test="%{#request.listVideoHelp != null && !#request.listVideoHelp.isEmpty()}">
	<div class="box">VIDEO TUTORIAL</div>

	<fieldset class="filterBolla" style="width: 700px;">
		<legend style="font-size: 100%"></legend>
		<s:iterator value="#request.listVideoHelp" id="hv">
			<div class="required">
				<div
					style="float: left; width: 500px; font-size: 14px; color: #1E90FF;">
					<s:property value="#hv.titolo" />
				</div>
				<div style="float: left; width: 200px; text-align: center">
					<a
						href="javascript:openDiv('popup_name',720,480,'/igeriv/helpVideo_execute.action?param=<s:property value="#hv.nomeFile" />','#fff')"><img
						src="/app_img/video.png" width="30px" height="30px" border="0"
						style="border-style: none; text-align: center" /></a>
				</div>
			</div>
		</s:iterator>
	</fieldset>
</s:if>





