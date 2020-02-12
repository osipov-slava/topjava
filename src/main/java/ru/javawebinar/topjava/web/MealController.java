package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealDaoImpl;
import ru.javawebinar.topjava.util.MealsMemory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class MealController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(MealController.class);
    private static String INSERT_OR_EDIT = "/addMeal.jsp";
    private static String LIST_Meal = "/listMeal.jsp";
    private MealDaoImpl dao;

    public MealController() {
        super();
        dao = MealsMemory.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String forward;
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            log.debug("delete");
            int MealId = Integer.parseInt(request.getParameter("mealId"));
            dao.delete(MealId);
            forward = LIST_Meal;
            request.setAttribute("Meals", dao.getAll());
        } else if (action.equalsIgnoreCase("edit")) {
            log.debug("edit");
            forward = INSERT_OR_EDIT;
            int MealId = Integer.parseInt(request.getParameter("mealId"));
            Meal Meal = dao.getById(MealId);
            request.setAttribute("Meal", Meal);
        } else if (action.equalsIgnoreCase("listMeal")) {
            log.debug("list all");
            forward = LIST_Meal;
            request.setAttribute("Meals", dao.getAll());
        } else {
            log.debug("create");
            request.setAttribute("Meal", new Meal(0, null, null, 0));
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("POST");
        request.setCharacterEncoding("UTF-8");
        LocalDateTime dob = LocalDateTime.parse(request.getParameter("dateTime"));
        int calories = Integer.parseInt(request.getParameter("calories"));
        String mealId = request.getParameter("id");
        if (mealId == null || mealId.isEmpty() || mealId.equals("0")) {
            dao.add(new Meal(0, dob, request.getParameter("description"), calories));
        } else {
            dao.update(new Meal(Integer.parseInt(mealId), dob, request.getParameter("description"), calories));
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_Meal);
        request.setAttribute("Meals", dao.getAll());
        view.forward(request, response);
    }
}

