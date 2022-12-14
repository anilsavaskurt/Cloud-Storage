package com.udacity.jwdnd.course1.cloudstorage.requests;

import lombok.Data;

@Data
public class NoteFormRequest {
    private String noteId;
    private String title;
    private String description;
}
