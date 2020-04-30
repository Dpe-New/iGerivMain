<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 80px;
}

div#footer {
	margin-top: 10px;
}
</style>
<s:form id="filterForm" action="bollaRivenditaSpunta_showBolla.action"
	method="POST" theme="simple" validate="false"
	onsubmit="return (ray.ajax())">
	<div style="text-align: center">
		<fieldset class="filterBolla">
			<legend style="font-size: 100%;">
				<s:property value="filterTitle" />
			</legend>
			<div class="required" style="float: left">
				<div style="float: left; text-align: left; width: 150px">
					<s:text name="igeriv.data.tipo.bolla" />
				</div>
				<div style="float: left; width: 300px">
					<select name="dataTipoBolla" id="dataTipoBolla"
						onselect="javascript:selectBollaRiassunto(this);"
						style="width: 300px">
						<option value=""></option>
						<s:iterator value="%{#request['listDateTipiBolla']}"
							status="status">
							<s:if test="%{bollaTrasmessaDl >= 2}">
								<option style="color: red"
									value="<s:property value='dataBollaFormat'/>|<s:property value='tipoBolla'/>|<s:property value='readonly'/>|<s:property value='gruppoSconto'/>"><s:property
										value='dataBollaFormat' />&nbsp;&nbsp;
									<s:property value='tipoBolla' />&nbsp;(
									<s:text name="igeriv.trasmessa" />)
								</option>
							</s:if>
							<s:elseif test="%{bollaTrasmessaDl == 1}">
								<option style="color: blue"
									value="<s:property value='dataBollaFormat'/>|<s:property value='tipoBolla'/>|<s:property value='readonly'/>|<s:property value='gruppoSconto'/>"><s:property
										value='dataBollaFormat' />&nbsp;&nbsp;
									<s:property value='tipoBolla' />&nbsp;(
									<s:text name="igeriv.in.trasmissione" />)
								</option>
							</s:elseif>
							<s:elseif test="%{bollaTrasmessaDl == 0}">
								<option
									value="<s:property value='dataBollaFormat'/>|<s:property value='tipoBolla'/>|<s:property value='readonly'/>|<s:property value='gruppoSconto'/>"><s:property
										value='dataBollaFormat' />&nbsp;&nbsp;
									<s:property value='tipoBolla' />&nbsp;(
									<s:text name="igeriv.non.trasmessa" />)
								</option>
							</s:elseif>
							<s:else>
								<option
									value="<s:property value='dataBollaFormat'/>|<s:property value='tipoBolla'/>|<s:property value='readonly'/>|<s:property value='gruppoSconto'/>"><s:property
										value='dataBollaFormat' />&nbsp;&nbsp;
									<s:property value='tipoBolla' /></option>
							</s:else>
						</s:iterator>
					</select>
				</div>
				<div style="float: left; text-align: left; width: 150px">&nbsp;</div>
			</div>
		</fieldset>
	</div>
</s:form>
