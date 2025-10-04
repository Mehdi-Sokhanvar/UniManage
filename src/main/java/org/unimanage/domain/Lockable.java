package org.unimanage.domain;

public interface Lockable {
    Long getVersion();
    void setVersion(Long version);
}
