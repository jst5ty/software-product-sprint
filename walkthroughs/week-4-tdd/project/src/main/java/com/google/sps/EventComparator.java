package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import java.util.Comparator;

public class EventComparator implements Comparator<Event> {

   @Override
   public int compare(Event event1, Event event2) {
       return TimeRange.ORDER_BY_START.compare(event1.getWhen(), event2.getWhen());
   }

}