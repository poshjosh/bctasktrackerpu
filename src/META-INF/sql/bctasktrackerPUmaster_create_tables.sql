drop table if exists unit;
create table unit
(
    unitid INTEGER(8) AUTO_INCREMENT not null primary key,
    unit VARCHAR(100) not null UNIQUE,
    abbreviation VARCHAR(100) null,
    parentunit INTEGER(8) null,

    FOREIGN KEY (parentunit) REFERENCES unit(unitid) ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

drop table if exists appointment;
create table appointment
(
    appointmentid INTEGER(8) AUTO_INCREMENT not null primary key,
    appointment VARCHAR(100) not null UNIQUE,
    abbreviation VARCHAR(100) null,
    parentappointment INTEGER(8) null,
    unit INTEGER(8) not null, 

    FOREIGN KEY (parentappointment) REFERENCES appointment(appointmentid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (unit) REFERENCES unit(unitid) ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

drop table if exists doc;
create table doc
(
    docid INTEGER(8) AUTO_INCREMENT not null primary key,
    datesigned DATE null,
    referencenumber VARCHAR(255) null,
    subject VARCHAR(1000) not null,                  
    timecreated TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    timemodified TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

drop table if exists task;
create table task
(
    taskid INTEGER(8) AUTO_INCREMENT not null primary key,
    doc INTEGER(8) not null,
    description VARCHAR(10000) null,
    author INTEGER(8) not null,
    reponsibility INTEGER(8) not null,
    timeopened DATETIME null,
    timeclosed DATETIME null, 
    timecreated TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    timemodified TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (doc) REFERENCES doc(docid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (author) REFERENCES appointment(appointmentid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (reponsibility) REFERENCES appointment(appointmentid) ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

drop table if exists taskresponse;
create table taskresponse
(
    taskresponseid INTEGER(8) AUTO_INCREMENT not null primary key,
    task INTEGER(8) not null,
    author INTEGER(8) not null,
    response VARCHAR(10000) not null,
    deadline TIMESTAMP null,
    timecreated TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    timemodified TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (task) REFERENCES task(taskid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (author) REFERENCES appointment(appointmentid) ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;
 


