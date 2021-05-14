/*
 * ngrax-jsonListFilter
 * 针对JSON列表作的过滤器
 * Version: 0.1.0 - 2016/05/31
 * Author 杨松<syang@amarsoft.com>
 * License: MIT
 */


if (typeof module !== "undefined" && typeof exports !== "undefined" && module.exports === exports){
      module.exports = 'ngrax-jsonListFilter';
}

(function (window, angular, undefined) {
	'use strict';
	angular.module('ngrax-jsonListFilter', [])
	.filter("jsonListFilter",function(){
		return function(input, attribute, expr) {
			if (!attribute || !expr) return input;
			if (input instanceof Array){
				var returnArray = [];
				for (var i = 0; i < input.length; i++) {
					var parseExpr = 'input[' + i + ']["' + attribute + '"]' + expr
					if (eval(parseExpr)) returnArray.push(input[i]);
				}
				return returnArray;
			}
			return input;
		};
	});

}(window, angular, undefined));