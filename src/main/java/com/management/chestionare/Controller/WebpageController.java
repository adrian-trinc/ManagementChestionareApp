package com.management.chestionare.Controller;

import com.management.chestionare.domain.Chestionar;
import com.management.chestionare.domain.Rol;
import com.management.chestionare.domain.Utilizator;
import com.management.chestionare.mapper.MapperChestionarEfectuat;
import com.management.chestionare.mapper.MapperIntrebare;
import com.management.chestionare.service.ServiceChestionar;
import com.management.chestionare.service.ServiceChestionarEfectSiIntrebareEfect;
import com.management.chestionare.service.ServiceIntrebare;
import com.management.chestionare.service.ServiceUtilizator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class WebpageController {
    private final RestTemplate restTemplate;
    private final ServiceUtilizator serviceUtilizator;
    private final ServiceIntrebare serviceIntrebare;
    private final ServiceChestionar serviceChestionar;
    private final ServiceChestionarEfectSiIntrebareEfect serviceChestionarEfectSiIntrebareEfect;
    private final MapperIntrebare mapperIntrebare;
    private final MapperChestionarEfectuat mapperChestionarEfectuat;

    @Autowired
    public WebpageController(RestTemplate restTemplate, ServiceUtilizator serviceUtilizator, ServiceIntrebare serviceIntrebare, ServiceChestionar serviceChestionar, ServiceChestionarEfectSiIntrebareEfect serviceChestionarEfectSiIntrebareEfect, MapperIntrebare mapperIntrebare, MapperChestionarEfectuat mapperChestionarEfectuat) {
        this.restTemplate = restTemplate;
        this.serviceUtilizator = serviceUtilizator;
        this.serviceIntrebare = serviceIntrebare;
        this.serviceChestionar = serviceChestionar;
        this.serviceChestionarEfectSiIntrebareEfect = serviceChestionarEfectSiIntrebareEfect;
        this.mapperIntrebare = mapperIntrebare;
        this.mapperChestionarEfectuat = mapperChestionarEfectuat;
    }

    @GetMapping("/")
    public String principal(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            boolean hasAdministratorRole = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_" + Rol.ADMINISTRATOR.toString()));
            String currentUserName = authentication.getName();
            System.out.println(currentUserName);
            System.out.println(hasAdministratorRole);
            if (hasAdministratorRole) {
                Optional<Utilizator> utilizatorOptional = serviceUtilizator.findUtilizatorByNumeDeUtilizator(currentUserName);
                if (utilizatorOptional.isPresent()) {
                    Utilizator administrator = utilizatorOptional.get();
                    model.addAttribute("numePrenume", administrator.getNumePrenume());
                    System.out.println(model.getAttribute("numePrenume"));
                    model.addAttribute("listaDeChestionare", serviceChestionar.findAll());
                    model.addAttribute("listaDeChestionareAdministratorCurent", serviceChestionar
                            .findAllByUtilizatorCreator_NumeDeUtilizator(currentUserName));
                }
                return "htmlfiles/administrator/administrator.html";
            } else {
                boolean hasUserRole = authentication
                        .getAuthorities()
                        .stream()
                        .anyMatch(r -> r.getAuthority().equals("ROLE_" + Rol.UTILIZATOR_OBISNUIT.toString()));
                System.out.println(hasUserRole);
                if (hasUserRole) {
                    Optional<Utilizator> utilizatorOptional = serviceUtilizator.findUtilizatorByNumeDeUtilizator(currentUserName);
                    if (utilizatorOptional.isPresent()) {
                        Utilizator utilizatorObisnuit = utilizatorOptional.get();
                        model.addAttribute("numePrenume", utilizatorObisnuit.getNumePrenume());
                        System.out.println(model.getAttribute("numePrenume"));
                        model.addAttribute("listaDeChestionare", serviceChestionar.findAll());
                        model.addAttribute("listaDeChestionareEfectuateDeUtilizatorulCurent", mapperChestionarEfectuat
                                .chestionareEfectuateToChestionareEfectuateDTO(
                                        serviceChestionarEfectSiIntrebareEfect
                                                .findAllByUtilizator_NumeDeUtilizator(currentUserName)
                                )
                        );
                    }
                    return "htmlfiles/utilizatorObisnuit/utilizatorObisnuit.html";
                } else {
                    model.addAttribute("titlu", "Conectare esuata");
                    return "htmlfiles/general/invalid.html";
                }
            }
        } else {
            model.addAttribute("titlu", "Conectare esuata");
            return "htmlfiles/general/invalid.html";
        }
    }

    @GetMapping("/administrator")
    public String administrator() {
        return "htmlfiles/administrator/administrator.html";
    }

    @GetMapping("/utilizator")
    public String utilizatorObisnuit() {
        return "htmlfiles/utilizatorObisnuit/utilizatorObisnuit.html";
    }

    @GetMapping("/adaugareChestionar")
    public String adaugareChestionar() {
        return "htmlfiles/administrator/adaugareChestionar.html";
    }

    @GetMapping("/adaugareIntrebarePtChestionar")
    public String adaugareIntrebarePtChestionar(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            boolean hasAdministratorRole = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_" + Rol.ADMINISTRATOR.toString()));
            String currentUserName = authentication.getName();
            if (hasAdministratorRole) {
                model.addAttribute("listaDeChestionareAdministratorCurent", serviceChestionar
                        .findAllByUtilizatorCreator_NumeDeUtilizator(currentUserName));
                return "htmlfiles/administrator/adaugareIntrebarePtChestionar.html";
            } else {
                model.addAttribute("titlu", "Eroare rol de administrator");
                model.addAttribute("mesaj", "Nu aveti rol de administrator.");
                return "htmlfiles/general/invalid.html";
            }
        } else {
            model.addAttribute("titlu", "Eroare server");
            model.addAttribute("mesaj", "Probleme cu SecurityContext.");
            return "htmlfiles/general/invalid.html";
        }
    }

    @GetMapping("/adaugareVariantaDeRaspuns")
    public String adaugareVariantaDeRaspuns(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            boolean hasAdministratorRole = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_" + Rol.ADMINISTRATOR.toString()));
            String currentUserName = authentication.getName();
            if (hasAdministratorRole) {
                List<Chestionar> chestionare = serviceChestionar.findAllAscByIdByUtilizatorCreator_NumeDeUtilizator(currentUserName);
                model.addAttribute("listaDeChestionareAdministratorCurent", chestionare);
                model.addAttribute("listaDeIntrebariPrimulChestionar", mapperIntrebare.intrebariToIntrebariDTOV2(serviceIntrebare
                        .findAllByChestionar_ChestionarId(chestionare.get(0).getChestionarId())));
                model.addAttribute("listaDeIntrebariChestionareAdministratorCurent", mapperIntrebare.intrebariToIntrebariDTOV2(serviceIntrebare
                        .findAllByChestionar_UtilizatorCreator_NumeDeUtilizator(currentUserName)));
                return "htmlfiles/administrator/adaugareVariantaDeRaspuns.html";
            } else {
                model.addAttribute("titlu", "Eroare rol de administrator");
                model.addAttribute("mesaj", "Nu aveti rol de administrator.");
                return "htmlfiles/general/invalid.html";
            }
        } else {
            model.addAttribute("titlu", "Eroare server");
            model.addAttribute("mesaj", "Probleme cu SecurityContext.");
            return "htmlfiles/general/invalid.html";
        }
    }

    @GetMapping("/adaugareVariantaDeRaspunsPtIntrebare/{intrebareId}")
    public String adaugareVariantaDeRaspunsPtIntrebare(@PathVariable Long intrebareId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            boolean hasAdministratorRole = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_" + Rol.ADMINISTRATOR.toString()));
            if (hasAdministratorRole) {
                model.addAttribute("intrebareId", intrebareId);
                return "htmlfiles/administrator/adaugareVariantaDeRaspunsPtIntrebare.html";
            } else {
                model.addAttribute("titlu", "Eroare rol de administrator");
                model.addAttribute("mesaj", "Nu aveti rol de administrator.");
                return "htmlfiles/general/invalid.html";
            }
        } else {
            model.addAttribute("titlu", "Eroare server");
            model.addAttribute("mesaj", "Probleme cu SecurityContext.");
            return "htmlfiles/general/invalid.html";
        }
    }

    @GetMapping("/jsfiles/administrator/adaugareVariantaDeRaspuns.js")
    public String adaugareVariantaDeRaspunsjs() {
        return "jsfiles/administrator/adaugareVariantaDeRaspuns.js";
    }
}
