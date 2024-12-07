package com.klef.jfsdexam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.Criteria;
import org.hibernate.criterion.Aggregations;

import java.util.List;

public class ClientDemo {
    public static void main(String[] args) {
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        SessionFactory sf = cfg.buildSessionFactory();
        Session session = sf.openSession();

        Transaction tx = session.beginTransaction();

        // Insert records
        insertProject(session, "Project Alpha", 12, 100000, "Alice");
        insertProject(session, "Project Beta", 8, 75000, "Bob");

        // Display inserted data
        retrieveAllProjects(session);

        // Perform aggregate functions
        performAggregates(session);

        tx.commit();
        session.close();
        sf.close();
    }

    private static void insertProject(Session session, String projectName, int duration, double budget, String teamLead) {
        Project project = new Project(projectName, duration, budget, teamLead);
        session.save(project);
        System.out.println("Inserted: " + projectName);
    }

    private static void retrieveAllProjects(Session session) {
        List<Project> projects = session.createQuery("from Project", Project.class).getResultList();
        for (Project project : projects) {
            System.out.println(
                "ID: " + project.getId() +
                ", Name: " + project.getProjectName() +
                ", Duration: " + project.getDuration() +
                ", Budget: " + project.getBudget() +
                ", Team Lead: " + project.getTeamLead()
            );
        }
    }

    private static void performAggregates(Session session) {
        Criteria criteria = session.createCriteria(Project.class);

        Double maxBudget = (Double) criteria.setProjection(org.hibernate.criterion.Projections.max("budget")).uniqueResult();
        Double minBudget = (Double) criteria.setProjection(org.hibernate.criterion.Projections.min("budget")).uniqueResult();
        Double sumBudget = (Double) criteria.setProjection(org.hibernate.criterion.Projections.sum("budget")).uniqueResult();
        Long count = (Long) criteria.setProjection(org.hibernate.criterion.Projections.rowCount()).uniqueResult();
        Double avgBudget = (Double) criteria.setProjection(org.hibernate.criterion.Projections.avg("budget")).uniqueResult();

        System.out.println("Max Budget: " + maxBudget);
        System.out.println("Min Budget: " + minBudget);
        System.out.println("Sum of Budgets: " + sumBudget);
        System.out.println("Total Number of Projects: " + count);
        System.out.println("Average Budget: " + avgBudget);
    }
}
