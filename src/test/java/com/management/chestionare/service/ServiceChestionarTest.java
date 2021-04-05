package com.management.chestionare.service;

import com.management.chestionare.domain.Chestionar;
import com.management.chestionare.domain.Utilizator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@Transactional
class ServiceChestionarTest {
    @Autowired
    private ServiceChestionar serviceChestionar;

    @Autowired
    private ServiceUtilizator serviceUtilizator;

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
        assertThat(serviceChestionar).isNotNull();
        assertThat(serviceUtilizator).isNotNull();
    }

    @Test
    @Order(2)
    @DisplayName("findAll")
    void findAll() {
        List<Chestionar> chestionare = serviceChestionar.findAll();
        assertThat(chestionare.size()).isEqualTo(5);

        Optional<Utilizator> utilizatorOptional = serviceUtilizator.findUtilizatorByNumeDeUtilizator("admin");
        if (utilizatorOptional.isPresent()) {
            Utilizator administrator = utilizatorOptional.get();

            Chestionar chestionar1 = new Chestionar(
                    1L,
                    "Chestionar1",
                    3,
                    true,
                    administrator);
            assertThat(chestionare).usingFieldByFieldElementComparator().contains(chestionar1);

            Chestionar chestionar2 = new Chestionar(
                    2L,
                    "Chestionar2",
                    5,
                    true,
                    administrator);
            assertThat(chestionare).usingFieldByFieldElementComparator().contains(chestionar2);

            Chestionar chestionar3 = new Chestionar(
                    3L,
                    "Chestionar3",
                    3,
                    true,
                    administrator);
            assertThat(chestionare).usingFieldByFieldElementComparator().contains(chestionar3);

            Chestionar chestionar4 = new Chestionar(
                    4L,
                    "Chestionar4",
                    5,
                    true,
                    administrator);
            assertThat(chestionare).usingFieldByFieldElementComparator().contains(chestionar4);

            Chestionar chestionar5 = new Chestionar(
                    5L,
                    "Chestionar5",
                    3,
                    true,
                    administrator);
            assertThat(chestionare).usingFieldByFieldElementComparator().contains(chestionar5);
        } else {
            fail("Nu exista administrator.");
        }
    }

    @Test
    @Order(3)
    @DisplayName("findAllByUtilizatorCreator_NumeDeUtilizator")
    void findAllByUtilizatorCreator_NumeDeUtilizator() {
        List<Chestionar> chestionare = serviceChestionar
                .findAllByUtilizatorCreator_NumeDeUtilizator("admin");
        assertThat(chestionare.size()).isEqualTo(5);
    }

    @Test
    @Order(4)
    @DisplayName("findAllAscByIdByUtilizatorCreator_NumeDeUtilizator")
    void findAllAscByIdByUtilizatorCreator_NumeDeUtilizator() {
        List<Chestionar> chestionare = serviceChestionar
                .findAllByUtilizatorCreator_NumeDeUtilizator("admin");
        assertThat(chestionare.size()).isEqualTo(5);
    }

    @Test
    @Order(5)
    @DisplayName("findById")
    void findById() {
        Optional<Utilizator> utilizatorOptional = serviceUtilizator.findUtilizatorByNumeDeUtilizator("admin");
        if (utilizatorOptional.isPresent()) {
            Utilizator administrator = utilizatorOptional.get();

            Optional<Chestionar> optionalDeChestionar1 = serviceChestionar.findById(1L);
            assertThat(optionalDeChestionar1).isNotEmpty();
            Chestionar chestionarReturnat1 = optionalDeChestionar1.get();
            Chestionar chestionar1 = new Chestionar(
                    1L,
                    "Chestionar1",
                    3,
                    true,
                    administrator);
            assertThat(chestionarReturnat1).usingRecursiveComparison().isEqualTo(chestionar1);

            Optional<Chestionar> optionalDeChestionar2 = serviceChestionar.findById(2L);
            assertThat(optionalDeChestionar2).isNotEmpty();
            Chestionar chestionarReturnat2 = optionalDeChestionar2.get();
            Chestionar chestionar2 = new Chestionar(
                    2L,
                    "Chestionar2",
                    5,
                    true,
                    administrator);
            assertThat(chestionarReturnat2).usingRecursiveComparison().isEqualTo(chestionar2);

            Optional<Chestionar> optionalDeChestionar3 = serviceChestionar.findById(3L);
            assertThat(optionalDeChestionar3).isNotEmpty();
            Chestionar chestionarReturnat3 = optionalDeChestionar3.get();
            Chestionar chestionar3 = new Chestionar(
                    3L,
                    "Chestionar3",
                    3,
                    true,
                    administrator);
            assertThat(chestionarReturnat3).usingRecursiveComparison().isEqualTo(chestionar3);
        } else {
            fail("Nu exista administrator.");
        }
    }

    @Test
    @Order(6)
    @DisplayName("save")
    void save() {
        Optional<Utilizator> utilizatorOptional = serviceUtilizator.findUtilizatorByNumeDeUtilizator("admin");
        if (utilizatorOptional.isPresent()) {
            Utilizator administrator = utilizatorOptional.get();
            Chestionar chestionar = new Chestionar(
                    0L,
                    "Chestionar6",
                    0,
                    false,
                    administrator
            );
            serviceChestionar.save(chestionar);

            Chestionar chestionarAsteptat = new Chestionar(
                    6L,
                    "Chestionar6",
                    0,
                    false,
                    administrator
            );

            Optional<Chestionar> optionalDeChestionar1 = serviceChestionar.findById(6L);
            assertThat(optionalDeChestionar1).isNotEmpty();
            Chestionar chestionarReturnat1 = optionalDeChestionar1.get();
            assertThat(chestionarReturnat1).usingRecursiveComparison().isEqualTo(chestionarAsteptat);
        }
    }

    @Test
    @Order(7)
    @DisplayName("update")
    void update() {
        Optional<Chestionar> chestionarOptional = serviceChestionar.findById(5L);
        if (chestionarOptional.isPresent()) {
            Chestionar chestionar = chestionarOptional.get();
            serviceChestionar.update(chestionar, "Chestionar5 modificat");
        } else {
            fail("Nu exista chestionarul.");
        }
    }

    @Test
    @Order(8)
    @DisplayName("delete")
    void delete() {
        Optional<Chestionar> chestionarOptional = serviceChestionar.findById(5L);
        if (chestionarOptional.isPresent()) {
            Chestionar chestionar = chestionarOptional.get();
            serviceChestionar.delete(chestionar);
        } else {
            fail("Nu exista chestionarul.");
        }
    }

    @Test
    @Order(9)
    @DisplayName("verificaFinalizare")
    void verificaFinalizare() {
    }

    @Test
    @Order(10)
    @DisplayName("setFinalizare")
    void setFinalizare() {
    }
}