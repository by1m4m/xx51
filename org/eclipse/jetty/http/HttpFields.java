/*      */ package org.eclipse.jetty.http;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.stream.Stream;
/*      */ import java.util.stream.StreamSupport;
/*      */ import org.eclipse.jetty.util.ArrayTernaryTrie;
/*      */ import org.eclipse.jetty.util.QuotedStringTokenizer;
/*      */ import org.eclipse.jetty.util.Trie;
/*      */ import org.eclipse.jetty.util.log.Log;
/*      */ import org.eclipse.jetty.util.log.Logger;
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
/*      */ public class HttpFields
/*      */   implements Iterable<HttpField>
/*      */ {
/*      */   @Deprecated
/*      */   public static final String __separators = ", \t";
/*   58 */   private static final Logger LOG = Log.getLogger(HttpFields.class);
/*      */   
/*      */ 
/*      */   private HttpField[] _fields;
/*      */   
/*      */   private int _size;
/*      */   
/*      */ 
/*      */   public HttpFields()
/*      */   {
/*   68 */     this._fields = new HttpField[20];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpFields(int capacity)
/*      */   {
/*   78 */     this._fields = new HttpField[capacity];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpFields(HttpFields fields)
/*      */   {
/*   88 */     this._fields = ((HttpField[])Arrays.copyOf(fields._fields, fields._fields.length + 10));
/*   89 */     this._size = fields._size;
/*      */   }
/*      */   
/*      */   public int size()
/*      */   {
/*   94 */     return this._size;
/*      */   }
/*      */   
/*      */ 
/*      */   public Iterator<HttpField> iterator()
/*      */   {
/*  100 */     return new Itr(null);
/*      */   }
/*      */   
/*      */   public ListIterator<HttpField> listIterator()
/*      */   {
/*  105 */     return new Itr(null);
/*      */   }
/*      */   
/*      */ 
/*      */   public Stream<HttpField> stream()
/*      */   {
/*  111 */     return StreamSupport.stream(Arrays.spliterator(this._fields, 0, this._size), false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<String> getFieldNamesCollection()
/*      */   {
/*  120 */     Set<String> set = new HashSet(this._size);
/*  121 */     for (HttpField f : this)
/*      */     {
/*  123 */       if (f != null)
/*  124 */         set.add(f.getName());
/*      */     }
/*  126 */     return set;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<String> getFieldNames()
/*      */   {
/*  136 */     return Collections.enumeration(getFieldNamesCollection());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpField getField(int index)
/*      */   {
/*  146 */     if (index >= this._size)
/*  147 */       throw new NoSuchElementException();
/*  148 */     return this._fields[index];
/*      */   }
/*      */   
/*      */   public HttpField getField(HttpHeader header)
/*      */   {
/*  153 */     for (int i = 0; i < this._size; i++)
/*      */     {
/*  155 */       HttpField f = this._fields[i];
/*  156 */       if (f.getHeader() == header)
/*  157 */         return f;
/*      */     }
/*  159 */     return null;
/*      */   }
/*      */   
/*      */   public HttpField getField(String name)
/*      */   {
/*  164 */     for (int i = 0; i < this._size; i++)
/*      */     {
/*  166 */       HttpField f = this._fields[i];
/*  167 */       if (f.getName().equalsIgnoreCase(name))
/*  168 */         return f;
/*      */     }
/*  170 */     return null;
/*      */   }
/*      */   
/*      */   public boolean contains(HttpField field)
/*      */   {
/*  175 */     for (int i = this._size; i-- > 0;)
/*      */     {
/*  177 */       HttpField f = this._fields[i];
/*  178 */       if ((f.isSameName(field)) && ((f.equals(field)) || (f.contains(field.getValue()))))
/*  179 */         return true;
/*      */     }
/*  181 */     return false;
/*      */   }
/*      */   
/*      */   public boolean contains(HttpHeader header, String value)
/*      */   {
/*  186 */     for (int i = this._size; i-- > 0;)
/*      */     {
/*  188 */       HttpField f = this._fields[i];
/*  189 */       if ((f.getHeader() == header) && (f.contains(value)))
/*  190 */         return true;
/*      */     }
/*  192 */     return false;
/*      */   }
/*      */   
/*      */   public boolean contains(String name, String value)
/*      */   {
/*  197 */     for (int i = this._size; i-- > 0;)
/*      */     {
/*  199 */       HttpField f = this._fields[i];
/*  200 */       if ((f.getName().equalsIgnoreCase(name)) && (f.contains(value)))
/*  201 */         return true;
/*      */     }
/*  203 */     return false;
/*      */   }
/*      */   
/*      */   public boolean contains(HttpHeader header)
/*      */   {
/*  208 */     for (int i = this._size; i-- > 0;)
/*      */     {
/*  210 */       HttpField f = this._fields[i];
/*  211 */       if (f.getHeader() == header)
/*  212 */         return true;
/*      */     }
/*  214 */     return false;
/*      */   }
/*      */   
/*      */   public boolean containsKey(String name)
/*      */   {
/*  219 */     for (int i = this._size; i-- > 0;)
/*      */     {
/*  221 */       HttpField f = this._fields[i];
/*  222 */       if (f.getName().equalsIgnoreCase(name))
/*  223 */         return true;
/*      */     }
/*  225 */     return false;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public String getStringField(HttpHeader header)
/*      */   {
/*  231 */     return get(header);
/*      */   }
/*      */   
/*      */   public String get(HttpHeader header)
/*      */   {
/*  236 */     for (int i = 0; i < this._size; i++)
/*      */     {
/*  238 */       HttpField f = this._fields[i];
/*  239 */       if (f.getHeader() == header)
/*  240 */         return f.getValue();
/*      */     }
/*  242 */     return null;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public String getStringField(String name)
/*      */   {
/*  248 */     return get(name);
/*      */   }
/*      */   
/*      */   public String get(String header)
/*      */   {
/*  253 */     for (int i = 0; i < this._size; i++)
/*      */     {
/*  255 */       HttpField f = this._fields[i];
/*  256 */       if (f.getName().equalsIgnoreCase(header))
/*  257 */         return f.getValue();
/*      */     }
/*  259 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<String> getValuesList(HttpHeader header)
/*      */   {
/*  270 */     List<String> list = new ArrayList();
/*  271 */     for (HttpField f : this)
/*  272 */       if (f.getHeader() == header)
/*  273 */         list.add(f.getValue());
/*  274 */     return list;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<String> getValuesList(String name)
/*      */   {
/*  285 */     List<String> list = new ArrayList();
/*  286 */     for (HttpField f : this)
/*  287 */       if (f.getName().equalsIgnoreCase(name))
/*  288 */         list.add(f.getValue());
/*  289 */     return list;
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
/*      */   public boolean addCSV(HttpHeader header, String... values)
/*      */   {
/*  302 */     QuotedCSV existing = null;
/*  303 */     for (HttpField f : this)
/*      */     {
/*  305 */       if (f.getHeader() == header)
/*      */       {
/*  307 */         if (existing == null)
/*  308 */           existing = new QuotedCSV(false, new String[0]);
/*  309 */         existing.addValue(f.getValue());
/*      */       }
/*      */     }
/*      */     
/*  313 */     String value = addCSV(existing, values);
/*  314 */     if (value != null)
/*      */     {
/*  316 */       add(header, value);
/*  317 */       return true;
/*      */     }
/*  319 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean addCSV(String name, String... values)
/*      */   {
/*  331 */     QuotedCSV existing = null;
/*  332 */     for (HttpField f : this)
/*      */     {
/*  334 */       if (f.getName().equalsIgnoreCase(name))
/*      */       {
/*  336 */         if (existing == null)
/*  337 */           existing = new QuotedCSV(false, new String[0]);
/*  338 */         existing.addValue(f.getValue());
/*      */       }
/*      */     }
/*  341 */     String value = addCSV(existing, values);
/*  342 */     if (value != null)
/*      */     {
/*  344 */       add(name, value);
/*  345 */       return true;
/*      */     }
/*  347 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   protected String addCSV(QuotedCSV existing, String... values)
/*      */   {
/*  353 */     boolean add = true;
/*  354 */     int i; if ((existing != null) && (!existing.isEmpty()))
/*      */     {
/*  356 */       add = false;
/*      */       
/*  358 */       for (i = values.length; i-- > 0;)
/*      */       {
/*  360 */         unquoted = QuotedCSV.unquote(values[i]);
/*  361 */         if (existing.getValues().contains(unquoted)) {
/*  362 */           values[i] = null;
/*      */         } else
/*  364 */           add = true;
/*      */       }
/*      */     }
/*      */     String unquoted;
/*  368 */     if (add)
/*      */     {
/*  370 */       StringBuilder value = new StringBuilder();
/*  371 */       for (String v : values)
/*      */       {
/*  373 */         if (v != null)
/*      */         {
/*  375 */           if (value.length() > 0)
/*  376 */             value.append(", ");
/*  377 */           value.append(v);
/*      */         } }
/*  379 */       if (value.length() > 0) {
/*  380 */         return value.toString();
/*      */       }
/*      */     }
/*  383 */     return null;
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
/*      */   public List<String> getCSV(HttpHeader header, boolean keepQuotes)
/*      */   {
/*  396 */     QuotedCSV values = null;
/*  397 */     for (HttpField f : this)
/*      */     {
/*  399 */       if (f.getHeader() == header)
/*      */       {
/*  401 */         if (values == null)
/*  402 */           values = new QuotedCSV(keepQuotes, new String[0]);
/*  403 */         values.addValue(f.getValue());
/*      */       }
/*      */     }
/*  406 */     return values == null ? Collections.emptyList() : values.getValues();
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
/*      */   public List<String> getCSV(String name, boolean keepQuotes)
/*      */   {
/*  419 */     QuotedCSV values = null;
/*  420 */     for (HttpField f : this)
/*      */     {
/*  422 */       if (f.getName().equalsIgnoreCase(name))
/*      */       {
/*  424 */         if (values == null)
/*  425 */           values = new QuotedCSV(keepQuotes, new String[0]);
/*  426 */         values.addValue(f.getValue());
/*      */       }
/*      */     }
/*  429 */     return values == null ? Collections.emptyList() : values.getValues();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<String> getQualityCSV(HttpHeader header)
/*      */   {
/*  441 */     QuotedQualityCSV values = null;
/*  442 */     for (HttpField f : this)
/*      */     {
/*  444 */       if (f.getHeader() == header)
/*      */       {
/*  446 */         if (values == null)
/*  447 */           values = new QuotedQualityCSV();
/*  448 */         values.addValue(f.getValue());
/*      */       }
/*      */     }
/*      */     
/*  452 */     return values == null ? Collections.emptyList() : values.getValues();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<String> getQualityCSV(String name)
/*      */   {
/*  464 */     QuotedQualityCSV values = null;
/*  465 */     for (HttpField f : this)
/*      */     {
/*  467 */       if (f.getName().equalsIgnoreCase(name))
/*      */       {
/*  469 */         if (values == null)
/*  470 */           values = new QuotedQualityCSV();
/*  471 */         values.addValue(f.getValue());
/*      */       }
/*      */     }
/*  474 */     return values == null ? Collections.emptyList() : values.getValues();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<String> getValues(final String name)
/*      */   {
/*  485 */     for (int i = 0; i < this._size; i++)
/*      */     {
/*  487 */       final HttpField f = this._fields[i];
/*      */       
/*  489 */       if ((f.getName().equalsIgnoreCase(name)) && (f.getValue() != null))
/*      */       {
/*  491 */         final int first = i;
/*  492 */         new Enumeration()
/*      */         {
/*  494 */           HttpField field = f;
/*  495 */           int i = first + 1;
/*      */           
/*      */ 
/*      */           public boolean hasMoreElements()
/*      */           {
/*  500 */             if (this.field == null)
/*      */             {
/*  502 */               while (this.i < HttpFields.this._size)
/*      */               {
/*  504 */                 this.field = HttpFields.this._fields[(this.i++)];
/*  505 */                 if ((this.field.getName().equalsIgnoreCase(name)) && (this.field.getValue() != null))
/*  506 */                   return true;
/*      */               }
/*  508 */               this.field = null;
/*  509 */               return false;
/*      */             }
/*  511 */             return true;
/*      */           }
/*      */           
/*      */           public String nextElement()
/*      */             throws NoSuchElementException
/*      */           {
/*  517 */             if (hasMoreElements())
/*      */             {
/*  519 */               String value = this.field.getValue();
/*  520 */               this.field = null;
/*  521 */               return value;
/*      */             }
/*  523 */             throw new NoSuchElementException();
/*      */           }
/*      */         };
/*      */       }
/*      */     }
/*      */     
/*  529 */     List<String> empty = Collections.emptyList();
/*  530 */     return Collections.enumeration(empty);
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
/*      */   @Deprecated
/*      */   public Enumeration<String> getValues(String name, final String separators)
/*      */   {
/*  545 */     final Enumeration<String> e = getValues(name);
/*  546 */     if (e == null)
/*  547 */       return null;
/*  548 */     new Enumeration()
/*      */     {
/*  550 */       QuotedStringTokenizer tok = null;
/*      */       
/*      */ 
/*      */       public boolean hasMoreElements()
/*      */       {
/*  555 */         if ((this.tok != null) && (this.tok.hasMoreElements())) return true;
/*  556 */         while (e.hasMoreElements())
/*      */         {
/*  558 */           String value = (String)e.nextElement();
/*  559 */           if (value != null)
/*      */           {
/*  561 */             this.tok = new QuotedStringTokenizer(value, separators, false, false);
/*  562 */             if (this.tok.hasMoreElements()) return true;
/*      */           }
/*      */         }
/*  565 */         this.tok = null;
/*  566 */         return false;
/*      */       }
/*      */       
/*      */       public String nextElement()
/*      */         throws NoSuchElementException
/*      */       {
/*  572 */         if (!hasMoreElements()) throw new NoSuchElementException();
/*  573 */         String next = (String)this.tok.nextElement();
/*  574 */         if (next != null) next = next.trim();
/*  575 */         return next;
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */   public void put(HttpField field)
/*      */   {
/*  582 */     boolean put = false;
/*  583 */     for (int i = this._size; i-- > 0;)
/*      */     {
/*  585 */       HttpField f = this._fields[i];
/*  586 */       if (f.isSameName(field))
/*      */       {
/*  588 */         if (put)
/*      */         {
/*  590 */           System.arraycopy(this._fields, i + 1, this._fields, i, --this._size - i);
/*      */         }
/*      */         else
/*      */         {
/*  594 */           this._fields[i] = field;
/*  595 */           put = true;
/*      */         }
/*      */       }
/*      */     }
/*  599 */     if (!put) {
/*  600 */       add(field);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void put(String name, String value)
/*      */   {
/*  611 */     if (value == null) {
/*  612 */       remove(name);
/*      */     } else {
/*  614 */       put(new HttpField(name, value));
/*      */     }
/*      */   }
/*      */   
/*      */   public void put(HttpHeader header, HttpHeaderValue value) {
/*  619 */     put(header, value.toString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void put(HttpHeader header, String value)
/*      */   {
/*  630 */     if (value == null) {
/*  631 */       remove(header);
/*      */     } else {
/*  633 */       put(new HttpField(header, value));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void put(String name, List<String> list)
/*      */   {
/*  644 */     remove(name);
/*  645 */     for (String v : list) {
/*  646 */       if (v != null) {
/*  647 */         add(name, v);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void add(String name, String value)
/*      */   {
/*  659 */     if (value == null) {
/*  660 */       return;
/*      */     }
/*  662 */     HttpField field = new HttpField(name, value);
/*  663 */     add(field);
/*      */   }
/*      */   
/*      */   public void add(HttpHeader header, HttpHeaderValue value)
/*      */   {
/*  668 */     add(header, value.toString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void add(HttpHeader header, String value)
/*      */   {
/*  680 */     if (value == null) { throw new IllegalArgumentException("null value");
/*      */     }
/*  682 */     HttpField field = new HttpField(header, value);
/*  683 */     add(field);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpField remove(HttpHeader name)
/*      */   {
/*  694 */     HttpField removed = null;
/*  695 */     for (int i = this._size; i-- > 0;)
/*      */     {
/*  697 */       HttpField f = this._fields[i];
/*  698 */       if (f.getHeader() == name)
/*      */       {
/*  700 */         removed = f;
/*  701 */         System.arraycopy(this._fields, i + 1, this._fields, i, --this._size - i);
/*      */       }
/*      */     }
/*  704 */     return removed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpField remove(String name)
/*      */   {
/*  715 */     HttpField removed = null;
/*  716 */     for (int i = this._size; i-- > 0;)
/*      */     {
/*  718 */       HttpField f = this._fields[i];
/*  719 */       if (f.getName().equalsIgnoreCase(name))
/*      */       {
/*  721 */         removed = f;
/*  722 */         System.arraycopy(this._fields, i + 1, this._fields, i, --this._size - i);
/*      */       }
/*      */     }
/*  725 */     return removed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getLongField(String name)
/*      */     throws NumberFormatException
/*      */   {
/*  738 */     HttpField field = getField(name);
/*  739 */     return field == null ? -1L : field.getLongValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getDateField(String name)
/*      */   {
/*  751 */     HttpField field = getField(name);
/*  752 */     if (field == null) {
/*  753 */       return -1L;
/*      */     }
/*  755 */     String val = valueParameters(field.getValue(), null);
/*  756 */     if (val == null) {
/*  757 */       return -1L;
/*      */     }
/*  759 */     long date = DateParser.parseDate(val);
/*  760 */     if (date == -1L)
/*  761 */       throw new IllegalArgumentException("Cannot convert date: " + val);
/*  762 */     return date;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void putLongField(HttpHeader name, long value)
/*      */   {
/*  774 */     String v = Long.toString(value);
/*  775 */     put(name, v);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void putLongField(String name, long value)
/*      */   {
/*  786 */     String v = Long.toString(value);
/*  787 */     put(name, v);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void putDateField(HttpHeader name, long date)
/*      */   {
/*  799 */     String d = DateGenerator.formatDate(date);
/*  800 */     put(name, d);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void putDateField(String name, long date)
/*      */   {
/*  811 */     String d = DateGenerator.formatDate(date);
/*  812 */     put(name, d);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addDateField(String name, long date)
/*      */   {
/*  823 */     String d = DateGenerator.formatDate(date);
/*  824 */     add(name, d);
/*      */   }
/*      */   
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  830 */     int hash = 0;
/*  831 */     for (HttpField field : this._fields)
/*  832 */       hash += field.hashCode();
/*  833 */     return hash;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean equals(Object o)
/*      */   {
/*  839 */     if (this == o)
/*  840 */       return true;
/*  841 */     if (!(o instanceof HttpFields)) {
/*  842 */       return false;
/*      */     }
/*  844 */     HttpFields that = (HttpFields)o;
/*      */     
/*      */ 
/*  847 */     if (size() != that.size()) {
/*  848 */       return false;
/*      */     }
/*  850 */     Iterator localIterator1 = iterator(); if (localIterator1.hasNext()) { HttpField fi = (HttpField)localIterator1.next();
/*      */       
/*  852 */       Iterator localIterator2 = that.iterator(); for (;;) { if (!localIterator2.hasNext()) break label103; HttpField fa = (HttpField)localIterator2.next();
/*      */         
/*  854 */         if (fi.equals(fa)) break;
/*      */       }
/*      */       label103:
/*  857 */       return false;
/*      */     }
/*  859 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public String toString()
/*      */   {
/*      */     try
/*      */     {
/*  867 */       StringBuilder buffer = new StringBuilder();
/*  868 */       for (HttpField field : this)
/*      */       {
/*  870 */         if (field != null)
/*      */         {
/*  872 */           String tmp = field.getName();
/*  873 */           if (tmp != null) buffer.append(tmp);
/*  874 */           buffer.append(": ");
/*  875 */           tmp = field.getValue();
/*  876 */           if (tmp != null) buffer.append(tmp);
/*  877 */           buffer.append("\r\n");
/*      */         }
/*      */       }
/*  880 */       buffer.append("\r\n");
/*  881 */       return buffer.toString();
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  885 */       LOG.warn(e);
/*  886 */       return e.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   public void clear()
/*      */   {
/*  892 */     this._size = 0;
/*      */   }
/*      */   
/*      */   public void add(HttpField field)
/*      */   {
/*  897 */     if (field != null)
/*      */     {
/*  899 */       if (this._size == this._fields.length)
/*  900 */         this._fields = ((HttpField[])Arrays.copyOf(this._fields, this._size * 2));
/*  901 */       this._fields[(this._size++)] = field;
/*      */     }
/*      */   }
/*      */   
/*      */   public void addAll(HttpFields fields)
/*      */   {
/*  907 */     for (int i = 0; i < fields._size; i++) {
/*  908 */       add(fields._fields[i]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void add(HttpFields fields)
/*      */   {
/*  919 */     if (fields == null) { return;
/*      */     }
/*  921 */     Enumeration<String> e = fields.getFieldNames();
/*  922 */     while (e.hasMoreElements())
/*      */     {
/*  924 */       String name = (String)e.nextElement();
/*  925 */       Enumeration<String> values = fields.getValues(name);
/*  926 */       while (values.hasMoreElements()) {
/*  927 */         add(name, (String)values.nextElement());
/*      */       }
/*      */     }
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
/*      */   public static String stripParameters(String value)
/*      */   {
/*  946 */     if (value == null) { return null;
/*      */     }
/*  948 */     int i = value.indexOf(';');
/*  949 */     if (i < 0) return value;
/*  950 */     return value.substring(0, i).trim();
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
/*      */ 
/*      */   public static String valueParameters(String value, Map<String, String> parameters)
/*      */   {
/*  969 */     if (value == null) { return null;
/*      */     }
/*  971 */     int i = value.indexOf(';');
/*  972 */     if (i < 0) return value;
/*  973 */     if (parameters == null) { return value.substring(0, i).trim();
/*      */     }
/*  975 */     StringTokenizer tok1 = new QuotedStringTokenizer(value.substring(i), ";", false, true);
/*  976 */     while (tok1.hasMoreTokens())
/*      */     {
/*  978 */       String token = tok1.nextToken();
/*  979 */       StringTokenizer tok2 = new QuotedStringTokenizer(token, "= ");
/*  980 */       if (tok2.hasMoreTokens())
/*      */       {
/*  982 */         String paramName = tok2.nextToken();
/*  983 */         String paramVal = null;
/*  984 */         if (tok2.hasMoreTokens()) paramVal = tok2.nextToken();
/*  985 */         parameters.put(paramName, paramVal);
/*      */       }
/*      */     }
/*      */     
/*  989 */     return value.substring(0, i).trim();
/*      */   }
/*      */   
/*      */   @Deprecated
/*  993 */   private static final Float __one = new Float("1.0");
/*      */   @Deprecated
/*  995 */   private static final Float __zero = new Float("0.0");
/*      */   @Deprecated
/*  997 */   private static final Trie<Float> __qualities = new ArrayTernaryTrie();
/*      */   
/*      */   static {
/* 1000 */     __qualities.put("*", __one);
/* 1001 */     __qualities.put("1.0", __one);
/* 1002 */     __qualities.put("1", __one);
/* 1003 */     __qualities.put("0.9", new Float("0.9"));
/* 1004 */     __qualities.put("0.8", new Float("0.8"));
/* 1005 */     __qualities.put("0.7", new Float("0.7"));
/* 1006 */     __qualities.put("0.66", new Float("0.66"));
/* 1007 */     __qualities.put("0.6", new Float("0.6"));
/* 1008 */     __qualities.put("0.5", new Float("0.5"));
/* 1009 */     __qualities.put("0.4", new Float("0.4"));
/* 1010 */     __qualities.put("0.33", new Float("0.33"));
/* 1011 */     __qualities.put("0.3", new Float("0.3"));
/* 1012 */     __qualities.put("0.2", new Float("0.2"));
/* 1013 */     __qualities.put("0.1", new Float("0.1"));
/* 1014 */     __qualities.put("0", __zero);
/* 1015 */     __qualities.put("0.0", __zero);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public static Float getQuality(String value)
/*      */   {
/* 1021 */     if (value == null) { return __zero;
/*      */     }
/* 1023 */     int qe = value.indexOf(";");
/* 1024 */     if ((qe++ < 0) || (qe == value.length())) { return __one;
/*      */     }
/* 1026 */     if (value.charAt(qe++) == 'q')
/*      */     {
/* 1028 */       qe++;
/* 1029 */       Float q = (Float)__qualities.get(value, qe, value.length() - qe);
/* 1030 */       if (q != null) {
/* 1031 */         return q;
/*      */       }
/*      */     }
/* 1034 */     Map<String, String> params = new HashMap(4);
/* 1035 */     valueParameters(value, params);
/* 1036 */     String qs = (String)params.get("q");
/* 1037 */     if (qs == null)
/* 1038 */       qs = "*";
/* 1039 */     Float q = (Float)__qualities.get(qs);
/* 1040 */     if (q == null)
/*      */     {
/*      */       try
/*      */       {
/* 1044 */         q = new Float(qs);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/* 1048 */         q = __one;
/*      */       }
/*      */     }
/* 1051 */     return q;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static List<String> qualityList(Enumeration<String> e)
/*      */   {
/* 1063 */     if ((e == null) || (!e.hasMoreElements())) {
/* 1064 */       return Collections.emptyList();
/*      */     }
/* 1066 */     QuotedQualityCSV values = new QuotedQualityCSV();
/* 1067 */     while (e.hasMoreElements())
/* 1068 */       values.addValue((String)e.nextElement());
/* 1069 */     return values.getValues();
/*      */   }
/*      */   
/*      */   private class Itr
/*      */     implements ListIterator<HttpField>
/*      */   {
/*      */     int _cursor;
/* 1076 */     int _last = -1;
/*      */     
/*      */     private Itr() {}
/*      */     
/*      */     public boolean hasNext() {
/* 1081 */       return this._cursor != HttpFields.this._size;
/*      */     }
/*      */     
/*      */ 
/*      */     public HttpField next()
/*      */     {
/* 1087 */       int i = this._cursor;
/* 1088 */       if (i >= HttpFields.this._size)
/* 1089 */         throw new NoSuchElementException();
/* 1090 */       this._cursor = (i + 1);
/* 1091 */       return HttpFields.this._fields[(this._last = i)];
/*      */     }
/*      */     
/*      */ 
/*      */     public void remove()
/*      */     {
/* 1097 */       if (this._last < 0) {
/* 1098 */         throw new IllegalStateException();
/*      */       }
/* 1100 */       System.arraycopy(HttpFields.this._fields, this._last + 1, HttpFields.this._fields, this._last, HttpFields.access$106(HttpFields.this) - this._last);
/* 1101 */       this._cursor = this._last;
/* 1102 */       this._last = -1;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean hasPrevious()
/*      */     {
/* 1108 */       return this._cursor > 0;
/*      */     }
/*      */     
/*      */ 
/*      */     public HttpField previous()
/*      */     {
/* 1114 */       if (this._cursor == 0)
/* 1115 */         throw new NoSuchElementException();
/* 1116 */       return HttpFields.this._fields[(this._last = --this._cursor)];
/*      */     }
/*      */     
/*      */ 
/*      */     public int nextIndex()
/*      */     {
/* 1122 */       return this._cursor + 1;
/*      */     }
/*      */     
/*      */ 
/*      */     public int previousIndex()
/*      */     {
/* 1128 */       return this._cursor - 1;
/*      */     }
/*      */     
/*      */ 
/*      */     public void set(HttpField field)
/*      */     {
/* 1134 */       if (this._last < 0)
/* 1135 */         throw new IllegalStateException();
/* 1136 */       HttpFields.this._fields[this._last] = field;
/*      */     }
/*      */     
/*      */ 
/*      */     public void add(HttpField field)
/*      */     {
/* 1142 */       HttpFields.this._fields = ((HttpField[])Arrays.copyOf(HttpFields.this._fields, HttpFields.this._fields.length + 1));
/* 1143 */       System.arraycopy(HttpFields.this._fields, this._cursor, HttpFields.this._fields, this._cursor + 1, HttpFields.access$108(HttpFields.this));
/* 1144 */       HttpFields.this._fields[(this._cursor++)] = field;
/* 1145 */       this._last = -1;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\HttpFields.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */