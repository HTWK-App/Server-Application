var jspArgs = (function() {
	var jspArgsValues = {}; // Values stored here, protected by a closure
	var jspArgsObj = {}; // This gets augmented and assigned to jspArgs

	jspArgsObj.set = function(name) {
		return function(value) {
			name && (jspArgsValues[name] = value);
		};
	};
	
	jspArgsObj.append = function(name) {
		return function(value) {
			name && (jspArgsValues[name] += value);
		};
	};

	jspArgsObj.get = function(name) {
		return jspArgsValues[name];
	};

	return jspArgsObj;
})();
