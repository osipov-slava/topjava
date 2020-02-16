package ru.javawebinar.topjava.web.meal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    
    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal save(Meal meal){
        return service.save(authUserId(), meal);
    }

    public boolean delete(int id){
        return service.delete(authUserId(),id);
    }

    public Meal get(int id){
        return service.get(authUserId(),id);
    }

    public Collection<MealTo> getAll(){
        return MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public Collection<MealTo> getAllBetweenDates(HttpServletRequest request){
        LocalDate startDate;
        try {
            startDate = LocalDate.parse(request.getParameter("startDate"));
        } catch (Exception e) {
            startDate = LocalDate.MIN;
        }
        LocalDate endDate;
        try {
            endDate = LocalDate.parse(request.getParameter("endDate"));
        } catch (Exception e) {
            endDate = LocalDate.MAX;
        }
        LocalTime startTime;
        try {
            startTime = LocalTime.parse(request.getParameter("startTime"));
        } catch (Exception e) {
            startTime = LocalTime.MIN;
        }
        LocalTime endTime;
        try {
            endTime = LocalTime.parse(request.getParameter("endTime"));
        } catch (Exception e) {
            endTime = LocalTime.MAX;
        }

        return MealsUtil.getFilteredTos(service.getAllBetweenDates(authUserId(), startDate, endDate), authUserCaloriesPerDay(), startTime, endTime);
    }
}