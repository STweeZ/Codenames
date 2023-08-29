package utils;

import fr.univartois.ili.jai.object.Player;
import fr.univartois.ili.jai.persistance.PlayerTDG;
import fr.univartois.ili.jai.persistance.TDGRegistry;

import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class PlayerTDGTest {

	private PlayerTDG ptdg;

	@BeforeAll
	static void createTable() throws SQLException {
		TDGRegistry.findTDG(Player.class).createTable();
	}

	@AfterAll
	static void deleteTable() throws SQLException {
		TDGRegistry.findTDG(Player.class).deleteTable();
	}

	@BeforeEach
	void setUp() {
		ptdg = TDGRegistry.findTDG(Player.class);
	}

	@Test
	void test01EmptyBase() throws SQLException {
		assertNull(ptdg.findById(1));
	}

	@Test
	void test02InsertElement() throws SQLException {
		Player p1 = new Player("Michel");
		assertEquals(0, p1.getId());
		ptdg.insert(p1);
		assertNotEquals(0, p1.getId());
	}

	@Test
	void test03FindElementById() throws SQLException {
		Player p1 = new Player("Jeanne");
		ptdg.insert(p1);
		Player p2 = ptdg.findById(p1.getId());
		assertEquals(p1, p2);
	}

	@Test
	void test04FindElementByIdWhereIdIsNull() throws SQLException {
		Player p1 = new Player("Michel");
		long p1Id = p1.getId();
		assertThrows(IllegalArgumentException.class, () -> ptdg.findById(p1Id));
	}

	@Test
	void test05InsertAlreadyExistingElement() throws SQLException {
		Player p1 = ptdg.findById(1);
		assertThrows(IllegalArgumentException.class, () -> ptdg.insert(p1));
	}

	@Test
	void test06InsertElementWithUsernameAlreadyExisting() throws SQLException {
		Player p1 = new Player("Marie");
		ptdg.insert(p1);
		Player p2 = new Player("Marie");
		ptdg.insert(p2);
		assertEquals(p1, p2);
	}

	@Test
	void test07UpdateUnknownElement() throws SQLException {
		Player p1 = new Player("Julien");
		assertThrows(IllegalArgumentException.class, () -> ptdg.update(p1));
	}

	@Test
	void test08UpdateElement() throws SQLException {
		Player p1 = ptdg.findById(1);
		assertEquals("Michel", p1.getUsername());
		p1.setUsername("Christine");
		assertEquals("Christine", p1.getUsername());
		ptdg.update(p1);
		Player p2 = ptdg.findById(1);
		assertEquals("Christine", p2.getUsername());
	}

	@Test
	void test09UpdateElementWithUsernameAlreadyExisting() throws SQLException {
		Player p1 = new Player("Daniel");
		ptdg.insert(p1);
		p1.setUsername("Christine");
		assertThrows(SQLException.class, () -> ptdg.update(p1));
	}

	@Test
	void test10DeleteUnknownElement() throws SQLException {
		Player p1 = new Player("Julien");
		assertThrows(IllegalArgumentException.class, () -> ptdg.delete(p1));
	}

	@Test
	void test11DeleteElement() throws SQLException {
		Player p1 = ptdg.findById(1);
		assertEquals("Christine", p1.getUsername());
		ptdg.delete(p1);
		assertNull(ptdg.findById(1));
	}

	@Test
	void test12RefreshUnknownElement() throws SQLException {
		Player p1 = new Player("Julien");
		assertThrows(IllegalArgumentException.class, () -> ptdg.refresh(p1));
	}

	@Test
	void test13RefreshElement() throws SQLException {
		Player p1 = ptdg.findById(3);
		p1.setUsername("Alexandra");
		assertEquals("Alexandra", p1.getUsername());
		ptdg.refresh(p1);
		assertEquals("Marie", p1.getUsername());
	}

	@Test
	void test14SelectWhere() throws SQLException {
		Player p1 = new Player("Maxime");
		ptdg.insert(p1);
		Player p2 = ptdg.selectWhere("USERNAME LIKE ?", p1.getUsername()).get(0);
		assertEquals(p1, p2);
		assertEquals(p1.getUsername(), p2.getUsername());
	}
}
