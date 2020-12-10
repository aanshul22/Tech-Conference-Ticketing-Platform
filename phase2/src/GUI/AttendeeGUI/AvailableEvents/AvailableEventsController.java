package GUI.AttendeeGUI.AvailableEvents;

import GUI.*;
import GUI.AttendeeGUI.EventHolder;
import UseCases.EventManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AvailableEventsController extends DisplayEventsController {
    private EventManager eventManager;

    public void initialize(){
        this.eventManager = ManagersStorage.getInstance().getEventManager();
        LocalDateTime currTime = LocalDateTime.now();
        List<UUID> availableEventIDs = eventManager.getAvailableEvents(currTime);
        List<List<String>> eventsInfo = eventManager.getAllEventsInfo(availableEventIDs);
        generateEventButtons("EventSignUp", eventsInfo);
    }
}
