let user = "";
let perms = [];
const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

function init(kick = false) {
    user = readCookie("user");
    let usrDrop = document.getElementById("userDropList")
    if(isLoggedIn()) {
        // let loginLink = document.getElementById("loginLink")
        // loginLink.innerHTML = "Logout"
        // loginLink.href = "javascript:logout()"
        document.getElementById("userName").innerHTML = readCookie("user");
        usrDrop.innerHTML = "<a href='/user/profile'>My Profile</a>";
        usrDrop.innerHTML += "<a href='/plot'>My Plots</a>";
        usrDrop.innerHTML += "<a href='javascript:logout()'>Logout</a>";
        // usrDrop.innerHTML += "<a href='/test'>Test</a>";
    } else {
        if(kick) {
            window.location.href = "/home"
        } else {
            usrDrop.innerHTML = "<a href='/login'>Login</a>"
        }
    }
    httpGetAsync("/perms", function(res) {

        updatePerms(res)
    })
    effected = document.querySelectorAll('.userName');
    for(i = 0; i < effected.length; i++) {
        effected[i].innerHTML = user
    }
}

function updatePerms(str) {
    // console.log(str)
    perms = JSON.parse(str)
    effected = document.querySelectorAll('[data-perm]');
    for(i = 0; i < effected.length; i++) {
        if(effected[i].dataset.perm == "!any") {
            if(perms.length == 0) {
                effected[i].style.display = "inline-block"
            } else {
                effected[i].style.display = "none"
            }
        } else {
            has = hasPerm(effected[i].dataset.perm)
            // console.log("perm: " + effected[i].dataset.perm + "; " + has)
            if(has) {
                effected[i].style.display = "inline-block"
            } else {
                effected[i].style.display = "none"
            }
        }
    }
}

// sends an aysconcronus http get request
function httpGetAsync(theUrl, callback) {
    httpGetAsync2(theUrl, function(res) {
        if (res.status == 200) {
            callback(res.responseText);
          }
    })
}
function httpGetAsync2(theUrl, callback) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4) {
            callback(xmlHttp);
        }
    }
    xmlHttp.open("GET", theUrl, true);
    xmlHttp.send(null);
}

function getAlert(url) {
    httpGetAsync(url,function(res){alert(res)})
}

// sends an aysconcronus http Post request, with type "application/x-www-form-urlencoded"
function httpPostAsyncA(theUrl, body, callback) {
    httpPostAsync(theUrl, "application/x-www-form-urlencoded", body, callback);
}
// sends an aysconcronus http Post request
function httpPostAsync(theUrl, type, body, callback) {
  var xmlHttp = new XMLHttpRequest();
  xmlHttp.onreadystatechange = function() {
    if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
      callback(xmlHttp.responseText);
    } else if(xmlHttp.readyState == 4) {
      console.log("HTTP POST response error; status:" + xmlHttp.status + "; res:'" + xmlHttp.responseText + "'");
    }
  }
  xmlHttp.open("POST", theUrl, true);
  xmlHttp.setRequestHeader("Content-Type", type);
  xmlHttp.send(body);
}

function httpPostAsync2(theUrl, type, body, callback) {
  var xmlHttp = new XMLHttpRequest();
  xmlHttp.onreadystatechange = function() {
    if(xmlHttp.readyState == 4) {
        callback(xmlHttp);
        if(xmlHttp.status != 200) {
          console.log("HTTP POST response error; status:" + xmlHttp.status + "; res:'" + xmlHttp.responseText + "'");
        }
    }
  }
  xmlHttp.open("POST", theUrl, true);
  xmlHttp.setRequestHeader("Content-Type", type);
  xmlHttp.send(body);
}

// reads a cookie from the web page of name 'name'
function readCookie(name) {
  name += '=';
  for (var ca = document.cookie.split(/;\s*/), i = ca.length - 1; i >= 0; i--) {
    if (!ca[i].indexOf(name)) {
      return ca[i].replace(name, '');
    }
  }
}

function logout() {
    document.cookie = "user=-Delete-;path=/"
    document.cookie = "loginKey=-Delete-;path=/"
    window.location.href = "/home"
    // httpPostAsync("/login","","logout", function(res){alert(res)})
}
function isLoggedIn() {
    key = readCookie("loginKey")
    if(!user || user == "" || user == "-Delete-" || !key || key == "" || key == "-Delete-") {
        document.cookie = "user=-Delete-;path=/"
        document.cookie = "loginKey=-Delete-;path=/"
        return false;
    } else {
        return true;
    }
}
function hasPerm(perm) {
    if(perms.includes(perm)) {
        return true
    }
    if(perms.includes("*")) {
        return true
    }
    if(perms.includes(perm+".*")) {
		return true
	}
    if(perm.includes(".")) {
        for(i = perm.length; i > 0; i--) {
            if(perm.slice(i-1,i) == ".") {
                if(perms.includes( perm.slice(0,i) + "*") ) {
                    return true
                }
            }
        }
    }
    return false
}
