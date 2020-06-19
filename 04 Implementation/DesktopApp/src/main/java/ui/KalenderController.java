package ui;

import com.calendarfx.model.*;
import database.DatabaseManager;
import entities.Bruger;
import javafx.event.EventType;
import org.controlsfx.control.PopOver;

import com.calendarfx.model.Calendar;
import com.calendarfx.view.CalendarView;
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.*;

/**
 * @author Benjamin
 */
public class KalenderController {
    private BrugerFacade brugerFacade;
    private BookingFacade bookingFacade;
    private CalendarView calendarView;
    private ArrayList<String> IDer;
    private ArrayList<Entry> entries;

    @FXML
    private AnchorPane kalenderAnchorPane, calenderViewHolder;

    @FXML
    private Button btnTilbage, btnTest;

    @FXML
    private MenuBar menuBar;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        bookingFacade = BookingFacade.getInstance();

        // Sæt UI-elementer til at skalere med vinduets størrelse
        ChangeListener<Number> redraw = (observable, oldValue, newValue) -> {
            menuBar.setMinWidth(kalenderAnchorPane.getWidth() - btnTilbage.getPrefWidth());
            btnTilbage.setMinWidth(btnTilbage.getPrefWidth());
        };
        kalenderAnchorPane.widthProperty().addListener(redraw);

        // Tilføj observer gemBegivenheder /
        bookingFacade.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("gemBegivenheder"))
                    DatabaseManager.getInstance().gemBegivenheder((Begivenhed) evt.getNewValue());
            }
        });

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
            calendarView.setPrefWidth(calenderViewHolder.getWidth());

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

            CalendarSource calendarSource = new CalendarSource("Begivenheder");
            calendarSource.getCalendars().addAll(ferie, konsulationer, behandlinger, moeder, eksaminer, eksamensforberedelse);
            calendarView.getCalendarSources().setAll(calendarSource);

            // Tilføj alle begivenheder fra listen af begivenheder i BookingManager
            tilfoejBegivenheder();


//            EventHandler<CalendarEvent> l = this::handleEvent;
//            eksaminer.addEventHandler(l);
//            // TODO tilføj EventHandler på alle kalendere
        });

        btnTest.setOnMouseClicked(e -> {
            gemBegivenhederIBookingManager();
        });
    }

    private void handleEvent(CalendarEvent e) {
        Entry entry = e.getEntry();

        for (int i = 0; i < entries.size(); i++) {
            if (!entries.contains(entry))
                entries.add(entry);
        }

//        LocalDateTime startTidspunkt = entry.getStartAsLocalDateTime();
//        Date startDate = new Date(startTidspunkt.getYear(), startTidspunkt.getMonthValue(), startTidspunkt.getDayOfMonth(),
//                startTidspunkt.getHour(), startTidspunkt.getMinute());
//        Timestamp startTimestamp = Timestamp.of(startDate);
//        System.out.println(startTimestamp.getSeconds());
//
//        LocalDateTime slutTidspunkt = entry.getEndAsLocalDateTime();
//        Date slutDate = new Date(slutTidspunkt.getYear(), slutTidspunkt.getMonthValue(), slutTidspunkt.getDayOfMonth(),
//                slutTidspunkt.getHour(), slutTidspunkt.getMinute());
//        Timestamp slutTimestamp = Timestamp.of(slutDate);
//        System.out.println(slutTimestamp.getSeconds());

        ArrayList<String> deltagere = new ArrayList<>();
        deltagere.add(brugerFacade.getAktivBruger().getNavn());

        // Programmet crasher, hvis vi prøver at kalde entry.getCalender().getName() i samme thread
        //TODO dette skal gøres som en thread, og vente på den bliver færdig, ellers tilføjer den en masse duplicate begivenheder
//        ExecutorService executor = Executors.newCachedThreadPool();
//        Future<Begivenhed> futureCall = executor.submit(new Begivenhed(entry.getTitle(), entry.getCalendar().getName(), startTidspunkt1, slutTidspunkt1, entry.getId(), deltagere));
//        Begivenhed begivenhed = futureCall.get(10, TimeUnit.SECONDS);
//        executor.shutdown();
//        Begivenhed begivenhed = new Begivenhed(entry.getTitle(), entry.getCalendar().getName(), startTidspunkt1, slutTidspunkt1, entry.getId(), deltagere);
//        for (int i = 0; i < bookingFacade.hentBegivenheder().size(); i++) {
//            if (bookingFacade.hentBegivenheder().get(i).getId().equals(entry.getId())) {
//                bookingFacade.hentBegivenheder().remove(i);
//                bookingFacade.hentBegivenheder().add(i, begivenhed);
//            } else if (!bookingFacade.hentBegivenheder().get(i).getId().equals(entry.getId())){
////                bookingFacade.hentBegivenheder().add(begivenhed);
//            }
//        }
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

    public void tilfoejBegivenheder() {
        entries = new ArrayList<>();
        ArrayList<Begivenhed> begivenheder = bookingFacade.hentBegivenheder();
        for (int i = 0; i < begivenheder.size(); i++) {
            Begivenhed begivenhed = begivenheder.get(i);
            for (int j = 0; j < calendarView.getCalendars().size(); j++) {
                Calendar kalender = calendarView.getCalendars().get(j);
                if (kalender.getName().equals(begivenhed.getKategori())){
                    Entry entry = tilfoejBegivenhed(begivenhed);
                    entry.setCalendar(kalender);
                    kalender.addEntry(entry);
                    entries.add(entry);
                }
            }
        }
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

    public Entry tilfoejBegivenhed(Begivenhed begivenhed) {
        Date start = new Date(begivenhed.getStartTidspunkt());
        Date slut = new Date(begivenhed.getSlutTidspunkt());
        LocalDate startDato = LocalDate.of(start.getYear() + 1900, start.getMonth() + 1, start.getDate());
        LocalTime startTidspunkt = LocalTime.of(start.getHours(), start.getMinutes(), start.getSeconds());
        LocalDate slutDato = LocalDate.of(slut.getYear() + 1900, slut.getMonth() + 1, slut.getDate());
        LocalTime slutTidspunkt = LocalTime.of(slut.getHours(), slut.getMinutes(), slut.getSeconds());
        Interval interval = new Interval(startDato, startTidspunkt, slutDato, slutTidspunkt);
        Entry entry = new Entry(begivenhed.getTitel(), interval);
        entry.setId(begivenhed.getId());
        return entry;
        //TODO: Metoden skal flyttes til logik på et tidspunkt
    }

    public void gemBegivenhederIBookingManager(){
        ArrayList<String> deltagere = new ArrayList<>();
        deltagere.add(brugerFacade.getAktivBruger().getNavn());
        bookingFacade.hentBegivenheder().clear();

        ArrayList<Begivenhed> begivenheder = new ArrayList<>();

        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            long startTidspunkt1 = entry.getStartMillis();
            long slutTidspunkt1 = entry.getEndMillis();
            Begivenhed begivenhed = new Begivenhed(entry.getTitle(), entry.getCalendar().getName(), startTidspunkt1, slutTidspunkt1, entry.getId(), deltagere);
            begivenheder.add(begivenhed);
        }

        bookingFacade.gemBegivenheder(begivenheder);
    }
}
