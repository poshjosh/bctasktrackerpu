create table "unit"
(
    "unitid" INTEGER not null primary key,
    "unit" VARCHAR(100) not null UNIQUE,
    "abbreviation" VARCHAR(100),
    "parentunit" INTEGER
);
ALTER TABLE "unit" ADD CONSTRAINT UNIT_PARENTUNIT_TO_UNIT_FK
FOREIGN KEY ("parentunit") REFERENCES "unit"("unitid") ON DELETE CASCADE;

create table "appointment"
(
    "appointmentid" INTEGER not null primary key,
    "appointment" VARCHAR(100) not null UNIQUE,
    "abbreviation" VARCHAR(100),
    "parentappointment" INTEGER,
    "unit" INTEGER not null
);
ALTER TABLE "appointment" ADD CONSTRAINT APPOINTMENT_PARENTAPPOINTMENT_TO_APPOINTMENT_FK
FOREIGN KEY ("parentappointment") REFERENCES "appointment"("appointmentid") ON DELETE CASCADE;

ALTER TABLE "appointment" ADD CONSTRAINT APPOINTMENT_UNIT_TO_UNIT_FK
FOREIGN KEY ("unit") REFERENCES "unit"("unitid") ON DELETE CASCADE;

create table "doc"
(
    "docid" INTEGER not null primary key,
    "datesigned" DATE,
    "referencenumber" VARCHAR(255),
    "subject" VARCHAR(1000) not null,                  
    "timecreated" TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    "timemodified" TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP
);

create table "task"
(
    "taskid" INTEGER not null primary key,
    "doc" INTEGER not null,
    "description" VARCHAR(10000),
    "author" INTEGER not null,
    "reponsibility" INTEGER not null,
    "timeopened" TIMESTAMP,
    "timeclosed" TIMESTAMP, 
    "timecreated" TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    "timemodified" TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE "task" ADD CONSTRAINT TASK_DOC_TO_DOC_FK
FOREIGN KEY ("doc") REFERENCES "doc"("docid") ON DELETE CASCADE;

ALTER TABLE "task" ADD CONSTRAINT TASK_AUTHOR_TO_APPOINTMENT_FK
FOREIGN KEY ("author") REFERENCES "appointment"("appointmentid") ON DELETE CASCADE;

ALTER TABLE "task" ADD CONSTRAINT TASK_RESPONSIBILITY_TO_APPOINTMENT_FK
FOREIGN KEY ("reponsibility") REFERENCES "appointment"("appointmentid") ON DELETE CASCADE;

create table "taskresponse"
(
    "taskresponseid" INTEGER not null primary key,
    "task" INTEGER not null,
    "author" INTEGER not null,
    "response" VARCHAR(10000) not null,
    "deadline" TIMESTAMP,
    "timecreated" TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    "timemodified" TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE "taskresponse" ADD CONSTRAINT TASKRESPONSE_TASK_TO_TASK_FK
FOREIGN KEY ("task") REFERENCES "task"("taskid") ON DELETE CASCADE;

ALTER TABLE "taskresponse" ADD CONSTRAINT TASKRESPONSE_AUTHOR_TO_APPOINTMENT_FK
FOREIGN KEY ("author") REFERENCES "appointment"("appointmentid") ON DELETE CASCADE;

