define(["ace/lib/oop", "ace/mode/text", "ace/mode/text_highlight_rules"], function(oop, mText, mTextHighlightRules) {
	var HighlightRules = function() {
		var keywords = "ARINC653|Ada|Background|Blocking|C|Container|DO_178B_ED_12B|DO_178C_ED_12C|Foreground|GeneralPurpose|Go|InboundMessage|Input|Java|Javascript|NonBlocking|OutboundMessage|POSIX|Python|Rust|SQL|SafetyBase|SafetyExtended|Scala|Security|T2T|T2U|Typescript|U2T|U2U|Unspecified|a|aUoP|aconn|aconv|agg|am|arr|b|basis|bool|c|cassoc|ccquery|cdm|centity|ch|char|client|conv|coord|cpp|cquery|csa|csconn|ctempl|d|dm|domain|double|e|ec|enum|filter|float|flsc|framework|ic|im|include|int|irc|isUnion|itc|l|landmark|lassoc|lcquery|ldm|ldouble|lentity|llong|long|lquery|lunit|maxis|mc|meas|msa|msc|msys|nat|observable|octet|passoc|pc|pcquery|pdm|pentity|pquery|psc|qconn|real|rec|rrc|runtime|seq|server|short|simconn|sink|smsys|src|str|string|struct|templ|uinst|ullong|ulong|um|ushort|vtu|xform|xport";
		this.$rules = {
			"start": [
				{token: "comment", regex: "\\/\\/.*$"},
				{token: "comment", regex: "\\/\\*", next : "comment"},
				{token: "string", regex: '["](?:(?:\\\\.)|(?:[^"\\\\]))*?["]'},
				{token: "string", regex: "['](?:(?:\\\\.)|(?:[^'\\\\]))*?[']"},
				{token: "constant.numeric", regex: "[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?\\b"},
				{token: "lparen", regex: "[\\[({]"},
				{token: "rparen", regex: "[\\])}]"},
				{token: "keyword", regex: "\\b(?:" + keywords + ")\\b"}
			],
			"comment": [
				{token: "comment", regex: ".*?\\*\\/", next : "start"},
				{token: "comment", regex: ".+"}
			]
		};
	};
	oop.inherits(HighlightRules, mTextHighlightRules.TextHighlightRules);
	
	var Mode = function() {
		this.HighlightRules = HighlightRules;
	};
	oop.inherits(Mode, mText.Mode);
	Mode.prototype.$id = "xtext/face";
	Mode.prototype.getCompletions = function(state, session, pos, prefix) {
		return [];
	}
	
	return {
		Mode: Mode
	};
});
