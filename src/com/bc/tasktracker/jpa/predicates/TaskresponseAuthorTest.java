/*
 * Copyright 2017 NUROX Ltd.
 *
 * Licensed under the NUROX Ltd Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bc.tasktracker.jpa.predicates;

import com.bc.tasktracker.jpa.entities.master.Appointment;
import com.bc.tasktracker.jpa.entities.master.Taskresponse;
import java.util.function.Predicate;

/**
 * @author Chinomso Bassey Ikwuagwu on Mar 25, 2017 10:26:14 AM
 */
public class TaskresponseAuthorTest implements Predicate<Taskresponse> {

    private final Appointment appointment;

    public TaskresponseAuthorTest() {
        this(null);
    }
    
    public TaskresponseAuthorTest(Appointment appointment) {
        this.appointment = appointment;
    }
    
    @Override
    public boolean test(Taskresponse tr) {
        return this.appointment == null || tr.getAuthor().equals(this.appointment);
    }
}
