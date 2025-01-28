package org.warm4ik.crud.model;

import lombok.*;
import org.warm4ik.crud.status.Status;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class Label {
    private Long id;
    private String name;
    private Status status;

    public Label(String name, Status status) {
        this.name = name;
        this.status = status;
    }
}
