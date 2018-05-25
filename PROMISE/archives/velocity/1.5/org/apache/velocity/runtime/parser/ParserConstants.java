package org.apache.velocity.runtime.parser;

public interface ParserConstants {

  int EOF = 0;
  int LBRACKET = 1;
  int RBRACKET = 2;
  int COMMA = 3;
  int DOUBLEDOT = 4;
  int COLON = 5;
  int LEFT_CURLEY = 6;
  int RIGHT_CURLEY = 7;
  int LPAREN = 8;
  int RPAREN = 9;
  int REFMOD2_RPAREN = 10;
  int ESCAPE_DIRECTIVE = 11;
  int SET_DIRECTIVE = 12;
  int DOLLAR = 13;
  int DOLLARBANG = 14;
  int HASH = 17;
  int SINGLE_LINE_COMMENT_START = 18;
  int DOUBLE_ESCAPE = 19;
  int ESCAPE = 20;
  int TEXT = 21;
  int SINGLE_LINE_COMMENT = 22;
  int FORMAL_COMMENT = 23;
  int MULTI_LINE_COMMENT = 24;
  int WHITESPACE = 26;
  int STRING_LITERAL = 27;
  int TRUE = 28;
  int FALSE = 29;
  int NEWLINE = 30;
  int MINUS = 31;
  int PLUS = 32;
  int MULTIPLY = 33;
  int DIVIDE = 34;
  int MODULUS = 35;
  int LOGICAL_AND = 36;
  int LOGICAL_OR = 37;
  int LOGICAL_LT = 38;
  int LOGICAL_LE = 39;
  int LOGICAL_GT = 40;
  int LOGICAL_GE = 41;
  int LOGICAL_EQUALS = 42;
  int LOGICAL_NOT_EQUALS = 43;
  int LOGICAL_NOT = 44;
  int EQUALS = 45;
  int END = 46;
  int IF_DIRECTIVE = 47;
  int ELSEIF_DIRECTIVE = 48;
  int ELSE_DIRECTIVE = 49;
  int STOP_DIRECTIVE = 50;
  int DIGIT = 51;
  int INTEGER_LITERAL = 52;
  int FLOATING_POINT_LITERAL = 53;
  int EXPONENT = 54;
  int LETTER = 55;
  int DIRECTIVE_CHAR = 56;
  int WORD = 57;
  int BRACKETED_WORD = 58;
  int ALPHA_CHAR = 59;
  int ALPHANUM_CHAR = 60;
  int IDENTIFIER_CHAR = 61;
  int IDENTIFIER = 62;
  int DOT = 63;
  int LCURLY = 64;
  int RCURLY = 65;
  int REFERENCE_TERMINATOR = 66;
  int DIRECTIVE_TERMINATOR = 67;

  int DIRECTIVE = 0;
  int REFMOD2 = 1;
  int REFMODIFIER = 2;
  int DEFAULT = 3;
  int PRE_DIRECTIVE = 4;
  int REFERENCE = 5;
  int IN_MULTI_LINE_COMMENT = 6;
  int IN_FORMAL_COMMENT = 7;
  int IN_SINGLE_LINE_COMMENT = 8;

  String[] tokenImage = {
    "<EOF>",
    "\"[\"",
    "\"]\"",
    "\",\"",
    "\"..\"",
    "\":\"",
    "\"{\"",
    "\"}\"",
    "\"(\"",
    "<RPAREN>",
    "\")\"",
    "<ESCAPE_DIRECTIVE>",
    "<SET_DIRECTIVE>",
    "<DOLLAR>",
    "<DOLLARBANG>",
    "<token of kind 15>",
    "\"#*\"",
    "\"#\"",
    "\"##\"",
    "\"\\\\\\\\\"",
    "\"\\\\\"",
    "<TEXT>",
    "<SINGLE_LINE_COMMENT>",
    "\"*#\"",
    "\"*#\"",
    "<token of kind 25>",
    "<WHITESPACE>",
    "<STRING_LITERAL>",
    "\"true\"",
    "\"false\"",
    "<NEWLINE>",
    "\"-\"",
    "\"+\"",
    "\"*\"",
    "\"/\"",
    "\"%\"",
    "<LOGICAL_AND>",
    "<LOGICAL_OR>",
    "<LOGICAL_LT>",
    "<LOGICAL_LE>",
    "<LOGICAL_GT>",
    "<LOGICAL_GE>",
    "<LOGICAL_EQUALS>",
    "<LOGICAL_NOT_EQUALS>",
    "<LOGICAL_NOT>",
    "\"=\"",
    "<END>",
    "<IF_DIRECTIVE>",
    "<ELSEIF_DIRECTIVE>",
    "<ELSE_DIRECTIVE>",
    "<STOP_DIRECTIVE>",
    "<DIGIT>",
    "<INTEGER_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "<LETTER>",
    "<DIRECTIVE_CHAR>",
    "<WORD>",
    "<BRACKETED_WORD>",
    "<ALPHA_CHAR>",
    "<ALPHANUM_CHAR>",
    "<IDENTIFIER_CHAR>",
    "<IDENTIFIER>",
    "<DOT>",
    "\"{\"",
    "\"}\"",
    "<REFERENCE_TERMINATOR>",
    "<DIRECTIVE_TERMINATOR>",
  };

}