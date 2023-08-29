package utils;

import fr.univartois.ili.jai.object.Game;
import fr.univartois.ili.jai.object.Player;
import fr.univartois.ili.jai.object.Team;
import fr.univartois.ili.jai.persistance.*;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class GameTDGTest {

    private GameTDG gtdg;
    private TeamTDG ttdg;

    @BeforeAll
    static void createTable() throws SQLException {
        TDGRegistry.findTDG(Player.class).createTable();
        TDGRegistry.findTDG(Team.class).createTable();
        TDGRegistry.findTDG(Game.class).createTable();
    }

    @AfterAll
    static void deleteTable() throws SQLException {
        TDGRegistry.findTDG(Game.class).deleteTable();
        TDGRegistry.findTDG(Team.class).deleteTable();
        TDGRegistry.findTDG(Player.class).deleteTable();
    }

    @BeforeEach
    void setUp() {
        gtdg = TDGRegistry.findTDG(Game.class);
        ttdg = TDGRegistry.findTDG(Team.class);
    }

    @Test
    void test01EmptyBase() throws SQLException {
        assertNull(gtdg.findById(1));
    }

    @Test
    void test02InsertElementWithoutSubElements() throws SQLException {
        Game g1 = new Game();
        assertThrows(SQLException.class, () -> gtdg.insert(g1));
    }

    @Test
    void test03InsertElement() throws SQLException {
        Game g1 = new Game();
        g1.setWinnerTeam(g1.getBlueTeam());
        assertEquals(0, g1.getId());
        gtdg.insert(g1);
        assertNotEquals(0, g1.getId());
        assertNotEquals(0, g1.getRedTeam().getId());
        assertNotEquals(0, g1.getBlueTeam().getId());
        assertNotEquals(0, g1.getWinnerTeam().getId());
    }

    @Test
    void test04FindById() throws SQLException {
        Game g1 = new Game();
        g1.setWinnerTeam(g1.getBlueTeam());
        gtdg.insert(g1);
        Game g2 = gtdg.findById(g1.getId());
        assertEquals(g1, g2);
    }

    @Test
    void test05FindByIdWhereIdIsNull() throws SQLException {
        Game g1 = new Game();
        long g1Id = g1.getId();
        assertThrows(IllegalArgumentException.class, () -> gtdg.findById(g1Id));
    }

    @Test
    void test06FindByIdWithSubElements() throws SQLException {
        Game g1 = new Game();
        g1.setWinnerTeam(g1.getBlueTeam());
        gtdg.insert(g1);
        Game g2 = gtdg.findById(g1.getId());
        assertEquals(g1.getId(), g2.getId());
        assertEquals(g1.getBlueTeam(), g2.getBlueTeam());
        assertEquals(g1.getRedTeam(), g2.getRedTeam());
        assertEquals(g1.getWinnerTeam(), g2.getBlueTeam());
    }

    @Test
    void test07InsertAlreadyExistingElement() throws SQLException {
        Game g1 = gtdg.findById(2);
        assertThrows(IllegalArgumentException.class, () -> gtdg.insert(g1));
    }

    @Test
    void test08InsertElementWithUnknownSubElement() throws SQLException {
        Game g1 = new Game();
        g1.setWinnerTeam(new Team());
        assertThrows(SQLException.class, () -> gtdg.insert(g1));
    }

    @Test
    void test09InsertElementWithSameSubElements() throws SQLException {
        Game g1 = new Game();
        Team t1 = new Team();
        ttdg.insert(t1);
        g1.setBlueTeam(t1);
        g1.setRedTeam(t1);
        g1.setWinnerTeam(t1);
        assertThrows(SQLException.class, () -> gtdg.insert(g1));
    }

    @Test
    void test10UpdateUnknownElement() throws SQLException {
        Game g1 = new Game();
        assertThrows(IllegalArgumentException.class, () -> gtdg.update(g1));
    }

    @Test
    void test11UpdateElement() throws SQLException {
        Game g1 = gtdg.findById(2);
        Team tWinner = ttdg.findById(g1.getWinnerTeam().getId());
        Team t1 = ttdg.findById(g1.getBlueTeam().getId());
        assertEquals(t1, tWinner);
        g1.setWinnerTeam(g1.getRedTeam());
        gtdg.update(g1);
        Game g2 = gtdg.findById(2);
        assertEquals(g1, g2);
        assertEquals(g1.getWinnerTeam(), g2.getWinnerTeam());
    }

    @Test
    void test12UpdateElementSetSubElementToNull() throws SQLException {
        Game g1 = new Game();
        g1.setWinnerTeam(g1.getBlueTeam());
        gtdg.insert(g1);
        assertEquals(g1.getBlueTeam(), g1.getWinnerTeam());
        g1.setRedTeam(null);
        assertThrows(SQLException.class, () -> gtdg.update(g1));
    }

    @Test
    void test13UpdateElementWithUnknownSubElement() throws SQLException {
        Game g1 = gtdg.findById(3);
        g1.setWinnerTeam(new Team());
        assertThrows(SQLException.class, () -> gtdg.update(g1));
    }

    @Test
    void test14UpdateElementWithSameSubElements() throws SQLException {
        Game g1 = new Game();
        g1.setWinnerTeam(g1.getBlueTeam());
        gtdg.insert(g1);
        g1.setRedTeam(g1.getBlueTeam());
        assertThrows(SQLException.class, () -> gtdg.update(g1));
    }

    @Test
    void test15DeleteUnknownElement() throws SQLException {
        Game g1 = new Game();
        assertThrows(IllegalArgumentException.class, () -> gtdg.delete(g1));
    }

    @Test
    void test16DeleteElement() throws SQLException {
        Game g1 = gtdg.findById(2);
        gtdg.delete(g1);
        assertNull(gtdg.findById(2));
    }

    @Test
    void test17DeleteSubElement() throws SQLException {
        Game g1 = new Game();
        g1.setWinnerTeam(g1.getRedTeam());
        gtdg.insert(g1);
        ttdg.delete(g1.getBlueTeam());
        assertNull(gtdg.findById(g1.getId()));
    }

    @Test
    void test18RefreshUnknownElement() throws SQLException {
        Game g1 = new Game();
        assertThrows(IllegalArgumentException.class, () -> gtdg.refresh(g1));
    }

    @Test
    void test19RefreshElement() throws SQLException {
        Game g1 = new Game();
        g1.setWinnerTeam(g1.getBlueTeam());
        gtdg.insert(g1);
        assertEquals(g1.getBlueTeam(), g1.getWinnerTeam());
        g1.setWinnerTeam(g1.getRedTeam());
        assertEquals(g1.getRedTeam(), g1.getWinnerTeam());
        gtdg.refresh(g1);
        assertEquals(g1.getBlueTeam(), g1.getWinnerTeam());
    }

    @Test
    void test20SelectWhere() throws SQLException {
        Game g1 = new Game();
        g1.setWinnerTeam(g1.getRedTeam());
        gtdg.insert(g1);
        Game g2 = gtdg.selectWhere("ID = ?", g1.getId()).get(0);
        assertEquals(g1, g2);
    }

    @Test
    void test21SelectWhereWithSubElements() throws SQLException {
        Game g1 = new Game();
        g1.setWinnerTeam(g1.getRedTeam());
        gtdg.insert(g1);
        Game g2 = gtdg.selectWhere("ID = ?", g1.getId()).get(0);
        assertEquals(g1, g2);
        assertEquals(g1.getBlueTeam(), g2.getBlueTeam());
        assertEquals(g1.getRedTeam(), g2.getRedTeam());
        assertEquals(g1.getWinnerTeam(), g2.getWinnerTeam());
        assertEquals(g1.getWinnerTeam(), g2.getRedTeam());
    }
}
