/*     */ package com.fasterxml.jackson.core.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TextBuffer
/*     */ {
/*  29 */   static final char[] NO_CHARS = new char[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int MIN_SEGMENT_LEN = 1000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int MAX_SEGMENT_LEN = 262144;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final BufferRecycler _allocator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private char[] _inputBuffer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int _inputStart;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int _inputLen;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ArrayList<char[]> _segments;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */   private boolean _hasSegments = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int _segmentSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private char[] _currentSegment;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int _currentSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String _resultString;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private char[] _resultArray;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TextBuffer(BufferRecycler allocator)
/*     */   {
/* 122 */     this._allocator = allocator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void releaseBuffers()
/*     */   {
/* 136 */     if (this._allocator == null) {
/* 137 */       resetWithEmpty();
/*     */     }
/* 139 */     else if (this._currentSegment != null)
/*     */     {
/* 141 */       resetWithEmpty();
/*     */       
/* 143 */       char[] buf = this._currentSegment;
/* 144 */       this._currentSegment = null;
/* 145 */       this._allocator.releaseCharBuffer(2, buf);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetWithEmpty()
/*     */   {
/* 156 */     this._inputStart = -1;
/* 157 */     this._currentSize = 0;
/* 158 */     this._inputLen = 0;
/*     */     
/* 160 */     this._inputBuffer = null;
/* 161 */     this._resultString = null;
/* 162 */     this._resultArray = null;
/*     */     
/*     */ 
/* 165 */     if (this._hasSegments) {
/* 166 */       clearSegments();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetWithShared(char[] buf, int start, int len)
/*     */   {
/* 179 */     this._resultString = null;
/* 180 */     this._resultArray = null;
/*     */     
/*     */ 
/* 183 */     this._inputBuffer = buf;
/* 184 */     this._inputStart = start;
/* 185 */     this._inputLen = len;
/*     */     
/*     */ 
/* 188 */     if (this._hasSegments) {
/* 189 */       clearSegments();
/*     */     }
/*     */   }
/*     */   
/*     */   public void resetWithCopy(char[] buf, int start, int len)
/*     */   {
/* 195 */     this._inputBuffer = null;
/* 196 */     this._inputStart = -1;
/* 197 */     this._inputLen = 0;
/*     */     
/* 199 */     this._resultString = null;
/* 200 */     this._resultArray = null;
/*     */     
/*     */ 
/* 203 */     if (this._hasSegments) {
/* 204 */       clearSegments();
/* 205 */     } else if (this._currentSegment == null) {
/* 206 */       this._currentSegment = buf(len);
/*     */     }
/* 208 */     this._currentSize = (this._segmentSize = 0);
/* 209 */     append(buf, start, len);
/*     */   }
/*     */   
/*     */   public void resetWithString(String value)
/*     */   {
/* 214 */     this._inputBuffer = null;
/* 215 */     this._inputStart = -1;
/* 216 */     this._inputLen = 0;
/*     */     
/* 218 */     this._resultString = value;
/* 219 */     this._resultArray = null;
/*     */     
/* 221 */     if (this._hasSegments) {
/* 222 */       clearSegments();
/*     */     }
/* 224 */     this._currentSize = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private char[] buf(int needed)
/*     */   {
/* 234 */     if (this._allocator != null) {
/* 235 */       return this._allocator.allocCharBuffer(2, needed);
/*     */     }
/* 237 */     return new char[Math.max(needed, 1000)];
/*     */   }
/*     */   
/*     */   private void clearSegments()
/*     */   {
/* 242 */     this._hasSegments = false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 250 */     this._segments.clear();
/* 251 */     this._currentSize = (this._segmentSize = 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 264 */     if (this._inputStart >= 0) {
/* 265 */       return this._inputLen;
/*     */     }
/* 267 */     if (this._resultArray != null) {
/* 268 */       return this._resultArray.length;
/*     */     }
/* 270 */     if (this._resultString != null) {
/* 271 */       return this._resultString.length();
/*     */     }
/*     */     
/* 274 */     return this._segmentSize + this._currentSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTextOffset()
/*     */   {
/* 282 */     return this._inputStart >= 0 ? this._inputStart : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasTextAsCharacters()
/*     */   {
/* 292 */     if ((this._inputStart >= 0) || (this._resultArray != null)) { return true;
/*     */     }
/* 294 */     if (this._resultString != null) return false;
/* 295 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public char[] getTextBuffer()
/*     */   {
/* 301 */     if (this._inputStart >= 0) return this._inputBuffer;
/* 302 */     if (this._resultArray != null) return this._resultArray;
/* 303 */     if (this._resultString != null) {
/* 304 */       return this._resultArray = this._resultString.toCharArray();
/*     */     }
/*     */     
/* 307 */     if (!this._hasSegments) { return this._currentSegment;
/*     */     }
/* 309 */     return contentsAsArray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String contentsAsString()
/*     */   {
/* 320 */     if (this._resultString == null)
/*     */     {
/* 322 */       if (this._resultArray != null) {
/* 323 */         this._resultString = new String(this._resultArray);
/*     */ 
/*     */       }
/* 326 */       else if (this._inputStart >= 0) {
/* 327 */         if (this._inputLen < 1) {
/* 328 */           return this._resultString = "";
/*     */         }
/* 330 */         this._resultString = new String(this._inputBuffer, this._inputStart, this._inputLen);
/*     */       }
/*     */       else {
/* 333 */         int segLen = this._segmentSize;
/* 334 */         int currLen = this._currentSize;
/*     */         
/* 336 */         if (segLen == 0) {
/* 337 */           this._resultString = (currLen == 0 ? "" : new String(this._currentSegment, 0, currLen));
/*     */         } else {
/* 339 */           StringBuilder sb = new StringBuilder(segLen + currLen);
/*     */           
/* 341 */           if (this._segments != null) {
/* 342 */             int i = 0; for (int len = this._segments.size(); i < len; i++) {
/* 343 */               char[] curr = (char[])this._segments.get(i);
/* 344 */               sb.append(curr, 0, curr.length);
/*     */             }
/*     */           }
/*     */           
/* 348 */           sb.append(this._currentSegment, 0, this._currentSize);
/* 349 */           this._resultString = sb.toString();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 354 */     return this._resultString;
/*     */   }
/*     */   
/*     */   public char[] contentsAsArray() {
/* 358 */     char[] result = this._resultArray;
/* 359 */     if (result == null) {
/* 360 */       this._resultArray = (result = resultArray());
/*     */     }
/* 362 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BigDecimal contentsAsDecimal()
/*     */     throws NumberFormatException
/*     */   {
/* 372 */     if (this._resultArray != null) {
/* 373 */       return NumberInput.parseBigDecimal(this._resultArray);
/*     */     }
/*     */     
/* 376 */     if ((this._inputStart >= 0) && (this._inputBuffer != null)) {
/* 377 */       return NumberInput.parseBigDecimal(this._inputBuffer, this._inputStart, this._inputLen);
/*     */     }
/*     */     
/* 380 */     if ((this._segmentSize == 0) && (this._currentSegment != null)) {
/* 381 */       return NumberInput.parseBigDecimal(this._currentSegment, 0, this._currentSize);
/*     */     }
/*     */     
/* 384 */     return NumberInput.parseBigDecimal(contentsAsArray());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double contentsAsDouble()
/*     */     throws NumberFormatException
/*     */   {
/* 392 */     return NumberInput.parseDouble(contentsAsString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void ensureNotShared()
/*     */   {
/* 406 */     if (this._inputStart >= 0) {
/* 407 */       unshare(16);
/*     */     }
/*     */   }
/*     */   
/*     */   public void append(char c)
/*     */   {
/* 413 */     if (this._inputStart >= 0) {
/* 414 */       unshare(16);
/*     */     }
/* 416 */     this._resultString = null;
/* 417 */     this._resultArray = null;
/*     */     
/* 419 */     char[] curr = this._currentSegment;
/* 420 */     if (this._currentSize >= curr.length) {
/* 421 */       expand(1);
/* 422 */       curr = this._currentSegment;
/*     */     }
/* 424 */     curr[(this._currentSize++)] = c;
/*     */   }
/*     */   
/*     */ 
/*     */   public void append(char[] c, int start, int len)
/*     */   {
/* 430 */     if (this._inputStart >= 0) {
/* 431 */       unshare(len);
/*     */     }
/* 433 */     this._resultString = null;
/* 434 */     this._resultArray = null;
/*     */     
/*     */ 
/* 437 */     char[] curr = this._currentSegment;
/* 438 */     int max = curr.length - this._currentSize;
/*     */     
/* 440 */     if (max >= len) {
/* 441 */       System.arraycopy(c, start, curr, this._currentSize, len);
/* 442 */       this._currentSize += len;
/* 443 */       return;
/*     */     }
/*     */     
/* 446 */     if (max > 0) {
/* 447 */       System.arraycopy(c, start, curr, this._currentSize, max);
/* 448 */       start += max;
/* 449 */       len -= max;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     do
/*     */     {
/* 456 */       expand(len);
/* 457 */       int amount = Math.min(this._currentSegment.length, len);
/* 458 */       System.arraycopy(c, start, this._currentSegment, 0, amount);
/* 459 */       this._currentSize += amount;
/* 460 */       start += amount;
/* 461 */       len -= amount;
/* 462 */     } while (len > 0);
/*     */   }
/*     */   
/*     */ 
/*     */   public void append(String str, int offset, int len)
/*     */   {
/* 468 */     if (this._inputStart >= 0) {
/* 469 */       unshare(len);
/*     */     }
/* 471 */     this._resultString = null;
/* 472 */     this._resultArray = null;
/*     */     
/*     */ 
/* 475 */     char[] curr = this._currentSegment;
/* 476 */     int max = curr.length - this._currentSize;
/* 477 */     if (max >= len) {
/* 478 */       str.getChars(offset, offset + len, curr, this._currentSize);
/* 479 */       this._currentSize += len;
/* 480 */       return;
/*     */     }
/*     */     
/* 483 */     if (max > 0) {
/* 484 */       str.getChars(offset, offset + max, curr, this._currentSize);
/* 485 */       len -= max;
/* 486 */       offset += max;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     do
/*     */     {
/* 493 */       expand(len);
/* 494 */       int amount = Math.min(this._currentSegment.length, len);
/* 495 */       str.getChars(offset, offset + amount, this._currentSegment, 0);
/* 496 */       this._currentSize += amount;
/* 497 */       offset += amount;
/* 498 */       len -= amount;
/* 499 */     } while (len > 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public char[] getCurrentSegment()
/*     */   {
/* 514 */     if (this._inputStart >= 0) {
/* 515 */       unshare(1);
/*     */     } else {
/* 517 */       char[] curr = this._currentSegment;
/* 518 */       if (curr == null) {
/* 519 */         this._currentSegment = buf(0);
/* 520 */       } else if (this._currentSize >= curr.length)
/*     */       {
/* 522 */         expand(1);
/*     */       }
/*     */     }
/* 525 */     return this._currentSegment;
/*     */   }
/*     */   
/*     */ 
/*     */   public char[] emptyAndGetCurrentSegment()
/*     */   {
/* 531 */     this._inputStart = -1;
/* 532 */     this._currentSize = 0;
/* 533 */     this._inputLen = 0;
/*     */     
/* 535 */     this._inputBuffer = null;
/* 536 */     this._resultString = null;
/* 537 */     this._resultArray = null;
/*     */     
/*     */ 
/* 540 */     if (this._hasSegments) {
/* 541 */       clearSegments();
/*     */     }
/* 543 */     char[] curr = this._currentSegment;
/* 544 */     if (curr == null) {
/* 545 */       this._currentSegment = (curr = buf(0));
/*     */     }
/* 547 */     return curr;
/*     */   }
/*     */   
/* 550 */   public int getCurrentSegmentSize() { return this._currentSize; }
/* 551 */   public void setCurrentLength(int len) { this._currentSize = len; }
/*     */   
/*     */ 
/*     */ 
/*     */   public String setCurrentAndReturn(int len)
/*     */   {
/* 557 */     this._currentSize = len;
/*     */     
/* 559 */     if (this._segmentSize > 0) {
/* 560 */       return contentsAsString();
/*     */     }
/*     */     
/* 563 */     int currLen = this._currentSize;
/* 564 */     String str = currLen == 0 ? "" : new String(this._currentSegment, 0, currLen);
/* 565 */     this._resultString = str;
/* 566 */     return str;
/*     */   }
/*     */   
/*     */   public char[] finishCurrentSegment() {
/* 570 */     if (this._segments == null) {
/* 571 */       this._segments = new ArrayList();
/*     */     }
/* 573 */     this._hasSegments = true;
/* 574 */     this._segments.add(this._currentSegment);
/* 575 */     int oldLen = this._currentSegment.length;
/* 576 */     this._segmentSize += oldLen;
/* 577 */     this._currentSize = 0;
/*     */     
/*     */ 
/* 580 */     int newLen = oldLen + (oldLen >> 1);
/* 581 */     if (newLen < 1000) {
/* 582 */       newLen = 1000;
/* 583 */     } else if (newLen > 262144) {
/* 584 */       newLen = 262144;
/*     */     }
/* 586 */     char[] curr = carr(newLen);
/* 587 */     this._currentSegment = curr;
/* 588 */     return curr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public char[] expandCurrentSegment()
/*     */   {
/* 598 */     char[] curr = this._currentSegment;
/*     */     
/* 600 */     int len = curr.length;
/* 601 */     int newLen = len + (len >> 1);
/*     */     
/* 603 */     if (newLen > 262144) {
/* 604 */       newLen = len + (len >> 2);
/*     */     }
/* 606 */     return this._currentSegment = Arrays.copyOf(curr, newLen);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public char[] expandCurrentSegment(int minSize)
/*     */   {
/* 619 */     char[] curr = this._currentSegment;
/* 620 */     if (curr.length >= minSize) return curr;
/* 621 */     this._currentSegment = (curr = Arrays.copyOf(curr, minSize));
/* 622 */     return curr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 636 */     return contentsAsString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void unshare(int needExtra)
/*     */   {
/* 650 */     int sharedLen = this._inputLen;
/* 651 */     this._inputLen = 0;
/* 652 */     char[] inputBuf = this._inputBuffer;
/* 653 */     this._inputBuffer = null;
/* 654 */     int start = this._inputStart;
/* 655 */     this._inputStart = -1;
/*     */     
/*     */ 
/* 658 */     int needed = sharedLen + needExtra;
/* 659 */     if ((this._currentSegment == null) || (needed > this._currentSegment.length)) {
/* 660 */       this._currentSegment = buf(needed);
/*     */     }
/* 662 */     if (sharedLen > 0) {
/* 663 */       System.arraycopy(inputBuf, start, this._currentSegment, 0, sharedLen);
/*     */     }
/* 665 */     this._segmentSize = 0;
/* 666 */     this._currentSize = sharedLen;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void expand(int minNewSegmentSize)
/*     */   {
/* 676 */     if (this._segments == null) {
/* 677 */       this._segments = new ArrayList();
/*     */     }
/* 679 */     char[] curr = this._currentSegment;
/* 680 */     this._hasSegments = true;
/* 681 */     this._segments.add(curr);
/* 682 */     this._segmentSize += curr.length;
/* 683 */     this._currentSize = 0;
/* 684 */     int oldLen = curr.length;
/*     */     
/*     */ 
/* 687 */     int newLen = oldLen + (oldLen >> 1);
/* 688 */     if (newLen < 1000) {
/* 689 */       newLen = 1000;
/* 690 */     } else if (newLen > 262144) {
/* 691 */       newLen = 262144;
/*     */     }
/* 693 */     this._currentSegment = carr(newLen);
/*     */   }
/*     */   
/*     */   private char[] resultArray()
/*     */   {
/* 698 */     if (this._resultString != null) {
/* 699 */       return this._resultString.toCharArray();
/*     */     }
/*     */     
/* 702 */     if (this._inputStart >= 0) {
/* 703 */       int len = this._inputLen;
/* 704 */       if (len < 1) {
/* 705 */         return NO_CHARS;
/*     */       }
/* 707 */       int start = this._inputStart;
/* 708 */       if (start == 0) {
/* 709 */         return Arrays.copyOf(this._inputBuffer, len);
/*     */       }
/* 711 */       return Arrays.copyOfRange(this._inputBuffer, start, start + len);
/*     */     }
/*     */     
/* 714 */     int size = size();
/* 715 */     if (size < 1) {
/* 716 */       return NO_CHARS;
/*     */     }
/* 718 */     int offset = 0;
/* 719 */     char[] result = carr(size);
/* 720 */     if (this._segments != null) {
/* 721 */       int i = 0; for (int len = this._segments.size(); i < len; i++) {
/* 722 */         char[] curr = (char[])this._segments.get(i);
/* 723 */         int currLen = curr.length;
/* 724 */         System.arraycopy(curr, 0, result, offset, currLen);
/* 725 */         offset += currLen;
/*     */       }
/*     */     }
/* 728 */     System.arraycopy(this._currentSegment, 0, result, offset, this._currentSize);
/* 729 */     return result;
/*     */   }
/*     */   
/* 732 */   private char[] carr(int len) { return new char[len]; }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\util\TextBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */