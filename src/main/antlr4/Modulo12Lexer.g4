lexer grammar Modulo12Lexer;

A : [aA];
B : [bB];
C : [cC];
D : [dD];
E : [eE];
F : [fF];
G : [gG];
H : [hH];
I : [iI];
J : [jJ];
K : [kK];
L : [lL];
M : [mM];
N : [nN];
O : [oO];
P : [pP];
Q : [qQ];
R : [rR];
S : [sS];
T : [tT];
U : [uU];
V : [vV];
W : [wW];
X : [xX];
Y : [yY];
Z : [zZ];

// Conditions
EQ: '=';
LEQ: '<=';
GEQ: '>=';
NEQ : '!=';
LT: '<';
GT: '>';
HAS: H A S;

// Logical operations
AND: A N D;
OR : O R;

// Key words
SELECT: S E L E C T;
MIDI: M I D I;
MUSICXML: M U S I C X M L;
FROM: F R O M;
WHERE: W H E R E;
SEMI: ';';
COMMA: ',';

// Song metadata factors
KEY : K E Y;
SHARP: '#';
FLAT: 'b';
// Keys with qualifiers, TODO: Simplify this somehow
FSHARP : F SHARP;
CSHARP : C SHARP;
BFLAT : B FLAT;
EFLAT : E FLAT;
AFLAT : A FLAT;
DFLAT : D FLAT;
GFLAT : G FLAT;
CFLAT : C FLAT;

SCALE: S C A L E;
SCALE_TYPE : M I N O R | M A J O R;
SONG: S O N G;
INSTRUMENT: I N S T R U M E N T;
LYRICS: L Y R I C S;
TEMPO: T E M P O;
NUMBARLINES: N U M B A R L I N E S;

// Generic definitions
ID: ('a'..'z' | 'A' .. 'Z' | '_' | '/')+ ;
NUMBER: ('0' .. '9') + ('.' ('0' .. '9') +)?;
NEWLINE: '\r' ? '\n' -> skip;
WS: (' ' | '\t' | '\n' | '\r')+ -> skip;