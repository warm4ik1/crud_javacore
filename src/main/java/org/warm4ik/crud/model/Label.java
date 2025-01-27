package org.warm4ik.crud.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.warm4ik.crud.status.Status;

@Getter
@Setter
@ToString
@Builder
public class Label {
    private Long id;
    private String name;
    private Status status;
}
