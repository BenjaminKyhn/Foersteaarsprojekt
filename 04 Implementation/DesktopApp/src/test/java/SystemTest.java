import model.BrugerFacade;
import model.exceptions.EksisterendeBrugerException;
import model.exceptions.TomEmailException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SystemTest {

    @Test
    public void ST010101() throws TomEmailException, EksisterendeBrugerException {
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        String email = "test@mail.dk";
        brugerFacade.tjekEmail(email);
    }

    @Test
    public void ST010102() {
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        String email = "";
        assertThrows(TomEmailException.class, () -> brugerFacade.tjekEmail(email));
    }

    @Test
    public void ST010103() {
        BrugerFacade brugerFacade = BrugerFacade.getInstance();
        String email = "fys@frbsport.dk";
        assertThrows(EksisterendeBrugerException.class, () -> brugerFacade.tjekEmail(email));
    }
}