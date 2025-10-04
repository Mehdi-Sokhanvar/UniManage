package org.unimanage.util.enumration;

public enum TermStatus {
    PLANNED,    //Term created, not started yet.
    COURSE_OFFERING, //Courses are being offered by departments.
    REGISTRATION, //Students can register or drop courses.
    ONGOING, //Classes are running.
    CLOSED //Term ended, archived.
}
