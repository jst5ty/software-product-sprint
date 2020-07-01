// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;

import java.util.Comparator;
import java.util.Iterator;

public final class FindMeetingQuery {
  /*
  * For a particular time slot to work, all attendees must be free to attend the meeting. When a query is made, it will be given a collection of all known events. 
  * SOLUTION : Check to see if any attendees from the meeting request are already in events for a specfied time, if they are then the timeslot is not avaiable else it is.
  */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    
    
    ArrayList<Event> sorted_events = new ArrayList<Event>(events);
    // sort by start time inplace
    EventComparator eventComparator = new EventComparator();
    Collections.sort(sorted_events, eventComparator);

    //System.out.print("EVENTS ARE: " + events);

    //base case : No events, and duration of request is greater than a whole day
    if( (request.getDuration() > TimeRange.WHOLE_DAY.duration()) && sorted_events.isEmpty()) {
        return Arrays.asList();
    }
    // base case: No events
    if(sorted_events.isEmpty()){
        return Arrays.asList(TimeRange.WHOLE_DAY);
    }
    
    // mark the time a meeting can occur (timeslot available)
    int start_time = TimeRange.START_OF_DAY;
    // mark the end of time the meeting can occur
    int end_time = 0;
    Event prevEvent = null;

    boolean canAllAttendeesAttend = true;
    boolean isInclusive = false;

    Collection<TimeRange> result = new ArrayList<TimeRange>();

    Iterator<Event> iterator = sorted_events.iterator();
    while (iterator.hasNext()) {
        Event event = iterator.next();
        
        // check to see if there are any attendees that can not particpate
        for(String attendee: request.getAttendees()) {          
            if(event.getAttendees().contains(attendee)) {
                canAllAttendeesAttend = false;
            }
        }

        if( (event.getWhen().start() == TimeRange.START_OF_DAY) && !canAllAttendeesAttend) {
            start_time = event.getWhen().end();
        }
        // overlapping case: more stricter
        else if( (prevEvent != null) &&  prevEvent.getWhen().overlaps(event.getWhen()) && !canAllAttendeesAttend) {
            // case below handles nested events
            if( prevEvent.getWhen().end() > event.getWhen().end()) {
                start_time = prevEvent.getWhen().end();
            }
            else {
                start_time = event.getWhen().end();
            }

        }
        else if(!canAllAttendeesAttend && ( (start_time + request.getDuration()) <= event.getWhen().start() ) ){
            end_time = event.getWhen().start();
            result.add(TimeRange.fromStartEnd(start_time, end_time, isInclusive));
            start_time = event.getWhen().end();
        }
        else {
            end_time = event.getWhen().end();
        }

        // last event
        if(!iterator.hasNext() && !(event.getWhen().end() == (TimeRange.END_OF_DAY+1)) ){
            isInclusive = true;
            end_time = TimeRange.END_OF_DAY;
            result.add(TimeRange.fromStartEnd(start_time, end_time, isInclusive));
        }

        canAllAttendeesAttend = true; //reset
        prevEvent = event;
    }

    return result;
  }
}
