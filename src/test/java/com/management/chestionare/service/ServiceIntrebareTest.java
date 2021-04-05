package com.management.chestionare.service;

import com.management.chestionare.domain.Chestionar;
import com.management.chestionare.domain.Intrebare;
import com.management.chestionare.domain.Rol;
import com.management.chestionare.domain.Utilizator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@Transactional
class ServiceIntrebareTest {
    @Autowired
    private ServiceIntrebare serviceIntrebare;

    @Autowired
    private ServiceChestionar serviceChestionar;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    @DisplayName("contextLoads")
    public void contextLoads() throws Exception {
        assertThat(serviceIntrebare).isNotNull();
    }

    @Test
    @Order(2)
    @DisplayName("findAll")
    void findAll() {
        List<Intrebare> intrebari = serviceIntrebare.findAll();
        assertThat(intrebari.size()).isEqualTo(19);

        List<Chestionar> chestionare = new ArrayList<>(5);
        Utilizator utilizatorImplicit = new Utilizator(0L, "numePrenume0", Rol.ADMINISTRATOR, "numeDeUtilizator0", "parola0");
        Chestionar chestionarImplicit = new Chestionar(0L, "descriere0", 0,true, utilizatorImplicit);
        chestionare.add(serviceChestionar.findById((long) 1).orElse(chestionarImplicit));
        chestionare.add(serviceChestionar.findById((long) 2).orElse(chestionarImplicit));
        chestionare.add(serviceChestionar.findById((long) 3).orElse(chestionarImplicit));
        chestionare.add(serviceChestionar.findById((long) 4).orElse(chestionarImplicit));
        chestionare.add(serviceChestionar.findById((long) 5).orElse(chestionarImplicit));

        assertThat(chestionare).usingFieldByFieldElementComparator().doesNotContain(chestionarImplicit);
    }

    @Test
    @Order(3)
    @DisplayName("findAllByChestionar_ChestionarId")
    void findAllByChestionar_ChestionarId() {
    }

    @Test
    @Order(4)
    @DisplayName("findAllByChestionar_UtilizatorCreator_NumeDeUtilizator")
    void findAllByChestionar_UtilizatorCreator_NumeDeUtilizator() {
    }

    @Test
    @Order(5)
    @DisplayName("findById")
    void findById() {
    }

    @Test
    @Order(6)
    @DisplayName("saveAll")
    void saveAll() {
    }

    @Test
    @Order(7)
    @DisplayName("save")
    void save() {
    }

    @Test
    @Order(8)
    @DisplayName("delete")
    void delete() {
    }

    @Test
    @Order(9)
    @DisplayName("verificaFinalizare")
    void verificaFinalizare() {
    }
}