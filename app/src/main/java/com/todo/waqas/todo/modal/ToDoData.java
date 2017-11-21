package com.todo.waqas.todo.modal;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class ToDoData {
    int ToDoID;
    String ToDoTaskDetails, ToDoTaskPriority, ToDoTaskStatus, ToDoNotes, ToDoCreationDate, ToDoAuthor ;

    public String getToDoCreationDate() {
        return ToDoCreationDate;
    }

    public void setToDoCreationDate(String toDoCreationDate) {
        ToDoCreationDate = toDoCreationDate;
    }

    public String getToDoAuthor() {
        return ToDoAuthor;
    }

    public void setToDoAuthor(String toDoAuthor) {
        ToDoAuthor = toDoAuthor;
    }

    public int getToDoID() {
        return ToDoID;
    }

    public void setToDoID(int toDoID) {
        ToDoID = toDoID;
    }

    public String getToDoTaskDetails() {
        return ToDoTaskDetails;
    }

    public void setToDoTaskDetails(String toDoTaskDetails) {
        ToDoTaskDetails = toDoTaskDetails;
    }

    public String getToDoTaskPriority() {
        return ToDoTaskPriority;
    }

    public void setToDoTaskPriority(String toDoTaskPriority) {
        ToDoTaskPriority = toDoTaskPriority;
    }

    public String getToDoTaskStatus() {
        return ToDoTaskStatus;
    }

    public void setToDoTaskStatus(String toDoTaskStatus) {
        ToDoTaskStatus = toDoTaskStatus;
    }

    public String getToDoNotes() {
        return ToDoNotes;
    }

    public void setToDoNotes(String toDoNotes) {
        ToDoNotes = toDoNotes;
    }

    @Override
    public String toString() {
        return "ToDoData {id-" + ToDoID + ", taskDetails-" + ToDoTaskDetails + ", priority-" + ToDoTaskPriority + ", status-" + ToDoTaskStatus + ", notes-" + ToDoNotes +
                ", creationDate-" + ToDoCreationDate +", author-" + ToDoAuthor +"}";
    }

    public ToDoData(String toDoTaskDetails, String toDoAuthor, String toDoTaskStatus, String toDoNotes, String toDoTaskPriority) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        this.ToDoCreationDate = sdf.format(new Date());
        this.ToDoTaskDetails = toDoTaskDetails;
        this.ToDoAuthor = toDoAuthor;
        this.ToDoTaskStatus = toDoTaskStatus;
        this.ToDoNotes = toDoNotes;
        this.ToDoTaskPriority = toDoTaskPriority;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ToDoTaskDetails", ToDoTaskDetails);
        result.put("ToDoNotes", ToDoNotes);
        result.put("ToDoCreationDate", ToDoCreationDate);
        result.put("ToDoTaskStatus", ToDoTaskStatus);

        return result;
    }

}
