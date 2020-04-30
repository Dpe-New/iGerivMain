<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#content1 {
	height: 530px;
}
</style>
<s:if
	test="%{#request.lavorazioneResaVo != null && #request.listLavorazioneResaImmagineVo != null}">
	<div style="width: 100%;">
		<div
			style="height: 500px; width: 520px; overflow: auto; margin-left: auto; margin-right: auto">
			<s:iterator value="%{#request.listLavorazioneResaImmagineVo}"
				status="status">
				<div
					style="width: 500px; height: 85px; text-align: center; margin-top: 5px; border-bottom: 1px solid black">
					<div class="required"
						style="float: left; width: 350px; text-align: left">
						<div
							style="float: none; width: 400px; heigth: 10px; text-align: left">
							<s:text name="igeriv.ordine.lavorazione.resa" />
							&nbsp;:&nbsp;
							<s:property value='#status.index + 1' />
						</div>
						<div
							style="float: none; width: 400px; heigth: 10px; text-align: left">
							<s:text name="igeriv.titolo" />
							&nbsp;:&nbsp;
							<s:property value='titolo' />
						</div>
						<div
							style="float: none; width: 400px; heigth: 10px; text-align: left">
							<s:text name="igeriv.report.numero" />
							&nbsp;:&nbsp;
							<s:property value='numeroCopertina' />
						</div>
						<div
							style="float: none; width: 400px; heigth: 10px; text-align: left">
							<s:text name="igeriv.data.lavorazione.resa" />
							&nbsp;:&nbsp;
							<s:property value='dataOraLavorazioneFormat' />
						</div>
						<div
							style="float: none; width: 400px; heigth: 10px; text-align: left">
							<s:text name="igeriv.copie.lavorazione.resa" />
							&nbsp;:&nbsp;
							<s:property value='copie' />
						</div>
					</div>
					<div style="float: right; width: 150px; text-align: right">
						<a
							href="/immagini_resa/<s:property value='dataBollaFolder'/>_<s:property value='tipoBolla'/>/<s:property value='nomeImmagine'/>"
							rel="thumbnail"> <img
							src="/immagini_resa/<s:property value='dataBollaFolder'/>_<s:property value='tipoBolla'/>/resized/<s:property value='nomeImmagine'/>"
							width="75px" height="75px" border="0" style="border-style: none" />
						</a>
					</div>
				</div>
			</s:iterator>
		</div>
	</div>
</s:if>
<s:else>
	<div class="tableFields"
		style="width: 100%; align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>
