String.prototype.replaceAll = function(search, replacement) {
  var target = this;
  return target.split(search).join(replacement);
};

var i18n = {};
onload = function() {
  [...document.querySelectorAll("nav a")]
    .filter(
      a => a.innerHTML == document.querySelector("html").getAttribute("lang")
    )[0]
    .classList.add("selected");
  downloadI18n("/i18n.xml", function(xml) {
    loadI18n(xml);
    downloadI18n("i18n.xml", function(xml) {
      loadI18n(xml);
      refreshI18n();
    });
  });
};

function downloadI18n(path, callback) {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      callback(xhttp.responseXML);
    }
  };
  xhttp.open("GET", path, true);
  xhttp.send();
}

function loadI18n(xml) {
  path = "//string";
  if (xml.evaluate) {
    var nodes = xml.evaluate(path, xml, null, XPathResult.ANY_TYPE, null);
    var result = null;
    while ((result = nodes.iterateNext())) {
      i18n[result.getAttribute("name")] = result.childNodes[0].nodeValue;
    }
  } else if (window.ActiveXObject || xhttp.responseType == "msxml-document") {
    xml.setProperty("SelectionLanguage", "XPath");
    nodes = xml.selectNodes(path);
    for (i = 0; i < nodes.length; i++) {
      var result = nodes[i].childNodes[0];
      i18n[result.getAttribute("name")] = result.nodeValue;
    }
  }
}

function refreshI18n() {
  [...document.querySelectorAll(".hide-default a[data-id]")].forEach(e =>
    i18nElement(e, i18nFullId(e.dataset.id))
  );
  i18nElement(
    document.querySelector(".show-default:first-child span"),
    i18nFullId("pb_campus")
  );
}

function i18nElement(element, value) {
  element.innerText = value;
}

function i18nFullId(fullId) {
  return fullId
    .split("@")
    .map(id => i18nId(id))
    .join("@");
}

function i18nId(id) {
  if (i18n["name_" + id] != null) {
    return normalize(i18n["name_" + id]);
  } else {
    return normalize(id.toUpperCase());
  }
}

function normalize(text) {
  return text
    .replaceAll("_", " ")
    .replaceAll("\n", " ")
    .replaceAll("\\n", " ")
    .trim();
}

function search(text) {
  if (text == null || text.length < 1) {
    [...document.querySelectorAll(".show-default.hide")].forEach(e =>
      e.classList.remove("hide")
    );
    [...document.querySelectorAll(".hide-default.show")].forEach(e =>
      e.classList.remove("show")
    );
    [...document.querySelectorAll(".none:not(.hide)")].forEach(e =>
      e.classList.add("hide")
    );
  } else {
    text = text.toUpperCase();
    [...document.querySelectorAll(".show-default:not(.hide)")].forEach(e =>
      e.classList.add("hide")
    );
    [...document.querySelectorAll(".hide-default.show")]
      .filter(e => !searchMatches(e.querySelector("a").innerText, text))
      .forEach(e => e.classList.remove("show"));
    [...document.querySelectorAll(".hide-default:not(.show)")]
      .filter(e => searchMatches(e.querySelector("a").innerText, text))
      .forEach(e => e.classList.add("show"));
    if (document.querySelectorAll(".hide-default.show").length === 0) {
      [...document.querySelectorAll(".none.hide")].forEach(e =>
        e.classList.remove("hide")
      );
    } else {
      [...document.querySelectorAll(".none:not(.hide)")].forEach(e =>
        e.classList.add("hide")
      );
    }
  }
}

function searchMatches(nodeText, searchText) {
  nodeText = nodeText.toUpperCase();
  var placeSpace = searchText.split("@");
  if (placeSpace.length == 1) {
    return nodeText.indexOf(searchText) !== -1;
  } else {
    var nodePlaceSpace = nodeText.split("@");
    if (nodePlaceSpace.length == 1) {
      return false;
    } else {
      return (
        nodePlaceSpace[0].indexOf(placeSpace[0]) !== -1 &&
        nodePlaceSpace[1].indexOf(placeSpace[1]) !== -1
      );
    }
  }
}
