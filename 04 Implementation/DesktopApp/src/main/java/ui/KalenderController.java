package ui;

import com.calendarfx.model.*;

import com.calendarfx.model.Calendar;
import com.calendarfx.view.CalendarView;
import com.google.cloud.Timestamp;
import entities.Begivenhed;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.BookingFacade;
import model.BrugerFacade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author Benjamin
 */
public class KalenderController {
    private BrugerFacade brugerFacade;
    private BookingFacade bookingFacade;
    private CalendarView calendarView;

    @FXML
    private AnchorPane kalenderAnchorPane, calenderViewHolder;

    @FXML
    private Button btnTilbage, btnTest;

    @FXML
    private MenuBar menuBar;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        bookingFacade = BookingFacade.getInstance();

        /** Sæt UI-elementer til at skalere med vinduets størrelse */
        ChangeListener<Number> redraw = (observable, oldValue, newValue) -> {
            menuBar.setMinWidth(kalenderAnchorPane.getWidth() - btnTilbage.getPrefWidth());
            btnTilbage.setMinWidth(btnTilbage.getPrefWidth());
        };
        kalenderAnchorPane.widthProperty().addListener(redraw);

        indlaesKalender();
    }

    /**
     * Kalenderen tager 2-3 sekunder at indlæse, så derfor gøres det i en thread
     */
    public void indlaesKalender() {
        Platform.runLater(() -> {
            calendarView = new CalendarView();
            calenderViewHolder.getChildren().clear();
            calenderViewHolder.getChildren().add(calendarView);

            Calendar ferie = new Calendar("Ferie");
            ferie.setStyle(Calendar.Style.STYLE1);
            Calendar konsulationer = new Calendar("Konsulationer");
            konsulationer.setStyle(Calendar.Style.STYLE2);
            Calendar behandlinger = new Calendar("Behandlinger");
            behandlinger.setStyle(Calendar.Style.STYLE3);
            Calendar moeder = new Calendar("Møder");
            moeder.setStyle(Calendar.Style.STYLE4);
            Calendar eksaminer = new Calendar("Eksaminer");
            eksaminer.setStyle(Calendar.Style.STYLE5);
            Calendar eksamensforberedelse = new Calendar("Eksamensforberedelse");
            eksamensforberedelse.setStyle(Calendar.Style.STYLE6);

            CalendarSource calendarSource = new CalendarSource("Aftaler");
            calendarSource.getCalendars().addAll(ferie, konsulationer, behandlinger, moeder, eksaminer, eksamensforberedelse);

            calendarView.getCalendarSources().setAll(calendarSource);

            eksaminer.addEntry(tilfoejBegivenhed("Førsteårseksamen", 2020, 6, 24, 10, 0,
                    2020, 6, 24, 12, 0));
            eksamensforberedelse.addEntry(tilfoejBegivenhed("Afprøv programmet"));
            eksamensforberedelse.addEntry(tilfoejBegivenhed("Kodning", 9, 0, 15, 0));

            EventHandler<CalendarEvent> l = e -> handleEvent(e);
            eksaminer.addEventHandler(l);

//            eksaminer.addEventHandler(CalendarEvent., e -> handleEvent1(e));

        });

        btnTest.setOnMouseClicked(e ->{
            System.out.println(bookingFacade.hentBegivenheder().size());
        });

    }

    private void handleEvent(CalendarEvent e) {
        Entry entry = e.getEntry();

        System.out.println(entry.getId());

        LocalDateTime startTidspunkt = entry.getStartAsLocalDateTime();
        Date startDate = new Date(startTidspunkt.getYear(), startTidspunkt.getMonthValue(), startTidspunkt.getDayOfMonth(),
                startTidspunkt.getHour(), startTidspunkt.getMinute());
        Timestamp startTimestamp = Timestamp.of(startDate);

        LocalDateTime slutTidspunkt = entry.getEndAsLocalDateTime();
        Date slutDate = new Date(slutTidspunkt.getYear(), slutTidspunkt.getMonthValue(), slutTidspunkt.getDayOfMonth(),
                slutTidspunkt.getHour(), slutTidspunkt.getMinute());
        Timestamp slutTimestamp = Timestamp.of(slutDate);
        Begivenhed begivenhed = new Begivenhed(entry.getTitle(), entry.getCalendar().toString(), startTimestamp, slutTimestamp, entry.getId());

        for (int i = 0; i < bookingFacade.hentBegivenheder().size(); i++) {
            if (bookingFacade.hentBegivenheder().get(i).getId().equals(entry.getId())) {
                bookingFacade.hentBegivenheder().remove(i);
            }
        }

        bookingFacade.gemBegivenheder(begivenhed);
    }

    public void logUd() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Start.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);

        Stage stage = (Stage) kalenderAnchorPane.getScene().getWindow();
        stage.setScene(scene);

        brugerFacade.logUd();
    }

    public void tilHovedmenu() {
        for (int i = 0; i < bookingFacade.hentBegivenheder().size(); i++) {
            System.out.println(bookingFacade.hentBegivenheder().get(i).getTitel());
        }

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);

        Stage stage = (Stage) kalenderAnchorPane.getScene().getWindow();
        stage.setScene(scene);
    }

    public Entry tilfoejBegivenhed(String titel, int startAar, int startMaaned, int startDag, int startTime, int startMinut,
                                   int slutAaar, int slutMaaned, int slutDag, int slutTime, int slutMinut) {
        LocalDate startDato = LocalDate.of(startAar, startMaaned, startDag);
        LocalTime startTidspunkt = LocalTime.of(startTime, startMinut, 0);
        LocalDate slutDato = LocalDate.of(slutAaar, slutMaaned, slutDag);
        LocalTime slutTidspunkt = LocalTime.of(slutTime, slutMinut, 0);
        Interval interval = new Interval(startDato, startTidspunkt, slutDato, slutTidspunkt);
        return new Entry(titel, interval);
        //TODO: Metoden skal flyttes til logik på et tidspunkt
    }

    public Entry tilfoejBegivenhed(String titel, int startTime, int startMinut, int slutTime, int slutMinut) {
        LocalDateTime nu = LocalDateTime.now();
        LocalDate startDato = LocalDate.of(nu.getYear(), nu.getMonth(), nu.getDayOfMonth());
        LocalTime startTidspunkt = LocalTime.of(startTime, startMinut, 0);
        LocalDate slutDato = LocalDate.of(nu.getYear(), nu.getMonth(), nu.getDayOfMonth());
        LocalTime slutTidspunkt = LocalTime.of(slutTime, slutMinut, 0);
        Interval interval = new Interval(startDato, startTidspunkt, slutDato, slutTidspunkt);
        return new Entry(titel, interval);
        //TODO: Metoden skal flyttes til logik på et tidspunkt
    }

    public Entry tilfoejBegivenhed(String titel) {
        LocalDateTime nu = LocalDateTime.now();
        LocalDate startDato = LocalDate.of(nu.getYear(), nu.getMonth(), nu.getDayOfMonth());
        LocalTime startTidspunkt = LocalTime.of(nu.getHour(), nu.getMinute(), nu.getSecond());
        LocalDate slutDato = LocalDate.of(nu.getYear(), nu.getMonth(), nu.getDayOfMonth());
        LocalTime slutTidspunkt = LocalTime.of(nu.getHour() + 1, nu.getMinute(), nu.getSecond());
        Interval interval = new Interval(startDato, startTidspunkt, slutDato, slutTidspunkt);
        return new Entry(titel, interval);
        //TODO: Metoden skal flyttes til logik på et tidspunkt
    }
}
