package com.heiko.textrpgcreator;

import com.heiko.textrpgcreator.controller.node.ChoiceEditorController;
import com.heiko.textrpgcreator.controller.node.Controller;
import com.heiko.textrpgcreator.controller.node.DragableScenarioController;
import com.heiko.textrpgcreator.controller.node.ScenarioEditorController;
import com.heiko.textrpgcreator.controller.node.WindowController;
import com.heiko.textrpgcreator.controller.ui.DragDropController;
import com.heiko.textrpgcreator.controller.ui.MouseController;
import com.heiko.textrpgcreator.controller.ui.ShortcutController;
import com.heiko.textrpgcreator.scenario.Arrow;
import com.heiko.textrpgcreator.scenario.Choice;
import com.heiko.textrpgcreator.scenario.Scenario;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private static WindowController windowController;

    private static List<DragableScenarioController> dragableScenarioControllers = new ArrayList<DragableScenarioController>();

    private static List<Scenario> scenarios = new ArrayList<Scenario>();

    private static ShortcutController shortcutController = new ShortcutController();

    private static MouseController mouseController = new MouseController();

    private static DragDropController dragDropController = new DragDropController();

    private static Scenario currentEdit;

    private static Choice currentChoice;

    private static List<Node> markedNodes = new ArrayList<>();

    private static boolean arrowExists = false;

    private static ChoiceEditorController choiceEditorController;

    private static ScenarioEditorController scenarioEditorController;

    private static Stage editorStage;

    private static Scene editorScene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Window"));
        scene.getStylesheets().add(App.class.getResource("MainCSS.css").toExternalForm());
        stage.setScene(scene);
        addKeyListeners();
        setMouseListeners();
        stage.show();
    }

    public static void addMarkedNode(Node node, boolean isShiftPressed) {
        if(!isShiftPressed) {
            clearMarkedNodes();
        }
        if(!markedNodes.contains(node)) {
            if(node.toString().contains("coverPane")) {
                markPane(node);
            }
            markedNodes.add(node);
        } else {
            if(node.toString().contains("coverPane")) {
                removeMarkedNode(node, "coverPane");
            }
        }
    }

    private static void markArrow(Arrow a) {
        a.setMarked();
    }

    private static void unmarkArrow(Arrow a) {
        a.removeMarked();
    }

    private static void markPane(Node node) {
        App.getScenarios().forEach((t) -> {
            if(t.getDragableScenarioController().getCoverPane() == node) {
                t.getDragableScenarioController().setMarked();
                App.getDragDropController().getMarkedPaneScenarios().add(t);
            }
        });
    }

    private static void unmarkPane(Node node) {
        App.getScenarios().forEach((t) -> {
            if(t.getDragableScenarioController().getCoverPane() == node) {
                t.getDragableScenarioController().removeMarked();
                App.getDragDropController().getMarkedPaneScenarios().remove(t);
            }
        });
    }

    public static void removeMarkedNode(Node node, String d) {
        markedNodes.remove(node);
        if(d.equals("coverPane")) {
            unmarkPane(node);
        }
    }

    public static void clearMarkedNodes() {
        markedNodes.forEach((t) -> {
            if(t.toString().contains("Line") || t.toString().contains("Circle")) {
                unmarkArrow((Arrow) t);
            } else if(t.toString().contains("coverPane")) {
                unmarkPane(t);
            }
        });
        markedNodes.clear();
    }

    public static void markAllNodes() {
        clearMarkedNodes();
        dragableScenarioControllers.forEach((t) -> {
            markedNodes.add(t.getCoverPane());
            markPane(t.getCoverPane());
        });
    }

    public static Scenario findScenario(AnchorPane pane) {
        scenarios.forEach((t) -> {
            if(t.getDragableScenarioController().getAnchorParentPane() == pane) {
                currentEdit = t;
            }
        });
        return currentEdit;
    }

    public static Choice findScenario(Node node) {
        App.getScenarios().forEach((t) -> {
            t.getIncomingChoices().forEach((b) -> {
                if(b.getChoiceArrow().getLine() == node || b.getChoiceArrow().getCircle() == node) {
                    currentChoice = b;
                }
            });
            if(currentChoice == null) {
                t.getOutgoingChoices().forEach((b) -> {
                    if(b.getChoiceArrow().getLine() == node || b.getChoiceArrow().getCircle() == node) {
                        currentChoice = b;
                    }
                });
            }
        });
        return currentChoice;
    }

    public static boolean arrowAllreadyExists(AnchorPane startPane, AnchorPane targetPane) {
        Scenario startScenario = findScenario(startPane);
        Scenario targetScenario = findScenario(targetPane);
        startScenario.getOutgoingChoices().forEach((s) -> {
            if(s.getEndScenario() == targetScenario) {
                arrowExists = true;
            }
        });
        return arrowExists;
    }

    private void addKeyListeners() {
        scene.setOnKeyPressed(shortcutController.getKeyPressed());
        scene.setOnKeyReleased(shortcutController.getKeyReleased());
    }

    private void setMouseListeners() {
        scene.setOnScroll(App.getMouseController().getMouseScrolled());
        scene.setOnMousePressed(mouseController.getMousePressed());
        scene.setOnMouseDragged(mouseController.getMouseDragged());
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
            return fxmlLoader.load();
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Controller loadFXMLController(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.load(App.class.getResource(fxml + ".fxml").openStream());
            return fxmlLoader.getController();
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void openEditor(String fxml, Scenario scenario, Choice choice) {
        System.out.println(fxml);
        editorStage = new Stage();
        editorScene = new Scene(loadFXML(fxml));
        editorScene.getStylesheets().clear();
        editorScene.getStylesheets().add(App.class.getResource("MainCSS.css").toExternalForm());
        editorStage.setScene(editorScene);
        editorStage.setResizable(false);
        if(fxml.equals("ScenarioEditor")) {
            scenarioEditorController.openScenarioEditor(scenario);
            editorStage.setOnCloseRequest(e -> {
                scenarioEditorController.closeScenarioEditor();
            });
        } else {
            choiceEditorController.openChoiceEditor(choice);
            editorStage.setOnCloseRequest(e -> {
                choiceEditorController.closeChoiceEditor();
            });
        }
        editorStage.show();
    }

    public static void closeEditor() {
        editorStage.close();
    }

    public static void main(String[] args) {
        launch();
    }

    public static WindowController getWindowController() {
        return windowController;
    }

    public static void setWindowController(WindowController windowController) {
        App.windowController = windowController;
    }

    public static List<DragableScenarioController> getDragableScenarioControllers() {
        return dragableScenarioControllers;
    }

    public static MouseController getMouseController() {
        return mouseController;
    }

    public static ShortcutController getShortcutController() {
        return shortcutController;
    }

    public static List<Scenario> getScenarios() {
        return scenarios;
    }

    public static DragDropController getDragDropController() {
        return dragDropController;
    }

    public static Scenario getCurrentEdit() {
        return currentEdit;
    }

    public static Scene getScene() {
        return scene;
    }

    public static List<Node> getMarkedNodes() {
        return markedNodes;
    }

    public static boolean isArrowExists() {
        return arrowExists;
    }

    public static void setArrowExists(boolean arrowExists) {
        App.arrowExists = arrowExists;
    }

    public static ChoiceEditorController getChoiceEditorController() {
        return choiceEditorController;
    }

    public static void setChoiceEditorController(ChoiceEditorController choiceEditorController) {
        App.choiceEditorController = choiceEditorController;
    }

    public static ScenarioEditorController getScenarioEditorController() {
        return scenarioEditorController;
    }

    public static void setScenarioEditorController(ScenarioEditorController scenarioEditorController) {
        App.scenarioEditorController = scenarioEditorController;
    }

    public static void setCurrentEdit(Scenario currentEdit) {
        App.currentEdit = currentEdit;
    }

    public static Choice getCurrentChoice() {
        return currentChoice;
    }

    public static void setCurrentChoice(Choice currentChoice) {
        App.currentChoice = currentChoice;
    }
}
