<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:set name="currListDataTipoBolla"
	value="%{#request['listDateTipiBolla']}" />
<s:set name="an" value="" />
<s:if test="actionName != null && actionName.contains('viewBollaResa_')">
	<s:set name="an" value="%{'viewBollaResa_showBollaResa.action'}" />
</s:if>
<s:else>
	<s:set name="an" value="%{actionName}" />
</s:else>
<s:set name="showResoOpts"
	value="#an != '' && #an.contains('bollaResa') && !#an.contains('viewBollaResa') && !#an.contains('showFilterLavorazioneResa') && !#an.contains('showQuadraturaResa')" />
<s:form id="filterForm" action="%{an}" method="POST" theme="simple"
	validate="false" onsubmit="return (ray.ajax())">
	<div style="text-align: center">
		<fieldset class="filterBolla">
			<legend style="font-size: 100%;">
				<s:property value="filterTitle" />
			</legend>
			<div class="required" style="float: left">
				<div style="float: left; text-align: left; width: 150px">
					<s:if test="#an != '' && !#an.contains('Resa')">
						<s:text name="igeriv.data.tipo.bolla" />
					</s:if>
					<s:else>
						<s:text name="igeriv.data.tipo.bolla.resa" />
					</s:else>
				</div>
				<div style="float: left; width: 300px">
					<select name="dataTipoBolla" id="dataTipoBolla"
						onselect="javascript:selectBollaRiassunto(this);"
						style="width: 300px">
						<s:iterator value="#currListDataTipoBolla" status="status">
							<s:if test="%{bollaTrasmessaDl >= 2}">
								<option style="color: red"
									value="<s:property value='dataBollaFormat'/>|<s:property value='tipoBolla'/>|<s:property value='readonly'/>|<s:property value='gruppoSconto'/>"
									drd="<s:property value='dataRegistrazioneDocumento'/>"
									ord="<s:property value='oraRegistrazioneDocumento'/>"><s:property
										value='dataBollaFormat' />&nbsp;&nbsp;
									<s:property value='tipoBolla' />&nbsp;(
									<s:text name="igeriv.trasmessa" />)
								</option>
							</s:if>
							<s:elseif test="%{bollaTrasmessaDl == 1}">
								<option style="color: #FF6633"
									value="<s:property value='dataBollaFormat'/>|<s:property value='tipoBolla'/>|<s:property value='readonly'/>|<s:property value='gruppoSconto'/>"
									drd="<s:property value='dataRegistrazioneDocumento'/>"
									ord="<s:property value='oraRegistrazioneDocumento'/>"><s:property
										value='dataBollaFormat' />&nbsp;&nbsp;
									<s:property value='tipoBolla' />&nbsp;(
									<s:text name="igeriv.in.trasmissione" />)
								</option>
							</s:elseif>
							<s:elseif test="%{bollaTrasmessaDl == 0}">
								<option
									value="<s:property value='dataBollaFormat'/>|<s:property value='tipoBolla'/>|<s:property value='readonly'/>|<s:property value='gruppoSconto'/>"
									drd="<s:property value='dataRegistrazioneDocumento'/>"
									ord="<s:property value='oraRegistrazioneDocumento'/>"><s:property
										value='dataBollaFormat' />&nbsp;&nbsp;
									<s:property value='tipoBolla' />&nbsp;(
									<s:text name="igeriv.non.trasmessa" />)
								</option>
							</s:elseif>
							<s:else>
								<option
									value="<s:property value='dataBollaFormat'/>|<s:property value='tipoBolla'/>|<s:property value='readonly'/>|<s:property value='gruppoSconto'/>"
									drd="<s:property value='dataRegistrazioneDocumento'/>"
									ord="<s:property value='oraRegistrazioneDocumento'/>"><s:property
										value='dataBollaFormat' />&nbsp;&nbsp;
									<s:property value='tipoBolla' /></option>
							</s:else>
						</s:iterator>
					</select>
				</div>
			</div>
			<s:if test="#an != '' && #an.contains('bollaRivendita')">
				<div class="required" style="float: left;">
					<div style="float: left; text-align: left; width: 370px">
						<s:text name="igeriv.view.solo.righe.spuntare" />
					</div>
					<div
						style="float:left; width:<s:if test="%{#request.sf eq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS}">140px</s:if><s:else>80px</s:else>">
						<s:select name="soloRigheSpuntare" id="soloRigheSpuntare"
							listKey="key" listValue="value"
							list="%{#request['listSoloRighaDaSpuntare']}"
							cssStyle="width:80px" />
					</div>
				</div>
			</s:if>
			<s:elseif test="%{showResoOpts}">
				<div class="required" style="float: left; padding: 0px 0px;">
					<div
						style="float: left; text-align: left; width: 240px; padding: 0px 0px;">
						<s:text name="igeriv.auto.incrementa.reso" />
					</div>
					<div style="float: left; width: 200px; padding: 0px 0px;">
						<s:checkbox name="autoincrement" id="autoincrement"
							cssClass="tableFields" />
					</div>
				</div>
				<div class="required" style="float: left; padding: 0px 0px;">
					<div
						style="float: left; text-align: left; width: 240px; padding: 0px 0px;">
						<s:text name="igeriv.solo.reso.da.inserire" />
					</div>
					<div style="float: left; width: 200px; padding: 0px 0px;">
						<s:checkbox name="soloResoDaInserire" id="soloResoDaInserire"
							cssClass="tableFields" value="soloResoDaInserire" />
					</div>
				</div>
				<div class="required" style="float: left; padding: 0px 0px;">
					<div
						style="float: left; text-align: left; width: 240px; padding: 0px 0px;">
						<s:text name="igeriv.solo.reso.con.giacenza" />
					</div>
					<div style="float: left; width: 200px; padding: 0px 0px;">
						<s:checkbox name="soloResoConGiacenza" id="soloResoConGiacenza"
							cssClass="tableFields" value="soloResoConGiacenza" />
					</div>
				</div>
			</s:elseif>
			<div
				style="width: 100%; text-align: center; float: left; padding: 0px 0px;">
				<div>
					<s:submit name="submitFilter" id="submitFilter"
						key="dpe.contact.form.submit" align="center"
						cssStyle="align:center; width:80px;" />
				</div>
			</div>
		</fieldset>
	</div>
</s:form>
<s:if test="%{message != null}">
	<fieldset style="width: 60%">
		<table width="100%" cellpadding="5" class="tableFields">
			<tr>
				<td align="center"><FONT color=red><s:text
							name="%{#request.message}" /></FONT></td>
			</tr>
		</table>
	</fieldset>
</s:if>
<s:if test="%{saved != null}">
	<fieldset style="width: 60%">
		<table width="100%" cellpadding="5" class="tableFields">
			<tr>
				<td align="center"><FONT color=red><s:text
							name="gp.dati.memorizzati" /></FONT></td>
			</tr>
		</table>
	</fieldset>
</s:if>
