package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.util.PSQLException;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID_START, USER_ID);
        assertThat(meal).isEqualToComparingFieldByField(MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void getOther() {
        Meal meal = service.get(MEAL_ID_START, ADMIN_ID);
        assertThat(meal).isEqualToComparingFieldByField(MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        service.delete(MEAL_ID_START, USER_ID);
        service.get(MEAL_ID_START, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteOther() {
        service.delete(MEAL_ID_START, ADMIN_ID);
    }

    @Test
    public void getBetweenHalfOpen() {
        List<Meal> all = service.getBetweenHalfOpen(LocalDate.of(2020, Month.JANUARY, 31),
                LocalDate.of(2020, Month.JANUARY, 31), USER_ID);
        assertMatch(all, MEAL7, MEAL6, MEAL5, MEAL4);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, MEAL7, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateOther() {
        Meal updated = getUpdated();
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test (expected = DuplicateKeyException.class)
    public void createExistDateTime() {
        Meal newMeal = new Meal(MEAL1);
        newMeal.setId(null);
        Meal created = service.create(newMeal, USER_ID);
        Assert.assertEquals(7, service.getAll(USER_ID).size());
        System.out.println();
    }
}