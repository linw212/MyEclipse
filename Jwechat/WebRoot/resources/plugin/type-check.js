/**
 * type-check.js 数据类型检测函数
 * 使用方法:Type.isArray(obj),Type.isObject(obj)....
 * @author 陈江林
 * @version 0.1
 */
(function(){
	Type={};
    //检测v的类型 辅助函数
    var type = function(v){
        return Object.prototype.toString.call(v);
    };
    
    /**
     * 是否为数组对象类型  如果是就返回true 如果不是就返回false
     * @namespace Type
     * @method isArray
     * @param {Any} v 被检测的变量
     * @return {Boolean} 结果
     */
    Type.isArray = function(v){
        return type(v) === '[object Array]';
    };
    /**
     * 是否为参数管理器Arguments 如果是就返回true 如果不是就返回false
     * @param {Any} v 被检测的变量
     * @return {Boolean}
     */
    Type.isArguments = function(v){
        return v.callee != undefined;
    };
    /**
     * 是否为迭代序列 包含Array与Arguments 如果是就返回true 如果不是就返回false
     * @param {Any} v 被检测的变量
     * @return {Boolean}
     */
    Type.isIterable = function(v){
        return Type.isArray(v) || Type.isArguments(v);
    };
    /**
     * 是否为空对象 null和undefined和数组的长度为0或空字符串("") 如果是就返回true 如果不是就返回false
     * @param {Any} v 被检测的变量 
     * @param {Boolean} allowBlank [可选] 默认false 空字符串认为是空对象 反之 空字符串不认为是空对象
     * @return {Boolean}
     */
    Type.isEmpty = function(v, allowBlank){
        return v === null || v === undefined ||
        (Type.isArray(v) && !v.length) ||
        (!allowBlank ? v === '' : false);
    };
    /**
     * 是否为字符串类型 如果是就返回true 如果不是就返回false
     * @param {Any} v 被检测的变量
     * @return {Boolean}
     */
    Type.isString = function(v){
        return typeof v === 'string';
    };
    /**
     * 是否为数字类型(为Number且不为正负无穷大数字) 如果是就返回true 如果不是就返回false
     * @param {Any} v 被检测的变量
     * @return {Boolean}
     */
    Type.isNumber = function(v){
        return typeof v === 'number' && isFinite(v);
        
    };
    /**
     * 是否为布尔值类型  如果是就返回true 如果不是就返回false
     * @param {Any} v 被检测的变量
     * @return {Boolean}
     */
    Type.isBoolean = function(v){
        return typeof v === 'boolean';
    };
    /**
     * 是否为函数类型 如果是就返回true 如果不是就返回false
     * @param {Any} v 被检测的变量
     * @return {Boolean}
     */
    Type.isFuntion = function(v){
        return type(v) === '[object Function]';
    };
    /**
     * 是否为对象类型
     * @param {Any} v 被检测的变量
     * @return {boolean}
     */
    Type.isObject = function(v){
        return !!v && type(v) === '[object Object]';
    };
    /**
     * 是否为日期类型  如果是就返回true 如果不是就返回false
     * @param {Any} v 被检测的变量
     * @return {boolean}
     */
    Type.isDate = function(v){
        return type(v) === '[object Date]';
    };
    /**
     * 是否为正则表达式类型  如果是就返回true 如果不是就返回false
     * @param {Any} v 被检测的变量
     * @return {Boolean}
     */
    Type.isRegexp = function(v){
        return type(v) == '[object RegExp]';
    };
    /**
     * 是否为原始数据类型 如果是就返回true 如果不是就返回false
     * @param {Any} v 被检测的变量
     * @return {Boolean}
     */
    Type.isPrimitive = function(v){
        return Type.isString(v) || Type.isNumber(v) ||
        Type.isBoolean(v);
    };
    /**
     * 返回数据类型的字符串形式<br>
     *  数字类型:"number" <br>
     *  布尔类型:"boolean" <br>
     *  字符串类型:"string" <br>
     *  数组类型:"array"<br>
     *  日期类型:"date"<br>
     *  正则表达式类型:"regexp" <br>
     *  函数类型:"function"<br>
     *  对象类型:"object"<br>
     *  参数管理器类型:"arguments"<br>
     *  其他类型:"unknow"
     * @param {Any} v 被检测的变量
     * @return {String}
     */
    Type.type = function(v){
        var result = "unknow";
        if (Type.isNumber(v)) {
            result = "number";
        }
        if (Type.isBoolean(v)) {
            result = "boolean";
        }
        if (Type.isString(v)) {
            result = "string";
        }
        if (Type.isArray(v)) {
            result = "array";
        }
        if (Type.isDate(v)) {
            result = "date";
        }
        if (Type.isRegexp(v)) {
            result = "regexp";
        }
        if (Type.isFuntion(v)) {
            result = "function";
        }
        if (Type.isObject(v)) {
            result = "object";
        }
        if (Type.isArguments(v)) {
            result = "argumetns";
        }
        return result;
    };
})();