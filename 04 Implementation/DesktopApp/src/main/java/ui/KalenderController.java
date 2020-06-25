package ui;

import com.calendarfx.model.*;
import database.DatabaseManager;

import com.calendarfx.model.Calendar;
import com.calendarfx.view.CalendarView;
import entities.Begivenhed;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.BookingFacade;
import model.BrugerFacade;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * @author Benjamin
 */
public class KalenderController {
    private BrugerFacade brugerFacade;
    private BookingFacade bookingFacade;
    private CalendarView calendarView;
    private ArrayList<Entry> entries;
    private ArrayList<String> kalendere;
    private Stage vindue;

    @FXML
    private AnchorPane kalenderAnchorPane, calenderViewHolder;

    @FXML
    private Button btnTilbage;

    @FXML
    private MenuBar menuBar;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        bookingFacade = BookingFacade.getInstance();

        // Tilføj observer til gemBegivenhed /
        bookingFacade.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("gemBegivenhed"))
                    DatabaseManager.getInstance().gemBegivenhed((Begivenhed) evt.getNewValue());
            }
        });

        //Tilføj observer til sletBegivenhed
        bookingFacade.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("sletBegivenhed"))
                    DatabaseManager.getInstance().sletBegivenhed((Begivenhed) evt.getNewValue()); // TODO tror ikke det virker fordi newValue er en String med ID
            }
        });
        indlaesKalender();
    }

    /**
     * Kalenderen tager 2-3 sekunder at indlæse, så derfor gøres det i en thread
     */
    public void indlaesKalender() {
        Platform.runLater(() -> {
            vindue = (Stage) kalenderAnchorPane.getScene().getWindow();
            vindue.setOnCloseRequest(e -> {
                e.consume();
                lukProgram();
            });

            calendarView = new CalendarView();
            calenderViewHolder.getChildren().clear();
            calenderViewHolder.getChildren().add(calendarView);
            calendarView.setPrefWidth(calenderViewHolder.getWidth());

            // Tilføj alle kalendere, som findes i listen af begivenheder i BookingManager
            tilfoejKalendere();

            // Tilføj alle begivenheder fra listen af begivenheder i BookingManager
            tilfoejBegivenheder();

            EventHandler<CalendarEvent> l = e -> handleEvent(e);
            for (int i = 0; i < calendarView.getCalendars().size(); i++) {
                calendarView.getCalendars().get(i).addEventHandler(l);
            }

            calendarView.prefWidthProperty().bind(calenderViewHolder.widthProperty());
            calendarView.prefHeightProperty().bind(calenderViewHolder.heightProperty());
        });
    }

    public void tilfoejKalendere() {
        kalendere = new ArrayList<>();
        ArrayList<Begivenhed> begivenheder = bookingFacade.hentBegivenheder();
        for (int i = 0; i < begivenheder.size(); i++) {
            if (!kalendere.contains(begivenheder.get(i).getKalender()))
                kalendere.add(begivenheder.get(i).getKalender());
        }

        ArrayList<Calendar.Style> kalenderTemaer = new ArrayList<>();
        kalenderTemaer.add(Calendar.Style.STYLE1);
        kalenderTemaer.add(Calendar.Style.STYLE2);
        kalenderTemaer.add(Calendar.Style.STYLE3);
        kalenderTemaer.add(Calendar.Style.STYLE4);
        kalenderTemaer.add(Calendar.Style.STYLE5);
        kalenderTemaer.add(Calendar.Style.STYLE6);
        kalenderTemaer.add(Calendar.Style.STYLE7);

        CalendarSource calendarSource = new CalendarSource("Begivenheder");

        for (int i = 0; i < kalendere.size(); i++) {
            Calendar kalender = new Calendar(kalendere.get(i));
            int tilfaeldigFarve = (int) (Math.random() * kalenderTemaer.size());
            kalender.setStyle(kalenderTemaer.get(tilfaeldigFarve));
            calendarSource.getCalendars().addAll(kalender);
        }

        calendarView.getCalendarSources().setAll(calendarSource);
    }

    /**
     * Kaldes automatisk hver gang, der sker en ændring på en Entry i UI'et.
     *
     * @param e en ændring i en Entry i UI'et
     */
    private void handleEvent(CalendarEvent e) {
        if (e.isEntryAdded()) {
            Entry entry = e.getEntry();
            entry.setId(UUID.randomUUID().toString());
            entries.add(entry);
            gemEntrySomBegivenhed(entry);
        } else if (e.isEntryRemoved()) {
            Entry entry = e.getEntry();
            entries.remove(e.getEntry());
            bookingFacade.sletBegivenhed(entry.getId());
        }

        // TODO evt. if-condition, der gemmer ændringer?
    }

    /**
     * Indlæser alle Begivenheder fra listen af Begivenheder i BookingManager og tilføjer dem til UI'et som Entries
     */
    public void tilfoejBegivenheder() {
        entries = new ArrayList<>();
        ArrayList<Begivenhed> begivenheder = bookingFacade.hentBegivenheder();
        for (int i = 0; i < begivenheder.size(); i++) {
            Begivenhed begivenhed = begivenheder.get(i);
            for (int j = 0; j < calendarView.getCalendars().size(); j++) {
                Calendar kalender = calendarView.getCalendars().get(j);
                if (kalender.getName().equals(begivenhed.getKalender())) {
                    Entry entry = tilfoejBegivenhed(begivenhed);
                    entry.setCalendar(kalender); // kalenderen bliver ikke automatisk sat i constructoren
                    kalender.addEntry(entry);
                    entries.add(entry);
                }
            }
        }
    }

    /**
     * Metoden omdanner et Begivenhedsobjekt til et Entryobjekt. Denne metode tilhører UI'et, fordi Entry er en ren UI-
     * klasse i CalendarFX.
     *
     * @param begivenhed begivenheden findes i BookingManagers liste af begivenheder, som oprindeligt kommer fra
     *                   Firestore
     * @return returnerer et Entryobjekt
     */
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
    }

    /**
     * Omdanner et Entryobjekt til et Begivenhedobjekt
     *
     * @param entry kommer fra UI'et
     */
    public void gemEntrySomBegivenhed(Entry entry) {
        ArrayList<String> deltagere = new ArrayList<>();
        deltagere.add(brugerFacade.getAktivBruger().getNavn());
        deltagere.add(entry.getCalendar().getName());
        long startTidspunkt1 = entry.getStartMillis();
        long slutTidspunkt1 = entry.getEndMillis();
        Begivenhed begivenhed = new Begivenhed(entry.getTitle(), entry.getCalendar().getName(), startTidspunkt1, slutTidspunkt1, entry.getId(), deltagere);
        bookingFacade.gemBegivenhed(begivenhed);
    }

    /**
     * Denne metode skal kaldes, for at gemme ændringer på Entries i både BookingManagers liste af begivenheder, men
     * også i databasen. EventHandleren kan ikke udløses ved ændringer på en Entry, så derfor skal brugeren kalde
     * metoden, når han vil gemme ændringerne.
     */
    public void gemAendringer() {
        ArrayList<String> deltagere = new ArrayList<>();
        bookingFacade.hentBegivenheder().clear();

        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            deltagere.clear();
            deltagere.add(brugerFacade.getAktivBruger().getNavn());
            deltagere.add(entry.getCalendar().getName());
            long startTidspunkt1 = entry.getStartMillis();
            long slutTidspunkt1 = entry.getEndMillis();
            Begivenhed begivenhed = new Begivenhed(entry.getTitle(), entry.getCalendar().getName(), startTidspunkt1, slutTidspunkt1, entry.getId(), deltagere);
            bookingFacade.gemBegivenhed(begivenhed);
        }
    }

    private void lukProgram() {
        LukProgramPopup lukProgramPopup = new LukProgramPopup();
        String svar = lukProgramPopup.vis("Vil du gemme ændringerne i kalenderen?");
        if (svar.equals("ja")) {
            gemAendringer();
            vindue.close();
        } else if (svar.equals("nej")) {
            vindue.close();
        }
    }

    public void logUd() {
        LukProgramPopup lukProgramPopup = new LukProgramPopup();
        String svar = lukProgramPopup.vis("Vil du gemme ændringerne i kalenderen?");

        if (svar.equals("ja")) {
            gemAendringer();
        } else if (svar.equals("annuller")) {
            return;
        }

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Start.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        kalenderAnchorPane.getScene().setRoot(root);

        brugerFacade.logUd();

        // Fjerner onCloseRequest fra Stage, fordi den ikke længere er relevant, når vi forlader kalenderen
        vindue.setOnCloseRequest(e -> {
        });
    }

    public void tilHovedmenu() {
        LukProgramPopup lukProgramPopup = new LukProgramPopup();
        String svar = lukProgramPopup.vis("Vil du gemme ændringerne i kalenderen?");

        if (svar.equals("ja")) {
            gemAendringer();
        } else if (svar.equals("annuller")) {
            return;
        }

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        kalenderAnchorPane.getScene().setRoot(root);

        // Fjerner onCloseRequest fra Stage, fordi den ikke længere er relevant, når vi forlader kalenderen
        vindue.setOnCloseRequest(e -> {
        });
    }
}
