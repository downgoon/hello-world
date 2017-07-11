package io.downgoon.hello.jcache;

import java.io.Serializable;

import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
 
public class MyEntryProcessor implements EntryProcessor <String, Integer,
   Integer>, Serializable 
   {
      public static final long serialVersionUID = 1L;
 
   public Integer process(MutableEntry<String, Integer> entry, 
      Object... arguments) throws EntryProcessorException 
      {

      if (entry.exists())
      {
         Integer current = entry.getValue();
         entry.setValue(current + 1);
         return current;
      } 
      else 
      {
         entry.setValue(0);
         return -1;
      }
   }
}