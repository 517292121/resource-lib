


function checkAll(formName, cbName, cbAllName) {
	var cbx = document.forms[formName].elements[cbName];
	var chk = document.forms[formName].elements[cbAllName];
	for(var i = 0; i < cbx.length; i++) {
		cbx[i].checked = chk.checked;
	}
}
