package utils;

import fr.univartois.ili.jai.object.Player;
import fr.univartois.ili.jai.object.Team;
import fr.univartois.ili.jai.persistance.PlayerTDG;
import fr.univartois.ili.jai.persistance.TDGRegistry;
import fr.univartois.ili.jai.persistance.TeamTDG;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class TeamTDGTest {

	private TeamTDG ttdg;
	private PlayerTDG ptdg;

	@BeforeAll
	static void createTable() throws SQLException {
		TDGRegistry.findTDG(Player.class).createTable();
		TDGRegistry.findTDG(Team.class).createTable();
	}

	@AfterAll
	static void deleteTable() throws SQLException {
		TDGRegistry.findTDG(Team.class).deleteTable();
		TDGRegistry.findTDG(Player.class).deleteTable();
	}

	@BeforeEach
	void setUp() {
		ttdg = TDGRegistry.findTDG(Team.class);
		ptdg = TDGRegistry.findTDG(Player.class);
	}

	@Test
	void test01EmptyBase() throws SQLException {
		assertNull(ttdg.findById(1));
	}

	@Test
	void test02InsertElement() throws SQLException {
		Team t1 = new Team();
		assertEquals(0, t1.getId());
		ttdg.insert(t1);
		assertNotEquals(0, t1.getId());
	}

	@Test
	void test03FindById() throws SQLException {
		Team t1 = new Team();
		ttdg.insert(t1);
		Team t2 = ttdg.findById(t1.getId());
		assertEquals(t1, t2);
	}

	@Test
	void test04FindByIdWhereIdIsNull() throws SQLException {
		Team t1 = new Team();
		long t1Id = t1.getId();
		assertThrows(IllegalArgumentException.class, () -> ttdg.findById(t1Id));
	}

	@Test
	void test05InsertAlreadyExistingElement() throws SQLException {
		Team t1 = ttdg.findById(1);
		assertThrows(IllegalArgumentException.class, () -> ttdg.insert(t1));
	}

	@Test
	void test06InsertElementWithSubElements() throws SQLException {
		Team t1 = new Team();
		Player p1 = new Player("Julien");
		Player p2 = new Player("Christine");
		t1.addPlayers(p1, p2);
		ttdg.insert(t1);
		assertNotEquals(0, t1.getId());
		assertNotEquals(0, p1.getId());
		assertNotEquals(0, p2.getId());
	}

	@Test
	void test07InsertElementWithSubElementsAlreadyExisting() throws SQLException {
		Team t1 = new Team();
		Player p1 = new Player("Julien");
		Player p2 = new Player("Christine");
		ptdg.insert(p1);
		ptdg.insert(p2);
		t1.addPlayers(p1, p2);
		ttdg.insert(t1);
		assertNotEquals(0, t1.getId());
		assertNotEquals(0, p1.getId());
		assertNotEquals(0, p2.getId());
	}

	@Test
	void test08FindByIdWithSubElements() throws SQLException {
		Team t1 = new Team();
		Player p1 = new Player("Clara");
		Player p2 = new Player("Christian");
		t1.addPlayers(p1, p2);
		ttdg.insert(t1);
		Team t2 = ttdg.findById(t1.getId());
		assertEquals(t1, t2);
		assertEquals(t1.getPlayers().size(), t2.getPlayers().size());
		Iterator<Player> iteratorP1 = t1.getPlayers().iterator();
		Iterator<Player> iteratorP2 = t2.getPlayers().iterator();
		Player p3 = null;
		Player p4 = null;
		while (iteratorP1.hasNext()) {
			p3 = iteratorP1.next();
			p4 = iteratorP2.next();
			assertEquals(p3.getUsername(), p4.getUsername());
			assertEquals(p3, p4);
		}
	}

	@Test
	void test09InsertElementWithMultipleSameSubElements() throws SQLException {
		Team t1 = new Team();
		Player p1 = new Player("Julien");
		Player p2 = new Player("Julien");
		t1.addPlayers(p1, p2);
		ttdg.insert(t1);
		assertEquals(1, ttdg.findById(t1.getId()).getPlayers().size());
	}

	@Test
	void test10InsertElementsWithSameSubElements() throws SQLException {
		Team t1 = new Team();
		Player p1 = new Player("Hugo");
		t1.addPlayers(p1);
		ttdg.insert(t1);
		Team t2 = new Team();
		Player p1Bis = new Player("Hugo");
		t2.addPlayers(p1Bis);
		ttdg.insert(t2);
		assertEquals(t1, t2);
		Iterator<Player> iteratorP1 = t1.getPlayers().iterator();
		Iterator<Player> iteratorP2 = t2.getPlayers().iterator();
		while (iteratorP1.hasNext()) {
			assertEquals(iteratorP1.next(), iteratorP2.next());
		}
	}

	@Test
	void test11InsertElementsWithSameSubElementsUnordered() throws SQLException {
		Team t1 = new Team();
		Player p1 = new Player("Julien");
		Player p2 = new Player("Christine");
		t1.addPlayers(p1, p2);
		ttdg.insert(t1);
		Team t2 = new Team();
		Player p1Bis = new Player("Christine");
		Player p2Bis = new Player("Julien");
		t2.addPlayers(p1Bis, p2Bis);
		ttdg.insert(t2);
		assertEquals(t1, t2);
		Iterator<Player> iteratorP1 = t1.getPlayers().iterator();
		Iterator<Player> iteratorP2 = t2.getPlayers().iterator();
		while (iteratorP1.hasNext()) {
			assertNotEquals(iteratorP1.next(), iteratorP2.next());
		}
	}

	@Test
	void test12UpdateUnknownElement() throws SQLException {
		Team t1 = new Team();
		assertThrows(IllegalArgumentException.class, () -> ttdg.update(t1));
	}

	@Test
	void test13UpdateElementWhenAddSubElement() throws SQLException {
		Team t1 = ttdg.findById(1);
		t1.addPlayer(new Player("Christine"));
		assertNotEquals(t1.getPlayers().size(), ttdg.findById(t1.getId()).getPlayers().size());
		ttdg.update(t1);
		Team t2 = ttdg.findById(t1.getId());
		assertEquals(t1.getPlayers().size(), t2.getPlayers().size());
		Iterator<Player> iteratorP1 = t1.getPlayers().iterator();
		Iterator<Player> iteratorP2 = t2.getPlayers().iterator();
		while (iteratorP1.hasNext()) {
			assertEquals(iteratorP1.next(), iteratorP2.next());
		}
	}

	@Test
	void test14UpdateElementWhenRemoveSubElement() throws SQLException {
		Team t1 = new Team();
		Player p1 = new Player("Julien");
		t1.addPlayer(p1);
		t1.addPlayer(new Player("Christine"));
		ttdg.insert(t1);
		t1.removePlayer(p1);
		assertEquals(t1.getPlayers().size(), ttdg.findById(t1.getId()).getPlayers().size() - 1);
		ttdg.update(t1);
		Iterator<Player> iteratorP1 = t1.getPlayers().iterator();
		Iterator<Player> iteratorP2 = ttdg.findById(t1.getId()).getPlayers().iterator();
		while (iteratorP1.hasNext()) {
			assertEquals(iteratorP1.next(), iteratorP2.next());
		}
	}

	@Test
	void test15UpdateElementWhenAddAlreadyExistingSubElement() throws SQLException {
		Team t1 = new Team();
		t1.addPlayers(new Player("Valérie"), new Player("Edouard"));
		ttdg.insert(t1);
		assertEquals(2, ttdg.findById(t1.getId()).getPlayers().size());
		t1.addPlayer(new Player("Valérie"));
		ttdg.update(t1);
		assertEquals(2, ttdg.findById(t1.getId()).getPlayers().size());
	}

	@Test
	void test16DeleteUnknownElement() throws SQLException {
		Team t1 = new Team();
		assertThrows(IllegalArgumentException.class, () -> ttdg.delete(t1));
	}

	@Test
	void test17DeleteElement() throws SQLException {
		Team t1 = ttdg.findById(1);
		ttdg.delete(t1);
		assertNull(ttdg.findById(1));
	}

	@Test
	void test18RefreshUnknownElement() throws SQLException {
		Team t1 = new Team();
		assertThrows(IllegalArgumentException.class, () -> ttdg.refresh(t1));
	}

	@Test
	void test19RefreshElementAfterAddSubElement() throws SQLException {
		Team t1 = new Team();
		t1.addPlayer(new Player("Charles"));
		ttdg.insert(t1);
		Team t2 = ttdg.findById(t1.getId());
		t1.addPlayer(new Player("Thimothée"));
		assertNotEquals(t1.getPlayers().size(), t2.getPlayers().size());
		ttdg.refresh(t1);
		assertEquals(t1.getPlayers().size(), t2.getPlayers().size());
		Iterator<Player> iteratorP1 = t1.getPlayers().iterator();
		Iterator<Player> iteratorP2 = t2.getPlayers().iterator();
		while (iteratorP1.hasNext()) {
			assertEquals(iteratorP1.next(), iteratorP2.next());
		}
	}

	@Test
	void test20RefreshElementAfterRemoveSubElement() throws SQLException {
		Team t1 = new Team();
		t1.addPlayer(new Player("Charles"));
		ttdg.insert(t1);
		Team t2 = ttdg.findById(t1.getId());
		t1.removeAllPlayers();
		assertNotEquals(t1.getPlayers().size(), t2.getPlayers().size());
		ttdg.refresh(t1);
		assertEquals(t1.getPlayers().size(), t2.getPlayers().size());
		Iterator<Player> iteratorP1 = t1.getPlayers().iterator();
		Iterator<Player> iteratorP2 = t2.getPlayers().iterator();
		while (iteratorP1.hasNext()) {
			assertEquals(iteratorP1.next(), iteratorP2.next());
		}
	}

	@Test
	void test21RefreshElementAfterDeleteSubElement() throws SQLException {
		Team t1 = new Team();
		Player p1 = new Player("Valérie");
		t1.addPlayers(p1, new Player("Edouard"));
		ttdg.insert(t1);
		ptdg.delete(p1);
		assertEquals(t1.getPlayers().size() - 1, ttdg.findById(t1.getId()).getPlayers().size());
		Team t2 = new Team();
		t2.addPlayer(new Player("Edouard"));
		ttdg.insert(t2);
		assertEquals(t2.getId(), ttdg.findById(t1.getId()).getId());
		ttdg.refresh(t1);
		assertEquals(1, t1.getPlayers().size());
	}

	@Test
	void test22SelectWhere() throws SQLException {
		Team t1 = new Team();
		ttdg.insert(t1);
		Team t2 = ttdg.selectWhere("ID = ?", t1.getId()).get(0);
		assertEquals(t1, t2);
	}

	@Test
	void test23SelectWhereWithSubElements() throws SQLException {
		Team t1 = new Team();
		t1.addPlayers(new Player("Charles"), new Player("José"));
		ttdg.insert(t1);
		Team t2 = ttdg.selectWhere("ID = ?", t1.getId()).get(0);
		assertEquals(t1, t2);
		assertEquals(t1.getPlayers().size(), t2.getPlayers().size());
		Iterator<Player> iteratorP1 = t1.getPlayers().iterator();
		Iterator<Player> iteratorP2 = t2.getPlayers().iterator();
		while (iteratorP1.hasNext()) {
			assertEquals(iteratorP1.next(), iteratorP2.next());
		}
	}
}
