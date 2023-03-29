grammar SimpleGo;

/*
    Parser
*/

program: packageClause EOL imports functions EOF;

imports: (importDecl (EOL importDecl | EOL)*)?;

functions: (functionDecl (EOL functionDecl | EOL)*)?;

packageClause: PACKAGE IDENTIFIER;

importDecl: IMPORT STRING_LIT;

functionDecl: FUNC IDENTIFIER (signature block?);

signature: parameters typeName | parameters;

parameters: L_PAREN (parameterDecl (COMMA parameterDecl)*)? R_PAREN;

parameterDecl: IDENTIFIER typeName;

typeName: 'bool' | 'float64' | 'int' | 'string';

block: EOL? L_CURLY EOL* statementList? EOL* R_CURLY;

statementList: statement EOL* (EOL statement EOL*)*;

statement
	: declaration
	| simpleStmt
	| returnStmt
	| block
	| forStmt
	;

declaration: varDecl;

simpleStmt
    : assignment
    | expressionStmt
    | ifStmt
    ;

forStmt: FOR expression block;

assignment: expression ASSIGN expression;

expressionStmt: expression;

ifStmt: IF expression block (ELSE (ifStmt | block))?;

returnStmt: RETURN expression?;

varDecl: VAR IDENTIFIER typeName (ASSIGN expression)?;

expressionList: expression (COMMA expression)*;

expression
    : primaryExpr #primaryExpression
	| unary_op = (PLUS | MINUS | EXCLAMATION) expression #unaryExpression
	| expression mul_op = (STAR | DIV | MOD) expression #multiplicativeExpression
	| expression add_op = (PLUS | MINUS) expression #additiveExpression
	| expression rel_op = (EQUALS | NOT_EQUALS | LESS | LESS_OR_EQUALS | GREATER | GREATER_OR_EQUALS) expression #relationalExpression
	| expression LOGICAL_AND expression #logicalAndExpression
	| expression LOGICAL_OR expression #logicalOrExpression
	;

primaryExpr
    : operand
	| primaryExpr ((DOT IDENTIFIER) | arguments);

operand: literal | operandName | L_PAREN expression R_PAREN;

literal: DECIMAL_LIT | STRING_LIT | FLOAT_LIT | BOOLEAN_LIT;

operandName: IDENTIFIER;

arguments: L_PAREN ((expressionList) COMMA?)? R_PAREN;

/*
    Lexer
*/

PACKAGE: 'package';
IMPORT: 'import';
FUNC: 'func';
IF: 'if';
ELSE: 'else';
VAR: 'var';
FOR: 'for';
RETURN: 'return';

BOOLEAN_LIT: 'true' | 'false';

IDENTIFIER: LETTER (LETTER | DIGIT)*;

L_PAREN: '(';
R_PAREN: ')';
L_CURLY: '{';
R_CURLY: '}';
L_BRACKET: '[';
R_BRACKET: ']';
ASSIGN: '=';
COMMA: ',';
DOT: '.';
PLUS_PLUS: '++';
MINUS_MINUS: '--';

LOGICAL_OR: '||';
LOGICAL_AND: '&&';

EQUALS: '==';
NOT_EQUALS: '!=';
LESS: '<';
LESS_OR_EQUALS: '<=';
GREATER: '>';
GREATER_OR_EQUALS: '>=';

OR: '|';
DIV: '/';
MOD: '%';

EXCLAMATION: '!';

PLUS: '+';
MINUS: '-';
STAR: '*';
AMPERSAND: '&';

SEMI: ';';

EOL: [\r\n]+;
WS: [ \t]+ -> skip;
MULTILINE_COMMENT: '/*' .*? '*/' -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip;

STRING_LIT: '"' (~["])* '"';
DECIMAL_LIT: ('0' | [1-9] [0-9]*);
FLOAT_LIT: DECIMALS ('.' DECIMALS?);

fragment LETTER
    : [a-zA-Z]
    | '_'
    ;

fragment DECIMALS:
    [0-9] ('_'? [0-9])*
    ;

fragment DIGIT
    : [0-9]
    ;