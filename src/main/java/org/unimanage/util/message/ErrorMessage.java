package org.unimanage.util.message;

public enum ErrorMessage {
    ROLE_NOT_FOUND("Role %s not found"),
    USER_ALREADY_HAS_ROLE("User already has role %s"),
    PERSON_EXISTS("Person with NationalCode %s already exists"),
    MAJOR_NOT_FOUND("Major with name %s not found"),
    USERNAME_NOT_FOUND("Username %s not found"),
    ACCOUNT_NOT_ACTIVE("Account %s not active"),
    ACCOUNT_NOT_FOUND("Account %s not found"),
    PERSON_NOT_FOUND("Person %s not found"),
    PASSWORD_NOT_MATCH("The password does not match the current password."),
    ENTITY_NOT_FOUND("Entity not found with ID: %d"),
    COURSE_ALREADY_EXISTS("Course with the same name already exists in this major"),
    MAJOR_INACTIVE("Cannot add course to inactive major"),
    DUPLICATE_MAJOR_NAME("Major already exists with name: %s"), COURSE_NOT_ACTIVE("Course %s not active"),
    COURSE_NOT_EXIST_IN_COURSE("Course with name %s not found in major  "),
    TIME_SELECT_UNIT_NOT_STARTED("Select unit term %s not started "),
    YOU_CAN_CHOOSE_COURSE("You can't choose course for this course"),
    THE_CAPACITY_IS_FULL("%s is not capable of"),
    TERM_EXCEPTION("Term status {0} is not valid for this operation"),
    START_TIME_INVALID("Start time {0} is invalid"),
    END_TIME_INVALID("End time {0} cannot be before start time"),
    TIME_MISSING("Start time or end time is missing for term {0}"),
    ;


    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(this.message, args);
    }
}
