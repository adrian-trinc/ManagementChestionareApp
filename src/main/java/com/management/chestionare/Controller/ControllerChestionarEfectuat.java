package com.management.chestionare.Controller;

import com.management.chestionare.domain.*;
import com.management.chestionare.dtodomain.ChestionarEfectuatDTO;
import com.management.chestionare.dtodomain.IntrebareEfectuataDTO;
import com.management.chestionare.mapper.MapperChestionarEfectuat;
import com.management.chestionare.mapper.MapperIntrebareEfectuata;
import com.management.chestionare.service.ServiceChestionarEfectSiIntrebareEfect;
import com.management.chestionare.service.ServiceUtilizator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class ControllerChestionarEfectuat {
    private final ServiceChestionarEfectSiIntrebareEfect serviceChestionarEfectSiIntrebareEfect;
    private final ServiceUtilizator serviceUtilizator;
    private final MapperChestionarEfectuat mapperChestionarEfectuat;
    private final MapperIntrebareEfectuata mapperIntrebareEfectuata;

    @Autowired
    public ControllerChestionarEfectuat(ServiceChestionarEfectSiIntrebareEfect serviceChestionarEfectSiIntrebareEfect, ServiceUtilizator serviceUtilizator, MapperChestionarEfectuat mapperChestionarEfectuat, MapperIntrebareEfectuata mapperIntrebareEfectuata) {
        this.serviceChestionarEfectSiIntrebareEfect = serviceChestionarEfectSiIntrebareEfect;
        this.serviceUtilizator = serviceUtilizator;
        this.mapperChestionarEfectuat = mapperChestionarEfectuat;
        this.mapperIntrebareEfectuata = mapperIntrebareEfectuata;
    }

    @GetMapping("/chestionarEfectuat-pentruUtilizatorObisnuit/{chestionarEfectuatId}")
    public String afisareChestionarPentruUtilizatorObisnuit(
            @PathVariable Long chestionarEfectuatId,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            boolean hasUserRole = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_" + Rol.UTILIZATOR_OBISNUIT.toString()));
            String currentUserName = authentication.getName();
            if (hasUserRole) {
                Optional<Utilizator> utilizatorOptional = serviceUtilizator.findUtilizatorByNumeDeUtilizator(currentUserName);
                if (utilizatorOptional.isPresent()) {
                    Utilizator utilizatorObisnuit = utilizatorOptional.get();
                    Optional<ChestionarEfectuat> chestionarEfectuatOptional = serviceChestionarEfectSiIntrebareEfect.findById(chestionarEfectuatId);
                    if (chestionarEfectuatOptional.isPresent()) {
                        ChestionarEfectuat chestionarEfectuat = chestionarEfectuatOptional.get();
                        ChestionarEfectuatDTO chestionarEfectuatDTO = this.mapperChestionarEfectuat
                                .chestionarEfectuatToChestionarEfectuatDTO(chestionarEfectuat);
                        model.addAttribute("chestionarEfectuat", chestionarEfectuatDTO);
                        List<IntrebareEfectuata> intrebariEfectuateChestionar = serviceChestionarEfectSiIntrebareEfect
                                .findAllByChestionarEfectuat_ChestionarEfectuatId(chestionarEfectuatId);
                        List<IntrebareEfectuataDTO> intrebariEfectuateChestionarDTO = mapperIntrebareEfectuata
                                .intrebariEfectuateToIntrebariEfectuateDTO(intrebariEfectuateChestionar);
                        model.addAttribute("intrebariEfectuateChestionar", intrebariEfectuateChestionarDTO);
                        return "htmlfiles/utilizatorObisnuit/vizualizareChestionarEfectuat.html";
                    } else {
                        model.addAttribute("titlu", "Eroare chestionar efectuat");
                        model.addAttribute("mesaj", "Chestionar efectuat inexistent.");
                        return "htmlfiles/general/invalid.html";
                    }
                } else {
                    model.addAttribute("titlu", "Eroare nume de utilizator");
                    model.addAttribute("mesaj", "Nume de utilizator inexistent.");
                    return "htmlfiles/general/invalid.html";
                }
            } else {
                model.addAttribute("titlu", "Eroare rol de utilizator obisnuit");
                model.addAttribute("mesaj", "Nu aveti rol de utilizator obisnuit.");
                return "htmlfiles/general/invalid.html";
            }
        } else {
            model.addAttribute("titlu", "Eroare server");
            model.addAttribute("mesaj", "Probleme cu SecurityContext.");
            return "htmlfiles/general/invalid.html";
        }
    }

    @PostMapping(value = "/rezolva-chestionar/{chestionarId}",
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8"})
    public String adaugareChestionar(@PathVariable Long chestionarId,
                                     @RequestParam MultiValueMap<String, String> paramMap,
                                     Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            boolean hasUserRole = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_" + Rol.UTILIZATOR_OBISNUIT.toString()));
            String currentUserName = authentication.getName();
            if (hasUserRole) {
                Optional<Utilizator> utilizatorOptional = serviceUtilizator.findUtilizatorByNumeDeUtilizator(currentUserName);
                if (utilizatorOptional.isPresent()) {
                    Utilizator utilizatorObisnuit = utilizatorOptional.get();

                    serviceChestionarEfectSiIntrebareEfect.save(
                            chestionarId,
                            "varianteIntrebare",
                            paramMap,
                            utilizatorObisnuit
                    );

                    model.addAttribute("succes", true);
                    model.addAttribute("mesaj", "Raspunsurile au fost trimise.");

                    return "htmlfiles/utilizatorObisnuit/chestionarRezolvatPaginaDeConfirmare.html";
                } else {
                    model.addAttribute("titlu", "Eroare nume de utilizator");
                    model.addAttribute("mesaj", "Nume de utilizator inexistent.");
                    return "htmlfiles/general/invalid.html";
                }
            } else {
                model.addAttribute("titlu", "Eroare rol de utilizator obisnuit");
                model.addAttribute("mesaj", "Nu aveti rol de utilizator obisnuit.");
                return "htmlfiles/general/invalid.html";
            }
        } else {
            model.addAttribute("titlu", "Eroare server");
            model.addAttribute("mesaj", "Probleme cu SecurityContext.");
            return "htmlfiles/general/invalid.html";
        }
    }
}
