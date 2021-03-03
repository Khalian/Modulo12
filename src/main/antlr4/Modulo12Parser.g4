parser grammar Modulo12Parser;

options {
	tokenVocab = Modulo12Lexer;

	// antlr will generate java lexer and parser
    language = Java;
}

sql_statement:
		select_key
		input_name
		from_clause
		SEMI
		;

select_key:
		SELECT
	;

input_name:
        MIDI
    ;

from_clause:
        FROM directory_name
    ;

directory_name:
        ID
	;