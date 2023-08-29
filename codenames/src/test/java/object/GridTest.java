package object;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import fr.univartois.ili.jai.object.CardType;
import fr.univartois.ili.jai.object.Grid;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class GridTest {
    @Test
    void test01Length() throws SQLException, IOException {
        Grid grid = new Grid();
        grid.initGrid();
        assertEquals(grid.getCards().size(), 25);
        assertEquals(grid.blueCardsRemaining(), 9);
        assertEquals(grid.redCardsRemaining(), 8);
        assertEquals(grid.teamCardsRemaining(CardType.NEUTRAL), 7);
        assertEquals(grid.teamCardsRemaining(CardType.TRAP), 1);
    }
}
