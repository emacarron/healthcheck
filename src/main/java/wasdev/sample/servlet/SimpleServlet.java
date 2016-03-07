package wasdev.sample.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet("/test")
public class SimpleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        
        Connection conn = null;
        try {
          if ("true".equals(request.getParameter("fail"))) {
            throw new RuntimeException("Forced fail");
          }
          InitialContext ic = new InitialContext();
          DataSource ds = (DataSource) ic.lookup("jdbc/ElephantSQL-hc");
          conn = ds.getConnection();
          Statement stmt = conn.createStatement();
          stmt.executeQuery("SELECT 1");
          response.getWriter().println("OK");
        } catch (Exception e) {
          response.setStatus(500);
          response.getWriter().println("KO");
          response.getWriter().println();
          e.printStackTrace(response.getWriter());
        } finally {
          if (conn != null) {
            try {
              conn.close();
            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        }
    }

}
