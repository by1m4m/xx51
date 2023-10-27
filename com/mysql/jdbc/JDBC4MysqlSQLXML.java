/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLXML;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLOutputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stax.StAXResult;
/*     */ import javax.xml.transform.stax.StAXSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
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
/*     */ public class JDBC4MysqlSQLXML
/*     */   implements SQLXML
/*     */ {
/*     */   private XMLInputFactory inputFactory;
/*     */   private XMLOutputFactory outputFactory;
/*     */   private String stringRep;
/*     */   private ResultSetInternalMethods owningResultSet;
/*     */   private int columnIndexOfXml;
/*     */   private boolean fromResultSet;
/*  86 */   private boolean isClosed = false;
/*     */   
/*     */   private boolean workingWithResult;
/*     */   
/*     */   private DOMResult asDOMResult;
/*     */   
/*     */   private SAXResult asSAXResult;
/*     */   
/*     */   private SimpleSaxToReader saxToReaderConverter;
/*     */   
/*     */   private StringWriter asStringWriter;
/*     */   
/*     */   private ByteArrayOutputStream asByteArrayOutputStream;
/*     */   private ExceptionInterceptor exceptionInterceptor;
/*     */   
/*     */   protected JDBC4MysqlSQLXML(ResultSetInternalMethods owner, int index, ExceptionInterceptor exceptionInterceptor)
/*     */   {
/* 103 */     this.owningResultSet = owner;
/* 104 */     this.columnIndexOfXml = index;
/* 105 */     this.fromResultSet = true;
/* 106 */     this.exceptionInterceptor = exceptionInterceptor;
/*     */   }
/*     */   
/*     */   protected JDBC4MysqlSQLXML(ExceptionInterceptor exceptionInterceptor) {
/* 110 */     this.fromResultSet = false;
/* 111 */     this.exceptionInterceptor = exceptionInterceptor;
/*     */   }
/*     */   
/*     */   public synchronized void free() throws SQLException {
/* 115 */     this.stringRep = null;
/* 116 */     this.asDOMResult = null;
/* 117 */     this.asSAXResult = null;
/* 118 */     this.inputFactory = null;
/* 119 */     this.outputFactory = null;
/* 120 */     this.owningResultSet = null;
/* 121 */     this.workingWithResult = false;
/* 122 */     this.isClosed = true;
/*     */   }
/*     */   
/*     */   public synchronized String getString() throws SQLException
/*     */   {
/* 127 */     checkClosed();
/* 128 */     checkWorkingWithResult();
/*     */     
/* 130 */     if (this.fromResultSet) {
/* 131 */       return this.owningResultSet.getString(this.columnIndexOfXml);
/*     */     }
/*     */     
/* 134 */     return this.stringRep;
/*     */   }
/*     */   
/*     */   private synchronized void checkClosed() throws SQLException {
/* 138 */     if (this.isClosed) {
/* 139 */       throw SQLError.createSQLException("SQLXMLInstance has been free()d", this.exceptionInterceptor);
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized void checkWorkingWithResult() throws SQLException
/*     */   {
/* 145 */     if (this.workingWithResult) {
/* 146 */       throw SQLError.createSQLException("Can't perform requested operation after getResult() has been called to write XML data", "S1009", this.exceptionInterceptor);
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
/*     */   public synchronized void setString(String str)
/*     */     throws SQLException
/*     */   {
/* 180 */     checkClosed();
/* 181 */     checkWorkingWithResult();
/*     */     
/* 183 */     this.stringRep = str;
/* 184 */     this.fromResultSet = false;
/*     */   }
/*     */   
/*     */   public synchronized boolean isEmpty() throws SQLException {
/* 188 */     checkClosed();
/* 189 */     checkWorkingWithResult();
/*     */     
/* 191 */     if (!this.fromResultSet) {
/* 192 */       return (this.stringRep == null) || (this.stringRep.length() == 0);
/*     */     }
/*     */     
/* 195 */     return false;
/*     */   }
/*     */   
/*     */   public synchronized InputStream getBinaryStream() throws SQLException {
/* 199 */     checkClosed();
/* 200 */     checkWorkingWithResult();
/*     */     
/* 202 */     return this.owningResultSet.getBinaryStream(this.columnIndexOfXml);
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
/*     */   public synchronized Reader getCharacterStream()
/*     */     throws SQLException
/*     */   {
/* 231 */     checkClosed();
/* 232 */     checkWorkingWithResult();
/*     */     
/* 234 */     return this.owningResultSet.getCharacterStream(this.columnIndexOfXml);
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
/*     */   public synchronized Source getSource(Class clazz)
/*     */     throws SQLException
/*     */   {
/* 286 */     checkClosed();
/* 287 */     checkWorkingWithResult();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 293 */     if ((clazz == null) || (clazz.equals(SAXSource.class)))
/*     */     {
/* 295 */       InputSource inputSource = null;
/*     */       
/* 297 */       if (this.fromResultSet) {
/* 298 */         inputSource = new InputSource(this.owningResultSet.getCharacterStream(this.columnIndexOfXml));
/*     */       }
/*     */       else {
/* 301 */         inputSource = new InputSource(new StringReader(this.stringRep));
/*     */       }
/*     */       
/* 304 */       return new SAXSource(inputSource); }
/* 305 */     if (clazz.equals(DOMSource.class)) {
/*     */       try {
/* 307 */         DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
/*     */         
/* 309 */         builderFactory.setNamespaceAware(true);
/* 310 */         DocumentBuilder builder = builderFactory.newDocumentBuilder();
/*     */         
/* 312 */         InputSource inputSource = null;
/*     */         
/* 314 */         if (this.fromResultSet) {
/* 315 */           inputSource = new InputSource(this.owningResultSet.getCharacterStream(this.columnIndexOfXml));
/*     */         }
/*     */         else {
/* 318 */           inputSource = new InputSource(new StringReader(this.stringRep));
/*     */         }
/*     */         
/*     */ 
/* 322 */         return new DOMSource(builder.parse(inputSource));
/*     */       } catch (Throwable t) {
/* 324 */         SQLException sqlEx = SQLError.createSQLException(t.getMessage(), "S1009", this.exceptionInterceptor);
/*     */         
/* 326 */         sqlEx.initCause(t);
/*     */         
/* 328 */         throw sqlEx;
/*     */       }
/*     */     }
/* 331 */     if (clazz.equals(StreamSource.class)) {
/* 332 */       Reader reader = null;
/*     */       
/* 334 */       if (this.fromResultSet) {
/* 335 */         reader = this.owningResultSet.getCharacterStream(this.columnIndexOfXml);
/*     */       }
/*     */       else {
/* 338 */         reader = new StringReader(this.stringRep);
/*     */       }
/*     */       
/* 341 */       return new StreamSource(reader); }
/* 342 */     if (clazz.equals(StAXSource.class)) {
/*     */       try {
/* 344 */         Reader reader = null;
/*     */         
/* 346 */         if (this.fromResultSet) {
/* 347 */           reader = this.owningResultSet.getCharacterStream(this.columnIndexOfXml);
/*     */         }
/*     */         else {
/* 350 */           reader = new StringReader(this.stringRep);
/*     */         }
/*     */         
/* 353 */         return new StAXSource(this.inputFactory.createXMLStreamReader(reader));
/*     */       }
/*     */       catch (XMLStreamException ex) {
/* 356 */         SQLException sqlEx = SQLError.createSQLException(ex.getMessage(), "S1009", this.exceptionInterceptor);
/*     */         
/* 358 */         sqlEx.initCause(ex);
/*     */         
/* 360 */         throw sqlEx;
/*     */       }
/*     */     }
/* 363 */     throw SQLError.createSQLException("XML Source of type \"" + clazz.toString() + "\" Not supported.", "S1009", this.exceptionInterceptor);
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
/*     */   public synchronized OutputStream setBinaryStream()
/*     */     throws SQLException
/*     */   {
/* 389 */     checkClosed();
/* 390 */     checkWorkingWithResult();
/*     */     
/* 392 */     this.workingWithResult = true;
/*     */     
/* 394 */     return setBinaryStreamInternal();
/*     */   }
/*     */   
/*     */   private synchronized OutputStream setBinaryStreamInternal() throws SQLException
/*     */   {
/* 399 */     this.asByteArrayOutputStream = new ByteArrayOutputStream();
/*     */     
/* 401 */     return this.asByteArrayOutputStream;
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
/*     */   public synchronized Writer setCharacterStream()
/*     */     throws SQLException
/*     */   {
/* 430 */     checkClosed();
/* 431 */     checkWorkingWithResult();
/*     */     
/* 433 */     this.workingWithResult = true;
/*     */     
/* 435 */     return setCharacterStreamInternal();
/*     */   }
/*     */   
/*     */   private synchronized Writer setCharacterStreamInternal() throws SQLException
/*     */   {
/* 440 */     this.asStringWriter = new StringWriter();
/*     */     
/* 442 */     return this.asStringWriter;
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
/*     */   public synchronized Result setResult(Class clazz)
/*     */     throws SQLException
/*     */   {
/* 491 */     checkClosed();
/* 492 */     checkWorkingWithResult();
/*     */     
/* 494 */     this.workingWithResult = true;
/* 495 */     this.asDOMResult = null;
/* 496 */     this.asSAXResult = null;
/* 497 */     this.saxToReaderConverter = null;
/* 498 */     this.stringRep = null;
/* 499 */     this.asStringWriter = null;
/* 500 */     this.asByteArrayOutputStream = null;
/*     */     
/* 502 */     if ((clazz == null) || (clazz.equals(SAXResult.class))) {
/* 503 */       this.saxToReaderConverter = new SimpleSaxToReader();
/*     */       
/* 505 */       this.asSAXResult = new SAXResult(this.saxToReaderConverter);
/*     */       
/* 507 */       return this.asSAXResult; }
/* 508 */     if (clazz.equals(DOMResult.class))
/*     */     {
/* 510 */       this.asDOMResult = new DOMResult();
/* 511 */       return this.asDOMResult;
/*     */     }
/* 513 */     if (clazz.equals(StreamResult.class))
/* 514 */       return new StreamResult(setCharacterStreamInternal());
/* 515 */     if (clazz.equals(StAXResult.class)) {
/*     */       try {
/* 517 */         if (this.outputFactory == null) {
/* 518 */           this.outputFactory = XMLOutputFactory.newInstance();
/*     */         }
/*     */         
/* 521 */         return new StAXResult(this.outputFactory.createXMLEventWriter(setCharacterStreamInternal()));
/*     */       }
/*     */       catch (XMLStreamException ex) {
/* 524 */         SQLException sqlEx = SQLError.createSQLException(ex.getMessage(), "S1009", this.exceptionInterceptor);
/*     */         
/* 526 */         sqlEx.initCause(ex);
/*     */         
/* 528 */         throw sqlEx;
/*     */       }
/*     */     }
/* 531 */     throw SQLError.createSQLException("XML Result of type \"" + clazz.toString() + "\" Not supported.", "S1009", this.exceptionInterceptor);
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
/*     */   private Reader binaryInputStreamStreamToReader(ByteArrayOutputStream out)
/*     */   {
/*     */     try
/*     */     {
/* 546 */       String encoding = "UTF-8";
/*     */       try
/*     */       {
/* 549 */         ByteArrayInputStream bIn = new ByteArrayInputStream(out.toByteArray());
/*     */         
/* 551 */         XMLStreamReader reader = this.inputFactory.createXMLStreamReader(bIn);
/*     */         
/*     */ 
/* 554 */         int eventType = 0;
/*     */         
/* 556 */         while ((eventType = reader.next()) != 8) {
/* 557 */           if (eventType == 7) {
/* 558 */             String possibleEncoding = reader.getEncoding();
/*     */             
/* 560 */             if (possibleEncoding != null) {
/* 561 */               encoding = possibleEncoding;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Throwable t) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 572 */       return new StringReader(new String(out.toByteArray(), encoding));
/*     */     } catch (UnsupportedEncodingException badEnc) {
/* 574 */       throw new RuntimeException(badEnc);
/*     */     }
/*     */   }
/*     */   
/*     */   protected String readerToString(Reader reader) throws SQLException {
/* 579 */     StringBuffer buf = new StringBuffer();
/*     */     
/* 581 */     int charsRead = 0;
/*     */     
/* 583 */     char[] charBuf = new char['Ȁ'];
/*     */     try
/*     */     {
/* 586 */       while ((charsRead = reader.read(charBuf)) != -1) {
/* 587 */         buf.append(charBuf, 0, charsRead);
/*     */       }
/*     */     } catch (IOException ioEx) {
/* 590 */       SQLException sqlEx = SQLError.createSQLException(ioEx.getMessage(), "S1009", this.exceptionInterceptor);
/*     */       
/* 592 */       sqlEx.initCause(ioEx);
/*     */       
/* 594 */       throw sqlEx;
/*     */     }
/*     */     
/* 597 */     return buf.toString();
/*     */   }
/*     */   
/*     */   protected synchronized Reader serializeAsCharacterStream() throws SQLException
/*     */   {
/* 602 */     checkClosed();
/* 603 */     if (this.workingWithResult)
/*     */     {
/* 605 */       if (this.stringRep != null) {
/* 606 */         return new StringReader(this.stringRep);
/*     */       }
/*     */       
/* 609 */       if (this.asDOMResult != null) {
/* 610 */         return new StringReader(domSourceToString());
/*     */       }
/*     */       
/* 613 */       if (this.asStringWriter != null) {
/* 614 */         return new StringReader(this.asStringWriter.toString());
/*     */       }
/*     */       
/* 617 */       if (this.asSAXResult != null) {
/* 618 */         return this.saxToReaderConverter.toReader();
/*     */       }
/*     */       
/* 621 */       if (this.asByteArrayOutputStream != null) {
/* 622 */         return binaryInputStreamStreamToReader(this.asByteArrayOutputStream);
/*     */       }
/*     */     }
/*     */     
/* 626 */     return this.owningResultSet.getCharacterStream(this.columnIndexOfXml);
/*     */   }
/*     */   
/*     */   protected String domSourceToString() throws SQLException {
/*     */     try {
/* 631 */       DOMSource source = new DOMSource(this.asDOMResult.getNode());
/* 632 */       Transformer identity = TransformerFactory.newInstance().newTransformer();
/*     */       
/* 634 */       StringWriter stringOut = new StringWriter();
/* 635 */       Result result = new StreamResult(stringOut);
/* 636 */       identity.transform(source, result);
/*     */       
/* 638 */       return stringOut.toString();
/*     */     } catch (Throwable t) {
/* 640 */       SQLException sqlEx = SQLError.createSQLException(t.getMessage(), "S1009", this.exceptionInterceptor);
/*     */       
/* 642 */       sqlEx.initCause(t);
/*     */       
/* 644 */       throw sqlEx;
/*     */     }
/*     */   }
/*     */   
/*     */   protected synchronized String serializeAsString() throws SQLException {
/* 649 */     checkClosed();
/* 650 */     if (this.workingWithResult)
/*     */     {
/* 652 */       if (this.stringRep != null) {
/* 653 */         return this.stringRep;
/*     */       }
/*     */       
/* 656 */       if (this.asDOMResult != null) {
/* 657 */         return domSourceToString();
/*     */       }
/*     */       
/* 660 */       if (this.asStringWriter != null) {
/* 661 */         return this.asStringWriter.toString();
/*     */       }
/*     */       
/* 664 */       if (this.asSAXResult != null) {
/* 665 */         return readerToString(this.saxToReaderConverter.toReader());
/*     */       }
/*     */       
/* 668 */       if (this.asByteArrayOutputStream != null) {
/* 669 */         return readerToString(binaryInputStreamStreamToReader(this.asByteArrayOutputStream));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 674 */     return this.owningResultSet.getString(this.columnIndexOfXml);
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
/*     */   class SimpleSaxToReader
/*     */     extends DefaultHandler
/*     */   {
/*     */     SimpleSaxToReader() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 699 */     StringBuffer buf = new StringBuffer();
/*     */     
/*     */     public void startDocument() throws SAXException {
/* 702 */       this.buf.append("<?xml version='1.0' encoding='UTF-8'?>"); }
/*     */     
/*     */ 
/*     */     public void endDocument()
/*     */       throws SAXException
/*     */     {}
/*     */     
/*     */     public void startElement(String namespaceURI, String sName, String qName, Attributes attrs)
/*     */       throws SAXException
/*     */     {
/* 712 */       this.buf.append("<");
/* 713 */       this.buf.append(qName);
/*     */       
/* 715 */       if (attrs != null) {
/* 716 */         for (int i = 0; i < attrs.getLength(); i++) {
/* 717 */           this.buf.append(" ");
/* 718 */           this.buf.append(attrs.getQName(i)).append("=\"");
/* 719 */           escapeCharsForXml(attrs.getValue(i), true);
/* 720 */           this.buf.append("\"");
/*     */         }
/*     */       }
/*     */       
/* 724 */       this.buf.append(">");
/*     */     }
/*     */     
/*     */     public void characters(char[] buf, int offset, int len) throws SAXException
/*     */     {
/* 729 */       if (!this.inCDATA) {
/* 730 */         escapeCharsForXml(buf, offset, len, false);
/*     */       } else {
/* 732 */         this.buf.append(buf, offset, len);
/*     */       }
/*     */     }
/*     */     
/*     */     public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
/*     */     {
/* 738 */       characters(ch, start, length);
/*     */     }
/*     */     
/* 741 */     private boolean inCDATA = false;
/*     */     
/*     */     public void startCDATA() throws SAXException {
/* 744 */       this.buf.append("<![CDATA[");
/* 745 */       this.inCDATA = true;
/*     */     }
/*     */     
/*     */     public void endCDATA() throws SAXException {
/* 749 */       this.inCDATA = false;
/* 750 */       this.buf.append("]]>");
/*     */     }
/*     */     
/*     */     public void comment(char[] ch, int start, int length)
/*     */       throws SAXException
/*     */     {
/* 756 */       this.buf.append("<!--");
/* 757 */       for (int i = 0; i < length; i++) {
/* 758 */         this.buf.append(ch[(start + i)]);
/*     */       }
/* 760 */       this.buf.append("-->");
/*     */     }
/*     */     
/*     */     Reader toReader()
/*     */     {
/* 765 */       return new StringReader(this.buf.toString());
/*     */     }
/*     */     
/*     */     private void escapeCharsForXml(String str, boolean isAttributeData) {
/* 769 */       if (str == null) {
/* 770 */         return;
/*     */       }
/*     */       
/* 773 */       int strLen = str.length();
/*     */       
/* 775 */       for (int i = 0; i < strLen; i++) {
/* 776 */         escapeCharsForXml(str.charAt(i), isAttributeData);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     private void escapeCharsForXml(char[] buf, int offset, int len, boolean isAttributeData)
/*     */     {
/* 783 */       if (buf == null) {
/* 784 */         return;
/*     */       }
/*     */       
/* 787 */       for (int i = 0; i < len; i++) {
/* 788 */         escapeCharsForXml(buf[(offset + i)], isAttributeData);
/*     */       }
/*     */     }
/*     */     
/*     */     private void escapeCharsForXml(char c, boolean isAttributeData) {
/* 793 */       switch (c) {
/*     */       case '<': 
/* 795 */         this.buf.append("&lt;");
/* 796 */         break;
/*     */       
/*     */       case '>': 
/* 799 */         this.buf.append("&gt;");
/* 800 */         break;
/*     */       
/*     */       case '&': 
/* 803 */         this.buf.append("&amp;");
/* 804 */         break;
/*     */       
/*     */ 
/*     */       case '"': 
/* 808 */         if (!isAttributeData) {
/* 809 */           this.buf.append("\"");
/*     */         }
/*     */         else {
/* 812 */           this.buf.append("&quot;");
/*     */         }
/*     */         
/* 815 */         break;
/*     */       
/*     */       case '\r': 
/* 818 */         this.buf.append("&#xD;");
/* 819 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       default: 
/* 824 */         if (((c >= '\001') && (c <= '\037') && (c != '\t') && (c != '\n')) || ((c >= '') && (c <= '')) || (c == ' ') || ((isAttributeData) && ((c == '\t') || (c == '\n'))))
/*     */         {
/*     */ 
/* 827 */           this.buf.append("&#x");
/* 828 */           this.buf.append(Integer.toHexString(c).toUpperCase());
/* 829 */           this.buf.append(";");
/*     */         }
/*     */         else {
/* 832 */           this.buf.append(c);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\JDBC4MysqlSQLXML.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */