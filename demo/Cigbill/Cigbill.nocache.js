function Cigbill(){var jb='',V=' top: -1000px;',sb='" for "gwt:onLoadErrorFn"',qb='" for "gwt:onPropertyErrorFn"',cb='");',tb='#',Gb='.cache.js',vb='/',Bb='//',Eb='8F411B1629DECD03C08E2D277D9DBD1E',Fb=':',kb='::',Kb=':moduleBase',W='<html><head><\/head><body><\/body><\/html>',nb='=',ub='?',pb='Bad handler "',ab='Chrome',O='Cigbill',Db='Cigbill.devmode.js',zb='Cigbill.nocache.js',ib='Cigbill::',_='DOMContentLoaded',Q='DUMMY',Jb='__gwtDevModeHook:Cigbill',Ab='base',yb='baseUrl',L='begin',R='body',K='bootstrap',xb='clear.cache.gif',mb='content',Ib='end',bb='eval("',M='gwt.codesvr.Cigbill=',N='gwt.codesvr=',rb='gwt:onLoadErrorFn',ob='gwt:onPropertyErrorFn',lb='gwt:property',fb='head',S='iframe',wb='img',Y='javascript',T='javascript:""',Hb='loadExternalRefs',gb='meta',eb='moduleRequested',db='moduleStartup',hb='name',U='position:absolute; width:0; height:0; border:none; left: -1000px;',X='script',Cb='selectingPermutation',P='startup',$='undefined',Z='var $wnd = window.parent;';var o=window;var p=document;r(K,L);function q(){var a=o.location.search;return a.indexOf(M)!=-1||a.indexOf(N)!=-1}
function r(a,b){if(o.__gwtStatsEvent){o.__gwtStatsEvent({moduleName:O,sessionId:o.__gwtStatsSessionId,subSystem:P,evtGroup:a,millis:(new Date).getTime(),type:b})}}
Cigbill.__sendStats=r;Cigbill.__moduleName=O;Cigbill.__errFn=null;Cigbill.__moduleBase=Q;Cigbill.__softPermutationId=0;Cigbill.__computePropValue=null;Cigbill.__getPropMap=null;Cigbill.__gwtInstallCode=function(){};Cigbill.__gwtStartLoadingFragment=function(){return null};var s=function(){return false};var t=function(){return null};__propertyErrorFunction=null;var u=o.__gwt_activeModules=o.__gwt_activeModules||{};u[O]={moduleName:O};var v;function w(){y();return v}
function x(){y();return v.getElementsByTagName(R)[0]}
function y(){if(v){return}var a=p.createElement(S);a.src=T;a.id=O;a.style.cssText=U+V;a.tabIndex=-1;p.body.appendChild(a);v=a.contentDocument;if(!v){v=a.contentWindow.document}v.open();v.write(W);v.close();var b=v.getElementsByTagName(R)[0];var c=v.createElement(X);c.language=Y;var d=Z;c.text=d;b.appendChild(c)}
function z(k){function l(a){function b(){if(typeof p.readyState==$){return typeof p.body!=$&&p.body!=null}return /loaded|complete/.test(p.readyState)}
var c=b();if(c){a();return}function d(){if(!c){c=true;a();if(p.removeEventListener){p.removeEventListener(_,d,false)}if(e){clearInterval(e)}}}
if(p.addEventListener){p.addEventListener(_,d,false)}var e=setInterval(function(){if(b()){d()}},50)}
function m(c){function d(a,b){a.removeChild(b)}
var e=x();var f=w();var g;if(navigator.userAgent.indexOf(ab)>-1&&window.JSON){var h=f.createDocumentFragment();h.appendChild(f.createTextNode(bb));for(var i=0;i<c.length;i++){var j=window.JSON.stringify(c[i]);h.appendChild(f.createTextNode(j.substring(1,j.length-1)))}h.appendChild(f.createTextNode(cb));g=f.createElement(X);g.language=Y;g.appendChild(h);e.appendChild(g);d(e,g)}else{for(var i=0;i<c.length;i++){g=f.createElement(X);g.language=Y;g.text=c[i];e.appendChild(g);d(e,g)}}}
Cigbill.onScriptDownloaded=function(a){l(function(){m(a)})};r(db,eb);var n=p.createElement(X);n.src=k;p.getElementsByTagName(fb)[0].appendChild(n)}
Cigbill.__startLoadingFragment=function(a){return C(a)};Cigbill.__installRunAsyncCode=function(a){var b=x();var c=w().createElement(X);c.language=Y;c.text=a;b.appendChild(c);b.removeChild(c)};function A(){var c={};var d;var e;var f=p.getElementsByTagName(gb);for(var g=0,h=f.length;g<h;++g){var i=f[g],j=i.getAttribute(hb),k;if(j){j=j.replace(ib,jb);if(j.indexOf(kb)>=0){continue}if(j==lb){k=i.getAttribute(mb);if(k){var l,m=k.indexOf(nb);if(m>=0){j=k.substring(0,m);l=k.substring(m+1)}else{j=k;l=jb}c[j]=l}}else if(j==ob){k=i.getAttribute(mb);if(k){try{d=eval(k)}catch(a){alert(pb+k+qb)}}}else if(j==rb){k=i.getAttribute(mb);if(k){try{e=eval(k)}catch(a){alert(pb+k+sb)}}}}}t=function(a){var b=c[a];return b==null?null:b};__propertyErrorFunction=d;Cigbill.__errFn=e}
function B(){function e(a){var b=a.lastIndexOf(tb);if(b==-1){b=a.length}var c=a.indexOf(ub);if(c==-1){c=a.length}var d=a.lastIndexOf(vb,Math.min(c,b));return d>=0?a.substring(0,d+1):jb}
function f(a){if(a.match(/^\w+:\/\//)){}else{var b=p.createElement(wb);b.src=a+xb;a=e(b.src)}return a}
function g(){var a=t(yb);if(a!=null){return a}return jb}
function h(){var a=p.getElementsByTagName(X);for(var b=0;b<a.length;++b){if(a[b].src.indexOf(zb)!=-1){return e(a[b].src)}}return jb}
function i(){var a=p.getElementsByTagName(Ab);if(a.length>0){return a[a.length-1].href}return jb}
function j(){var a=p.location;return a.href==a.protocol+Bb+a.host+a.pathname+a.search+a.hash}
var k=g();if(k==jb){k=h()}if(k==jb){k=i()}if(k==jb&&j()){k=e(p.location.href)}k=f(k);return k}
function C(a){if(a.match(/^\//)){return a}if(a.match(/^[a-zA-Z]+:\/\//)){return a}return Cigbill.__moduleBase+a}
function D(){var f=[];var g;var h=[];var i=[];function j(a){var b=i[a](),c=h[a];if(b in c){return b}var d=[];for(var e in c){d[c[e]]=e}if(__propertyErrorFunc){__propertyErrorFunc(a,d,b)}throw null}
s=function(a,b){return b in h[a]};Cigbill.__getPropMap=function(){var a={};for(var b in h){a[b]=j(b)}return a};Cigbill.__computePropValue=j;o.__gwt_activeModules[O].bindings=Cigbill.__getPropMap;r(K,Cb);if(q()){return C(Db)}var k;try{k=Eb;var l=k.indexOf(Fb);if(l!=-1){g=parseInt(k.substring(l+1),10);k=k.substring(0,l)}}catch(a){}Cigbill.__softPermutationId=g;return C(k+Gb)}
function E(){if(!o.__gwt_stylesLoaded){o.__gwt_stylesLoaded={}}r(Hb,L);r(Hb,Ib)}
A();Cigbill.__moduleBase=B();u[O].moduleBase=Cigbill.__moduleBase;var F=D();if(o){o.__gwt_activeModules[O].canRedirect=true}var G=Jb;var H=o.sessionStorage[G];if(H&&!o[G]){o[G]=true;var I=p.createElement(X);o[G+Kb]=B();I.src=H;var J=p.getElementsByTagName(fb)[0];J.insertBefore(I,J.firstElementChild);return false}E();r(K,Ib);z(F);return true}
Cigbill.succeeded=Cigbill();