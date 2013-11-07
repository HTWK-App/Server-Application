package com.htwk.app.controller;
 
import java.io.IOException;
import java.net.URISyntaxException;
 
import javax.naming.directory.InvalidAttributesException;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xmlpull.v1.XmlPullParserException;
 
import com.google.gson.JsonObject;
import com.htwk.app.model.impl.Day;
import com.htwk.app.model.mensa.Meal;
import com.htwk.app.repository.MensaRepository;
 
@Controller
@RequestMapping(value = "/mensa")
public class MensaController {
 
    private static final Logger logger = LoggerFactory.getLogger(MensaController.class);
 
    @Autowired
    private MensaRepository repo;
 
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String redirectHome() {
        return "redirect:/mensa/";
    }
 
    @Cacheable("timeCache")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String home() {
        JsonObject json = new JsonObject();
         
        JsonObject location = new JsonObject();
        location.addProperty("Cafeteria Dittrichring", 153);
        location.addProperty("Cafeteria Koburger Straﬂe", 121);
        location.addProperty("Cafeteria Philipp-Rosenthal-Straﬂe", 127);
        location.addProperty("Cafeteria W‰chterstraﬂe", 129);
        location.addProperty("Mensa Academica", 118);
        location.addProperty("Mensa am Park", 106);
        location.addProperty("Mensa am Elsterbecken", 115);
        location.addProperty("Mensaria Liebigstraﬂe", 162);
        location.addProperty("Mensa Peterssteinweg", 111);
        location.addProperty("Mensa Schˆnauer Straﬂe", 140);
        location.addProperty("Mensa Tierklinik", 170);
        json.add("location", location);
         
        return ""+json;
    }
 
    @Cacheable("timeCache")
    @RequestMapping(value = "/{location}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody
    Day<Meal> getMenuByLocation(@PathVariable(value = "location") String location) throws InvalidAttributesException,
            IOException, URISyntaxException, XmlPullParserException {
        return repo.get(location);
    }
 
    @Cacheable("timeCache")
    @RequestMapping(value = "/{location}/{date}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody
    Day<Meal> getMenuByLocationAndDate(@PathVariable(value = "location") String location,
            @PathVariable(value = "date") String date) throws InvalidAttributesException, IOException,
            URISyntaxException, XmlPullParserException {
        return repo.get(location, date);
    }
}