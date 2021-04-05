package com.management.chestionare.Controller;

import com.management.chestionare.domain.*;
import com.management.chestionare.dtodomain.AdaugareIntrebareDTO;
import com.management.chestionare.dtodomain.IntrebareDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class ControllerIntrebare {
    private final ServiceIntrebare serviceIntrebare;
    private final ServiceChestionar serviceChestionar;
    private final ServiceUtilizator serviceUtilizator;
    private final ServiceChestionarEfectSiIntrebareEfect serviceChestionarEfectSiIntrebareEfect;
    private final MapperIntrebare mapperIntrebare;

    @Autowired
    public ControllerIntrebare(ServiceIntrebare serviceIntrebare, ServiceChestionar serviceChestionar, ServiceUtilizator serviceUtilizator, ServiceChestionarEfectSiIntrebareEfect serviceChestionarEfectSiIntrebareEfect, MapperIntrebare mapperIntrebare) {
        this.serviceIntrebare = serviceIntrebare;
        this.serviceChestionar = serviceChestionar;
        this.serviceUtilizator = serviceUtilizator;
        this.serviceChestionarEfectSiIntrebareEfect = serviceChestionarEfectSiIntrebareEfect;
        this.mapperIntrebare = mapperIntrebare;
    }

    @PostMapping("/intrebare/format-json")
    public String adaugareIntrebare(@RequestBody AdaugareIntrebareDTO adaugareIntrebareDTO, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            boolean hasAdministratorRole = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_" + Rol.ADMINISTRATOR.toString()));
            String currentUserName = authentication.getName();
            if (hasAdministratorRole) {
                Optional<Utilizator> utilizatorOptional = serviceUtilizator.findUtilizatorByNumeDeUtilizator(currentUserName);
                if (utilizatorOptional.isPresent()) {
                    Utilizator administrator = utilizatorOptional.get();
                    Optional<Chestionar> chestionarOptional = serviceChestionar.findById(adaugareIntrebareDTO.chestionarId);
                    if (chestionarOptional.isPresent()) {
                        Chestionar chestionar = chestionarOptional.get();
                        if (administrator.getUtilizatorId().equals(chestionar.getUtilizatorCreator().getUtilizatorId())) {
                            Intrebare intrebare = new Intrebare(
                                    0L,
                                    adaugareIntrebareDTO.continut,
                                    adaugareIntrebareDTO.numarDePuncte,
                                    false,
                                    chestionar,
                                    new HashSet<>());
                            serviceIntrebare.save(chestionar, intrebare);
                            model.addAttribute("succes", true);
                            return "htmlfiles/administrator/adaugareIntrebarePtChestionar.html";
                        } else {
                            model.addAttribute("titlu", "Eroare permisiune");
                            model.addAttribute("mesaj", "Nu aveti permisunea de modificare a unui chestionar pentru care nu sunteti autor.");
                            return "htmlfiles/general/invalid.html";
                        }
                    } else {
                        model.addAttribute("titlu", "Eroare chestionar");
                        model.addAttribute("mesaj", "Chestionar inexistent.");
                        return "htmlfiles/general/invalid.html";
                    }
                } else {
                    model.addAttribute("titlu", "Eroare nume de utilizator");
                    model.addAttribute("mesaj", "Nume de utilizator inexistent.");
                    return "htmlfiles/general/invalid.html";
                }
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

    @PostMapping(value = "/intrebare", consumes = {"application/x-www-form-urlencoded;charset=UTF-8"})
    public String adaugareIntrebare(@RequestParam("continut") String continut,
                                    @RequestParam("numarDePuncte") Integer numarDePuncte,
                                    @RequestParam("chestionarId") Long chestionarId,
                                    Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            boolean hasAdministratorRole = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_" + Rol.ADMINISTRATOR.toString()));
            String currentUserName = authentication.getName();
            if (hasAdministratorRole) {
                Optional<Utilizator> utilizatorOptional = serviceUtilizator.findUtilizatorByNumeDeUtilizator(currentUserName);
                if (utilizatorOptional.isPresent()) {
                    Utilizator administrator = utilizatorOptional.get();
                    Optional<Chestionar> chestionarOptional = serviceChestionar.findById(chestionarId);
                    if (chestionarOptional.isPresent()) {
                        Chestionar chestionar = chestionarOptional.get();
                        if (administrator.getUtilizatorId().equals(chestionar.getUtilizatorCreator().getUtilizatorId())) {
                            boolean existaChestionarEfectuat = serviceChestionarEfectSiIntrebareEfect
                                    .existsChestionarEfectuatByChestionar_ChestionarId(chestionarId);
                            if (!existaChestionarEfectuat) {
                                Intrebare intrebare = new Intrebare(
                                        0L,
                                        continut,
                                        numarDePuncte,
                                        false,
                                        chestionar,
                                        new HashSet<>());
                                serviceIntrebare.save(chestionar, intrebare);
                                serviceChestionar.setFinalizare(chestionar, false);

                                model.addAttribute("deschideModal", false);
                                model.addAttribute("succes", true);
                                model.addAttribute("chestionar", chestionar);
                                model.addAttribute("listaDeChestionareAdministratorCurent", serviceChestionar
                                        .findAllByUtilizatorCreator_NumeDeUtilizator(currentUserName));
                            }else {
                                Chestionar chestionarNou = CloneUtils.clone(chestionar, false);
                                Chestionar chestionarNouReturnat = serviceChestionar.save(chestionarNou);
                                serviceChestionar.update(chestionarNouReturnat, "Chestionar" + chestionarNouReturnat.getChestionarId());
                                List<Intrebare> intrebariChestionarVechi = serviceIntrebare
                                        .findAllByChestionar_ChestionarId(chestionar.getChestionarId());
                                List<Intrebare> intrebariChestionarNou = CloneUtils.clone(
                                        intrebariChestionarVechi,
                                        chestionarNouReturnat);
                                Intrebare intrebare = new Intrebare(
                                        0L,
                                        continut,
                                        numarDePuncte,
                                        false,
                                        chestionarNouReturnat,
                                        new HashSet<>());
                                intrebariChestionarNou.add(intrebare);
                                List<Intrebare> intrebariReturnateChestionarNou = serviceIntrebare.saveAll(intrebariChestionarNou);
                                serviceChestionar.verificaFinalizare(chestionarNouReturnat);

                                model.addAttribute("deschideModal", true);
                                model.addAttribute("titluModal", "Creare chestionar");
                                model.addAttribute("mesajPentruModalBody", "A fost creat un chestionar nou.");
                                model.addAttribute("succes", true);
                                model.addAttribute("chestionar", chestionarNouReturnat);
                                model.addAttribute("listaDeChestionareAdministratorCurent", serviceChestionar
                                        .findAllByUtilizatorCreator_NumeDeUtilizator(currentUserName));
                            }

                            return "htmlfiles/administrator/adaugareIntrebarePtChestionar.html";
                        } else {
                            model.addAttribute("titlu", "Eroare permisiune");
                            model.addAttribute("mesaj", "Nu aveti permisunea de modificare a unui chestionar pentru care nu sunteti autor.");
                            return "htmlfiles/general/invalid.html";
                        }
                    } else {
                        model.addAttribute("titlu", "Eroare chestionar");
                        model.addAttribute("mesaj", "Chestionar inexistent.");
                        return "htmlfiles/general/invalid.html";
                    }
                } else {
                    model.addAttribute("titlu", "Eroare nume de utilizator");
                    model.addAttribute("mesaj", "Nume de utilizator inexistent.");
                    return "htmlfiles/general/invalid.html";
                }
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

    @PostMapping("/sterge-intrebare/{intrebareId}")
    public String stergereIntrebare(@PathVariable Long intrebareId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            boolean hasAdministratorRole = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_" + Rol.ADMINISTRATOR.toString()));
            String currentUserName = authentication.getName();
            if (hasAdministratorRole) {
                Optional<Utilizator> utilizatorOptional = serviceUtilizator.findUtilizatorByNumeDeUtilizator(currentUserName);
                if (utilizatorOptional.isPresent()) {
                    Utilizator administrator = utilizatorOptional.get();
                    Optional<Intrebare> intrebareOptional = serviceIntrebare.findById(intrebareId);
                    if (intrebareOptional.isPresent()) {

                        Intrebare intrebare = intrebareOptional.get();
                        Chestionar chestionar = intrebare.getChestionar();

                        if (administrator.getUtilizatorId().equals(chestionar.getUtilizatorCreator().getUtilizatorId())) {
                            boolean existaIntrebareEfectuata = serviceChestionarEfectSiIntrebareEfect
                                    .existsIntrebareEfectuataByIntrebare_IntrebareId(intrebareId);

                            if (!existaIntrebareEfectuata) {
                                serviceIntrebare.delete(chestionar, intrebare);
                                serviceChestionar.verificaFinalizare(chestionar);

                                model.addAttribute("deschideModal", false);
                                model.addAttribute("succes", true);
                                model.addAttribute("mesajSucces", "Intrebarea a fost stearsa.");
                                model.addAttribute("autor", true);
                                model.addAttribute("chestionar", chestionar);
                                List<Intrebare> intrebariChestionar = serviceIntrebare.findAllByChestionar_ChestionarId(chestionar.getChestionarId());
                                List<IntrebareDTO> intrebariChestionarDTO = mapperIntrebare.intrebariToIntrebariDTO(intrebariChestionar);
                                model.addAttribute("intrebariChestionar", intrebariChestionarDTO);
                            } else {
                                Chestionar chestionarNou = CloneUtils.clone(chestionar, true);
                                Chestionar chestionarNouReturnat = serviceChestionar.save(chestionarNou);
                                serviceChestionar.update(chestionarNouReturnat, "Chestionar" + chestionarNouReturnat.getChestionarId());
                                List<Intrebare> intrebariChestionarVechi = serviceIntrebare
                                        .findAllByChestionar_ChestionarId(chestionar.getChestionarId());
                                List<Intrebare> intrebariChestionarNou = CloneUtils.cloneCuIntrebareStearsa(
                                        intrebariChestionarVechi,
                                        chestionarNouReturnat,
                                        intrebareId);
                                List<Intrebare> intrebariReturnateChestionarNou = serviceIntrebare.saveAll(intrebariChestionarNou);
                                serviceChestionar.verificaFinalizare(chestionarNouReturnat);

                                model.addAttribute("deschideModal", true);
                                model.addAttribute("titluModal", "Creare chestionar");
                                model.addAttribute("mesajPentruModalBody", "A fost creat un chestionar nou.");
                                model.addAttribute("succes", true);
                                model.addAttribute("mesajSucces", "Intrebarea a fost stearsa.");
                                model.addAttribute("autor", true);
                                model.addAttribute("chestionar", chestionarNouReturnat);
                                List<IntrebareDTO> intrebariChestionarDTO = mapperIntrebare.intrebariToIntrebariDTO(intrebariReturnateChestionarNou);
                                model.addAttribute("intrebariChestionar", intrebariChestionarDTO);
                            }

                            return "htmlfiles/administrator/afisareChestionar.html";
                        } else {
                            model.addAttribute("titlu", "Eroare permisiune");
                            model.addAttribute("mesaj", "Nu aveti permisunea de stergere a unei intrebari al unui chestionar pentru care nu sunteti autor.");
                            return "htmlfiles/general/invalid.html";
                        }
                    } else {
                        model.addAttribute("titlu", "Eroare intrebare");
                        model.addAttribute("mesaj", "Intrebare inexistenta.");
                        return "htmlfiles/general/invalid.html";
                    }
                } else {
                    model.addAttribute("titlu", "Eroare nume de utilizator");
                    model.addAttribute("mesaj", "Nume de utilizator inexistent.");
                    return "htmlfiles/general/invalid.html";
                }
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
}
