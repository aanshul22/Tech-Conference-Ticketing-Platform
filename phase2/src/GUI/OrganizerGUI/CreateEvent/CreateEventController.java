package GUI.OrganizerGUI.CreateEvent;

import GUI.EventHolder;
import GUI.ManagersStorage;
import GUI.UserHolder;
import UseCases.EventManager;
import UseCases.RoomManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class CreateEventController {

    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private TextField roomField;
    @FXML private TextField eventCapacityField;
    @FXML private TextField eventNameField;
    private RoomManager roomManager;
    private EventManager eventManager;
    private String eventName ;
    private String startTimeString;
    private String endTimeString;
    private String roomName;
    private int eventCapacity;
    private String username;


    /**
     * Initializes the Create Event scene.
     */
    @FXML public void initialize(){
        EventHolder.getInstance().setRoomAvailabilityChecked(false);
        roomManager  = ManagersStorage.getInstance().getRoomManager();
        eventManager = ManagersStorage.getInstance().getEventManager();
        username = UserHolder.getInstance().getUsername();
    }

    /**
     * Handles action when the create button is clicked. Creates the event.
     */
    @FXML protected void handleCreateButtonAction() {
        eventName = eventNameField.getText();
        startTimeString = startTimeField.getText();
        endTimeString = endTimeField.getText();
        roomName = roomField.getText();
        if(missingInput()){
            createAlertMessage("Please fill in all boxes");
            return;
        }
        if(!validTime(startTimeString) || !validTime(endTimeString)){
            createAlertMessage("Invalid Time Input");
            return;
        }
        LocalDateTime startTime = getTime(startTimeString);
        LocalDateTime endTime = getTime(endTimeString);

        eventCapacity = eventCapacityFieldToInteger(eventCapacityField.getText());
        if(roomAvailabilityChecked()){
            UUID eventID = eventManager.addEvent(eventName, username, startTime, endTime, roomName, eventCapacity);
            roomManager.addEventToSchedule(eventID, roomName, startTime, endTime);
            createAlertMessage("Event Created");
        }
        else{
            createAlertMessage("Please check availability of the room first.");
        }

    }

    /**
     * Handles action when the check availability button is clicked. Checks if room is available for this event.
     */
    @FXML  protected void handleCheckAvailabilityButtonAction(ActionEvent event) {
        roomName = roomField.getText();
        String eventCapacityString = eventCapacityField.getText();
        startTimeString = startTimeField.getText();
        endTimeString = endTimeField.getText();

        roomAvailablilityChecker(startTimeString, endTimeString, eventCapacityString,roomName);

        LocalDateTime startTime = getTime(startTimeString);
        LocalDateTime endTime = getTime(endTimeString);

        if(!roomManager.canAddEvent(roomName, startTime, endTime)){
            createAlertMessage("This room is not available at this time");
            return;
        }
        EventHolder.getInstance().setRoomAvailabilityChecked(true);
        createAlertMessage("This room can host this event!");

    }

    private boolean missingInput(){
        boolean eventNameEmpty = eventName.isEmpty();
        boolean startTimeEmpty = startTimeString.isEmpty();
        boolean endTimeEmpty = endTimeString.isEmpty();
        boolean eventCapacityEmpty = eventCapacityField.getText().isEmpty();
        boolean roomNameEmpty = roomField.getText().isEmpty();
        return(eventNameEmpty || startTimeEmpty || endTimeEmpty || eventCapacityEmpty || roomNameEmpty);
    }

    private void roomAvailablilityChecker(String startTimeString, String endTimeString, String eventCapacityString, String roomName){
        if(startTimeString.isEmpty()||endTimeString.isEmpty()){
            createAlertMessage("Please enter time");
            return;
        }
        if(eventCapacityString.isEmpty()){
            createAlertMessage("Please enter event capacity");
            return;
        }
        if(!roomManager.roomExists(roomName)){
            createAlertMessage("This room does not exist");
            return;
        }
        eventCapacity = eventCapacityFieldToInteger(eventCapacityString);
        if (!roomManager.hasSpace(roomName, eventCapacity)){
            createAlertMessage("This event capacity exceeds the room capacity. Please choose another room");
            return;
        }
        if(!validTime(startTimeString) || !validTime(endTimeString)){
            createAlertMessage("Invalid Time Input");
        }
    }

    private boolean roomAvailabilityChecked(){
        return EventHolder.getInstance().getRoomAvailabilityChecked();
    }

    private int eventCapacityFieldToInteger(String eventCapacityField){
        return Integer.parseInt(eventCapacityField);
    }

    private boolean validTime(String time){
        String pattern = "^([0-9][0-9][0-9][0-9])-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1]) (09|1[0-6]):([0-5][0-9])$|^.{0}$";

        return time.matches(pattern);
    }

    private LocalDateTime getTime(String time) {
        int year = Integer.parseInt(time.substring(0, 4));
        int month = Integer.parseInt(time.substring(5, 7));
        int day = Integer.parseInt(time.substring(8, 10));
        int hour = Integer.parseInt(time.substring(11, 13));
        int minute = Integer.parseInt(time.substring(14, 16));
        return LocalDateTime.of(year, month, day, hour, minute);
    }
    private void createAlertMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}