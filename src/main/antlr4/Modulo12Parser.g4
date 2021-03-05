parser grammar Modulo12Parser;

options {
	tokenVocab = Modulo12Lexer;

	// antlr will generate java lexer and parser
    language = Java;
}

sql_statement:
		select_key
		input_list_clause
		from_clause
		SEMI
    ;

input_list_clause:
        input_name (COMMA input_name)*
    ;

input_name:
        MIDI | MUSICXML
    ;

select_key:
		SELECT
	;

from_clause:
        FROM directory_name
    ;

directory_name:
        ID
	;