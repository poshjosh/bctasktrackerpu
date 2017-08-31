SELECT DISTINCT t1.taskid AS a1, t1.description AS a2, t1.timeclosed AS a3, t1.timecreated AS a4, 
t1.timemodified AS a5, t1.timeopened AS a6, t1.author AS a7, t1.doc AS a8, t1.reponsibility AS a9 
FROM task t1 LEFT OUTER JOIN taskresponse t2 ON (t2.task = t1.taskid), doc t0 
WHERE (((((t1.description LIKE '%Query on 12 Apr%' OR t0.subject LIKE '%Query on 12 Apr%') 
OR t0.referencenumber LIKE '%Query on 12 Apr%') OR t2.response LIKE '%Query on 12 Apr%') 
AND (t1.timeclosed IS NULL)) AND (t0.docid = t1.doc)) ORDER BY t1.taskid DESC LIMIT 0, 100;

SELECT DISTINCT COUNT(taskid) FROM task t1 LEFT OUTER JOIN taskresponse t2 ON (t2.task = t1.taskid), doc t0;

drop table if exists worker;
drop table if exists job;
create table job
(
    jobid INTEGER(8) AUTO_INCREMENT not null primary key,
    jobtitle VARCHAR(100) not null UNIQUE,
    jobstartdate TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    jobenddate DATETIME null 

)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;
insert into job VALUES(null, 'Chief of the Air Staff', null, null);
insert into job VALUES(null, 'Chief of Administration', null, null);

--Query for 'query_today' report
SELECT DISTINCT taskid AS a1, description AS a2, timeclosed AS a3, timecreated AS a4, timemodified AS a5, timeopened AS a6, author AS a7, doc AS a8, reponsibility AS a9 FROM task WHERE (timeclosed IS NULL) ORDER BY taskid DESC LIMIT 0, 100;


create table worker
(
    workerid INTEGER(8) AUTO_INCREMENT not null primary key,
    workername VARCHAR(100) not null UNIQUE,
    job INTEGER(8) null, 

    FOREIGN KEY (job) REFERENCES job(jobid) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci;
insert into worker VALUES(null, 'Somebody', 1);

select * from job;

