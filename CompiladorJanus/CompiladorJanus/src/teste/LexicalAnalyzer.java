/* The following code was generated by JFlex 1.6.1 */

package teste;

import java_cup.runtime.*;
import newpackage.*;



/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.6.1
 * from the specification file <tt>janus.lex</tt>
 */
public class LexicalAnalyzer implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\3\1\5\2\0\1\4\22\0\1\3\1\34\1\0\1\46"+
    "\2\0\1\44\1\0\1\36\1\37\1\47\1\31\1\0\1\33\1\0"+
    "\1\50\1\1\11\2\1\35\1\7\1\42\1\32\1\43\2\0\32\6"+
    "\1\40\1\51\1\41\3\0\1\27\1\6\1\13\1\15\1\14\1\20"+
    "\1\6\1\22\1\17\2\6\1\24\1\26\1\23\1\12\1\10\1\6"+
    "\1\11\1\25\1\21\1\16\1\6\1\30\3\6\1\0\1\45\1\0"+
    "\1\52\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uff91\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\2\2\1\3\1\4\1\5\13\4\1\6"+
    "\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1\16"+
    "\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26"+
    "\1\27\2\5\4\4\1\30\1\4\1\31\1\4\1\32"+
    "\3\4\1\33\1\34\1\35\1\36\1\37\13\4\1\40"+
    "\1\41\1\42\2\4\1\43\1\44\1\45\3\4\1\46"+
    "\1\47\1\4\1\50\2\4\1\51";

  private static int [] zzUnpackAction() {
    int [] result = new int[84];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\53\0\53\0\126\0\53\0\201\0\254\0\327"+
    "\0\u0102\0\u012d\0\u0158\0\u0183\0\u01ae\0\u01d9\0\u0204\0\u022f"+
    "\0\u025a\0\u0285\0\u02b0\0\53\0\u02db\0\u0306\0\53\0\53"+
    "\0\53\0\53\0\53\0\u0331\0\u035c\0\53\0\53\0\53"+
    "\0\53\0\53\0\53\0\53\0\u0387\0\53\0\u03b2\0\u03dd"+
    "\0\u0408\0\u0433\0\201\0\u045e\0\201\0\u0489\0\201\0\u04b4"+
    "\0\u04df\0\u050a\0\53\0\53\0\53\0\53\0\53\0\u0535"+
    "\0\u0560\0\u058b\0\u05b6\0\u05e1\0\u060c\0\u0637\0\u0662\0\u068d"+
    "\0\u06b8\0\u06e3\0\201\0\201\0\201\0\u070e\0\u0739\0\201"+
    "\0\201\0\201\0\u0764\0\u078f\0\u07ba\0\201\0\201\0\u07e5"+
    "\0\201\0\u0810\0\u083b\0\201";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[84];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\1\4\1\5\1\2\1\5\1\6\1\7"+
    "\1\10\1\11\1\6\1\12\1\13\1\14\1\15\1\16"+
    "\1\17\1\20\2\6\1\21\3\6\1\22\1\23\1\24"+
    "\1\25\1\26\1\27\1\30\1\31\1\32\1\33\1\34"+
    "\1\35\1\36\1\37\1\40\1\41\1\42\1\43\1\44"+
    "\54\0\2\4\51\0\2\6\3\0\1\6\1\0\21\6"+
    "\22\0\4\7\1\45\1\46\45\7\1\0\2\6\3\0"+
    "\1\6\1\0\1\6\1\47\17\6\23\0\2\6\3\0"+
    "\1\6\1\0\4\6\1\50\14\6\23\0\2\6\3\0"+
    "\1\6\1\0\17\6\1\51\1\6\23\0\2\6\3\0"+
    "\1\6\1\0\14\6\1\52\4\6\23\0\2\6\3\0"+
    "\1\6\1\0\2\6\1\53\16\6\23\0\2\6\3\0"+
    "\1\6\1\0\13\6\1\54\5\6\23\0\2\6\3\0"+
    "\1\6\1\0\10\6\1\55\10\6\23\0\2\6\3\0"+
    "\1\6\1\0\1\6\1\56\5\6\1\57\11\6\23\0"+
    "\2\6\3\0\1\6\1\0\12\6\1\60\6\6\23\0"+
    "\2\6\3\0\1\6\1\0\2\6\1\61\16\6\23\0"+
    "\2\6\3\0\1\6\1\0\1\6\1\62\17\6\54\0"+
    "\1\63\52\0\1\64\52\0\1\65\52\0\1\66\52\0"+
    "\1\67\25\0\1\46\46\0\2\6\3\0\1\6\1\0"+
    "\2\6\1\70\16\6\23\0\2\6\3\0\1\6\1\0"+
    "\17\6\1\71\1\6\23\0\2\6\3\0\1\6\1\0"+
    "\14\6\1\72\4\6\23\0\2\6\3\0\1\6\1\0"+
    "\15\6\1\73\3\6\23\0\2\6\3\0\1\6\1\0"+
    "\3\6\1\74\5\6\1\75\7\6\23\0\2\6\3\0"+
    "\1\6\1\0\2\6\1\76\16\6\23\0\2\6\3\0"+
    "\1\6\1\0\4\6\1\77\14\6\23\0\2\6\3\0"+
    "\1\6\1\0\2\6\1\100\16\6\23\0\2\6\3\0"+
    "\1\6\1\0\7\6\1\101\11\6\23\0\2\6\3\0"+
    "\1\6\1\0\3\6\1\102\15\6\23\0\2\6\3\0"+
    "\1\6\1\0\5\6\1\103\13\6\23\0\2\6\3\0"+
    "\1\6\1\0\14\6\1\104\4\6\23\0\2\6\3\0"+
    "\1\6\1\0\4\6\1\105\14\6\23\0\2\6\3\0"+
    "\1\6\1\0\17\6\1\106\1\6\23\0\2\6\3\0"+
    "\1\6\1\0\7\6\1\107\11\6\23\0\2\6\3\0"+
    "\1\6\1\0\16\6\1\110\2\6\23\0\2\6\3\0"+
    "\1\6\1\0\13\6\1\111\5\6\23\0\2\6\3\0"+
    "\1\6\1\0\1\112\20\6\23\0\2\6\3\0\1\6"+
    "\1\0\11\6\1\113\7\6\23\0\2\6\3\0\1\6"+
    "\1\0\4\6\1\114\14\6\23\0\2\6\3\0\1\6"+
    "\1\0\14\6\1\115\4\6\23\0\2\6\3\0\1\6"+
    "\1\0\14\6\1\116\4\6\23\0\2\6\3\0\1\6"+
    "\1\0\4\6\1\117\14\6\23\0\2\6\3\0\1\6"+
    "\1\0\5\6\1\120\13\6\23\0\2\6\3\0\1\6"+
    "\1\0\14\6\1\121\4\6\23\0\2\6\3\0\1\6"+
    "\1\0\6\6\1\122\12\6\23\0\2\6\3\0\1\6"+
    "\1\0\1\6\1\123\17\6\23\0\2\6\3\0\1\6"+
    "\1\0\4\6\1\124\14\6\22\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[2150];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\2\11\1\1\1\11\16\1\1\11\2\1\5\11"+
    "\2\1\7\11\1\1\1\11\14\1\5\11\35\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[84];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public LexicalAnalyzer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 156) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      throw new java.io.IOException("Reader returned 0 characters. See JFlex examples for workaround.");
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      /* If numRead == requested, we might have requested to few chars to
         encode a full Unicode character. We assume that a Reader would
         otherwise never return half characters. */
      if (numRead == requested) {
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
            zzDoEOF();
              {
                return new Symbol(Sym.EOF, yyline+1, yycolumn+1, new Token("End of file", yyline+1, yycolumn+1));
              }
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { return new Symbol(Sym.ILLEGAL_CHAR, yyline+1, yycolumn+1, new Token(yytext(),yyline+1,yycolumn+1));
            }
          case 42: break;
          case 2: 
            { return new Symbol(Sym.NUM, yyline+1, yycolumn+1, new Token(new Integer(Integer.parseInt(yytext())), yyline+1, yycolumn+1));
            }
          case 43: break;
          case 3: 
            { /*Do nothing*/
            }
          case 44: break;
          case 4: 
            { return new Symbol(Sym.IDENT, yyline+1, yycolumn+1, new Token(yytext(), yyline+1, yycolumn+1));
            }
          case 45: break;
          case 5: 
            { 
            }
          case 46: break;
          case 6: 
            { return new Symbol(Sym.PLUS, yyline+1, yycolumn+1, new Token("+", yyline+1, yycolumn+1));
            }
          case 47: break;
          case 7: 
            { return new Symbol(Sym.EQ, yyline+1, yycolumn+1, new Token("=", yyline+1, yycolumn+1));
            }
          case 48: break;
          case 8: 
            { return new Symbol(Sym.MINUS, yyline+1, yycolumn+1, new Token("-", yyline+1, yycolumn+1));
            }
          case 49: break;
          case 9: 
            { return new Symbol(Sym.XOR, yyline+1, yycolumn+1, new Token("!", yyline+1, yycolumn+1));
            }
          case 50: break;
          case 10: 
            { return new Symbol(Sym.SWAP, yyline+1, yycolumn+1, new Token(":", yyline+1, yycolumn+1));
            }
          case 51: break;
          case 11: 
            { return new Symbol(Sym.LEFTPAREN, yyline+1, yycolumn+1, new Token("(", yyline+1, yycolumn+1));
            }
          case 52: break;
          case 12: 
            { return new Symbol(Sym.RIGHTPAREN, yyline+1, yycolumn+1, new Token(")", yyline+1, yycolumn+1));
            }
          case 53: break;
          case 13: 
            { return new Symbol(Sym.LEFTBRACKET, yyline+1, yycolumn+1, new Token("[", yyline+1, yycolumn+1));
            }
          case 54: break;
          case 14: 
            { return new Symbol(Sym.RIGHTBRACKET, yyline+1, yycolumn+1, new Token("]", yyline+1, yycolumn+1));
            }
          case 55: break;
          case 15: 
            { return new Symbol(Sym.LT, yyline+1, yycolumn+1, new Token("<", yyline+1, yycolumn+1));
            }
          case 56: break;
          case 16: 
            { return new Symbol(Sym.GT, yyline+1, yycolumn+1, new Token(">", yyline+1, yycolumn+1));
            }
          case 57: break;
          case 17: 
            { return new Symbol(Sym.AND, yyline+1, yycolumn+1, new Token("&", yyline+1, yycolumn+1));
            }
          case 58: break;
          case 18: 
            { return new Symbol(Sym.OR, yyline+1, yycolumn+1, new Token("|", yyline+1, yycolumn+1));
            }
          case 59: break;
          case 19: 
            { return new Symbol(Sym.NOTEQ, yyline+1, yycolumn+1, new Token("#", yyline+1, yycolumn+1));
            }
          case 60: break;
          case 20: 
            { return new Symbol(Sym.MULT, yyline+1, yycolumn+1, new Token("*", yyline+1, yycolumn+1));
            }
          case 61: break;
          case 21: 
            { return new Symbol(Sym.DIV, yyline+1, yycolumn+1, new Token("/", yyline+1, yycolumn+1));
            }
          case 62: break;
          case 22: 
            { return new Symbol(Sym.MOD, yyline+1, yycolumn+1, new Token("\\", yyline+1, yycolumn+1));
            }
          case 63: break;
          case 23: 
            { return new Symbol(Sym.NOT, yyline+1, yycolumn+1, new Token("~", yyline+1, yycolumn+1));
            }
          case 64: break;
          case 24: 
            { return new Symbol(Sym.DO, yyline+1, yycolumn+1, new Token("do", yyline+1, yycolumn+1));
            }
          case 65: break;
          case 25: 
            { return new Symbol(Sym.IF, yyline+1, yycolumn+1, new Token("if", yyline+1, yycolumn+1));
            }
          case 66: break;
          case 26: 
            { return new Symbol(Sym.FI, yyline+1, yycolumn+1, new Token("fi", yyline+1, yycolumn+1));
            }
          case 67: break;
          case 27: 
            { return new Symbol(Sym.PE, yyline+1, yycolumn+1, new Token("+=", yyline+1, yycolumn+1));
            }
          case 68: break;
          case 28: 
            { return new Symbol(Sym.ME, yyline+1, yycolumn+1, new Token("-=", yyline+1, yycolumn+1));
            }
          case 69: break;
          case 29: 
            { return new Symbol(Sym.XE, yyline+1, yycolumn+1, new Token("!=", yyline+1, yycolumn+1));
            }
          case 70: break;
          case 30: 
            { return new Symbol(Sym.LEQT, yyline+1, yycolumn+1, new Token("<=", yyline+1, yycolumn+1));
            }
          case 71: break;
          case 31: 
            { return new Symbol(Sym.GEQT, yyline+1, yycolumn+1, new Token(">=", yyline+1, yycolumn+1));
            }
          case 72: break;
          case 32: 
            { return new Symbol(Sym.READ, yyline+1, yycolumn+1, new Token("read", yyline+1, yycolumn+1));
            }
          case 73: break;
          case 33: 
            { return new Symbol(Sym.CALL, yyline+1, yycolumn+1, new Token("call", yyline+1, yycolumn+1));
            }
          case 74: break;
          case 34: 
            { return new Symbol(Sym.ELSE, yyline+1, yycolumn+1, new Token("else", yyline+1, yycolumn+1));
            }
          case 75: break;
          case 35: 
            { return new Symbol(Sym.FROM, yyline+1, yycolumn+1, new Token("from", yyline+1, yycolumn+1));
            }
          case 76: break;
          case 36: 
            { return new Symbol(Sym.THEN, yyline+1, yycolumn+1, new Token("then", yyline+1, yycolumn+1));
            }
          case 77: break;
          case 37: 
            { return new Symbol(Sym.LOOP, yyline+1, yycolumn+1, new Token("loop", yyline+1, yycolumn+1));
            }
          case 78: break;
          case 38: 
            { return new Symbol(Sym.UNTIL, yyline+1, yycolumn+1, new Token("until", yyline+1, yycolumn+1));
            }
          case 79: break;
          case 39: 
            { return new Symbol(Sym.WRITE, yyline+1, yycolumn+1, new Token("write", yyline+1, yycolumn+1));
            }
          case 80: break;
          case 40: 
            { return new Symbol(Sym.UNCALL, yyline+1, yycolumn+1, new Token("uncall", yyline+1, yycolumn+1));
            }
          case 81: break;
          case 41: 
            { return new Symbol(Sym.PROCEDURE, yyline+1, yycolumn+1, new Token("procedure", yyline+1, yycolumn+1));
            }
          case 82: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }


}
