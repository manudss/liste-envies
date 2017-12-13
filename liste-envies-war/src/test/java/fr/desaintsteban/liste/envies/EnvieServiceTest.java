package fr.desaintsteban.liste.envies;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cache.AsyncCacheFilter;
import fr.desaintsteban.liste.envies.dto.WishDto;
import fr.desaintsteban.liste.envies.dto.NoteDto;
import fr.desaintsteban.liste.envies.model.AppUser;
import fr.desaintsteban.liste.envies.model.Wish;
import fr.desaintsteban.liste.envies.model.WishList;
import fr.desaintsteban.liste.envies.model.Notification;
import fr.desaintsteban.liste.envies.service.AppUserService;
import fr.desaintsteban.liste.envies.service.WishesService;
import fr.desaintsteban.liste.envies.service.WishListService;
import fr.desaintsteban.liste.envies.service.OfyService;
import fr.desaintsteban.liste.envies.util.EncodeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class EnvieServiceTest {
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy(),
            new LocalMemcacheServiceTestConfig(),
            new LocalTaskQueueTestConfig());
    private Closeable closable;
    private AppUser patrice;
    private Long livreId;
    private Long dvdId;
    private AppUser emmanuel;
    private WishList listePatrice;
    private WishList listeEmmanuel;
    private AppUser clemence;

    @BeforeClass
    public static void setUpBeforeClass()
    {
        //ObjectifyService.reset();
        // Reset the Factory so that all translators work properly.
        ObjectifyService.setFactory(new ObjectifyFactory() {
            @Override
            public Objectify begin()
            {
                return super.begin().cache(false);
            }
        });
        ObjectifyService.factory().register(AppUser.class);
        ObjectifyService.factory().register(Wish.class);
        ObjectifyService.factory().register(WishList.class);
        ObjectifyService.factory().register(Notification.class);
    }

    @Before
    public void setUp() {
        helper.setUp();
        closable = OfyService.begin();
        patrice = new AppUser("patrice@desaintsteban.fr", "Patrice");
        AppUserService.createOrUpdate(patrice);
        emmanuel = new AppUser("emmanuel@desaintsteban.fr", "Emmanuel");
        AppUserService.createOrUpdate(emmanuel);
        clemence = AppUserService.createOrUpdate(new AppUser("clemence@desaintsteban.fr", "Clemence"));
        listePatrice = WishListService.createOrUpdate(patrice, new WishList("liste-patrice", "Liste de Patrice", patrice.getEmail(), emmanuel.getEmail()));
        listeEmmanuel = WishListService.createOrUpdate(emmanuel, new WishList("liste-emmanuel", "Liste d'Emmanuel", emmanuel.getEmail(), patrice.getEmail(), clemence.getEmail()));

        WishDto itemLivre = WishesService.createOrUpdate(patrice, "liste-patrice", new Wish(listePatrice, "Livre"));
        livreId = itemLivre.getId();
        WishDto itemDvd = WishesService.createOrUpdate(patrice, "liste-patrice", new Wish(listePatrice, "DVD"));
        dvdId = itemDvd.getId();
        WishesService.given(emmanuel, "liste-patrice", livreId);
    }

    @After
    public void tearDown() {
        helper.tearDown();
        AsyncCacheFilter.complete();
        try {
            closable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSameUser() throws Exception {
        Wish envie = WishesService.get(patrice, "liste-patrice", livreId);
        assertThat(envie.getLabel()).isEqualTo("Livre");
        assertThat(envie.getUserTake()).isNull();
    }

    @Test
    public void testGetNotSameUser() throws Exception {
        Wish envie = WishesService.get(emmanuel, "liste-patrice", livreId);
        assertThat(envie.getLabel()).isEqualTo("Livre");
        assertThat(envie.getUserTake()).contains(EncodeUtils.encode("emmanuel@desaintsteban.fr"));
    }

    @Test
    public void testList() throws Exception {
        List<Wish> list = WishesService.list(patrice, "liste-patrice");
        assertThat(list).hasSize(2).onProperty("label").contains("Livre", "DVD");
        assertThat(list).hasSize(2).onProperty("userTake").excludes(EncodeUtils.encode("emmanuel@desaintsteban.fr"));
    }

    @Test
    public void testListOther() throws Exception {
        List<Wish> list = WishesService.list(emmanuel, "liste-patrice");
        assertThat(list).hasSize(2).onProperty("label").contains("Livre", "DVD");
        //assertThat(list).hasSize(2).onProperty("userTake"). contains(EncodeUtils.encode("emmanuel@desaintsteban.fr"));
    }

    @Test
    public void testCreate() throws Exception {
        WishDto itemDvd = WishesService.createOrUpdate(emmanuel, "liste-emmanuel", new Wish(listeEmmanuel, "DVD"));
    }

    @Test
    public void testDelete() throws Exception {
        WishesService.delete(patrice, "liste-patrice", livreId);
    }

    @Test
    public void testSaveNote() throws Exception {
        WishDto initdto = new WishDto();
        initdto.setLabel("Test");
        NoteDto c1 = new NoteDto();
        c1.setOwner("Emmanuel");
        c1.setEmail("emmanuel@desaintsteban.fr");
        c1.setText("Commentaire");
        NoteDto c2 = new NoteDto();
        c2.setEmail("clemence@desaintsteban.fr");
        c2.setOwner("Patrice");
        c2.setText("Commentaire2");
        Wish envie = new Wish(initdto);

        WishDto saved = WishesService.createOrUpdate(emmanuel, "liste-emmanuel", envie);

        WishesService.addNote(patrice, saved.getId(), "liste-emmanuel", c1);
        WishesService.addNote(clemence, saved.getId(), "liste-emmanuel", c2);
        Wish get = WishesService.get(patrice, "liste-emmanuel", saved.getId());

        WishDto dto = get.toDto();

        assertThat(dto.getLabel()).isEqualTo(initdto.getLabel());
        assertThat(dto.getNotes()).onProperty("email").contains("clemence@desaintsteban.fr", "patrice@desaintsteban.fr");
        assertThat(dto.getNotes()).onProperty("text").contains("Commentaire", "Commentaire2");
    }
}
