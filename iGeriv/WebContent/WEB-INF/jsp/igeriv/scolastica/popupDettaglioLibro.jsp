<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<s:set name="templateSuffix" value="'ftl'" />
<s:form id="popupDettaglioLibroForm"
	action="gestioneClienti_saveCliente.action" namespace="/" method="POST"
	theme="igeriv" validate="false" onsubmit="return (ray.ajax())">
	<div id="mainDiv" style="width: 100%">
		<fieldset class="filterBolla" style="text-align: left; width: 100%">
			<legend style="font-size: 100%">${tableTitle}</legend>
			<div class="tableFields">

				<table id="dettagliolibrotable" width="810px" cellspacing="3">

					<tr>
						<td><s:text name="dpe.dettaglio.libro.sku" /></td>
						<td><s:textfield label="" name="SKU" id="SKU"
								value="%{#request.dettaglioLibroScolastico.SKU}"
								cssStyle="width: 400px" cssClass="tableFields" maxlength="25"
								disabled="false" /></td>
						<td rowspan="11"><img id="imgCaptcha" src="<s:property value='%{#request.dettaglioLibroScolastico.URL}'/>" /></td>
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.barcode" /></td>
						<td><s:textfield label="" name="BARCODE" id="BARCODE"
								value="%{#request.dettaglioLibroScolastico.BARCODE}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.titolo" /></td>
						<td><s:textfield label="" name="TITOLO" id="TITOLO"
								value="%{#request.dettaglioLibroScolastico.TITOLO}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.autori" /></td>
						<td><s:textfield label="" name="AUTORI" id="AUTORI"
								value="%{#request.dettaglioLibroScolastico.AUTORI}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.editore" /></td>
						<td><s:textfield label="" name="EDITORE" id="EDITORE"
								value="%{#request.dettaglioLibroScolastico.EDITORE}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.prezzo" /></td>
						<td><s:textfield label="" name="PREZZO" id="PREZZO"
								value="%{#request.dettaglioLibroScolastico.PREZZO}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
<!-- 					<tr> -->
<%-- 						<td><s:text name="dpe.dettaglio.libro.aliquota" /></td> --%>
<%-- 						<td><s:textfield label="" name="ALIQUOTA" id="ALIQUOTA" --%>
<%-- 								value="%{#request.dettaglioLibroScolastico.ALIQUOTA}" --%>
<%-- 								cssStyle="width: 400px" cssClass="tableFields" disabled="false" /> --%>
<!-- 					</tr> -->
					<tr>
						<td><s:text name="dpe.dettaglio.libro.disponibile" /></td>
						<td><s:textfield label="" name="DISPONIBILE" id="DISPONIBILE"
								value="%{#request.dettaglioLibroScolastico.DISPONIBILE}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.tomi" /></td>
						<td><s:textfield label="" name="TOMI" id="TOMI"
								value="%{#request.dettaglioLibroScolastico.TOMI}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<!-- 
					<tr>
						<td><s:text name="dpe.dettaglio.libro.volume" /></td>
						<td><s:textfield label="" name="VOLUME" id="VAOLUME"
								value="%{#request.dettaglioLibroScolastico.VOLUME}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>-->
<!-- 					<tr> -->
<%-- 						<td><s:text name="dpe.dettaglio.libro.annopubb" /></td> --%>
<%-- 						<td><s:textfield label="" name="ANNAPUBBL" id="ANNAPUBBL" --%>
<%-- 								value="%{#request.dettaglioLibroScolastico.ANNAPUBBL}" --%>
<%-- 								cssStyle="width: 400px" cssClass="tableFields" disabled="false" /> --%>
<!-- 					</tr> -->
<!-- 					<tr> -->
<%-- 						<td><s:text name="dpe.dettaglio.libro.abstract" /></td> --%>
<%-- 						<td><s:textarea name="ABSTRACT" id="ABSTRACT" --%>
<%-- 								value="%{#request.dettaglioLibroScolastico.ABSTRACT}" --%>
<%-- 								disabled="false" cols="25" rows="5" cssStyle="width:400px" --%>
<%-- 								maxlength="3999" /></td> --%>
<!-- 					</tr> -->

				</table>
			</div>
		</fieldset>
		
		<s:if test="%{#request.seqOrdine_modifica !=null}">
			<input type="hidden" id="seqOrdine" 		 name="seqOrdine"			value="<s:property value='%{#request.seqOrdine_modifica}'/>" /> 
			<input type="hidden" id="sceltaCopertina_js" name="sceltaCopertina_js"	value="<s:property value='%{#request.dettaglioLibro_copertina.flagCopertina}'/>" /> 
			<input type="hidden" id="sceltaLogo_js" 	 name="sceltaLogo_js"		value="<s:property value='%{#request.dettaglioLibro_copertina.idLogoCopertina}'/>" /> 
		</s:if>
		
		<fieldset class="filterBolla" style="text-align: left; width: 100%">
			<legend style="font-size: 100%">AGGIUNGI LA COPERTINA</legend>
			<div class="tableFields">
				<table id="dettagliolibrotable" width="810px" cellspacing="3" >
					<tr>
						<td width="15%"><input type="radio" name="sceltaCopertina" value="true" id="sceltaCopertina"> SI <input type="radio" name="sceltaCopertina" value="false" id="sceltaCopertina" checked="checked"> NO</td>
						<td width="25%">				
							<input type="button" align="middle" onclick="javascript: recuperaInfoCopertinaPrecedente();" style="text-align: center; width: 150px;" class="tableFields" value="INFO PRECEDENTI"  id="recuperaInfo" name="recuperaInfo">
						</td>
						<td colspan="3"><div id="strCostoCopertinatura" style="text-align: center; font-size: 12px; color: orange; padding: 1px 1px 1px 1px;"></div>
						<input type="hidden" id="costoCopertinatura" name="costoCopertinatura" />
						</td>
					</tr>
					<tr>
					
						
						<td><div style="font-size: 10px;">Nome Alunno</div></td>
						<td><input type="text" name="copertina_primariga" id="copertina_primariga" value="<s:property value='%{#request.dettaglioLibro_copertina.primaRigaCopertina}'/>" TABINDEX=1 maxlength=16></td>
						
						
						<td colspan="3" rowspan="3"> 
										<!--  
										<table id="dettagliolibrotable" width="100%" >
											<tr>
												<td colspan="3">
													<input type="radio" name="sceltaLogo" value="103" id="sceltaLogo"><span class="bqy_no"> NESSUNO LOGO</span>
												</td>
											</tr>
											<tr>
												<td>
													<input type="radio" name="sceltaLogo" value="146" id="sceltaLogo">
													<a class="show_img1"  class="btn"><span class="bqy_no">FARFALLE</span></a>
													<div class="hide_img1"><img src="/app_img/scolasticaLoghi/1FARFALLE.jpg" height="100px" width="192px"></div>
												</td>
												<td>
													<input type="radio" name="sceltaLogo" value="147" id="sceltaLogo">
													<a class="show_img2"  class="btn"><span class="bqy_no">CANE</span></a>
													<div class="hide_img2"><img src="/app_img/scolasticaLoghi/2CANE.jpg" height="100px" width="192px"></div>
												</td>
												<td>
													<input type="radio" name="sceltaLogo" value="148" id="sceltaLogo">
													<a class="show_img3"  class="btn"><span class="bqy_no">CHITARRA</span></a>
													<div class="hide_img3"><img src="/app_img/scolasticaLoghi/3CHITARRA.jpg" height="100px" width="192px"></div>
												</td>
										  	</tr>
											<tr>
												<td>
													<input type="radio" name="sceltaLogo" value="149" id="sceltaLogo">
													<a class="show_img4"  class="btn"><span class="bqy_no">MUSIC</span></a>
													<div class="hide_img4"><img src="/app_img/scolasticaLoghi/4MUSIC.jpg" height="100px" width="192px"></div>
												</td>
											
												<td>
													<input type="radio" name="sceltaLogo" value="150" id="sceltaLogo">
													<a class="show_img5"  class="btn"><span class="bqy_no">STELLE</span></a>
													<div class="hide_img5"><img src="/app_img/scolasticaLoghi/5STELLE.jpg" height="100px" width="192px"></div>
												</td>
												<td>
													<input type="radio" name="sceltaLogo" value="151" id="sceltaLogo">
													<a class="show_img6"  class="btn"><span class="bqy_no">POSITIVO</span></a>
													<div class="hide_img6"><img src="/app_img/scolasticaLoghi/6POSITIVO.jpg" height="100px" width="192px"></div>
												</td>
											</tr>
											<tr>
												<td>
													<input type="radio" name="sceltaLogo" value="152" id="sceltaLogo">
													<a class="show_img7"  class="btn"><span class="bqy_no">EASY</span></a>
													<div class="hide_img7"><img src="/app_img/scolasticaLoghi/7EASY.jpg" height="100px" width="192px"></div>
												</td>
											
												<td>
													<input type="radio" name="sceltaLogo" value="153" id="sceltaLogo">
													<a class="show_img8"  class="btn"><span class="bqy_no">BIMBA</span></a>
													<div class="hide_img8"><img src="/app_img/scolasticaLoghi/8BIMBA.jpg" height="100px" width="192px"></div>
												</td>
												<td>
													<input type="radio" name="sceltaLogo" value="154" id="sceltaLogo">
													<a class="show_img9"  class="btn"><span class="bqy_no">PESCI</span></a>
													<div class="hide_img9"><img src="/app_img/scolasticaLoghi/9PESCI.jpg" height="100px" width="192px"></div>
												</td>
											</tr>
											<tr>
												<td>
													<input type="radio" name="sceltaLogo" value="155" id="sceltaLogo">
													<a class="show_img10"  class="btn"><span class="bqy_no">SOLE</span></a>
													<div class="hide_img10"><img src="/app_img/scolasticaLoghi/10SOLE.jpg" height="100px" width="192px"></div>
												</td>
											
												<td>
													<input type="radio" name="sceltaLogo" value="156" id="sceltaLogo">
													<a class="show_img11"  class="btn"><span class="bqy_no">TESCHI</span></a>
													<div class="hide_img11"><img src="/app_img/scolasticaLoghi/11TESCHIO.jpg" height="100px" width="192px"></div>
												</td>
												<td>
													<input type="radio" name="sceltaLogo" value="157" id="sceltaLogo">
													<a class="show_img12"  class="btn"><span class="bqy_no">CALCIO</span></a>
													<div class="hide_img12"><img src="/app_img/scolasticaLoghi/12CALCIO.jpg" height="100px" width="192px"></div>
												</td>
											</tr>
										</table>
										-->
						</td>
					</tr>
					
					<tr>
						<td><div style="font-size: 10px;">Cognome Alunno</div></td>
						<td><input type="text" name="copertina_secondariga" id="copertina_secondariga"  value="<s:property value='%{#request.dettaglioLibro_copertina.secondaRigaCopertina}'/>" TABINDEX=2 maxlength=16></td>
					</tr>
					<tr>
						<td><div style="font-size: 10px;">Classe</div></td>
						<td><input type="text" name="copertina_terzariga" id="copertina_terzariga" value="<s:property value='%{#request.dettaglioLibro_copertina.terzaRigaCopertina}'/>" TABINDEX=3 maxlength=16></td> 
					</tr>
				</table>
			</div>
		</fieldset>
		
		
		
		<div style="width: 100%; margin-top: 0px;">
			<div style="float: left; width: 400px; margin-left: 320px">
				<div style="float: left; width: 100px; margin-top: 0px;">
					<input type="button" name="igeriv.aggiungi.carrello"
						id="aggiungicarrello"
						value="<s:text name='igeriv.aggiungi.carrello'/>" align="center"
						class="tableFields" style="text-align: center; width: 200px"
						onclick="javascript: validateForm()" />
				</div>
			</div>
		</div>
	</div>
	<s:hidden name="idNumeroOrdine" id="idNumeroOrdine" value="%{#request.numeroOrdine}" />
	<s:hidden name="guid" id="guid" value="%{#request.ricParams.guid}" />
	<s:hidden name="sku" id="sku" value="%{#request.ricParams.sku}" />

</s:form>
<div id="one">
	</td>