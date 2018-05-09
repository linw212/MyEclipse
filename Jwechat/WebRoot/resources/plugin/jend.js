// ==================================================
// ==== JEND v2.0.0.1 ====
// 基于jQuery v1.3.2，在旧版JEND基础上扩展，包括以下方法集合：
// - JS原型方法扩展： String/Number/Array/Date
// - jQuery方法扩展: $.browser/$.fn
// - JEND旧版保留方法: flash/DOM/DSS/cookie
// - JEND方法库： core/json/base/page/login
// ==================================================
! function(a) {
	a && a.extend(String.prototype, {
		_toBoolean: function() {
			return "false" === this.toString() || "" === this.toString() || "0" === this.toString() ? !1 : !0
		},
		_toNumber: function() {
			return isNaN(this) ? this.toString() : Number(this)
		},
		_toRealValue: function() {
			return "true" === this.toString() || "false" === this.toString() ? this._toBoolean() : this._toNumber()
		},
		trim: function() {
			return this.replace(/(^\s*)|(\s*$)/g, "")
		},
		ltrim: function() {
			return this.replace(/(^\s*)/g, "")
		},
		rtrim: function() {
			return this.replace(/(\s*$)/g, "")
		},
		trimAll: function() {
			return this.replace(/\s/g, "")
		},
		trimNoteChar: function() {
			return this.replace(/^[^\{]*\{\s*\/\*!?|\*\/[;|\s]*\}$/g, "").trim()
		},
		left: function(a) {
			return this.substring(0, a)
		},
		right: function(a) {
			return this.length <= a ? this.toString() : this.substring(this.length - a, this.length)
		},
		reverse: function() {
			return this.split("").reverse().join("")
		},
		startWith: function(a, b) {
			return !(b ? this.toLowerCase().indexOf(a.toLowerCase()) : this.indexOf(a))
		},
		endWith: function(a, b) {
			return b ? new RegExp(a.toLowerCase() + "$").test(this.toLowerCase().trim()) : new RegExp(a + "$").test(this.trim())
		},
		sliceAfter: function(a) {
			return this.indexOf(a) >= 0 ? this.substring(this.indexOf(a) + a.length, this.length) : ""
		},
		sliceBefore: function(a) {
			return this.indexOf(a) >= 0 ? this.substring(0, this.indexOf(a)) : ""
		},
		getByteLength: function() {
			return this.replace(/[^\x00-\xff]/gi, "xx").length
		},
		subByte: function(a, b) {
			if (0 > a || this.getByteLength() <= a) return this.toString();
			var c = this;
			return c = c.substr(0, a).replace(/([^\x00-\xff])/g, "$1 ").substr(0, a).replace(/[^\x00-\xff]$/, "").replace(/([^\x00-\xff]) /g, "$1"), c + (b || "")
		},
		encodeURI: function(a) {
			var b = a || "utf",
				c = "uni" == b ? escape : encodeURIComponent;
			return c(this)
		},
		decodeURI: function(a) {
			var b = a || "utf",
				c = "uni" == b ? unescape : decodeURIComponent;
			try {
				for (var d = this.toString(), e = c(d); d != e;) d = e, e = c(d);
				return d
			} catch (f) {
				return this.toString()
			}
		},
		textToHtml: function() {
			return this.replace(/</gi, "&lt;").replace(/>/gi, "&gt;").replace(/\r\n/gi, "<br>").replace(/\n/gi, "<br>")
		},
		htmlToText: function() {
			return this.replace(/<br>/gi, "\r\n")
		},
		htmlEncode: function() {
			var a = this,
				b = {
					"<": "&lt;",
					">": "&gt;",
					"&": "&amp;",
					'"': "&quot;"
				};
			for (var c in b) a = a.replace(new RegExp(c, "g"), b[c]);
			return a
		},
		htmlDecode: function() {
			var a = this,
				b = {
					"&lt;": "<",
					"&gt;": ">",
					"&amp;": "&",
					"&quot;": '"'
				};
			for (var c in b) a = a.replace(new RegExp(c, "g"), b[c]);
			return a
		},
		stripHtml: function() {
			return this.replace(/(<\/?[^>\/]*)\/?>/gi, "")
		},
		stripScript: function() {
			return this.replace(/<script(.|\n)*\/script>\s*/gi, "").replace(/on[a-z]*?\s*?=".*?"/gi, "")
		},
		replaceAll: function(a, b) {
			return this.replace(new RegExp(a, "gm"), b)
		},
		escapeReg: function() {
			return this.replace(new RegExp("([.*+?^=!:${}()|[\\]/\\\\])", "g"), "\\$1")
		},
		addQueryValue: function(b, c) {
			var d = this.getPathName(),
				e = this.getQueryJson();
			return e[b] || (e[b] = c), d + "?" + a.param(e)
		},
		getQueryValue: function(a) {
			var b = new RegExp("(^|&|\\?|#)" + a.escapeReg() + "=([^&]*)(&|$)", ""),
				c = this.match(b);
			return c ? c[2] : ""
		},
		getQueryJson: function() {
			if (this.indexOf("?") < 0) return {};
			for (var a, b, c, d, e = this.substr(this.indexOf("?") + 1), f = e.split("&"), g = f.length, h = {}, i = 0; g > i; i++) d = f[i].split("="), a = d[0], b = d[1], c = h[a], "undefined" == typeof c ? h[a] = b : "[object Array]" == Object.prototype.toString.call(c) ? c.push(b) : h[a] = [c, b];
			return h
		},
		getDomain: function() {
			return this.startWith("http://") ? this.split("/")[2] : ""
		},
		getPathName: function() {
			var a = this.toString();
			return a.indexOf("?") > 0 && (a = a.sliceBefore("?")), a.indexOf("#") > 0 && (a = a.sliceBefore("#")), a
		},
		getFilePath: function() {
			return this.substring(0, this.lastIndexOf("/") + 1)
		},
		getFileName: function() {
			return this.substring(this.lastIndexOf("/") + 1)
		},
		getFileExt: function() {
			return this.substring(this.lastIndexOf(".") + 1)
		},
		parseDate: function() {
			return (new Date).parse(this.toString())
		},
		parseJSON: function() {
			return new Function("return " + this.toString())()
		},
		parseAttrJSON: function() {
			for (var a = {}, b = this.toString().split(";"), c = 0; c < b.length; c++)
				if (!("" === b[c].trim() || b[c].indexOf(":") < 1)) {
					var d = b[c].sliceBefore(":").trim(),
						e = b[c].sliceAfter(":").trim();
					"" !== d && "" !== e && (a[d.toCamelCase()] = e._toRealValue())
				}
			return a
		},
		_pad: function(a, b, c) {
			for (var d = [c ? "" : this, c ? this : ""]; d[c].length < (a ? a : 0) && (d[c] = d[1] + (b || " ") + d[0]););
			return d[c]
		},
		padLeft: function(a, b) {
			return this.length >= a ? this.toString() : this._pad(a, b, 0)
		},
		padRight: function(a, b) {
			return this.length >= a ? this.toString() : this._pad(a, b, 1)
		},
		toHalfWidth: function() {
			return this.replace(/[\uFF01-\uFF5E]/g, function(a) {
				return String.fromCharCode(a.charCodeAt(0) - 65248)
			}).replace(/\u3000/g, " ")
		},
		toCamelCase: function() {
			return this.indexOf("-") < 0 && this.indexOf("_") < 0 ? this.toString() : this.replace(/[-_][^-_]/g, function(a) {
				return a.charAt(1).toUpperCase()
			})
		},
		format: function() {
			var b = this;
			if (arguments.length > 0) {
				var c = 1 == arguments.length && a.isArray(arguments[0]) ? arguments[0] : a.makeArray(arguments);
				a.each(c, function(a, c) {
					b = b.replace(new RegExp("\\{" + a + "\\}", "g"), c)
				})
			}
			return b
		},
		substitute: function(a) {
			return a && "object" == typeof a ? this.replace(/\{([^{}]+)\}/g, function(b, c) {
				var d = a[c];
				return void 0 !== d ? "" + d : ""
			}) : this.toString()
		},
		toEAN13: function(a) {
			for (var b = 12 - a.length, c = a + (this.length >= b ? this.left(b) : this.padLeft(b, "0")), d = 0, e = 0, f = 0, g = c.reverse(), h = 0; h < g.length; h++) h % 2 ? e += parseInt(g.charAt(h), 10) : d += parseInt(g.charAt(h), 10);
			return (3 * d + e) % 10 && (f = 10 - (3 * d + e) % 10), c + f
		},
		formatToDSS: function() {
			var a = this.match(/^http(?:s)?:\/\/([^:\/]*)(:\d+)?([^\?]*)/),
				b = "";
			if (a[1].match(/^[\d\.]*$/)) b = "/ip/" + a[1];
			else
				for (var c = a[1].split("."), d = c.length; d > 0; d--) b += "/" + c[d - 1];
			return a[2] && (b += "/" + a[2]), b + a[3]
		},
		formatToDSSID: function() {
			return this.formatToDSS().replace(/\//g, "_")
		},
		toMapObject: function(a) {
			a = a || "/";
			var b = this.split(a),
				c = {},
				d = function(a, b, e) {
					e < b.length && (a[b[e]] || (a[b[e]] = {}), c = a[b[e]], d(a[b[e]], b, e + 1))
				};
			return d(window, b, 1), c
		}
	})
}(jQuery),
function(a) {
	a && a.extend(String.prototype, {
		isIP: function() {
			var a = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
			return a.test(this.trim())
		},
		isUrl: function() {
			return new RegExp(/^(ftp|https?):\/\/([^\s\.]+\.[^\s]{2,}|localhost)$/i).test(this.trim())
		},
		isURL: function() {
			return this.isUrl()
		},
		isDate: function() {
			var a = this.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
			if (null === a) return !1;
			var b = new Date(a[1], a[3] - 1, a[4]);
			return b.getFullYear() == a[1] && b.getMonth() + 1 == a[3] && b.getDate() == a[4]
		},
		isTime: function() {
			var a = this.match(/^(\d{1,2})(:)?(\d{1,2})\2(\d{1,2})$/);
			return null === a ? !1 : a[1] > 24 || a[3] > 60 || a[4] > 60 ? !1 : !0
		},
		isDateTime: function() {
			var a = this.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/);
			if (null === a) return !1;
			var b = new Date(a[1], a[3] - 1, a[4], a[5], a[6], a[7]);
			return b.getFullYear() == a[1] && b.getMonth() + 1 == a[3] && b.getDate() == a[4] && b.getHours() == a[5] && b.getMinutes() == a[6] && b.getSeconds() == a[7]
		},
		isInteger: function() {
			return new RegExp(/^(-|\+)?\d+$/).test(this.trim())
		},
		isPositiveInteger: function() {
			return new RegExp(/^\d+$/).test(this.trim()) && parseInt(this, 10) > 0
		},
		isNegativeInteger: function() {
			return new RegExp(/^-\d+$/).test(this.trim())
		},
		isNumber: function() {
			return !isNaN(this)
		},
		isRealName: function() {
			return new RegExp(/^[A-Za-z \u4E00-\u9FA5]+$/).test(this)
		},
		isLogName: function() {
			return this.isEmail() || this.isMobile()
		},
		isEmail: function() {
			return new RegExp(/^([_a-zA-Z\d\-\.])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/).test(this.trim())
		},
		isMobile: function() {
			return new RegExp(/^(13|14|15|17|18)\d{9}$/).test(this.trim())
		},
		isPhone: function() {
			return new RegExp(/^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/).test(this.trim())
		},
		isAreacode: function() {
			return new RegExp(/^0\d{2,3}$/).test(this.trim())
		},
		isPostcode: function() {
			return new RegExp(/^\d{6}$/).test(this.trim())
		},
		isLetters: function() {
			return new RegExp(/^[A-Za-z]+$/).test(this.trim())
		},
		isDigits: function() {
			return new RegExp(/^[1-9][0-9]+$/).test(this.trim())
		},
		isAlphanumeric: function() {
			return new RegExp(/^[a-zA-Z0-9]+$/).test(this.trim())
		},
		isValidString: function() {
			return new RegExp(/^[a-zA-Z0-9\s.\-_]+$/).test(this.trim())
		},
		isLowerCase: function() {
			return new RegExp(/^[a-z]+$/).test(this.trim())
		},
		isUpperCase: function() {
			return new RegExp(/^[A-Z]+$/).test(this.trim())
		},
		isChinese: function() {
			return new RegExp(/^[\u4e00-\u9fa5]+$/).test(this.trim())
		},
		isIDCard: function() {
			var a = new RegExp(/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/),
				b = new RegExp(/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X|x)$/);
			return a.test(this.trim()) || b.test(this.trim())
		},
		isCardNo: function(a) {
			var b = {
				UleCard: {
					lengths: "16",
					prefixes: "",
					checkdigit: !0
				},
				Visa: {
					lengths: "13,16",
					prefixes: "4",
					checkdigit: !0
				},
				MasterCard: {
					lengths: "16",
					prefixes: "51,52,53,54,55",
					checkdigit: !0
				},
				BankCard: {
					lengths: "16,17,19",
					prefixes: "3,4,5,6,9",
					checkdigit: !1
				}
			};
			if (!b[a]) return !1;
			var c = this.replace(/[\s-]/g, ""),
				d = /^[0-9]{13,19}$/;
			if (0 !== c.length && d.exec(c)) {
				c = c.replace(/\D/g, "");
				var e = !0,
					f = !1,
					g = !1;
				if (b[a].checkdigit) {
					var h, i = 0,
						j = 1;
					for (n = c.length - 1; n >= 0; n--) h = Number(c.charAt(n)) * j, h > 9 && (i += 1, h -= 10), i += h, j = 1 == j ? 2 : 1;
					i % 10 !== 0 && (e = !1)
				}
				if ("" === b[a].prefixes) f = !0;
				else {
					var k = b[a].prefixes.split(",");
					for (n = 0; n < k.length; n++) {
						var l = new RegExp("^" + k[n]);
						l.test(c) && (f = !0)
					}
				}
				for (var m = b[a].lengths.split(","), n = 0; n < m.length; n++) c.length == m[n] && (g = !0);
				return e && f && g ? !0 : !1
			}
			return !1
		},
		isUleCard: function() {
			return this.isCardNo("UleCard")
		},
		isVisa: function() {
			return this.isCardNo("Visa")
		},
		isMasterCard: function() {
			return this.isCardNo("MasterCard")
		},
		isValidEAN: function() {
			for (var a = this.trim(), b = 0, c = 0, d = parseInt(a.right(1), 10), e = a.left(a.length - 1).reverse(), f = 0; f < e.length; f++) f % 2 ? c += parseInt(e.charAt(f), 10) : b += parseInt(e.charAt(f), 10);
			return (3 * b + c + d) % 10 ? !1 : !0
		},
		isEAN8: function() {
			var a = this.trim();
			return new RegExp(/^\d{8}$/).test(a) && a.isValidEAN()
		},
		isEAN12: function() {
			var a = this.trim();
			return new RegExp(/^\d{12}$/).test(a) && a.isValidEAN()
		},
		isEAN13: function() {
			var a = this.trim();
			return new RegExp(/^\d{13}$/).test(a) && a.isValidEAN()
		},
		isISBN10: function() {
			var a = this.trim();
			if (!new RegExp(/^\d{9}([0-9]|X|x)$/).test(a)) return !1;
			for (var b = 0, c = a.right(1), d = a.reverse(), e = 1; e < d.length; e++) b += parseInt(d.charAt(e), 10) * (e + 1);
			return ("X" == c || "x" == c) && (c = 10), (b + parseInt(c, 10)) % 11 ? !1 : !0
		},
		isISBN: function() {
			return this.isEAN13()
		},
		isEANCode: function() {
			return this.isEAN8() || this.isEAN12() || this.isEAN13() || this.isISBN10()
		}
	})
}(jQuery),
function(a) {
	a && a.extend(Number.prototype, {
		comma: function(a) {
			(!a || 1 > a) && (a = 3);
			var b = ("" + this).split(".");
			return b[0] = b[0].replace(new RegExp("(\\d)(?=(\\d{" + a + "})+$)", "ig"), "$1,"), b.join(".")
		},
		randomInt: function(a, b) {
			return Math.floor(Math.random() * (b - a + 1) + a)
		},
		padLeft: function(a, b) {
			return ("" + this).padLeft(a, b)
		},
		padRight: function(a, b) {
			return ("" + this).padRight(a, b)
		}
	})
}(jQuery),
function(a) {
	a && a.extend(Array.prototype, {
		indexOf: function(a, b) {
			for (var c = 0; c < this.length; c++)
				if (a == (b ? this[c][b] : this[c])) return c;
			return -1
		},
		remove: function(a, b) {
			this.removeAt(this.indexOf(a, b))
		},
		removeAt: function(a) {
			if (a >= 0 && a < this.length) {
				for (var b = a; b < this.length - 1; b++) this[b] = this[b + 1];
				this.length--
			}
		},
		removeEmpty: function() {
			for (var a = [], b = 0; b < this.length; b++) "" !== this[b].trim() && a.push(this[b].trim());
			return a
		},
		add: function(a) {
			return this.indexOf(a) > -1 ? !1 : (this.push(a), !0)
		},
		swap: function(a, b) {
			if (a < this.length && b < this.length && a != b) {
				var c = this[a];
				this[a] = this[b], this[b] = c
			}
		},
		unique: function() {
			var a, b, c = [],
				d = {},
				e = this.length;
			if (2 > e) return this;
			for (a = 0; e > a; a++) b = this[a], 1 !== d[b] && (c.push(b), d[b] = 1);
			return c
		},
		sortby: function(a, b, c) {
			for (var d = function(a, b, c, d) {
				"int" == c ? (a = parseInt(a, 10), b = parseInt(b, 10)) : "float" == c && (a = parseFloat(a), b = parseFloat(b));
				var e = 0;
				return b > a && (e = 1), a > b && (e = -1), "desc" == d && (e = 0 - e), e
			}, e = [], f = 0; f < this.length; f++) e[e.length] = this[f];
			for (f = 0; f < e.length; f++) {
				for (var g = f, h = "" !== a ? e[f][a] : e[f], i = f + 1; i < e.length; i++) {
					var j = "" !== a ? e[i][a] : e[i],
						k = d(h, j, b, c);
					0 > k && (g = i, h = j)
				}
				if (g > f) {
					var l = e[g];
					e[g] = e[f], e[f] = l
				}
			}
			return e
		}
	})
}(jQuery),
function(a) {
	a && a.extend(Date.prototype, {
		parse: function(a) {
			return "string" == typeof a ? a.indexOf("GMT") > 0 || a.indexOf("gmt") > 0 || !isNaN(Date.parse(a)) ? this._parseGMT(a) : a.indexOf("UTC") > 0 || a.indexOf("utc") > 0 || a.indexOf(",") > 0 ? this._parseUTC(a) : this._parseCommon(a) : new Date
		},
		_parseGMT: function(a) {
			return this.setTime(Date.parse(a)), this
		},
		_parseUTC: function(a) {
			return new Date(a)
		},
		_parseCommon: function(a) {
			var b = a.split(/ |T/),
				c = b.length > 1 ? b[1].split(/[^\d]/) : [0, 0, 0],
				d = b[0].split(/[^\d]/);
			return new Date(d[0] - 0, d[1] - 1, d[2] - 0, (c[0] || 0) - 0, (c[1] || 0) - 0, (c[2] || 0) - 0)
		},
		clone: function() {
			return (new Date).setTime(this.getTime())
		},
		dateAdd: function(a, b) {
			var c = this.getFullYear(),
				d = this.getMonth(),
				e = this.getDate(),
				f = this.getHours(),
				g = this.getMinutes(),
				h = this.getSeconds();
			switch (a) {
				case "y":
					this.setFullYear(c + b);
					break;
				case "m":
					this.setMonth(d + b);
					break;
				case "d":
					this.setDate(e + b);
					break;
				case "h":
					this.setHours(f + b);
					break;
				case "n":
					this.setMinutes(g + b);
					break;
				case "s":
					this.setSeconds(h + b)
			}
			return this
		},
		dateDiff: function(a, b) {
			var c = b - this;
			switch (a) {
				case "w":
					return c / 1e3 / 3600 / 24 / 7;
				case "d":
					return c / 1e3 / 3600 / 24;
				case "h":
					return c / 1e3 / 3600;
				case "n":
					return c / 1e3 / 60;
				case "s":
					return c / 1e3
			}
		},
		format: function(a) {
			if (isNaN(this)) return "";
			var b = {
				"m+": this.getMonth() + 1,
				"d+": this.getDate(),
				"h+": this.getHours(),
				"n+": this.getMinutes(),
				"s+": this.getSeconds(),
				S: this.getMilliseconds(),
				W: ["日", "一", "二", "三", "四", "五", "六"][this.getDay()],
				"q+": Math.floor((this.getMonth() + 3) / 3)
			};
			a.indexOf("am/pm") >= 0 && (a = a.replace("am/pm", b["h+"] >= 12 ? "下午" : "上午"), b["h+"] >= 12 && (b["h+"] -= 12)), /(y+)/.test(a) && (a = a.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length)));
			for (var c in b) new RegExp("(" + c + ")").test(a) && (a = a.replace(RegExp.$1, 1 == RegExp.$1.length ? b[c] : ("00" + b[c]).substr(("" + b[c]).length)));
			return a
		}
	})
}(jQuery),
function(a) {
	a && a.extend($.fn, {
		formJson: function() {
			var o = {};
			var a = this.serializeArray();
			$.each(a, function() {
				if (o[this.name]) {
					if (!o[this.name].push) {
						o[this.name] = [ o[this.name] ];
					}
					o[this.name].push(this.value || '');
				} else {
					o[this.name] = this.value || '';
				}
			});
			return $.toJSON(o);
		}
	})
}(jQuery),
function(a) {
	var MATH_UUID_CHARS = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
	a && a.extend(Math, {
		uuid: function(len, radix) {
			var chars = MATH_UUID_CHARS, uuid = [], i;
			radix = radix || chars.length;
			if (len) {
				for (i = 0; i < len; i++)
					uuid[i] = chars[0 | Math.random() * radix];
			} else {
				var r;
				uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
				uuid[14] = '4';
				for (i = 0; i < 36; i++) {
					if (!uuid[i]) {
						r = 0 | Math.random() * 16;
						uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
					}
				}
			}
			return uuid.join('');
		}
	})
}(jQuery);
