package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.DataRepository;
import com.example.servingwebcontent.repository.Data;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SQLIController {
    DataRepository dataRepository;

    @RequestMapping("/sqli")
    public String sqlInjection(@RequestParam String sqli, Model model, HttpServletRequest request, HttpServletResponse response) {
        String hql = "FROM Data WHERE name = '" + sqli + "'";

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Data.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        //create the query
        Query query = session.createQuery(hql);
        List<Data> dataList = query.getResultList();

        System.out.println(dataList);
        model.addAttribute("dataList", dataList);

        session.close();
        return "sql_injection";
    }

}
