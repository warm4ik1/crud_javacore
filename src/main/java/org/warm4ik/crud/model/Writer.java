package org.warm4ik.crud.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.warm4ik.crud.status.Status;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class Writer {
    private Long id;
    private String firstName;
    private String lastName;
    private Status status;
    List<Post> posts;
}
