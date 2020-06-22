/*
 * $Id: validation.js 692578 2008-09-05 23:30:16Z davenewton $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

function clearErrorMessages(form) {
	clearErrorMessagesXHTML(form);
}

function clearErrorMessagesXHTML(form) {

	var e = document.getElementsByTagName("span");
	for ( var i = 0; i < e.length; i++) {
		if (e[i].id.indexOf("err_") != -1) {
			document.getElementById(e[i].id).innerHTML = "";
		}
	}

}

function clearErrorLabels(form) {
	clearErrorLabelsXHTML(form);
}

function clearErrorLabelsXHTML(form) {

}

function addError(e, errorText) {
	addErrorXHTML(e, errorText);
}

function addErrorXHTML(e, errorText) {
	try {
		var spans = document.getElementsByTagName("span");
		for ( var i = 0; i < spans.length; i++) {
			if (spans[i].id.indexOf("err_" + e.id) != -1) {
				document.getElementById(spans[i].id).innerHTML = errorText;
				break;
			}
		}
	} catch (e) {
		alert(e);
	}
}
