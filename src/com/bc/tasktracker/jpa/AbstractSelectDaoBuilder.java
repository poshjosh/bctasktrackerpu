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

import com.bc.jpa.context.PersistenceUnitContext;
import com.bc.jpa.dao.Criteria;
import com.bc.tasktracker.jpa.entities.master.Appointment;
import com.bc.tasktracker.jpa.entities.master.Appointment_;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.bc.jpa.dao.Select;

/**
 * @author Chinomso Bassey Ikwuagwu on Mar 10, 2017 3:57:32 PM
 */
public abstract class AbstractSelectDaoBuilder<T> implements SelectDaoBuilder<T> {
    
    private final String PARAM_JPACONTEXT = "jpaContext";
    private final String PARAM_RESULT_TYPE = "entityType";
    private final String PARAM_QUERY = "query";
    private final String PARAM_FROM = "from";
    private final String PARAM_TO = "to";
    private final String PARAM_OPENED = "opened";
    private final String PARAM_CLOSED = "closed";
    private final String PARAM_WHO = "responsibility";
    private final String PARAM_DEADLINE_FROM = "deadlineFrom";
    private final String PARAM_DEADLINE_TO = "deadlineto";
    
    private Map<String, Object> parameters;
    
    protected AbstractSelectDaoBuilder() {  
        parameters = new HashMap<>();
    }

    @Override
    public Map<String, Object> getParameters() {
        return this.parameters;
    }

    @Override
    public SelectDaoBuilder<T> parameters(Map<String, Object> parameters) {
        this.parameters = parameters;
        return this;
    }
    
    @Override
    public SelectDaoBuilder<T> persistenceUnitContext(PersistenceUnitContext context) {
        this.parameters.put(PARAM_JPACONTEXT, context);
        return this;
    }

    @Override
    public SelectDaoBuilder<T> resultType(Class<T> resultType) {
        this.parameters.put(PARAM_RESULT_TYPE, resultType);
        return this;
    }
    
    @Override
    public SelectDaoBuilder<T> textToFind(String query) {
        this.parameters.put(PARAM_QUERY, query);
        return this;
    }

    @Override
    public SelectDaoBuilder<T> from(Date from) {
        this.parameters.put(PARAM_FROM, from);
        return this;
    }

    @Override
    public SelectDaoBuilder<T> to(Date to) {
        this.parameters.put(PARAM_TO, to);
        return this;
    }

    @Override
    public SelectDaoBuilder<T> opened(Boolean opened) {
        this.parameters.put(PARAM_OPENED, opened);
        return this;
    }

    @Override
    public SelectDaoBuilder<T> closed(Boolean closed) {
        this.parameters.put(PARAM_CLOSED, closed);
        return this;
    }

    @Override
    public SelectDaoBuilder<T> who(String who) {
        if(who != null && !who.isEmpty()) {
            try(final Select<Appointment> select = this.getPersistenceUnitContext()
                    .getDao().forSelect(Appointment.class)) {
                final Appointment appt = select.from(Appointment.class)
                        .where(Appointment_.appointment.getName(), Criteria.ComparisonOperator.EQUALS, who)
                        .or().where(Appointment_.abbreviation.getName(), Criteria.ComparisonOperator.EQUALS, who)
                        .createQuery().getSingleResult();
                this.who(appt);
            }
        }
        return this;
    }

    @Override
    public SelectDaoBuilder<T> who(Appointment who) {
        this.parameters.put(PARAM_WHO, who);
        return this;
    }

    @Override
    public SelectDaoBuilder<T> deadlineFrom(Date deadlineFrom) {
        this.parameters.put(PARAM_DEADLINE_FROM, deadlineFrom);
        return this;
    }

    @Override
    public SelectDaoBuilder<T> deadlineTo(Date deadlineTo) {
        this.parameters.put(PARAM_DEADLINE_TO, deadlineTo);
        return this;
    }

    public PersistenceUnitContext getPersistenceUnitContext() {
        return (PersistenceUnitContext)this.parameters.get(PARAM_JPACONTEXT);
    }
    
    public Class<T> getResultType() {
        return (Class<T>)this.parameters.get(PARAM_RESULT_TYPE);
    }

    public String getQuery() {
        return (String)this.parameters.get(PARAM_QUERY);
    }

    public Date getFrom() {
        return (Date)this.parameters.get(PARAM_FROM);
    }

    public Date getTo() {
        return (Date)this.parameters.get(PARAM_TO);
    }

    public Boolean getOpened() {
        return (Boolean)this.parameters.get(PARAM_OPENED);
    }

    public Boolean getClosed() {
        return (Boolean)this.parameters.get(PARAM_CLOSED);
    }

    public Appointment getAppointment() {
        return (Appointment)this.parameters.get(PARAM_WHO);
    }

    public Date getDeadlineFrom() {
        return (Date)this.parameters.get(PARAM_DEADLINE_FROM);
    }

    public Date getDeadlineTo() {
        return (Date)this.parameters.get(PARAM_DEADLINE_TO);
    }
}
