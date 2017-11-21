package com.todo.waqas.todo.modal;



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

}
