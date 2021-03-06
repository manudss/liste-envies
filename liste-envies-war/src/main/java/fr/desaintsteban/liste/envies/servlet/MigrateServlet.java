package fr.desaintsteban.liste.envies.servlet;

import com.googlecode.objectify.Objectify;
import fr.desaintsteban.liste.envies.model.Envy;
import fr.desaintsteban.liste.envies.model.ListEnvies;
import fr.desaintsteban.liste.envies.model.UserShare;
import fr.desaintsteban.liste.envies.service.OfyService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class MigrateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        Objectify ofy = OfyService.ofy();
        List<Envy> list = ofy.load().type(Envy.class).list();
        List<Envy> listConverted = new ArrayList<>();
        for (Envy envie : list) {
           if (envie.getUserTake() != null) {
               if (envie.getArchived() == null) {
                   envie.setArchived(false);
               }
               listConverted.add(envie);
           }
        }
        ofy.save().entities(listConverted);

        out.println("OK");
    }
}
