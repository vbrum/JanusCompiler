package teste;

import java_cup.runtime.*;
import newpackage.*;


%%


%cupsym Sym
%cup
%public
%class LexicalAnalyzer
%line
%column


num = 0|[1-9][0-9]*
brancos = [\n| |\t]
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

id = [a-zA-Z][a-zA-Z0-9]*
comment = ";" {InputCharacter}* {LineTerminator}?

%%


"procedure" { return new Symbol(Sym.PROCEDURE, yyline+1, yycolumn+1, new Token("procedure", yyline+1, yycolumn+1));}
{num}     { return new Symbol(Sym.NUM, yyline+1, yycolumn+1, new Token(new Integer(Integer.parseInt(yytext())), yyline+1, yycolumn+1)); }
"if"      { return new Symbol(Sym.IF, yyline+1, yycolumn+1, new Token("if", yyline+1, yycolumn+1)); }
"then"    { return new Symbol(Sym.THEN, yyline+1, yycolumn+1, new Token("then", yyline+1, yycolumn+1)); }
"else"    { return new Symbol(Sym.ELSE, yyline+1, yycolumn+1, new Token("else", yyline+1, yycolumn+1)); }
"fi"      { return new Symbol(Sym.FI, yyline+1, yycolumn+1, new Token("fi", yyline+1, yycolumn+1)); }
"from"    { return new Symbol(Sym.FROM, yyline+1, yycolumn+1, new Token("from", yyline+1, yycolumn+1)); }
"do"      { return new Symbol(Sym.DO, yyline+1, yycolumn+1, new Token("do", yyline+1, yycolumn+1)); }
"loop"    { return new Symbol(Sym.LOOP, yyline+1, yycolumn+1, new Token("loop", yyline+1, yycolumn+1)); }
"until"   { return new Symbol(Sym.UNTIL, yyline+1, yycolumn+1, new Token("until", yyline+1, yycolumn+1)); }
"call"    { return new Symbol(Sym.CALL, yyline+1, yycolumn+1, new Token("call", yyline+1, yycolumn+1)); }
"uncall"  { return new Symbol(Sym.UNCALL, yyline+1, yycolumn+1, new Token("uncall", yyline+1, yycolumn+1)); }
"read"    { return new Symbol(Sym.READ, yyline+1, yycolumn+1, new Token("read", yyline+1, yycolumn+1)); }
"write"   { return new Symbol(Sym.WRITE, yyline+1, yycolumn+1, new Token("write", yyline+1, yycolumn+1)); }
"+="      { return new Symbol(Sym.PE, yyline+1, yycolumn+1, new Token("+=", yyline+1, yycolumn+1)); }
"-="      { return new Symbol(Sym.ME, yyline+1, yycolumn+1, new Token("-=", yyline+1, yycolumn+1)); }
"!="      { return new Symbol(Sym.XE, yyline+1, yycolumn+1, new Token("!=", yyline+1, yycolumn+1)); }
":"       { return new Symbol(Sym.SWAP, yyline+1, yycolumn+1, new Token(":", yyline+1, yycolumn+1)); }
"("       { return new Symbol(Sym.LEFTPAREN, yyline+1, yycolumn+1, new Token("(", yyline+1, yycolumn+1)); }
")"       { return new Symbol(Sym.RIGHTPAREN, yyline+1, yycolumn+1, new Token(")", yyline+1, yycolumn+1)); }
"["       { return new Symbol(Sym.LEFTBRACKET, yyline+1, yycolumn+1, new Token("[", yyline+1, yycolumn+1)); }
"]"       { return new Symbol(Sym.RIGHTBRACKET, yyline+1, yycolumn+1, new Token("]", yyline+1, yycolumn+1)); }
"+"       { return new Symbol(Sym.PLUS, yyline+1, yycolumn+1, new Token("+", yyline+1, yycolumn+1)); }
"-"       { return new Symbol(Sym.MINUS, yyline+1, yycolumn+1, new Token("-", yyline+1, yycolumn+1)); }
"!"       { return new Symbol(Sym.XOR, yyline+1, yycolumn+1, new Token("!", yyline+1, yycolumn+1)); }
"<"       { return new Symbol(Sym.LT, yyline+1, yycolumn+1, new Token("<", yyline+1, yycolumn+1)); }
">"       { return new Symbol(Sym.GT, yyline+1, yycolumn+1, new Token(">", yyline+1, yycolumn+1)); }
"&"       { return new Symbol(Sym.AND, yyline+1, yycolumn+1, new Token("&", yyline+1, yycolumn+1)); }
"|"       { return new Symbol(Sym.OR, yyline+1, yycolumn+1, new Token("|", yyline+1, yycolumn+1)); }
"="       { return new Symbol(Sym.EQ, yyline+1, yycolumn+1, new Token("=", yyline+1, yycolumn+1)); }
"#"       { return new Symbol(Sym.NOTEQ, yyline+1, yycolumn+1, new Token("#", yyline+1, yycolumn+1)); }
"<="      { return new Symbol(Sym.LEQT, yyline+1, yycolumn+1, new Token("<=", yyline+1, yycolumn+1)); }
">="      { return new Symbol(Sym.GEQT, yyline+1, yycolumn+1, new Token(">=", yyline+1, yycolumn+1)); }
"*"       { return new Symbol(Sym.MULT, yyline+1, yycolumn+1, new Token("*", yyline+1, yycolumn+1)); }
"/"       { return new Symbol(Sym.DIV, yyline+1, yycolumn+1, new Token("/", yyline+1, yycolumn+1)); }
"\\"      { return new Symbol(Sym.MOD, yyline+1, yycolumn+1, new Token("\\", yyline+1, yycolumn+1)); }
"~"       { return new Symbol(Sym.NOT, yyline+1, yycolumn+1, new Token("~", yyline+1, yycolumn+1)); }

{id}      { return new Symbol(Sym.IDENT, yyline+1, yycolumn+1, new Token(yytext(), yyline+1, yycolumn+1)); }
{comment} { }
{brancos} { /*Do nothing*/ }

//[^] { throw new RuntimeException("Caractere inválido " + yytext() + " na linha " + yyline + ", coluna " +yycolumn); }
[^] { return new Symbol(Sym.ILLEGAL_CHAR, yyline+1, yycolumn+1, new Token(yytext(),yyline+1,yycolumn+1));  }
<<EOF>>  { return new Symbol(Sym.EOF, yyline+1, yycolumn+1, new Token("End of file", yyline+1, yycolumn+1)); }