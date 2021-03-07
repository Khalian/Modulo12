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
        WHERE simple_expression
    ;

simple_expression:
       scale_type
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

scale_type:
        SCALE EQ SCALE_TYPE
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