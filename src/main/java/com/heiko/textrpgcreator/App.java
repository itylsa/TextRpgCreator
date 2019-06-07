package com.heiko.textrpgcreator;

import com.heiko.textrpgcreator.controller.node.ChoiceEditorController;
import com.heiko.textrpgcreator.controller.node.Controller;
import com.heiko.textrpgcreator.controller.node.DeleteChoiceController;
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
import javafx.stage.WindowEvent;

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

    private static FileController fileController = new FileController();

    private static Scenario currentEdit;

    private static Choice currentChoice;

    private static List<Node> markedNodes = new ArrayList<>();

    private static boolean arrowExists = false;

    private static ChoiceEditorController choiceEditorController;

    private static ScenarioEditorController scenarioEditorController;

    private static Stage editorStage;

    private static Scene editorScene;

    private static Stage choiceStage;

    private static Scene choiceScene;

    private static List<Scenario> scenariosToDelete = new ArrayList<>();

    private static List<Choice> choicesToDelete = new ArrayList<>();

    private static boolean readyForDelete = false;

    private static DeleteChoiceController deleteChoiceController;

    private static int highestId = 0;

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

    public static void deleteMarkedScenarios() {
        if(!readyForDelete) {
            markedNodes.forEach((n) -> {
                if(n.toString().contains("coverPane")) {
                    scenariosToDelete.add(findScenario((AnchorPane) n.getParent()));
                }
            });
            deleteScenarios();
            openDeleteChoice("DeleteChoice");
        } else {
            deleteScenarios();
            markedNodes.clear();
            scenariosToDelete.clear();
            choicesToDelete.clear();
            readyForDelete = false;
        }
    }

    private static void deleteScenarios() {
        scenariosToDelete.forEach((s) -> {
            deleteScenario(s);
        });
    }

    private static void deleteScenario(Scenario s) {
        if(!readyForDelete) {
            s.getIncomingChoices().forEach((c) -> {
                choicesToDelete.add(c);
            });
            s.getOutgoingChoices().forEach((c) -> {
                choicesToDelete.add(c);
            });
        } else {
            choicesToDelete.forEach((c) -> {
                deleteArrow(c.getChoiceArrow());
                c.getStartScenario().getOutgoingChoices().remove(c);
                c.getEndScenario().getIncomingChoices().remove(c);
            });
            scenariosToDelete.forEach((sc) -> {
                windowController.getScalingPane().getChildren().remove(sc.getDragableScenarioController().getAnchorParentPane());
                scenarios.remove(sc);
                sc = null;
            });
        }
    }

    private static void deleteArrow(Arrow a) {
        windowController.getScalingPane().getChildren().remove(a.getLine());
        windowController.getScalingPane().getChildren().remove(a.getCircle());
        a = null;
    }

    public static void clearScenariosToDelete() {
        scenariosToDelete.clear();
        choicesToDelete.clear();
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
        if(editorStage != null) {
            App.getEditorStage().fireEvent(new WindowEvent(App.getEditorStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
        }
        editorStage = new Stage();
        editorScene = new Scene(loadFXML(fxml));
        editorScene.setOnKeyPressed(shortcutController.getEscPressed());
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
        editorStage = null;
    }

    public static void openDeleteChoice(String fxml) {
        choiceStage = new Stage();
        choiceScene = new Scene(loadFXML(fxml));
        choiceScene.getStylesheets().clear();
        choiceScene.getStylesheets().add(App.class.getResource("MainCSS.css").toExternalForm());
        choiceStage.setScene(choiceScene);
        choiceStage.setResizable(false);
        if(fxml.equals("DeleteChoice")) {
            deleteChoiceController.openDeleteChoice();
            choiceStage.setOnCloseRequest((e) -> {
                deleteChoiceController.closeDeleteChoice();
            });
        }
        choiceStage.show();
    }

    public static void closeDeleteChoice() {
        choiceStage.close();
        choiceStage = null;
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

    public static boolean isReadyForDelete() {
        return readyForDelete;
    }

    public static void setReadyForDelete(boolean readyForDelete) {
        App.readyForDelete = readyForDelete;
    }

    public static List<Scenario> getScenariosToDelete() {
        return scenariosToDelete;
    }

    public static DeleteChoiceController getDeleteChoiceController() {
        return deleteChoiceController;
    }

    public static void setDeleteChoiceController(DeleteChoiceController deleteChoiceController) {
        App.deleteChoiceController = deleteChoiceController;
    }

    public static List<Choice> getChoicesToDelete() {
        return choicesToDelete;
    }

    public static Stage getEditorStage() {
        return editorStage;
    }

    public static Scene getEditorScene() {
        return editorScene;
    }

    public static Stage getChoiceStage() {
        return choiceStage;
    }

    public static Scene getChoiceScene() {
        return choiceScene;
    }

    public static FileController getFileController() {
        return fileController;
    }

    public static int getHighestId() {
        return highestId;
    }

    public static void setHighestId(int highestId) {
        App.highestId = highestId;
    }
}
