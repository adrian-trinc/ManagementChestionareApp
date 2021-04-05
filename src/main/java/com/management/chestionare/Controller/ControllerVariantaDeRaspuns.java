package com.management.chestionare.Controller;

import com.management.chestionare.domain.*;
import com.management.chestionare.dtodomain.IntrebareDTO;
import com.management.chestionare.mapper.MapperIntrebare;
import com.management.chestionare.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class ControllerVariantaDeRaspuns {
    private final ServiceVariantaDeRaspuns serviceVariantaDeRaspuns;
    private final ServiceIntrebare serviceIntrebare;
    private final ServiceChestionar serviceChestionar;
    private final ServiceUtilizator serviceUtilizator;
    private final ServiceChestionarEfectSiIntrebareEfect serviceChestionarEfectSiIntrebareEfect;
    private final MapperIntrebare mapperIntrebare;

    @Autowired
    public ControllerVariantaDeRaspuns(ServiceVariantaDeRaspuns serviceVariantaDeRaspuns, ServiceIntrebare serviceIntrebare, ServiceChestionar serviceChestionar, ServiceUtilizator serviceUtilizator, ServiceChestionarEfectSiIntrebareEfect serviceChestionarEfectSiIntrebareEfect, MapperIntrebare mapperIntrebare) {
        this.serviceVariantaDeRaspuns = serviceVariantaDeRaspuns;
        this.serviceIntrebare = serviceIntrebare;
        this.serviceChestionar = serviceChestionar;
        this.serviceUtilizator = serviceUtilizator;
        this.serviceChestionarEfectSiIntrebareEfect = serviceChestionarEfectSiIntrebareEfect;
        this.mapperIntrebare = mapperIntrebare;
    }

    @PostMapping(value = "/variantaDeRaspuns", consumes = {"application/x-www-form-urlencoded;charset=UTF-8"})
    public String adaugareVariantaDeRaspuns(@RequestParam("continut") String continut,
                                            @RequestParam("variantaCorecta") Boolean variantaCorecta,
                                            @RequestParam("intrebareId") Long intrebareId,
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
                    Optional<Intrebare> intrebareOptionala = serviceIntrebare.findById(intrebareId);
                    if (intrebareOptionala.isPresent()) {
                        Intrebare intrebare = intrebareOptionala.get();
                        if (administrator.getUtilizatorId().equals(intrebare.getChestionar().getUtilizatorCreator().getUtilizatorId())) {
                            VariantaDeRaspuns variantaDeRaspuns = new VariantaDeRaspuns(
                                    0L,
                                    continut,
                                    variantaCorecta,
                                    intrebare);
                            try {
                                serviceVariantaDeRaspuns.save(intrebare, variantaDeRaspuns);
                                serviceIntrebare.verificaFinalizare(intrebare);
                                serviceChestionar.verificaFinalizare(intrebare.getChestionar());
                                model.addAttribute("succes", true);
                            } catch (ServiceException e) {
                                model.addAttribute("succes", false);
                            }
                            model.addAttribute("intrebareId", intrebareId);
                            return "htmlfiles/administrator/adaugareVariantaDeRaspunsPtIntrebare.html";
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

    @PostMapping("/sterge-variantaDeRaspuns/{variraspunsId}")
    public String stergereVariantaDeRaspuns(@PathVariable Long variraspunsId, Model model) {
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
                    Optional<VariantaDeRaspuns> variantaDeRaspunsOptional = serviceVariantaDeRaspuns.findById(variraspunsId);
                    if (variantaDeRaspunsOptional.isPresent()) {

                        VariantaDeRaspuns variantaDeRaspuns = variantaDeRaspunsOptional.get();
                        Intrebare intrebare = variantaDeRaspuns.getIntrebare();
                        Chestionar chestionar = intrebare.getChestionar();
                        Utilizator utilizatorCreator = chestionar.getUtilizatorCreator();

                        if (administrator.getUtilizatorId().equals(utilizatorCreator.getUtilizatorId())) {
                            boolean existaIntrebareEfectuata = serviceChestionarEfectSiIntrebareEfect
                                    .existsIntrebareEfectuataByIntrebare_IntrebareId(intrebare.getIntrebareId());

                            if (!existaIntrebareEfectuata) {
                                serviceVariantaDeRaspuns.delete(intrebare, variantaDeRaspuns);
                                serviceIntrebare.verificaFinalizare(intrebare);
                                serviceChestionar.verificaFinalizare(chestionar);

                                model.addAttribute("deschideModal", false);
                                model.addAttribute("succes", true);
                                model.addAttribute("mesajSucces", "Varianta de raspuns a fost stearsa.");
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
                                List<Intrebare> intrebariChestionarNou = CloneUtils.cloneCuVariantaDeRaspunsStearsa(
                                        intrebariChestionarVechi,
                                        chestionarNouReturnat,
                                        intrebare.getIntrebareId(),
                                        variraspunsId);
                                List<Intrebare> intrebariReturnateChestionarNou = serviceIntrebare.saveAll(intrebariChestionarNou);

                                Optional<Intrebare> intrebareNouaOptional = intrebariReturnateChestionarNou
                                        .stream()
                                        .filter(intrebareIter -> intrebareIter.getIntrebareId().equals(intrebare.getIntrebareId()))
                                        .findFirst();
                                if (intrebareNouaOptional.isPresent()) {
                                    Intrebare intrebareNoua = intrebareNouaOptional.get();
                                    serviceIntrebare.verificaFinalizare(intrebareNoua);
                                    serviceChestionar.verificaFinalizare(chestionarNouReturnat);
                                }

                                model.addAttribute("deschideModal", true);
                                model.addAttribute("titluModal", "Creare chestionar");
                                model.addAttribute("mesajPentruModalBody", "A fost creat un chestionar nou.");
                                model.addAttribute("succes", true);
                                model.addAttribute("mesajSucces", "Varianta de raspuns a fost stearsa.");
                                model.addAttribute("autor", true);
                                model.addAttribute("chestionar", chestionarNouReturnat);
                                List<IntrebareDTO> intrebariChestionarDTO = mapperIntrebare.intrebariToIntrebariDTO(intrebariReturnateChestionarNou);
                                model.addAttribute("intrebariChestionar", intrebariChestionarDTO);
                            }

                            return "htmlfiles/administrator/afisareChestionar.html";
                        } else {
                            model.addAttribute("titlu", "Eroare permisiune");
                            model.addAttribute("mesaj", "Nu aveti permisunea de stergere a unei variante de raspuns al unui chestionar pentru care nu sunteti autor.");
                            return "htmlfiles/general/invalid.html";
                        }
                    } else {
                        model.addAttribute("titlu", "Eroare varianta de raspuns");
                        model.addAttribute("mesaj", "Varianta de raspuns inexistenta.");
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
