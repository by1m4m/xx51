/*      */ package com.google.common.base;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated=true)
/*      */ public abstract class CharMatcher
/*      */   implements Predicate<Character>
/*      */ {
/*      */   private static final int DISTINCT_CHARS = 65536;
/*      */   
/*      */   public static CharMatcher any()
/*      */   {
/*  118 */     return Any.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static CharMatcher none()
/*      */   {
/*  127 */     return None.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static CharMatcher whitespace()
/*      */   {
/*  145 */     return Whitespace.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static CharMatcher breakingWhitespace()
/*      */   {
/*  156 */     return BreakingWhitespace.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static CharMatcher ascii()
/*      */   {
/*  165 */     return Ascii.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static CharMatcher digit()
/*      */   {
/*  178 */     return Digit.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static CharMatcher javaDigit()
/*      */   {
/*  191 */     return JavaDigit.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static CharMatcher javaLetter()
/*      */   {
/*  204 */     return JavaLetter.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static CharMatcher javaLetterOrDigit()
/*      */   {
/*  216 */     return JavaLetterOrDigit.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static CharMatcher javaUpperCase()
/*      */   {
/*  229 */     return JavaUpperCase.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static CharMatcher javaLowerCase()
/*      */   {
/*  242 */     return JavaLowerCase.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static CharMatcher javaIsoControl()
/*      */   {
/*  254 */     return JavaIsoControl.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static CharMatcher invisible()
/*      */   {
/*  270 */     return Invisible.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static CharMatcher singleWidth()
/*      */   {
/*  288 */     return SingleWidth.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static CharMatcher is(char match)
/*      */   {
/*  295 */     return new Is(match);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static CharMatcher isNot(char match)
/*      */   {
/*  304 */     return new IsNot(match);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static CharMatcher anyOf(CharSequence sequence)
/*      */   {
/*  312 */     switch (sequence.length()) {
/*      */     case 0: 
/*  314 */       return none();
/*      */     case 1: 
/*  316 */       return is(sequence.charAt(0));
/*      */     case 2: 
/*  318 */       return isEither(sequence.charAt(0), sequence.charAt(1));
/*      */     }
/*      */     
/*      */     
/*  322 */     return new AnyOf(sequence);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static CharMatcher noneOf(CharSequence sequence)
/*      */   {
/*  331 */     return anyOf(sequence).negate();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static CharMatcher inRange(char startInclusive, char endInclusive)
/*      */   {
/*  342 */     return new InRange(startInclusive, endInclusive);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static CharMatcher forPredicate(Predicate<? super Character> predicate)
/*      */   {
/*  350 */     return (predicate instanceof CharMatcher) ? (CharMatcher)predicate : new ForPredicate(predicate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract boolean matches(char paramChar);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CharMatcher negate()
/*      */   {
/*  371 */     return new Negated(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public CharMatcher and(CharMatcher other)
/*      */   {
/*  378 */     return new And(this, other);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public CharMatcher or(CharMatcher other)
/*      */   {
/*  385 */     return new Or(this, other);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CharMatcher precomputed()
/*      */   {
/*  398 */     return Platform.precomputeCharMatcher(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   CharMatcher precomputedInternal()
/*      */   {
/*  415 */     BitSet table = new BitSet();
/*  416 */     setBits(table);
/*  417 */     int totalCharacters = table.cardinality();
/*  418 */     if (totalCharacters * 2 <= 65536) {
/*  419 */       return precomputedPositive(totalCharacters, table, toString());
/*      */     }
/*      */     
/*  422 */     table.flip(0, 65536);
/*  423 */     int negatedCharacters = 65536 - totalCharacters;
/*  424 */     String suffix = ".negate()";
/*  425 */     final String description = toString();
/*      */     
/*      */ 
/*  428 */     String negatedDescription = description + suffix;
/*      */     
/*  430 */     new NegatedFastMatcher(
/*  431 */       precomputedPositive(negatedCharacters, table, negatedDescription))
/*      */       {
/*      */         public String toString()
/*      */         {
/*  434 */           return description;
/*      */         }
/*      */       };
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     @GwtIncompatible
/*      */     private static CharMatcher precomputedPositive(int totalCharacters, BitSet table, String description)
/*      */     {
/*  446 */       switch (totalCharacters) {
/*      */       case 0: 
/*  448 */         return none();
/*      */       case 1: 
/*  450 */         return is((char)table.nextSetBit(0));
/*      */       case 2: 
/*  452 */         char c1 = (char)table.nextSetBit(0);
/*  453 */         char c2 = (char)table.nextSetBit(c1 + '\001');
/*  454 */         return isEither(c1, c2);
/*      */       }
/*  456 */       return isSmall(totalCharacters, table.length()) ? 
/*  457 */         SmallCharMatcher.from(table, description) : new BitSetMatcher(table, description, null);
/*      */     }
/*      */     
/*      */ 
/*      */     @GwtIncompatible
/*      */     private static boolean isSmall(int totalCharacters, int tableLength)
/*      */     {
/*  464 */       return (totalCharacters <= 1023) && (tableLength > totalCharacters * 4 * 16);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table)
/*      */     {
/*  472 */       for (int c = 65535; c >= 0; c--) {
/*  473 */         if (matches((char)c)) {
/*  474 */           table.set(c);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean matchesAnyOf(CharSequence sequence)
/*      */     {
/*  493 */       return !matchesNoneOf(sequence);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean matchesAllOf(CharSequence sequence)
/*      */     {
/*  507 */       for (int i = sequence.length() - 1; i >= 0; i--) {
/*  508 */         if (!matches(sequence.charAt(i))) {
/*  509 */           return false;
/*      */         }
/*      */       }
/*  512 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean matchesNoneOf(CharSequence sequence)
/*      */     {
/*  527 */       return indexIn(sequence) == -1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int indexIn(CharSequence sequence)
/*      */     {
/*  541 */       return indexIn(sequence, 0);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int indexIn(CharSequence sequence, int start)
/*      */     {
/*  560 */       int length = sequence.length();
/*  561 */       Preconditions.checkPositionIndex(start, length);
/*  562 */       for (int i = start; i < length; i++) {
/*  563 */         if (matches(sequence.charAt(i))) {
/*  564 */           return i;
/*      */         }
/*      */       }
/*  567 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int lastIndexIn(CharSequence sequence)
/*      */     {
/*  581 */       for (int i = sequence.length() - 1; i >= 0; i--) {
/*  582 */         if (matches(sequence.charAt(i))) {
/*  583 */           return i;
/*      */         }
/*      */       }
/*  586 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int countIn(CharSequence sequence)
/*      */     {
/*  595 */       int count = 0;
/*  596 */       for (int i = 0; i < sequence.length(); i++) {
/*  597 */         if (matches(sequence.charAt(i))) {
/*  598 */           count++;
/*      */         }
/*      */       }
/*  601 */       return count;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String removeFrom(CharSequence sequence)
/*      */     {
/*  615 */       String string = sequence.toString();
/*  616 */       int pos = indexIn(string);
/*  617 */       if (pos == -1) {
/*  618 */         return string;
/*      */       }
/*      */       
/*  621 */       char[] chars = string.toCharArray();
/*  622 */       int spread = 1;
/*      */       
/*      */ 
/*      */       for (;;)
/*      */       {
/*  627 */         pos++;
/*      */         for (;;) {
/*  629 */           if (pos == chars.length) {
/*      */             break label79;
/*      */           }
/*  632 */           if (matches(chars[pos])) {
/*      */             break;
/*      */           }
/*  635 */           chars[(pos - spread)] = chars[pos];
/*  636 */           pos++;
/*      */         }
/*  638 */         spread++; }
/*      */       label79:
/*  640 */       return new String(chars, 0, pos - spread);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String retainFrom(CharSequence sequence)
/*      */     {
/*  654 */       return negate().removeFrom(sequence);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String replaceFrom(CharSequence sequence, char replacement)
/*      */     {
/*  677 */       String string = sequence.toString();
/*  678 */       int pos = indexIn(string);
/*  679 */       if (pos == -1) {
/*  680 */         return string;
/*      */       }
/*  682 */       char[] chars = string.toCharArray();
/*  683 */       chars[pos] = replacement;
/*  684 */       for (int i = pos + 1; i < chars.length; i++) {
/*  685 */         if (matches(chars[i])) {
/*  686 */           chars[i] = replacement;
/*      */         }
/*      */       }
/*  689 */       return new String(chars);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String replaceFrom(CharSequence sequence, CharSequence replacement)
/*      */     {
/*  711 */       int replacementLen = replacement.length();
/*  712 */       if (replacementLen == 0) {
/*  713 */         return removeFrom(sequence);
/*      */       }
/*  715 */       if (replacementLen == 1) {
/*  716 */         return replaceFrom(sequence, replacement.charAt(0));
/*      */       }
/*      */       
/*  719 */       String string = sequence.toString();
/*  720 */       int pos = indexIn(string);
/*  721 */       if (pos == -1) {
/*  722 */         return string;
/*      */       }
/*      */       
/*  725 */       int len = string.length();
/*  726 */       StringBuilder buf = new StringBuilder(len * 3 / 2 + 16);
/*      */       
/*  728 */       int oldpos = 0;
/*      */       do {
/*  730 */         buf.append(string, oldpos, pos);
/*  731 */         buf.append(replacement);
/*  732 */         oldpos = pos + 1;
/*  733 */         pos = indexIn(string, oldpos);
/*  734 */       } while (pos != -1);
/*      */       
/*  736 */       buf.append(string, oldpos, len);
/*  737 */       return buf.toString();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String trimFrom(CharSequence sequence)
/*      */     {
/*  759 */       int len = sequence.length();
/*      */       
/*      */ 
/*      */ 
/*  763 */       for (int first = 0; first < len; first++) {
/*  764 */         if (!matches(sequence.charAt(first))) {
/*      */           break;
/*      */         }
/*      */       }
/*  768 */       for (int last = len - 1; last > first; last--) {
/*  769 */         if (!matches(sequence.charAt(last))) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/*  774 */       return sequence.subSequence(first, last + 1).toString();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String trimLeadingFrom(CharSequence sequence)
/*      */     {
/*  788 */       int len = sequence.length();
/*  789 */       for (int first = 0; first < len; first++) {
/*  790 */         if (!matches(sequence.charAt(first))) {
/*  791 */           return sequence.subSequence(first, len).toString();
/*      */         }
/*      */       }
/*  794 */       return "";
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String trimTrailingFrom(CharSequence sequence)
/*      */     {
/*  808 */       int len = sequence.length();
/*  809 */       for (int last = len - 1; last >= 0; last--) {
/*  810 */         if (!matches(sequence.charAt(last))) {
/*  811 */           return sequence.subSequence(0, last + 1).toString();
/*      */         }
/*      */       }
/*  814 */       return "";
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String collapseFrom(CharSequence sequence, char replacement)
/*      */     {
/*  838 */       int len = sequence.length();
/*  839 */       for (int i = 0; i < len; i++) {
/*  840 */         char c = sequence.charAt(i);
/*  841 */         if (matches(c)) {
/*  842 */           if ((c == replacement) && ((i == len - 1) || (!matches(sequence.charAt(i + 1)))))
/*      */           {
/*  844 */             i++;
/*      */           } else {
/*  846 */             StringBuilder builder = new StringBuilder(len).append(sequence, 0, i).append(replacement);
/*  847 */             return finishCollapseFrom(sequence, i + 1, len, replacement, builder, true);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  852 */       return sequence.toString();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String trimAndCollapseFrom(CharSequence sequence, char replacement)
/*      */     {
/*  862 */       int len = sequence.length();
/*  863 */       int first = 0;
/*  864 */       int last = len - 1;
/*      */       
/*  866 */       while ((first < len) && (matches(sequence.charAt(first)))) {
/*  867 */         first++;
/*      */       }
/*      */       
/*  870 */       while ((last > first) && (matches(sequence.charAt(last)))) {
/*  871 */         last--;
/*      */       }
/*      */       
/*  874 */       return (first == 0) && (last == len - 1) ? 
/*  875 */         collapseFrom(sequence, replacement) : 
/*  876 */         finishCollapseFrom(sequence, first, last + 1, replacement, new StringBuilder(last + 1 - first), false);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private String finishCollapseFrom(CharSequence sequence, int start, int end, char replacement, StringBuilder builder, boolean inMatchingGroup)
/*      */     {
/*  887 */       for (int i = start; i < end; i++) {
/*  888 */         char c = sequence.charAt(i);
/*  889 */         if (matches(c)) {
/*  890 */           if (!inMatchingGroup) {
/*  891 */             builder.append(replacement);
/*  892 */             inMatchingGroup = true;
/*      */           }
/*      */         } else {
/*  895 */           builder.append(c);
/*  896 */           inMatchingGroup = false;
/*      */         }
/*      */       }
/*  899 */       return builder.toString();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     @Deprecated
/*      */     public boolean apply(Character character)
/*      */     {
/*  909 */       return matches(character.charValue());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String toString()
/*      */     {
/*  918 */       return super.toString();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private static String showCharacter(char c)
/*      */     {
/*  926 */       String hex = "0123456789ABCDEF";
/*  927 */       char[] tmp = { '\\', 'u', '\000', '\000', '\000', '\000' };
/*  928 */       for (int i = 0; i < 4; i++) {
/*  929 */         tmp[(5 - i)] = hex.charAt(c & 0xF);
/*  930 */         c = (char)(c >> '\004');
/*      */       }
/*  932 */       return String.copyValueOf(tmp);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     static abstract class FastMatcher
/*      */       extends CharMatcher
/*      */     {
/*      */       public final CharMatcher precomputed()
/*      */       {
/*  942 */         return this;
/*      */       }
/*      */       
/*      */       public CharMatcher negate()
/*      */       {
/*  947 */         return new CharMatcher.NegatedFastMatcher(this);
/*      */       }
/*      */     }
/*      */     
/*      */     static abstract class NamedFastMatcher extends CharMatcher.FastMatcher
/*      */     {
/*      */       private final String description;
/*      */       
/*      */       NamedFastMatcher(String description)
/*      */       {
/*  957 */         this.description = ((String)Preconditions.checkNotNull(description));
/*      */       }
/*      */       
/*      */       public final String toString()
/*      */       {
/*  962 */         return this.description;
/*      */       }
/*      */     }
/*      */     
/*      */     static class NegatedFastMatcher extends CharMatcher.Negated
/*      */     {
/*      */       NegatedFastMatcher(CharMatcher original)
/*      */       {
/*  970 */         super();
/*      */       }
/*      */       
/*      */       public final CharMatcher precomputed()
/*      */       {
/*  975 */         return this;
/*      */       }
/*      */     }
/*      */     
/*      */     @GwtIncompatible
/*      */     private static final class BitSetMatcher extends CharMatcher.NamedFastMatcher
/*      */     {
/*      */       private final BitSet table;
/*      */       
/*      */       private BitSetMatcher(BitSet table, String description)
/*      */       {
/*  986 */         super();
/*  987 */         if (table.length() + 64 < table.size()) {
/*  988 */           table = (BitSet)table.clone();
/*      */         }
/*      */         
/*  991 */         this.table = table;
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/*  996 */         return this.table.get(c);
/*      */       }
/*      */       
/*      */       void setBits(BitSet bitSet)
/*      */       {
/* 1001 */         bitSet.or(this.table);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private static final class Any
/*      */       extends CharMatcher.NamedFastMatcher
/*      */     {
/* 1010 */       static final Any INSTANCE = new Any();
/*      */       
/*      */       private Any() {
/* 1013 */         super();
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1018 */         return true;
/*      */       }
/*      */       
/*      */       public int indexIn(CharSequence sequence)
/*      */       {
/* 1023 */         return sequence.length() == 0 ? -1 : 0;
/*      */       }
/*      */       
/*      */       public int indexIn(CharSequence sequence, int start)
/*      */       {
/* 1028 */         int length = sequence.length();
/* 1029 */         Preconditions.checkPositionIndex(start, length);
/* 1030 */         return start == length ? -1 : start;
/*      */       }
/*      */       
/*      */       public int lastIndexIn(CharSequence sequence)
/*      */       {
/* 1035 */         return sequence.length() - 1;
/*      */       }
/*      */       
/*      */       public boolean matchesAllOf(CharSequence sequence)
/*      */       {
/* 1040 */         Preconditions.checkNotNull(sequence);
/* 1041 */         return true;
/*      */       }
/*      */       
/*      */       public boolean matchesNoneOf(CharSequence sequence)
/*      */       {
/* 1046 */         return sequence.length() == 0;
/*      */       }
/*      */       
/*      */       public String removeFrom(CharSequence sequence)
/*      */       {
/* 1051 */         Preconditions.checkNotNull(sequence);
/* 1052 */         return "";
/*      */       }
/*      */       
/*      */       public String replaceFrom(CharSequence sequence, char replacement)
/*      */       {
/* 1057 */         char[] array = new char[sequence.length()];
/* 1058 */         Arrays.fill(array, replacement);
/* 1059 */         return new String(array);
/*      */       }
/*      */       
/*      */       public String replaceFrom(CharSequence sequence, CharSequence replacement)
/*      */       {
/* 1064 */         StringBuilder result = new StringBuilder(sequence.length() * replacement.length());
/* 1065 */         for (int i = 0; i < sequence.length(); i++) {
/* 1066 */           result.append(replacement);
/*      */         }
/* 1068 */         return result.toString();
/*      */       }
/*      */       
/*      */       public String collapseFrom(CharSequence sequence, char replacement)
/*      */       {
/* 1073 */         return sequence.length() == 0 ? "" : String.valueOf(replacement);
/*      */       }
/*      */       
/*      */       public String trimFrom(CharSequence sequence)
/*      */       {
/* 1078 */         Preconditions.checkNotNull(sequence);
/* 1079 */         return "";
/*      */       }
/*      */       
/*      */       public int countIn(CharSequence sequence)
/*      */       {
/* 1084 */         return sequence.length();
/*      */       }
/*      */       
/*      */       public CharMatcher and(CharMatcher other)
/*      */       {
/* 1089 */         return (CharMatcher)Preconditions.checkNotNull(other);
/*      */       }
/*      */       
/*      */       public CharMatcher or(CharMatcher other)
/*      */       {
/* 1094 */         Preconditions.checkNotNull(other);
/* 1095 */         return this;
/*      */       }
/*      */       
/*      */       public CharMatcher negate()
/*      */       {
/* 1100 */         return none();
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class None
/*      */       extends CharMatcher.NamedFastMatcher
/*      */     {
/* 1107 */       static final None INSTANCE = new None();
/*      */       
/*      */       private None() {
/* 1110 */         super();
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1115 */         return false;
/*      */       }
/*      */       
/*      */       public int indexIn(CharSequence sequence)
/*      */       {
/* 1120 */         Preconditions.checkNotNull(sequence);
/* 1121 */         return -1;
/*      */       }
/*      */       
/*      */       public int indexIn(CharSequence sequence, int start)
/*      */       {
/* 1126 */         int length = sequence.length();
/* 1127 */         Preconditions.checkPositionIndex(start, length);
/* 1128 */         return -1;
/*      */       }
/*      */       
/*      */       public int lastIndexIn(CharSequence sequence)
/*      */       {
/* 1133 */         Preconditions.checkNotNull(sequence);
/* 1134 */         return -1;
/*      */       }
/*      */       
/*      */       public boolean matchesAllOf(CharSequence sequence)
/*      */       {
/* 1139 */         return sequence.length() == 0;
/*      */       }
/*      */       
/*      */       public boolean matchesNoneOf(CharSequence sequence)
/*      */       {
/* 1144 */         Preconditions.checkNotNull(sequence);
/* 1145 */         return true;
/*      */       }
/*      */       
/*      */       public String removeFrom(CharSequence sequence)
/*      */       {
/* 1150 */         return sequence.toString();
/*      */       }
/*      */       
/*      */       public String replaceFrom(CharSequence sequence, char replacement)
/*      */       {
/* 1155 */         return sequence.toString();
/*      */       }
/*      */       
/*      */       public String replaceFrom(CharSequence sequence, CharSequence replacement)
/*      */       {
/* 1160 */         Preconditions.checkNotNull(replacement);
/* 1161 */         return sequence.toString();
/*      */       }
/*      */       
/*      */       public String collapseFrom(CharSequence sequence, char replacement)
/*      */       {
/* 1166 */         return sequence.toString();
/*      */       }
/*      */       
/*      */       public String trimFrom(CharSequence sequence)
/*      */       {
/* 1171 */         return sequence.toString();
/*      */       }
/*      */       
/*      */       public String trimLeadingFrom(CharSequence sequence)
/*      */       {
/* 1176 */         return sequence.toString();
/*      */       }
/*      */       
/*      */       public String trimTrailingFrom(CharSequence sequence)
/*      */       {
/* 1181 */         return sequence.toString();
/*      */       }
/*      */       
/*      */       public int countIn(CharSequence sequence)
/*      */       {
/* 1186 */         Preconditions.checkNotNull(sequence);
/* 1187 */         return 0;
/*      */       }
/*      */       
/*      */       public CharMatcher and(CharMatcher other)
/*      */       {
/* 1192 */         Preconditions.checkNotNull(other);
/* 1193 */         return this;
/*      */       }
/*      */       
/*      */       public CharMatcher or(CharMatcher other)
/*      */       {
/* 1198 */         return (CharMatcher)Preconditions.checkNotNull(other);
/*      */       }
/*      */       
/*      */       public CharMatcher negate()
/*      */       {
/* 1203 */         return any();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     @VisibleForTesting
/*      */     static final class Whitespace
/*      */       extends CharMatcher.NamedFastMatcher
/*      */     {
/*      */       static final String TABLE = " 　\r   　 \013　   　 \t     \f 　 　　 \n 　";
/*      */       
/*      */       static final int MULTIPLIER = 1682554634;
/*      */       
/* 1217 */       static final int SHIFT = Integer.numberOfLeadingZeros(" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".length() - 1);
/*      */       
/* 1219 */       static final Whitespace INSTANCE = new Whitespace();
/*      */       
/*      */       Whitespace() {
/* 1222 */         super();
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1227 */         return " 　\r   　 \013　   　 \t     \f 　 　　 \n 　".charAt(1682554634 * c >>> SHIFT) == c;
/*      */       }
/*      */       
/*      */       @GwtIncompatible
/*      */       void setBits(BitSet table)
/*      */       {
/* 1233 */         for (int i = 0; i < " 　\r   　 \013　   　 \t     \f 　 　　 \n 　".length(); i++) {
/* 1234 */           table.set(" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".charAt(i));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class BreakingWhitespace
/*      */       extends CharMatcher
/*      */     {
/* 1242 */       static final CharMatcher INSTANCE = new BreakingWhitespace();
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1246 */         switch (c) {
/*      */         case '\t': 
/*      */         case '\n': 
/*      */         case '\013': 
/*      */         case '\f': 
/*      */         case '\r': 
/*      */         case ' ': 
/*      */         case '': 
/*      */         case ' ': 
/*      */         case ' ': 
/*      */         case ' ': 
/*      */         case ' ': 
/*      */         case '　': 
/* 1259 */           return true;
/*      */         case ' ': 
/* 1261 */           return false;
/*      */         }
/* 1263 */         return (c >= ' ') && (c <= ' ');
/*      */       }
/*      */       
/*      */ 
/*      */       public String toString()
/*      */       {
/* 1269 */         return "CharMatcher.breakingWhitespace()";
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class Ascii
/*      */       extends CharMatcher.NamedFastMatcher
/*      */     {
/* 1276 */       static final Ascii INSTANCE = new Ascii();
/*      */       
/*      */       Ascii() {
/* 1279 */         super();
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1284 */         return c <= '';
/*      */       }
/*      */     }
/*      */     
/*      */     private static class RangesMatcher extends CharMatcher
/*      */     {
/*      */       private final String description;
/*      */       private final char[] rangeStarts;
/*      */       private final char[] rangeEnds;
/*      */       
/*      */       RangesMatcher(String description, char[] rangeStarts, char[] rangeEnds)
/*      */       {
/* 1296 */         this.description = description;
/* 1297 */         this.rangeStarts = rangeStarts;
/* 1298 */         this.rangeEnds = rangeEnds;
/* 1299 */         Preconditions.checkArgument(rangeStarts.length == rangeEnds.length);
/* 1300 */         for (int i = 0; i < rangeStarts.length; i++) {
/* 1301 */           Preconditions.checkArgument(rangeStarts[i] <= rangeEnds[i]);
/* 1302 */           if (i + 1 < rangeStarts.length) {
/* 1303 */             Preconditions.checkArgument(rangeEnds[i] < rangeStarts[(i + 1)]);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1310 */         int index = Arrays.binarySearch(this.rangeStarts, c);
/* 1311 */         if (index >= 0) {
/* 1312 */           return true;
/*      */         }
/* 1314 */         index = (index ^ 0xFFFFFFFF) - 1;
/* 1315 */         return (index >= 0) && (c <= this.rangeEnds[index]);
/*      */       }
/*      */       
/*      */ 
/*      */       public String toString()
/*      */       {
/* 1321 */         return this.description;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private static final class Digit
/*      */       extends CharMatcher.RangesMatcher
/*      */     {
/*      */       private static final String ZEROES = "0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０";
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       private static char[] zeroes()
/*      */       {
/* 1339 */         return "0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０".toCharArray();
/*      */       }
/*      */       
/*      */       private static char[] nines() {
/* 1343 */         char[] nines = new char["0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０".length()];
/* 1344 */         for (int i = 0; i < "0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０".length(); i++) {
/* 1345 */           nines[i] = ((char)("0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０".charAt(i) + '\t'));
/*      */         }
/* 1347 */         return nines;
/*      */       }
/*      */       
/* 1350 */       static final Digit INSTANCE = new Digit();
/*      */       
/*      */       private Digit() {
/* 1353 */         super(zeroes(), nines());
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class JavaDigit
/*      */       extends CharMatcher
/*      */     {
/* 1360 */       static final JavaDigit INSTANCE = new JavaDigit();
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1364 */         return Character.isDigit(c);
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 1369 */         return "CharMatcher.javaDigit()";
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class JavaLetter
/*      */       extends CharMatcher
/*      */     {
/* 1376 */       static final JavaLetter INSTANCE = new JavaLetter();
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1380 */         return Character.isLetter(c);
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 1385 */         return "CharMatcher.javaLetter()";
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class JavaLetterOrDigit
/*      */       extends CharMatcher
/*      */     {
/* 1392 */       static final JavaLetterOrDigit INSTANCE = new JavaLetterOrDigit();
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1396 */         return Character.isLetterOrDigit(c);
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 1401 */         return "CharMatcher.javaLetterOrDigit()";
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class JavaUpperCase
/*      */       extends CharMatcher
/*      */     {
/* 1408 */       static final JavaUpperCase INSTANCE = new JavaUpperCase();
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1412 */         return Character.isUpperCase(c);
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 1417 */         return "CharMatcher.javaUpperCase()";
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class JavaLowerCase
/*      */       extends CharMatcher
/*      */     {
/* 1424 */       static final JavaLowerCase INSTANCE = new JavaLowerCase();
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1428 */         return Character.isLowerCase(c);
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 1433 */         return "CharMatcher.javaLowerCase()";
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class JavaIsoControl
/*      */       extends CharMatcher.NamedFastMatcher
/*      */     {
/* 1440 */       static final JavaIsoControl INSTANCE = new JavaIsoControl();
/*      */       
/*      */       private JavaIsoControl() {
/* 1443 */         super();
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1448 */         return (c <= '\037') || ((c >= '') && (c <= ''));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private static final class Invisible
/*      */       extends CharMatcher.RangesMatcher
/*      */     {
/*      */       private static final String RANGE_STARTS = "\000­؀؜۝܏࣢ ᠎   ⁦　?﻿￹";
/*      */       
/*      */ 
/*      */ 
/*      */       private static final String RANGE_ENDS = "  ­؅؜۝܏࣢ ᠎‏ ⁤⁯　﻿￻";
/*      */       
/*      */ 
/* 1465 */       static final Invisible INSTANCE = new Invisible();
/*      */       
/*      */       private Invisible() {
/* 1468 */         super("\000­؀؜۝܏࣢ ᠎   ⁦　?﻿￹".toCharArray(), "  ­؅؜۝܏࣢ ᠎‏ ⁤⁯　﻿￻".toCharArray());
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class SingleWidth
/*      */       extends CharMatcher.RangesMatcher
/*      */     {
/* 1475 */       static final SingleWidth INSTANCE = new SingleWidth();
/*      */       
/*      */       private SingleWidth() {
/* 1478 */         super("\000־א׳؀ݐ฀Ḁ℀ﭐﹰ｡"
/*      */         
/* 1480 */           .toCharArray(), "ӹ־ת״ۿݿ๿₯℺﷿﻿ￜ"
/* 1481 */           .toCharArray());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     private static class Negated
/*      */       extends CharMatcher
/*      */     {
/*      */       final CharMatcher original;
/*      */       
/*      */       Negated(CharMatcher original)
/*      */       {
/* 1493 */         this.original = ((CharMatcher)Preconditions.checkNotNull(original));
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1498 */         return !this.original.matches(c);
/*      */       }
/*      */       
/*      */       public boolean matchesAllOf(CharSequence sequence)
/*      */       {
/* 1503 */         return this.original.matchesNoneOf(sequence);
/*      */       }
/*      */       
/*      */       public boolean matchesNoneOf(CharSequence sequence)
/*      */       {
/* 1508 */         return this.original.matchesAllOf(sequence);
/*      */       }
/*      */       
/*      */       public int countIn(CharSequence sequence)
/*      */       {
/* 1513 */         return sequence.length() - this.original.countIn(sequence);
/*      */       }
/*      */       
/*      */       @GwtIncompatible
/*      */       void setBits(BitSet table)
/*      */       {
/* 1519 */         BitSet tmp = new BitSet();
/* 1520 */         this.original.setBits(tmp);
/* 1521 */         tmp.flip(0, 65536);
/* 1522 */         table.or(tmp);
/*      */       }
/*      */       
/*      */       public CharMatcher negate()
/*      */       {
/* 1527 */         return this.original;
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 1532 */         return this.original + ".negate()";
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class And extends CharMatcher
/*      */     {
/*      */       final CharMatcher first;
/*      */       final CharMatcher second;
/*      */       
/*      */       And(CharMatcher a, CharMatcher b)
/*      */       {
/* 1543 */         this.first = ((CharMatcher)Preconditions.checkNotNull(a));
/* 1544 */         this.second = ((CharMatcher)Preconditions.checkNotNull(b));
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1549 */         return (this.first.matches(c)) && (this.second.matches(c));
/*      */       }
/*      */       
/*      */       @GwtIncompatible
/*      */       void setBits(BitSet table)
/*      */       {
/* 1555 */         BitSet tmp1 = new BitSet();
/* 1556 */         this.first.setBits(tmp1);
/* 1557 */         BitSet tmp2 = new BitSet();
/* 1558 */         this.second.setBits(tmp2);
/* 1559 */         tmp1.and(tmp2);
/* 1560 */         table.or(tmp1);
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 1565 */         return "CharMatcher.and(" + this.first + ", " + this.second + ")";
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class Or extends CharMatcher
/*      */     {
/*      */       final CharMatcher first;
/*      */       final CharMatcher second;
/*      */       
/*      */       Or(CharMatcher a, CharMatcher b)
/*      */       {
/* 1576 */         this.first = ((CharMatcher)Preconditions.checkNotNull(a));
/* 1577 */         this.second = ((CharMatcher)Preconditions.checkNotNull(b));
/*      */       }
/*      */       
/*      */       @GwtIncompatible
/*      */       void setBits(BitSet table)
/*      */       {
/* 1583 */         this.first.setBits(table);
/* 1584 */         this.second.setBits(table);
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1589 */         return (this.first.matches(c)) || (this.second.matches(c));
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 1594 */         return "CharMatcher.or(" + this.first + ", " + this.second + ")";
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     private static final class Is
/*      */       extends CharMatcher.FastMatcher
/*      */     {
/*      */       private final char match;
/*      */       
/*      */       Is(char match)
/*      */       {
/* 1606 */         this.match = match;
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1611 */         return c == this.match;
/*      */       }
/*      */       
/*      */       public String replaceFrom(CharSequence sequence, char replacement)
/*      */       {
/* 1616 */         return sequence.toString().replace(this.match, replacement);
/*      */       }
/*      */       
/*      */       public CharMatcher and(CharMatcher other)
/*      */       {
/* 1621 */         return other.matches(this.match) ? this : none();
/*      */       }
/*      */       
/*      */       public CharMatcher or(CharMatcher other)
/*      */       {
/* 1626 */         return other.matches(this.match) ? other : super.or(other);
/*      */       }
/*      */       
/*      */       public CharMatcher negate()
/*      */       {
/* 1631 */         return isNot(this.match);
/*      */       }
/*      */       
/*      */       @GwtIncompatible
/*      */       void setBits(BitSet table)
/*      */       {
/* 1637 */         table.set(this.match);
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 1642 */         return "CharMatcher.is('" + CharMatcher.showCharacter(this.match) + "')";
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class IsNot extends CharMatcher.FastMatcher
/*      */     {
/*      */       private final char match;
/*      */       
/*      */       IsNot(char match)
/*      */       {
/* 1652 */         this.match = match;
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1657 */         return c != this.match;
/*      */       }
/*      */       
/*      */       public CharMatcher and(CharMatcher other)
/*      */       {
/* 1662 */         return other.matches(this.match) ? super.and(other) : other;
/*      */       }
/*      */       
/*      */       public CharMatcher or(CharMatcher other)
/*      */       {
/* 1667 */         return other.matches(this.match) ? any() : this;
/*      */       }
/*      */       
/*      */       @GwtIncompatible
/*      */       void setBits(BitSet table)
/*      */       {
/* 1673 */         table.set(0, this.match);
/* 1674 */         table.set(this.match + '\001', 65536);
/*      */       }
/*      */       
/*      */       public CharMatcher negate()
/*      */       {
/* 1679 */         return is(this.match);
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 1684 */         return "CharMatcher.isNot('" + CharMatcher.showCharacter(this.match) + "')";
/*      */       }
/*      */     }
/*      */     
/*      */     private static IsEither isEither(char c1, char c2) {
/* 1689 */       return new IsEither(c1, c2);
/*      */     }
/*      */     
/*      */     private static final class IsEither extends CharMatcher.FastMatcher
/*      */     {
/*      */       private final char match1;
/*      */       private final char match2;
/*      */       
/*      */       IsEither(char match1, char match2)
/*      */       {
/* 1699 */         this.match1 = match1;
/* 1700 */         this.match2 = match2;
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1705 */         return (c == this.match1) || (c == this.match2);
/*      */       }
/*      */       
/*      */       @GwtIncompatible
/*      */       void setBits(BitSet table)
/*      */       {
/* 1711 */         table.set(this.match1);
/* 1712 */         table.set(this.match2);
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 1717 */         return "CharMatcher.anyOf(\"" + CharMatcher.showCharacter(this.match1) + CharMatcher.showCharacter(this.match2) + "\")";
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class AnyOf extends CharMatcher
/*      */     {
/*      */       private final char[] chars;
/*      */       
/*      */       public AnyOf(CharSequence chars)
/*      */       {
/* 1727 */         this.chars = chars.toString().toCharArray();
/* 1728 */         Arrays.sort(this.chars);
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1733 */         return Arrays.binarySearch(this.chars, c) >= 0;
/*      */       }
/*      */       
/*      */       @GwtIncompatible
/*      */       void setBits(BitSet table)
/*      */       {
/* 1739 */         for (char c : this.chars) {
/* 1740 */           table.set(c);
/*      */         }
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 1746 */         StringBuilder description = new StringBuilder("CharMatcher.anyOf(\"");
/* 1747 */         for (char c : this.chars) {
/* 1748 */           description.append(CharMatcher.showCharacter(c));
/*      */         }
/* 1750 */         description.append("\")");
/* 1751 */         return description.toString();
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class InRange extends CharMatcher.FastMatcher
/*      */     {
/*      */       private final char startInclusive;
/*      */       private final char endInclusive;
/*      */       
/*      */       InRange(char startInclusive, char endInclusive)
/*      */       {
/* 1762 */         Preconditions.checkArgument(endInclusive >= startInclusive);
/* 1763 */         this.startInclusive = startInclusive;
/* 1764 */         this.endInclusive = endInclusive;
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1769 */         return (this.startInclusive <= c) && (c <= this.endInclusive);
/*      */       }
/*      */       
/*      */       @GwtIncompatible
/*      */       void setBits(BitSet table)
/*      */       {
/* 1775 */         table.set(this.startInclusive, this.endInclusive + '\001');
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 1780 */         return 
/*      */         
/*      */ 
/* 1783 */           "CharMatcher.inRange('" + CharMatcher.showCharacter(this.startInclusive) + "', '" + CharMatcher.showCharacter(this.endInclusive) + "')";
/*      */       }
/*      */     }
/*      */     
/*      */     private static final class ForPredicate
/*      */       extends CharMatcher
/*      */     {
/*      */       private final Predicate<? super Character> predicate;
/*      */       
/*      */       ForPredicate(Predicate<? super Character> predicate)
/*      */       {
/* 1794 */         this.predicate = ((Predicate)Preconditions.checkNotNull(predicate));
/*      */       }
/*      */       
/*      */       public boolean matches(char c)
/*      */       {
/* 1799 */         return this.predicate.apply(Character.valueOf(c));
/*      */       }
/*      */       
/*      */ 
/*      */       public boolean apply(Character character)
/*      */       {
/* 1805 */         return this.predicate.apply(Preconditions.checkNotNull(character));
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 1810 */         return "CharMatcher.forPredicate(" + this.predicate + ")";
/*      */       }
/*      */     }
/*      */   }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\base\CharMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */