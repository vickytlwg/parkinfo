(function(e){"use strict";if(typeof define==="function"&&define.amd){define(["jquery"],e)}else{e(typeof jQuery!="undefined"?jQuery:window.Zepto)}})(function(e){"use strict";var t={};t.fileapi=e("<input type='file'/>").get(0).files!==undefined;t.formdata=window.FormData!==undefined;var r=!!e.fn.prop;e.fn.attr2=function(){if(!r){return this.attr.apply(this,arguments)}var e=this.prop.apply(this,arguments);if(e&&e.jquery||typeof e==="string"){return e}return this.attr.apply(this,arguments)};e.fn.ajaxSubmit=function(a){if(!this.length){n("ajaxSubmit: skipping submit process - no element selected");return this}var i,o,s,f=this;if(typeof a=="function"){a={success:a}}else if(a===undefined){a={}}i=a.type||this.attr2("method");o=a.url||this.attr2("action");s=typeof o==="string"?e.trim(o):"";s=s||window.location.href||"";if(s){s=(s.match(/^([^#]+)/)||[])[1]}a=e.extend(true,{url:s,success:e.ajaxSettings.success,type:i||e.ajaxSettings.type,iframeSrc:/^https/i.test(window.location.href||"")?"javascript:false":"about:blank"},a);var u={};this.trigger("form-pre-serialize",[this,a,u]);if(u.veto){n("ajaxSubmit: submit vetoed via form-pre-serialize trigger");return this}if(a.beforeSerialize&&a.beforeSerialize(this,a)===false){n("ajaxSubmit: submit aborted via beforeSerialize callback");return this}var l=a.traditional;if(l===undefined){l=e.ajaxSettings.traditional}var c=[];var d,m=this.formToArray(a.semantic,c);if(a.data){a.extraData=a.data;d=e.param(a.data,l)}if(a.beforeSubmit&&a.beforeSubmit(m,this,a)===false){n("ajaxSubmit: submit aborted via beforeSubmit callback");return this}this.trigger("form-submit-validate",[m,this,a,u]);if(u.veto){n("ajaxSubmit: submit vetoed via form-submit-validate trigger");return this}var p=e.param(m,l);if(d){p=p?p+"&"+d:d}if(a.type.toUpperCase()=="GET"){a.url+=(a.url.indexOf("?")>=0?"&":"?")+p;a.data=null}else{a.data=p}var v=[];if(a.resetForm){v.push(function(){f.resetForm()})}if(a.clearForm){v.push(function(){f.clearForm(a.includeHidden)})}if(!a.dataType&&a.target){var h=a.success||function(){};v.push(function(t){var r=a.replaceTarget?"replaceWith":"html";e(a.target)[r](t).each(h,arguments)})}else if(a.success){v.push(a.success)}a.success=function(e,t,r){var i=a.context||this;for(var n=0,o=v.length;n<o;n++){v[n].apply(i,[e,t,r||f,f])}};if(a.error){var g=a.error;a.error=function(e,t,r){var i=a.context||this;g.apply(i,[e,t,r,f])}}if(a.complete){var x=a.complete;a.complete=function(e,t){var r=a.context||this;x.apply(r,[e,t,f])}}var y=e("input[type=file]:enabled",this).filter(function(){return e(this).val()!==""});var b=y.length>0;var T="multipart/form-data";var j=f.attr("enctype")==T||f.attr("encoding")==T;var w=t.fileapi&&t.formdata;n("fileAPI :"+w);var S=(b||j)&&!w;var D;if(a.iframe!==false&&(a.iframe||S)){if(a.closeKeepAlive){e.get(a.closeKeepAlive,function(){D=E(m)})}else{D=E(m)}}else if((b||j)&&w){D=L(m)}else{D=e.ajax(a)}f.removeData("jqxhr").data("jqxhr",D);for(var k=0;k<c.length;k++){c[k]=null}this.trigger("form-submit-notify",[this,a]);return this;function A(t){var r=e.param(t,a.traditional).split("&");var i=r.length;var n=[];var o,s;for(o=0;o<i;o++){r[o]=r[o].replace(/\+/g," ");s=r[o].split("=");n.push([decodeURIComponent(s[0]),decodeURIComponent(s[1])])}return n}function L(t){var r=new FormData;for(var n=0;n<t.length;n++){r.append(t[n].name,t[n].value)}if(a.extraData){var o=A(a.extraData);for(n=0;n<o.length;n++){if(o[n]){r.append(o[n][0],o[n][1])}}}a.data=null;var s=e.extend(true,{},e.ajaxSettings,a,{contentType:false,processData:false,cache:false,type:i||"POST"});if(a.uploadProgress){s.xhr=function(){var t=e.ajaxSettings.xhr();if(t.upload){t.upload.addEventListener("progress",function(e){var t=0;var r=e.loaded||e.position;var i=e.total;if(e.lengthComputable){t=Math.ceil(r/i*100)}a.uploadProgress(e,r,i,t)},false)}return t}}s.data=null;var f=s.beforeSend;s.beforeSend=function(e,t){if(a.formData){t.data=a.formData}else{t.data=r}if(f){f.call(this,e,t)}};return e.ajax(s)}function E(t){var o=f[0],s,u,l,d,m,p,v,h,g,x,y,b;var T=e.Deferred();T.abort=function(e){h.abort(e)};if(t){for(u=0;u<c.length;u++){s=e(c[u]);if(r){s.prop("disabled",false)}else{s.removeAttr("disabled")}}}l=e.extend(true,{},e.ajaxSettings,a);l.context=l.context||l;m="jqFormIO"+(new Date).getTime();if(l.iframeTarget){p=e(l.iframeTarget);x=p.attr2("name");if(!x){p.attr2("name",m)}else{m=x}}else{p=e('<iframe name="'+m+'" src="'+l.iframeSrc+'" />');p.css({position:"absolute",top:"-1000px",left:"-1000px"})}v=p[0];h={aborted:0,responseText:null,responseXML:null,status:0,statusText:"n/a",getAllResponseHeaders:function(){},getResponseHeader:function(){},setRequestHeader:function(){},abort:function(t){var r=t==="timeout"?"timeout":"aborted";n("aborting upload... "+r);this.aborted=1;try{if(v.contentWindow.document.execCommand){v.contentWindow.document.execCommand("Stop")}}catch(e){}p.attr("src",l.iframeSrc);h.error=r;if(l.error){l.error.call(l.context,h,r,t)}if(d){e.event.trigger("ajaxError",[h,l,r])}if(l.complete){l.complete.call(l.context,h,r)}}};d=l.global;if(d&&0===e.active++){e.event.trigger("ajaxStart")}if(d){e.event.trigger("ajaxSend",[h,l])}if(l.beforeSend&&l.beforeSend.call(l.context,h,l)===false){if(l.global){e.active--}T.reject();return T}if(h.aborted){T.reject();return T}g=o.clk;if(g){x=g.name;if(x&&!g.disabled){l.extraData=l.extraData||{};l.extraData[x]=g.value;if(g.type=="image"){l.extraData[x+".x"]=o.clk_x;l.extraData[x+".y"]=o.clk_y}}}var j=1;var w=2;function S(e){var t=null;try{if(e.contentWindow){t=e.contentWindow.document}}catch(e){n("cannot get iframe.contentWindow document: "+e)}if(t){return t}try{t=e.contentDocument?e.contentDocument:e.document}catch(r){n("cannot get iframe.contentDocument: "+r);t=e.document}return t}var D=e("meta[name=csrf-token]").attr("content");var k=e("meta[name=csrf-param]").attr("content");if(k&&D){l.extraData=l.extraData||{};l.extraData[k]=D}function A(){var t=f.attr2("target"),r=f.attr2("action"),a="multipart/form-data",s=f.attr("enctype")||f.attr("encoding")||a;o.setAttribute("target",m);if(!i||/post/i.test(i)){o.setAttribute("method","POST")}if(r!=l.url){o.setAttribute("action",l.url)}if(!l.skipEncodingOverride&&(!i||/post/i.test(i))){f.attr({encoding:"multipart/form-data",enctype:"multipart/form-data"})}if(l.timeout){b=setTimeout(function(){y=true;O(j)},l.timeout)}function u(){try{var e=S(v).readyState;n("state = "+e);if(e&&e.toLowerCase()=="uninitialized"){setTimeout(u,50)}}catch(e){n("Server abort: ",e," (",e.name,")");O(w);if(b){clearTimeout(b)}b=undefined}}var c=[];try{if(l.extraData){for(var d in l.extraData){if(l.extraData.hasOwnProperty(d)){if(e.isPlainObject(l.extraData[d])&&l.extraData[d].hasOwnProperty("name")&&l.extraData[d].hasOwnProperty("value")){c.push(e('<input type="hidden" name="'+l.extraData[d].name+'">').val(l.extraData[d].value).appendTo(o)[0])}else{c.push(e('<input type="hidden" name="'+d+'">').val(l.extraData[d]).appendTo(o)[0])}}}}if(!l.iframeTarget){p.appendTo("body")}if(v.attachEvent){v.attachEvent("onload",O)}else{v.addEventListener("load",O,false)}setTimeout(u,15);try{o.submit()}catch(e){var h=document.createElement("form").submit;h.apply(o)}}finally{o.setAttribute("action",r);o.setAttribute("enctype",s);if(t){o.setAttribute("target",t)}else{f.removeAttr("target")}e(c).remove()}}if(l.forceSync){A()}else{setTimeout(A,10)}var L,E,M=50,F;function O(t){if(h.aborted||F){return}E=S(v);if(!E){n("cannot access response document");t=w}if(t===j&&h){h.abort("timeout");T.reject(h,"timeout");return}else if(t==w&&h){h.abort("server abort");T.reject(h,"error","server abort");return}if(!E||E.location.href==l.iframeSrc){if(!y){return}}if(v.detachEvent){v.detachEvent("onload",O)}else{v.removeEventListener("load",O,false)}var r="success",a;try{if(y){throw"timeout"}var i=l.dataType=="xml"||E.XMLDocument||e.isXMLDoc(E);n("isXml="+i);if(!i&&window.opera&&(E.body===null||!E.body.innerHTML)){if(--M){n("requeing onLoad callback, DOM not available");setTimeout(O,250);return}}var o=E.body?E.body:E.documentElement;h.responseText=o?o.innerHTML:null;h.responseXML=E.XMLDocument?E.XMLDocument:E;if(i){l.dataType="xml"}h.getResponseHeader=function(e){var t={"content-type":l.dataType};return t[e.toLowerCase()]};if(o){h.status=Number(o.getAttribute("status"))||h.status;h.statusText=o.getAttribute("statusText")||h.statusText}var s=(l.dataType||"").toLowerCase();var f=/(json|script|text)/.test(s);if(f||l.textarea){var u=E.getElementsByTagName("textarea")[0];if(u){h.responseText=u.value;h.status=Number(u.getAttribute("status"))||h.status;h.statusText=u.getAttribute("statusText")||h.statusText}else if(f){var c=E.getElementsByTagName("pre")[0];var m=E.getElementsByTagName("body")[0];if(c){h.responseText=c.textContent?c.textContent:c.innerText}else if(m){h.responseText=m.textContent?m.textContent:m.innerText}}}else if(s=="xml"&&!h.responseXML&&h.responseText){h.responseXML=X(h.responseText)}try{L=_(h,s,l)}catch(e){r="parsererror";h.error=a=e||r}}catch(e){n("error caught: ",e);r="error";h.error=a=e||r}if(h.aborted){n("upload aborted");r=null}if(h.status){r=h.status>=200&&h.status<300||h.status===304?"success":"error"}if(r==="success"){if(l.success){l.success.call(l.context,L,"success",h)}T.resolve(h.responseText,"success",h);if(d){e.event.trigger("ajaxSuccess",[h,l])}}else if(r){if(a===undefined){a=h.statusText}if(l.error){l.error.call(l.context,h,r,a)}T.reject(h,"error",a);if(d){e.event.trigger("ajaxError",[h,l,a])}}if(d){e.event.trigger("ajaxComplete",[h,l])}if(d&&!--e.active){e.event.trigger("ajaxStop")}if(l.complete){l.complete.call(l.context,h,r)}F=true;if(l.timeout){clearTimeout(b)}setTimeout(function(){if(!l.iframeTarget){p.remove()}else{p.attr("src",l.iframeSrc)}h.responseXML=null},100)}var X=e.parseXML||function(e,t){if(window.ActiveXObject){t=new ActiveXObject("Microsoft.XMLDOM");t.async="false";t.loadXML(e)}else{t=(new DOMParser).parseFromString(e,"text/xml")}return t&&t.documentElement&&t.documentElement.nodeName!="parsererror"?t:null};var C=e.parseJSON||function(e){return window["eval"]("("+e+")")};var _=function(t,r,a){var i=t.getResponseHeader("content-type")||"",n=r==="xml"||!r&&i.indexOf("xml")>=0,o=n?t.responseXML:t.responseText;if(n&&o.documentElement.nodeName==="parsererror"){if(e.error){e.error("parsererror")}}if(a&&a.dataFilter){o=a.dataFilter(o,r)}if(typeof o==="string"){if(r==="json"||!r&&i.indexOf("json")>=0){o=C(o)}else if(r==="script"||!r&&i.indexOf("javascript")>=0){e.globalEval(o)}}return o};return T}};e.fn.ajaxForm=function(t){t=t||{};t.delegation=t.delegation&&e.isFunction(e.fn.on);if(!t.delegation&&this.length===0){var r={s:this.selector,c:this.context};if(!e.isReady&&r.s){n("DOM not ready, queuing ajaxForm");e(function(){e(r.s,r.c).ajaxForm(t)});return this}n("terminating; zero elements found by selector"+(e.isReady?"":" (DOM not ready)"));return this}if(t.delegation){e(document).off("submit.form-plugin",this.selector,a).off("click.form-plugin",this.selector,i).on("submit.form-plugin",this.selector,t,a).on("click.form-plugin",this.selector,t,i);return this}return this.ajaxFormUnbind().bind("submit.form-plugin",t,a).bind("click.form-plugin",t,i)};function a(t){var r=t.data;if(!t.isDefaultPrevented()){t.preventDefault();e(t.target).ajaxSubmit(r)}}function i(t){var r=t.target;var a=e(r);if(!a.is("[type=submit],[type=image]")){var i=a.closest("[type=submit]");if(i.length===0){return}r=i[0]}var n=this;n.clk=r;if(r.type=="image"){if(t.offsetX!==undefined){n.clk_x=t.offsetX;n.clk_y=t.offsetY}else if(typeof e.fn.offset=="function"){var o=a.offset();n.clk_x=t.pageX-o.left;n.clk_y=t.pageY-o.top}else{n.clk_x=t.pageX-r.offsetLeft;n.clk_y=t.pageY-r.offsetTop}}setTimeout(function(){n.clk=n.clk_x=n.clk_y=null},100)}e.fn.ajaxFormUnbind=function(){return this.unbind("submit.form-plugin click.form-plugin")};e.fn.formToArray=function(r,a){var i=[];if(this.length===0){return i}var n=this[0];var o=this.attr("id");var s=r?n.getElementsByTagName("*"):n.elements;var f;if(s&&!/MSIE [678]/.test(navigator.userAgent)){s=e(s).get()}if(o){f=e(":input[form="+o+"]").get();if(f.length){s=(s||[]).concat(f)}}if(!s||!s.length){return i}var u,l,c,d,m,p,v;for(u=0,p=s.length;u<p;u++){m=s[u];c=m.name;if(!c||m.disabled){continue}if(r&&n.clk&&m.type=="image"){if(n.clk==m){i.push({name:c,value:e(m).val(),type:m.type});i.push({name:c+".x",value:n.clk_x},{name:c+".y",value:n.clk_y})}continue}d=e.fieldValue(m,true);if(d&&d.constructor==Array){if(a){a.push(m)}for(l=0,v=d.length;l<v;l++){i.push({name:c,value:d[l]})}}else if(t.fileapi&&m.type=="file"){if(a){a.push(m)}var h=m.files;if(h.length){for(l=0;l<h.length;l++){i.push({name:c,value:h[l],type:m.type})}}else{i.push({name:c,value:"",type:m.type})}}else if(d!==null&&typeof d!="undefined"){if(a){a.push(m)}i.push({name:c,value:d,type:m.type,required:m.required})}}if(!r&&n.clk){var g=e(n.clk),x=g[0];c=x.name;if(c&&!x.disabled&&x.type=="image"){i.push({name:c,value:g.val()});i.push({name:c+".x",value:n.clk_x},{name:c+".y",value:n.clk_y})}}return i};e.fn.formSerialize=function(t){return e.param(this.formToArray(t))};e.fn.fieldSerialize=function(t){var r=[];this.each(function(){var a=this.name;if(!a){return}var i=e.fieldValue(this,t);if(i&&i.constructor==Array){for(var n=0,o=i.length;n<o;n++){r.push({name:a,value:i[n]})}}else if(i!==null&&typeof i!="undefined"){r.push({name:this.name,value:i})}});return e.param(r)};e.fn.fieldValue=function(t){for(var r=[],a=0,i=this.length;a<i;a++){var n=this[a];var o=e.fieldValue(n,t);if(o===null||typeof o=="undefined"||o.constructor==Array&&!o.length){continue}if(o.constructor==Array){e.merge(r,o)}else{r.push(o)}}return r};e.fieldValue=function(t,r){var a=t.name,i=t.type,n=t.tagName.toLowerCase();if(r===undefined){r=true}if(r&&(!a||t.disabled||i=="reset"||i=="button"||(i=="checkbox"||i=="radio")&&!t.checked||(i=="submit"||i=="image")&&t.form&&t.form.clk!=t||n=="select"&&t.selectedIndex==-1)){return null}if(n=="select"){var o=t.selectedIndex;if(o<0){return null}var s=[],f=t.options;var u=i=="select-one";var l=u?o+1:f.length;for(var c=u?o:0;c<l;c++){var d=f[c];if(d.selected){var m=d.value;if(!m){m=d.attributes&&d.attributes.value&&!d.attributes.value.specified?d.text:d.value}if(u){return m}s.push(m)}}return s}return e(t).val()};e.fn.clearForm=function(t){return this.each(function(){e("input,select,textarea",this).clearFields(t)})};e.fn.clearFields=e.fn.clearInputs=function(t){var r=/^(?:color|date|datetime|email|month|number|password|range|search|tel|text|time|url|week)$/i;return this.each(function(){var a=this.type,i=this.tagName.toLowerCase();if(r.test(a)||i=="textarea"){this.value=""}else if(a=="checkbox"||a=="radio"){this.checked=false}else if(i=="select"){this.selectedIndex=-1}else if(a=="file"){if(/MSIE/.test(navigator.userAgent)){e(this).replaceWith(e(this).clone(true))}else{e(this).val("")}}else if(t){if(t===true&&/hidden/.test(a)||typeof t=="string"&&e(this).is(t)){this.value=""}}})};e.fn.resetForm=function(){return this.each(function(){if(typeof this.reset=="function"||typeof this.reset=="object"&&!this.reset.nodeType){this.reset()}})};e.fn.enable=function(e){if(e===undefined){e=true}return this.each(function(){this.disabled=!e})};e.fn.selected=function(t){if(t===undefined){t=true}return this.each(function(){var r=this.type;if(r=="checkbox"||r=="radio"){this.checked=t}else if(this.tagName.toLowerCase()=="option"){var a=e(this).parent("select");if(t&&a[0]&&a[0].type=="select-one"){a.find("option").selected(false)}this.selected=t}})};e.fn.ajaxSubmit.debug=false;function n(){if(!e.fn.ajaxSubmit.debug){return}var t="[jquery.form] "+Array.prototype.join.call(arguments,"");if(window.console&&window.console.log){window.console.log(t)}else if(window.opera&&window.opera.postError){window.opera.postError(t)}}});