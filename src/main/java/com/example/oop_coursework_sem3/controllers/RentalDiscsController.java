package com.example.oop_coursework_sem3.controllers;

import com.example.oop_coursework_sem3.Rental;
import com.example.oop_coursework_sem3.RentalUser;
import com.example.oop_coursework_sem3.VideoDisc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RentalDiscsController implements Initializable {

    public static final File USERS_FILE = new File("inputUsers.txt");
    public static final File DISCS_FILE = new File("inputDiscs.txt");
    public static final File RENTALS_FILE = new File("inputRentals.txt");

    public static final long RECORD_LENGTH = 100;

    private Alert emptyFieldsAlert = new Alert(Alert.AlertType.ERROR);
    private Alert changesWasApplied = new Alert(Alert.AlertType.INFORMATION);
    private Alert alreadyExistsAlert = new Alert(Alert.AlertType.ERROR);
    private Alert discIsRented = new Alert(Alert.AlertType.ERROR);
    private Alert userHaveRentals = new Alert(Alert.AlertType.ERROR);
    private Alert discInRent = new Alert(Alert.AlertType.ERROR);
    private Alert mustContainsNumber =  new Alert(Alert.AlertType.ERROR);
    private Alert hasExpiredRentals = new Alert(Alert.AlertType.ERROR);

    private Stage stage;
    private Scene scene;
    private Parent root;

    protected ObservableList<RentalUser> userList = FXCollections.observableArrayList();

    protected ObservableList<VideoDisc> videoDiscList = FXCollections.observableArrayList();

    protected ObservableList<Rental> rentalList = FXCollections.observableArrayList();

    public String paddingRight(String line) {
        StringBuilder result = new StringBuilder(100);
        if (line != null) {
            result.append(line);
            for (int i = 0; i < RECORD_LENGTH - line.length(); i++) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    public void changeRentals() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RENTALS_FILE))) {
            writer.write("\n");
            for (Rental r : rentalList) {
                writer.write(r.toString() + "\n");
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToMainScene (ActionEvent event) throws IOException {
        mainTabPane.getSelectionModel().select(0);
        tab1.setDisable(false);
        tab2.setDisable(false);
        tab3.setDisable(false);
        tab4.setDisable(true);
        tab5.setDisable(true);
    }

    @FXML
    public void switchToDiscSelectingScene (ActionEvent event) throws IOException {
        mainTabPane.getSelectionModel().select(3);
        tab1.setDisable(true);
        tab2.setDisable(true);
        tab3.setDisable(true);
        tab4.setDisable(false);
        RentalUser selectedUser = usersTable_tab1.getSelectionModel().getSelectedItem();
        userInfoField_addRent.setText(selectedUser.getSurname() + " " + selectedUser.getUserName() + " " +
                selectedUser.getPatronymic() + ", " + selectedUser.getUserPhone());

        List<Integer> rentalDiscsId = rentalList.stream().mapToInt(Rental::getDiscId).boxed().collect(Collectors.toList());
        discsTable_addRent.setItems(videoDiscList.filtered(el -> !rentalDiscsId.contains(el.getDiscId())));
    }

    @FXML
    public void switchToViewRentalsScene (ActionEvent event) throws IOException {
        mainTabPane.getSelectionModel().select(4);
        tab1.setDisable(true);
        tab2.setDisable(true);
        tab3.setDisable(true);
        tab5.setDisable(false);
        RentalUser selectedUser = usersTable_tab1.getSelectionModel().getSelectedItem();
        userInfoField_viewRentals.setText(selectedUser.getSurname() + " " + selectedUser.getUserName() + " " +
                selectedUser.getPatronymic() + ", " + selectedUser.getUserPhone());

        rentalTable_viewRentals.setItems(rentalList.filtered(el -> el.getUserPhone().equals(userInfoField_viewRentals.getText().split(" ")[3])));
        rentalTable_viewRentals.refresh();
    }

    @FXML
    private AnchorPane rentalUsersPane;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Tab tab1;

    @FXML
    private Tab tab2;

    @FXML
    private Tab tab3;

    @FXML
    private Tab tab4;

    @FXML
    private Tab tab5;

    @FXML
    private TextField inpAddress_tab1;

    @FXML
    private TextField inpName_tab1;

    @FXML
    private TextField inpPatronymic_tab1;

    @FXML
    private TextField inpPhone_tab1;

    @FXML
    private TextField inpSurname_tab1;

    @FXML
    private TextField inputSearchPar_tab1;

    @FXML
    private TableView<RentalUser> usersTable_tab1;

    @FXML
    private TableColumn<RentalUser, String> addressCol_tab1;

    @FXML
    private TableColumn<RentalUser, String> nameCol_tab1;

    @FXML
    private TableColumn<RentalUser, String> patronymicCol_tab1;

    @FXML
    private TableColumn<RentalUser, String> phoneCol_tab1;

    @FXML
    private TableColumn<RentalUser, String> surnameCol_tab1;

    @FXML
    private Button clearFields_tab1;

    @FXML
    void clearFields_tab1_event(MouseEvent event) {
        inpSurname_tab1.setText("");
        inpName_tab1.setText("");
        inpPatronymic_tab1.setText("");
        inpAddress_tab1.setText("");
        inpPhone_tab1.setText("");
    }

    @FXML
    private Button addNewUser_tab1;

    @FXML
    void addNewUser_tab1_event(MouseEvent event) {
        if (inpSurname_tab1.getText().equals("") || inpName_tab1.getText().equals("")
            || inpPatronymic_tab1.getText().equals("") || inpAddress_tab1.getText().equals("")
            || inpPhone_tab1.getText().equals("")) {
            emptyFieldsAlert.showAndWait();
        } else if (userList.contains(new RentalUser(
                inpSurname_tab1.getText(),
                inpName_tab1.getText(),
                inpPatronymic_tab1.getText(),
                inpAddress_tab1.getText(),
                inpPhone_tab1.getText()
        ))) {
            alreadyExistsAlert.showAndWait();
        } else {
            RentalUser user = new RentalUser(
                    inpSurname_tab1.getText(),
                    inpName_tab1.getText(),
                    inpPatronymic_tab1.getText(),
                    inpAddress_tab1.getText(),
                    inpPhone_tab1.getText()
            );
            userList.add(user);
            usersTable_tab1.setItems(userList);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
                if (USERS_FILE.length() == 0) writer.write("\n");
                writer.write(paddingRight(user.toString()) + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private Button changeUser_tab1;

    @FXML
    void changeUser_tab1_event(MouseEvent event) {
        int selectedId = usersTable_tab1.getSelectionModel().getSelectedIndex();
        if (selectedId <= -1) return;

        if (inpSurname_tab1.getText().equals("") || inpName_tab1.getText().equals("")
                || inpPatronymic_tab1.getText().equals("") || inpAddress_tab1.getText().equals("")
                || inpPhone_tab1.getText().equals("")) {
            emptyFieldsAlert.showAndWait();
        } else {
//TODO
            RentalUser selectedUser = usersTable_tab1.getItems().get(selectedId);
            String selectedUserPhoneBeforeChanging = selectedUser.getUserPhone();
            String selectedUserBeforeChanging = selectedUser.toString();

            selectedUser.setSurname(inpSurname_tab1.getText());
            selectedUser.setUserName(inpName_tab1.getText());
            selectedUser.setPatronymic(inpPatronymic_tab1.getText());
            selectedUser.setAddress(inpAddress_tab1.getText());
            selectedUser.setUserPhone(inpPhone_tab1.getText());

            try {
                RandomAccessFile usersFile = new RandomAccessFile(USERS_FILE, "rw");
                for (String line; (line = usersFile.readLine()) != null;) {
                    if (line.startsWith(selectedUserBeforeChanging)) {
                        usersFile.seek(usersFile.getFilePointer() - RECORD_LENGTH - 1);
                        usersFile.writeBytes(paddingRight(""));
                        usersFile.seek(usersFile.getFilePointer() - RECORD_LENGTH - 1);
                        usersFile.writeBytes( "\n" + selectedUser.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            rentalList.forEach(el -> {
                if (el.getUserPhone().equals(selectedUserPhoneBeforeChanging)) {
                    el.setUserPhone(selectedUser.getUserPhone());
                    rentalTable_tab3.refresh();
                    changeRentals();
                };
            });

            usersTable_tab1.refresh();
            changesWasApplied.showAndWait();
        }
    }

    @FXML
    private Button delUser_tab1;

    @FXML
    void delUser_tab1_event(MouseEvent event) {
        int selectedID = usersTable_tab1.getSelectionModel().getSelectedIndex();
        RentalUser selectedUser = usersTable_tab1.getSelectionModel().getSelectedItem();
        if (selectedID <= -1) return;
        if (rentalList.stream().map(Rental::getUserPhone).collect(Collectors.toList()).contains(usersTable_tab1.getItems().get(selectedID).getUserPhone())) {
            userHaveRentals.showAndWait();
        } else {
            if (selectedID <= -1) return;
//TODO
            try {
                RandomAccessFile usersFile = new RandomAccessFile(USERS_FILE, "rw");
                for (String line; (line = usersFile.readLine()) != null;) {
                    if (line.startsWith(selectedUser.toString())) {
                        usersFile.seek(usersFile.getFilePointer() - RECORD_LENGTH - 1);
                        usersFile.writeBytes(paddingRight(""));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            usersTable_tab1.getItems().remove(selectedID);
        }
    }

    @FXML
    private ComboBox<String> searchCombobox_tab1;

    @FXML
    void changeCombo_tab1_event(ActionEvent event) {
        if (searchCombobox_tab1.getValue().equals("не выбрано")) {
            inputSearchPar_tab1.setDisable(true);
            inputSearchPar_tab1.setText("");
            find_tab1.setDisable(true);
            reset_tab1.setDisable(true);
        } else {
            inputSearchPar_tab1.setDisable(false);
            find_tab1.setDisable(false);
            reset_tab1.setDisable(false);
        }
    }

    @FXML
    private Button find_tab1;

    @FXML
    void findUser_tab1_event(MouseEvent event) {
        if (inputSearchPar_tab1.getText().equals("")) {
            emptyFieldsAlert.showAndWait();
        } else {
            ObservableList<RentalUser> finedUsers = FXCollections.observableArrayList();
            switch (searchCombobox_tab1.getValue()) {
                case "Фамилия":
                    userList.stream()
                            .filter(el -> el.getSurname().toLowerCase().equals(inputSearchPar_tab1.getText().toLowerCase()))
                            .forEach(el -> finedUsers.add(el));
                    usersTable_tab1.setItems(finedUsers);
                    break;

                case "Телефон":
                    userList.stream()
                            .filter(el -> el.getUserPhone().equals(inputSearchPar_tab1.getText()))
                            .forEach(el -> finedUsers.add(el));
                    usersTable_tab1.setItems(finedUsers);
                    break;

                default:
                    break;
            }
        }
    }



    @FXML
    void selectTableItem_event(MouseEvent event) {
        int selectedID = usersTable_tab1.getSelectionModel().getSelectedIndex();
        if (selectedID <= -1) return;
        inpSurname_tab1.setText(surnameCol_tab1.getCellData(selectedID));
        inpName_tab1.setText(nameCol_tab1.getCellData(selectedID));
        inpPatronymic_tab1.setText((patronymicCol_tab1.getCellData(selectedID)));
        inpAddress_tab1.setText(addressCol_tab1.getCellData(selectedID));
        inpPhone_tab1.setText(phoneCol_tab1.getCellData(selectedID));
        addRental_tab1.setDisable(false);
        viewRentals_tab1.setDisable(false);

    }

    @FXML
    private Button reset_tab1;

    @FXML
    void reset_tab1_event(MouseEvent event) {
        searchCombobox_tab1.setValue("не выбрано");
        inputSearchPar_tab1.setText("");
        usersTable_tab1.setItems(userList);
    }

    @FXML
    private Button addRental_tab1;

    @FXML
    private Button viewRentals_tab1;

    //----------------TAB_2--------------------

    @FXML
    private Button addNewDisc_tab2;

    @FXML
    private Button changeDisc_tab2;

    @FXML
    private Button clearFields_tab2;

    @FXML
    private Button delDisc_tab2;

    @FXML
    private TableView<VideoDisc> discsTable_tab2;

    @FXML
    private TableColumn<VideoDisc, String> discDirectorCol_tab2;

    @FXML
    private TableColumn<VideoDisc, String> discGenreCol_tab2;

    @FXML
    private TableColumn<VideoDisc, Integer> discIdCol_tab2;

    @FXML
    private TableColumn<VideoDisc, String> discNameCol_tab2;

    @FXML
    private TableColumn<VideoDisc, Integer> discReleaseCol_tab2;

    @FXML
    private TextField inpDiscDirector_tab2;

    @FXML
    private TextField inpDiscGenre_tab2;

    @FXML
    private TextField inpDiscName_tab2;

    @FXML
    private TextField inpDiscRelease_tab2;

    @FXML
    private Button reset_tab2;

    @FXML
    private TextField inputSearchPar_tab2;

    @FXML
    private ComboBox<String> searchCombobox_tab2;

    @FXML
    private Button find_tab2;

    public int checkForNum(String inpNum) {
        try {
            Integer.parseInt(inpNum);
            return 1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @FXML
    void addNewDisc_tab2_event(MouseEvent event) {
        if (inpDiscName_tab2.getText().equals("") || inpDiscGenre_tab2.getText().equals("")
                || inpDiscDirector_tab2.getText().equals("") || inpDiscRelease_tab2.getText().equals("")) {
            emptyFieldsAlert.showAndWait();
        } else if (checkForNum(inpDiscRelease_tab2.getText()) == -1) {
            mustContainsNumber.showAndWait();
        } else if (videoDiscList.contains(new VideoDisc(
                videoDiscList.size() + 1,
                inpDiscName_tab2.getText(),
                inpDiscGenre_tab2.getText(),
                Integer.parseInt(inpDiscRelease_tab2.getText()),
                inpDiscDirector_tab2.getText()
        ))) {
            alreadyExistsAlert.showAndWait();
        } else {
            VideoDisc videoDisc = new VideoDisc(
                    videoDiscList.size() + 1,
                    inpDiscName_tab2.getText(),
                    inpDiscGenre_tab2.getText(),
                    Integer.parseInt(inpDiscRelease_tab2.getText()),
                    inpDiscDirector_tab2.getText()
            );
            videoDiscList.add(videoDisc);
            discsTable_tab2.setItems(videoDiscList);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DISCS_FILE, true))) {
                if (DISCS_FILE.length() == 0) writer.write("\n");
                writer.write(paddingRight(videoDisc.toString()) + "\n");
            }  catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void changeCombo_tab2_event(ActionEvent event) {
        if (searchCombobox_tab2.getValue().equals("не выбрано")) {
            inputSearchPar_tab2.setDisable(true);
            find_tab2.setDisable(true);
            reset_tab2.setDisable(true);
        } else {
            inputSearchPar_tab2.setDisable(false);
            find_tab2.setDisable(false);
            reset_tab2.setDisable(false);
        }
    }

    @FXML
    void changeDisc_tab2_event(MouseEvent event) {
        int selectedId = discsTable_tab2.getSelectionModel().getSelectedIndex();
        String selectedDiscBeforeChanging = discsTable_tab2.getItems().get(selectedId).toString();
        VideoDisc selectedDisc = discsTable_tab2.getItems().get(selectedId);

        if (selectedId <= -1) return;
        if (inpDiscName_tab2.getText().equals("") || inpDiscGenre_tab2.getText().equals("")
                || inpDiscDirector_tab2.getText().equals("") || inpDiscRelease_tab2.getText().equals("")) {
            emptyFieldsAlert.showAndWait();
        } else if (rentalList.stream().map(Rental::getDiscId).collect(Collectors.toList()).contains(discsTable_tab2.getItems().get(selectedId).getDiscId())) {
            discInRent.showAndWait();
        } else if (checkForNum(inpDiscRelease_tab2.getText()) == -1) {
            mustContainsNumber.showAndWait();
        } else {
            selectedDisc.setDiscName(inpDiscName_tab2.getText());
            selectedDisc.setGenre(inpDiscGenre_tab2.getText());
            selectedDisc.setReleaseYear(Integer.parseInt(inpDiscRelease_tab2.getText()));
            selectedDisc.setDirector(inpDiscDirector_tab2.getText());

            try {
                RandomAccessFile discsFile = new RandomAccessFile(DISCS_FILE, "rw");
                for (String line; (line = discsFile.readLine()) != null;) {
                    if (line.startsWith(selectedDiscBeforeChanging)) {
                        discsFile.seek(discsFile.getFilePointer() - RECORD_LENGTH - 1);
                        discsFile.writeBytes(paddingRight(""));
                        discsFile.seek(discsFile.getFilePointer() - RECORD_LENGTH - 1);
                        discsFile.writeBytes( "\n" + selectedDisc.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            discsTable_tab2.refresh();
            rentalTable_tab3.refresh();
            discsTable_addRent.refresh();
            changesWasApplied.showAndWait();
        }
    }

    @FXML
    void clearFields_tab2_event(MouseEvent event) {
        inpDiscName_tab2.setText("");
        inpDiscGenre_tab2.setText("");
        inpDiscRelease_tab2.setText("");
        inpDiscDirector_tab2.setText("");
    }

    @FXML
    void delDisc_tab2_event(MouseEvent event) {
        int selectedID = discsTable_tab2.getSelectionModel().getSelectedIndex();
        VideoDisc selectedDisc = discsTable_tab2.getSelectionModel().getSelectedItem();

        if (selectedID <= -1) return;
        if (rentalList.stream().map(Rental::getDiscId).collect(Collectors.toList()).contains(selectedDisc.getDiscId())) {
            discInRent.showAndWait();
        } else {
            discsTable_tab2.getItems().remove(selectedID);
            videoDiscList.forEach(el -> el.setDiscId(videoDiscList.indexOf(el) + 1));

            if (!rentalList.stream().map(Rental::getDiscId).max(Comparator.naturalOrder()).equals(selectedDisc.getDiscId())) {
                rentalList.filtered(el -> selectedDisc.getDiscId() < el.getDiscId()).forEach(el -> el.setDiscId(el.getDiscId() - 1));
                rentalTable_tab3.refresh();
                changeRentals();
            }

            try {
                RandomAccessFile discsFile = new RandomAccessFile(DISCS_FILE, "rw");
                for (String line; (line = discsFile.readLine()) != null;) {
                    if (line.startsWith(selectedDisc.toString())) {
                        discsFile.seek(discsFile.getFilePointer() - RECORD_LENGTH - 1);
                        discsFile.writeBytes(paddingRight(""));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void findDisc_tab2_event(MouseEvent event) {
        if (inputSearchPar_tab2.getText().equals("")) {
            emptyFieldsAlert.showAndWait();
        } else {
            ObservableList<VideoDisc> finedDiscs = FXCollections.observableArrayList();
            switch (searchCombobox_tab2.getValue()) {
                case "Название":
                    videoDiscList.stream()
                            .filter(el -> el.getDiscName().toLowerCase().equals(inputSearchPar_tab2.getText().toLowerCase()))
                            .forEach(el -> finedDiscs.add(el));
                    discsTable_tab2.setItems(finedDiscs);
                    break;

                case "Жанр":
                    videoDiscList.stream()
                            .filter(el -> el.getGenre().toLowerCase().equals(inputSearchPar_tab2.getText().toLowerCase()))
                            .forEach(el -> finedDiscs.add(el));
                    discsTable_tab2.setItems(finedDiscs);
                    break;

                case "Выпуск":
                    videoDiscList.stream()
                            .filter(el -> el.getReleaseYear().equals(Integer.parseInt(inputSearchPar_tab2.getText().toLowerCase())))
                            .forEach(el -> finedDiscs.add(el));
                    discsTable_tab2.setItems(finedDiscs);
                    break;

                case "Режиссер":
                    videoDiscList.stream()
                            .filter(el -> el.getDirector().toLowerCase().equals(inputSearchPar_tab2.getText().toLowerCase()))
                            .forEach(el -> finedDiscs.add(el));
                    discsTable_tab2.setItems(finedDiscs);
                    break;

                default:
                    break;
            }
        }
    }

    @FXML
    void reset_tab2_event(MouseEvent event) {
        searchCombobox_tab2.setValue("не выбрано");
        inputSearchPar_tab2.setText("");
        discsTable_tab2.setItems(videoDiscList);
    }

    @FXML
    void selectDiscTableItem_event(MouseEvent event) {
        int selectedID = discsTable_tab2.getSelectionModel().getSelectedIndex();
        if (selectedID <= -1) return;

        inpDiscName_tab2.setText(discNameCol_tab2.getCellData(selectedID));
        inpDiscGenre_tab2.setText(discGenreCol_tab2.getCellData(selectedID));
        inpDiscRelease_tab2.setText(String.valueOf(discReleaseCol_tab2.getCellData(selectedID)));
        inpDiscDirector_tab2.setText(discDirectorCol_tab2.getCellData(selectedID));
    }

    //----------------TAB_3--------------------

    @FXML
    private TableView<Rental> rentalTable_tab3;

    @FXML
    private TableColumn<Rental, Integer> rentalIdCol_tab3;

    @FXML
    private TableColumn<Rental, Integer> discIdCol_tab3;

    @FXML
    private TableColumn<Rental, String> phoneCol_tab3;

    @FXML
    private TableColumn<Rental, String> issueDateCol_tab3;

    @FXML
    private TableColumn<Rental, String> returnDateCol_tab3;

    @FXML
    private TableColumn<Rental, Boolean> isReturnedCol_tab3;

    @FXML
    private Button refreshTableBtn_tab3;

    @FXML
    void refreshTableBtn_tab3_event(MouseEvent event) {
        rentalTable_tab3.setItems(rentalList);
    }

    //----------------TAB_4--------------------

    @FXML
    private AnchorPane discSelecting;

    @FXML
    private Button bntAddRent_addRent;

    @FXML
    private Button btnDone_addRent;

    @FXML
    private TableColumn<VideoDisc, String> discDirectorCol_addRent;

    @FXML
    private TableColumn<VideoDisc, String> discGenreCol_addRent;

    @FXML
    private TableColumn<VideoDisc, Integer> discIdCol_addRent;

    @FXML
    private TableColumn<VideoDisc, String> discNameCol_addRent;

    @FXML
    private TableColumn<VideoDisc, Integer> discReleaseCol_addRent;

    @FXML
    private TableView<VideoDisc> discsTable_addRent;

    @FXML
    private Label  userInfoField_addRent;

    @FXML
    void btnAddRent_addRent_action(MouseEvent event) {
        List<Integer> rentalDiscsId = rentalList.stream().mapToInt(Rental::getDiscId).boxed().collect(Collectors.toList());
        VideoDisc selectedDisc = discsTable_addRent.getSelectionModel().getSelectedItem();

        RentalUser selectedUser = usersTable_tab1.getSelectionModel().getSelectedItem();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date curDate_d = new Date();
        Boolean hasExpired = false;

        List<String> userRetDatesList = new ArrayList<>();
        rentalList.stream().filter(rental -> rental.getUserPhone().equals(selectedUser.getUserPhone()))
                .map(rental -> rental.getReturnDate())
                .forEach(date -> userRetDatesList.add(date));

        for (var date : userRetDatesList) {
            try {
                Date retDate_d = formatter.parse(date);
                if (retDate_d.before(curDate_d)) {
                    hasExpired = true;
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (rentalDiscsId.contains(selectedDisc.getDiscId())) {
            discInRent.showAndWait();
        } else if (hasExpired == true) {
            hasExpiredRentals.showAndWait();
        } else {
            Calendar curCalendar = new GregorianCalendar();
            String curDate = formatter.format(curCalendar.getTime());

            Calendar retCalendar = new GregorianCalendar();
            retCalendar.add(Calendar.MONTH, 1);
            String retDate = formatter.format(retCalendar.getTime());

            Rental newRent = new Rental(
                    rentalList.size() + 1,
                    selectedDisc.getDiscId(),
                    selectedUser.getUserPhone(),
                    curDate,
                    retDate,
                    false
            );
            rentalList.add(newRent);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(RENTALS_FILE, true))) {
                if (RENTALS_FILE.length() == 0) writer.write("\n");
                writer.write(newRent.toString() + "\n");
            }  catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    @FXML
    void selectDiscTableItem_addRent_event(MouseEvent event) {
        bntAddRent_addRent.setDisable(false);
    }

    //----------------TAB_5--------------------

    @FXML
    private Button bntReturnRent_viewRentals;

    @FXML
    private Button btnDone_viewRentals;

    @FXML
    private AnchorPane viewRentals;

    @FXML
    private TableView<Rental> rentalTable_viewRentals;

    @FXML
    private TableColumn<Rental, Integer> rentalIdCol_viewRentals;

    @FXML
    private TableColumn<Rental, Integer> discIdCol_viewRentals;

    @FXML
    private TableColumn<Rental, String> phoneCol_viewRentals;

    @FXML
    private TableColumn<Rental, String> issueDateCol_viewRentals;

    @FXML
    private TableColumn<Rental, String> returnDateCol_viewRentals;

    @FXML
    private TableColumn<Rental, Boolean> isReturnedCol_viewRentals;

    @FXML
    private Label userInfoField_viewRentals;

    @FXML
    void btnReturnRent_viewRentals_action(MouseEvent event) {
        Rental selectedRental = rentalTable_viewRentals.getSelectionModel().getSelectedItem();
        rentalTable_tab3.getItems().remove(selectedRental);
        rentalList.forEach(el -> el.setRentalId(rentalList.indexOf(el) + 1));
        changeRentals();
    }

    @FXML
    void selectRentalTableItem_viewRentals_event(MouseEvent event) {
        bntReturnRent_viewRentals.setDisable(false);
    }

    @FXML
    void selectDiscTableItem_viewRentals_event(MouseEvent event) {
        bntReturnRent_viewRentals.setDisable(false);
    }

    //----------------INIT--------------------

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //reading_from_files
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            for (String line; (line = reader.readLine()) != null;) {
                if (line.length() != 0 && !line.equals(paddingRight(""))) {
                    String[] itemsInLine = line.split(" _ ");
                    userList.add(new RentalUser(
                            itemsInLine[0],
                            itemsInLine[1],
                            itemsInLine[2],
                            itemsInLine[3],
                            itemsInLine[4]
                    ));
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DISCS_FILE))) {
            for (String line; (line = reader.readLine()) != null;) {
                if (line.length() != 0 && !line.equals(paddingRight(""))) {
                    String[] itemsInLine = line.split(" _ ");
                    videoDiscList.add(new VideoDisc(
                            videoDiscList.size() + 1,
                            itemsInLine[0],
                            itemsInLine[1],
                            Integer.parseInt(itemsInLine[2]),
                            itemsInLine[3]
                    ));
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(RENTALS_FILE))) {
            for (String line; (line = reader.readLine()) != null;) {
                if (line.length() != 0 && !line.equals(paddingRight(""))) {
                    String[] itemsInLine = line.split(" _ ");
                    rentalList.add(new Rental(
                            rentalList.size() + 1,
                            Integer.parseInt(itemsInLine[0]),
                            itemsInLine[1],
                            itemsInLine[2],
                            itemsInLine[3],
                            false
                    ));
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }


        //alerts
        emptyFieldsAlert.setTitle("Error");
        emptyFieldsAlert.setHeaderText("Все поля должны быть заполнены!");

        changesWasApplied.setTitle("Success");
        changesWasApplied.setHeaderText("Изменения были применены!");

        alreadyExistsAlert.setTitle("Error");
        alreadyExistsAlert.setHeaderText("В таблице уже содержится строка с таким значением!");

        discIsRented.setTitle("Error");
        discIsRented.setHeaderText("Этот пользователь уже имеет этот диск в прокате!");

        userHaveRentals.setTitle("Error");
        userHaveRentals.setHeaderText("Этот пользователь имеет не возвращенные или простроченные диски!");

        discInRent.setTitle("Error");
        discInRent.setHeaderText("Этот диск находится в прокате!");

        mustContainsNumber.setTitle("Error");
        mustContainsNumber.setHeaderText("Поле \"год выпуска\" должно должно содержать числовое значение!");

        hasExpiredRentals.setTitle("Error");
        hasExpiredRentals.setHeaderText("Пользователь имеет просроченные диски!");

        tab4.setDisable(true);
        tab5.setDisable(true);

        //tab_1

        surnameCol_tab1.setCellValueFactory(new PropertyValueFactory<RentalUser, String>("surname"));
        nameCol_tab1.setCellValueFactory(new PropertyValueFactory<RentalUser, String>("userName"));
        patronymicCol_tab1.setCellValueFactory(new PropertyValueFactory<RentalUser, String>("patronymic"));
        addressCol_tab1.setCellValueFactory(new PropertyValueFactory<RentalUser, String>("address"));
        phoneCol_tab1.setCellValueFactory(new PropertyValueFactory<RentalUser, String>("userPhone"));
        usersTable_tab1.setItems(userList);

        searchCombobox_tab1.getItems().setAll("не выбрано", "Фамилия", "Телефон");
        searchCombobox_tab1.setValue("не выбрано");
        inputSearchPar_tab1.setDisable(true);
        find_tab1.setDisable(true);
        reset_tab1.setDisable(true);

        addRental_tab1.setDisable(true);
        viewRentals_tab1.setDisable(true);

        //tab_2

        discIdCol_tab2.setCellValueFactory(new PropertyValueFactory<VideoDisc, Integer>("discId"));
        discNameCol_tab2.setCellValueFactory(new PropertyValueFactory<VideoDisc, String>("discName"));
        discGenreCol_tab2.setCellValueFactory(new PropertyValueFactory<VideoDisc, String>("genre"));
        discReleaseCol_tab2.setCellValueFactory(new PropertyValueFactory<VideoDisc, Integer>("releaseYear"));
        discDirectorCol_tab2.setCellValueFactory(new PropertyValueFactory<VideoDisc, String>("director"));
        discsTable_tab2.setItems(videoDiscList);

        searchCombobox_tab2.getItems().setAll("не выбрано", "Название", "Жанр", "Выпуск", "Режиссер");
        searchCombobox_tab2.setValue("не выбрано");
        inputSearchPar_tab2.setDisable(true);
        find_tab2.setDisable(true);
        reset_tab2.setDisable(true);

        //tab_3

        rentalIdCol_tab3.setCellValueFactory(new PropertyValueFactory<Rental, Integer>("rentalId"));
        discIdCol_tab3.setCellValueFactory(new PropertyValueFactory<Rental, Integer>("discId"));
        phoneCol_tab3.setCellValueFactory(new PropertyValueFactory<Rental, String>("userPhone"));
        issueDateCol_tab3.setCellValueFactory(new PropertyValueFactory<Rental, String >("issueDate"));
        returnDateCol_tab3.setCellValueFactory(new PropertyValueFactory<Rental, String >("returnDate"));
        isReturnedCol_tab3.setCellValueFactory(new PropertyValueFactory<Rental, Boolean>("isReturned"));
        rentalTable_tab3.setItems(rentalList);

        //tab_4

        discIdCol_addRent.setCellValueFactory(new PropertyValueFactory<VideoDisc, Integer>("discId"));
        discNameCol_addRent.setCellValueFactory(new PropertyValueFactory<VideoDisc, String>("discName"));
        discGenreCol_addRent.setCellValueFactory(new PropertyValueFactory<VideoDisc, String>("genre"));
        discReleaseCol_addRent.setCellValueFactory(new PropertyValueFactory<VideoDisc, Integer>("releaseYear"));
        discDirectorCol_addRent.setCellValueFactory(new PropertyValueFactory<VideoDisc, String>("director"));

        bntAddRent_addRent.setDisable(true);

        //tab_5

        rentalIdCol_viewRentals.setCellValueFactory(new PropertyValueFactory<Rental, Integer>("rentalId"));
        discIdCol_viewRentals.setCellValueFactory(new PropertyValueFactory<Rental, Integer>("discId"));
        phoneCol_viewRentals.setCellValueFactory(new PropertyValueFactory<Rental, String>("userPhone"));
        issueDateCol_viewRentals.setCellValueFactory(new PropertyValueFactory<Rental, String >("issueDate"));
        returnDateCol_viewRentals.setCellValueFactory(new PropertyValueFactory<Rental, String >("returnDate"));
        isReturnedCol_viewRentals.setCellValueFactory(new PropertyValueFactory<Rental, Boolean>("isReturned"));

        bntReturnRent_viewRentals.setDisable(true);

    }
}