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

import com.bc.jpa.JpaContext;
import com.bc.jpa.dao.SelectDao;
import com.bc.tasktracker.jpa.entities.master.Appointment;
import java.util.Date;
import java.util.Map;

/**
 * @author Chinomso Bassey Ikwuagwu on Feb 20, 2017 6:51:29 PM
 * @param <T> The type of the entity
 */
public interface SelectDaoBuilder<T> {
    
    Map<String, Object> getParameters();

    SelectDaoBuilder<T> parameters(Map<String, Object> parameters);
    
    SelectDaoBuilder<T> jpaContext(JpaContext jpaContext);
    
    SelectDaoBuilder<T> resultType(Class<T> resultType);
    
    SelectDaoBuilder<T> textToFind(String query);
    
    SelectDaoBuilder<T> from(Date from);
    
    SelectDaoBuilder<T> to(Date to);
    
    SelectDaoBuilder<T> opened(Boolean opened);
    
    SelectDaoBuilder<T> closed(Boolean closed);
    
    SelectDaoBuilder<T> who(String who);
    
    SelectDaoBuilder<T> who(Appointment who);
    
    SelectDaoBuilder<T> deadlineFrom(Date deadlineFrom);
    
    SelectDaoBuilder<T> deadlineTo(Date deadlineTo);
    
    SelectDao<T> build();
}
