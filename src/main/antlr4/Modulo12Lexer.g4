lexer grammar Modulo12Lexer;

fragment DIGIT : [0-9];
fragment PLUS: '+' ;
fragment NUMBER: DIGIT+;
fragment WHITESPACE : ' ' -> skip ;