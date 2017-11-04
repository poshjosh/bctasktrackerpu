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

package com.bc.tasktracker.jpa;

import com.bc.jpa.sync.impl.SlaveUpdateListener;
import com.bc.jpa.sync.PendingUpdatesManager;
import java.util.function.Predicate;

/**
 * @author Chinomso Bassey Ikwuagwu on Jul 29, 2017 11:55:06 AM
 */
public class SlaveUpdateListenerImpl extends SlaveUpdateListener {

    private static PendingUpdatesManager pendingUpdatesManager;
    
    private static Predicate entityFilter;
    
    public SlaveUpdateListenerImpl() {
        super(
                pendingUpdatesManager, entityFilter 
        );
    }

    public static PendingUpdatesManager getPendingUpdatesManager() {
        return pendingUpdatesManager;
    }

    public static void setPendingUpdatesManager(PendingUpdatesManager pendingUpdatesManager) {
        SlaveUpdateListenerImpl.pendingUpdatesManager = pendingUpdatesManager;
    }

    public static Predicate getEntityFilter() {
        return entityFilter;
    }

    public static void setEntityFilter(Predicate entityFilter) {
        SlaveUpdateListenerImpl.entityFilter = entityFilter;
    }
}
