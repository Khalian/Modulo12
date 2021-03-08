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
		(where_clause)?
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

where_clause:
        WHERE expression
    ;

logical_op:
		AND | OR
    ;

expression:
        expression logical_op expression
		| simple_expression
	;

simple_expression:
       scale_comparison
        | key_comparison
        | song_has_instrument
        | tempo_comparison
        | num_barlines_comparsion
        | lyrics_comparison
    ;

lyrics_comparison:
        SONG HAS LYRICS words
    ;

num_barlines_comparsion:
         NUMBARLINES relational_op NUMBER
     ;

tempo_comparison:
         TEMPO relational_op NUMBER
     ;

scale_comparison:
        SCALE EQ SCALE_TYPE
   ;

key_comparison:
       KEY EQ key_type
  ;

key_type:
       C | G | D | A | E | B | FSHARP | CSHARP | F | BFLAT | EFLAT | AFLAT | DFLAT | GFLAT | CFLAT
  ;

relational_op:
        EQ | LEQ | GEQ | LT | GT | NEQ
  ;

song_has_instrument:
        SONG HAS INSTRUMENT ID
    ;

words:
        word (COMMA word)*
    ;

word:
        ID
    ;

directory_name:
        ID
	;