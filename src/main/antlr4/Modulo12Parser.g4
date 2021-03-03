parser grammar Modulo12Parser;

options {
	tokenVocab = Modulo12Lexer;

	// antlr will generate java lexer and parser
    language = Java;
}

sql_statement:
		select_key
		from_clause
		where_clause
		SEMI
		;

select_key:
		SELECT
	;

from_clause:
        FROM directory_name
    ;

where_clause:
        WHERE expression
    ;

expression:
		simple_expression (expr_op simple_expression)*
	;

expr_op:
        AND | OR | NOT
    ;

// TODO: Add other branches for simple expression
simple_expression:
		detail EQ |
	;

// TODO: Add other criteria
detail :
        ARTIST_NAME
    ;

directory_name:
        ID
	;