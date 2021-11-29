package org.project.api;

import org.project.dao.AccountDao;
import org.project.entity.Account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/api/login"})
public class LoginApi extends HttpServlet {
    private static final AccountDao accountDao = new AccountDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String role = req.getParameter("role");

        Account account = accountDao.login(username, password, Integer.parseInt(role));

        PrintWriter printWriter = resp.getWriter();
        if (account == null){
            printWriter.write("Incorrect username or password!");
            printWriter.close();
        } else {
            HttpSession session = req.getSession();
            session.setAttribute("account", account);
            resp.sendRedirect("home");
        }


    }
}
